/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.timekeeping.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.timekeeping.model.Timekeeping;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class TimekeepingDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public Date getPreviousPayrollDate(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Date date = new Date();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT payrollDate FROM payroll_table WHERE employeeId = '"+employeeId+"' ORDER BY payrollDate DESC LIMIT 1");
            while(rs.next()){
                date = util.parsingDate(rs.getString("payrollDate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return date;
    }
    
    public List<Timekeeping> getAllAttendancePerPayroll(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT p.id AS payrollId, p.attendancePeriodFrom AS attendancePeriodFrom, "
                    + "p.attendancePeriodTo AS attendancePeriodTo, SUM(t.lates) AS lates, SUM(t.undertime) AS undertime, "
                    + "SUM(t.overtime) AS overtime, SUM(t.latesDeduction) AS latesDeduction, SUM(t.undertimeDeduction) AS undertimeDeduction, "
                    + "SUM(t.overtimePaid) AS overtimePaid, SUM(t.nightDifferentialPaid) AS nightDifferentialPaid, "
                    + "SUM(t.legalHolidayPaid) AS legalHolidayPaid, SUM(t.specialHolidayPaid) AS specialHolidayPaid, "
                    + "SUM(t.workingDayOffPaid) AS workingDayOffPaid, SUM(t.psHolidayPaid) AS psHolidayPaid "
                    + "FROM timekeeping_table t INNER JOIN payroll_table p ON t.payrollId = p.id ");
            while(rs.next()){
                Timekeeping t = new Timekeeping();
                t.setId(Integer.parseInt(rs.getString("payrollId")));
                t.setAttendancePeriodFrom(util.parsingDate(rs.getString("attendancePeriodFrom")));
                t.setAttendancePeriodTo(util.parsingDate(rs.getString("attendancePeriodTo")));
                t.setLates(Integer.parseInt(rs.getString("lates")));
                t.setUndertime(Integer.parseInt(rs.getString("undertime")));
                t.setOvertime(Integer.parseInt(rs.getString("overtime")));
                t.setLateDeduction(Double.parseDouble(rs.getString("latesDeduction")));
                t.setUndertimeDeduction(Double.parseDouble(rs.getString("undertimeDeduction")));
                t.setOvertimePaid(Double.parseDouble(rs.getString("overtimePaid")));
                t.setNightDifferentialPaid(Double.parseDouble(rs.getString("nightDifferentialPaid")));
                t.setLegalHolidayPaid(Double.parseDouble(rs.getString("legalHolidayPaid")));
                t.setSpecialHolidayPaid(Double.parseDouble(rs.getString("specialHolidayPaid")));
                t.setWorkingDayOffPaid(Double.parseDouble(rs.getString("workingDayOffPaid")));
                t.setNonWorkingHolidayPaid(Double.parseDouble(rs.getString("psHolidayPaid")));
                attendanceList.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return attendanceList;
    }
    
    public List<Timekeeping> getAttendanceByBranchAndEmployee(int branchId, String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String queryAttendanceList;
        
        if(employeeId == null){
            queryAttendanceList = "SELECT * FROM timekeeping_summary WHERE branchId = "+branchId+" "
                    + "ORDER BY payrollId DESC";
        } else {
            queryAttendanceList = "SELECT * FROM timekeeping_summary "
                    + "WHERE branchId = "+branchId+" AND employeeId = '"+employeeId+"' "
                    + "ORDER BY payrollId DESC";
        }
        
        List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryAttendanceList);
            while(rs.next()){
                Timekeeping t = new Timekeeping();
                t.setId(util.convertStringToInteger(rs.getString("payrollId")));
                t.setAttendancePeriodFrom(util.parsingDate(rs.getString("attendancePeriodFrom")));
                t.setAttendancePeriodTo(util.parsingDate(rs.getString("attendancePeriodTo")));
                t.setLates(util.convertStringToDouble(rs.getString("lates")));
                t.setUndertime(util.convertStringToDouble(rs.getString("undertime")));
                t.setOvertime(util.convertStringToDouble(rs.getString("overtime")));
                t.setNightDifferential(util.convertStringToDouble(rs.getString("nightDifferential")));
                t.setLateDeduction(util.convertStringToDouble(rs.getString("latesDeduction")));
                t.setUndertimeDeduction(util.convertStringToDouble(rs.getString("undertimeDeduction")));
                t.setOvertimePaid(util.convertStringToDouble(rs.getString("overtimePaid")));
                t.setNightDifferentialPaid(util.convertStringToDouble(rs.getString("nightDifferentialPaid")));
                t.setLegalHolidayPaid(util.convertStringToDouble(rs.getString("legalHolidayPaid")));
                t.setSpecialHolidayPaid(util.convertStringToDouble(rs.getString("specialHolidayPaid")));
                t.setWorkingDayOffPaid(util.convertStringToDouble(rs.getString("workingDayOffPaid")));
                t.setNonWorkingHolidayPaid(util.convertStringToDouble(rs.getString("nonWorkingHolidaypaid")));
                attendanceList.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return attendanceList;
    }
    
    public boolean checkAttendanceDateIfExist(String date, String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM payroll_table WHERE "
                    + "(attendancePeriodFrom <= '"+date+"' AND attendancePeriodTo >= '"+date+"') AND employeeId = '"+employeeId+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public List<Timekeeping> getTimekeepingRowData(String date, int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;         
        List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM timekeeping_table WHERE payrollId = "+payrollId+" AND attendanceDate = '"+date+"' ");
            while(rs.next()){
                Timekeeping t = new Timekeeping();
                t.setId(util.convertStringToInteger(rs.getString("payrollId")));
                t.setAttendanceDate(util.parsingDate(rs.getString("attendanceDate")));
                t.setPolicy(rs.getString("policy"));
                t.setHoliday(rs.getString("holiday"));
                t.setLates(util.convertStringToDouble(rs.getString("lates")));
                t.setUndertime(util.convertStringToDouble(rs.getString("undertime")));
                t.setOvertime(util.convertStringToDouble(rs.getString("overtime")));
                t.setNightDifferential(util.convertStringToDouble(rs.getString("nightDifferential")));
                t.setLateDeduction(util.convertStringToDouble(rs.getString("latesDeduction")));
                t.setUndertimeDeduction(util.convertStringToDouble(rs.getString("undertimeDeduction")));
                t.setOvertimePaid(util.convertStringToDouble(rs.getString("overtimePaid")));
                t.setNightDifferentialPaid(util.convertStringToDouble(rs.getString("nightDifferentialPaid")));
                t.setLegalHolidayPaid(util.convertStringToDouble(rs.getString("legalHolidayPaid")));
                t.setSpecialHolidayPaid(util.convertStringToDouble(rs.getString("specialHolidayPaid")));
                t.setWorkingDayOffPaid(util.convertStringToDouble(rs.getString("workingDayOffPaid")));
                t.setNonWorkingHolidayPaid(util.convertStringToDouble(rs.getString("psHolidayPaid")));
                attendanceList.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return attendanceList;
    }
}
