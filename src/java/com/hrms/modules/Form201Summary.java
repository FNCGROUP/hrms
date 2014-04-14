/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.TradeName;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class Form201Summary extends VerticalLayout {
    
    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities convertUtil = new ConvertionUtilities();
    CorporateName corporateNames = new CorporateName();
    TradeName tradeNames = new TradeName();
    BranchName branchNames = new BranchName();
    NativeSelect corporateName;
    NativeSelect tradeName;
    NativeSelect branchName;
    Table form201 = new Table();
    
    private String userRole;
    
    Integer branchId;
    
    public Form201Summary(String userRole){
        
        this.userRole = userRole;
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        
        VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(15, Sizeable.UNITS_PERCENTAGE);
        
        GridLayout comboBoxGrid = new GridLayout(3, 1);
        comboBoxGrid.setMargin(true);
        comboBoxGrid.setSpacing(true);
        comboBoxGrid.setWidth(90, Sizeable.UNITS_PERCENTAGE);
        
        vsplit.setFirstComponent(comboBoxGrid);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        corporateName = new NativeSelect("Select Corporate Name:");
        corporateName.setWidth("270px");
        corporateNames.getCorporateName(corporateName);
        comboBoxGrid.addComponent(corporateName, 0, 0);
        
        tradeName = new NativeSelect("Select Trade Name:"); 
        tradeName.setWidth("270px");
        corporateName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                tradeNames.getTradeName(tradeName, corporateName.getValue().toString());
            }
            
        });        
        comboBoxGrid.addComponent(tradeName, 1, 0);
        
        branchName = new NativeSelect("Select Branch:");
        branchName.setWidth("270px");        
        tradeName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tradeName.getValue() == null){                    
                }else{
                    branchNames.getBranchName(branchName, tradeName.getValue().toString(), corporateName.getValue().toString());
                }                
            }
        });
        branchName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branchName.getValue() == null){                    
                }else{
                    branchId = query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), corporateName.getValue().toString());
                    newForm201Table();
                }                
            }
            
        });
        comboBoxGrid.addComponent(branchName, 2, 0);
        
        vsplit.setSecondComponent(form201);
    }
    
    
    public void newForm201Table(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        form201.removeAllItems();
        form201.setSizeFull();
        form201.setImmediate(true);
        form201.setSelectable(true);
        
        form201.addContainerProperty("name", String.class, null);
        form201.addContainerProperty("sss", String.class, null);
        form201.addContainerProperty("tin", String.class, null);
        form201.addContainerProperty("phic", String.class, null);
        form201.addContainerProperty("hdmf", String.class, null);
        form201.addContainerProperty("status", String.class, null);
        form201.addContainerProperty("wage", String.class, null);
        form201.addContainerProperty("wage status", String.class, null);
        form201.addContainerProperty("wage entry", String.class, null);
        form201.addContainerProperty("allowance", String.class, null);
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT concat_ws(', ',lastname,concat_ws(' ',firstname,left(middlename,1))) AS name, "
                    + "sssNo AS sss, tinNo AS tin, philhealthNo AS phic, hdmfNo AS hdmf, employmentStatus AS status, employmentWage AS wage, "
                    + "employmentWageStatus AS wageStatus, employmentWageEntry AS wageEntry, allowanceEntry AS allowance FROM employee "
                    + "WHERE branchId  = '"+branchId+"' AND (rowStatus = 'resigned' OR rowStatus IS NULL) ORDER BY lastname ASC ");
            while(rs.next()){
                form201.addItem(new Object[]{ 
                    rs.getString("name").toUpperCase(), 
                    rs.getString("sss"), 
                    rs.getString("tin"), 
                    rs.getString("phic"), 
                    rs.getString("hdmf"), 
                    rs.getString("status").toUpperCase(), 
                    rs.getString("wage"), 
                    rs.getString("wageStatus").toUpperCase(), 
                    rs.getString("wageEntry").toUpperCase(), 
                    rs.getString("allowance")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Form201Summary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
