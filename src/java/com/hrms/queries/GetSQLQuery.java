/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.queries;

import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.ConvertionUtilities;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class GetSQLQuery {
    
    GetSQLConnection getConnection = new GetSQLConnection();       
    
    ConvertionUtilities convertionUtilities = new ConvertionUtilities();
    public String username = System.getProperty("username");
    
    public Boolean saveHMOData(Double minSalary, Double maxSalary, Double baseSalary, Double totalMonthlyPremium, 
            Double employeeShare, Double employerShare){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String query = "INSERT INTO phic_table(minSalary, maxSalary, baseSalary, totalMonthlyPremium, employeeShare, employerShare) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, minSalary);
            pstmt.setDouble(2, maxSalary);
            pstmt.setDouble(3, baseSalary);
            pstmt.setDouble(4, totalMonthlyPremium);
            pstmt.setDouble(5, employeeShare);
            pstmt.setDouble(6, employerShare);
            pstmt.executeUpdate();           
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }    
    
    public Boolean saveSSSData(Double minSalary, Double maxSalary, Double monthlySalaryCredit, Double employeeContribution, 
            Double employerContribution, Double totalContribution){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String query = " INSERT INTO sss_table(minSalary, maxSalary, monthlySalaryCredit, employeeContribution, employerContribution, totalContribution) "
                + "VALUE(?, ?, ?, ?, ?, ?) ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, minSalary);
            pstmt.setDouble(2, maxSalary);
            pstmt.setDouble(3, monthlySalaryCredit);
            pstmt.setDouble(4, employeeContribution);
            pstmt.setDouble(5, employerContribution);
            pstmt.setDouble(6, totalContribution);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;        
    }
    
    public Boolean saveCorporateName(String corporateName){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String query = " INSERT INTO corporate_table(name) VALUE(?) ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, corporateName.toLowerCase());
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean saveTradeName(String corporateName, String tradeName){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String query = " INSERT INTO trade_table(corporateId, name) VALUE(getCorporateId('"+ corporateName +"'), ?) ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, tradeName.toLowerCase());
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean saveBranchName(int tradeId, String branchName){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String query = " INSERT INTO branch_table(tradeId, name) VALUE(?, ?) ";
        Boolean result = false;
        try {            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, tradeId);
            pstmt.setString(2, branchName.toLowerCase());
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }    
    
    public Boolean saveNewUser(String username, String password, String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String queryUser_ = " INSERT INTO user_(username_, password_, userRole, employeeId) VALUES(?, ?, ?, ?) ";
        String queryUserAccess = "INSERT INTO user_access(userId) VALUES(?)";
        String queryAdvanceUserAccess = "INSERT INTO advance_user_access(userId) VALUES(?)";
        Boolean result = false;
        String userId = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(queryUser_);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, "encoder");
            pstmt.setString(4, id);
            pstmt.executeUpdate();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id FROM user_ WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                userId = rs.getString("id");
            }
            
            pstmt = conn.prepareStatement(queryUserAccess);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryAdvanceUserAccess);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            if(conn != null){
                try {
                    conn.rollback();
                    System.out.println("Connection rollback...");
                } catch (SQLException ex1) {
                    Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
        
    public Integer getCoporateId(String corporateName){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        int corporateId = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT getCorporateId('"+corporateName+"') AS id ");
            while(rs.next()){
                corporateId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateId;
    }
    
    public List getCorporateNameListForUser(int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> corporateLists = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT ct.name AS name FROM user_corporate_access uca, corporate_table ct "
                    + "WHERE ct.id = uca.corporateId AND uca.userId = '"+userId+"' ");
            while(rs.next()){
                corporateLists.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateLists;
    }
    
    public List getCorporateLists(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> corporateLists = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT name FROM corporate_table ORDER BY name ASC");
            while(rs.next()){
                corporateLists.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateLists;
    }
    
    public List getTradeNameListForUser(String corporateName){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> tradeLists = new ArrayList<String>();
        int userId = getUserId(GlobalVariables.getUsername());
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT tt.name AS name FROM user_trade_access uta INNER JOIN trade_table tn ON uta.tradeId = tn.id "
                    + "INNER JOIN user_corporate_access uca ON uta.corporateId = uca.corporateId "
                    + "WHERE uta.corporateId = getCorporateId('"+corporateName+"') AND uta.userId = "+userId+" ");
            while(rs.next()){
                tradeLists.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeLists;
    }
    
    public List getTradeLists(String corporateName){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> tradeLists = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT name FROM trade_table WHERE corporateId = getCorporateId('"+ corporateName.toLowerCase() +"') "
                    + "ORDER BY name ASC");
            while(rs.next()){
                tradeLists.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeLists;
    }    
    
    public Integer getTradeId(String tradeName, String corporateName){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        int tradeId = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id FROM trade_table WHERE name = '"+tradeName+"' AND corporateId = getCorporateId('"+corporateName+"') ");
            while(rs.next()){
                tradeId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeId;
    }
    
    public List getBranchListForUser(int tradeId){
        List<String> branchLists = new ArrayList<String>();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT bt.name AS name FROM user_branch_access uba, branch_table bt "
                    + "WHERE uba.branchId = bt.id AND uba.tradeId = "+tradeId+" AND b.actionTaken IS NULL ");
            while(rs.next()){
                branchLists.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchLists;
    }
    
    public List getBranchLists(int id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> branchLists = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM branch_table WHERE tradeId = '"+id+"' AND actionTaken IS NULL ORDER BY name ASC");
            while(rs.next()){
                branchLists.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchLists;
    }
    
    public List getAdvancesTypeLists(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> advancesTypeLists = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT advancesType FROM advances_type ");
            while(rs.next()){
                advancesTypeLists.add(rs.getString("advancesType"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return advancesTypeLists;
    }
    
    public Integer getBranchId(String branchName, String tradeName, String corporateName){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        int branchId = 0;
        int tradeId = getTradeId(tradeName, corporateName);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id FROM branch_table WHERE actionTaken IS NULL AND tradeId = '"+tradeId+"' "
                    + "AND name = '"+ branchName.toLowerCase() +"' ");
            while(rs.next()){
                branchId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchId;
    }
    
    public List getEmployeesListPerBranch(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> employeesListPerBranch = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT CONCAT_WS(', ', lastname, CONCAT_WS(' ', firstname, LEFT(middlename, 1))) AS names "
                    + "FROM employee WHERE branchId = "+branchId+" AND (currentStatus != 'removed' OR currentStatus IS NULL) ORDER BY lastname ASC ");
            while(rs.next()){
                employeesListPerBranch.add(rs.getString("names").toUpperCase());
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeesListPerBranch;
    }
    
    public String getEmployeesId(String name){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String employeeId = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT employeeId FROM employee WHERE "
                    + "CONCAT_WS(', ', lastname, CONCAT_WS(' ', firstname, LEFT(middlename, 1))) = '"+name+"' AND "
                    + "currentStatus IS NULL ");
            while(rs.next()){
                employeeId = rs.getString("employeeId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeeId;
    }    
    
    public String getEmployeesIdBySalaryId(String salaryId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String employeeId = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT employeeId FROM salary WHERE id = '"+salaryId+"' ");
            while(rs.next()){
                employeeId = rs.getString("employeeId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeeId;
    }
    
    public String getEmployeesIdByUsername(String name){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String employeeId = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT employeeId FROM user_ WHERE username_ = '"+name+"' ");
            while(rs.next()){
                employeeId = rs.getString("employeeId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeeId;
    }
    
    public Boolean checkDateIfExist(String date, String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM salary WHERE "
                    + "(attendancePeriodFrom <= '"+date+"' AND attendancePeriodTo >= '"+date+"') AND employeeId = '"+id+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean checkEmployeeIdIfExist(String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM employee WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public String getBranchAddress(Integer id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String address = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT address FROM branch WHERE id = '"+id+"' ");
            while(rs.next()){
                address = rs.getString("address");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return address;
    }

    public Boolean checkUsernameIfExist(String username){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM user_ WHERE username_ = '"+username+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = true;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Integer getUserId(String username){
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
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return userId;
    }
    
    public Boolean addCorporateToUser(int userId, int corporateId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Boolean result = false;
        String query = "INSERT INTO user_corporate_access(userId, corporateId) VALUES(?, ?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean addTradeToUser(int userId, int corporateId, int tradeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Boolean result = false;
        String query = "INSERT INTO user_trade_access(userId, corporateId, tradeId) VALUES(?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.setInt(3, tradeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean addBranchToUser(int userId, int tradeId, int branchId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Boolean result = false;
        String query = "INSERT INTO user_branch_access(userId, tradeId, branchId) VALUES(?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, tradeId);
            pstmt.setInt(3, branchId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean checkBranchIfExist(int userId, String branch){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM user_branch_access WHERE userId = '"+userId+"' AND "
                    + "branch = '"+branch.toLowerCase()+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = false;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean checkCorporateIfExist(String corporateName){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM corporate_table WHERE name = '"+corporateName.toLowerCase()+"' ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = true;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean checkCorporateIfExistForUser(int corporateId, int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT COUNT(*) FROM user_corporate_access "
                    + "WHERE corporateId = "+corporateId+" AND userId = "+userId+" ");
            while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    result = true;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean checkSalaryLedgerRowForEdit(String payrollDate, String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT payrollDate FROM SALARY WHERE employeeId = '"+id+"' ORDER BY payrollDate DESC LIMIT 1 ");
            while(rs.next()){
                if(rs.getString("payrollDate").equals(payrollDate)){
                    result = true;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean checkEmployeesStatus(String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT currentStatus FROM employee WHERE employeeId = '"+id+"' AND currentStatus IS NULL");
            while(rs.next()){
                if(rs.getString("currentStatus") == null){
                    result = true;                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public String getEmployeesEndDate(String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        String endDate = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT endDate FROM employee WHERE employeeId = '"+id+"' AND currentStatus IS NULL");
            while(rs.next()){
                endDate = rs.getString("endDate");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return endDate;
    }
    
    public String getEmployeesEntryDate(String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        String entryDate = null;        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT entryDate FROM employee WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                entryDate = rs.getString("entryDate");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return entryDate;
    }
    
    public String getEmploymentStatus(String id){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        String status = null;        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT currentStatus FROM employee WHERE employeeId = '"+id+"' AND currentStatus IS NULL");
            while(rs.next()){
                status = rs.getString("currentStatus");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return status;
    }
    
    public String getPayrollPeriod(String date){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        String payrollPeriod = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT payrollPeriod FROM salary WHERE payrollDate = '"+date+"' LIMIT 1");
            while(rs.next()){
                payrollPeriod = rs.getString("payrollPeriod");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollPeriod;
    }
}
