/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping.reports;

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
public class IndividualAttendanceReport extends Window {

    GetSQLConnection getConnection = new GetSQLConnection();
    private int payrollId;
        
    String filePath;
    Application attendanceApplication;
    
    public IndividualAttendanceReport(int payrollId, Application attendanceApplication) {
        this.payrollId = payrollId;
        this.attendanceApplication = attendanceApplication;
        
        setCaption("Attendance Report");
        setSizeFull();
        setWidth("800px");
        setHeight("600px");
        center();
        
        Connection conn = getConnection.connection();
        File reportFile = new File("C:/reportsJasper/IndividualAttendanceReport.jasper");
        
        final HashMap hm = new HashMap();
        hm.put("PAYROLL_ID", getPayrollId());

        try{
             JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
             SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
             String timestamp = df.format(new Date());
             filePath = "C:/reportsPdf/IndividualAttendanceReport_"+timestamp+".pdf";
             JasperExportManager.exportReportToPdfFile(jpReport, filePath);             
        }catch(Exception e){
             e.getMessage();
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

        StreamResource resource = new StreamResource(source, filePath, getAttendanceApplication());
        resource.setMIMEType("application/pdf");       

        Embedded e = new Embedded();
        e.setMimeType("application/pdf");
        e.setType(Embedded.TYPE_OBJECT);
        e.setSizeFull();
        e.setSource(resource);
        e.setParameter("Content-Disposition", "attachment; filename=" + resource.getFilename());

        addComponent(e);
        getAttendanceApplication().getMainWindow().open(resource, "_blank");
    }
    
    int getPayrollId(){
        return payrollId;
    }
    
    Application getAttendanceApplication(){
        return attendanceApplication;
    }
}
