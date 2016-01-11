/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Phic;
import com.openhris.model.Sss;
import com.openhris.model.Tax;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.model.SssSchedule;
import com.openhris.service.ContributionService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class ContributionServiceImpl implements ContributionService {

    GetSQLConnection getConnection = new GetSQLConnection();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    ServiceGetDAO serviceGet = new ServiceGetDAO();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    
    @Override
    public List<Sss> getSssContributionList() {
        return serviceGet.getSssContributionList();
    }

    @Override
    public List<Phic> getPhicContributionList() {
        return serviceGet.getPhicContributionList();
    }

    @Override
    public List<Tax> getTaxContributionList() {
        return serviceGet.getTaxContributionList();
    }

    @Override
    public boolean updateSssTableData(List<Sss> sssList) {
        return serviceUpdate.updateSssTableData(sssList);
    }

    @Override
    public boolean updatePhicTableData(List<Phic> phicList) {
        return serviceUpdate.updatePhicTableData(phicList);
    }

    @Override
    public boolean updateTaxTableData(List<Tax> taxList) {
        return serviceUpdate.updateTaxTableData(taxList);
    }

    @Override
    public boolean insertNewSssData(List<Sss> sssList) {
        return serviceInsert.insertNewSssData(sssList);
    }

    @Override
    public boolean insertNewPhicData(List<Phic> phicList) {
        return serviceInsert.insertNewPhicData(phicList);
    }

    @Override
    public double getSssTableLastRowMaxSalary() {
        return serviceGet.getSssTableLastRowMaxSalary();
    }

    @Override
    public double getPhicTableLastRowMaxSalary() {
        return serviceGet.getPhicTableLastRowMaxSalary();
    }

    @Override
    public List<SssSchedule> getSssEmployerShare(int corporateId, int month, int year) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SssSchedule> sssList = new ArrayList<>();
        
        try {
            pstmt = conn.prepareStatement("SELECT * FROM sss_schedule "
                    + "WHERE (currentStatus != 'removed' OR currentStatus IS NULL) AND "
                    + "CorporateID = ? AND MONTH(payrollDate) = ? AND YEAR(payrollDate) = ? AND "
                    + "(eeShare != 0 AND erShare IS NOT NULL) "
                    + "ORDER BY name ASC");
            pstmt.setInt(1, corporateId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();
            while(rs.next()){
                SssSchedule s = new SssSchedule();
                s.setEmployeeId(rs.getString("employeeId"));
                s.setName(rs.getString("name"));
                s.setErShare(util.convertStringToDouble(rs.getString("erShare")));
                s.setEc(util.convertStringToDouble(rs.getString("ec")));
                sssList.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContributionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ContributionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return sssList;
    }
    
}
