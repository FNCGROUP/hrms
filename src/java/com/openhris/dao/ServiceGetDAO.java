/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.administrator.model.User;
import com.openhris.administrator.model.UserAdvanceAccess;
import com.openhris.administrator.model.UserToolbarMenuAccess;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.company.model.Branch;
import com.openhris.company.model.Company;
import com.openhris.company.model.Trade;
import com.openhris.contributions.model.Phic;
import com.openhris.contributions.model.Sss;
import com.openhris.contributions.model.Tax;
import com.openhris.employee.model.Employee;
import com.openhris.employee.model.EmploymentInformation;
import com.openhris.employee.model.PositionHistory;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.timekeeping.model.Timekeeping;
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
 * @author jet
 */
public class ServiceGetDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public List<Branch> getAllCorporateTradeBranch(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Branch> allList = new ArrayList<Branch>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" CALL companyList() ");
            while(rs.next()){
                Branch b = new Branch();
                b.setCompanyName(rs.getString("corporate"));
                b.setTradeName(rs.getString("trade"));
                b.setBranchName(rs.getString("branch"));
                b.setBranchAddress(rs.getString("address"));
                allList.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return allList;
    }
    
    public int getCoporateId(String corporateName){
        Connection conn = getConnection.connection();
        ResultSet rs = null; 
        PreparedStatement pstmt = null;
        int corporateId = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT id FROM corporate_table WHERE name = ? ");
            pstmt.setString(1, corporateName);
            rs = pstmt.executeQuery();
            while(rs.next()){
                corporateId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateId;
    }
    
    public int getCorporateIdByTradeId(int tradeId){
	Connection conn = getConnection.connection();
        ResultSet rs = null; 
        PreparedStatement pstmt = null;
        int corporateId = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT corporateId FROM trade_table WHERE id = ? ");
            pstmt.setInt(1, tradeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                corporateId = Integer.parseInt(rs.getString("corporateId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateId;    
    }
    
    public String getCorporateById(int corporateId){
        Connection conn = getConnection.connection();
        ResultSet rs = null; 
        PreparedStatement pstmt = null;
        String corporateName = null;
        try {
            pstmt = conn.prepareStatement(" SELECT name FROM corporate_table WHERE id = ? ");
            pstmt.setInt(1, corporateId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                corporateName = rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateName;	    
    }
    
    public List<Company> getCorporateListAssignedForUser(int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Company> corporateLists = new ArrayList<Company>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT ct.name AS name, ct.id AS id FROM user_corporate_access uca "
                    + "INNER JOIN corporate_table ct ON uca.corporateId = ct.id "
                    + "WHERE uca.userId = '"+userId+"' ");
            while(rs.next()){
                Company c = new Company();
                c.setCompanyId(util.convertStringToInteger(rs.getString("id")));
                c.setCompanyName(rs.getString("name"));
                corporateLists.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateLists;
    }
    
    public List<Company> getCorporateLists(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Company> corporateLists = new ArrayList<Company>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id, name FROM corporate_table ORDER BY name ASC");
            while(rs.next()){
                Company company = new Company();
                company.setCompanyId(Integer.parseInt(rs.getString("id")));
                company.setCompanyName(rs.getString("name"));
                corporateLists.add(company);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateLists;
    }
      
    public List<Trade> getTradeListAssignedForUser(int userId, int corporateId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Trade> tradeLists = new ArrayList<Trade>();
        String queryTradeListForUser;
        if(corporateId == 0){
            queryTradeListForUser = " SELECT tt.name AS name, tt.id AS id FROM user_trade_access uta "
                    + "INNER JOIN trade_table tt ON uta.tradeId = tt.id "
                    + "WHERE uta.userId = "+userId+" ";
        } else {
            queryTradeListForUser = " SELECT tt.name AS name, tt.id AS id FROM user_trade_access uta "
                    + "INNER JOIN trade_table tt ON uta.tradeId = tt.id "
                    + "WHERE uta.userId = "+userId+" AND uta.corporateId = "+corporateId+" ";
        }
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryTradeListForUser);
            while(rs.next()){
                Trade t = new Trade();
                t.setTradeId(util.convertStringToInteger(rs.getString("id")));
                t.setTradeName(rs.getString("name"));
                tradeLists.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeLists;
    }
    
    public List<Trade> getTradeByCorporateId(int corporateId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Trade> tradeLists = new ArrayList<Trade>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id, name FROM trade_table WHERE "
                    + "corporateId = "+corporateId+" ORDER BY name ASC");
            while(rs.next()){
                Trade trade = new Trade();
                trade.setTradeId(Integer.parseInt(rs.getString("id")));
                trade.setTradeName(rs.getString("name"));
                tradeLists.add(trade);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeLists;
    }  
    
    public int getTradeId(String tradeName, int corporateId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        int tradeId = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT id FROM trade_table WHERE "
                    + "name = ? AND corporateId = ? ");
            pstmt.setString(1, tradeName);
            pstmt.setInt(2, corporateId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                tradeId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeId;
    }
    
    public String getTradeById(int tradeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String tradeName = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT name FROM trade_table WHERE id = '"+tradeId+"' ");
            while(rs.next()){
                tradeName = rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeName;
    }
    
    public int getTradeIdByBranchId(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        int tradeId = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT tradeId FROM branch_table WHERE id = "+branchId+" AND actionTaken IS NULL ");
            while(rs.next()){ 
                tradeId = util.convertStringToInteger(rs.getString("tradeId"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeId;
    }
    
    public List<Branch> getBranchListAssignedForUser(int userId, int tradeId){
        List<Branch> branchLists = new ArrayList<Branch>();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String branchListForUser;
        
        if(tradeId == 0){
            branchListForUser = " SELECT bt.name AS name, bt.id AS id FROM user_branch_access uba "
                    + "INNER JOIN branch_table bt ON uba.branchId = bt.id "
                    + "WHERE uba.userId = "+userId+" ";
        } else {
            branchListForUser = " SELECT bt.name AS name, bt.id AS id FROM user_branch_access uba "
                    + "INNER JOIN branch_table bt ON uba.branchId = bt.id "
                    + "WHERE uba.userId = "+userId+" AND uba.tradeId = "+tradeId+" ";
        }
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(branchListForUser);
            while(rs.next()){
                Branch b = new Branch();
                b.setBranchId(util.convertStringToInteger(rs.getString("id")));
                b.setBranchName(rs.getString("name"));
                branchLists.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return branchLists;
    }
        
    public List<Branch> getBranchByTrade(int tradeId, int corporateId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Branch> branchLists = new ArrayList<Branch>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT b.id AS id, b.name AS name FROM branch_table b "
                + "INNER JOIN trade_table tn ON b.tradeId = tn.id  "
		+ "INNER JOIN corporate_table cn ON tn.corporateId = cn.id WHERE "
		+ "tn.id = "+tradeId+" AND cn.id = "+corporateId+" "
                    + "AND b.actionTaken IS NULL ORDER BY b.name ASC");
            while(rs.next()){
                Branch branch = new Branch();
                branch.setBranchId(util.convertStringToInteger(rs.getString("id")));
		branch.setBranchName(rs.getString("name"));
		branchLists.add(branch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchLists;
    }
    
    public int getBranchId(String branchName, int tradeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        int branchId = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT id FROM branch_table WHERE "
                    + "actionTaken IS NULL AND "
                    + "tradeId = ? AND name = ? ");
            pstmt.setInt(1, tradeId);
            pstmt.setString(2, branchName);
            rs = pstmt.executeQuery();
            while(rs.next()){
                branchId = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchId;
    }
    
    public String getBranchById(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String branchName = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT name FROM branch_table WHERE id = "+branchId+" ORDER BY name ASC");
            while(rs.next()){
                branchName = rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchName;
    }
    
    public String getEmployeeId(String name){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String employeeId = null;
        try {
            pstmt = conn.prepareStatement(" SELECT employeeId FROM employee WHERE "
                    + "CONCAT_WS(', ', lastname, CONCAT_WS(' ', firstname, middlename)) = ? ");
            pstmt.setString(1, name.toLowerCase());
            rs = pstmt.executeQuery();
            while(rs.next()){
                employeeId = rs.getString("employeeId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeeId;
    }
    
    public double getEmploymentWage(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        double wage = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT employmentWage FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                wage = Double.parseDouble(rs.getString("employmentWage"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return wage;
    }
    
    public String getEmploymentWageStatus(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String wageStatus = null;
        try {
            pstmt = conn.prepareStatement(" SELECT employmentWageStatus FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                wageStatus = rs.getString("employmentWageStatus");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return wageStatus;
    }
    
    public String getEmploymentWageEntry(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String wageEntry = null;
        try {
            pstmt = conn.prepareStatement(" SELECT employmentWageEntry FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                wageEntry = rs.getString("employmentWageEntry");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return wageEntry;
    }
    
    public double getEmploymentAllowance(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        double allowance = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT allowance FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                allowance = Double.parseDouble(rs.getString("allowance"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return allowance;
    }
        
    public String getEmploymentAllowanceEntry(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String allowanceEntry = null;
        try {
            pstmt = conn.prepareStatement(" SELECT allowanceEntry FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                allowanceEntry = rs.getString("allowanceEntry");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return allowanceEntry;
    }
    
    public double getEmploymentAllowanceForLiquidation(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        double allowanceForLiquidation = 0;
        try {
            pstmt = conn.prepareStatement(" SELECT allowanceForLiquidation FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                allowanceForLiquidation = Double.parseDouble(rs.getString("allowanceForLiquidation"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return allowanceForLiquidation;
    }
    
    public String getEmploymentEntryDate(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String date = null;
        try {
            pstmt = conn.prepareStatement(" SELECT entryDate FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                date = rs.getString("entryDate");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return date;
    }
    
    public String getEmploymentEndDate(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String date = null;
        try {
            pstmt = conn.prepareStatement(" SELECT endDate FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                date = rs.getString("endDate");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return date;
    }
    
    public String getEmploymentCurrentStatus(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String status = null;
        try {
            pstmt = conn.prepareStatement(" SELECT currentStatus FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                status = rs.getString("currentStatus");;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return status;
    }
    
    public String getEmployeeTotalDependent(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        String totalDependent = null;
        try {
            pstmt = conn.prepareStatement(" SELECT totalDependent FROM employee WHERE "
                    + "employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                totalDependent = rs.getString("totalDependent");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return totalDependent;
    }
    
    public List<PositionHistory> getEmployeePerBranch(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String querySelectEmployeePerBranch;
        if(branchId == 0){
            querySelectEmployeePerBranch = "SELECT e.employeeId AS employeeId, e.firstname AS firstname, "
                    + "e.middlename AS middlename, e.lastname AS lastname, "
                    + "ct.name as corporate, tt.name AS trade, bt.name AS branch FROM employee e "
                    + "INNER JOIN branch_table bt ON e.branchId = bt.id "
                    + "INNER JOIN trade_table tt ON bt.tradeId = tt.id "
                    + "INNER JOIN corporate_table ct ON tt.corporateId = ct.id "
                    + "WHERE (currentStatus != 'removed' OR currentStatus IS NULL) ORDER BY lastname ASC ";
        } else {
            querySelectEmployeePerBranch = " SELECT e.employeeId AS employeeId, e.firstname AS firstname, "
                    + "e.middlename AS middlename, e.lastname AS lastname, "
                    + "ct.name as corporate, tt.name AS trade, bt.name AS branch FROM employee e "
                    + "INNER JOIN branch_table bt ON e.branchId = bt.id "
                    + "INNER JOIN trade_table tt ON bt.tradeId = tt.id "
                    + "INNER JOIN corporate_table ct ON tt.corporateId = ct.id "
                    + "WHERE branchId = "+branchId+" AND (currentStatus != 'removed' OR currentStatus IS NULL) ORDER BY lastname ASC ";
        }
        
        List<PositionHistory> employeesListPerBranch = new ArrayList<PositionHistory>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(querySelectEmployeePerBranch);
            while(rs.next()){
                PositionHistory p = new PositionHistory();
                p.setEmployeeId(rs.getString("employeeId"));
                p.setFirstname(rs.getString("firstname"));
                p.setMiddlename(rs.getString("middlename"));
                p.setLastname(rs.getString("lastname"));
                p.setCompany(rs.getString("corporate"));
                p.setTrade(rs.getString("trade"));
                p.setBranch(rs.getString("branch"));
                employeesListPerBranch.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeesListPerBranch;
    }
       
    public List<Employee> getEmployeePerBranchForDropDownList(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Employee> employeesListPerBranch = new ArrayList<Employee>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT * FROM employee WHERE branchId = "+branchId+" AND "
                    + "currentStatus IS NULL ORDER BY lastname ASC ");
            while(rs.next()){
                Employee e = new Employee();
                e.setFirstname(rs.getString("firstname"));
                e.setMiddlename(rs.getString("middlename"));
                e.setLastname(rs.getString("lastname"));
                employeesListPerBranch.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeesListPerBranch;
    }
    
    public List<PositionHistory> getEmployeePositionHistory(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<PositionHistory> employeePositionHistoryList = new ArrayList<PositionHistory>();
        try {
            stmt = conn.createStatement();
            rs  = stmt.executeQuery("SELECT * FROM employee_position_history WHERE employeeId = '"+employeeId+"' ");
            while(rs.next()){
                PositionHistory ph = new PositionHistory();
                ph.setPosition(rs.getString("position"));
                ph.setCompany(rs.getString("corporate"));
                ph.setTrade(rs.getString("trade"));
                ph.setBranch(rs.getString("branch"));
                ph.setDepartment(rs.getString("department"));
                employeePositionHistoryList.add(ph);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return employeePositionHistoryList;
    }
    
    public List<EmploymentInformation> getEmployeeEmploymentInformation(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<EmploymentInformation> employmentInformationList = new ArrayList<EmploymentInformation>();
        try {
            stmt = conn.createStatement();
            rs  = stmt.executeQuery("SELECT * FROM employee WHERE employeeId = '"+employeeId+"' ");
            while(rs.next()){
                EmploymentInformation ei = new EmploymentInformation();
                ei.setFirstname(rs.getString("firstname"));
                ei.setMiddlename(rs.getString("middlename"));
                ei.setLastname(rs.getString("lastname"));
                ei.setSssNo(rs.getString("sssNo"));
                ei.setTinNo(rs.getString("tinNo"));
                ei.setPhicNo(rs.getString("phicNo"));
                ei.setHdmfNo(rs.getString("hdmfNo"));
                ei.setEmploymentStatus(rs.getString("employmentStatus"));
                ei.setEmploymentWageStatus(rs.getString("employmentWageStatus"));
                ei.setEmploymentWageEntry(rs.getString("employmentWageEntry"));
                ei.setEmploymentWage(Double.parseDouble(rs.getString("employmentWage")));
                ei.setAllowance(Double.parseDouble(rs.getString("allowance")));
                ei.setAllowanceEntry(rs.getString("allowanceEntry"));
                ei.setEntryDate(util.parsingDate(rs.getString("entryDate")));
                ei.setTotalDependent(rs.getString("totalDependent"));
                ei.setBankAccountNo(rs.getString("bankAccountNo"));
                employmentInformationList.add(ei);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return employmentInformationList;
    }
                 
    public List<Sss> getSssContributionList(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Sss> sssList = new ArrayList<Sss>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM sss_table");
            while(rs.next()){
                Sss sss = new Sss();
                sss.setId(util.convertStringToInteger(rs.getString("id")));
                sss.setMinSalary(util.convertStringToDouble(rs.getString("minSalary")));
                sss.setMaxSalary(util.convertStringToDouble(rs.getString("maxSalary")));
                sss.setMonthlySalaryCredit(util.convertStringToDouble(rs.getString("monthlySalaryCredit")));
                sss.setEmployeeContribution(util.convertStringToDouble(rs.getString("employeeContribution")));
                sss.setEmployerContribution(util.convertStringToDouble(rs.getString("employerContribution")));
                sss.setTotalMontlyContribution(util.convertStringToDouble(rs.getString("totalContribution")));
                sssList.add(sss);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        return sssList;
    }
    
    public double getSssTableLastRowMaxSalary(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double maxSalary = 0;;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT maxSalary FROM sss_table ORDER BY id DESC LIMIT 1");
            while(rs.next()){
                maxSalary = util.convertStringToDouble(rs.getString("maxSalary"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxSalary;
    }
    
    public List<Phic> getPhicContributionList(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Phic> phicList = new ArrayList<Phic>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM phic_table");
            while(rs.next()){
                Phic phic = new Phic();
                phic.setId(util.convertStringToInteger(rs.getString("id")));
                phic.setMinSalary(util.convertStringToDouble(rs.getString("minSalary")));
                phic.setMaxSalary(util.convertStringToDouble(rs.getString("maxSalary")));
                phic.setBaseSalary(util.convertStringToDouble(rs.getString("baseSalary")));
                phic.setTotalMonthlyPremium(util.convertStringToDouble(rs.getString("totalMonthlyPremium")));
                phic.setEmployeeShare(util.convertStringToDouble(rs.getString("employeeShare")));
                phic.setEmployerShare(util.convertStringToDouble(rs.getString("employerShare")));
                phicList.add(phic);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        return phicList;
    }
    
    public double getPhicTableLastRowMaxSalary(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double maxSalary = 0;;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT maxSalary FROM phic_table ORDER BY id DESC LIMIT 1");
            while(rs.next()){
                maxSalary = util.convertStringToDouble(rs.getString("maxSalary"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxSalary;
    }
        
    public List<Tax> getTaxContributionList(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Tax> taxList = new ArrayList<Tax>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM tax_table");
            while(rs.next()){
                Tax tax = new Tax();
                tax.setId(util.convertStringToInteger(rs.getString("id")));
                tax.setStatus(rs.getString("status"));
                tax.setTaxRate1(util.convertStringToDouble(rs.getString("taxRate1")));
                tax.setTaxRate2(util.convertStringToDouble(rs.getString("taxRate2")));
                tax.setTaxRate3(util.convertStringToDouble(rs.getString("taxRate3")));
                tax.setTaxRate4(util.convertStringToDouble(rs.getString("taxRate4")));
                tax.setTaxRate5(util.convertStringToDouble(rs.getString("taxRate5")));
                tax.setTaxRate6(util.convertStringToDouble(rs.getString("taxRate6")));
                tax.setTaxRate7(util.convertStringToDouble(rs.getString("taxRate7")));
                taxList.add(tax);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        return taxList;
    }
               
}
