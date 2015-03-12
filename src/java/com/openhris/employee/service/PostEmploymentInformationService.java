/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.service;

import com.openhris.employee.model.PostEmploymentInformationBean;
import java.util.List;

/**
 *
 * @author jetdario
 */
public interface PostEmploymentInformationService {
    
    public List<PostEmploymentInformationBean> getPositionHistory(String employeeId);
    
    public boolean updatePositionHistory(String employeeId, PostEmploymentInformationBean positionHistory, boolean isEdit, int positionId);
    
    public boolean removePositionHistory(int positionId);
    
    public boolean insertEndDate(String employeeId, String endDate);
    
    public List<PostEmploymentInformationBean> getPositionHistoryById(int positionId);
    
    public boolean editDateEntryFromEmployment(String employeeId, String entryDate);
}
