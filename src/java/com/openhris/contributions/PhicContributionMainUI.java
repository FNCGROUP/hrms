/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.contributions;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Phic;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.openhris.service.ContributionService;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
public class PhicContributionMainUI extends VerticalLayout {
    
    ContributionService contributionService = new ContributionServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    Table phicTbl = new Table();  
    
    TextField min;
    TextField max;
    TextField base;
    TextField ee;
    TextField er;
    TextField total;
    CheckBox addRowCheckBox = new CheckBox("Check to add new row");
    
    boolean addNewRowToPhicTable = false;
    int rowId;
    
    public PhicContributionMainUI(){
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        
        final VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(60, Sizeable.UNITS_PERCENTAGE);
                
        phicComponentContainer();
        vsplit.setFirstComponent(phicTbl);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        vsplit.setSecondComponent(secondComponentContainer());
    }
    
    public void phicComponentContainer(){
        phicTbl.removeAllItems();
        phicTbl.setSizeFull();
        phicTbl.setSelectable(true);
        phicTbl.setImmediate(true);        
        phicTbl.setStyleName("striped");
        
        phicTbl.addContainerProperty("id", String.class, null);
        phicTbl.addContainerProperty("min", String.class, null);
        phicTbl.addContainerProperty("max", String.class, null);
        phicTbl.addContainerProperty("base", String.class, null);        
        phicTbl.addContainerProperty("ee share", String.class, null);
        phicTbl.addContainerProperty("er share", String.class, null);
        phicTbl.addContainerProperty("total", String.class, null);
        
        List<Phic> phicList = contributionService.getPhicContributionList();
        int i = 0;
        for(Phic p : phicList){
            phicTbl.addItem(new Object[]{
                p.getId(), 
                p.getMinSalary(), 
                p.getMaxSalary(), 
                p.getBaseSalary(), 
                p.getEmployeeShare(), 
                p.getEmployerShare(), 
                p.getTotalMonthlyPremium()
            }, new Integer(i));
            i++;
        }
        
        for(Object listener : phicTbl.getListeners(ItemClickEvent.class)){
            phicTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        phicTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = phicTbl.getItem(itemId);
                
                addRowCheckBox.setValue(false);
                
                if(event.getPropertyId().equals("id")){
                    rowId = Integer.parseInt(item.getItemProperty("id").toString());
                    min.setValue(item.getItemProperty("min").toString());
                    max.setValue(item.getItemProperty("max").toString());
                    base.setValue(item.getItemProperty("base").toString());
                    ee.setValue(item.getItemProperty("ee share").toString());
                    er.setValue(item.getItemProperty("er share").toString());
                    total.setValue(item.getItemProperty("total").toString());
                }
            }
        });
        
    }
    
    public ComponentContainer secondComponentContainer(){
        GridLayout glayout = new GridLayout(3, 3);
        glayout.setMargin(true);
        glayout.setSpacing(true);
        glayout.setWidth(80, Sizeable.UNITS_PERCENTAGE);
        
        min = new TextField("Min: ");
        min.setWidth("100%");
        min.setNullSettingAllowed(false);
        glayout.addComponent(min, 0, 0);
        
        max = new TextField("Max: ");
        max.setWidth("100%");
        max.setNullSettingAllowed(false);
        glayout.addComponent(max, 1, 0);
        
        base = new TextField("Credit: ");
        base.setWidth("100%");
        base.setNullSettingAllowed(false);
        glayout.addComponent(base, 2, 0);
        
        ee = new TextField("Employee's Share: ");
        ee.setWidth("100%");
        ee.setNullSettingAllowed(false);
        glayout.addComponent(ee, 0, 1);
        
        er = new TextField("Employer's Share: ");
        er.setWidth("100%");
        er.setNullSettingAllowed(false);
        glayout.addComponent(er, 1, 1);
        
        total = new TextField("Total: ");
        total.setWidth("100%");
        total.setNullSettingAllowed(false);
        glayout.addComponent(total, 2, 1);
        
        Button updatePhicContributionButton = new Button();
        if(!UserAccessControl.isContributions()){
            updatePhicContributionButton.setCaption("PHIC Table button is Disabled");
            updatePhicContributionButton.setEnabled(UserAccessControl.isContributions());
        } else {
            updatePhicContributionButton.setCaption("UPDATE PHIC TABLE");
            updatePhicContributionButton.setEnabled(UserAccessControl.isContributions());
        }
        updatePhicContributionButton.setWidth("100%");
        updatePhicContributionButton.addListener(updatePhicDataTable);
        glayout.addComponent(updatePhicContributionButton, 1, 2);
        
        Button clearPhicData = new Button("CLEAR TEXT FIELDS");
        clearPhicData.setWidth("100%");
        clearPhicData.addListener(clearTextField);
        glayout.addComponent(clearPhicData, 2, 2);
        
        addRowCheckBox.addListener(addRow);
        addRowCheckBox.setImmediate(true);
        glayout.addComponent(addRowCheckBox, 0, 2);
        
        return glayout;
    }
    
    Button.ClickListener updatePhicDataTable = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if(ee.getValue().toString().trim().isEmpty() || 
                    ee.getValue().toString().trim().isEmpty() || 
                    total.getValue().toString().trim().isEmpty()){
                getWindow().showNotification("Empty Field is not allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                return;
            }
            
            List phicField = new ArrayList();
            phicField.add(min.getValue().toString().trim());
            phicField.add(max.getValue().toString().trim());
            phicField.add(base.getValue().toString().trim());
            phicField.add(ee.getValue().toString().trim());
            phicField.add(er.getValue().toString().trim());
            
            for(int i = 0; i < phicField.size(); i++){
                Boolean checkInputValueForSss = util.checkInputIfDouble(util.removeCommaFromString(phicField.get(i).toString()));
                if(!checkInputValueForSss){
                    getWindow().showNotification("Please enter a numerical value!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
            }
            
            List<Phic> phicList = new ArrayList<Phic>();
            Phic phic = new Phic();
            phic.setMinSalary(util.convertStringToDouble(min.getValue().toString().trim()));
            phic.setMaxSalary(util.convertStringToDouble(max.getValue().toString().trim()));
            phic.setBaseSalary(util.convertStringToDouble(base.getValue().toString().trim()));
            phic.setEmployeeShare(util.convertStringToDouble(util.removeCommaFromString(ee.getValue().toString().trim())));
            phic.setEmployerShare(util.convertStringToDouble(util.removeCommaFromString(er.getValue().toString().trim())));
            phic.setTotalMonthlyPremium(util.convertStringToDouble(util.removeCommaFromString(ee.getValue().toString().trim())) + 
                    util.convertStringToDouble(util.removeCommaFromString(er.getValue().toString().trim())));
            phic.id = rowId;
            phicList.add(phic);
            
            boolean result;
            if(!addNewRowToPhicTable){
                if(rowId == 0){
                    getWindow().showNotification("Click id column to edit row Data!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;  
                }
                result = contributionService.updatePhicTableData(phicList);
            } else {    
                double maxSalary = contributionService.getPhicTableLastRowMaxSalary();
//                if(phic.getMinSalary() < maxSalary){
//                    getWindow().showNotification("Max Salary of Phic Table is greater than the new entered Min Salary!", Window.Notification.TYPE_ERROR_MESSAGE);
//                    return;
//                }
                result = contributionService.insertNewPhicData(phicList);
            }
            
            if(result){
                phicComponentContainer();
                getWindow().showNotification("Successfully update Phic Table!");
                clearTextFields();
                addNewRowToPhicTable = false;
            }else{
                getWindow().showNotification("Cannot update Phic Table!", Window.Notification.TYPE_ERROR_MESSAGE);                
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
            addNewRowToPhicTable = event.getButton().booleanValue();
            clearTextFields();
            rowId = 0;
        }
    };
    
    public void clearTextFields(){
        min.setValue("");
        max.setValue("");
        base.setValue("");
        ee.setValue("");
        er.setValue("");
        total.setValue("");
    }
}
