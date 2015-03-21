/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.administrator.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.administrator.model.User;
import com.openhris.administrator.model.UserAdvanceAccess;
import com.openhris.administrator.model.UserToolbarMenuAccess;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceUpdateDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class AdministratorDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    
    public boolean checkEnteredPasswordIfCorrect(int userId, 
            String password){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = true;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM user_ where id = "+userId+" AND password_ = '"+password+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn == null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateUserPassword(int userId, 
            String password){        
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE user_ SET password_ = ? WHERE id = ?");
            pstmt.setString(1, password);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<User> getUserList(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<User> userList = new ArrayList<User>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user_access_control WHERE currentStatus IS NULL");
            while(rs.next()){
                User user = new User();
                user.setId(utilities.convertStringToInteger(rs.getString("uId")));
                user.setName(rs.getString("name"));
                user.setTrade(rs.getString("trade"));
                user.setBranch(rs.getString("branch"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return userList;
    }
    
    public boolean insertNewUser(String username, 
            String password, 
            String role, 
            String employeeId){        
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Statement stmt;
        ResultSet rs;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("INSERT INTO user_(username_, password_, userRole, employeeId) "
                    + "VALUE(?, ?, ?, ?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, employeeId);
            pstmt.executeUpdate();
            
            int userId = 0;            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT last_insert_id() AS id FROM user_ ");
            while(rs.next()){
                userId = Integer.parseInt(rs.getString("id"));
            }
            
            pstmt = conn.prepareStatement("INSERT INTO user_toolbar_menu_access(userId) VALUES(?)");
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("INSERT INTO user_advance_access(userId) VALUES(?)");
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public boolean checkUsernameIfExist(String username){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS result FROM user_ WHERE username_ = '"+username+"' ");
            while(rs.next()){
                if(rs.getString("result").equals("0")){
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateUserRole(int id, 
            String role){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_ SET userRole = ? WHERE id = ?");
            pstmt.setString(1, role);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean removeUser(int id){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("DELETE FROM user_ WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean insertNewBranchForAccessToUser(int userId,
            int tradeId, 
            int branchId){        
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO user_branch_access(userId, tradeId, branchId) VALUES(?, ?, ?)");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, tradeId);
            pstmt.setInt(3, branchId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public boolean checkBranchAssignedToUserIfExist(int branchId, 
            int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM user_branch_access WHERE "
                    + "branchId = "+branchId+" AND userId = "+userId+" ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = false;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return result;
    }
    
    public boolean insertNewCorporateForAccessToUser(int userId, 
            int corporateId){
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO user_corporate_access(userId, corporateId) VALUES(?, ?)");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean insertNewTradeForAccessToUser(int userId, 
            int corporateId, 
            int tradeId){        
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO user_trade_access(userId, corporateId, tradeId) VALUES(?, ?, ?)");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.setInt(3, tradeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public boolean checkTradeAssignedToUserIfExist(int tradeId, 
            int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM user_trade_access WHERE "
                    + "tradeId = "+tradeId+" AND userId = "+userId+" ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = false;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return result;
    }
    
    public boolean checkCorporateAssignedToUserIfExist(int corporateId, 
            int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM user_corporate_access WHERE "
                    + "corporateId = "+corporateId+" AND userId = "+userId+" ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = false;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return result;
    }
    
    public int getUserId(String username){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        int userId = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id FROM user_ WHERE username_ = '"+username+"' ");
            while(rs.next()){
                userId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
        
        return userId;
    }
    
    public List<UserToolbarMenuAccess> getListOfUserToolbarMenuAccess(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<UserToolbarMenuAccess> userToolbarMenuAccessList = new ArrayList<UserToolbarMenuAccess>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user_access_control");
            while(rs.next()){
                UserToolbarMenuAccess utma = new UserToolbarMenuAccess();
                utma.setId(utilities.convertStringToInteger(rs.getString("uId")));
                utma.setName(rs.getString("name"));
                utma.setUsername(rs.getString("username"));
                utma.setRole(rs.getString("role"));
                utma.setMainMenu(utilities.convertStringToBoolean(rs.getString("mainMenu")));
                utma.setTimekeepingMenu(utilities.convertStringToBoolean(rs.getString("timekeepingMenu")));
                utma.setPayrollMenu(utilities.convertStringToBoolean(rs.getString("payrollMenu")));
                utma.setLoansMenu(utilities.convertStringToBoolean(rs.getString("loansMenu")));
                utma.setEventsMenu(utilities.convertStringToBoolean(rs.getString("eventsMenu")));
                utma.setContributionsMenu(utilities.convertStringToBoolean(rs.getString("contributionsMenu")));
                userToolbarMenuAccessList.add(utma);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return userToolbarMenuAccessList;
    }
    
    public boolean allowAccessOfUserToolbarMenu(int userId, 
            String column, 
            boolean isAllowed){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_toolbar_menu_access SET "+column+" = ? WHERE userId = ?");
            pstmt.setString(1, String.valueOf(isAllowed));
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean allowAccessOfUserAdvanceAccess(int userId, 
            String column, 
            boolean isAllowed){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_advance_access SET "+column+" = ? WHERE userId = ?");
            pstmt.setString(1, String.valueOf(isAllowed));
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<UserAdvanceAccess> getListOfUserAdvanceAccess(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<UserAdvanceAccess> UserAdvanceAccessList = new ArrayList<UserAdvanceAccess>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user_access_control");
            while(rs.next()){
                UserAdvanceAccess uaa = new UserAdvanceAccess();
                uaa.setId(utilities.convertStringToInteger(rs.getString("uId")));
                uaa.setName(rs.getString("name"));
                uaa.setUsername(rs.getString("username"));
                uaa.setRole(rs.getString("role"));
                uaa.setTimekeeping(utilities.convertStringToBoolean(rs.getString("timekeeping")));
                uaa.setContributions(utilities.convertStringToBoolean(rs.getString("contributions")));
                uaa.setCashBond(utilities.convertStringToBoolean(rs.getString("cashBond")));
                uaa.setAdvances(utilities.convertStringToBoolean(rs.getString("advances")));
                uaa.setAdjustment(utilities.convertStringToBoolean(rs.getString("adjustment")));
                uaa.setPayroll(utilities.convertStringToBoolean(rs.getString("payroll")));
                uaa.setEditEmployeesInfo(utilities.convertStringToBoolean(rs.getString("editEmployeesInfo")));
                uaa.setAddEvents(utilities.convertStringToBoolean(rs.getString("addEvents")));
                uaa.setAdjustPayroll(utilities.convertStringToBoolean(rs.getString("adjustPayroll")));
                uaa.setLockPayroll(utilities.convertStringToBoolean(rs.getString("lockPayroll")));
                uaa.setEditAttendance(utilities.convertStringToBoolean(rs.getString("isEditAttendance")));
                uaa.setEditSss(utilities.convertStringToBoolean(rs.getString("isEditSss")));
                uaa.setEditPhic(utilities.convertStringToBoolean(rs.getString("isEditPhic")));
                uaa.setEditHdmf(utilities.convertStringToBoolean(rs.getString("isEditHdmf")));
                UserAdvanceAccessList.add(uaa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return UserAdvanceAccessList;
    }

    public boolean getUserAdvanceAccess(int userId, String column){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT "+column+" FROM user_advance_access WHERE userId = "+userId+" ");
            while(rs.next()){                
                result = utilities.convertStringToBoolean(rs.getString("lockPayroll"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdministratorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
