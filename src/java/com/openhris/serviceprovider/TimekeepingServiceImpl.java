/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.openhris.dao.TimekeepingDAO;
import com.openhris.model.Timekeeping;
import com.openhris.service.TimekeepingService;
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
    public List<Timekeeping> getAttendanceByEmployee(String employeeId) {
        return timekeepingDAO.getAttendanceByEmployee(employeeId);
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

    @Override
    public double getNonWorkingHolidayPay(int payrollId) {
        return timekeepingDAO.getNonWorkingHolidayPay(payrollId);
    }
}
