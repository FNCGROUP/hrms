/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.classes.GenerateCompanyId;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author jet
 */
public class EmployeesInfoBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    private String employeeId;
    private String firstname;
    private String middlename;
    private String lastname;  
    private String sssNo;
    private String tinNo;
    private String philhealthNo;
    private String employmentStatus;
    private String employmentWageStatus;
    private String employmentWageEntry;
    private Double employmentWage;    
    private Integer branchId;
    private String entryDate;
    private String hdmfNo;
    private Double allowance;
    private String allowanceEntry;
    private Double allowanceForLiquidation;
    private String dependent;
    private String position;
    private String department;
    private String bankAccountNo;
    private String corporate;
    private String trade;
    private String branch;
    private String employeeStatus;
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public String getEmployeeId(){ return employeeId; }
    
    public void setFirstname(String firstname){ this.firstname = firstname; }
    public String getFirstname(){ return firstname; }
    
    public void setMiddlename(String middlename){ this.middlename = middlename; }
    public String getMiddlename(){ return middlename; }
    
    public void setLastname(String lastname){ this.lastname = lastname; }
    public String getLastname(){ return lastname; }
        
    public void setSssNo(String sssNo){ this.sssNo = sssNo; }
    public String getSssNo(){ return sssNo; }
    
    public void setTinNo(String tinNo){ this.tinNo = tinNo; }
    public String getTinNo(){ return tinNo; }
    
    public void setPhilhealthNo(String philhealthNo){ this.philhealthNo = philhealthNo; }
    public String getPhilhealthNo(){ return philhealthNo; }
    
    public void setEmploymentStatus(String employmentStatus){ this.employmentStatus  =employmentStatus; }
    public String getEmploymentStatus(){ return employmentStatus; }
    
    public void setEmploymentWageStatus(String employmentWageStatus){ this.employmentWageStatus = employmentWageStatus; }
    public String getEmploymentWageStatus(){ return employmentWageStatus; }
    
    public void setEmploymentWageEntry(String employmentWageEntry){ this.employmentWageEntry = employmentWageEntry; }
    public String getEmploymentWageEntry(){ return employmentWageEntry; }
    
    public void setEmploymentWage(Double employmentWage){ this.employmentWage = employmentWage; }
    public Double getEmploymentWage(){ return employmentWage; }
    
    public void setBranchId(int branchId){ this.branchId = branchId; }
    public Integer getBranchId(){ return branchId; }
    
    public void setEntryDate(String entryDate){ this.entryDate = entryDate; }
    public String getEntryDate(){ return entryDate; }
    
    public void setHdmfNo(String hdmfNo){ this.hdmfNo = hdmfNo; }
    public String getHdmfNo(){ return hdmfNo; }
    
    public void setAllowance(Double allowance){ this.allowance = allowance; }
    public Double getAllowance(){ return allowance; }
    
    public void setAllowanceEntry(String allowanceEntry){ this.allowanceEntry = allowanceEntry; }
    public String getAllowanceEntry(){ return allowanceEntry; }
    
    public void setAllowanceForLiquidation(Double allowanceForLiquidation){ this.allowanceForLiquidation = allowanceForLiquidation; }
    public Double getAllowanceForLiquidation(){ return allowanceForLiquidation; }
    
    public void setDependent(String dependent){ this.dependent = dependent; }
    public String getDependent(){ return dependent; }
    
    public void setPosition(String position){ this.position = position; }
    public String getPosition(){ return position; }
    
    public void setDepartment(String department){ this.department = department; }
    public String getDepartment(){ return department; }
    
    public void setBankAccountNo(String bankAccountNo){ this.bankAccountNo = bankAccountNo; }
    public String getBankAccountNo(){ return bankAccountNo; }
    
    public void setCorporate(String corporate){ this.corporate = corporate; }
    public String getCorporate(){ return corporate; }
    
    public void setTrade(String trade){ this.trade = trade; }
    public String getTrade(){ return trade; }
    
    public void setBranch(String branch){ this.branch = branch; }
    public String getBranch(){ return branch; }
    
    public void setEmployeeStatus(String employeeStatus){ this.employeeStatus = employeeStatus; }
    public String getEmployeeStatus(){ return employeeStatus; }
        
    public Boolean saveNewEmployeeInfo(){
        
        Connection conn = getConnection.connection();
        Boolean result = false;
        Statement stmt;
        PreparedStatement pstmt;
        ResultSet rs;
        
        String queryInsertEmployeeInfo = " INSERT INTO employee(employeeId, firstname, middlename, lastname, sssNo, tinNo, phicNo, hdmfNo, "
                + "employmentStatus, employmentWageStatus, employmentWageEntry, employmentWage, allowance, allowanceEntry, branchId, "
                + "entryDate, totalDependent, bankAccountNo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        
        String queryInsertEmployeePositionHistory = "INSERT INTO employee_position_history(employeeId, position, corporate, trade, branch, "
                + "department, entryDate, branchId) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        String queryInsertEmployeeSalaryHistory = "INSERT INTO salary_history(employeeId, salary, datePosted) VALUES(?, ?, ?)";
        
        GenerateCompanyId companyId = new GenerateCompanyId();
        employeeId = companyId.generateId(corporate, trade, entryDate);
        
        try {
            
            conn.setAutoCommit(false);
            
            pstmt = conn.prepareStatement(queryInsertEmployeeInfo);
            pstmt.setString(1, employeeId);
            pstmt.setString(2, firstname.toLowerCase().trim());
            pstmt.setString(3, middlename.toLowerCase().trim());
            pstmt.setString(4, lastname.toLowerCase().trim());          
            pstmt.setString(5, sssNo);
            pstmt.setString(6, tinNo);
            pstmt.setString(7, philhealthNo);
            pstmt.setString(8, hdmfNo);
            pstmt.setString(9, employmentStatus);
            pstmt.setString(10, employmentWageStatus);
            pstmt.setString(11, employmentWageEntry);
            pstmt.setDouble(12, employmentWage);
            pstmt.setDouble(13, allowance);
            pstmt.setString(14, allowanceEntry);
            pstmt.setInt(15, branchId);
            pstmt.setString(16, entryDate);
            pstmt.setString(17, dependent);
            pstmt.setString(18, bankAccountNo);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryInsertEmployeePositionHistory);
            pstmt.setString(1, employeeId.toLowerCase().trim());
            pstmt.setString(2, position.toLowerCase().trim());
            pstmt.setString(3, corporate.toLowerCase());
            pstmt.setString(4, trade.toLowerCase());
            pstmt.setString(5, branch.toLowerCase());
            pstmt.setString(6, department.toLowerCase().trim());
            pstmt.setString(7, entryDate.toLowerCase());
            pstmt.setInt(8, branchId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryInsertEmployeeSalaryHistory);
            pstmt.setString(1, employeeId.toLowerCase());
            pstmt.setDouble(2, employmentWage);
            pstmt.setString(3, entryDate);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Connection rollback...");
                } catch (SQLException ex1) {
                    Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }                
            }
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateFirstname(String fname, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET firstname = '"+fname.toLowerCase()+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateMiddlename(String mname, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET middlename = '"+mname.toLowerCase()+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateLastname(String lname, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET lastname = '"+lname.toLowerCase()+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateCompanyId(String companyId, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET employeeId = '"+companyId+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateHdmf(String hdmf, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET hdmfNo = '"+hdmf+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateSss(String sss, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET sssNo = '"+sss+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateTin(String tin, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET tinNo = '"+tin+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updatePhilhealth(String philhealth, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET phicNo = '"+philhealth+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
        
    public Boolean updateEmploymentWageStatus(String ewStatus, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET employmentWageStatus = '"+ewStatus+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateEmploymentWageEntry(String ewEntry, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET employmentWageEntry = '"+ewEntry+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateEmploymentWage(String eWage, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET employmentWage = '"+eWage+"' WHERE employeeId = '"+id+"' ";
        String queryInsertEmployeeSalaryHistory = "INSERT INTO salary_history(employeeId, salary, datePosted) VALUES('"+id+"', '"+eWage+"', now())";
        
        try {
            conn.setAutoCommit(false);
            
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryInsertEmployeeSalaryHistory);
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
                    Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateAllowance(String aEntry, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET allowance = '"+aEntry+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateAllowanceEntry(String aStatus, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET allowanceEntry = '"+aStatus+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateAllowanceForLiquidation(String afl, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET allowanceForLiquidation = '"+afl+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateAccountNo(String accountNo, String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String query = "UPDATE employee SET bankAccountNo = '"+accountNo+"' WHERE employeeId = '"+id+"' ";
        
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateEmploymentStatus(String eStatus, String id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE employee SET employmentStatus = ? WHERE employeeId = ? ";
        PreparedStatement pstmt;
        Boolean result = false;
        
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, eStatus);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateEmploymentEntryDate(String eStatus, String id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE employee SET entryDate = ? WHERE employeeId = ? ";
        PreparedStatement pstmt;
        Boolean result = false;
        
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, eStatus);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateEmployeeDependents(String eStatus, String id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE employee SET totalDependent = ? WHERE employeeId = ? ";
        PreparedStatement pstmt;
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, eStatus);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    //Resigned Employees
    public Boolean removeEmployeeFromCompany(String endDate, String id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE employee SET endDate = ?, rowStatus = ? WHERE employeeId = ? ";
        PreparedStatement pstmt;
        Boolean result = false;
        
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, endDate);
            pstmt.setString(2, "resigned");
            pstmt.setString(3, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean getSalaryInfo(String id){
        Connection conn = getConnection.connection();
        Statement stmt;
        ResultSet rs;
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                hdmfNo = rs.getString("hdmfNo");
                sssNo = rs.getString("sssNo");
                philhealthNo = rs.getString("philhealthNo");
                tinNo = rs.getString("tinNo");
                employmentWage = Double.parseDouble(rs.getString("employmentWage"));
                allowance  = Double.parseDouble(rs.getString("allowance"));
                allowanceForLiquidation = Double.parseDouble(rs.getString("allowanceForLiquidation"));
                bankAccountNo = rs.getString("bankAccountNo");
                employmentWageStatus = rs.getString("employmentWageStatus");
                employmentWageEntry = rs.getString("employmentWageEntry");
                employmentStatus = rs.getString("employmentStatus");
                allowanceEntry = rs.getString("allowanceEntry");
                employeeStatus = rs.getString("rowStatus");
                entryDate = rs.getString("entryDate");
                dependent = rs.getString("totalDependent");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean checkForDuplicateEntry(){
        Connection conn = getConnection.connection();
        Statement stmt;
        ResultSet rs;
        Boolean result = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS result FROM employee WHERE firstname = '"+firstname+"' AND middlename = '"+middlename+"' "
                    + "AND lastname = '"+lastname+"' ");
            while(rs.next()){
                if(rs.getString("result").equals("0")){
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesInfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
