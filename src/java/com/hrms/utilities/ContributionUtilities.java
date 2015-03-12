/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.utilities;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.ui.VerticalLayout;
import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class ContributionUtilities extends VerticalLayout { 
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    ConvertionUtilities conUtil = new ConvertionUtilities();
    Boolean result = false;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;
    
    public ContributionUtilities(){
        
    }
    
    public Double getPhilhealth(Double salary){
        Connection conn = getConnection.connection();
        Double philhealthContribution = 0.0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT ifnull(getPhilhealth("+salary+"),0) AS philhealthContribution ");
            while(rs.next()){
                //philhealthContribution = conUtil.convertStringToDouble(rs.getString("philhealthContribution"));
                philhealthContribution = Double.parseDouble(rs.getString("philhealthContribution"));                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContributionUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ContributionUtilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return philhealthContribution;
    }
    
    public Double getSss(Double salary, String employeeId, String payrollDate){
        Connection conn = getConnection.connection();
        double sssContribution = 0;
        double previousGrossPay = 0;   
        double grossPay = 0;
        try {
            java.util.Date date = conUtil.parsingDate(payrollDate);
            Calendar cal = Calendar.getInstance();
            String calDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + 15;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id, halfMonthSalary, absences, totalOvertimePaid, totalLegalHolidayPaid, "
                    + "totalSpecialHolidayPaid, totalNightDifferentialPaid, totalWorkingDayOffPaid, totalLatesDeduction, "
                    + "totalUndertimeDeduction, payrollPeriod, payrollDate, rowStatus "
                    + "FROM payroll_table where employeeId = '"+employeeId+"' AND payrollDate = '"+calDate+"' ");
            while(rs.next()){
                previousGrossPay = (conUtil.convertStringToDouble(rs.getString("halfMonthSalary"))
                    +conUtil.convertStringToDouble(rs.getString("totalOvertimePaid"))
                    +conUtil.convertStringToDouble(rs.getString("totalLegalHolidayPaid"))
                    +conUtil.convertStringToDouble(rs.getString("totalSpecialHolidayPaid"))
                    +conUtil.convertStringToDouble(rs.getString("totalNightDifferentialPaid"))
                    +conUtil.convertStringToDouble(rs.getString("totalWorkingDayOffPaid"))) - 
                    (conUtil.convertStringToDouble(rs.getString("absences"))
                    +conUtil.convertStringToDouble(rs.getString("totalLatesDeduction"))
                    +conUtil.convertStringToDouble(rs.getString("totalUndertimeDeduction")));   
            }
                      
            grossPay = salary + previousGrossPay;
            
            rs = stmt.executeQuery("SELECT ifnull(getSss("+grossPay+"),0) AS sssContribution");
            while(rs.next()){
                sssContribution = Double.parseDouble(rs.getString("sssContribution"));
            }            
        } catch (SQLException ex) {
            Logger.getLogger(ContributionUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ContributionUtilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return sssContribution;
    }
    
    public Double getHdmf(Double salary){
        double hdmfContribution  = Math.round(((salary * .02)*100.0)/100.0);
        //double hdmfContribution  = 100.0;
        return hdmfContribution;
    }
    
}
