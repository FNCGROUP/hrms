/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.tables;

import com.hrms.utilities.CommonUtil;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.global.AbstractTableProperties;
import com.openhris.payroll.AdjustmentWindow;
import com.openhris.payroll.containers.PayrollRegisterDataContainer;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class PayrollRegisterTableProperties extends AbstractTableProperties {

    private int branchId;
    private String payrollDate;
    private boolean prev;

    public PayrollRegisterTableProperties() {
        setContainerDataSource(new PayrollRegisterDataContainer());
        tableGenerateRenderer();
    }

    public PayrollRegisterTableProperties(int branchId, String payrollDate, boolean prev) {
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        this.prev = prev;
        
        setContainerDataSource(new PayrollRegisterDataContainer(branchId, payrollDate, prev));
        tableGenerateRenderer();        
    }
    
    @Override
    protected void tableGenerateRenderer() {
//        addListener(new ItemClickEvent.ItemClickListener() {
//
//            @Override
//            public void itemClick(ItemClickEvent event) {
//                Object itemId = event.getItemId();
//                Item item = getItem(itemId);
//                
//                if(event.getPropertyId().equals("adjustments")){
//                    if(!UserAccessControl.isAdjustment()){
//                        getWindow().showNotification("You are not allowed to add adjustments", Window.Notification.TYPE_WARNING_MESSAGE);
//                        return;
//                    }
//                    
//                    int payrollId = Integer.parseInt(item.getItemProperty("id").toString());
//                    double amountToBeReceive = CommonUtil.convertStringToDouble(item.getItemProperty("amount to be receive").getValue().toString());
//                    double amountReceive = CommonUtil.convertStringToDouble(item.getItemProperty("amount received").getValue().toString());
//                    double adjustments = CommonUtil.convertStringToDouble(item.getItemProperty("adjustments").getValue().toString());
//                            
//                    Window subWindow = new AdjustmentWindow(payrollId, 
//                            amountToBeReceive, 
//                            amountReceive, 
//                            adjustments);
//                    if(subWindow.getParent() == null){
//                        getWindow().addWindow(subWindow);
//                    }
//                    subWindow.setModal(true);
//                    subWindow.center();
//                    
//                    subWindow.addListener(new Window.CloseListener() {
//
//                        @Override
//                        public void windowClose(Window.CloseEvent e) {
//                            setContainerDataSource(new PayrollRegisterDataContainer(getBranchId(), getPayrollDate(), isPrev()));
//                        }
//                    });
//                }
//            }
//        });
        
    }

    public int getBranchId() {
        return branchId;
    }

    public String getPayrollDate() {
        return payrollDate;
    }

    public boolean isPrev() {
        return prev;
    }
    
}
