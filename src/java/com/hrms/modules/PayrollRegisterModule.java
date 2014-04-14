/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.openhris.administrator.model.UserAccessControl;
import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.TradeName;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.AdjustmentsDAO;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author jet
 */
public class PayrollRegisterModule extends VerticalLayout {
    
    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    CorporateName corporateNames = new CorporateName();
    TradeName tradeNames = new TradeName();
    BranchName branchNames = new BranchName();
    NativeSelect corporateName;
    NativeSelect tradeName;
    NativeSelect branchName;
    Table employeeSalaryTable = new Table();
    Table adjustedEmployeeSalaryTable = new Table();
    Table selectedTable = new Table();
    
    private String userRole;
    private Boolean selectedPayrollRegisterReport;
    
    Integer branchId;
    Integer corporateId;
    String date;
    private static final String FILE_DIR = "c:\\reportsPdf";
    private static final String FILE_TEXT_EXT = ".pdf";
    
    private String payrollRegisterQuery = "SELECT * FROM payroll_register";
    
    public PayrollRegisterModule(final String userRole){
        
        this.userRole = userRole;
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        
        final VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(32, Sizeable.UNITS_PERCENTAGE);
        
        GridLayout comboBoxGrid = new GridLayout(3, 3);
        comboBoxGrid.setMargin(true);
        comboBoxGrid.setSpacing(true);
        comboBoxGrid.setWidth(90, Sizeable.UNITS_PERCENTAGE);
        
        vsplit.setFirstComponent(comboBoxGrid);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        corporateName = new NativeSelect("Select Corporate Name:");
        corporateName.setWidth("270px");
        corporateNames.getCorporateName(corporateName);
        comboBoxGrid.addComponent(corporateName, 0, 0);
        
        tradeName = new NativeSelect("Select Trade Name:"); 
        tradeName.setWidth("270px");
        corporateName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                corporateId = query.getCoporateId(corporateName.getValue().toString());
                tradeNames.getTradeName(tradeName, corporateName.getValue().toString());
            }
            
        });        
        comboBoxGrid.addComponent(tradeName, 1, 0);
        
        branchName = new NativeSelect("Select Branch:");
        branchName.setWidth("270px");        
        tradeName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tradeName.getValue() == null){                    
                }else{
                    branchNames.getBranchName(branchName, tradeName.getValue().toString(), corporateName.getValue().toString());
                }                
            }
        });
        branchName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(branchName.getValue() == null){                    
                }else{
                    branchId = query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), corporateName.getValue().toString());
                }                
            }
            
        });
        comboBoxGrid.addComponent(branchName, 2, 0);
        
        final PopupDateField payrollDate = new PopupDateField("Payroll Date");
        payrollDate.addStyleName("mydate");
        payrollDate.setValue(null);
        payrollDate.setWidth("270px");
        payrollDate.setDateFormat("EEE - MMM dd, yyyy");
        payrollDate.setLenient(true);
        payrollDate.setResolution(DateField.RESOLUTION_DAY);
        payrollDate.setImmediate(true);
        comboBoxGrid.addComponent(payrollDate, 0, 1);        
        
        Button payrollRegisterReportButton = new Button("Generate Payroll Register Table");
        payrollRegisterReportButton.setWidth("270px");
        payrollRegisterReportButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(corporateName.getValue() == null){
                    getWindow().showNotification("Select a Corporate Name!");
                    return;
                }
                
                if(tradeName.getValue() == null){
                    getWindow().showNotification("Select a Trade Name!");
                    return;
                }
                
                if(branchName.getValue() == null){
                    getWindow().showNotification("Select a Branch!");
                    return;
                }
                date = conUtil.convertDateFormat(payrollDate.getValue().toString());
                String newPayrollQuery = payrollRegisterQuery+" Where branchId = "+branchId+" AND payrollDate = '"+date+"' "
                    + "AND (rowStatus != 'removed' OR rowStatus IS NULL) AND (actionTaken = 'previous' OR actionTaken IS NULL ) ORDER BY name ASC";
                employeeSalaryTbl(branchId, date, newPayrollQuery);
                selectedTable = employeeSalaryTable;
                selectedPayrollRegisterReport = true;
                vsplit.setSecondComponent(employeeSalaryTable);
            }
            
        });
        comboBoxGrid.addComponent(payrollRegisterReportButton, 1, 1);
        comboBoxGrid.setComponentAlignment(payrollRegisterReportButton, Alignment.BOTTOM_LEFT);
        
        Button adjustedPayrollRegisterReportButton = new Button("Generate Adjusted Payroll Register Table");
        adjustedPayrollRegisterReportButton.setWidth("270px");
        adjustedPayrollRegisterReportButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(corporateName.getValue() == null){
                    getWindow().showNotification("Select a Corporate Name!");
                    return;
                }
                
                if(tradeName.getValue() == null){
                    getWindow().showNotification("Select a Trade Name!");
                    return;
                }
                
                if(branchName.getValue() == null){
                    getWindow().showNotification("Select a Branch!");
                    return;
                }
                date = conUtil.convertDateFormat(payrollDate.getValue().toString());
                String newPayrollQuery = payrollRegisterQuery+" Where branchId = "+branchId+" AND payrollDate = '"+date+"' "
                    + "AND (rowStatus != 'removed' OR rowStatus IS NULL) AND (actionTaken = 'adjusted' OR actionTaken IS NULL ) ORDER BY name ASC";
                adjustedEmployeeSalaryTbl(branchId, date, newPayrollQuery);
                selectedTable = adjustedEmployeeSalaryTable;
                selectedPayrollRegisterReport = false;
                vsplit.setSecondComponent(adjustedEmployeeSalaryTable);
            }
            
        });
        comboBoxGrid.addComponent(adjustedPayrollRegisterReportButton, 2, 1);
        comboBoxGrid.setComponentAlignment(adjustedPayrollRegisterReportButton, Alignment.BOTTOM_LEFT);
        
        String[] reportTypeArray = {"Payroll Register", "Payslip Report", "SSS Report", "SSS Loans Payable","Philhealth Report", 
            "HDMF Report", "HDMF Savings", "HDMF Loans Payable","Witholding Tax", "Attendance Report", "Bank Debit Memo", "Allowances"};
        
        final ComboBox reportType = new ComboBox("Select Report Type: ");
        reportType.setWidth("270px");
        reportType.setNullSelectionAllowed(false);
        for(int i = 0; i < reportTypeArray.length; i++){
            reportType.addItem(reportTypeArray[i]);
        }        
        reportType.setImmediate(true);
        comboBoxGrid.addComponent(reportType, 0, 2);
        
        Button payrollRegisterTableButton = new Button("Generate Report");
        payrollRegisterTableButton.setWidth("270px");
        payrollRegisterTableButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                GetSQLConnection getConnection = new GetSQLConnection();
                Connection conn = getConnection.connection();
                
                if(reportType.getValue().equals("Allowances")){
                    String fileName = "AllowancesReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    String payroll_date = conUtil.convertDateFormat(payrollDate.getValue().toString());
                    allowancesReport(conn, corporateName.getValue().toString(), payroll_date);
                    return;
                }
                
                if(branchName.getValue() == null){
                    getWindow().showNotification("Select a Branch!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(date == null || date.trim().isEmpty()){
                    getWindow().showNotification("Select Payroll Date!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(reportType.getValue() == null){
                    getWindow().showNotification("Select Report Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                String payrollPeriod = query.getPayrollPeriod(conUtil.convertDateFormat(payrollDate.getValue().toString()));
                                
                if(reportType.getValue().equals("Payroll Register")){
                    String fileName = "payrollRegisterReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    fileName = "PayrollRegisterReportOld_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    payrollRegisterReport(conn);
                }else if(reportType.getValue().equals("Payslip Report")){
                    String fileName = "PayslipReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    payslipReport(conn);
                }else if(reportType.getValue().equals("SSS Report")){
                    if(payrollPeriod.equals("15th of the month")){
                        String fileName = "SssReport_";
                        new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                        sssReport(conn);
                    }else{
                        getWindow().showNotification("SSS Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("HDMF Report")){
                    if(payrollPeriod.equals("30th of the month")){
                        String fileName = "HdmfReport_";
                        new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                        hdmfReport(conn);
                    }else{
                        getWindow().showNotification("HDMF Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("HDMF Savings")){
                    String fileName = "HdmfVoluntarySavingsReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    hdmfSavingsReport(conn);
                }else if(reportType.getValue().equals("Philhealth Report")){
                    if(payrollPeriod.equals("30th of the month")){
                        String fileName = "PhicReport_";
                        new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                        phicReport(conn);
                    }else{
                        getWindow().showNotification("Philhealth Report is disabled for this payroll period", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                }else if(reportType.getValue().equals("Witholding Tax")){
                    String fileName = "WitholdingTaxesReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    witholdingTaxReport(conn);
                }else if(reportType.getValue().equals("Attendance Report")){
                    String fileName = "AttendanceReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    attendanceReport(conn);
                }else if(reportType.getValue().equals("Bank Debit Memo")){
                    String fileName = "BankDebitMemo_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    bankDebitMemoReport(conn);
                }else if(reportType.getValue().equals("SSS Loans Payable")){
                    String fileName = "SssLoanReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    sssLoanReport(conn);
                }else{
                    String fileName = "HdmfLoanReport_";
                    new PayrollRegisterModule(userRole).deleteFile(FILE_DIR, fileName);
                    hdmfLoanReport(conn);
                }
            }
            
        });
        comboBoxGrid.addComponent(payrollRegisterTableButton, 1, 2);
        comboBoxGrid.setComponentAlignment(payrollRegisterTableButton, Alignment.BOTTOM_LEFT);
        
        Button exportTableToExcelButton = new Button("Export Payroll Register to Excel");
        exportTableToExcelButton.setWidth("270px");
        exportTableToExcelButton.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = -73954695086117200L;
            private ExcelExport excelExport;
            
            @Override
            public void buttonClick(ClickEvent event) {
                excelExport = new ExcelExport(selectedTable, "MySheet");
                excelExport.excludeCollapsedColumns();
                excelExport.setReportTitle(tradeName.getValue().toString().toUpperCase()+" Payroll Register");
                excelExport.setExportFileName(tradeName.getValue().toString().toUpperCase()+"PayrollRegister");                    
                excelExport.export();
                
                /*try {                    
                   File tempFile = File.createTempFile("tmp", ".xls");
                    // Create contents here, using POI, and write to tempFile
                    TemporaryFileDownloadResource resource = new TemporaryFileDownloadResource(getApplication(), 
                            excelExport.getExportFileName(), "application/vnd.ms-excel", tempFile);
                        getWindow().open(resource, "_self");
                } catch (IOException ex) {
                    Logger.getLogger(PayrollRegisterModule.class.getName()).log(Level.SEVERE, null, ex);
                } */
            }
            
        });
        comboBoxGrid.addComponent(exportTableToExcelButton, 2, 2);
        comboBoxGrid.setComponentAlignment(exportTableToExcelButton, Alignment.BOTTOM_LEFT);
                
    }
    
    public final Table employeeSalaryTbl (Integer id, String date, final String queryPayrollRegisters){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employeeSalaryTable.removeAllItems();
        employeeSalaryTable.setSizeFull();
        employeeSalaryTable.setImmediate(true);
        employeeSalaryTable.setSelectable(true);
        employeeSalaryTable.setColumnCollapsingAllowed(true);
        
        employeeSalaryTable.addContainerProperty("id", String.class, null);
        employeeSalaryTable.addContainerProperty("name", String.class, null);
        employeeSalaryTable.addContainerProperty("no. of days", String.class, null);        
        employeeSalaryTable.addContainerProperty("rate per day", String.class, null);        
        employeeSalaryTable.addContainerProperty("basic salary", String.class, null);        
        employeeSalaryTable.addContainerProperty("half-month salary", String.class, null);        
        employeeSalaryTable.addContainerProperty("overtime pay", String.class, null);        
        employeeSalaryTable.addContainerProperty("legal holiday", String.class, null);        
        employeeSalaryTable.addContainerProperty("special holiday", String.class, null);        
        employeeSalaryTable.addContainerProperty("night differential", String.class, null);        
        employeeSalaryTable.addContainerProperty("wdo", String.class, null);        
        employeeSalaryTable.addContainerProperty("absent", String.class, null);        
        employeeSalaryTable.addContainerProperty("lates", String.class, null);        
        employeeSalaryTable.addContainerProperty("undertime", String.class, null);        
        employeeSalaryTable.addContainerProperty("gross pay", String.class, null);         
        employeeSalaryTable.addContainerProperty("sss", String.class, null);        
        employeeSalaryTable.addContainerProperty("phic", String.class, null);        
        employeeSalaryTable.addContainerProperty("hdmf", String.class, null);        
        employeeSalaryTable.addContainerProperty("tax", String.class, null);        
        employeeSalaryTable.addContainerProperty("net pay", String.class, null);        
        employeeSalaryTable.addContainerProperty("meal allowance", String.class, null);        
        employeeSalaryTable.addContainerProperty("allowance for liquidation", String.class, null);        
        employeeSalaryTable.addContainerProperty("advances to o/e", String.class, null);         
        employeeSalaryTable.addContainerProperty("adjustments", String.class, null);        
        employeeSalaryTable.addContainerProperty("amount to be receive", String.class, null);        
        employeeSalaryTable.addContainerProperty("amount received", String.class, null);        
        
        employeeSalaryTable.setColumnAlignment("no. of days", Table.ALIGN_CENTER);
        employeeSalaryTable.setColumnAlignment("rate per day", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("absent", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("meal allowance", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        employeeSalaryTable.setColumnAlignment("amount received", Table.ALIGN_RIGHT);
        
        employeeSalaryTable.setColumnCollapsed("amount received", true);
                
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryPayrollRegisters);
            while(rs.next()){
                employeeSalaryTable.addItem(new Object[]{
                    rs.getString("salaryId"),
                    rs.getString("name").toUpperCase(),
                    rs.getString("numberOfDays"),
                    rs.getString("ratePerDay"),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("basicSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("halfMonthSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("overtimePay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("legalHolidayPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("specialHolidayPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("nightDifferentialPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("workingDayOffPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("absences"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalLatesDeduction"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalUndertimeDeduction"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("grossPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("sss"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("philhealth"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("hdmf"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("tax"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("netSalary"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("allowance"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("allowanceForLiquidation"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("advances"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("adjustments"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("amountToBeReceive"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("amountReceivable")))
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : employeeSalaryTable.getListeners(ItemClickEvent.class)){
            employeeSalaryTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employeeSalaryTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                try{
                    Object itemId = event.getItemId();
                    Item item = employeeSalaryTable.getItem(itemId);

                    Object loanItemId = event.getPropertyId();
                    employeeSalaryTable.refreshRowCache();
                    
                    if(event.getPropertyId().equals("adjustments")){
                        if(UserAccessControl.isAdjustment()== true){
                            Integer salaryId = Integer.parseInt(item.getItemProperty("id").toString());
                            Double amountToBeReceive = Double.valueOf(conUtil.removeCommaFromString(item.getItemProperty("amount to be receive").getValue().toString())).doubleValue();
                            Double amountReceive = Double.valueOf(conUtil.removeCommaFromString(item.getItemProperty("amount received").getValue().toString())).doubleValue();
                            Double adjustments = Double.valueOf(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString())).doubleValue();
                            
                            Window subWindow = addAdjustments(salaryId, amountToBeReceive, amountReceive, adjustments, queryPayrollRegisters);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }
                    }
                    
                    if(event.getPropertyId().equals("advances to o/e")){
                        if(UserAccessControl.isAdvances()== true){
                            String salaryId = item.getItemProperty("id").getValue().toString();
                            Double amountReceivable = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("amount received").getValue().toString()));
                            Double amountToBeReceive = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("amount to be receive").getValue().toString()));
                            Double adjustments = 0.00;
                            
                            if(item.getItemProperty("adjustments").getValue() != null){
                                adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            }
                            Window subWindow = addAdvances(salaryId, amountToBeReceive, amountReceivable);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    }
                    
                }catch(Exception e){
                    e.getMessage();
                }
                
            }
            
        }); 
        
        employeeSalaryTable.setPageLength(employeeSalaryTable.size());
                
        return employeeSalaryTable;
    }
    
    public final Table adjustedEmployeeSalaryTbl (Integer id, String date, final String queryPayrollRegisters){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        adjustedEmployeeSalaryTable.removeAllItems();
        adjustedEmployeeSalaryTable.setSizeFull();
        adjustedEmployeeSalaryTable.setImmediate(true);
        adjustedEmployeeSalaryTable.setSelectable(true);
        
        adjustedEmployeeSalaryTable.addContainerProperty("id", String.class, null);
        adjustedEmployeeSalaryTable.addContainerProperty("name", String.class, null);
        adjustedEmployeeSalaryTable.addContainerProperty("no. of days", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("half-month salary", Table.ALIGN_CENTER);
        adjustedEmployeeSalaryTable.addContainerProperty("rate per day", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("rate per day", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("basic salary", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("half-month salary", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("overtime pay", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("legal holiday", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("special holiday", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("night differential", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("wdo", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("absent", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("absent", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("lates", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("undertime", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("gross pay", String.class, null); 
        adjustedEmployeeSalaryTable.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("sss", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("phic", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("hdmf", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("tax", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("net pay", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("meal allowance", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("meal allowance", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("allowance for liquidation", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("advances to o/e", String.class, null); 
        adjustedEmployeeSalaryTable.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("adjustments", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("amount to be receive", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("amount received", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("amount received", Table.ALIGN_RIGHT);
        adjustedEmployeeSalaryTable.addContainerProperty("for adjustments", String.class, null);
        adjustedEmployeeSalaryTable.setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryPayrollRegisters);
            while(rs.next()){
                adjustedEmployeeSalaryTable.addItem(new Object[]{
                    rs.getString("salaryId"),
                    rs.getString("name").toUpperCase(),
                    rs.getString("numberOfDays"),
                    rs.getString("ratePerDay"),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("basicSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("halfMonthSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("overtimePay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("legalHolidayPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("specialHolidayPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("nightDifferentialPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("workingDayOffPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("absences"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalLatesDeduction"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalUndertimeDeduction"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("grossPay"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("sss"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("philhealth"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("hdmf"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("tax"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("netSalary"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("allowance"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("allowanceForLiquidation"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("advances"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("adjustments"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("amountToBeReceive"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("amountReceivable"))),                     
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("forAdjustments")))
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : adjustedEmployeeSalaryTable.getListeners(ItemClickEvent.class)){
            adjustedEmployeeSalaryTable.removeListener(ItemClickEvent.class, listener);
        }
        
        adjustedEmployeeSalaryTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                try{
                    Object itemId = event.getItemId();
                    Item item = adjustedEmployeeSalaryTable.getItem(itemId);

                    Object loanItemId = event.getPropertyId();
                    adjustedEmployeeSalaryTable.refreshRowCache();
                                                
                }catch(Exception e){
                    e.getMessage();
                }
                
            }
            
        }); 
        
        adjustedEmployeeSalaryTable.setPageLength(adjustedEmployeeSalaryTable.size());
                
        return adjustedEmployeeSalaryTable;
    }
    
    private Window addAdjustments(final Integer salaryId, final Double amountToBeReceive, final Double amountReceive, 
            final Double adjustments, final String queryPayrollReport){
        final Window subWindow = new Window("ADJUSTMENTS");
        subWindow.setWidth("300px");
        
        TabSheet ts = new TabSheet();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Post Adjustments");
        
        final TextField amount = new TextField("Amount: ");
        amount.setWidth("100%");
        vlayout.addComponent(amount);
        
        final TextField remarks = new TextField("Remarks");
        remarks.setWidth("100%");
        vlayout.addComponent(remarks);
        
        final PopupDateField datePosted = new PopupDateField("Date Posted:");
        datePosted.addStyleName("mydate");
        datePosted.setValue(new Date());
        datePosted.setWidth("100%");
        datePosted.setDateFormat("EEE - MMM dd, yyyy");
        datePosted.setLenient(true);
        datePosted.setResolution(DateField.RESOLUTION_DAY);
        vlayout.addComponent(datePosted);
        
        Button saveAdjustments = new Button("POST ADJUSTMENTS");
        saveAdjustments.setWidth("100%");
        saveAdjustments.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                
                if(amount.getValue().toString().trim().isEmpty() || remarks.getValue().toString().trim().isEmpty() || 
                        datePosted.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Complate all details!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Double newAdjustments = conUtil.convertStringToDouble(amount.getValue().toString().trim());
                Double amount_to_be_receive = 0.0;
                if(newAdjustments < 0 ){
                    Double totalAdjustments = adjustments + newAdjustments;
                    amount_to_be_receive = Math.round((amountToBeReceive + newAdjustments)*100.0)/100.0;
                }else{
                    Double totalAdjustments = adjustments + newAdjustments;
                    amount_to_be_receive = Math.round((amountReceive + totalAdjustments)*100.0)/100.0;
                }
                
                String postedDate = conUtil.convertDateFormat(datePosted.getValue().toString());
                
                Boolean result = queryUpdate.updateSalaryByAdjustments(salaryId, newAdjustments, amount_to_be_receive, amountReceive, 
                        remarks.getValue().toString().trim(), postedDate);
                if(result == true){
                    employeeSalaryTbl(branchId, date, queryPayrollReport);
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        vlayout.addComponent(saveAdjustments);
        
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Remove Adjustments");
        
        Label label = new Label("Remarks: ");
        vlayout.addComponent(label);
        
        AdjustmentsDAO adjustmentsDAO = new AdjustmentsDAO();
        String adjustmentsRemarks = adjustmentsDAO.getAdjustmentsRemarks(salaryId);
        TextArea textArea = new TextArea();
        textArea.setValue(adjustmentsRemarks);
        textArea.setWidth("100%");
        textArea.setRows(3);
        vlayout.addComponent(textArea);
        
        Button removeAdjustments = new Button("REMOVE ADJUSTMENTS");
        removeAdjustments.setWidth("100%");
        removeAdjustments.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(adjustments == 0.0){
                    subWindow.getWindow().showNotification("No adjustment to be REMOVED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Double amountToBeReceivable = amountReceive;
                Boolean result = queryUpdate.removeAdjustmentsOnSalary(salaryId, amountToBeReceivable);
                if(result == true){
                    employeeSalaryTbl(branchId, date, queryPayrollReport);
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        vlayout.addComponent(removeAdjustments);
        
        ts.addComponent(vlayout);
        
        subWindow.addComponent(ts);
        
        return subWindow;
    }
    
    private Window addAdvances(final String salaryRowId, final Double amountToBeReceive, final Double amountReceivable){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        final Window subWindow = new Window("ADD ADVANCES", vlayout);
        subWindow.setWidth("300px");
        
        final ComboBox advancesType = new ComboBox("Select Type: ");
        advancesType.setWidth("100%");
        List<String> advancesTypeLists = query.getAdvancesTypeLists();
        for(int i = 0; i < advancesTypeLists.size(); i++){
            advancesType.addItem(advancesTypeLists.get(i));
        }
        advancesType.setNullSelectionAllowed(false);        
        advancesType.setImmediate(true);
        subWindow.addComponent(advancesType);
        
        final TextField particulars = new TextField("Particulars:");
        particulars.setWidth("100%");
        subWindow.addComponent(particulars);
        
        final TextField amount = new TextField("Amount:");
        amount.setWidth("100%");
        subWindow.addComponent(amount);
        
        final PopupDateField datePosted = new PopupDateField("Date Posted:");
        datePosted.addStyleName("mydate");
        datePosted.setValue(new Date());
        datePosted.setWidth("100%");
        datePosted.setDateFormat("EEE - MMM dd, yyyy");
        datePosted.setLenient(true);
        datePosted.setResolution(DateField.RESOLUTION_DAY);
        subWindow.addComponent(datePosted);
        
        Button button = new Button("POST ADVANCE AMOUNT");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {       
                if(advancesType.getValue() == null){
                    getWindow().showNotification("Select Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(particulars.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Particulars!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(!amount.getValue().toString().trim().isEmpty()){
                    boolean result = conUtil.checkInputIfDouble(amount.getValue().toString().trim());
                    if(result == false){
                        getWindow().showNotification("Error entered Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                    if(Double.parseDouble(amount.getValue().toString().trim()) > 0){
                        getWindow().showNotification("Add advances on Salary Ledger if Amount is greater than 0!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }else{
                    getWindow().showNotification("Please Enter an Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                                
                Double advances = Double.parseDouble(amount.getValue().toString().trim());
                String postedDate = conUtil.convertDateFormat(datePosted.getValue().toString());
                String employeeId = query.getEmployeesIdBySalaryId(salaryRowId);
                Boolean result = queryUpdate.updateSalaryAdvancesByPayrollRegister(salaryRowId, advances, amountToBeReceive, amountReceivable, 
                        postedDate, advancesType.getValue().toString(), particulars.getValue().toString().trim(), employeeId);
                
                if(result == true){
                    String newPayrollQuery = payrollRegisterQuery+" Where branchId = "+branchId+" AND payrollDate = '"+date+"' "
                    + "AND (rowStatus != 'removed' OR rowStatus IS NULL) AND (actionTaken = 'new' OR actionTaken IS NULL ) ORDER BY name ASC";
                    employeeSalaryTbl(branchId, date, newPayrollQuery);
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        subWindow.addComponent(button);
        
        return subWindow;
    }
    
    public void deleteFile(String folder, String ext){
 
        GenericExtFilter filter = new GenericExtFilter(ext);
        File dir = new File(folder);
 
        //list out all the file name with .txt extension
        String[] list = dir.list(filter);
 
        if (list.length == 0) return;
 
        File fileDelete;
 
        for (String file : list){
            String temp = new StringBuffer(FILE_DIR)
                        .append(File.separator)
                        .append(file).toString();
            fileDelete = new File(temp);
            boolean isdeleted = fileDelete.delete();
            //System.out.println("file : " + temp + " is deleted : " + isdeleted);
        }
   }
    
    //inner class, generic extension filter 
   public class GenericExtFilter implements FilenameFilter {
 
        private String ext;
 
        public GenericExtFilter(String ext) {
            this.ext = ext;             
        }
 
        @Override
        public boolean accept(File dir, String name) {
            //return (name.endsWith(ext));
            return (name.startsWith(ext));
        }
    }
   
   private void payrollRegisterReport(Connection conn){
       File reportFile;
       if(selectedPayrollRegisterReport == true){
           reportFile = new File("C:/reportsJasper/payrollRegisterReport.jasper");
       }else{
           reportFile = new File("C:/reportsJasper/payrollRegisterReportAdjusted.jasper");
       }       
       //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", date);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/payrollRegisterReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void payslipReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/PayslipReport.jasper");
       //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", date);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/PayslipReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void hdmfReport(Connection conn){       
       File reportFile = new File("C:/reportsJasper/HdmfReport.jasper");
       //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", date);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/HdmfReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void hdmfSavingsReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/HdmfVoluntarySavingsReport.jasper");
       //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

       final HashMap hm = new HashMap();
       hm.put("BRANCH_ID", branchId);
       hm.put("PAYROLL_DATE", date);

       try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/HdmfVoluntarySavingsReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void sssReport(Connection conn){
        File reportFile = new File("C:/reportsJasper/SssReport.jasper");
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/SssReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void phicReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/PhicReport.jasper");
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/PhicReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void witholdingTaxReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/WitholdingTaxesReport.jasper");
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/WitholdingTaxesReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void attendanceReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/AttendanceReport.jasper");
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/AttendanceReport_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void bankDebitMemoReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/BankDebitMemo.jasper");
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/BankDebitMemo_"+timestamp+".pdf";
            //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void sssLoanReport(Connection conn){
       File reportFile = new File("C:/reportsJasper/SssLoanReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

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
                        //File f = new File("C:\\reports\\payrollRegisterReport.pdf");
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
   
   private void hdmfLoanReport(Connection conn){
        File reportFile = new File("C:/reportsJasper/HdmfLoanReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", corporateId);
        hm.put("PAYROLL_DATE", date);

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

   private void allowancesReport(Connection conn, String coporateName, String payroll_date){
       File reportFile = new File("C:/reportsJasper/AllowancesReport.jasper");

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
