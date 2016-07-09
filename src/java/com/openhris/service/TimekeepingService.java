/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.model.Timekeeping;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public interface TimekeepingService {
    
    public List<Timekeeping> getAttendancePerPayroll();
    
    public List<Timekeeping> getAttendanceByEmployee(String employeeId);
    
    public boolean checkAttendanceDateIfExist(String date, String employeeId);
    
    public List<Timekeeping> getTimekeepingRowData(String date, int salaryId);
    
    public Date getPreviousPayrollDate(String employeeId);
    
    public double getNonWorkingHolidayPay(int payrollId);
    
    public double getTotalLates(int payrollId);
    
    public double getTotalUndertime(int payrollId);
    
    public double getTotalOvertime(int payrollId);
    
    public double getTotalNightDifferential(int payrollId);
    
    public double getTotalLatesDeduction(int payrollId);
    
    public double getTotalUndertimeDeduction(int payrollId);
    
    public double getTotalOvertimePaid(int payrollId);
    
    public double getTotalNightDifferentialPaid(int payrollId);
    
    public double getTotalLegalHolidayPaid(int payrollId);
    
    public double getTotalSpecialHolidayPaid(int payrollId);
    
    public double getTotalWorkingDayOffPaid(int payrollId);
    
    public double getTotalPsHolidayPaid(int payrollId);
    
    public double findTotalLatesLegalHolidayDeduction(int payrollId);
    
    public double findTotalLatesSpecialHolidayDeduction(int payrollId);
    
    public double findTotalLatesWorkingDayOffDeduction(int payrollId);
    
    public double findTotalUndertimeLegalHolidayDeduction(int payrollId);
    
    public double findTotalUndertimeSpecialHolidayDeduction(int payrollId);
    
    public double findTotalUndertimeWorkingDayOffDeduction(int payrollId);
}
