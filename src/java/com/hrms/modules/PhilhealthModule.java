/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.beans.PhilhealthBean;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
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
public class PhilhealthModule extends VerticalLayout {
    
    GetSQLQuery query = new GetSQLQuery();
    PhilhealthBean philhealth = new PhilhealthBean();
    GridLayout grid = new GridLayout(2, 4);
    Table philhealthTbl = new Table();
    
    TextField minSal = new TextField("Minimum Salary");
    TextField maxSal = new TextField("Maximum Salary");
    TextField baseSal = new TextField("Base Salary");
    TextField premium = new TextField("Total Monthly Premium");
    TextField eeShare = new TextField("Employee Share");
    TextField erShare = new TextField("Employer Share");    
    
    public PhilhealthModule(){
        
        setSpacing(true);
        grid.setWidth("310px");
        philhealthTbl.setSizeFull();
        philhealthTbl.setPageLength(20);
        philhealthTbl.setSelectable(true);
        philhealthTbl.setImmediate(true);
        
        philhealthTbl.setStyleName("striped");
        philhealthTable(philhealthTbl);
        
        
        /*minSal.setWidth("150px");
        grid.addComponent(minSal, 0, 0);
        
        maxSal.setWidth("150px");
        grid.addComponent(maxSal, 1, 0);
        
        baseSal.setWidth("150px");
        grid.addComponent(baseSal, 0, 1);
        
        premium.setWidth("150px");
        grid.addComponent(premium, 1, 1);
        
        eeShare.setWidth("150px");
        grid.addComponent(eeShare, 0, 2);
        
        erShare.setWidth("150px");
        grid.addComponent(erShare, 1, 2);
        
        Button button = new Button("Save");
        button.setSizeFull();
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                
                boolean result = false, input1, input2, input3, input4, input5, input6;
                input1 = checkInputValue(minSal.getValue().toString());
                input2 = checkInputValue(maxSal.getValue().toString());
                input3 = checkInputValue(baseSal.getValue().toString());
                input4 = checkInputValue(premium.getValue().toString());
                input5 = checkInputValue(eeShare.getValue().toString());
                input6 = checkInputValue(erShare.getValue().toString());
                
                if(input1 == false || input2 == false || input3 == false|| input4 == false || input5 == false || input6 == false){
                    getWindow().showNotification("Wrong entered format!");
                }else{
                    philhealth.setMinSalary(Double.parseDouble(minSal.getValue().toString()));
                    philhealth.setMaxSalary(Double.parseDouble(maxSal.getValue().toString()));
                    philhealth.setBaseSalary(Double.parseDouble(baseSal.getValue().toString()));
                    philhealth.setTotalMonthlyPremium(Double.parseDouble(premium.getValue().toString()));
                    philhealth.setEmployeeShare(Double.parseDouble(eeShare.getValue().toString()));
                    philhealth.setEmployerShare(Double.parseDouble(erShare.getValue().toString()));
                    
                    result = query.saveHMOData(philhealth.getMinSalary(), philhealth.getMaxSalary(), 
                            philhealth.getBaseSalary(), philhealth.getTotalMonthlyPremium(), philhealth.getEmployeeShare(), 
                            philhealth.getEmployerShare());
                    if(result == true){
                        philhealthTable(philhealthTbl);
                        clearTextField();
                    }else{
                        getWindow().showNotification("SQL ERROR!");
                    }
                }
                
            }
            
        });
        grid.addComponent(button, 0, 3, 1, 3); */
        
        //addComponent(grid);
        addComponent(philhealthTbl);
    }
    
    public final Table philhealthTable(Table table){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        table.removeAllItems();
        table.addContainerProperty("Min", String.class, null);
        table.addContainerProperty("Max", String.class, null);
        table.addContainerProperty("Base", String.class, null);
        table.addContainerProperty("Total", Double.class, null);
        table.addContainerProperty("EE Share", Double.class, null);
        table.addContainerProperty("ER Share", Double.class, null);
        try {
            int i = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT * FROM phic_table");
            while(rs.next()){
                table.addItem(new Object[]{formatValue(Double.parseDouble(rs.getString("minSalary"))), 
                    formatValue(Double.parseDouble(rs.getString("maxSalary"))), 
                    formatValue(Double.parseDouble(rs.getString("baseSalary"))), 
                    rs.getString("totalMonthlyPremium"), rs.getString("employeeShare"), rs.getString("employerShare")}, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhilhealthModule.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null | !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PhilhealthModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        table.setPageLength(table.size());
        return table;
    }
    
    public Boolean checkInputValue(String input){
        boolean result = false;
        try{
            Double x = Double.parseDouble(input);
            result = true;
        }catch (NumberFormatException e){
            e.getMessage();
        }
        return result;
    }
    
    public void clearTextField(){
        minSal.setValue("");
        maxSal.setValue("");
        baseSal.setValue("");
        premium.setValue("");
        eeShare.setValue("");
        erShare.setValue("");
    }
    
    public String formatValue(Double val){
        DecimalFormat df = new DecimalFormat( "#,###,##0.00" );
        return df.format(val);
    }
    
}
