/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.model.Branch;
import com.openhris.model.Company;
import com.openhris.model.Trade;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.model.Employee;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.openhris.service.CompanyService;
import com.openhris.service.EmployeeService;
import com.openhris.service.PayrollService;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jet
 */
public class DropDownComponent extends ComboBox {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    Constant constant = new Constant();
    ServiceGetDAO serviceDAO = new ServiceGetDAO();
    EmployeeService employeeService = new EmployeeServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    PayrollService payrollService = new PayrollServiceImpl();
    
    public ComboBox populateCorporateComboBox(ComboBox corporation){
        corporation.removeAllItems();;
        corporation.setWidth("100%");
        corporation.setCaption("Corporation: ");        
        corporation.setNullSelectionAllowed(false);
        List<Company> companyList = companyService.getAllCorporation();
        for(Company c : companyList){
            corporation.addItem(c.getCompanyName());
        }
        corporation.setImmediate(true);
        
        return corporation;
    }
    
    public ComboBox populateCorporateListAssignedForUser(ComboBox corporation, int userId){
        corporation.removeAllItems();;
        corporation.setWidth("100%");
        corporation.setCaption("Corporation: ");        
        corporation.setNullSelectionAllowed(false);
        List<Company> companyList = companyService.getCorporateListAssignedForUser(userId);
        for(Company c : companyList){
            corporation.addItem(c.getCompanyName());
        }
        corporation.setImmediate(true);
        
        return corporation;
    }
    
    public ComboBox populateTradeComboBox(ComboBox trade, int corporateId){
        trade.removeAllItems();
        trade.setWidth("100%");
        trade.setCaption("Trade: ");        
        trade.setNullSelectionAllowed(false);
        List<Trade> tradeList = companyService.getTradeByCorporateId(corporateId);
        for(Trade t : tradeList){
            trade.addItem(t.getTradeName());
        }
        trade.setImmediate(true);
        
        return trade;
    }
    
    public ComboBox populateTradeListAssignedForUser(ComboBox trade, int userId, int corporateId){
        trade.removeAllItems();
        trade.setWidth("100%");
        trade.setCaption("Trade: ");        
        trade.setNullSelectionAllowed(false);
        List<Trade> tradeList = companyService.getTradeListAssignedForUser(userId, corporateId);
        for(Trade t : tradeList){
            trade.addItem(t.getTradeName());
        }
        trade.setImmediate(true);
        
        return trade;
    }
    
    public ComboBox populateBranchComboBox(ComboBox branch, int tradeId, int corporateId){
        branch.removeAllItems();
        branch.setWidth("100%");
        branch.setCaption("Branch: ");
        branch.setNullSelectionAllowed(false);
        List<Branch> branchList = companyService.getBranchByTrade(tradeId, corporateId);
        for(Branch b : branchList){
            branch.addItem(b.getBranchName());
        }
        branch.setImmediate(true);
        
        return branch;
    }
    
//    public ComboBox populateBranchListAssignedForUser(ComboBox branch, int tradeId){
//        branch.removeAllItems();
//        branch.setWidth("100%");
//        branch.setCaption("Branch: ");
//        branch.setNullSelectionAllowed(false);
//        List<Branch> branchList = companyService.getBranchListAssignedForUser(tradeId);
//        for(Branch b : branchList){
//            branch.addItem(b.getBranchName());
//        }
//        branch.setImmediate(true);
//        
//        return branch;
//    }
    
    public ComboBox populateEmployeesComboBox(ComboBox employees, int branchId){
        employees.removeAllItems();
        employees.setWidth("100%");
        employees.setCaption("Employees: ");
        employees.setNullSelectionAllowed(false);
        List<Employee> employeesList = employeeService.getEmployeePerBranchForDropDownList(branchId);        
        for(Employee e : employeesList){
            String name = e.getLastname()+ ", " + e.getFirstname() + " " + e.getLastname();
            employees.addItem(name.toUpperCase());
        }
        employees.setImmediate(true);
        
        return employees;
    }
 
