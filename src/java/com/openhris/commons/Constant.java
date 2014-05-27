/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.company.model.Branch;
import com.openhris.company.model.Company;
import com.openhris.company.model.Trade;
import com.openhris.company.serviceprovider.CompanyServiceImpl;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.service.CompanyService;
import com.openhris.service.PayrollService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jet
 */
public class Constant {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    CompanyService companyService = new CompanyServiceImpl();
    
    public static final Map<Integer, String> MAP_CONSTANT_DEPENDENT = createConstantMapDependent();
    public static final Map<Integer, String> MAP_CONSTANT_EMPLOYMENT_WAGE_STATUS = getConstantEmploymentWageStatus();
    public static final Map<Integer, String> MAP_CONSTANT_EMPLOYMENT_STATUS = getConstantEmploymentStatus(); 
    public static final Map<Integer, String> MAP_CONSTANT_EMPLOYMENT_WAGE_ENTRY = getConstantEmploymentWageEntry(); 
    public static final Map<Integer, String> MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY = getConstantEmploymentAllowanceEntry();
    public static final Map<Integer, String> MAP_CONSTANT_POLICY = createConstantPolicyList();
    public static final Map<Integer, String> MAP_CONSTANT_HOLIDAY = createConstantHolidayList();
    public static final String[] CONSTANT_ARRAY_CIVIL_STATUS = createConstantCivilStatusList();
    public static final String[] CONSTANT_ARRAY_GENDER = createConstantGenderList();
    public static final Map<Integer, String> MAP_CONSTANT_EDUCATION = createConstantEducationList();
    public static final Map<Integer, String> MAP_CONSTANT_BENEFITS_TYPE = createConstantBenefitsTypeList();
    public static final Map<Integer, String> MAP_CONSTANT_TARGET_TYPE = createConstantTargetTypeList();
    public static final Map<Integer, String> MAP_CONSTANT_VLSL_TYPE = createConstantVLSLTypeList();
    public static final Map<Integer, String> MAP_CONSTANT_LENGTH_OF_SERVICE = createConstantLengthOfServiceList();
    public static final Map<Integer, String> MAP_CONSTANT_CALENDAR_EVENT_TYPE = createConstantCalendarEventTypeList();
    public static final Map<Integer, String> MAP_CONSTANT_CALENDAR_STYLE_NAME = createConstantCalendarStyleNameList();
    public static final Map<Integer, String> MAP_CONSTANT_PAYROLL_PERIOD = createConstantPayrollPeriodList();
    public static final String[] CONSTANT_ARRAY_PAYROLL_REPORT_TYPE = createConstantPayrollReportTypeList();
    public static final Map<Integer, String> MAP_CONSTANT_USER_ROLE = createConstantUserRoleList();
    
