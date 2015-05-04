/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Adjustment;
import com.openhris.service.PayrollService;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class AdjustmentWindow extends Window {

    PayrollService payrollService = new PayrollServiceImpl();
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    
    private int payrollId;
    private double amountToBeReceive;
    private double amountReceived;
    private double adjustment;
    
    private Table adjustmentTbl = new Table();
    
    public AdjustmentWindow(int payrollId, 
            double amountToBeReceive, 
            double amountReceived, 
            double adjustment) {
        this.payrollId = payrollId;
        this.amountToBeReceive = amountToBeReceive;
        this.amountReceived = amountReceived;
        this.adjustment = adjustment;
        
        setCaption("ADJUSTMENTS");
        setWidth("400px");
        
        TabSheet ts = new TabSheet();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Post Adjustments");
        
        final TextField amount = new TextField("Amount: ");
        amount.setWidth("100%");
        vlayout.addComponent(amount);
        
        final TextField remarks = new TextField("Remarks");
        remarks.setWidth("100%");
        vlayout.addComponent(remarks);
        
        Button saveAdjustments = new Button("POST ADJUSTMENTS");
        saveAdjustments.setWidth("100%");
        saveAdjustments.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(amount.getValue() == null || amount.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Amount for adjustment.", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                } else {
                    if(!utilities.checkInputIfDouble(amount.getValue().toString().trim())){
                        getWindow().showNotification("Enter a numeric value for amount.", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add remarks for adjustment.", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                double amountForAdjustment = utilities.convertStringToDouble(amount.getValue().toString().trim());
                String remarksForAdjustment = remarks.getValue().toString().trim().toLowerCase();
                boolean result = payrollService.insertAdjustmentToPayroll(getPayrollId(), 
                        getAmountToBeReceive(), 
                        getAmountReceived(), 
                        amountForAdjustment, 
                        remarksForAdjustment);
                if(result){
                    adjustmentTable();
                    close();
                    getWindow().showNotification("Successfully added adjustment.", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                }
            }
        });
        vlayout.addComponent(saveAdjustments);
        
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Adjustments Table");
        
        Label label = new Label("Remarks: Click ID Column to delete Adjustment");
        vlayout.addComponent(label);
        
        vlayout.addComponent(adjustmentTable());
        
        Button closeBtn = new Button("CLOSE");
        closeBtn.setWidth("100%");
        closeBtn.addListener(closeBtnListener);
        vlayout.addComponent(closeBtn);
                
        ts.addComponent(vlayout);
        addComponent(ts);
    }

    private Table adjustmentTable(){
        adjustmentTbl.removeAllItems();
        adjustmentTbl.setWidth("100%");
        adjustmentTbl.setImmediate(true);
        adjustmentTbl.setSelectable(true);
        
        adjustmentTbl.addContainerProperty("id", Integer.class, null);
        adjustmentTbl.addContainerProperty("amount", Double.class, null);
        adjustmentTbl.addContainerProperty("remarks", String.class, null);
        adjustmentTbl.addContainerProperty("date posted", String.class, null);
        
        int i = 0;
        List<Adjustment> adjustmentList = payrollService.getListOfAdjustmentFromPayrollId(getPayrollId());
        for(Adjustment adj : adjustmentList){
            adjustmentTbl.addItem(new Object[]{  
                adj.getAdjustmentId(), 
                adj.getAmount(), 
                adj.getRemarks(), 
                utilities.convertDateFormat(adj.getDatePosted().toString())
            }, i);
            i++;
        }
        
        adjustmentTbl.setPageLength(adjustmentTbl.size());
        
        for(Object listener : adjustmentTbl.getListeners(ItemClickEvent.class)){
            adjustmentTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        adjustmentTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                final Item item = adjustmentTbl.getItem(itemId);
                
                double adjustments = utilities.convertStringToDouble(item.getItemProperty("amount").getValue().toString());
                String remarks = item.getItemProperty("remarks").getValue().toString();
                
                if(remarks.equals("edit timekeeping table")){
                    getWindow().showNotification("You cannot delete adjustment from previous Payroll!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(event.getPropertyId().equals("id")){
                    Window subWindow = removeAdjustment(utilities.convertStringToInteger(item.getItemProperty("id").getValue().toString()), 
                            getAmountToBeReceive(), 
                            getAmountReceived(), 
                            adjustments, 
                            getPayrollId());
                    if(subWindow.getParent() == null){
                        getApplication().getMainWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }                
                
            }
        });
        
        return adjustmentTbl;
    }
    
    private Window removeAdjustment(final int adjustmentId, 
            final double amountToBeReceive, 
            final double amountReceived, 
            final double adjustment, 
            final int payrollId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        final Window subWindow = new Window("REMOVE ADVANCES", vlayout);
        subWindow.setWidth("200px");
        
        Button removeAdjBtn = new Button("REMOVE ADJUSTMENT?");
        removeAdjBtn.setWidth("100%");
        removeAdjBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = payrollService.removeAdjustmentById(adjustmentId, 
                        amountToBeReceive, 
                        amountReceived, 
                        adjustment, 
                        payrollId);
                if(result){
                    (subWindow.getParent()).removeWindow(subWindow);
                    adjustmentTable();
                }
            }
        });
        subWindow.addComponent(removeAdjBtn);
        
        return subWindow;
    }
    
    public int getPayrollId() {
        return payrollId;
    }

    public double getAmountToBeReceive() {
        return amountToBeReceive;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public double getAdjustment() {
        return adjustment;
    }
    
    Button.ClickListener closeBtnListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            close();
        }
    };
}
