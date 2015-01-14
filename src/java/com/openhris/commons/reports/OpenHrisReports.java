/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons.reports;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.payroll.service.PayrollService;
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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author jet
 */
public class OpenHrisReports extends VerticalLayout {
    
    private static final String FILE_DIR = "c:\\reportsPdf";
//    private static final String FILE_DIR = "/home/ReportPdf/";
    private static final String FILE_TEXT_EXT = ".pdf";
    GetSQLConnection getConnection = new GetSQLConnection();
    PayrollService payrollService = new PayrollServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities(); 
    
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
        
    public Window payrollRegisterTable(boolean prev, Table payrollRegister){
        Window subWindow = new Window("Payroll Register Report");
        subWindow.setSizeFull();        
        subWindow.addComponent(payrollRegister);
        
        Button printButton = new Button("Print Payroll Register");
        printButton.setWidth("200px");
        printButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String fileName = "payrollRegisterReport_";
                deleteFile(fileName);
                payrollRegisterReport(payrollDate);
            }
        });
        printButton.setImmediate(true); 
        printButton.setEnabled(false);
        subWindow.addComponent(printButton);
        
        return subWindow;
    }
    
    public void payrollRegisterReport(String payrollDate){
        Connection conn = getConnection.connection();
        File reportFile = new File("C:/reportsJasper/payrollRegisterReport.jasper");
        
        System.out.println("payroll date: "+payrollDate);
        
        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", payrollDate);

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             final String filePath = "C:/reportsPdf/payrollRegisterReport_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, filePath);

             Window reportWindow = new Window("Payroll Register Report");
             ((VerticalLayout) reportWindow.getContent()).setSizeFull();
             reportWindow.setWidth("800px");
             reportWindow.setHeight("600px");
             reportWindow.center();

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

             reportWindow.addComponent(e);

             getApplication().getMainWindow().open(resource, "_blank");
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
