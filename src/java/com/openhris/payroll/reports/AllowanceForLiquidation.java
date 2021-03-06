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
public class AllowanceForLiquidation extends Window {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    private String corporate;
    private String payrollDate;
    private int branchId;
        
//    File file;
    String file;
    Application payrollApplication;

    public AllowanceForLiquidation(String corporate, 
            String payrollDate, 
            int branchId, 
            Application payrollApplication) {
        this.corporate = corporate;
        this.payrollDate = payrollDate;
        this.branchId = branchId;
        this.payrollApplication = payrollApplication;
        
        setCaption("Advances Report");
        setSizeFull();
        setWidth("800px");
        setHeight("600px");
        center();
        
        Connection conn = getConnection.connection();
//        URL url = this.getClass().getResource("/com/openhris/reports/AllowanceForLiquidation.jasper");
        File url = new File("C:/reportsJasper/AllowanceForLiquidation.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("CORPORATE_NAME", getCorporate());
        hm.put("PAYROLL_DATE", getPayrollDate());
        hm.put("BRANCH_ID", getBranchId());

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(url.getPath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
//             file = File.createTempFile("AllowanceForLiquidation_"+timestamp, ".pdf");
             file = "C:/reportsPdf/AllowanceForLiquidation_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, file);             
        }catch(Exception e){
             e.getMessage();
        }
        
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
                    File f = new File(file);
                    FileInputStream fis = new FileInputStream(f);
                    return fis;
                } catch (Exception e) {
                    e.getMessage();
                    return null;
                }
            }
        };

        StreamResource resource = new StreamResource(source, file, getPayrollApplication());
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

    String getCorporate() {
        return corporate;
    }

    String getPayrollDate() {
        return payrollDate;
    }

    Application getPayrollApplication() {
        return payrollApplication;
    }

    public int getBranchId() {
        return branchId;
    }
    
}