    public ComboBox populateTotalDependent(String caption){
        ComboBox select = new ComboBox();
        select.setCaption(caption);        
        select.setWidth("100%");
        select.setNullSelectionAllowed(false);
        select.addContainerProperty("y", String.class, "");
        select.setItemCaptionPropertyId("y");
        
//        Map<Integer, String> mapTotalDependent = Constant.MAP_CONSTANT_DEPENDENT;
        Item item;
        for(Map.Entry<Integer, String> entry : Constant.MAP_CONSTANT_DEPENDENT.entrySet()){
            item = select.addItem(entry.getKey());
            item.getItemProperty("y").setValue(entry.getValue());
        }
        select.setImmediate(true);
        return select;
    }
    
    public ComboBox populateEmploymentWageStatus(String caption){
        ComboBox select = new ComboBox();
        select.setCaption(caption);        
        select.setWidth("100%");
        select.setNullSelectionAllowed(false);
        select.addContainerProperty("y", String.class, "");
        select.setItemCaptionPropertyId("y");
        
        Map<Integer, String> mapWageStatus = Constant.MAP_CONSTANT_EMPLOYMENT_WAGE_STATUS;           
        Item item;
        for(Map.Entry<Integer, String> entry : mapWageStatus.entrySet()){
            item = select.addItem(entry.getKey());
            item.getItemProperty("y").setValue(entry.getValue());
        }
        
        select.setImmediate(true);
        return select;
    }
    
    public ComboBox populateEmploymentStatus(String caption){
        ComboBox select = new ComboBox();
        select.setCaption(caption);        
        select.setWidth("100%");
        select.setNullSelectionAllowed(false);
        select.addContainerProperty("y", String.class, "");
        select.setItemCaptionPropertyId("y");
        
        Map<Integer, String> mapStatus = Constant.MAP_CONSTANT_EMPLOYMENT_STATUS;
        Item item;
        for(Map.Entry<Integer, String> entry : mapStatus.entrySet()){
            item = select.addItem(entry.getKey());
            item.getItemProperty("y").setValue(entry.getValue());
        }
        select.setImmediate(true);
        return select;
    }
    
    public ComboBox populateEmploymentWageEntry(String caption){
        ComboBox select = new ComboBox();
        select.setCaption(caption);        
        select.setWidth("100%");
        select.setNullSelectionAllowed(false);
        select.addContainerProperty("y", String.class, "");
        select.setItemCaptionPropertyId("y");
        
        Map<Integer, String> mapStatus = Constant.MAP_CONSTANT_EMPLOYMENT_WAGE_ENTRY;
        Item item;
        for(Map.Entry<Integer, String> entry : mapStatus.entrySet()){
            item = select.addItem(entry.getKey());
            item.getItemProperty("y").setValue(entry.getValue());
        }
        
        select.setImmediate(true);
        return select;
    }
    
    public ComboBox populateEmploymentAllowanceEntry(String allowanceCaption){
        ComboBox select = new ComboBox();
        select.setCaption(allowanceCaption);        
        select.setWidth("100%");
        select.setNullSelectionAllowed(false);
        select.addContainerProperty("y", String.class, "");
        select.setItemCaptionPropertyId("y");
        
        Map<Integer, String> mapStatus = Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY;
        Item item;
        for(Map.Entry<Integer, String> entry : mapStatus.entrySet()){
            item = select.addItem(entry.getKey());
            item.getItemProperty("y").setValue(entry.getValue());
        }
        select.setImmediate(true);
        return select;
    }
    
    public ComboBox populateAttendancePolicyDropDownList(ComboBox policy){
        policy.setCaption("Policy: ");
        policy.setNullSelectionAllowed(false);
        policy.setWidth("100%");
        Map<Integer, String> mapPolicy = Constant.MAP_CONSTANT_POLICY;
        for(int i = 0; i < mapPolicy.size(); i++){
            policy.addItem(mapPolicy.get(i));
        }
        policy.setImmediate(true);
        return policy;
    }
    
