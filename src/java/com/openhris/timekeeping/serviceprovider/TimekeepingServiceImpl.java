/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.TimekeepingDAO;
import com.openhris.model.Timekeeping;
import com.openhris.service.TimekeepingService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class TimekeepingServiceImpl implements TimekeepingService {

    TimekeepingDAO timekeepingDAO = new TimekeepingDAO();
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    @Override
    public List<Timekeeping> getAttendancePerPayroll() {
        return timekeepingDAO.getAllAttendancePerPayroll();
    }

    @Override
    public List<Timekeeping> getAttendanceByEmployee(String employeeId) {
        return timekeepingDAO.getAttendanceByEmployee(employeeId);
    }

    @Override
    public boolean checkAttendanceDateIfExist(String date, String employeeId) {
        return timekeepingDAO.checkAttendanceDateIfExist(date, employeeId);
    }

    @Override
    public List<Timekeeping> getTimekeepingRowData(String date, int payrollId) {
        return timekeepingDAO.getTimekeepingRowData(date, payrollId);
    }    

    @Override
    public Date getPreviousPayrollDate(String employeeId) {
        return timekeepingDAO.getPreviousPayrollDate(employeeId);
    }

    @Override
    public double getNonWorkingHolidayPay(int payrollId) {
        return timekeepingDAO.getNonWorkingHolidayPay(payrollId);
    }

    @Override
    public double getTotalLates(int payrollId) {
        return timekeepingDAO.getTotalLates(payrollId);
    }

    @Override
    public double getTotalUndertime(int payrollId) {
        return timekeepingDAO.getTotalUndertime(payrollId);
    }

    @Override
    public double getTotalOvertime(int payrollId) {
        return timekeepingDAO.getTotalOvertime(payrollId);
    }

    @Override
    public double getTotalNightDifferential(int payrollId) {
        return timekeepingDAO.getTotalNightDifferential(payrollId);
    }

    @Override
    public double getTotalLatesDeduction(int payrollId) {
        return timekeepingDAO.getTotalLatesDeduction(payrollId);
    }

    @Override
    public double getTotalUndertimeDeduction(int payrollId) {
        return timekeepingDAO.getTotalUndertimeDeduction(payrollId);
    }

    @Override
    public double getTotalOvertimePaid(int payrollId) {
        return timekeepingDAO.getTotalOvertimePaid(payrollId);
    }

    @Override
    public double getTotalNightDifferentialPaid(int payrollId) {
        return timekeepingDAO.getTotalNightDifferentialPaid(payrollId);
    }

    @Override
    public double getTotalLegalHolidayPaid(int payrollId) {
        return timekeepingDAO.getTotalLegalHolidayPaid(payrollId);
    }

    @Override
    public double getTotalSpecialHolidayPaid(int payrollId) {
        return timekeepingDAO.getTotalSpecialHolidayPaid(payrollId);
    }

    @Override
    public double getTotalWorkingDayOffPaid(int payrollId) {
        return timekeepingDAO.getTotalWorkingDayOffPaid(payrollId);
    }

    @Override
    public double getTotalPsHolidayPaid(int payrollId) {
        return timekeepingDAO.getTotalPsHolidayPaid(payrollId);
    }

    @Override
    public double findTotalLatesLegalHolidayDeduction(int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT sum(ifnull(latesLegalHolidayDeduction, 0)) AS LatesLegalHolidayDeduction "
                    + "FROM timekeeping_table WHERE payrollId = ? ");
            pstmt.setInt(1, payrollId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("LatesLegalHolidayDeduction"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }

    @Override
    public double findTotalLatesSpecialHolidayDeduction(int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT sum(ifnull(latesSpecialHolidayDeduction, 0)) AS LatesSpecialHolidayDeduction "
                    + "FROM timekeeping_table WHERE payrollId = ? ");
            pstmt.setInt(1, payrollId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("LatesSpecialHolidayDeduction"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }

    @Override
    public double findTotalLatesWorkingDayOffDeduction(int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT sum(ifnull(latesWorkingDayOffDeduction, 0)) AS LatesWorkingDayOffDeduction "
                    + "FROM timekeeping_table WHERE payrollId = ? ");
            pstmt.setInt(1, payrollId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("LatesWorkingDayOffDeduction"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }

    @Override
    public double findTotalUndertimeLegalHolidayDeduction(int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT sum(ifnull(undertimeLegalHolidayDeduction, 0)) AS UndertimeLegalHolidayDeduction "
                    + "FROM timekeeping_table WHERE payrollId = ? ");
            pstmt.setInt(1, payrollId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("UndertimeLegalHolidayDeduction"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }

    @Override
    public double findTotalUndertimeSpecialHolidayDeduction(int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT sum(ifnull(undertimeSpecialHolidayDeduction, 0)) AS UndertimeSpecialHolidayDeduction "
                    + "FROM timekeeping_table WHERE payrollId = ? ");
            pstmt.setInt(1, payrollId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("UndertimeSpecialHolidayDeduction"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }

    @Override
    public double findTotalUndertimeWorkingDayOffDeduction(int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double total = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT sum(ifnull(undertimeWorkingDayOffDeduction, 0)) AS UndertimeWorkingDayOffDeduction "
                    + "FROM timekeeping_table WHERE payrollId = ? ");
            pstmt.setInt(1, payrollId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                total = util.convertStringToDouble(rs.getString("UndertimeWorkingDayOffDeduction"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
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
