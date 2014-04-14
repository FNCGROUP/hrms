/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

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
 * @author jet
 */
public class PayrollReports extends VerticalLayout {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    Connection conn = getConnection.connection();
    
    public void payslipReport(Integer branchId, String date){
        File reportFile = new File("C:/reportsJasper/PayslipReport.jasper");
        //File reportFile = new File("C:\\reports\\payrollRegisterReport.jasper");

        final HashMap hm = new HashMap();
        hm.put("BRANCH_ID", branchId);
        hm.put("PAYROLL_DATE", date);

        try{
            JasperPrint jpReport = JasperFillManager.fillReport(reportFile.getAbsolutePath(), hm, conn);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timestamp = df.format(new Date());
            final String filePath = "C:/reportsPdf/"+timestamp+"PayslipReport.pdf";
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
    
}
