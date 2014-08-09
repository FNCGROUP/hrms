/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping.serviceprovider;

import com.openhris.dao.ServiceGetDAO;
import com.openhris.timekeeping.dao.TimekeepingDAO;
import com.openhris.timekeeping.model.Timekeeping;
import com.openhris.timekeeping.service.TimekeepingService;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class TimekeepingServiceImpl implements TimekeepingService {

    TimekeepingDAO timekeepingDAO = new TimekeepingDAO();
    
    @Override
    public List<Timekeeping> getAttendancePerPayroll() {
        return timekeepingDAO.getAllAttendancePerPayroll();
    }

    @Override
    public List<Timekeeping> getAttendanceByBranchAndEmployee(int branchId, String employeeId) {
        return timekeepingDAO.getAttendanceByBranchAndEmployee(branchId, employeeId);
    }

    @Override
    public boolean checkAttendanceDateIfExist(String date, String employeeId) {
        return timekeepingDAO.checkAttendanceDateIfExist(date, employeeId);
    }

    @Override
    public List<Timekeeping> getTimekeepingRowData(String date, int payrollId) {
        return timekeepingDAO.getTimekeepingRowData(date, payrollId);
    }    

    @Override
    public Date getPreviousPayrollDate(String employeeId) {
        return timekeepingDAO.getPreviousPayrollDate(employeeId);
    }
}
