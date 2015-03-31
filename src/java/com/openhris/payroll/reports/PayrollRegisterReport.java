/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.reports;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author jetdario
 */
public class PayrollRegisterReport extends Window {

    GetSQLConnection getConnection = new GetSQLConnection();
    private int branchId;
    private String payrollDate;
        
    String filePath;
    Application payrollApplication;
    
    public PayrollRegisterReport(int branchId, String payrollDate, Application payrollApplication) {
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        this.payrollApplication = payrollApplication;
        
        setCaption("Payroll Register Report");
        setSizeFull();
        center();
        
        Connection conn = getConnection.connection();
        File reportFile = new File("C:/reportsJasper/payrollRegisterReport.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", getBranchId());
        hm.put("PAYROLL_DATE", getPayrollDate());
        
        try {
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            filePath = "C:/reportsPdf/payrollRegisterReport_"+timestamp+".pdf";
            JasperExportManager.exportReportToPdfFile(jpReport, filePath);                   
        } catch (JRException ex) {
            Logger.getLogger(PayrollRegisterReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollRegisterReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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

        StreamResource resource = new StreamResource(source, filePath, getPayrollApplication());
        resource.setMIMEType("application/pdf");       

        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSizeFull();
        Embedded e = new Embedded("", new FileResource(new File(resource.getFilename()), getPayrollApplication()));
//        e.setMimeType("application/pdf");
//        e.setType(Embedded.TYPE_OBJECT);
        e.setSizeFull();
        e.setType(Embedded.TYPE_BROWSER);        
//        e.setSource(resource);
//        e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());
        vlayout.addComponent(e);
        
        addComponent(vlayout);

//        getPayrollApplication().getMainWindow().addWindow(this);
        getPayrollApplication().getMainWindow().open(resource, "_blank");
    }
        
    int getBranchId(){
        return branchId;
    }
    
    String getPayrollDate(){
        return payrollDate;
    }
    
    Application getPayrollApplication(){
        return payrollApplication;
    }
}
