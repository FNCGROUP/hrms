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

    @Override
    public double getTotalLates(int payrollId) {
        return timekeepingDAO.getTotalLates(payrollId);
    }

    @Override
    public double getTotalUndertime(int payrollId) {
        return timekeepingDAO.getTotalUndertime(payrollId);
    }

    @Override
    public double getTotalOvertime(int payrollId) {
        return timekeepingDAO.getTotalOvertime(payrollId);
    }

    @Override
    public double getTotalNightDifferential(int payrollId) {
        return timekeepingDAO.getTotalNightDifferential(payrollId);
    }

    @Override
    public double getTotalLatesDeduction(int payrollId) {
        return timekeepingDAO.getTotalLatesDeduction(payrollId);
    }

    @Override
    public double getTotalUndertimeDeduction(int payrollId) {
        return timekeepingDAO.getTotalUndertimeDeduction(payrollId);
    }

    @Override
    public double getTotalOvertimePaid(int payrollId) {
        return timekeepingDAO.getTotalOvertimePaid(payrollId);
    }

    @Override
    public double getTotalNightDifferentialPaid(int payrollId) {
        return timekeepingDAO.getTotalNightDifferentialPaid(payrollId);
    }

    @Override
    public double getTotalLegalHolidayPaid(int payrollId) {
        return timekeepingDAO.getTotalLegalHolidayPaid(payrollId);
    }

    @Override
    public double getTotalSpecialHolidayPaid(int payrollId) {
        return timekeepingDAO.getTotalSpecialHolidayPaid(payrollId);
    }

    @Override
    public double getTotalWorkingDayOffPaid(int payrollId) {
        return timekeepingDAO.getTotalWorkingDayOffPaid(payrollId);
    }

    @Override
    public double getTotalPsHolidayPaid(int payrollId) {
        return timekeepingDAO.getTotalPsHolidayPaid(payrollId);
    }
}
