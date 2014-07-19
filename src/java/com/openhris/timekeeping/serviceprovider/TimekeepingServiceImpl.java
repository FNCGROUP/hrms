/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping.serviceprovider;

import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.timekeeping.service.TimekeepingService;
import com.openhris.timekeeping.model.Timekeeping;
import java.util.List;

/**
 *
 * @author jet
 */
public class TimekeepingServiceImpl implements TimekeepingService {

    ServiceGetDAO serviceGet = new ServiceGetDAO();
    
    @Override
    public List<Timekeeping> getAttendancePerPayroll() {
        return serviceGet.getAllAttendancePerPayroll();
    }

    @Override
    public List<Timekeeping> getAttendanceByBranchAndEmployee(int branchId, String employeeId) {
        return serviceGet.getAttendanceByBranchAndEmployee(branchId, employeeId);
    }

    @Override
    public boolean checkAttendanceDateIfExist(String date, String employeeId) {
        return serviceGet.checkAttendanceDateIfExist(date, employeeId);
    }

    @Override
    public List<Timekeeping> getTimekeepingRowData(String date, int payrollId) {
        return serviceGet.getTimekeepingRowData(date, payrollId);
    }    
}
