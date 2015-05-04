/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.contributions;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Tax;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.openhris.service.ContributionService;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
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
public class TaxContributionMainUI extends VerticalLayout {
    
    ContributionService contributionService = new ContributionServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();    
    Table taxTbl = new Table();    
    
    Boolean updateTaxTable = false;
    Integer rowId;
    
    Label status;
    TextField col1;
    TextField col2;
    TextField col3;
    TextField col4;
    TextField col5;
    TextField col6;
    TextField col7;
    
    public TaxContributionMainUI(){
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
        
        vsplit.setSplitPosition(30, Sizeable.UNITS_PERCENTAGE);
        
        taxTbl.setSizeFull();
        taxTbl.setSelectable(true);
        taxTbl.setImmediate(true);        
        taxTbl.setStyleName("striped");
        
        taxComponentContainer();
        vsplit.setFirstComponent(taxTbl);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);        
        
        vsplit.setSecondComponent(secondComponentContainer());
    }
    
    public void taxComponentContainer(){
        taxTbl.removeAllItems();
        taxTbl.setSizeFull();
        taxTbl.setSelectable(true);
        taxTbl.setImmediate(true);        
        taxTbl.setStyleName("striped");
        
        taxTbl.addContainerProperty("id", String.class, null);
        taxTbl.addContainerProperty("status", String.class, null);
        taxTbl.addContainerProperty("1", String.class, null);
        taxTbl.addContainerProperty("2", String.class, null);
        taxTbl.addContainerProperty("3", String.class, null);
        taxTbl.addContainerProperty("4", String.class, null);
        taxTbl.addContainerProperty("5", String.class, null);
        taxTbl.addContainerProperty("6", String.class, null);
        taxTbl.addContainerProperty("7", String.class, null);
        
        List<Tax> taxList = contributionService.getTaxContributionList();
        int i = 0;
        for(Tax t : taxList){
            taxTbl.addItem(new Object[]{
                t.getId(), 
                t.getStatus(), 
                t.getTaxRate1(), 
                t.getTaxRate2(), 
                t.getTaxRate3(), 
                t.getTaxRate4(), 
                t.getTaxRate5(), 
                t.getTaxRate6(), 
                t.getTaxRate7()
            }, new Integer(i));
            i++;
        }
        
        for(Object listener : taxTbl.getListeners(ItemClickEvent.class)){
            taxTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        taxTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = taxTbl.getItem(itemId);                
                
                if(event.getPropertyId().equals("id")){
                    updateTaxTable = true;
                    rowId = Integer.parseInt(item.getItemProperty("id").toString());
                    status.setCaption("Status: "+item.getItemProperty("status").toString());
                    col1.setValue(item.getItemProperty("1").toString());
                    col2.setValue(item.getItemProperty("2").toString());
                    col3.setValue(item.getItemProperty("3").toString());
                    col4.setValue(item.getItemProperty("4").toString());
                    col5.setValue(item.getItemProperty("5").toString());
                    col6.setValue(item.getItemProperty("6").toString());
                    col7.setValue(item.getItemProperty("7").toString());
                }
            }
        });
    }
    
    public ComponentContainer secondComponentContainer(){
        GridLayout glayout = new GridLayout(4, 4);
        glayout.setMargin(true);
        glayout.setSpacing(true);
        glayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        
        status = new Label();
        status.setCaption("Status: ");
        glayout.addComponent(status, 0, 0);
                
        col1 = new TextField("Column 1: ");
        col1.setWidth("100%");
        col1.setNullSettingAllowed(false);
        glayout.addComponent(col1, 0, 1);
        
        col2 = new TextField("Column 2: ");
        col2.setWidth("100%");
        col2.setNullSettingAllowed(false);
        glayout.addComponent(col2, 1, 1);
        
        col3 = new TextField("Column 3: ");
        col3.setWidth("100%");
        col3.setNullSettingAllowed(false);
        glayout.addComponent(col3, 2, 1);
        
        col4 = new TextField("Column 4: ");
        col4.setWidth("100%");
        col4.setNullSettingAllowed(false);
        glayout.addComponent(col4, 3, 1);
        
        col5 = new TextField("Column 5: ");
        col5.setWidth("100%");
        col5.setNullSettingAllowed(false);
        glayout.addComponent(col5, 0, 2);
        
        col6 = new TextField("Column 6: ");
        col6.setWidth("100%");
        col6.setNullSettingAllowed(false);
        glayout.addComponent(col6, 1, 2);
        
        col7 = new TextField("Column 7: ");
        col7.setWidth("100%");
        col7.setNullSettingAllowed(false);
        glayout.addComponent(col7, 2, 2);
        
        Button update = new Button();
        if(!UserAccessControl.isContributions()){
            update.setCaption("TAX Button is Disabled");
            update.setEnabled(UserAccessControl.isContributions());
        } else {
            update.setCaption("UPDATE TAX TABLE");
            update.setEnabled(UserAccessControl.isContributions());
        }
        update.setWidth("100%");
        update.addListener(updateTaxDataTable);
        glayout.addComponent(update, 3, 2);
        glayout.setComponentAlignment(update, Alignment.BOTTOM_CENTER);
        
        Button cancel = new Button("CLEAR TEXT FIELDS");
        cancel.setWidth("100%");
        cancel.addListener(clearTextField);
        glayout.addComponent(cancel, 3, 3);
        glayout.setComponentAlignment(cancel, Alignment.BOTTOM_CENTER);
        
        return glayout;
    }
    
    Button.ClickListener updateTaxDataTable = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if(col1.getValue().toString().trim().isEmpty() || 
                    col2.getValue().toString().trim().isEmpty() || 
                    col3.getValue().toString().trim().isEmpty() || 
                    col4.getValue().toString().trim().isEmpty() || 
                    col5.getValue().toString().trim().isEmpty() || 
                    col6.getValue().toString().trim().isEmpty() || 
                    col7.getValue().toString().trim().isEmpty() ){
                getWindow().showNotification("Empty Field is not Allowed!", Window.Notification.TYPE_ERROR_MESSAGE);
                return;
            }
            
            List taxRates = new ArrayList();
            taxRates.add(col1.getValue().toString().trim());
            taxRates.add(col2.getValue().toString().trim());
            taxRates.add(col3.getValue().toString().trim());
            taxRates.add(col4.getValue().toString().trim());
            taxRates.add(col5.getValue().toString().trim());
            taxRates.add(col6.getValue().toString().trim());
            taxRates.add(col7.getValue().toString().trim());
            
            for(int i = 0; i < taxRates.size(); i++){
                Boolean checkInputValueForTaxRate = util.checkInputIfDouble(util.removeCommaFromString(taxRates.get(i).toString()));
                if(!checkInputValueForTaxRate){
                    getWindow().showNotification("Please enter a numerical value!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
            }
            
            List<Tax> taxList = new ArrayList<Tax>();
            Tax tax = new Tax();
            tax.setTaxRate1(Double.parseDouble(util.removeCommaFromString(col1.getValue().toString().trim())));
            tax.setTaxRate2(Double.parseDouble(util.removeCommaFromString(col2.getValue().toString().trim())));
            tax.setTaxRate3(Double.parseDouble(util.removeCommaFromString(col3.getValue().toString().trim())));
            tax.setTaxRate4(Double.parseDouble(util.removeCommaFromString(col4.getValue().toString().trim())));
            tax.setTaxRate5(Double.parseDouble(util.removeCommaFromString(col5.getValue().toString().trim())));
            tax.setTaxRate6(Double.parseDouble(util.removeCommaFromString(col6.getValue().toString().trim())));
            tax.setTaxRate7(Double.parseDouble(util.removeCommaFromString(col7.getValue().toString().trim())));
            tax.setId(rowId);
            taxList.add(tax);
            
            boolean result = contributionService.updateTaxTableData(taxList);
            if(result){
                getWindow().showNotification("Successfully update Tax Table!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                taxComponentContainer();
                clearTextFields();
            }else{
                getWindow().showNotification("Cannot update Tax Table!", Window.Notification.TYPE_ERROR_MESSAGE);
            }
        }
    };
    
    Button.ClickListener clearTextField = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            clearTextFields();
        }
    };
    
    private void clearTextFields(){
        col1.setValue("");
        col2.setValue("");
        col3.setValue("");
        col4.setValue("");
        col5.setValue("");
        col6.setValue("");
        col7.setValue("");
    }
    
}
