/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.CommonUtil;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Phic;
import com.openhris.model.Sss;
import com.openhris.model.Tax;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.model.AFLSchedule;
import com.openhris.model.HdmfSchedule;
import com.openhris.model.PhicSchedule;
import com.openhris.model.SssSchedule;
import com.openhris.model.TaxSchedule;
import com.openhris.service.ContributionService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
//    public List<SssSchedule> getSssContribution(int branchId, int year, int month) {
    public List<SssSchedule> getSssContribution(int branchId, Date payrollDate) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SssSchedule> sssList = new ArrayList<>();
        
        try {
//            pstmt = conn.prepareStatement("SELECT * FROM sss_schedule "
//                    + "WHERE (currentStatus != 'removed' OR currentStatus IS NULL) "
//                    + "AND ECMCorporateID = ? "                    
//                    + "AND YEAR(payrollDate) = ? "
//                    + "AND MONTH(payrollDate) = ? "
//                    + "AND (eeShare != 0 AND erShare IS NOT NULL) "
//                    + "ORDER BY name ASC");    
            
//                    + "AND (SELECT branchId FROM employee_contribution_main WHERE employeeId = sss.employeeId ORDER BY id DESC LIMIT 1) = ? "            
            pstmt = conn.prepareStatement("SELECT * FROM sss_schedule sss "
                    + "WHERE (currentStatus != 'removed' OR currentStatus IS NULL) "
                    + "AND tag = ? "
                    + "AND payrollDate = ? "
                    + "AND (actionTaken IS NULL OR actionTaken = 'adjusted') "
                    + "ORDER BY EmployeeName ASC ");
            pstmt.setInt(1, branchId);
            pstmt.setString(2, CommonUtil.convertDateFormat(payrollDate.toString()));
            rs = pstmt.executeQuery();
            while(rs.next()){
                SssSchedule s = new SssSchedule();
                s.setEmployeeId(rs.getString("employeeId"));
                s.setName(rs.getString("EmployeeName"));
                s.setSssNo(rs.getString("sssNo"));
                s.setEeShare(util.convertStringToDouble(rs.getString("eeShare")));
                s.setErShare(util.convertStringToDouble(rs.getString("erShare")));
                s.setEc(util.convertStringToDouble(rs.getString("ec")));
                s.setBranch(rs.getString("branch"));
                s.setCorporate(rs.getString("corporateName"));
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

    @Override
//    public List<PhicSchedule> getPhicContribution(int corporateId, int month, int year) {
    public List<PhicSchedule> getPhicContribution(int branchId, Date payrollDate) {
            Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PhicSchedule> psList = new ArrayList<>();
                
        try {
//            pstmt = conn.prepareStatement("SELECT * FROM phic_schedule "
//                    + "WHERE (CurrentStatus != 'removed' OR CurrentStatus IS NULL) "                  
//                    + "AND YEAR(payrollDate) = ? "
//                    + "AND MONTH(payrollDate) = ? "
//                    + "AND ECMCorporateID = ? " 
//                    + "AND PhicAmount != 0 ORDER BY EmployeeName ASC");
            
//                    + "AND (SELECT branchId FROM employee_contribution_main WHERE employeeId = phic.employeeId ORDER BY id DESC LIMIT 1) = ? "            
            pstmt = conn.prepareStatement("SELECT * FROM phic_schedule phic "
                    + "WHERE (currentStatus != 'removed' OR currentStatus IS NULL) "
                    + "AND tag = ? "
                    + "AND payrollDate = ? "
                    + "AND (actionTaken IS NULL OR actionTaken = 'adjusted') "
                    + "ORDER BY EmployeeName ASC ");
            pstmt.setInt(1, branchId);
            pstmt.setString(2, CommonUtil.convertDateFormat(payrollDate.toString()));
            rs = pstmt.executeQuery();
            while(rs.next()){
                PhicSchedule ps = new PhicSchedule();
                ps.setEmployeeId(rs.getString("EmployeeID"));
                ps.setEmployeeName(rs.getString("EmployeeName"));
                ps.setPhicNo(rs.getString("PhicNo"));
                ps.setEePhic(util.convertStringToDouble(rs.getString("PhicAmount")));
                ps.setErPhic(util.convertStringToDouble(rs.getString("PhicAmount")));
                ps.setBranchName(rs.getString("BranchName"));
                ps.setCorporateName(rs.getString("corporateName"));
                psList.add(ps);
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
        
        return psList;
    }    

    @Override
//    public List<HdmfSchedule> getHdmfContribution(int corporateId, int month, int year) {
    public List<HdmfSchedule> getHdmfContribution(int branchId, Date payrollDate) {
            Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<HdmfSchedule> hsList = new ArrayList<>();
                
        try {
//            pstmt = conn.prepareStatement("SELECT * FROM hdmf_schedule "
//                    + "WHERE (CurrentStatus != 'removed' OR CurrentStatus IS NULL) "
//                    + "AND YEAR(payrollDate) = ? "
//                    + "AND MONTH(payrollDate) = ? "
//                    + "AND ECMCorporateID = ? " 
//                    + "AND HdmfAmount != 0 ORDER BY EmployeeName ASC");
            
//                    + "AND (SELECT branchId FROM employee_contribution_main WHERE employeeId = hdmf.employeeId ORDER BY id DESC LIMIT 1) = ? "            
            pstmt = conn.prepareStatement("SELECT * FROM hdmf_schedule hdmf "
                    + "WHERE (currentStatus != 'removed' OR currentStatus IS NULL) "
                    + "AND tag = ? "
                    + "AND payrollDate = ? "
                    + "AND (actionTaken IS NULL OR actionTaken = 'adjusted') "
                    + "ORDER BY EmployeeName ASC ");
            pstmt.setInt(1, branchId);
            pstmt.setString(2, CommonUtil.convertDateFormat(payrollDate.toString()));
            rs = pstmt.executeQuery();
            while(rs.next()){
                HdmfSchedule hs = new HdmfSchedule();
                hs.setEmployeeId(rs.getString("EmployeeID"));
                hs.setEmployeeName(rs.getString("EmployeeName"));
                hs.setHdmfNo(rs.getString("HdmfNo"));
                hs.setEeHdmf(util.convertStringToDouble(rs.getString("HdmfAmount")));
                hs.setErHdmf(util.convertStringToDouble(rs.getString("HdmfAmount")));
                hs.setBranchName(rs.getString("BranchName"));
                hs.setCorporateName(rs.getString("corporateName"));
                hsList.add(hs);
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
        
        return hsList;
    }

    @Override
    public List<TaxSchedule> getTaxContribution(int corporateId, Date payrollDate) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<TaxSchedule> tsList = new ArrayList<>();
                
        try {
            pstmt = conn.prepareStatement("SELECT * FROM tax_schedule "
                    + "WHERE (CurrentStatus != 'removed' OR CurrentStatus IS NULL) "
                    + "AND CorporateID = ? "
                    + "AND payrollDate = ? "
                    + "AND TaxAmount != 0 ORDER BY EmployeeName ASC");
//            pstmt = conn.prepareStatement("SELECT * FROM tax_schedule tax "
//                    + "LEFT JOIN (SELECT ecm.CorporateID, ecm.employeeId FROM employee_contribution_main ecm) ec "
//                    + "ON tax.EmployeeID = ec.employeeId "
//                    + "WHERE ec.CorporateId = ? "
//                    + "AND MONTH(payrollDate) = ? "
//                    + "AND YEAR(payrollDate) = ? "
//                    + "AND tax.TaxAmount != 0 ORDER BY tax.EmployeeName ASC");
            pstmt.setInt(1, corporateId);
            pstmt.setString(2, util.convertDateFormat(payrollDate.toString()));
            rs = pstmt.executeQuery();
            while(rs.next()){
                TaxSchedule ts = new TaxSchedule();
                ts.setEmployeeId(rs.getString("EmployeeID"));
                ts.setEmployeeName(rs.getString("EmployeeName"));
                ts.setTinNo(rs.getString("TinNo"));
                ts.setTaxAmount(util.convertStringToDouble(rs.getString("TaxAmount")));
                ts.setBranchName(rs.getString("BranchName"));
                ts.setCorporateName(rs.getString("corporateName"));
                tsList.add(ts);
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
        
        return tsList;
    }

    @Override
    public List<AFLSchedule> findAFLByCompany(int corporateId, Date payrollDate) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AFLSchedule> aflList = new ArrayList<>();
                        
        try {
            pstmt = conn.prepareStatement("SELECT * FROM afl_schedule "
                    + "WHERE (CurrentStatus != 'removed' OR CurrentStatus IS NULL) "
                    + "AND CorporateID = ? "
                    + "AND payrollDate = ? "
                    + "AND AllowanceForLiquidation != 0 "
                    + "ORDER BY EmployeeName ASC");
            pstmt.setInt(1, corporateId);
            pstmt.setString(2, util.convertDateFormat(payrollDate.toString()));
            rs = pstmt.executeQuery();
            while(rs.next()){
                AFLSchedule a = new AFLSchedule();
                a.setEmployeeId(rs.getString("EmployeeID"));
                a.setEmployeeName(rs.getString("EmployeeName"));
                a.setAmount(util.convertStringToDouble(rs.getString("AllowanceForLiquidation")));
                a.setBranchName(rs.getString("BranchName"));
                a.setCorporateName(rs.getString("corporateName"));
                aflList.add(a);
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
        
        return aflList;        
    }
}
