/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.PayrollService;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class PayrollSubModules {
    
    PayrollService ps = new PayrollServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    private Item item;
    
    public Window perDiemWindow(Item item){
        this.item = item;
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window sub = new Window("PER DIEM", vlayout);
        sub.setWidth("220px");
        sub.setModal(true);
        sub.center();
        
        final TextField perDiemAmount = new TextField("Amount: ");
        perDiemAmount.setWidth("100%");
        perDiemAmount.setValue(util.convertStringToDouble(getPayrollTableItem().getItemProperty("per diem").toString()));
        perDiemAmount.setNullSettingAllowed(false);
        sub.addComponent(perDiemAmount);
        
        Button save = new Button("SAVE");
        save.setWidth("100%");
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                int payrollId = util.convertStringToInteger(getPayrollTableItem().getItemProperty("id").getValue().toString());
                double amountToBeReceive = util.convertStringToDouble(getPayrollTableItem().getItemProperty("amount to be receive").toString());
                double amountReceived = util.convertStringToDouble(getPayrollTableItem().getItemProperty("amount received").toString());
                
                boolean result = ps.addPerDiem(
                        payrollId, 
                        util.convertStringToDouble(perDiemAmount.getValue().toString()), 
                        amountToBeReceive, 
                        amountReceived);
                if(result){
                    getPayrollTableItem().getItemProperty("amount to be receive").setValue(amountToBeReceive+util.convertStringToDouble(perDiemAmount.getValue().toString()));
                    getPayrollTableItem().getItemProperty("amount received").setValue(amountReceived+util.convertStringToDouble(perDiemAmount.getValue().toString()));
                    getPayrollTableItem().getItemProperty("per diem").setValue(perDiemAmount.getValue());
                    (sub.getParent()).removeWindow(sub);
                }
            }
        });
        sub.addComponent(save);
        
        return sub;
    } 
    
    Item getPayrollTableItem(){
        return item;
    }    
}