    public ComboBox populateAttendanceHolidayDropDownList(ComboBox holiday){
        holiday.setCaption("Holiday: ");
        holiday.setWidth("100%");
        Map<Integer, String> mapHoliday = Constant.MAP_CONSTANT_HOLIDAY;
        for(int i = 0; i < mapHoliday.size(); i++){
            holiday.addItem(mapHoliday.get(i));
        }
        holiday.setImmediate(true);
        return holiday;
    }
    
    public ComboBox populatePayrollPeriodDropDownList(ComboBox payrollPeriod){
        payrollPeriod.setCaption("Payroll Period: ");
        payrollPeriod.setWidth("100%");
        payrollPeriod.setNullSelectionAllowed(false);
        Map<Integer, String> mapHoliday = Constant.MAP_CONSTANT_PAYROLL_PERIOD;
        for(int i = 0; i < mapHoliday.size(); i++){
            payrollPeriod.addItem(mapHoliday.get(i));
        }
        payrollPeriod.setImmediate(true);
        return payrollPeriod;
    }
    
    public ComboBox populateUserRoleList(ComboBox userRole){
        userRole.setCaption("User Role: ");
        userRole.setWidth("100%");
        userRole.setNullSelectionAllowed(false);
        Map<Integer, String> mapUserRole = Constant.MAP_CONSTANT_USER_ROLE;
        for(int i = 0; i < mapUserRole.size(); i++){
            userRole.addItem(mapUserRole.get(i));
        }
        userRole.setImmediate(true);
        return userRole;
    }
    
    public ComboBox populateAdvanceTypeDropDownList(ComboBox advanceType){
        advanceType.removeAllItems();
        advanceType.setWidth("100%");
        advanceType.setCaption("Advance Type: ");
        advanceType.setNullSelectionAllowed(false);
        List<String> advanceTypeList = payrollService.getAdvanceTypeList();        
        for(String s : advanceTypeList){
            advanceType.addItem(s);
        }
        advanceType.setImmediate(true);
        
        return advanceType;
    }
    
    public ComboBox populatePayrollReportTypeList(ComboBox reportType){
        reportType.removeAllItems();
        reportType.setWidth("100%");
        reportType.setCaption("Report Type: ");
        reportType.setNullSelectionAllowed(false);
        String[] reportTypeList = Constant.CONSTANT_ARRAY_PAYROLL_REPORT_TYPE;        
        for(int i = 0; i < reportTypeList.length; i++){
            reportType.addItem(reportTypeList[i]);
        }
        reportType.setImmediate(true);
        
        return reportType;
    }
    
    public ComboBox populateCivilStatusList(ComboBox civilStatus){
	civilStatus.removeAllItems();
	civilStatus.setWidth("100%");
	civilStatus.setCaption("Civil Status: ");
	civilStatus.setNullSelectionAllowed(false);
	String[] civilStatusList = Constant.CONSTANT_ARRAY_CIVIL_STATUS;
	for(int i = 0; i < civilStatusList.length; i++){
	     civilStatus.addItem(civilStatusList[i]);
	}
	civilStatus.setImmediate(true);
	
	return civilStatus;	
    }
    
    public ComboBox populateGenderList(ComboBox gender){
	gender.removeAllItems();
	gender.setWidth("100%");
	gender.setCaption("Gender: ");
	gender.setNullSelectionAllowed(false);
	String[] genderList = Constant.CONSTANT_ARRAY_GENDER;
	for(int i = 0; i < genderList.length; i++){
	     gender.addItem(genderList[i]);
	}
	gender.setImmediate(true);
	
	return gender;
    }
    
    public ComboBox populateAddressType(ComboBox addressType){
        addressType.removeAllItems();
        addressType.setWidth("100%");
        addressType.setCaption("Address Type: ");
        addressType.setNullSelectionAllowed(false);
        String[] type = Constant.CONSTANT_ADDRESS_TYPE;
        for(int i = 0; i < type.length; i++){
            addressType.addItem(type[i]);
        }
        addressType.setImmediate(true);
        
        return addressType;
    }
}
