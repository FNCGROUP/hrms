/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.EmployeesListPerBranch;
import com.hrms.classes.TradeName;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;

/**
 *
 * @author jet
 */
public class LoansModule extends VerticalLayout {
    
    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities convertUtil = new ConvertionUtilities();
    CorporateName corporateNames = new CorporateName();
    TradeName tradeNames = new TradeName();
    BranchName branchNames = new BranchName();
    EmployeesListPerBranch employeesListPerBranch = new EmployeesListPerBranch();
    NativeSelect corporateName;
    NativeSelect tradeName;
    NativeSelect branchName;
    NativeSelect employeesName;
    
    Table loansTbl;
        
    private Integer branchId;
    private String employeeId;
    private String userRole;
    private Integer userId;
    
    public LoansModule(String userRole){
        
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
        
        vsplit.setSplitPosition(24, Sizeable.UNITS_PERCENTAGE);
        
        GridLayout comboBoxGrid = new GridLayout(3, 3);
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
                    branchId = query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), 
                            corporateName.getValue().toString());
                }                
            }
            
        });
        comboBoxGrid.addComponent(branchName, 2, 0);
        
        employeesName = new NativeSelect("Select Employee: ");
        employeesName.setWidth("270px");
        branchName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branchName.getValue() == null){
                    getWindow().showNotification("Select a Branch!");
                }else{
                    employeesListPerBranch.getEmployeesListPerBranch(employeesName, 
                            query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), 
                            corporateName.getValue().toString()));
                }
            }
            
        });
        employeesName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                employeeId = query.getEmployeesId(employeesName.getValue().toString());
            }
            
        });
        comboBoxGrid.addComponent(employeesName, 0, 1);
        
        Button newLoansButton = new Button("Add New Loan");
        newLoansButton.setWidth("270px");
        comboBoxGrid.addComponent(newLoansButton, 1, 1);
        comboBoxGrid.setComponentAlignment(newLoansButton, Alignment.BOTTOM_LEFT);
        
    }
    
    
    
}
