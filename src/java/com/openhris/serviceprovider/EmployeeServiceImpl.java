/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.dao.EmployeeDAO;
import com.openhris.model.Employee;
import com.openhris.model.EmploymentInformation;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.EmployeeService;
import java.util.List;

/**
 *
 * @author jet
 */
public class EmployeeServiceImpl implements EmployeeService {
    
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
    public List<Employee> getEmployeeById(String employeeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return serviceInsert.checkForDuplicateEntry(firstname, middlename, lastname);
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
    
}
