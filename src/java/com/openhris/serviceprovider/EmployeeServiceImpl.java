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
import com.openhris.model.Employee;
import com.openhris.model.EmploymentInformation;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.EmployeeService;
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
    public List<EmploymentInformation> findBankDebitMemo(int branchId, String payrollDate) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<EmploymentInformation> eiList = new ArrayList<>();        
        
        try {
            pstmt = conn.prepareStatement("SELECT e.bankAccountNo AS BankAccountNo, "
                    + "e.employeeId AS EmployeeNo, "
                    + "e.firstname As Firstname, "
                    + "e.middlename AS Middlename, "
                    + "e.lastname AS Lastname, "
                    + "s.amountToBeReceive AS Amount "
                    + "FROM employee e INNER JOIN payroll_table s ON e.employeeId = s.employeeId "
                    + "WHERE (e.currentStatus != 'removed' OR e.currentStatus IS NULL) "
                    + "AND s.branchId = ? AND s.payrollDate = ? ORDER BY e.lastname ASC ");
            pstmt.setInt(1, branchId);
            pstmt.setString(2, payrollDate);
            rs = pstmt.executeQuery();
            while(rs.next()){
                EmploymentInformation ei = new EmploymentInformation();
                ei.setEmployeeId(rs.getString("EmployeeNo"));
                ei.setBankAccountNo(rs.getString("BankAccountNo"));
                ei.setFirstname(rs.getString("Firstname"));
                ei.setMiddlename(rs.getString("Middlename"));
                ei.setLastname(rs.getString("Lastname"));
                ei.setEmploymentWage(util.convertStringToDouble(rs.getString("Amount")));
                eiList.add(ei);
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
        
        return eiList;
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
    
}
