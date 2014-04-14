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
    
    ConvertionUtilities conUtil = new ConvertionUtilities();
    OpenHrisUtilities util = new OpenHrisUtilities();
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
                result = true;                
            }
            
            if(result == true){
                stmt = conn.createStatement();
                rs = stmt.executeQuery(queryUserAccess);
                while(rs.next()){
                    UserAccessControl.setTimekeeping(conUtil.convertStringToBoolean(rs.getString("timekeeping")));
                    UserAccessControl.setContributions(conUtil.convertStringToBoolean(rs.getString("contributions")));
                    UserAccessControl.setCashBond(conUtil.convertStringToBoolean(rs.getString("cashBond")));
                    UserAccessControl.setAdjustment(conUtil.convertStringToBoolean(rs.getString("adjustment")));
                    UserAccessControl.setAdvances(conUtil.convertStringToBoolean(rs.getString("advances")));
                    UserAccessControl.setPayroll(conUtil.convertStringToBoolean(rs.getString("payroll")));
                    UserAccessControl.setEditEmployeesInfo(conUtil.convertStringToBoolean(rs.getString("editEmployeesInfo")));
                    UserAccessControl.setAddEvents(conUtil.convertStringToBoolean(rs.getString("addEvents")));
                    UserAccessControl.setAdjustPayroll(conUtil.convertStringToBoolean(rs.getString("adjustPayroll")));
                 
                    UserAccessControl.setMainMenu(util.convertStringToBoolean(rs.getString("mainMenu")));
                    UserAccessControl.setTimekeepingMenu(util.convertStringToBoolean(rs.getString("timekeepingMenu")));
                    UserAccessControl.setPayrollMenu(util.convertStringToBoolean(rs.getString("payrollMenu")));
                    UserAccessControl.setContributionsMenu(util.convertStringToBoolean(rs.getString("contributionsMenu")));
                    UserAccessControl.setEventsMenu(util.convertStringToBoolean(rs.getString("eventsMenu")));
                    UserAccessControl.setLoansMenu(util.convertStringToBoolean(rs.getString("loansMenu")));
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
