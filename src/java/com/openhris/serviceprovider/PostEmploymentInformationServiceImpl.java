/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.PostEmploymentInformationDAO;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.PostEmploymentInformationService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class PostEmploymentInformationServiceImpl implements PostEmploymentInformationService {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
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
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee "
                    + "SET endDate = ?, "
                    + "currentStatus = ?, "
                    + "EmployeeStatus = ? "
                    + "WHERE employeeId = ? ");
            pstmt.setString(1, endDate);
            pstmt.setString(2, "resigned");
            pstmt.setInt(3, 1);
            pstmt.setString(4, employeeId);
            pstmt.executeUpdate();
                        
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
//        return postEmploymentInformationDAO.insertEndDate(employeeId, endDate);
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
