/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.containers;

import com.hrms.utilities.CommonUtil;
import com.openhris.model.PayrollRegister;
import com.openhris.service.PayrollService;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class PayrollRegisterDataContainer extends IndexedContainer {

    PayrollService payrollService = new PayrollServiceImpl();
    
    private int branchId;
    private String payrollDate;
    private boolean prev;
    
    public PayrollRegisterDataContainer() {
        addContainerProperty("id", String.class, null);
        addContainerProperty("name", String.class, null);
        addContainerProperty("no. of days", Integer.class, null);        
        addContainerProperty("rate per day", Double.class, null);        
        addContainerProperty("basic salary", Double.class, null);        
        addContainerProperty("half-month salary", Double.class, null);        
        addContainerProperty("overtime pay", Double.class, null);        
        addContainerProperty("legal holiday", Double.class, null);        
        addContainerProperty("special holiday", Double.class, null);        
        addContainerProperty("night differential", Double.class, null);        
        addContainerProperty("wdo", Double.class, null);        
        addContainerProperty("absent", Double.class, null);        
        addContainerProperty("lates", Double.class, null);        
        addContainerProperty("undertime", Double.class, null);        
        addContainerProperty("gross pay", Double.class, null);         
        addContainerProperty("sss", Double.class, null);        
        addContainerProperty("phic", Double.class, null);        
        addContainerProperty("hdmf", Double.class, null);        
        addContainerProperty("tax", Double.class, null);        
        addContainerProperty("net pay", Double.class, null);        
        addContainerProperty("communication", Double.class, null); 
        addContainerProperty("per diem", Double.class, null); 
        addContainerProperty("cola", Double.class, null); 
        addContainerProperty("meal", Double.class, null); 
        addContainerProperty("transportation", Double.class, null); 
        addContainerProperty("others", Double.class, null);         
        addContainerProperty("allowance for liquidation", Double.class, null); 
        addContainerProperty("advances to o/e", Double.class, null);         
        addContainerProperty("adjustments", Double.class, null);        
        addContainerProperty("amount to be receive", Double.class, null);        
        addContainerProperty("amount received", Double.class, null);  
	addContainerProperty("for adjustments", Double.class, null);
        addContainerProperty("cut-off from", String.class, null);
        addContainerProperty("cut-off to", String.class, null);
        addContainerProperty("payroll period", String.class, null);
    }

    public PayrollRegisterDataContainer(int branchId, String payrollDate, boolean prev) {
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        this.prev = prev;
        
        addContainerProperty("id", String.class, null);
        addContainerProperty("name", String.class, null);
        addContainerProperty("no. of days", Integer.class, null);        
        addContainerProperty("rate per day", Double.class, null);        
        addContainerProperty("basic salary", Double.class, null);        
        addContainerProperty("half-month salary", Double.class, null);        
        addContainerProperty("overtime pay", Double.class, null);        
        addContainerProperty("legal holiday", Double.class, null);        
        addContainerProperty("special holiday", Double.class, null);        
        addContainerProperty("night differential", Double.class, null);        
        addContainerProperty("wdo", Double.class, null);        
        addContainerProperty("absent", Double.class, null);        
        addContainerProperty("lates", Double.class, null);        
        addContainerProperty("undertime", Double.class, null);        
        addContainerProperty("gross pay", Double.class, null);         
        addContainerProperty("sss", Double.class, null);        
        addContainerProperty("phic", Double.class, null);        
        addContainerProperty("hdmf", Double.class, null);        
        addContainerProperty("tax", Double.class, null);        
        addContainerProperty("net pay", Double.class, null);        
        addContainerProperty("communication", Double.class, null); 
        addContainerProperty("per diem", Double.class, null); 
        addContainerProperty("cola", Double.class, null); 
        addContainerProperty("meal", Double.class, null); 
        addContainerProperty("transportation", Double.class, null); 
        addContainerProperty("others", Double.class, null);         
        addContainerProperty("allowance for liquidation", Double.class, null); 
        addContainerProperty("advances to o/e", Double.class, null);         
        addContainerProperty("adjustments", Double.class, null);        
        addContainerProperty("amount to be receive", Double.class, null);        
        addContainerProperty("amount received", Double.class, null);  
	addContainerProperty("for adjustments", Double.class, null);
        addContainerProperty("cut-off from", String.class, null);
        addContainerProperty("cut-off to", String.class, null);
        addContainerProperty("payroll period", String.class, null);
        
        for(PayrollRegister pr : payrollService.getPayrollRegisterByBranch(branchId, payrollDate, prev)){
            Item item = getItem(addItem());
            item.getItemProperty("id").setValue(pr.getId());
            item.getItemProperty("name").setValue(pr.getName().toUpperCase());
            item.getItemProperty("no. of days").setValue(pr.getNumOfDays());
            item.getItemProperty("rate per day").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getRatePerDay()));
            item.getItemProperty("basic salary").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getBasicSalary()));
            item.getItemProperty("half-month salary").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getHalfMonthSalary()));
            item.getItemProperty("overtime pay").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalOvertimePaid()));
            item.getItemProperty("legal holiday").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalLegalHolidayPaid()));
            item.getItemProperty("special holiday").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalSpecialHolidayPaid()));
            item.getItemProperty("night differential").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalNightDifferentialPaid()));
            item.getItemProperty("wdo").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalWorkingDayOffPaid()));
            item.getItemProperty("absent").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getAbsences()));
            item.getItemProperty("lates").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalLatesDeduction()));
            item.getItemProperty("undertime").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTotalUndertimeDeduction()));
            item.getItemProperty("gross pay").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getGrossPay()));
            item.getItemProperty("sss").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getSss()));
            item.getItemProperty("phic").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getPhic()));
            item.getItemProperty("hdmf").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getHdmf()));
            item.getItemProperty("tax").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTax()));
            item.getItemProperty("net pay").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getNetSalary()));
            item.getItemProperty("communication").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getCommunicationAllowance()));
            item.getItemProperty("per diem").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getPerDiemAllowance()));
            item.getItemProperty("cola").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getColaAllowance()));
            item.getItemProperty("meal").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getMealAllowance()));
            item.getItemProperty("transportation").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getTransportationAllowance()));
            item.getItemProperty("others").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getOtherAllowances()));
            item.getItemProperty("allowance for liquidation").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getAllowanceForLiquidation()));
            item.getItemProperty("advances to o/e").setValue((String.valueOf(pr.getAmount()) == null) ? 0 : CommonUtil.roundOffToTwoDecimalPlaces(pr.getAmount()));
            item.getItemProperty("adjustments").setValue((String.valueOf(pr.getAdjustment()) == null)? 0 : CommonUtil.roundOffToTwoDecimalPlaces(pr.getAdjustment()));
            item.getItemProperty("amount to be receive").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getAmountToBeReceive()));
            item.getItemProperty("amount received").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getAmountReceivable()));
            item.getItemProperty("for adjustments").setValue(CommonUtil.roundOffToTwoDecimalPlaces(pr.getForAdjustments()));
            item.getItemProperty("cut-off from").setValue(CommonUtil.convertDateFormat(pr.getAttendancePeriodFrom().toString()));
            item.getItemProperty("cut-off to").setValue(CommonUtil.convertDateFormat(pr.getAttendancePeriodTo().toString()));
            item.getItemProperty("payroll period").setValue(CommonUtil.convertDateFormat(pr.getPayrollDate().toString()));
        }
    }
    
}
