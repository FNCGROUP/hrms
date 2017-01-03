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
public class PreviousPayslipReport extends Window {
    GetSQLConnection getConnection = new GetSQLConnection();
    private int branchId;
    private String payrollDate;
        
//    File file;
    String file;
    Application payrollApplication;

    public PreviousPayslipReport(int branchId, String payrollDate, Application payrollApplication) {
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        this.payrollApplication = payrollApplication;
        
        setCaption("Previous Payslip Report");
        setSizeFull();
        setWidth("800px");
        setHeight("600px");
        center();
        
        Connection conn = getConnection.connection();
//        URL url = this.getClass().getResource("/com/openhris/reports/PayslipReport.jasper");
        File reportFile = new File("C:/reportsJasper/AdjustedPayslip.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", getBranchId());
        hm.put("PAYROLL_DATE", getPayrollDate());

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getPath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
//             file = File.createTempFile("PayslipReport_"+timestamp, ".pdf");
             file = "C:/reportsPdf/AdjustedPayslip_"+timestamp+".pdf";
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

    private int getBranchId() {
        return branchId;
    }

    private String getPayrollDate() {
        return payrollDate;
    }

    private Application getPayrollApplication() {
        return payrollApplication;
    }
    
}
