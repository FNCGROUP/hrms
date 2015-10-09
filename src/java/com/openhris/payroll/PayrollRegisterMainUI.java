/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISPopupDateField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.commons.reports.OpenHrisReports;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.model.PayrollRegister;
import com.openhris.payroll.reports.AdjustedPayrollRegisterReport;
import com.openhris.payroll.reports.AdvancesReport;
import com.openhris.payroll.reports.AllowancesReport;
import com.openhris.payroll.reports.AttendanceReport;
import com.openhris.payroll.reports.BankDebitMemoReport;
import com.openhris.payroll.reports.HDMFLoansPayableReport;
import com.openhris.payroll.reports.HDMFReport;
import com.openhris.payroll.reports.HDMFSavingsReport;
import com.openhris.payroll.reports.PHICReport;
import com.openhris.payroll.reports.PayrollRegisterReport;
import com.openhris.payroll.reports.PayslipReport;
import com.openhris.payroll.reports.SSSGeneralReport;
import com.openhris.payroll.reports.SSSLoansPayable;
import com.openhris.payroll.reports.SSSSbarroReport;
import com.openhris.payroll.reports.WitholdingTaxReport;
import com.openhris.service.PayrollService;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author jet
 */
public class PayrollRegisterMainUI extends VerticalLayout {
    
    OpenHrisUtilities util = new OpenHrisUtilities();    
    PayrollService payrollService = new PayrollServiceImpl();
    EmployeeService employeeService = new EmployeeServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    GetSQLConnection getConnection = new GetSQLConnection();
    
    int branchId;
    String employeeId;
    boolean lastAddedAdvanceType;
    String payrollDate;
    String payrollPeriod;
    int day = 0;
    
    Table payrollRegisterTbl = new PayrollRegisterTable();
    Table advancesTbl = new Table();
    Table adjustmentTbl = new Table();
    ComboBox employeesName = new ComboBox("Employees: ");
    
    public PayrollRegisterMainUI(final int branchId){
        this.branchId = branchId;
        
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);        
        
        final VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(15, Sizeable.UNITS_PERCENTAGE);
        
        GridLayout glayout = new GridLayout(4, 1);
        glayout.setMargin(true);
        glayout.setSpacing(true);
        glayout.setWidth("100%");
                
        final PopupDateField payrollDateField = new HRISPopupDateField("Payroll Date");
        glayout.addComponent(payrollDateField, 0, 0);
        
