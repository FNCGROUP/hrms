/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class TaxTableModule extends VerticalLayout {
    
    Table taxTbl = new Table();
    
    public TaxTableModule(){
        
        setSpacing(true);
        taxTbl.setSizeFull();
        taxTbl.setSelectable(true);
        taxTbl.setImmediate(true);
        
        taxTbl.setStyleName("striped");
        taxTable(taxTbl);
        addComponent(taxTbl);
        
    }
    
    private Table taxTable(Table table){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        ResultSet rs = null;
        Statement stmt = null;
        table.removeAllItems();
        table.addContainerProperty("Status", String.class, null);
        table.addContainerProperty("1", String.class, null);
        table.addContainerProperty("2", String.class, null);
        table.addContainerProperty("3", String.class, null);
        table.addContainerProperty("4", String.class, null);
        table.addContainerProperty("5", String.class, null);
        table.addContainerProperty("6", String.class, null);
        table.addContainerProperty("7", String.class, null);
        try {
            int i = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT * FROM tax_table ");
            while(rs.next()){
                table.addItem(new Object[]{rs.getString("status"), 
                    formatValue(Double.parseDouble(rs.getString("taxRate1"))), 
                    formatValue(Double.parseDouble(rs.getString("taxRate2"))), 
                    formatValue(Double.parseDouble(rs.getString("taxRate3"))), 
                    formatValue(Double.parseDouble(rs.getString("taxRate4"))), 
                    formatValue(Double.parseDouble(rs.getString("taxRate5"))), 
                    formatValue(Double.parseDouble(rs.getString("taxRate6"))), 
                    formatValue(Double.parseDouble(rs.getString("taxRate7")))}, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaxTableModule.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
                stmt.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(TaxTableModule.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        table.setPageLength(table.size());        
        return table;
    }
    
    private String formatValue(Double val){
        DecimalFormat df = new DecimalFormat( "#,###,##0.00" );
        return df.format(val);
    }
    
    private String formatToPercentage(Double val){
        double percent = val * 100;
        String percentage = "+"+val.toString()+"%"+" over";
        return percentage;
    }
}
