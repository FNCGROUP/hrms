/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons.reports;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
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
 * @author jetdario
 */
public class AdvancesReport extends VerticalLayout {

    GetSQLConnection getConnection = new GetSQLConnection();
    private String corporate;
    private String payrollDate;
    
    public AdvancesReport(String corporate, String payrollDate) {
        this.corporate = corporate;
        this.payrollDate = payrollDate;        
    }
    
    public void openAdvancesReport(){
        Connection conn = getConnection.connection();
        File reportFile = new File("C:/reportsJasper/AdvancesReport.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("CORPORATE", getCorporate());
        hm.put("PAYROLL_DATE", getPayrollDate());

        Window subWindow = new Window("Advances Report");
        ((VerticalLayout) subWindow.getContent()).setSizeFull();
        subWindow.setWidth("800px");
        subWindow.setHeight("600px");
        subWindow.center();
        
        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             final String filePath = "C:/reportsPdf/AdvancesReport_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, filePath);

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
    
    String getCorporate(){
        return corporate;
    }
    
    String getPayrollDate(){
        return payrollDate;
    }
    
}
