/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.contributions;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.contributions.model.Sss;
import com.openhris.contributions.serviceprovider.ContributionServiceImpl;
import com.openhris.service.ContributionService;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jet
 */
public class SssContributionMainUI extends VerticalLayout{
    
    ContributionService contributionService = new ContributionServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    Table sssTbl = new Table();
    
    TextField min;
    TextField max;
    TextField credit;
    TextField ee;
    TextField er;
    TextField total;
    CheckBox addRowCheckBox = new CheckBox("Check to add new row");
        
    Boolean addNewRowToSssTable = false;
    int rowId;
    
    public SssContributionMainUI(){
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        
        VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(70, Sizeable.UNITS_PERCENTAGE);
        
        sssComponentContainer();
        vsplit.setFirstComponent(sssTbl);
        
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        vsplit.setSecondComponent(sssSecondComponentContainer());        
    }
    
    public void sssComponentContainer(){        
        sssTbl.removeAllItems();
        sssTbl.setSizeFull();
        sssTbl.setSelectable(true);
        sssTbl.setImmediate(true);        
        sssTbl.setStyleName("striped");
        
        sssTbl.addContainerProperty("id", Integer.class, null);
        sssTbl.addContainerProperty("min", Double.class, null);
        sssTbl.addContainerProperty("max", Double.class, null);
        sssTbl.addContainerProperty("credit", Double.class, null);        
        sssTbl.addContainerProperty("ee contribution", Double.class, null);
        sssTbl.addContainerProperty("er contribution", Double.class, null);
        sssTbl.addContainerProperty("total", Double.class, null);
        
        List<Sss> sssList = contributionService.getSssContributionList();
        int i = 0;
        for(Sss s: sssList){
            sssTbl.addItem(new Object[]{                
                s.getId(), 
                s.getMinSalary(), 
                s.getMaxSalary(), 
                s.getMonthlySalaryCredit(), 
                s.getEmployeeContribution(), 
                s.getEmployerContribution(), 
                s.getTotalMontlyContribution()
            }, new Integer(i));
            i++;
        }
        sssTbl.setPageLength(sssTbl.size());
        
        for(Object listener : sssTbl.getListeners(ItemClickEvent.class)){
            sssTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        sssTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = sssTbl.getItem(itemId);
                
                addRowCheckBox.setValue(false);
                
                if(event.getPropertyId().equals("id")){
                    rowId = Integer.parseInt(item.getItemProperty("id").toString());
                    min.setValue(item.getItemProperty("min").toString());
                    max.setValue(item.getItemProperty("max").toString());
                    credit.setValue(item.getItemProperty("credit").toString());
                    ee.setValue(item.getItemProperty("ee contribution").toString());
                    er.setValue(item.getItemProperty("er contribution").toString());
                    total.setValue(item.getItemProperty("total").toString());
                }
            }
        });        
    }
    
    private ComponentContainer sssSecondComponentContainer(){
        GridLayout glayout = new GridLayout(3, 3);
        glayout.setMargin(true);
        glayout.setSpacing(true);
        glayout.setWidth(80, Sizeable.UNITS_PERCENTAGE);
        
        min = new TextField("Min: ");
        min.setWidth("100%");
        glayout.addComponent(min, 0, 0);
        
        max = new TextField("Max: ");
        max.setWidth("100%");
        glayout.addComponent(max, 1, 0);
        
        credit = new TextField("Credit: ");
        credit.setWidth("100%");
        glayout.addComponent(credit, 2, 0);
        
        ee = new TextField("Employee's Contribution: ");
        ee.setWidth("100%");
        ee.setNullSettingAllowed(false);
        glayout.addComponent(ee, 0, 1);
        
        er = new TextField("Employer's Contribution: ");
        er.setWidth("100%");
        er.setNullSettingAllowed(false);
        glayout.addComponent(er, 1, 1);
        
        total = new TextField("Total: ");
        total.setWidth("100%");
        glayout.addComponent(total, 2, 1);
        
        Button updatePhicContributionButton = new Button();
        if(!UserAccessControl.isContributions()){
            updatePhicContributionButton.setCaption("SSS Button is Disabled");
            updatePhicContributionButton.setEnabled(UserAccessControl.isContributions());
        } else {
            updatePhicContributionButton.setCaption("UPDATE SSS TABLE");
            updatePhicContributionButton.setEnabled(UserAccessControl.isContributions());
        }
        updatePhicContributionButton.setWidth("100%");
        updatePhicContributionButton.addListener(updateSssTableData);
        updatePhicContributionButton.setImmediate(true);
        glayout.addComponent(updatePhicContributionButton, 1, 2);
        
        Button clearPhicData = new Button("CLEAR TEXT FIELDS");
        clearPhicData.setWidth("100%");
        clearPhicData.addListener(clearTextField);
        clearPhicData.setImmediate(true);
        glayout.addComponent(clearPhicData, 2, 2);
                
        addRowCheckBox.addListener(addRow);
        addRowCheckBox.setImmediate(true);
        glayout.addComponent(addRowCheckBox, 0, 2);
        
        return glayout;
    }
    
    Button.ClickListener updateSssTableData = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if(ee.getValue().toString().trim().isEmpty() || 
                    er.getValue().toString().isEmpty()){
                getWindow().showNotification("Empty Field is not Allowed!", Window.Notification.TYPE_ERROR_MESSAGE);
                return;
            }
            
            List sssField = new ArrayList();
            sssField.add(ee.getValue().toString().trim());
            sssField.add(er.getValue().toString().trim());
            sssField.add(min.getValue().toString().trim());
            sssField.add(max.getValue().toString().trim());
            sssField.add(credit.getValue().toString().trim());
            
            for(int i = 0; i < sssField.size(); i++){
                Boolean checkInputValueForSss = util.checkInputIfDouble(util.removeCommaFromString(sssField.get(i).toString()));
                if(!checkInputValueForSss){
                    getWindow().showNotification("Please enter a numerical value!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
            }
            
            List<Sss> sssList = new ArrayList<Sss>();
            Sss sss = new Sss();
            sss.setMinSalary(util.convertStringToDouble(min.getValue().toString().trim()));
            sss.setMaxSalary(util.convertStringToDouble(max.getValue().toString().trim()));
            sss.setMonthlySalaryCredit(util.convertStringToDouble(credit.getValue().toString().trim()));
            sss.setEmployeeContribution(util.convertStringToDouble(util.removeCommaFromString(ee.getValue().toString().trim())));
            sss.setEmployerContribution(util.convertStringToDouble(util.removeCommaFromString(er.getValue().toString().trim())));
            sss.setTotalMontlyContribution(util.convertStringToDouble(util.removeCommaFromString(ee.getValue().toString().trim())) + 
                    util.convertStringToDouble(util.removeCommaFromString(er.getValue().toString().trim())));
            sss.setId(rowId);                          
            sssList.add(sss);
                        
            boolean result;
            if(!addNewRowToSssTable){    
                if(rowId == 0){
                    getWindow().showNotification("Click id column to edit row Data!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;  
                }
                result = contributionService.updateSssTableData(sssList);
            } else {
                double maxSalary = contributionService.getSssTableLastRowMaxSalary();
//                if(sss.getMinSalary() < maxSalary){
//                    getWindow().showNotification("Max Salary of SSS Table is greater than the new entered Min Salary!", Window.Notification.TYPE_ERROR_MESSAGE);
//                    return;
//                }
                result = contributionService.insertNewSssData(sssList);
            }
            if(result){
                sssComponentContainer();
                getWindow().showNotification("Successfully update SSS Table");
                clearTextFields();
                addNewRowToSssTable = false;
            }else{
                getWindow().showNotification("Cannot update SSS Table", Window.Notification.TYPE_WARNING_MESSAGE);
            }
        }
    };
    
    Button.ClickListener clearTextField = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            clearTextFields();
            addRowCheckBox.setValue(false);
        }
    };
    
    Button.ClickListener addRow = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            addNewRowToSssTable = event.getButton().booleanValue();
            rowId = 0;
            clearTextFields();
        }
    };
    
    public void clearTextFields(){
        min.setValue("");
        max.setValue("");
        credit.setValue("");
        ee.setValue("");
        er.setValue("");
        total.setValue("");
    }
}
