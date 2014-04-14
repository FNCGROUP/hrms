/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons.reports;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.payroll.PayrollRegisterMainUI;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.payroll.model.serviceprovider.PayrollServiceImpl;
import com.openhris.service.PayrollService;
import com.vaadin.Application;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
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
public class OpenHrisReports extends VerticalLayout {
    
    private static final String FILE_DIR = "c:\\reportsPdf";
    private static final String FILE_TEXT_EXT = ".pdf";
    GetSQLConnection getConnection = new GetSQLConnection();
    PayrollService payrollService = new PayrollServiceImpl();
    
    private int branchId;
    private String payrollDate;
    
    public OpenHrisReports(int branchId, String payrollDate){
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        setHeight("100%");
        setImmediate(true); 
    }
        
    public Window payrollRegisterTable(boolean prev){
        Window subWindow = new Window("Payroll Register Report");
        subWindow.setSizeFull();
        
        Table table = new Table();
        table.removeAllItems();
        table.setSizeFull();
        table.setImmediate(true);
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        
        table.addContainerProperty("id", String.class, null);
        table.addContainerProperty("name", String.class, null);
        table.addContainerProperty("no. of days", Integer.class, null);        
        table.addContainerProperty("rate per day", Double.class, null);        
        table.addContainerProperty("basic salary", Double.class, null);        
        table.addContainerProperty("half-month salary", Double.class, null);        
        table.addContainerProperty("overtime pay", Double.class, null);        
        table.addContainerProperty("legal holiday", Double.class, null);        
        table.addContainerProperty("special holiday", Double.class, null);        
        table.addContainerProperty("night differential", Double.class, null);        
        table.addContainerProperty("wdo", Double.class, null);        
        table.addContainerProperty("absent", Double.class, null);        
        table.addContainerProperty("lates", Double.class, null);        
        table.addContainerProperty("undertime", Double.class, null);        
        table.addContainerProperty("gross pay", Double.class, null);         
        table.addContainerProperty("sss", Double.class, null);        
        table.addContainerProperty("phic", Double.class, null);        
        table.addContainerProperty("hdmf", Double.class, null);        
        table.addContainerProperty("tax", Double.class, null);        
        table.addContainerProperty("net pay", Double.class, null);        
        table.addContainerProperty("meal allowance", Double.class, null);        
        table.addContainerProperty("allowance for liquidation", Double.class, null);        
        table.addContainerProperty("advances to o/e", Double.class, null);         
        table.addContainerProperty("adjustments", Double.class, null);        
        table.addContainerProperty("amount to be receive", Double.class, null);        
        table.addContainerProperty("amount received", Double.class, null);        
        
        table.setColumnAlignment("no. of days", Table.ALIGN_CENTER);
        table.setColumnAlignment("rate per day", Table.ALIGN_RIGHT);
        table.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        table.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        table.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        table.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        table.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        table.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        table.setColumnAlignment("absent", Table.ALIGN_RIGHT);
        table.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        table.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        table.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        table.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        table.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        table.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        table.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        table.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        table.setColumnAlignment("meal allowance", Table.ALIGN_RIGHT);
        table.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        table.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        table.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        table.setColumnAlignment("amount received", Table.ALIGN_RIGHT);
        
        List<PayrollRegister> payrollRegisterList = payrollService.getPayrollRegisterByBranch(branchId, payrollDate, prev);
        int i = 0;
        for(PayrollRegister pr : payrollRegisterList){
            table.addItem(new Object[]{
                pr.getId(), pr.getName(), pr.getNumOfDays(), pr.getRatePerDay(), pr.getBasicSalary(), 
                pr.getHalfMonthSalary(), pr.getTotalOvertimePaid(), pr.getTotalLegalHolidayPaid(), 
                pr.getTotalSpecialHolidayPaid(), pr.getTotalNightDifferentialPaid(), 
                pr.getTotalWorkingDayOffPaid(), pr.getAbsences(), pr.getTotalLatesDeduction(), 
                pr.getTotalUndertimeDeduction(), pr.getGrossPay(), pr.getSss(), pr.getPhic(), 
                pr.getHdmf(), pr.getTax(), pr.getNetSalary(), pr.getAllowance(), 
                pr.getAllowanceForLiquidation(), pr.getAmount(), pr.getAdjustment(), 
                pr.getAmountToBeReceive(), pr.getAmountReceivable()
            }, new Integer(i));
            i++;
        }
        table.setPageLength(table.size());
        
        for(Object listener : table.getListeners(ItemClickEvent.class)){
            table.removeListener(ItemClickEvent.class, listener);
        }
                
        table.setColumnCollapsed("amount received", true);
        subWindow.addComponent(table);
        
        Button printButton = new Button("Print Payroll Register");
        printButton.setWidth("200px");
        printButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String fileName = "payrollRegisterReport_";
                deleteFile(fileName);
                payrollRegisterReport(true);
            }
        });
        printButton.setImmediate(true); 
        printButton.setEnabled(false);
        subWindow.addComponent(printButton);
        
        return subWindow;
    }
    
    public void payrollRegisterReport(boolean selectedPayrollRegisterReport){
        Window subWindow = null;
        Connection conn = getConnection.connection();
        File reportFile;
        if(selectedPayrollRegisterReport == true){
            reportFile = new File("C:/reportsJasper/payrollRegisterReport.jasper");
        }else{
            reportFile = new File("C:/reportsJasper/payrollRegisterReportAdjusted.jasper");
        }       
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);
        
        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             final String filePath = "C:/reportsPdf/payrollRegisterReport_"+timestamp+".pdf";
             //JasperExportManager.exportReportToPdfFile(jpReport, "C:\\reports\\payrollRegisterReport.pdf");
             JasperExportManager.exportReportToPdfFile(jpReport, filePath);

             subWindow = new Window("Payroll Register Report");
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

             getApplication().getMainWindow().open(resource, "_top");
        }catch(Exception e){
             e.getMessage();
        }
    }
    
    public void deleteFile(String ext){
 
        OpenHrisReports.GenericExtFilter filter = new OpenHrisReports.GenericExtFilter(ext);
        File dir = new File(FILE_DIR);
 
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
}