    private static Map<Integer, String> createConstantMapDependent(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "s/me");
        map.put(1, "s1/me1");
        map.put(2, "s2/me2");
        map.put(3, "s3/me3");
        map.put(4, "s4/me4");
        return Collections.unmodifiableMap(map);
    }
    
    private static Map<Integer, String> getConstantEmploymentWageStatus(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "regular");
        map.put(1, "minimum");
        return Collections.unmodifiableMap(map);
    }
    
    private static Map<Integer, String> getConstantEmploymentStatus(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "regular");
        map.put(1, "contractual");
        map.put(2, "trainee");
        return Collections.unmodifiableMap(map);
    }
    
    private static Map<Integer, String> getConstantEmploymentWageEntry(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "monthly");
        map.put(1, "daily");
        return Collections.unmodifiableMap(map);
    }
    
    private static Map<Integer, String> getConstantEmploymentAllowanceEntry(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "monthly");
        map.put(1, "daily");
        map.put(2, "N/A");
        return Collections.unmodifiableMap(map);
    } 
    
    public static Map<Integer, String> createConstantPolicyList(){
        Map<Integer, String> map = new HashMap<Integer, String>();        
        map.put(0, "day-off");
        map.put(1, "working-day-off");
        map.put(2, "holiday");
        map.put(3, "working-holiday");
        map.put(4, "paternity-leave");
        map.put(5, "service-incentive-leave");
        map.put(6, "paid-vacation-leave");
        map.put(7, "paid-sick-leave");
        map.put(8, "unpaid-vacation-leave");
        map.put(9, "unpaid-sick-leave");
        map.put(10, "absent");
        map.put(11, "suspended");
        return Collections.unmodifiableMap(map);
    }
     
    public static Map<Integer, String> createConstantHolidayList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "legal-holiday");
        map.put(1, "special-holiday");
        return Collections.unmodifiableMap(map);
    }
    
    public static String[] createConstantCivilStatusList(){
        String[] civilStatusArray = {"Single", "Married", "Widow", "Separated", "Annulled"};
        return civilStatusArray;
    }
    
    public static String[] createConstantGenderList(){
        String[] genderList = {"Male", "Female"};
        return genderList;
    }
    
    public static Map<Integer, String> createConstantEducationList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "elementary");
        map.put(1, "high school");
        map.put(2, "college");
        map.put(3, "graduate school");
        map.put(4, "vocational course");
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantBenefitsTypeList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "allowance");
        map.put(1, "medical");
        map.put(2, "insurance");
        map.put(3, "government benefits");
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantTargetTypeList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "monthly");
        map.put(1, "annually");
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantVLSLTypeList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "vacation leave");
        map.put(1, "sick leave");
        map.put(2, "emergency leave");
        map.put(3, "bereavement leave");
        map.put(4, "birthday leave");        
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantLengthOfServiceList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "1 yr srvc");
        map.put(1, "2 to 5 yrs srvc");
        map.put(2, "6 to 10 yrs srvc");
        map.put(3, "11 to 15 yrs srvc");
        map.put(4, "16 & above srvc");
        return Collections.unmodifiableMap(map);
    }

    public static Map<Integer, String> createConstantCalendarEventTypeList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "legal holiday");
        map.put(1, "special holiday");
        map.put(2, "birthday");
        map.put(3, "meeting");
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantCalendarStyleNameList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "color 1");
        map.put(1, "color 2");
        map.put(2, "color 3");
        map.put(3, "color 4");
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantUserRoleList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "encoder");
        map.put(1, "accounting");
        map.put(2, "hr");
        map.put(3, "audit");
        return Collections.unmodifiableMap(map);
    }
    
    public static Map<Integer, String> createConstantPayrollPeriodList(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "15th of the month");
        map.put(1, "30th of the month");
        return Collections.unmodifiableMap(map);
    }
    
    public static String[] createConstantPayrollReportTypeList(){
        String[] reportTypeArray = {"Payroll Register", "Adjusted Payroll Register","Payslip Report", "SSS Report", 
		"SSS Report Sbarro", "SSS Loans Payable","Philhealth Report", "HDMF Report", "HDMF Savings", "HDMF Loans Payable", 
		"Witholding Tax", "Attendance Report", "Bank Debit Memo", "Allowances"};
        return reportTypeArray;
    }
    
    public Map<Integer, String> createConstantAllCorporation(){
        Map<Integer, String> map = new HashMap<Integer, String>(); 
        Integer i = 0;
        for(Company c : companyService.getAllCorporation()){
            map.put(i, c.getCompanyName());
            i++;
        } 
        return Collections.unmodifiableMap(map);
    }
    
    public Map<Integer, String> createConstantTradeByCorporation(int corporateId){
        Map<Integer, String> map = new HashMap<Integer, String>(); 
        Integer i = 0;
        for(Trade t : companyService.getTradeByCorporateId(corporateId)){
            map.put(i, t.getTradeName());
            i++;
        } 
        return Collections.unmodifiableMap(map);
    }
    
    public Map<Integer, String> createConstantBranchByTrade(int corporateId, int tradeId){
        Map<Integer, String> map = new HashMap<Integer, String>(); 
        Integer i = 0;
        for(Branch b : companyService.getBranchByTrade(tradeId, corporateId)){
            map.put(i, b.getBranchName());
            i++;
        } 
        return Collections.unmodifiableMap(map);
    }     
        
}
