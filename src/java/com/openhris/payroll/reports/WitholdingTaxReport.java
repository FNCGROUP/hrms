/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.reports;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
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
public class WitholdingTaxReport extends Window {
    GetSQLConnection getConnection = new GetSQLConnection();
    private int branchId;
    private String payrollDate;
        
    File file;
    Application payrollApplication;

    public WitholdingTaxReport(int branchId, String payrollDate, Application payrollApplication) {
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        this.payrollApplication = payrollApplication;
        
        setCaption("Witholding Tax Report");
        setSizeFull();
        setWidth("800px");
        setHeight("600px");
        center();
        
        Connection conn = getConnection.connection();
        URL url = this.getClass().getResource("/com/openhris/reports/WitholdingTaxesReport.jasper");
//        File reportFile = new File("C:/reportsJasper/WitholdingTaxesReport.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", getBranchId());
        hm.put("PAYROLL_DATE", getPayrollDate());

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(url.getPath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             file = File.createTempFile("WitholdingTaxesReport_"+timestamp, ".pdf");
//             filePath = "C:/reportsPdf/WitholdingTaxesReport_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, file.getAbsolutePath());             
        }catch(Exception e){
             e.getMessage();
        }
        
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
//                    File f = new File(filePath);
                    FileInputStream fis = new FileInputStream(file);
                    return fis;
                } catch (Exception e) {
                    e.getMessage();
                    return null;
                }
            }
        };

        StreamResource resource = new StreamResource(source, file.getAbsolutePath(), getPayrollApplication());
        resource.setMIMEType("application/pdf");       

        Embedded e = new Embedded();
        e.setMimeType("application/pdf");
        e.setType(Embedded.TYPE_OBJECT);
        e.setSizeFull();
        e.setSource(resource);
        e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

        addComponent(e);
        getPayrollApplication().getMainWindow().open(resource, "_blank");
    }

    public int getBranchId() {
        return branchId;
    }

    public String getPayrollDate() {
        return payrollDate;
    }

    public Application getPayrollApplication() {
        return payrollApplication;
    }
    
}
