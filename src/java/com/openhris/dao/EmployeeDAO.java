/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.model.Employee;
import com.openhris.model.PostEmploymentInformationBean;
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
public class EmployeeDAO {
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public List<Employee> getAllEmployees(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        
        List<Employee> employeesListPerBranch = new ArrayList<Employee>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT employeeId, "
                    + "firstname, "
                    + "middlename, "
                    + "lastname FROM employee "
                    + "WHERE currentStatus IS NULL ORDER BY lastname ASC ");
            while(rs.next()){
                PostEmploymentInformationBean p = new PostEmploymentInformationBean();
                p.setEmployeeId(rs.getString("employeeId"));
                p.setFirstname(rs.getString("firstname"));
                p.setMiddlename(rs.getString("middlename"));
                p.setLastname(rs.getString("lastname"));
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
                Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeesListPerBranch;
    }
    
    public List<Employee> getEmployeePerBranch(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
                
        List<Employee> employeesListPerBranch = new ArrayList<Employee>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT e.employeeId AS employeeId, e.firstname AS firstname, "
                    + "e.middlename AS middlename, e.lastname AS lastname, "
                    + "ct.name as corporate, tt.name AS trade, bt.name AS branch FROM employee e "
                    + "INNER JOIN branch_table bt ON e.branchId = bt.id "
                    + "INNER JOIN trade_table tt ON bt.tradeId = tt.id "
                    + "INNER JOIN corporate_table ct ON tt.corporateId = ct.id "
                    + "WHERE branchId = "+branchId+" AND currentStatus IS NULL ORDER BY lastname ASC");
            while(rs.next()){
                PostEmploymentInformationBean p = new PostEmploymentInformationBean();
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
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
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
}
