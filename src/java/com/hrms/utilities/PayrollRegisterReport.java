/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.utilities;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author jet
 */
public class PayrollRegisterReport extends VerticalLayout {
        
    public void processPayrollRegisterReport(){
        GetSQLConnection getConnection = new GetSQLConnection();
        final Connection conn = getConnection.connection();
            
        File reportFile = new File("reports/sampleReport.jasper");
        final HashMap hm = new HashMap();
        
        try {
            //final JasperReport report = (JasperReport) JRLoader.loadObject(reportFile.getAbsoluteFile());
            
            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    byte[] b = null;
                    try {
                        b = JasperRunManager.runReportToPdf(getClass().getClassLoader().getResourceAsStream("reports/sampleReport.jasper"), hm, conn);
                    } catch (JRException ex) {
                        System.out.println(ex);
                    }
                    return new ByteArrayInputStream(b);
                }
            };
            
            StreamResource resource = new StreamResource(source, "sampleReport.pdf", getApplication());
            resource.setMIMEType("application/pdf");
            getApplication().getMainWindow().open(resource, "_new");
        } catch (Exception ex) {
            Logger.getLogger(PayrollRegisterReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
