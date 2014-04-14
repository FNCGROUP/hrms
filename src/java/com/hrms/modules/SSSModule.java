/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.beans.SSSBean;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
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
public class SSSModule extends VerticalLayout {
    
    GetSQLQuery query = new GetSQLQuery();
    SSSBean sss = new SSSBean();
    GridLayout grid = new GridLayout(2, 4);
    Table sssTbl = new Table();
    
    TextField minSal = new TextField("Minimum Salary");
    TextField maxSal = new TextField("Maximum Salary");
    TextField salCredit = new TextField("Monthly Salary Credit");
    TextField totalContribution = new TextField("Total Monthly Contribution");
    TextField eeShare = new TextField("Employee Share");
    TextField erShare = new TextField("Employer Share");
    
    public SSSModule(){
        
        setSpacing(true);
        grid.setWidth("310px");
        sssTbl.setSizeFull();
        sssTbl.setPageLength(20);
        sssTbl.setSelectable(true);
        sssTbl.setImmediate(true);
        
        sssTbl.setStyleName("striped");
        sssTable(sssTbl);
        
        
        minSal.setWidth("150px");
        grid.addComponent(minSal, 0, 0);
        
        maxSal.setWidth("150px");
        grid.addComponent(maxSal, 1, 0);
        
        salCredit.setWidth("150px");
        grid.addComponent(salCredit, 0, 1);
        
        totalContribution.setWidth("150px");
        grid.addComponent(totalContribution, 1, 1);
        
        eeShare.setWidth("150px");
        grid.addComponent(eeShare, 0, 2);
        
        erShare.setWidth("150px");
        grid.addComponent(erShare, 1, 2);
        
        Button button = new Button("Save");
        button.setSizeFull();
        button.addListener(new Button.ClickListener(){

            @Override
            public void buttonClick(ClickEvent event) {
                
                boolean result = false, input1, input2, input3, input4, input5, input6;
                input1 = checkInputValue(minSal.getValue().toString());
                input2 = checkInputValue(maxSal.getValue().toString());
                input3 = checkInputValue(salCredit.getValue().toString());
                //input4 = checkInputValue(totalContribution.getValue().toString());
                input5 = checkInputValue(eeShare.getValue().toString());
                input6 = checkInputValue(erShare.getValue().toString());
                
                if(input1 == false || input2 == false || input3 == false|| input5 == false || input6 == false){
                    getWindow().showNotification("Wrong entered format!");
                }else{
                    sss.setMinSalary(Double.parseDouble(minSal.getValue().toString()));
                    sss.setMaxSalary(Double.parseDouble(maxSal.getValue().toString()));
                    sss.setMonthlySalaryCredit(Double.parseDouble(salCredit.getValue().toString()));                    
                    sss.setEmployeeContribution(Double.parseDouble(eeShare.getValue().toString()));
                    sss.setEmployerContribution(Double.parseDouble(erShare.getValue().toString()));
                    double total = sss.getEmployeeContribution()+sss.getEmployerContribution();
                    sss.setTotalMonthlyContribution(total);
                    
                    result = query.saveSSSData(sss.getMinSalary(), sss.getMaxSalary(), sss.getMonthlySalaryCredit(), 
                            sss.getEmployeeContribution(), sss.getEmployerContribution(), sss.getTotalMonthlyContribution());
                    if(result == true){
                        sssTable(sssTbl);
                        clearTextField();
                    }else{
                        getWindow().showNotification("SQL ERROR!");
                    }
                }
                
            }
            
        });
        grid.addComponent(button, 0, 3, 1, 3);
        
        //addComponent(grid);
        addComponent(sssTbl);
    }
    
    public final Table sssTable(Table table){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        table.removeAllItems();
        table.addContainerProperty("Min", String.class, null);
        table.addContainerProperty("Max", String.class, null);
        table.addContainerProperty("Credit", String.class, null);
        table.addContainerProperty("Total",String.class, null);
        table.addContainerProperty("EE Contribution", Double.class, null);
        table.addContainerProperty("ER Contribution", String.class, null);
        try {
            int i = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT * FROM sss_table");
            while(rs.next()){
                table.addItem(new Object[]{formatValue(Double.parseDouble(rs.getString("minSalary"))), 
                    formatValue(Double.parseDouble(rs.getString("maxSalary"))), 
                    formatValue(Double.parseDouble(rs.getString("monthlySalaryCredit"))), 
                    formatValue(Double.parseDouble(rs.getString("totalContribution"))), 
                    rs.getString("employeeContribution"), 
                    formatValue(Double.parseDouble(rs.getString("employerContribution")))}, 
                        new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhilhealthModule.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(SSSModule.class.getName()).log(Level.SEVERE, null, ex);
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
        salCredit.setValue("");
        totalContribution.setValue("");
        eeShare.setValue("");
        erShare.setValue("");
    }
    
    public String formatValue(Double val){
        DecimalFormat df = new DecimalFormat( "#,###,##0.00" );
        return df.format(val);
    }
    
}
