/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.utilities.ConvertionUtilities;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.payroll.service.PayrollService;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class ViewFullScreen extends Window {

    private boolean prev;
    private String payrollDate;
    private Table payrollRegisterTbl = new Table();
    private int branchId;
    
    OpenHrisUtilities util = new OpenHrisUtilities();
    PayrollService payrollService = new PayrollServiceImpl();
    VerticalLayout vlayout = new VerticalLayout();
    
    public ViewFullScreen(boolean prev, int branchId, String payrollDate) {
        this.prev = prev;
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        
        setSizeFull();
        setImmediate(true); 
        setClosable(false);
                
        vlayout.setMargin(false);
        vlayout.setSpacing(true);
        vlayout.setSizeFull();
        
        payrollRegisterTable(getBranchId(), getPayrollDate(), getPrev());
        vlayout.addComponent(payrollRegisterTbl);
        
        Button closeBtn = new Button("Close");
        closeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                PayrollRegisterMainUI payrollRegisterMainUI = new PayrollRegisterMainUI(getBranchId());
                payrollRegisterMainUI.payrollRegisterTable(getBranchId(), getPayrollDate(), getPrev()).removeAllItems();
                payrollRegisterMainUI.payrollRegisterTable(getBranchId(), getPayrollDate(), getPrev());
                close();
            }
        });
        vlayout.addComponent(closeBtn);
        vlayout.setComponentAlignment(closeBtn, Alignment.BOTTOM_RIGHT);
        addComponent(vlayout);
    }
    
    public Table payrollRegisterTable(int branchId, String payrollDate, boolean prev){
        payrollRegisterTbl.removeAllItems();
        payrollRegisterTbl.setWidth("100%");
        payrollRegisterTbl.setHeight("70%");
        payrollRegisterTbl.setImmediate(true);
        payrollRegisterTbl.setSelectable(true);
        payrollRegisterTbl.setColumnCollapsingAllowed(true);
        payrollRegisterTbl.addStyleName("employees-table-layout");
        
        payrollRegisterTbl.addContainerProperty("id", String.class, null);
        payrollRegisterTbl.addContainerProperty("name", String.class, null);
        payrollRegisterTbl.addContainerProperty("no. of days", Integer.class, null);        
        payrollRegisterTbl.addContainerProperty("rate per day", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("basic salary", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("half-month salary", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("overtime pay", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("legal holiday", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("special holiday", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("night differential", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("wdo", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("absent", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("lates", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("undertime", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("gross pay", Double.class, null);         
        payrollRegisterTbl.addContainerProperty("sss", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("phic", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("hdmf", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("tax", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("net pay", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("meal allowance", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("allowance for liquidation", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("advances to o/e", Double.class, null);         
        payrollRegisterTbl.addContainerProperty("adjustments", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("amount to be receive", Double.class, null);        
        payrollRegisterTbl.addContainerProperty("amount received", Double.class, null);  
	payrollRegisterTbl.addContainerProperty("for adjustments", Double.class, null);
        
        payrollRegisterTbl.setColumnAlignment("no. of days", Table.ALIGN_CENTER);
        payrollRegisterTbl.setColumnAlignment("rate per day", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("absent", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("meal allowance", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        payrollRegisterTbl.setColumnAlignment("amount received", Table.ALIGN_RIGHT);
	payrollRegisterTbl.setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
        
        List<PayrollRegister> payrollRegisterList = payrollService.getPayrollRegisterByBranch(branchId, payrollDate, prev);
        int i = 0;
        for(PayrollRegister pr : payrollRegisterList){
            payrollRegisterTbl.addItem(new Object[]{
                pr.getId(), 
		pr.getName().toUpperCase(), 
		pr.getNumOfDays(), 
		util.roundOffToTwoDecimalPlaces(pr.getRatePerDay()), 
		util.roundOffToTwoDecimalPlaces(pr.getBasicSalary()), 
                util.roundOffToTwoDecimalPlaces(pr.getHalfMonthSalary()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalOvertimePaid()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalLegalHolidayPaid()), 
                util.roundOffToTwoDecimalPlaces(pr.getTotalSpecialHolidayPaid()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalNightDifferentialPaid()), 
                util.roundOffToTwoDecimalPlaces(pr.getTotalWorkingDayOffPaid()), 
		util.roundOffToTwoDecimalPlaces(pr.getAbsences()), 
		util.roundOffToTwoDecimalPlaces(pr.getTotalLatesDeduction()), 
                util.roundOffToTwoDecimalPlaces(pr.getTotalUndertimeDeduction()), 
		util.roundOffToTwoDecimalPlaces(pr.getGrossPay()), 
		util.roundOffToTwoDecimalPlaces(pr.getSss()), 
		util.roundOffToTwoDecimalPlaces(pr.getPhic()), 
                util.roundOffToTwoDecimalPlaces(pr.getHdmf()), 
		util.roundOffToTwoDecimalPlaces(pr.getTax()), 
		util.roundOffToTwoDecimalPlaces(pr.getNetSalary()), 
		util.roundOffToTwoDecimalPlaces(pr.getAllowance()), 
                util.roundOffToTwoDecimalPlaces(pr.getAllowanceForLiquidation()), 
		util.roundOffToTwoDecimalPlaces(pr.getAmount()), 
		util.roundOffToTwoDecimalPlaces(pr.getAdjustment()), 
                util.roundOffToTwoDecimalPlaces(pr.getAmountToBeReceive()-pr.getAllowanceForLiquidation()), 
		util.roundOffToTwoDecimalPlaces(pr.getAmountReceivable()-pr.getAllowanceForLiquidation()), 
		util.roundOffToTwoDecimalPlaces(pr.getForAdjustments())
            }, i);
            i++;
        }
        payrollRegisterTbl.setPageLength(25);
        
        for(Object listener : payrollRegisterTbl.getListeners(ItemClickEvent.class)){
            payrollRegisterTbl.removeListener(ItemClickEvent.class, listener);
        }
                
        payrollRegisterTbl.setColumnCollapsed("amount received", true);
	payrollRegisterTbl.setColumnCollapsed("for adjustments", true);
        
        return payrollRegisterTbl;
    }
    
    private boolean getPrev(){
        return prev;
    }
        
    private int getBranchId(){
        return branchId;
    }
    
    private String getPayrollDate(){
        return payrollDate;
    }
}
