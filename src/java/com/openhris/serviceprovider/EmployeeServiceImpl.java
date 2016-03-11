/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.dao.EmployeeDAO;
import com.openhris.model.BankDebitMemo;
import com.openhris.model.Employee;
import com.openhris.model.EmploymentInformation;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.EmployeeService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeeServiceImpl implements EmployeeService {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    ServiceGetDAO serviceGet = new ServiceGetDAO();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public String getEmployeeId(String name) {
//        return serviceGet.getEmployeeId(name);
        return employeeDAO.getEmployeeId(name);
    }

    @Override
    public List<BankDebitMemo> findBankDebitMemo(int branchId, String payrollDate) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BankDebitMemo> dbmList = new ArrayList<>();        
        
        try {
//            pstmt = conn.prepareStatement("SELECT e.bankAccountNo AS BankAccountNo, "
//                    + "e.employeeId AS EmployeeNo, "
//                    + "e.firstname As Firstname, "
//                    + "e.middlename AS Middlename, "
//                    + "e.lastname AS Lastname, "
//                    + "s.amountToBeReceive AS Amount "
//                    + "FROM employee e INNER JOIN payroll_table s ON e.employeeId = s.employeeId "
//                    + "WHERE (e.currentStatus != 'removed' OR e.currentStatus IS NULL) "
//                    + "AND s.branchId = ? AND s.payrollDate = ? ORDER BY e.lastname ASC ");
            pstmt = conn.prepareStatement("SELECT * FROM bank_debit_memo "
                    + "WHERE CorporateID = ? "
                    + "AND PayrollDate = ? "
                    + "ORDER BY Lastname ASC");
            pstmt.setInt(1, branchId);
            pstmt.setString(2, payrollDate);
            rs = pstmt.executeQuery();
            while(rs.next()){
                BankDebitMemo dbm = new BankDebitMemo();
                dbm.setEmployeeId(rs.getString("EmployeeNo"));
                dbm.setBankAccountNo(rs.getString("BankAccountNo"));
                dbm.setFirstname(rs.getString("Firstname"));
                dbm.setMiddlename(rs.getString("Middlename"));
                dbm.setLastname(rs.getString("Lastname"));
                dbm.setAmount(util.convertStringToDouble(rs.getString("Amount")));
                dbm.setBranch(rs.getString("Branch"));
                dbm.setPayrollDate(util.parsingDate(rs.getString("PayrollDate")));
                dbmList.add(dbm);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return dbmList;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    @Override
    public List<Employee> getEmployeePerBranch(int branchId) {
        return employeeDAO.getEmployeePerBranch(branchId);
    }

    @Override
    public List<PostEmploymentInformationBean> getEmployeePositionHistory(String employeeId) {
        return serviceGet.getEmployeePositionHistory(employeeId);
    }

    @Override
    public List<EmploymentInformation> getEmployeeEmploymentInformation(String employeeId) {
        return serviceGet.getEmployeeEmploymentInformation(employeeId);
    }

    @Override
    public boolean updateEmployeeEmploymentInformation(String employeeId, List<PostEmploymentInformationBean> updateList) {
        return serviceUpdate.updateEmployeeEmploymentInformation(employeeId, updateList);
    }

    @Override
    public boolean insertNewEmployee(PostEmploymentInformationBean pe) {
        return serviceInsert.insertNewEmployee(pe);
    }

    @Override
    public boolean checkForDuplicateEmployee(String firstname, String middlename, String lastname) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Boolean result = true;
        
        try {
            pstmt = conn.prepareStatement("SELECT COUNT(*) AS result FROM employee "
                    + "WHERE firstname = ? "
                    + "AND middlename = ? "
                    + "AND lastname = ? "
                    + "AND currentStatus IS NULL");
            pstmt.setString(1, firstname);
            pstmt.setString(2, middlename);
            pstmt.setString(3, lastname);
            rs = pstmt.executeQuery();
            while(rs.next()){
                if(rs.getString("result").equals("0")){
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }

    @Override
    public List<Employee> getEmployeePerBranchForDropDownList(int branchId) {
//        return serviceGet.getEmployeePerBranchForDropDownList(branchId);
        return employeeDAO.getEmployeePerBranch(branchId);
    }

    @Override
    public double getEmploymentWage(String employeeId) {
        return serviceGet.getEmploymentWage(employeeId);
    }

    @Override
    public String getEmploymentWageStatus(String employeeId) {
        return serviceGet.getEmploymentWageStatus(employeeId);
    }

    @Override
    public double getEmploymentAllowance(String employeeId) {
        return serviceGet.getEmploymentAllowance(employeeId);
    }

    @Override
    public String getEmploymentAllowanceEntry(String employeeId) {
        return serviceGet.getEmploymentAllowanceEntry(employeeId);
    }

    @Override
    public double getEmploymentAllowanceForLiquidation(String employeeId) {
        return serviceGet.getEmploymentAllowanceForLiquidation(employeeId);
    }

    @Override
    public String getEmploymentWageEntry(String employeeId) {
        return serviceGet.getEmploymentWageEntry(employeeId);
    }

    @Override
    public String getEmploymentEntryDate(String employeeId) {
        return employeeDAO.getEmploymentEntryDate(employeeId);
    }

    @Override
    public String getEmploymentEndDate(String employeeId) {
        return serviceGet.getEmploymentEndDate(employeeId);
    }

    @Override
    public String getEmployeeCurrentStatus(String employeeId) {
        return serviceGet.getEmploymentCurrentStatus(employeeId);
    }

    @Override
    public String getEmployeeTotalDependent(String employeeId) {
        return serviceGet.getEmployeeTotalDependent(employeeId);
    }

    @Override
    public boolean updateEmploymentAllowanceForLiquidation(double afl, String employeeId) {
        return serviceUpdate.updateEmploymentAllowanceForLiquidation(afl, employeeId);
    }

    @Override
    public List<Employee> getEmployeePerBranchMainView(int branchId) {
        return employeeDAO.getEmployeePerBranchMainView(branchId);
    }

    @Override
    public List<Employee> getAllEmployeeMainView() {
        return employeeDAO.getAllEmployeeMainView();
    }

    @Override
    public EmploymentInformation findEmployeeById(String employeeId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmploymentInformation ei = new EmploymentInformation();
        
        try {
            pstmt = conn.prepareStatement("SELECT * FROM employee WHERE employeeId = ? ");
            pstmt.setString(1, employeeId);
            rs  = pstmt.executeQuery();
            while(rs.next()){
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
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
        return ei;
    }
    
}
