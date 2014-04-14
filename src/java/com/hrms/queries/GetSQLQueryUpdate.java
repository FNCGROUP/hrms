/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.queries;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.ConvertionUtilities;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class GetSQLQueryUpdate {
    GetSQLConnection getConnection = new GetSQLConnection();       
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs; 
    ConvertionUtilities convertionUtilities = new ConvertionUtilities();
    
    public Boolean updateSalaryByAdvances(String id, Double advances, Double amountToBeReceive, Double amountReceivable, Double adjustments, 
            String date, String advancesType, String particulars, String employeeId){
        Connection conn = getConnection.connection();        
        String queryInsert = "INSERT INTO advances(salaryId, amount, advancesType, particulars, datePosted, employeeId) VALUES(?, ?, ?, ?, ?, ?)";
        String queryUpdate = "UPDATE salary SET amountToBeReceive = ?, amountReceivable = ?, advances = ? WHERE id = ? ";
        String querySelect = "SELECT SUM(ROUND(amount, 2)) AS totalAmount FROM advances WHERE salaryId = "+id+" AND rowStatus IS NULL ";
        Boolean result = false;
        try {
            conn.setAutoCommit(false);            
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.setDouble(2, advances);
            pstmt.setString(3, advancesType.toLowerCase());
            pstmt.setString(4, particulars.toLowerCase());
            pstmt.setString(5, date);
            pstmt.setString(6, employeeId);
            pstmt.executeUpdate();
                  
            amountReceivable = amountReceivable - advances;
            amountToBeReceive = Math.round((amountToBeReceive - advances)*100.0)/100.0;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                advances = Double.valueOf(rs.getString("totalAmount")).doubleValue();
            }
            
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceivable);
            pstmt.setDouble(3, advances);
            pstmt.setInt(4, Integer.parseInt(id));
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateSalaryAdvancesByPayrollRegister(String id, Double advances, Double amountToBeReceive, Double amountReceivable, 
            String date, String advancesType, String particulars, String employeeId){
        Connection conn = getConnection.connection();        
        String queryInsert = "INSERT INTO advances(salaryId, amount, advancesType, particulars, datePosted, employeeId) VALUES(?, ?, ?, ?, ?, ?)";
        String queryUpdate = "UPDATE salary SET amountToBeReceive = ?, amountReceivable = ?, advances = ? WHERE id = ? ";
        String querySelect = "SELECT SUM(ROUND(amount, 2)) AS totalAmount FROM advances WHERE salaryId = "+id+" AND rowStatus IS NULL ";
        Boolean result = false;
        try {
            conn.setAutoCommit(false);            
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.setDouble(2, advances);
            pstmt.setString(3, advancesType.toLowerCase());
            pstmt.setString(4, particulars.toLowerCase());
            pstmt.setString(5, date);
            pstmt.setString(6, employeeId);
            pstmt.executeUpdate();
                  
            amountToBeReceive = Math.round((amountToBeReceive - advances)*100.0)/100.0;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                advances = Double.valueOf(rs.getString("totalAmount")).doubleValue();
            }
            
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceivable);
            pstmt.setDouble(3, advances);
            pstmt.setInt(4, Integer.parseInt(id));
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removeAdvancesOnSalary(String salaryRowId, Integer rowId, Double removedAdvances, Double amountToBeReceive, Double amountReceivable, 
            Double adjustments, String remarks){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE salary SET amountToBeReceive = ?, amountReceivable = ?, advances = ? WHERE id = ? ";
        String queryRemove = "UPDATE advances SET rowStatus = 'removed', remarks = ?, dateRemoved = now() WHERE id = ?";
        String querySelect = "SELECT ifnull(SUM(ROUND(amount, 2)), 0) AS totalAmount FROM advances WHERE salaryId = "+salaryRowId+" AND rowStatus IS NULL ";
        Boolean result = false;
        Double newAdvances = 0.0;
        try {
            conn.setAutoCommit(false);            
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.setString(1, remarks);
            pstmt.setInt(2, rowId);
            pstmt.executeUpdate();
                      
            amountReceivable = amountReceivable + removedAdvances;
            amountToBeReceive = Math.round((amountToBeReceive + removedAdvances)*100.0)/100.0;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(querySelect);
            while(rs.next()){
                newAdvances = Double.valueOf(rs.getString("totalAmount")).doubleValue();
            }            
            System.out.println(salaryRowId+" "+amountToBeReceive+" "+amountReceivable);
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceivable);
            pstmt.setDouble(3, newAdvances);
            pstmt.setInt(4, Integer.parseInt(salaryRowId));
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removeSalary(String id){
        Connection conn = getConnection.connection();
        String queryRemove = "DELETE FROM salary WHERE id = ?";
        Boolean result = false;        
        try {
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }    
    
    public Boolean updateSalaryByAdjustments(Integer salaryId, Double adjustments, Double amountToBeReceive, Double amountReceive, 
            String remarks, String date){
        Connection conn = getConnection.connection();
        String queryInsert = "INSERT INTO adjustments(salaryId, amount, remarks, datePosted) VALUES(?, ?, ?, ?)";
        String queryUpdate = "UPDATE salary SET amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        Boolean result = false;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setInt(1, salaryId);
            pstmt.setDouble(2, adjustments);
            pstmt.setString(3, remarks.toLowerCase());
            pstmt.setString(4, date);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceive);
            pstmt.setInt(3, salaryId);
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
        return result;
    }
    
    public Boolean removeAdjustmentsOnSalary(Integer salaryId, Double amountToBeReceive){
        Connection conn = getConnection.connection();
        Boolean result = false;
        String queryUpdate = "UPDATE salary SET amountToBeReceive = ? WHERE id = ? ";
        String queryDelete = "DELETE FROM adjustments WHERE salaryId = "+salaryId+" ";
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setInt(2, salaryId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryDelete);
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return result;
    }
    
    public Boolean updateSalaryByCashBond(String id, Double cashBond, Double amountToBeReceive, Double amountReceivable){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE salary SET cashBond = ?, amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, cashBond);
            pstmt.setDouble(2, amountToBeReceive);
            pstmt.setDouble(3, amountReceivable);
            pstmt.setString(4, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removeSalaryByCashBond(String id, double cashBond, double amountReceivable){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE salary SET cashBond = ?, amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        Boolean result = false;
        Double amountToBeReceive = amountReceivable;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, cashBond);
            pstmt.setDouble(2, amountToBeReceive);
            pstmt.setDouble(3, amountReceivable);
            pstmt.setString(4, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removePhicAndHdmf(Double taxableSalary, Double netPay, Double amountToBeReceive, Double amountReceivable, String id){
        Connection conn = getConnection.connection();
        Double philhealth = 0.0, hdmf = 0.0, tax = 0.0;
        String queryUpdate = "UPDATE salary SET philhealth = ? , hdmf = ?, taxableSalary = ?, tax = ?, netSalary = ?, "
                + "amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, philhealth);
            pstmt.setDouble(2, hdmf);
            pstmt.setDouble(3, taxableSalary);
            pstmt.setDouble(4, tax);
            pstmt.setDouble(5, netPay);
            pstmt.setDouble(6, amountToBeReceive);
            pstmt.setDouble(7, amountReceivable);
            pstmt.setString(8, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removeSss(Double taxableSalary, Double netPay, Double amountToBeReceive, Double amountReceivable, String id, String remarks){
        Connection conn = getConnection.connection();
        double sss = 0,tax = 0, amount = 0;
        String queryUpdate = "UPDATE salary SET sss = ?, taxableSalary = ?, tax = ?, netSalary = ?, amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        String queryInsert = "INSERT INTO contribution_trigger_track(salaryId, previousAmount, remarks, type, dateRemoved) VALUES(?, ?, ?, 'sss', now())";
        Boolean result = false;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT sss FROM salary WHERE id = '"+id+"' ");
            while(rs.next()){
                amount = Double.parseDouble(rs.getString("sss"));
            }
            
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, sss);
            pstmt.setDouble(2, taxableSalary);
            pstmt.setDouble(3, tax);
            pstmt.setDouble(4, netPay);
            pstmt.setDouble(5, amountToBeReceive);
            pstmt.setDouble(6, amountReceivable);
            pstmt.setString(7, id);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setString(1, id);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, remarks);
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateHdmf(Double newHdmf, Double oldHdmf, Double taxableSalary, Double netPay, Double amountToBeReceive, 
            Double amountReceivable, Double tax, String id, String remarks){
        Connection conn = getConnection.connection();
        //double tax = 0;
        String queryUpdate = "UPDATE salary SET hdmf = ?, taxableSalary = ?, tax = ?, netSalary = ?, amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        String queryInsert = "INSERT INTO contribution_trigger_track(salaryId, previousAmount, remarks, type, dateRemoved) VALUES(?, ?, ?, 'hdmf', now())";
        Boolean result = false;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, newHdmf);
            pstmt.setDouble(2, taxableSalary);
            pstmt.setDouble(3, tax);
            pstmt.setDouble(4, netPay);
            pstmt.setDouble(5, amountToBeReceive);
            pstmt.setDouble(6, amountReceivable);
            pstmt.setString(7, id);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setString(1, id);
            pstmt.setDouble(2, oldHdmf);
            pstmt.setString(3, remarks);
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateTax(Double newTax, Double oldTax, Double netPay, Double amountToBeReceive, Double amountReceivable, String id, String remarks){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE salary SET tax = ?, netSalary = ?, amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        String queryInsert = "INSERT INTO contribution_trigger_track(salaryId, previousAmount, remarks, type, dateRemoved) VALUES(?, ?, ?, 'tax', now())";
        Boolean result = false;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setDouble(1, newTax);
            pstmt.setDouble(2, netPay);
            pstmt.setDouble(3, amountToBeReceive);
            pstmt.setDouble(4, amountReceivable);
            pstmt.setString(5, id);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setString(1, id);
            pstmt.setDouble(2, oldTax);
            pstmt.setString(3, remarks);
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
                    Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                }                    
            }
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updatePayrollDate(String id, String date){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE salary SET payrollDate = ? WHERE id = ?";
        Boolean result = false;        
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, date);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateSalaryRowStatus(String id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE salary SET rowStatus = 'locked' WHERE id = ?";
        Boolean result = false;        
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateBranchAddress(Integer id, String address){
        Connection conn = getConnection.connection();
        String query = " UPDATE branch_table SET address = ? WHERE id = ?  ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, address.toLowerCase());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQuery.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean updateUserRole(String id, String role){
        Connection conn = getConnection.connection();
        String query = " UPDATE user_ SET userRole = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, role);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Boolean updateUserAccessControl(String id, String userAccess, String value){
        Connection conn = getConnection.connection();
        String query = " UPDATE user_access SET "+userAccess+" = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, value);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public Boolean updateAdvanceUserAccessControl(String id, String advanceUserAccess, String value){
        Connection conn = getConnection.connection();
        String query = " UPDATE advance_user_access SET "+advanceUserAccess+" = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, value);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Boolean removeUserAccount(int id){
        Connection conn = getConnection.connection();
        String queryRemove = "DELETE FROM user_ WHERE id = "+id+" ";
        Boolean result = false;
        String userId = null;
        try {
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {            
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removeBranchFromUser(String id){
        Connection conn = getConnection.connection();
        String queryRemove = "DELETE FROM user_branch_access WHERE id = '"+id+"' ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {            
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean removeTradeFromUser(String id){
        Connection conn = getConnection.connection();
        String queryRemove = "DELETE FROM user_trade_access WHERE id = '"+id+"' ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {            
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean removeCorporateFromUser(String id){
        Connection conn = getConnection.connection();
        String queryRemove = "DELETE FROM user_corporate_access WHERE id = '"+id+"' ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {            
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateUserPassword(String password, int id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE user_ SET password_ = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, password);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {            
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean removeEmployee(String id){
        Connection conn = getConnection.connection();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = df.format(new java.util.Date());
        String queryUpdate = "UPDATE employee SET employeeId = ?, currentStatus = ? WHERE employeeId = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, timestamp);
            pstmt.setString(2, "removed");
            pstmt.setString(3, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }

    public Boolean removeBranchFromTrade(Integer id){
        Connection conn = getConnection.connection();
        String queryUpdate = "UPDATE branch SET actionTaken = ? WHERE id = ? ";
        Boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, "removed");
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GetSQLQueryUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
