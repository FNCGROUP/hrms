/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.dbconnection;

import com.hrms.admin.classes.AdvanceUserAccessControl;
import com.openhris.administrator.model.UserAccessControl;
import com.hrms.classes.GlobalVariables;
import com.hrms.utilities.ConvertionUtilities;
import com.openhris.administrator.model.UserToolbarMenuAccess;
import com.openhris.commons.OpenHrisUtilities;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class AuthenticateLogin implements Serializable {
    
//    ConvertionUtilities conUtil = new ConvertionUtilities();
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    GetSQLConnection connect = new GetSQLConnection();
    Connection conn;
        
    public Boolean login(String username, String password){
        conn = connect.connection();
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        String queryLogin = "SELECT * FROM user_ WHERE username_ = '"+username+"' AND password_ = '"+password+"' ";
        String queryUserAccess = "SELECT * FROM user_access_control WHERE username = '"+username+"' ";
        String user = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryLogin);
            while(rs.next()){
                GlobalVariables.setUsername(rs.getString("username_"));
                GlobalVariables.setUserRole(rs.getString("userRole"));
                GlobalVariables.setUserId(utilities.convertStringToInteger(rs.getString("id")));
                result = true;                
            }
            
            if(result == true){
                stmt = conn.createStatement();
                rs = stmt.executeQuery(queryUserAccess);
                while(rs.next()){
                    UserAccessControl.setTimekeeping(utilities.convertStringToBoolean(rs.getString("timekeeping")));
                    UserAccessControl.setContributions(utilities.convertStringToBoolean(rs.getString("contributions")));
                    UserAccessControl.setCashBond(utilities.convertStringToBoolean(rs.getString("cashBond")));
                    UserAccessControl.setAdjustment(utilities.convertStringToBoolean(rs.getString("adjustment")));
                    UserAccessControl.setAdvances(utilities.convertStringToBoolean(rs.getString("advances")));
                    UserAccessControl.setPayroll(utilities.convertStringToBoolean(rs.getString("payroll")));
                    UserAccessControl.setEditEmployeesInfo(utilities.convertStringToBoolean(rs.getString("editEmployeesInfo")));
                    UserAccessControl.setAddEvents(utilities.convertStringToBoolean(rs.getString("addEvents")));
                    UserAccessControl.setAdjustPayroll(utilities.convertStringToBoolean(rs.getString("adjustPayroll")));
                    UserAccessControl.setLockPayroll(utilities.convertStringToBoolean(rs.getString("lockPayroll")));
                    UserAccessControl.setEditAttendance(utilities.convertStringToBoolean(rs.getString("isEditAttendance")));
                    UserAccessControl.setEditSss(utilities.convertStringToBoolean(rs.getString("isEditSss")));
                    UserAccessControl.setEditPhic(utilities.convertStringToBoolean(rs.getString("isEditPhic")));
                    UserAccessControl.setEditHdmf(utilities.convertStringToBoolean(rs.getString("isEditHdmf")));
                 
                    UserAccessControl.setMainMenu(utilities.convertStringToBoolean(rs.getString("mainMenu")));
                    UserAccessControl.setTimekeepingMenu(utilities.convertStringToBoolean(rs.getString("timekeepingMenu")));
                    UserAccessControl.setPayrollMenu(utilities.convertStringToBoolean(rs.getString("payrollMenu")));
                    UserAccessControl.setContributionsMenu(utilities.convertStringToBoolean(rs.getString("contributionsMenu")));
                    UserAccessControl.setEventsMenu(utilities.convertStringToBoolean(rs.getString("eventsMenu")));
                    UserAccessControl.setLoansMenu(utilities.convertStringToBoolean(rs.getString("loansMenu")));                    
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticateLogin.class.getName()).log(Level.SEVERE, null, ex);
        } finally { 
            try {
                    if(conn != null || !conn.isClosed()){
                        stmt.close();
                        rs.close();
                        conn.close();
                    }
            } catch (SQLException ex) {
                Logger.getLogger(AuthenticateLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return result;
    }  
}
