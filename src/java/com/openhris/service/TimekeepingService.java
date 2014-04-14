/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.timekeeping.model.Timekeeping;
import java.util.List;

/**
 *
 * @author jet
 */
public interface TimekeepingService {
    
    public List<Timekeeping> getAttendancePerPayroll();
    
    public List<Timekeeping> getAttendanceByBranchAndEmployee(int branchId, String employeeId);
    
    public boolean checkAttendanceDateIfExist(String date, String employeeId);
    
    public List<Timekeeping> getTimekeepingRowData(String date, int salaryId);
}
