/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.PositionHistoryDAO;
import com.openhris.employee.model.PositionHistory;
import com.openhris.employee.service.PositionHistoryService;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class PositionHistoryServiceImpl implements PositionHistoryService {
    
    PositionHistoryDAO positionHistoryDAO = new PositionHistoryDAO();

    @Override
    public List<PositionHistory> getPositionHistory(String employeeId) {
        return positionHistoryDAO.getPositionHistory(employeeId);
    }

    @Override
    public boolean updatePositionHistory(String employeeId, PositionHistory positionHistory) {
        return positionHistoryDAO.updatePositionHistory(employeeId, positionHistory);
    }

    @Override
    public boolean removePositionHistory(int positionId) {
        return positionHistoryDAO.removePosition(positionId);
    }

    @Override
    public boolean insertEndDate(String employeeId, String endDate) {
        return positionHistoryDAO.insertEndDate(employeeId, endDate);
    }
    
}
