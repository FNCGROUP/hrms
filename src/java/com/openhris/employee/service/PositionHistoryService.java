/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.service;

import com.openhris.employee.model.PositionHistory;
import java.util.List;

/**
 *
 * @author jetdario
 */
public interface PositionHistoryService {
    
    public List<PositionHistory> getPositionHistory(String employeeId);
    
    public boolean updatePositionHistory(String employeeId, PositionHistory positionHistory);
    
    public boolean removePositionHistory(int positionId);
    
}
