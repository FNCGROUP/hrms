/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.PersonalInformationDAO;
import com.openhris.employee.dao.PositionHistoryDAO;
import com.openhris.employee.model.PositionHistory;
import com.openhris.employee.service.PositionHistoryService;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class PositionHistoryServiceImpl implements PositionHistoryService{
    
    PositionHistoryDAO phDAO = new PositionHistoryDAO();

    @Override
    public List<PositionHistory> getPositionHistory(String employeeId) {
        return phDAO.getPositionHistory(employeeId);
    }

    @Override
    public boolean updatePositionHistory(String employeeId, PositionHistory positionHistory) {
        return phDAO.updatePositionHistory(employeeId, positionHistory);
    }

    @Override
    public boolean removePositionHistory(int positionId) {
        return phDAO.removePosition(positionId);
    }
    
}
