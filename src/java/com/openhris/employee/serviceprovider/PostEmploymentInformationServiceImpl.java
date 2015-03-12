/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.PostEmploymentInformationDAO;
import com.openhris.employee.model.PostEmploymentInformationBean;
import com.openhris.employee.service.PostEmploymentInformationService;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class PostEmploymentInformationServiceImpl implements PostEmploymentInformationService {
    
    PostEmploymentInformationDAO postEmploymentInformationDAO = new PostEmploymentInformationDAO();

    @Override
    public List<PostEmploymentInformationBean> getPositionHistory(String employeeId) {
        return postEmploymentInformationDAO.getPositionHistory(employeeId);
    }

    @Override
    public boolean updatePositionHistory(String employeeId, PostEmploymentInformationBean positionHistory, boolean isEdit, int positionId) {
        return postEmploymentInformationDAO.updatePositionHistory(employeeId, positionHistory, isEdit, positionId);
    }

    @Override
    public boolean removePositionHistory(int positionId) {
        return postEmploymentInformationDAO.removePosition(positionId);
    }

    @Override
    public boolean insertEndDate(String employeeId, String endDate) {
        return postEmploymentInformationDAO.insertEndDate(employeeId, endDate);
    }

    @Override
    public List<PostEmploymentInformationBean> getPositionHistoryById(int positionId) {
        return postEmploymentInformationDAO.getPositionHistoryById(positionId);
    }

    @Override
    public boolean editDateEntryFromEmployment(String employeeId, String entryDate) {
        return postEmploymentInformationDAO.editDateEntryFromEmployment(employeeId, entryDate);
    }
    
}
