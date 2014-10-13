/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.beans.AdvancesTypeBean;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.commons.reports.OpenHrisReports;
import com.openhris.company.serviceprovider.CompanyServiceImpl;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.company.service.CompanyService;
import com.openhris.employee.service.EmployeeService;
import com.openhris.payroll.service.PayrollService;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

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
    String payroll_date;
    String payrollPeriod;
    int day = 0;
    
    Table payrollRegisterTbl = new Table();
    Table advancesTbl = new Table();
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
                
        final PopupDateField payrollDate = new PopupDateField("Payroll Date");
        payrollDate.addStyleName("mydate");
        payrollDate.setValue(new Date());
        payrollDate.setWidth("100%");
        payrollDate.setDateFormat("EEE - MMM dd, yyyy");
        payrollDate.setLenient(true);
        payrollDate.setResolution(DateField.RESOLUTION_DAY);
        glayout.addComponent(payrollDate, 0, 0);
        
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
                payroll_date = util.convertDateFormat(payrollDate.getValue().toString());
                payrollRegisterTable(getBranchId(), 
                        payroll_date, 
                        false); 
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(util.parsingDate(util.convertDateFormat(payrollDate.getValue().toString())));
		day = cal.get(Calendar.DAY_OF_MONTH);
            }
        });
        glayout.addComponent(payrollRegisterButton, 1, 0);
        glayout.setComponentAlignment(payrollRegisterButton, Alignment.BOTTOM_LEFT);
        
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
			excelExport.setReportTitle(companyService.getTradeById(tradeId).toUpperCase()+" Payroll Register");
			excelExport.setExportFileName(companyService.getTradeById(tradeId).toUpperCase()+" Payroll Register");                    
			excelExport.export();
		}
	});
	glayout.addComponent(exportTableToExcel, 2, 0);
        glayout.setComponentAlignment(exportTableToExcel, Alignment.BOTTOM_LEFT);
	        
        vsplit.setFirstComponent(glayout);        
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
                      
        VerticalSplitPanel innerVsplit = new VerticalSplitPanel();        
        innerVsplit.addStyleName("small blue white");
        innerVsplit.setLocked(true);        
        innerVsplit.setSplitPosition(75, Sizeable.UNITS_PERCENTAGE);
        
        payrollRegisterTable(getBranchId(), null, false); 
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
                OpenHrisReports reports = new OpenHrisReports(getBranchId(), util.convertDateFormat(payrollDate.getValue().toString()));     
                
                if(payrollRegisterTbl.getPageLength() == 0){
                    getWindow().showNotification("No Data on table to be printed!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(payrollDate.getValue() == null){
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
                    payrollRegisterReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else if(reportType.getValue().equals("Adjusted Payroll Register")){
		    String fileName = "PayrollRegisterReportAdjusted_";
                    reports.deleteFile(fileName);
                    adjustedPayrollRegisterReport(util.convertDateFormat(payrollDate.getValue().toString()));	
		}else if(reportType.getValue().equals("Payslip Report")){
                    String fileName = "PayslipReport_";
                    reports.deleteFile(fileName);
                    payslipReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else if(reportType.getValue().equals("SSS Report")){
                    if(day == 15){
                        getWindow().showNotification("SSS Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }else{                        
			String fileName = "SssReport_";
                        reports.deleteFile(fileName);
                        sssReport(util.convertDateFormat(payrollDate.getValue().toString()));
                    }
                }else if(reportType.getValue().equals("SSS Report Sbarro")){
                    if(day == 15){
                        getWindow().showNotification("SSS Report Sbarro is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }else{                        
			String fileName = "SssReportSbarro_";
                        reports.deleteFile(fileName);
                        sssReportSbarro(util.convertDateFormat(payrollDate.getValue().toString()));
                    }
                }else if(reportType.getValue().equals("HDMF Report")){
                    if(day == 15){
                        String fileName = "HdmfReport_";
                        reports.deleteFile(fileName);
                        hdmfReport(util.convertDateFormat(payrollDate.getValue().toString()));
                    }else{
                        getWindow().showNotification("HDMF Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("HDMF Savings")){
                    String fileName = "HdmfVoluntarySavingsReport_";
                    reports.deleteFile(fileName);
                    hdmfSavingsReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else if(reportType.getValue().equals("Philhealth Report")){
                    if(day == 15){
                        String fileName = "PhicReport_";
                        reports.deleteFile(fileName);
                        phicReport(util.convertDateFormat(payrollDate.getValue().toString()));
                    }else{
                        getWindow().showNotification("Philhealth Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("Witholding Tax")){
                    String fileName = "WitholdingTaxesReport_";
                    reports.deleteFile(fileName);
                    witholdingTaxReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else if(reportType.getValue().equals("Attendance Report")){
                    String fileName = "AttendanceReport_";
                    reports.deleteFile(fileName);
                    attendanceReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else if(reportType.getValue().equals("Bank Debit Memo")){
                    String fileName = "BankDebitMemo_";
                    reports.deleteFile(fileName);
                    bankDebitMemoReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else if(reportType.getValue().equals("SSS Loans Payable")){
                    String fileName = "SssLoanReport_";
                    reports.deleteFile(fileName);
                    sssLoanReport(util.convertDateFormat(payrollDate.getValue().toString()));
                }else{
                    String fileName = "HdmfLoanReport_";
                    reports.deleteFile(fileName);
                    hdmfLoanReport(util.convertDateFormat(payrollDate.getValue().toString()));
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
                OpenHrisReports reports = new OpenHrisReports(getBranchId(), util.convertDateFormat(payrollDate.getValue().toString()));
                Window subWindow = reports.payrollRegisterTable(true, payrollRegisterTbl);
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    getWindow().addWindow(subWindow);
                }
                subWindow.center();
            }
        });
        innerGlayout.addComponent(viewFullScreenButton, 2, 0);
        innerGlayout.setComponentAlignment(viewFullScreenButton, Alignment.BOTTOM_LEFT);
        
        innerVsplit.addComponent(innerGlayout);
        vsplit.setSecondComponent(innerVsplit); 
    }
    
    public void payrollRegisterTable(int branchId, String payrollDate, boolean prev){
        payrollRegisterTbl.removeAllItems();
        payrollRegisterTbl.setSizeFull();
        payrollRegisterTbl.setImmediate(true);
        payrollRegisterTbl.setSelectable(true);
        payrollRegisterTbl.setColumnCollapsingAllowed(true);
        payrollRegisterTbl.addStyleName("employees-table-layout");
        
        payrollRegisterTbl.addContainerProperty("id", String.class, null);
        payrollRegisterTbl.addContainerProperty("name", String.class, null);
        payrollRegisterTbl.addContainerProperty("no. of days", Integer.class, null);        
        payrollRegisterTbl.addContainerProperty("rate per day", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("basic salary", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("half-month salary", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("overtime pay", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("legal holiday", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("special holiday", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("night differential", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("wdo", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("absent", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("lates", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("undertime", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("gross pay", Double.class, null);         
        payrollRegisterTbl.addContainerProperty("sss", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("phic", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("hdmf", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("tax", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("net pay", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("meal allowance", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("allowance for liquidation", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("advances to o/e", Double.class, null);         
        payrollRegisterTbl.addContainerProperty("adjustments", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("amount to be receive", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("amount received", Double.class, null);  
	payrollRegisterTbl.addContainerProperty("for adjustments", Double.class, null);
        
        payrollRegisterTbl.setColumnAlignment("no. of days", Table.ALIGN_CENTER);
        payrollRegisterTbl.setColumnAlignment("rate per day", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("absent", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("meal allowance", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("amount received", Table.ALIGN_RIGHT);
	payrollRegisterTbl.setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
        
        List<PayrollRegister> payrollRegisterList = payrollService.getPayrollRegisterByBranch(branchId, payrollDate, prev);
        int i = 0;
        for(PayrollRegister pr : payrollRegisterList){
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
		util.roundOffToTwoDecimalPlaces(pr.getAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getAllowanceForLiquidation()), 
		util.roundOffToTwoDecimalPlaces(pr.getAmount()), 
		util.roundOffToTwoDecimalPlaces(pr.getAdjustment()), 
                util.roundOffToTwoDecimalPlaces(pr.getAmountToBeReceive()-pr.getAllowanceForLiquidation()), 
		util.roundOffToTwoDecimalPlaces(pr.getAmountReceivable()-pr.getAllowanceForLiquidation()), 
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
                
                if(event.getPropertyId().equals("advances to o/e")){
                    if(!UserAccessControl.isAdvances()){
                        getWindow().showNotification("You are not allowed to add/delete advances!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    double amountToBeReceive = Double.parseDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = Double.parseDouble(item.getItemProperty("amount received").toString());
                    
                    Window subWindow = addAdvances(payrollId, amountToBeReceive, amountReceivable);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
        });
        
        payrollRegisterTbl.setColumnCollapsed("amount received", true);
	payrollRegisterTbl.setColumnCollapsed("for adjustments", true);
    }
    
    public int getBranchId(){
        return branchId;
    }
    
    public void setBranchId(int branchId){
        this.branchId = branchId;
    }    
    
    private Window addAdvances(final int payrollId, 
            final double amountToBeReceive, 
            final double amountReceivable){       
        final Window subWindow = new Window("ADD/REMOVE ADVANCES");
        subWindow.setWidth("450px");
        
        TabSheet ts = new TabSheet();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("View Advances");
        
        advancesTable(payrollId, 
                amountToBeReceive, 
                amountReceivable, 
                subWindow);       
        
        vlayout.addComponent(advancesTbl);
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Post Advances");
        
        final ComboBox advanceType = dropDown.populateAdvanceTypeDropDownList(new ComboBox());
        advanceType.setNewItemsAllowed(true);
        advanceType.setNewItemHandler(new AbstractSelect.NewItemHandler() {

            @Override
            public void addNewItem(String newItemCaption) {
                AdvancesTypeBean advancesTypeBean = new AdvancesTypeBean();
                if (!advanceType.containsId(newItemCaption)) {
                    advancesTypeBean.setAdvancesType(newItemCaption);
                    Boolean result = payrollService.insertAdvanceType(newItemCaption);
                    if(result = true){
                        getWindow().showNotification("Added Type: " + newItemCaption);
                        lastAddedAdvanceType = true;
                        advanceType.addItem(newItemCaption);
                        advanceType.setValue(newItemCaption);
                    }
                }
            }
            
        });
        advanceType.setImmediate(true);
        advanceType.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (!lastAddedAdvanceType) {
                    getWindow().showNotification(
                            "Selected Type: " + event.getProperty());
                }
                lastAddedAdvanceType = false;
            }
            
        });
        vlayout.addComponent(advanceType);
        
        final TextField particulars = new TextField("Particulars:");
        particulars.setWidth("100%");
        vlayout.addComponent(particulars);
        
        final TextField amount = new TextField("Amount:");
        amount.setWidth("100%");
        vlayout.addComponent(amount);
        
        final PopupDateField datePosted = new PopupDateField("Date Posted:");
        datePosted.addStyleName("mydate");
        datePosted.setValue(new Date());
        datePosted.setWidth("100%");
        datePosted.setDateFormat("EEE - MMM dd, yyyy");
        datePosted.setLenient(true);
        datePosted.setResolution(DateField.RESOLUTION_DAY);
        vlayout.addComponent(datePosted);
        
        Button button = new Button("POST ADVANCE AMOUNT");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {       
                if(advanceType.getValue() == null){
                    getWindow().showNotification("Select Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(particulars.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Particulars!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(!amount.getValue().toString().trim().isEmpty()){
                    boolean result = util.checkInputIfDouble(amount.getValue().toString().trim());
                    if(result == false){
                        getWindow().showNotification("Error entered Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                }else{
                    getWindow().showNotification("Please Enter an Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                                
                List<Advances> advanceList = new ArrayList<Advances>();
                Advances a = new Advances();
                a.setId(payrollId);
                a.setAmountToBeReceive(amountToBeReceive);
                a.setAmountReceivable(amountReceivable);
                a.setAmount(util.convertStringToDouble(amount.getValue().toString().trim()));
                a.setDatePosted(util.parsingDate(util.convertDateFormat(datePosted.getValue().toString())));
                a.setAdvanceType(advanceType.getValue().toString());
                a.setParticulars(particulars.getValue().toString());
                advanceList.add(a);
                
                Double advances = Double.parseDouble(amount.getValue().toString().trim());
                String postedDate = util.convertDateFormat(datePosted.getValue().toString());
                Boolean result = payrollService.updateSalaryByAdvances(advanceList);                
                if(result == true){
                    payrollRegisterTable(getBranchId(), payroll_date, false);
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        }); 
        vlayout.addComponent(button);
        
        ts.addComponent(vlayout);       
        
        subWindow.addComponent(ts);
        
        return subWindow;
    }
    
    private Table advancesTable(final int payrollId, 
            final double amountToBeReceive, 
            final double amountReceivable, 
            final Window window){
                
        advancesTbl.removeAllItems();
        advancesTbl.setWidth("100%");
        advancesTbl.setImmediate(true);
        advancesTbl.setSelectable(true);
        
        advancesTbl.addContainerProperty("id", String.class, null);
        advancesTbl.addContainerProperty("amount", String.class, null);
        advancesTbl.addContainerProperty("type", String.class, null);
        advancesTbl.addContainerProperty("particulars", String.class, null);
        advancesTbl.addContainerProperty("date posted", String.class, null);
        
        int i = 0;
        List<Advances> advancesList = payrollService.getAdvancesByPayroll(payrollId);
        for(Advances a : advancesList){
            advancesTbl.addItem(new Object[]{
                a.getAdvanceId(), a.getAmount(), a.getAdvanceType(), 
                a.getParticulars(), util.convertDateFormat(a.getDatePosted().toString())
            }, new Integer(i));
            i++;
        }
        advancesTbl.setPageLength(advancesTbl.size());
        
        for(Object listener : advancesTbl.getListeners(ItemClickEvent.class)){
            advancesTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        advancesTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                final Item item = advancesTbl.getItem(itemId);
                              
                if(event.getPropertyId().equals("id")){
                    String amount = item.getItemProperty("amount").getValue().toString();                
                    Double removedAdvances = Double.parseDouble(util.removeCommaFromString(amount));
                    int advanceId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                        
                    Window subWindow = removeAdvances(payrollId, 
                            advanceId, 
                            removedAdvances, 
                            amountToBeReceive, 
                            amountReceivable, 
                            window);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
            
        });
        
        return advancesTbl;
    }
    
    private Window removeAdvances(final int payrollId, 
            final int advanceId, 
            final double removedAmount, 
            final double amountToBeReceive, 
            final double amountReceivable, 
            final Window window){
        final Window subWindow = new Window("REMOVE ADVANCES");
        subWindow.setWidth("220px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        
        final TextField remarks = new TextField("Remarks: ");
        remarks.setWidth("100%");
        vlayout.addComponent(remarks);
        
        Button removeAdvances = new Button("REMOVE ADVANCE AMOUNT");
        removeAdvances.setWidth("100%");
        removeAdvances.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add remarks!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                boolean result = payrollService.removeAdvanceById(advanceId, payrollId, 
                        removedAmount, amountToBeReceive, amountReceivable, remarks.getValue().toString());
                if(result == true){
                    payrollRegisterTable(getBranchId(), payroll_date, false);
                    advancesTable(payrollId, 
                            amountToBeReceive, 
                            amountReceivable, 
                            window);
                    
                    (subWindow.getParent()).removeWindow(subWindow);
                    (window.getParent()).removeWindow(window);
                }
            }    

        });
        vlayout.addComponent(removeAdvances);
        
        subWindow.addComponent(vlayout);
        return subWindow;
    }
    
    public void payrollRegisterReport(String payrollDate){
        Connection conn = getConnection.connection();
        File reportFile = new File("C:/reportsJasper/payrollRegisterReport.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             final String filePath = "C:/reportsPdf/payrollRegisterReport_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, filePath);

             Window subWindow = new Window("Payroll Register Report");
             ((VerticalLayout) subWindow.getContent()).setSizeFull();
             subWindow.setWidth("800px");
             subWindow.setHeight("600px");
             subWindow.center();

             StreamResource.StreamSource source = new StreamResource.StreamSource() {
                 @Override
                 public InputStream getStream() {
                     try {
                         File f = new File(filePath);
                         FileInputStream fis = new FileInputStream(f);
                         return fis;
                     } catch (Exception e) {
                         e.getMessage();
                         return null;
                     }
                 }
             };

             StreamResource resource = new StreamResource(source, filePath, getApplication());
             resource.setMIMEType("application/pdf");       

             Embedded e = new Embedded();
             e.setMimeType("application/pdf");
             e.setType(Embedded.TYPE_OBJECT);
             e.setSizeFull();
             e.setSource(resource);
             e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

             subWindow.addComponent(e);

             getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
             e.getMessage();
        }
    }
    
    public void adjustedPayrollRegisterReport(String payrollDate){
        Connection conn = getConnection.connection();
        File reportFile = new File("C:/reportsJasper/PayrollRegisterReportAdjusted.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             final String filePath = "C:/reportsPdf/payrollRegisterReportAdjusted_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, filePath);

             Window subWindow = new Window("Payroll Register Report");
             ((VerticalLayout) subWindow.getContent()).setSizeFull();
             subWindow.setWidth("800px");
             subWindow.setHeight("600px");
             subWindow.center();

             StreamResource.StreamSource source = new StreamResource.StreamSource() {
                 @Override
                 public InputStream getStream() {
                     try {
                         File f = new File(filePath);
                         FileInputStream fis = new FileInputStream(f);
                         return fis;
                     } catch (Exception e) {
                         e.getMessage();
                         return null;
                     }
                 }
             };

             StreamResource resource = new StreamResource(source, filePath, getApplication());
             resource.setMIMEType("application/pdf");       

             Embedded e = new Embedded();
             e.setMimeType("application/pdf");
             e.setType(Embedded.TYPE_OBJECT);
             e.setSizeFull();
             e.setSource(resource);
             e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

             subWindow.addComponent(e);

             getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
             e.getMessage();
        }
    }
    
    private void payslipReport(String payrollDate){
       Connection conn = getConnection.connection();
       File reportFile = new File("C:/reportsJasper/PayslipReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", payrollDate);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/PayslipReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("Payslip Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");     

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
       }catch(Exception e){
            e.getMessage();
       }
   }
   
   private void hdmfReport(String payrollDate){       
       Connection conn = getConnection.connection();
       File reportFile = new File("C:/reportsJasper/HdmfReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", payrollDate);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/HdmfReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("HDMF Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");  

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
       }catch(Exception e){
            e.getMessage();
       }
   }
   
   private void hdmfSavingsReport(String payrollDate){
       Connection conn = getConnection.connection();
       File reportFile = new File("C:/reportsJasper/HdmfVoluntarySavingsReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", payrollDate);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/HdmfVoluntarySavingsReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("HDMF Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");      

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
       }catch(Exception e){
            e.getMessage();
       }
   }
   
   private void sssReport(String payrollDate){
	Connection conn = getConnection.connection();   
        File reportFile = new File("C:/reportsJasper/SssReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/SssReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("SSS Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");     

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }
   
   private void sssReportSbarro(String payrollDate){
	Connection conn = getConnection.connection();   
        File reportFile = new File("C:/reportsJasper/SssReportSbarro.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/SssReportSbarro_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("SSS Report Sbarro");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");     

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }
   
   private void phicReport(String payrollDate){
       Connection conn = getConnection.connection();	   
       File reportFile = new File("C:/reportsJasper/PhicReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/PhicReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("Phic Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");     

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }
   
   private void witholdingTaxReport(String payrollDate){
       Connection conn = getConnection.connection();	   
       File reportFile = new File("C:/reportsJasper/WitholdingTaxesReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/WitholdingTaxesReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("Witholding Taxes Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");     

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }
   
   private void attendanceReport(String payrollDate){
       Connection conn = getConnection.connection();	   
       File reportFile = new File("C:/reportsJasper/AttendanceReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/AttendanceReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("Witholding Taxes Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");       

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }
   
   private void bankDebitMemoReport(String payrollDate){
       Connection conn = getConnection.connection();	   
       File reportFile = new File("C:/reportsJasper/BankDebitMemo.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/BankDebitMemo_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("Witholding Taxes Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");    

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }     
   
   private void sssLoanReport(String payrollDate){
       Connection conn = getConnection.connection();	   
       File reportFile = new File("C:/reportsJasper/SssLoanReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/SssLoanReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("SSS Loans Payable Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");        

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }  
   
   private void hdmfLoanReport(String payrollDate){
	Connection conn = getConnection.connection();   
        File reportFile = new File("C:/reportsJasper/HdmfLoanReport.jasper");
	int tradeId = companyService.getTradeIdByBranchId(branchId);
	int corporateId = companyService.getCorporateIdByTradeId(tradeId);

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", corporateId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/HdmfLoanReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("HDMF Loans Payable Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");   

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }

   private void allowancesReport(String payroll_date){
        Connection conn = getConnection.connection(); 
        File reportFile = new File("C:/reportsJasper/AllowancesReport.jasper");
        int tradeId = companyService.getTradeIdByBranchId(branchId);
        int corporateId = companyService.getCorporateIdByTradeId(tradeId);
	String corporateName = companyService.getCorporateById(corporateId);

        final HashMap hm = new HashMap();
        hm.put("CORPORATE_NAME", corporateName);
        hm.put("PAYROLL_DATE", payroll_date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/AllowancesReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);

            Window subWindow = new Window("Allowances Report");
            ((VerticalLayout) subWindow.getContent()).setSizeFull();
            subWindow.setWidth("800px");
            subWindow.setHeight("600px");
            subWindow.center();

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        File f = new File(filePath);
                        FileInputStream fis = new FileInputStream(f);
                        return fis;
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            };

            StreamResource resource = new StreamResource(source, filePath, getApplication());
            resource.setMIMEType("application/pdf");
            //resource.getStream();                    

            Embedded e = new Embedded();
            e.setMimeType("application/pdf");
            e.setType(Embedded.TYPE_OBJECT);
            e.setSizeFull();
            e.setSource(resource);
            e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

            subWindow.addComponent(e);

            getApplication().getMainWindow().open(resource, "_blank");
        }catch(Exception e){
            e.getMessage();
        }
   }
}