        Button payrollRegisterButton = new Button();
        if(!UserAccessControl.isPayroll()){
            payrollRegisterButton.setCaption("Payroll Register Button is Disabled");
            payrollRegisterButton.setEnabled(UserAccessControl.isPayroll());
        } else {
            payrollRegisterButton.setCaption("Generate Payroll Register");
            payrollRegisterButton.setEnabled(UserAccessControl.isPayroll());
        }
        payrollRegisterButton.setWidth("100%");
        payrollRegisterButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                payrollDate = util.convertDateFormat(payrollDateField.getValue().toString());
                payrollRegisterTable(getBranchId(), 
                        getPayrollDate(), 
                        false); 
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(util.parsingDate(util.convertDateFormat(payrollDateField.getValue().toString())));
		day = cal.get(Calendar.DAY_OF_MONTH);
            }
        });
        glayout.addComponent(payrollRegisterButton, 1, 0);
        glayout.setComponentAlignment(payrollRegisterButton, Alignment.BOTTOM_LEFT);
        
        Button adjustedPayrollRegisterBtn = new Button("Adjusted Payroll Register");
        adjustedPayrollRegisterBtn.setWidth("100%");
        adjustedPayrollRegisterBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                payrollDate = util.convertDateFormat(payrollDateField.getValue().toString());
                payrollRegisterTable(getBranchId(), 
                        getPayrollDate(), 
                        true); 
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(util.parsingDate(util.convertDateFormat(payrollDateField.getValue().toString())));
		day = cal.get(Calendar.DAY_OF_MONTH);
            }
        });
        glayout.addComponent(adjustedPayrollRegisterBtn);
        glayout.setComponentAlignment(adjustedPayrollRegisterBtn, Alignment.BOTTOM_CENTER);
        
	Button exportTableToExcel = new Button();
	if(!UserAccessControl.isPayroll()){
            exportTableToExcel.setCaption("Export Table to Excel is Disabled");
            exportTableToExcel.setEnabled(UserAccessControl.isPayroll());
        } else {
            exportTableToExcel.setCaption("Export Table to Excel");
            exportTableToExcel.setEnabled(UserAccessControl.isPayroll());
        }
	exportTableToExcel.setWidth("100%");
	exportTableToExcel.addListener(new Button.ClickListener() {

	    private static final long serialVersionUID = -73954695086117200L;
            private ExcelExport excelExport;
		
		@Override
		public void buttonClick(Button.ClickEvent event) {
			
			int tradeId = companyService.getTradeIdByBranchId(getBranchId());
			excelExport = new ExcelExport(payrollRegisterTbl, "Payroll Register");
			excelExport.excludeCollapsedColumns();
			excelExport.setReportTitle(companyService.getTradeById(tradeId).toUpperCase()+" Payroll Register "
                                    +new Label(companyService.getBranchById(getBranchId()), Label.CONTENT_PREFORMATTED));
			excelExport.setExportFileName(companyService.getTradeById(tradeId).toUpperCase()+"-Payroll Register");
			excelExport.export();
		}
	});
	glayout.addComponent(exportTableToExcel, 3, 0);
        glayout.setComponentAlignment(exportTableToExcel, Alignment.BOTTOM_LEFT);
	        
        vsplit.setFirstComponent(glayout);        
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
                      
        VerticalSplitPanel innerVsplit = new VerticalSplitPanel();        
        innerVsplit.addStyleName("small blue white");
        innerVsplit.setLocked(true);        
        innerVsplit.setSplitPosition(75, Sizeable.UNITS_PERCENTAGE);
        
        payrollRegisterTable(getBranchId(), getPayrollDate(), false); 
        innerVsplit.setFirstComponent(payrollRegisterTbl);
        
        GridLayout innerGlayout = new GridLayout(4, 1);
        innerGlayout.setWidth("100%");
        innerGlayout.setMargin(true);
        innerGlayout.setSpacing(true);
        
        final ComboBox reportType = dropDown.populatePayrollReportTypeList(new ComboBox());
        innerGlayout.addComponent(reportType, 0, 0);
        
        Button payrollReportButton = new Button("Generate Report");
        payrollReportButton.setWidth("100%");
        payrollReportButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {   
                OpenHrisReports reports = new OpenHrisReports(getBranchId(), util.convertDateFormat(payrollDateField.getValue().toString()));     
                
                if(payrollRegisterTbl.getPageLength() == 0){
                    getWindow().showNotification("No Data on table to be printed!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(payrollDateField.getValue() == null){
                    getWindow().showNotification("Payroll Date Required!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(reportType.getValue() == null){
                    getWindow().showNotification("Select a Report Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(reportType.getValue().equals("Payroll Register")){
                    String fileName = "payrollRegisterReport_";
                    reports.deleteFile(fileName);
                    Window reportWindow = new PayrollRegisterReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                }else if(reportType.getValue().equals("Adjusted Payroll Register")){
		    String fileName = "PayrollRegisterReportAdjusted_";
                    reports.deleteFile(fileName);
                    Window reportWindow = new AdjustedPayrollRegisterReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
		}else if(reportType.getValue().equals("Payslip Report")){
                    String fileName = "PayslipReport_";
                    reports.deleteFile(fileName);
                    PayslipReport payslipReport = new PayslipReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                }else if(reportType.getValue().equals("SSS Report")){
                    if(day == 15){
                        getWindow().showNotification("SSS Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }else{                        
			String fileName = "SssReport_";
                        reports.deleteFile(fileName);
                        SSSGeneralReport sssGeneralReport = new SSSGeneralReport(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                    }
                }else if(reportType.getValue().equals("SSS Report Sbarro")){
                    if(day == 15){
                        getWindow().showNotification("SSS Report Sbarro is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }else{                        
			String fileName = "SssReportSbarro_";
                        reports.deleteFile(fileName);
                        SSSSbarroReport sssSbarroReport = new SSSSbarroReport(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                    }
                }else if(reportType.getValue().equals("HDMF Report")){
                    if(day == 15){
                        String fileName = "HdmfReport_";
                        reports.deleteFile(fileName);
                        HDMFReport hdmfReport = new HDMFReport(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                    }else{
                        getWindow().showNotification("HDMF Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("HDMF Savings")){
                    if(day == 15){
                        String fileName = "HdmfVoluntarySavingsReport_";
                        reports.deleteFile(fileName);
                        HDMFSavingsReport hdmfSavingsReport = new HDMFSavingsReport(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                    } else {
                        getWindow().showNotification("HDMF Savings Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("HDMF Loans Payable")){
                    if(day == 15){
                        String fileName = "HdmfLoanReport_";
                        reports.deleteFile(fileName);
                        System.out.println("branchId: "+getBranchId());
                        HDMFLoansPayableReport hdmfLoansPayableReport = new HDMFLoansPayableReport(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                        
                    } else {
                        getWindow().showNotification("HDMF Loan Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("Philhealth Report")){
                    if(day == 15){
                        String fileName = "PhicReport_";
                        reports.deleteFile(fileName);
                        PHICReport phicReport = new PHICReport(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                    }else{
                        getWindow().showNotification("Philhealth Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("Witholding Tax")){
                    String fileName = "WitholdingTaxesReport_";
                    reports.deleteFile(fileName);
                    WitholdingTaxReport witholdingTaxReport = new WitholdingTaxReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                }else if(reportType.getValue().equals("Attendance Report")){
                    String fileName = "AttendanceReport_";
                    reports.deleteFile(fileName);
                    AttendanceReport attendanceReport = new AttendanceReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                }else if(reportType.getValue().equals("Bank Debit Memo")){
                    String fileName = "BankDebitMemo_";
                    reports.deleteFile(fileName);
                    BankDebitMemoReport bankDebitMemoReport = new BankDebitMemoReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                }else if(reportType.getValue().equals("SSS Loans Payable")){
                    if(day == 15){
                        getWindow().showNotification("SSS Loan Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }else{
                        String fileName = "SssLoanReport_";
                        reports.deleteFile(fileName);
                        SSSLoansPayable sssLoansPayable = new SSSLoansPayable(getBranchId(), 
                                util.convertDateFormat(payrollDateField.getValue().toString()), 
                                getApplication());
                    }
                }else if(reportType.getValue().equals("Advances")){
                    reports.deleteFile("AdvancesReport_");
//                    String corporate = companyService.getCorporateNameByBranchId(getBranchId());
                    AdvancesReport advancesReport = new AdvancesReport(getBranchId(), 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                } else if(reportType.getValue().equals("Allowances")){
                    reports.deleteFile("Allowances_");
                    String corporate = companyService.getCorporateNameByBranchId(getBranchId());
                    AllowancesReport allowancesReport = new AllowancesReport(corporate, 
                            util.convertDateFormat(payrollDateField.getValue().toString()), 
                            getApplication());
                }
            }
        });
        innerGlayout.addComponent(payrollReportButton, 1, 0);
        innerGlayout.setComponentAlignment(payrollReportButton, Alignment.BOTTOM_LEFT);
        
        Button viewFullScreenButton = new Button("View On Full Screen");
        viewFullScreenButton.setWidth("100%");
        viewFullScreenButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window sub = new ViewFullScreen(false, 
                        getBranchId(), 
                        getPayrollDate());
                sub.setModal(true);
                sub.setSizeFull();
                if(sub.getParent() == null){
                    getWindow().addWindow(sub);
                }
                sub.center();
            }
        });
        innerGlayout.addComponent(viewFullScreenButton, 2, 0);
        innerGlayout.setComponentAlignment(viewFullScreenButton, Alignment.BOTTOM_LEFT);
        
        innerVsplit.addComponent(innerGlayout);
        vsplit.setSecondComponent(innerVsplit); 
    }
    
    public Table payrollRegisterTable(int branchId, String payrollDate, boolean prev){
        payrollRegisterTbl.removeAllItems();        
//        List<PayrollRegister> payrollRegisterList = payrollService.getPayrollRegisterByBranch(branchId, payrollDate, prev);
        int i = 0;
        for(PayrollRegister pr : payrollService.getPayrollRegisterByBranch(branchId, payrollDate, prev)){
            payrollRegisterTbl.addItem(new Object[]{
                pr.getId(), 
		pr.getName().toUpperCase(), 
		pr.getNumOfDays(), 
		util.roundOffToTwoDecimalPlaces(pr.getRatePerDay()), 
		util.roundOffToTwoDecimalPlaces(pr.getBasicSalary()), 
                util.roundOffToTwoDecimalPlaces(pr.getHalfMonthSalary()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalOvertimePaid()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalLegalHolidayPaid()), 
                util.roundOffToTwoDecimalPlaces(pr.getTotalSpecialHolidayPaid()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalNightDifferentialPaid()), 
                util.roundOffToTwoDecimalPlaces(pr.getTotalWorkingDayOffPaid()), 
		util.roundOffToTwoDecimalPlaces(pr.getAbsences()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalLatesDeduction()), 
                util.roundOffToTwoDecimalPlaces(pr.getTotalUndertimeDeduction()), 
		util.roundOffToTwoDecimalPlaces(pr.getGrossPay()), 
		util.roundOffToTwoDecimalPlaces(pr.getSss()), 
		util.roundOffToTwoDecimalPlaces(pr.getPhic()), 
                util.roundOffToTwoDecimalPlaces(pr.getHdmf()), 
		util.roundOffToTwoDecimalPlaces(pr.getTax()), 
		util.roundOffToTwoDecimalPlaces(pr.getNetSalary()), 
		util.roundOffToTwoDecimalPlaces(pr.getCommunicationAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getPerDiemAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getColaAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getMealAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getTransportationAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getOtherAllowances()),  
                util.roundOffToTwoDecimalPlaces(pr.getAllowanceForLiquidation()),                  
		util.roundOffToTwoDecimalPlaces(pr.getAmount()), 
		util.roundOffToTwoDecimalPlaces(pr.getAdjustment()), 
                util.roundOffToTwoDecimalPlaces(pr.getAmountToBeReceive()), 
		util.roundOffToTwoDecimalPlaces(pr.getAmountReceivable()), 
		util.roundOffToTwoDecimalPlaces(pr.getForAdjustments())
            }, i);
            i++;
        }
        payrollRegisterTbl.setPageLength(payrollRegisterTbl.size());
        
        for(Object listener : payrollRegisterTbl.getListeners(ItemClickEvent.class)){
            payrollRegisterTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        payrollRegisterTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = payrollRegisterTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("adjustments")){
                    if(!UserAccessControl.isAdjustment()){
                        getWindow().showNotification("You are not allowed to add adjustments", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    int payrollId = Integer.parseInt(item.getItemProperty("id").toString());
                    double amountToBeReceive = util.convertStringToDouble(item.getItemProperty("amount to be receive").getValue().toString());
                    double amountReceive = util.convertStringToDouble(item.getItemProperty("amount received").getValue().toString());
                    double adjustments = util.convertStringToDouble(item.getItemProperty("adjustments").getValue().toString());
                            
                    Window subWindow = new AdjustmentWindow(payrollId, 
                            amountToBeReceive, 
                            amountReceive, 
                            adjustments);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                    
                    subWindow.addListener(adjustmentWindowListener);
                } 
                
            }
        });
        
        payrollRegisterTbl.setColumnCollapsed("amount received", true);
	payrollRegisterTbl.setColumnCollapsed("for adjustments", true);
        
        return payrollRegisterTbl;
    }
    
    public int getBranchId(){
        return branchId;
    }
    
    public void setBranchId(int branchId){
        this.branchId = branchId;
    }    
    
    String getPayrollDate(){
        return payrollDate;
    }
    
    Window.CloseListener adjustmentWindowListener = new Window.CloseListener() {

        @Override
        public void windowClose(Window.CloseEvent e) {
            payrollRegisterTable(getBranchId(), getPayrollDate(), false);
        }
    };
}
