/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Timekeeping;
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
        String d = "2009-08-24";
        Date date = util.parsingDate(d);
        
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
    
    public List<Timekeeping> getAttendanceByEmployee(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT payrollId, attendancePeriodFrom, attendancePeriodTo "
//                    + "lates, undertime, overtime, nightDifferential, latesDeduction, undertimeDeduction, "
//                    + "overtimePaid, nightDifferentialPaid, legalHolidayPaid, specialHolidayPaid, "
//                    + "workingDayOffPaid, nonWorkingHolidayPaid "
                    + "FROM timekeeping_summary WHERE currentStatus IS NULL AND "
                    + "employeeId = '"+employeeId+"' ORDER BY payrollId DESC");
            while(rs.next()){
                Timekeeping t = new Timekeeping();
                t.setId(util.convertStringToInteger(rs.getString("payrollId")));
                t.setAttendancePeriodFrom(util.parsingDate(rs.getString("attendancePeriodFrom")));
                t.setAttendancePeriodTo(util.parsingDate(rs.getString("attendancePeriodTo")));
//                t.setLates(util.convertStringToDouble(rs.getString("lates")));
//                t.setUndertime(util.convertStringToDouble(rs.getString("undertime")));
//                t.setOvertime(util.convertStringToDouble(rs.getString("overtime")));
//                t.setNightDifferential(util.convertStringToDouble(rs.getString("nightDifferential")));
//                t.setLateDeduction(util.convertStringToDouble(rs.getString("latesDeduction")));
//                t.setUndertimeDeduction(util.convertStringToDouble(rs.getString("undertimeDeduction")));
//                t.setOvertimePaid(util.convertStringToDouble(rs.getString("overtimePaid")));
//                t.setNightDifferentialPaid(util.convertStringToDouble(rs.getString("nightDifferentialPaid")));
//                t.setLegalHolidayPaid(util.convertStringToDouble(rs.getString("legalHolidayPaid")));
//                t.setSpecialHolidayPaid(util.convertStringToDouble(rs.getString("specialHolidayPaid")));
//                t.setWorkingDayOffPaid(util.convertStringToDouble(rs.getString("workingDayOffPaid")));
//                t.setNonWorkingHolidayPaid(util.convertStringToDouble(rs.getString("nonWorkingHolidaypaid")));
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
                t.setLatesLegalHolidayDeduction(util.convertStringToDouble(rs.getString("latesLegalHolidayDeduction")));
                t.setLatesSpecialHolidayDeduction(util.convertStringToDouble(rs.getString("latesSpecialHolidayDeduction")));
                t.setLatesWorkingDayOffDeduction(util.convertStringToDouble(rs.getString("latesWorkingDayOffDeduction")));
                t.setUndertimeLegalHolidayDeduction(util.convertStringToDouble(rs.getString("undertimeLegalHolidayDeduction")));
                t.setUndertimeSpecialHolidayDeduction(util.convertStringToDouble(rs.getString("undertimeSpecialHolidayDeduction")));
                t.setUndertimeWorkingDayOffDeduction(util.convertStringToDouble(rs.getString("undertimeWorkingDayOffDeduction")));
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
    
    public double getNonWorkingHolidayPay(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double nonWorkingHolidayPay = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT SUM(psHolidayPaid) AS nonWorkingHolidayPay FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                nonWorkingHolidayPay = util.convertStringToDouble(rs.getString("nonWorkingHolidayPay"));
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
        
        return nonWorkingHolidayPay;
    }
    
    public double getTotalLates(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT sum(lates) AS lates FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("lates"));
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
        
        return total;
    }
    
    public double getTotalUndertime(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT sum(undertime) AS undertime FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("undertime"));
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
        
        return total;
    }
    
    public double getTotalOvertime(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT sum(overtime) AS overtime FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("overtime"));
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
        
        return total;
    }
    
    public double getTotalNightDifferential(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT sum(nightDifferential) AS nightDifferential FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("nightDifferential"));
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
        
        return total;
    }
    
    public double getTotalLatesDeduction(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(latesDeduction),2) AS latesDeduction, "
                    + "sum(ifnull(latesLegalHolidayDeduction, 0)) AS latesLH, "
                    + "sum(ifnull(latesSpecialHolidayDeduction, 0)) AS latesSH, "
                    + "sum(ifnull(latesWorkingDayOffDeduction, 0)) AS latesWDO "
                    + "FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){                
                total = util.convertStringToDouble(rs.getString("latesDeduction")) + 
                        util.convertStringToDouble(rs.getString("latesLH")) + 
                        util.convertStringToDouble(rs.getString("latesSH")) + 
                        util.convertStringToDouble(rs.getString("latesWDO"));
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
        
        return total;
    }
    
    public double getTotalUndertimeDeduction(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(undertimeDeduction),2) AS undertimeDeduction, "
                    + "sum(ifnull(undertimeLegalHolidayDeduction, 0)) AS undertimeLH, "
                    + "sum(ifnull(undertimeSpecialHolidayDeduction, 0)) AS undertimeSH, "
                    + "sum(ifnull(undertimeWorkingDayOffDeduction, 0)) AS undertimeWDO "
                    + "FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("undertimeDeduction")) + 
                        util.convertStringToDouble(rs.getString("undertimeLH")) + 
                        util.convertStringToDouble(rs.getString("undertimeSH")) + 
                        util.convertStringToDouble(rs.getString("undertimeWDO"));
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
        
        return total;
    }
    
    public double getTotalOvertimePaid(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(overtimePaid),2) AS overtimePaid FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("overtimePaid"));
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
        
        return total;
    }
    
    public double getTotalNightDifferentialPaid(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(nightDifferentialPaid),2) AS nightDifferentialPaid FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("nightDifferentialPaid"));
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
        
        return total;
    }
    
    public double getTotalLegalHolidayPaid(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(legalHolidayPaid),2) AS legalHolidayPaid FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("legalHolidayPaid"));
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
        
        return total;
    }
    
    public double getTotalSpecialHolidayPaid(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(specialHolidayPaid),2) AS specialHolidayPaid FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("specialHolidayPaid"));
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
        
        return total;
    }
    
    public double getTotalWorkingDayOffPaid(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(workingDayOffPaid),2) AS workingDayOffPaid FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("workingDayOffPaid"));
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
        
        return total;
    }
    
    public double getTotalPsHolidayPaid(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT round(sum(psHolidayPaid),2) AS psHolidayPaid FROM timekeeping_table WHERE payrollId = "+payrollId+"");
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("psHolidayPaid"));
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
        
        return total;
    }
}
