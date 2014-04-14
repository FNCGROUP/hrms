/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.queries;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.entities.Attendance;
import com.hrms.utilities.ConvertionUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class AttendanceDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    
    public AttendanceDAO(){        
    }
    
    public List<Attendance> getAttendanceOfEmployee(Integer salaryId){
        List<Attendance> attendanceList = new ArrayList<Attendance>();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT attendanceDate, policy, holiday, latesPremium, ifnull(lates, 0) AS lates, "
                    + "undertimePremium, ifnull(undertime, 0) AS undertime, overtimePremium, ifnull(overtime, 0) AS overtime, "
                    + "ifnull(nightDifferential, 0) AS nightDifferential, ifnull(latesDeduction, 0) AS latesDeduction, "
                    + "ifnull(undertimeDeduction, 0) AS undertimeDeduction, ifnull(overtimePaid, 0) AS overtimePaid, "
                    + "ifnull(nightDifferentialPaid, 0) AS nightDifferentialPaid, ifnull(legalHolidayPaid, 0) AS legalHolidayPaid, "
                    + "ifnull(specialHolidayPaid, 0) AS specialHolidayPaid, ifnull(workingDayOffPaid, 0) AS workingDayOffPaid, "
                    + "ifnull(psHolidayPaid, 0) AS psHolidayPaid FROM attendance WHERE salaryId = '"+salaryId+"' ");
            while(rs.next()){
                Attendance attendance = new Attendance();
                attendance.attendanceDate =  rs.getString("attendanceDate");
                attendance.policy = rs.getString("policy");
                attendance.holiday = rs.getString("holiday");
                attendance.latesPremium = conUtil.convertStringToBoolean(rs.getString("latesPremium"));
                attendance.lates = conUtil.convertStringToDouble(rs.getString("lates"));
                attendance.undertimePremium = conUtil.convertStringToBoolean(rs.getString("undertimePremium"));
                attendance.undertime = conUtil.convertStringToDouble(rs.getString("undertime"));
                attendance.overtimePremium = conUtil.convertStringToBoolean(rs.getString("overtimePremium"));
                attendance.overtime = conUtil.convertStringToDouble(rs.getString("overtime"));
                attendance.nightDifferential = conUtil.convertStringToDouble(rs.getString("nightDifferential"));
                attendance.latesDeduction = conUtil.convertStringToDouble(rs.getString("latesDeduction"));
                attendance.undertimeDeduction = conUtil.convertStringToDouble(rs.getString("undertimeDeduction"));
                attendance.overtimePaid = conUtil.convertStringToDouble(rs.getString("overtimePaid"));
                attendance.nightDifferentialPaid = conUtil.convertStringToDouble(rs.getString("nightDifferentialPaid"));
                attendance.legalHolidayPaid = conUtil.convertStringToDouble(rs.getString("legalHolidayPaid"));
                attendance.specialHolidayPaid = conUtil.convertStringToDouble(rs.getString("specialHolidayPaid"));
                attendance.workingDayOffPaid = conUtil.convertStringToDouble(rs.getString("workingDayOffPaid"));
                attendance.psHolidayPaid = conUtil.convertStringToDouble(rs.getString("psHolidayPaid"));
                attendanceList.add(attendance);
            }            
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AttendanceDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return attendanceList;
    }
    
}
