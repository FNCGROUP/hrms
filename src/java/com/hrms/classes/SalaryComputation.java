/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.entities.Advances;
import com.hrms.queries.AdvancesDAO;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.SalaryDAO;
import com.hrms.utilities.ContributionUtilities;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.ui.VerticalLayout;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class SalaryComputation extends VerticalLayout{
    
    GetSQLConnection getConnection = new GetSQLConnection();    
    Boolean result = false;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;
    ContributionUtilities contributionUtil = new ContributionUtilities();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    SalaryDAO salaryDAO = new SalaryDAO();
    
    private String employeeId;
    private String employmentWageEntry;
    private String employmentWageStatus;
    private String totalDependent;
    private Double employmentWage;
    private Integer branchId;
    private double totalDeduction;
    private double totalAddition;
    private double totalAbsences;  
    private double totalLatesDeduction;
    private double totalUndertimeDeduction;
    private double totalOvertimePaid;
    private double totalNightDifferentialPaid;
    private double totalLegalHolidayPaid;
    private double totalSpecialHolidayPaid;
    private double totalWorkingDayOffPaid;
    private double totalHolidayLegalHolidayPaid;
    
    private Double cashBond;
    private Double allowanceEntry = null;
    private String allowanceStatus;
    private Double allowanceForLiquidation = null;
    private Date entryDate;
    private Date dateNow = new Date();
    GetSQLQuery query = new GetSQLQuery();
    DecimalFormat df = new DecimalFormat("0.00");
      
    
    public String getEmployeeId(String name){
        return employeeId = query.getEmployeesId(name);
    }
    
    public void getEmploymentWageAndEntry(String id){
        Connection conn = getConnection.connection();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT * FROM employee WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                employmentWageEntry = rs.getString("employmentWageEntry");
                employmentWage = conUtil.convertStringToDouble(rs.getString("employmentWage"));
                entryDate = conUtil.parsingDate(rs.getString("entryDate"));
                totalDependent = rs.getString("totalDependent");
                employmentWageStatus = rs.getString("employmentWageStatus");
                allowanceForLiquidation = conUtil.convertStringToDouble(rs.getString("allowanceForLiquidation"));
                branchId = Integer.parseInt(rs.getString("branchId"));
                
                if(rs.getString("allowanceEntry").equals("0.0")){
                    allowanceEntry = null;
                    allowanceStatus = null;
                }else{
                    allowanceEntry = conUtil.convertStringToDouble(rs.getString("allowanceEntry"));
                    allowanceStatus = rs.getString("allowanceStatus");
                }   
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryComputation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getEmploymentWageEntry(){
        return employmentWageEntry;
    }
    
    public Double getEmploymentWage(){
        return employmentWage;
    }   
    
    public String getEmploymentWageStatus(){
        return employmentWageStatus;
    }
    
    public String getTotalDependent(){
        return totalDependent;
    }
    
    public Boolean saveAttendanceData(List list, String id, String attendanceDateFrom, 
            String attendanceDateTo, String paydayPeriod, String salaryDate, String salaryRowId){
        Connection conn = getConnection.connection();
        List editList = new ArrayList(); //for editting
        List dateList = new ArrayList();
        List policyList = new ArrayList();
        List holidayList = new ArrayList();
        List lpList = new ArrayList();
        List latesList = new ArrayList();
        List upList = new ArrayList();
        List undertimeList = new ArrayList();
        List opList = new ArrayList();
        List overtimeList = new ArrayList();
        List nightDifferentialList = new ArrayList();
        List<Double> latesDeductionList = new ArrayList<Double>();
        List<Double> undertimeDeductionList = new ArrayList<Double>();
        List<Double> overtimePaidList = new ArrayList<Double>();
        List<Double> nightDifferentialPaidList = new ArrayList<Double>();
        List<Double> legalHolidayPaidList = new ArrayList<Double>();
        List<Double> specialHolidayPaidList = new ArrayList<Double>();
        List<Double> workingDayOffPaidList = new ArrayList<Double>();
        List<Double> holidayLegalHolidayPaidList = new ArrayList<Double>();
        
        double lates, undertime, overtime, nightDifferential, latesDeduction = 0, 
                undertimeDeduction = 0, overtimePaid = 0, nightDifferentialPaid = 0, 
                legalHolidayPaid = 0, specialHolidayPaid = 0, workingDayOffPaid = 0, psHolidayPaid = 0;
        
        String[] rowPropertyNew = {"date", "policy", "holiday", "lp", "lates", "up","undertime", 
                "op","overtime", "nightDifferential", "latesPerMinute", "undertimePerMinute", "overtimePerMinute", "nightDifferentialPerMinute", 
                "lholiday", "sholiday", "wdo", "psday"};
        String[] rowPropertyEdit = {"edit","date", "policy", "holiday", "lp", "lates", "up","undertime", 
                "op","overtime", "nightDifferential", "latesPerMinute", "undertimePerMinute", "overtimePerMinute", "nightDifferentialPerMinute", 
                "lholiday", "sholiday", "wdo", "psday"};
        String [] rowProperty;
        if(salaryRowId != null){
            rowProperty = rowPropertyEdit;
        }else{
            rowProperty = rowPropertyNew;
        }
        
        Map<String, String> propertyMap;
        for(int i = 0; i < list.size(); i++){
            String str = list.get(i).toString();
            String delimiter = " ";
            String[] temp;
            temp = str.split(delimiter);
            /*String[] rowProperty = {"date", "policy", "holiday", "lp", "lates", "up","undertime", 
                "op","overtime", "nightDifferential", "latesPerMinute", "undertimePerMinute", "overtimePerMinute", "nightDifferentialPerMinute", 
                "lholiday", "sholiday", "wdo", "psday"}; */
            propertyMap = new HashMap();
            for(int j = 0; j < temp.length; j++){                
                propertyMap.put(rowProperty[j], temp[j]);                
                if(rowProperty[j].equals("date")){ dateList.add(temp[j]); }                
                if(rowProperty[j].equals("policy")){ policyList.add(temp[j]); }                
                if(rowProperty[j].equals("holiday")){ holidayList.add(temp[j]); }                
                if(rowProperty[j].equals("lp")){ lpList.add(temp[j]); }                
                if(rowProperty[j].equals("lates")){ latesList.add(temp[j]); }                
                if(rowProperty[j].equals("up")){ upList.add(temp[j]); }                
                if(rowProperty[j].equals("undertime")){ undertimeList.add(temp[j]); }                
                if(rowProperty[j].equals("op")){ opList.add(temp[j]); }
                if(rowProperty[j].equals("overtime")){ overtimeList.add(temp[j]); }
                if(rowProperty[j].equals("nightDifferential")){ nightDifferentialList.add(temp[j]); }
                if(rowProperty[j].equals("latesPerMinute")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        latesDeductionList.add(Double.parseDouble(temp[j])); 
                    }else{
                        latesDeductionList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("undertimePerMinute")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        undertimeDeductionList.add(Double.parseDouble(temp[j])); 
                    }else{
                        undertimeDeductionList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("overtimePerMinute")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        overtimePaidList.add(Double.parseDouble(temp[j])); 
                    }else{
                        overtimePaidList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("nightDifferentialPerMinute")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        nightDifferentialPaidList.add(Double.parseDouble(temp[j])); 
                    }else{
                        nightDifferentialPaidList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("lholiday")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        legalHolidayPaidList.add(Double.parseDouble(temp[j])); 
                    }else{
                        legalHolidayPaidList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("sholiday")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        specialHolidayPaidList.add(Double.parseDouble(temp[j])); 
                    }else{
                        specialHolidayPaidList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("wdo")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        workingDayOffPaidList.add(Double.parseDouble(temp[j]));
                    }else{
                        workingDayOffPaidList.add(Double.parseDouble(temp[j]));
                    }
                }
                if(rowProperty[j].equals("psday")){
                    if(temp[j].equals("null")){
                        temp[j] = "0.0";
                        holidayLegalHolidayPaidList.add(Double.parseDouble(temp[j]));
                    }else{
                        holidayLegalHolidayPaidList.add(Double.parseDouble(temp[j]));
                    }
                }
            }                        
        }
        
        totalDeduction = 0; 
        totalAddition = 0;
        
        //process all late deduction
        totalLatesDeduction = 0;
        Double[] doubleArrayLatesDeduction = new Double[latesDeductionList.size()];
        for(int i = 0; i < latesDeductionList.size(); i++){
            double d = latesDeductionList.get(i);
            doubleArrayLatesDeduction[i] = d;          
        }
        for(int i = 0; i < doubleArrayLatesDeduction.length; i++){
            totalLatesDeduction = totalLatesDeduction + Double.valueOf(doubleArrayLatesDeduction[i]).doubleValue();
        }
        
        //process all undertime deduction
        totalUndertimeDeduction = 0;
        Double[] doubleArrayUndertimeDeduction = new Double[undertimeDeductionList.size()];
        for(int i = 0; i < undertimeDeductionList.size(); i++){
            double d = undertimeDeductionList.get(i);
            doubleArrayUndertimeDeduction[i] = d;          
        }
        for(int i = 0; i < doubleArrayUndertimeDeduction.length; i++){
            totalUndertimeDeduction = totalUndertimeDeduction + Double.valueOf(doubleArrayUndertimeDeduction[i]).doubleValue();
        }
        
        //process all paid overtime
        totalOvertimePaid = 0;
        Double[] doubleArrayOvertimePaid = new Double[overtimePaidList.size()];
        for(int i = 0; i < overtimePaidList.size(); i++){
            double d = overtimePaidList.get(i);
            doubleArrayOvertimePaid[i] = d;          
        }
        for(int i = 0; i < doubleArrayOvertimePaid.length; i++){
            totalOvertimePaid = totalOvertimePaid + Double.valueOf(doubleArrayOvertimePaid[i]).doubleValue();
        }
        
        //process all paid night differential
        totalNightDifferentialPaid = 0;
        Double[] doubleArrayNightDifferentialPaid = new Double[nightDifferentialPaidList.size()];
        for(int i = 0; i < nightDifferentialPaidList.size(); i++){
            double d = nightDifferentialPaidList.get(i);
            doubleArrayNightDifferentialPaid[i] = d;          
        }
        for(int i = 0; i < doubleArrayNightDifferentialPaid.length; i++){
            totalNightDifferentialPaid = totalNightDifferentialPaid + Double.valueOf(doubleArrayNightDifferentialPaid[i]).doubleValue();
        }
        
        //process all paid legal holiday
        totalLegalHolidayPaid = 0;
        Double[] doubleArrayLegalHolidayPaid = new Double[legalHolidayPaidList.size()];
        for(int i = 0; i < legalHolidayPaidList.size(); i++){
            double d = legalHolidayPaidList.get(i);
            doubleArrayLegalHolidayPaid[i] = d;          
        }
        for(int i = 0; i < doubleArrayLegalHolidayPaid.length; i++){
            totalLegalHolidayPaid = totalLegalHolidayPaid + Double.valueOf(doubleArrayLegalHolidayPaid[i]).doubleValue();
        }
        
        //process all paid special holiday
        totalSpecialHolidayPaid = 0;
        Double[] doubleArraySpecialHolidayPaid = new Double[specialHolidayPaidList.size()];
        for(int i = 0; i < specialHolidayPaidList.size(); i++){
            double d = specialHolidayPaidList.get(i);
            doubleArraySpecialHolidayPaid[i] = d;          
        }
        for(int i = 0; i < doubleArraySpecialHolidayPaid.length; i++){
            totalSpecialHolidayPaid = totalSpecialHolidayPaid + Double.valueOf(doubleArraySpecialHolidayPaid[i]).doubleValue();
        }
        
        //process all paid working day-off
        totalWorkingDayOffPaid = 0;
        Double[] doubleArrayWorkingDayOffPaid = new Double[workingDayOffPaidList.size()];
        for(int i = 0; i < workingDayOffPaidList.size(); i++){
            double d = workingDayOffPaidList.get(i);
            doubleArrayWorkingDayOffPaid[i] = d;
        }
        for(int i = 0; i < doubleArrayWorkingDayOffPaid.length; i++){
            totalWorkingDayOffPaid = totalWorkingDayOffPaid + Double.valueOf(doubleArrayWorkingDayOffPaid[i].doubleValue());
        }
        
        //process Holiday Legal-Holiday
        totalHolidayLegalHolidayPaid = 0;
        Double[] doubleArrayHolidaySpecialHolidayPaid = new Double[holidayLegalHolidayPaidList.size()];
        for(int i = 0; i < holidayLegalHolidayPaidList.size(); i++){
            double d = holidayLegalHolidayPaidList.get(i);
            doubleArrayHolidaySpecialHolidayPaid[i] = d;
        }
        for(int i = 0; i < doubleArrayHolidaySpecialHolidayPaid.length; i++){
            totalHolidayLegalHolidayPaid = totalHolidayLegalHolidayPaid + Double.valueOf(doubleArrayHolidaySpecialHolidayPaid[i].doubleValue());
        } 
        
        totalDeduction = totalLatesDeduction + totalUndertimeDeduction;
        totalAddition = totalOvertimePaid + totalNightDifferentialPaid + totalLegalHolidayPaid + totalSpecialHolidayPaid + totalWorkingDayOffPaid;
        double basicSalary = getBasicSalary(employmentWage, employmentWageEntry); //get basic salary          
        double halfMonthSalary = getHalfMonthSalary(employmentWageEntry, policyList, basicSalary, employmentWage, dateList, id); //process half month salary
        double taxableSalary = getTaxableSalary(employmentWage, employmentWageEntry, policyList, 
                halfMonthSalary, paydayPeriod); //process taxable salary    
        System.out.println("sss");
        double sssContribution = contributionUtil.getSss(taxableSalary, id, salaryDate); //get sss contribution
        System.out.println("phic");
        double philhealthContribution = contributionUtil.getPhilhealth(basicSalary); // get philhealth contribution                  
        double hdmfContribution = contributionUtil.getHdmf(basicSalary); //get pag-ibig contribution
        double grossPay;
        if(paydayPeriod.equals("15th of the month")){ 
            grossPay = taxableSalary - (philhealthContribution + hdmfContribution);
            sssContribution = 0;             
        }else{                        
            grossPay = taxableSalary - sssContribution;
            philhealthContribution = 0;
            hdmfContribution = 0;
        }
        double tax = getTax(totalDependent, grossPay); //get tax  
        
        //System.out.println(allowanceForLiquidation);
        if(employmentWageStatus.equals("minimum") || tax < 0){
            tax = 0;
        }        
                
        cashBond = 0.0;
        
        double allowance = 0.0;
        
        if(allowanceEntry != null){
            allowance = getAllowance(policyList, allowanceStatus, allowanceEntry);
        }
        
        int numberOfDays = getNumberOfDays(dateList, policyList);
        Double advances = 0.0;        
        Double afl = (allowanceForLiquidation/2) - getAllowanceForLiquidationDeduction(policyList, allowanceForLiquidation);        
        Double netSalary = grossPay - tax; //process net salary
        Double amountReceivable = new Double(df.format(netSalary + allowance + afl));  
        Double amountToBeReceive = amountReceivable;
            
        String attendanceQuery = "INSERT INTO attendance(salaryId, attendanceDate, policy, holiday, latesPremium, lates, "
                + "undertimePremium, undertime, overtimePremium, overtime, nightDifferential, latesDeduction, undertimeDeduction, "
                + "overtimePaid, nightDifferentialPaid, legalHolidayPaid, specialHolidayPaid, workingDayOffPaid, psHolidayPaid) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        String salaryQuery = "INSERT INTO salary(employeeId, attendancePeriodFrom, attendancePeriodTo, basicSalary, halfMonthSalary, philhealth, "
                + "sss, hdmf, absences, numberOfDays, taxableSalary, tax, cashBond, totalLatesDeduction, totalUndertimeDeduction, totalOvertimePaid, "
                + "totalNightDifferentialPaid, totalLegalHolidayPaid, totalSpecialHolidayPaid, totalWorkingDayOffPaid, advances, "
                + "allowance, allowanceForLiquidation, netSalary, amountToBeReceive, amountReceivable, branchId, payrollPeriod, payrollDate) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";        
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(salaryQuery);
            pstmt.setString(1, id);
            pstmt.setString(2, attendanceDateFrom);
            pstmt.setString(3, attendanceDateTo);
            pstmt.setDouble(4, basicSalary);
            pstmt.setDouble(5, halfMonthSalary);
            pstmt.setDouble(6, philhealthContribution);
            pstmt.setDouble(7, sssContribution);
            pstmt.setDouble(8, hdmfContribution);
            pstmt.setDouble(9, totalAbsences);
            pstmt.setInt(10, numberOfDays);
            pstmt.setDouble(11, grossPay);
            pstmt.setDouble(12, tax);
            pstmt.setDouble(13, cashBond);
            pstmt.setDouble(14, totalLatesDeduction);
            pstmt.setDouble(15, totalUndertimeDeduction);
            pstmt.setDouble(16, totalOvertimePaid);
            pstmt.setDouble(17, totalNightDifferentialPaid);
            pstmt.setDouble(18, totalLegalHolidayPaid);
            pstmt.setDouble(19, totalSpecialHolidayPaid);
            pstmt.setDouble(20, totalWorkingDayOffPaid);
            pstmt.setDouble(21, advances);
            pstmt.setDouble(22, allowance);
            pstmt.setDouble(23, afl);
            pstmt.setDouble(24, netSalary);
            pstmt.setDouble(25, amountToBeReceive);
            pstmt.setDouble(26, amountReceivable);
            pstmt.setInt(27, branchId);
            pstmt.setString(28, paydayPeriod);
            pstmt.setString(29, salaryDate);
            pstmt.executeUpdate();
            
            String salaryId = null;            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT last_insert_id() AS id FROM salary ");
            while(rs.next()){
                salaryId = rs.getString("id");
            }
            
            for(int i = 0; i < dateList.size(); i++){
                String policy = null, holiday = null;
                if(!policyList.get(i).equals("null")){ policy = policyList.get(i).toString(); }
                
                if(!holidayList.get(i).equals("null")){ holiday = holidayList.get(i).toString(); }
                
                if(!latesList.get(i).equals("")){ lates = Double.parseDouble(latesList.get(i).toString()); }
                else{ lates = 0; }
                
                if(!undertimeList.get(i).toString().isEmpty()){ undertime = Double.parseDouble(undertimeList.get(i).toString()); }
                else{ undertime = 0; }
                
                if(!overtimeList.get(i).toString().isEmpty()){ overtime = Double.parseDouble(overtimeList.get(i).toString()); }
                else{ overtime = 0; }
                
                if(!nightDifferentialList.get(i).toString().isEmpty()){ nightDifferential = Double.parseDouble(nightDifferentialList.get(i).toString()); }
                else{ nightDifferential = 0; }
                
                if(!latesDeductionList.get(i).toString().isEmpty()){ 
                    latesDeduction = Double.valueOf(latesDeductionList.get(i)).doubleValue();
                }
                
                if(!undertimeDeductionList.get(i).toString().isEmpty()){
                    undertimeDeduction = Double.valueOf(undertimeDeductionList.get(i)).doubleValue();
                }
                
                if(!overtimePaidList.get(i).toString().isEmpty()){
                    overtimePaid = Double.valueOf(overtimePaidList.get(i)).doubleValue();
                }
                
                if(!nightDifferentialPaidList.get(i).toString().isEmpty()){
                    nightDifferentialPaid = Double.valueOf(nightDifferentialPaidList.get(i)).doubleValue();
                }
                
                if(!legalHolidayPaidList.get(i).toString().isEmpty()){
                    legalHolidayPaid = Double.valueOf(legalHolidayPaidList.get(i)).doubleValue();
                }
                
                if(!specialHolidayPaidList.get(i).toString().isEmpty()){
                    specialHolidayPaid = Double.valueOf(specialHolidayPaidList.get(i)).doubleValue();
                }
                
                if(!workingDayOffPaidList.get(i).toString().isEmpty()){
                    workingDayOffPaid = Double.valueOf(workingDayOffPaidList.get(i)).doubleValue();
                }
                
                if(!holidayLegalHolidayPaidList.get(i).toString().isEmpty()){
                    psHolidayPaid = Double.valueOf(holidayLegalHolidayPaidList.get(i)).doubleValue();
                }
                                               
                pstmt = conn.prepareStatement(attendanceQuery);
                pstmt.setString(1, salaryId);
                pstmt.setString(2, dateList.get(i).toString());
                pstmt.setString(3, policy);
                pstmt.setString(4, holiday);
                pstmt.setString(5, lpList.get(i).toString());
                pstmt.setDouble(6, lates);
                pstmt.setString(7, upList.get(i).toString());
                pstmt.setDouble(8, undertime);
                pstmt.setString(9, opList.get(i).toString());
                pstmt.setDouble(10, overtime);
                pstmt.setDouble(11, nightDifferential);
                pstmt.setDouble(12, latesDeduction);
                pstmt.setDouble(13, undertimeDeduction);
                pstmt.setDouble(14, overtimePaid);
                pstmt.setDouble(15, nightDifferentialPaid);
                pstmt.setDouble(16, legalHolidayPaid);
                pstmt.setDouble(17, specialHolidayPaid);
                pstmt.setDouble(18, workingDayOffPaid);
                pstmt.setDouble(19, psHolidayPaid);
                pstmt.executeUpdate();           
            }                         
                        
            Double forAdjustments = 0.0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ifnull(forAdjustments, 0) AS forAdjustments FROM salary WHERE employeeId = '"+id+"' "
                    + "AND rowStatus IS NULL ORDER BY payrollDate DESC, id DESC LIMIT 1, 1");
            while(rs.next()){
                forAdjustments = Double.parseDouble(rs.getString("forAdjustments"));                
            }
            
            if(forAdjustments != 0){
                pstmt = conn.prepareStatement("INSERT INTO adjustments(salaryId, amount, remarks, datePosted) VALUES(?, ?, ?, now())");
                pstmt.setString(1, salaryId);
                pstmt.setDouble(2, forAdjustments);
                pstmt.setString(3, "for adjustments column");
                pstmt.executeUpdate();
                
                amountReceivable = Math.round((amountReceivable + forAdjustments)*100.0)/100.0;
                amountToBeReceive = amountReceivable;
                
                pstmt = conn.prepareStatement("UPDATE salary SET amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ");
                pstmt.setDouble(1, amountToBeReceive);
                pstmt.setDouble(2, amountReceivable);
                pstmt.setString(3, salaryId);
                pstmt.executeUpdate();
            }
            
            if(salaryRowId != null){
                Double previousAmountReceived = salaryDAO.getAmountReceivable(Integer.parseInt(salaryRowId));
                Double previousAdvances = salaryDAO.getTotalAdvancesPerSalaryId(Integer.parseInt(salaryRowId));
                Double previousAdjustments = salaryDAO.getTotalAdjustmentsPerSalaryId(Integer.parseInt(salaryRowId));
                amountReceivable = (amountReceivable - previousAdvances) + previousAdjustments ;
                                
                Double adjustments = Math.round((amountReceivable - previousAmountReceived)*100.0)/100.0; 
                
                pstmt = conn.prepareStatement("UPDATE salary SET advances = ?, amountToBeReceive = ?, "
                        + "amountReceivable = ? WHERE id = ? ");
                pstmt.setDouble(1, previousAdvances);
                pstmt.setDouble(2, amountReceivable);
                pstmt.setDouble(3, previousAmountReceived);
                pstmt.setString(4, salaryId);
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("INSERT INTO adjustments(salaryId, amount, remarks, datePosted) VALUES(?, ?, ?, now())");
                pstmt.setString(1, salaryId);
                pstmt.setDouble(2, previousAdjustments);
                pstmt.setString(3, "from edited salary");
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("UPDATE salary SET forAdjustments = ? WHERE id = ? ");
                pstmt.setDouble(1, adjustments);
                pstmt.setString(2, salaryId);
                pstmt.executeUpdate();
                
                AdvancesDAO  advancesDAO = new AdvancesDAO();
                Boolean checkForAdvancesIfNotExist = advancesDAO.checkIfAdvancesExistForSalary(Integer.parseInt(salaryRowId));
                if(!checkForAdvancesIfNotExist){
                    List<Advances> advancesList = advancesDAO.getAdvancesBySalaryId(Integer.parseInt(salaryRowId));
                    for(Advances a : advancesList){
                        pstmt = conn.prepareStatement("INSERT INTO advances(salaryId, amount, advancesType, particulars, datePosted, "
                                + "employeeId, rowStatus, remarks, dateRemoved) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pstmt.setString(1, salaryId);
                        pstmt.setDouble(2, a.getAmount());
                        pstmt.setString(3, a.getAdvancesType());
                        pstmt.setString(4, a.getParticulars());
                        pstmt.setString(5, a.getDatePosted());
                        pstmt.setString(6, a.getEmployeeId());
                        pstmt.setString(7, a.getRowStatus());
                        pstmt.setString(8, a.getRemarks());
                        pstmt.setString(9, a.getDateRemoved());
                        pstmt.executeUpdate();
                    }
                    
                }
                                
                pstmt = conn.prepareStatement("UPDATE salary SET actionTaken = 'adjusted' WHERE id = '"+salaryId+"' ");
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("UPDATE salary SET actionTaken = 'previous' WHERE id = '"+salaryRowId+"' ");
                pstmt.executeUpdate();
            }
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {  
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Connection rollback...");
                } catch (SQLException ex1) {
                    Logger.getLogger(SalaryComputation.class.getName()).log(Level.SEVERE, null, ex1);
                }                
            }
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryComputation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    private Double getBasicSalary(Double salary, String wageEntry){
        double basic_salary;    
        if(wageEntry.equals("daily")){            
            basic_salary = Math.round((((salary * 314) / 12)*100.0)/100.0);
        }else{
            basic_salary = salary;
        }
        return basic_salary;    
    }
    
    private Double getHalfMonthSalary(String wageEntry, List policyList, double salary, double dailyWage, List dateList, String id){
        double halfMonthSalary = 0;
        
        String holiday = null;        
        
        if(wageEntry.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).equals("paid-vacation-leave") || 
                        policyList.get(i).equals("paid-sick-leave")){
                    halfMonthSalary = halfMonthSalary + dailyWage;                   
                }
            }            
            halfMonthSalary = halfMonthSalary + totalHolidayLegalHolidayPaid;
        }else{
            String dateEmployed = query.getEmployeesEntryDate(id);
            String dateResigned = query.getEmployeesEndDate(id);
            Boolean checkIfResigned = query.checkEmployeesStatus(id);
            
            if(checkIfResigned){
                Boolean checkResultNewEmployee = checkEntryDateIfBetweenDateLists(dateList, dateEmployed);
                if(checkResultNewEmployee == true){
                    System.out.println("If new Employee!");
                    int numberOfDays = getNumberOfDaysForMonthlyEmployee(dateList, policyList);
                    double salaryPerDay = new Double(df.format((salary/314) * 12));
                    halfMonthSalary = salaryPerDay * numberOfDays;
                }else{
                    System.out.println("If old Employee!");
                    halfMonthSalary = salary / 2;
                }
            }else{  
                Boolean checkResultResignedEmployee = checkEntryDateIfBetweenDateLists(dateList, dateResigned);
                if(checkResultResignedEmployee == true){
                    //System.out.println("If resigned Employee!");
                    int numberOfDays = getNumberOfDaysForMonthlyEmployee(dateList, policyList);
                    double salaryPerDay = new Double(df.format((salary/314) * 12));
                    halfMonthSalary = salaryPerDay * numberOfDays;
                }else{
                    //System.out.println("If not resigned Employee!");
                    halfMonthSalary = salary / 2;
                }
            }
            
        }
        return halfMonthSalary;
    }
    
    private Double getTaxableSalary(double salary, String wageEntry, List policyList, double halfSalary, String payday){
        double salaryAfterContribution;
        double salaryAfterAbsences = 0;
        double absences = 0;
        totalAbsences = 0.0;
        
        if(wageEntry.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).equals("paid-vacation-leave") || 
                        policyList.get(i).equals("paid-sick-leave") || policyList.get(i).equals("paternity-leave")){
                    salaryAfterAbsences = salaryAfterAbsences + salary;
                }
            }            
            if(payday.equals("15th of the month")){
                salaryAfterAbsences = salaryAfterAbsences - (totalDeduction) + (totalAddition + totalHolidayLegalHolidayPaid);
            }else{
                salaryAfterAbsences = salaryAfterAbsences - (totalDeduction) + (totalAddition + totalHolidayLegalHolidayPaid);
            }            
        }else{
            salary = new Double(df.format((salary * 12)/314));
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("absent") || policyList.get(i).equals("unpaid-vacation-leave") || 
                        policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended")){
                    absences = absences + salary;
                }
            }
            /*if(payday.equals("15th of the month")){
                salaryAfterContribution = halfSalary;
            }else{
                salaryAfterContribution = halfSalary;
            } */
            salaryAfterAbsences = (halfSalary - absences - totalDeduction) + (totalAddition);
            totalAbsences = absences;
        }
        
        return salaryAfterAbsences;
    }    
    
    private Double getDeduction(){ return totalDeduction; }
    
    private Double getAddition(){ return totalAddition; }    
        
    private Integer getMonthDifference(Date date1, Date date2){   
        Calendar firstDate = new GregorianCalendar(date2.getYear(), date2.getMonth(), date2.getDay());
        Calendar secondDate = new GregorianCalendar(date1.getYear(), date1.getMonth(), date1.getDay());
        int months  = (firstDate.get(Calendar.YEAR) - secondDate.get(Calendar.YEAR)) * 12 + 
                (firstDate.get(Calendar.MONTH)- secondDate.get(Calendar.MONTH)) + 
                (firstDate.get(Calendar.DAY_OF_MONTH) >= secondDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 
        return months;
    }
    
    public Double getTax(String status, Double salary){
        Connection conn = getConnection.connection();
        double tax = 0.0;
        double taxRateExemption = 0;
        double taxRate = 0;
        double taxableRate = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT getExemption('"+status+"', "+salary+") AS exemption ");
            while(rs.next()){
                taxRateExemption = Double.parseDouble(rs.getString("exemption"));
            }
            
            rs = stmt.executeQuery(" SELECT getTaxRate('"+status+"', "+salary+") as taxRate ");
            while(rs.next()){
                taxRate = Double.parseDouble(rs.getString("taxRate"));
            }
            
            rs = stmt.executeQuery(" SELECT getTaxableRate('"+status+"', "+salary+") as taxableRate ");
            while(rs.next()){
                taxableRate = Double.parseDouble(rs.getString("taxableRate"));
            }
            
            tax = ((salary - taxableRate) * taxRate) + taxRateExemption;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryComputation.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(SalaryComputation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tax;
    }
    
    private Double getAllowance(List policyList, String allowanceStatus, Double allowance_entry){
        double allowance = 0;
        if(allowanceStatus.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday")){
                    allowance = allowance + allowance_entry;
                }
            }            
        }else{
            double allowance_entry_per_day = new Double(df.format((allowance_entry * 12)/314));
            Double halfAllowance = allowance_entry / 2;
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("absent") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("holiday") || 
                    policyList.get(i).equals("paid-vacation-leave") || policyList.get(i).equals("paid-sick-leave") || 
                    policyList.get(i).equals("unpaid-vacation-leave") || policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended")){
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                    //allowancePerDay = allowancePerDay + allowance_entry_per_day;
                }
            }
            allowance = halfAllowance;
        }
        return allowance;
    }
    
    private Double getAllowanceForLiquidationDeduction(List policyList, Double allowance_entry){
        double allowanceToBeDeductedPerDay = 0;
        double allowance_entry_per_day = new Double(df.format((allowance_entry * 12)/314));
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("unpaid-vacation-leave") || 
                        policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended")){
                allowanceToBeDeductedPerDay =  allowanceToBeDeductedPerDay + allowance_entry_per_day;
            }
        }
        
        return allowanceToBeDeductedPerDay;
    }
    
    private Integer getNumberOfDays(List dateList, List policyList){
        int numberOfDays = dateList.size();
        int count = 0;
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("day-off") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("holiday") || 
                    policyList.get(i).equals("paid-vacation-leave") || policyList.get(i).equals("paid-sick-leave") || 
                    policyList.get(i).equals("unpaid-vacation-leave") || policyList.get(i).equals("unpaid-sick-leave")){
                count++;
            }
        }
        numberOfDays = numberOfDays - count;
        return numberOfDays;
    }
    
    private Integer getNumberOfDaysForMonthlyEmployee(List dateList, List policyList){
        int numberOfDays = dateList.size();
        int count = 0;
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("paid-vacation-leave") || 
                    policyList.get(i).equals("paid-sick-leave") || policyList.get(i).equals("unpaid-vacation-leave") || 
                    policyList.get(i).equals("unpaid-sick-leave")|| policyList.get(i).equals("suspended")){
                count++;
            }
        }
        numberOfDays = numberOfDays - count;
        return numberOfDays;
    }
    
    private Boolean checkEntryDateIfBetweenDateLists(List dateList, String dateEmployed){
        Boolean checkResult = false;
        for(int i = 0; i < dateList.size(); i++){
            if(dateList.get(i).toString().trim().equals(dateEmployed.trim())){
                checkResult = true;
            }
        }
        return checkResult;
    }
}
