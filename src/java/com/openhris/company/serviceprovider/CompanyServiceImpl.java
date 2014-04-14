/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.company.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.company.model.Branch;
import com.openhris.company.model.Company;
import com.openhris.company.model.Trade;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.service.CompanyService;
import java.util.List;

/**
 *
 * @author jet
 */
public class CompanyServiceImpl implements CompanyService {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    ServiceGetDAO serviceGet = new ServiceGetDAO();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();

    @Override
    public List<Company> getAllCorporation() {
        return serviceGet.getCorporateLists();
    }

    @Override
    public int getCorporateId(String corporateName) {
        return serviceGet.getCoporateId(corporateName);
    }

    @Override
    public List<Trade> getTradeByCorporateId(int corporateId) {
        return serviceGet.getTradeByCorporateId(corporateId);
    }

    @Override
    public int getTradeId(String tradeName, int corporateId) {
        return serviceGet.getTradeId(tradeName, corporateId);
    }

    @Override
    public String getTradeById(int tradeId) {
        return serviceGet.getTradeById(tradeId);
    }

    @Override
    public List<Branch> getBranchByTrade(int tradeId, int corporateId) {
        return serviceGet.getBranchByTrade(tradeId, corporateId);
    }

    @Override
    public int getBranchId(int tradeId, String branchName) {
        return serviceGet.getBranchId(branchName, tradeId);
    }

    @Override
    public String getBranchById(int branchId) {
        return serviceGet.getBranchById(branchId);
    }

    @Override
    public List<Company> getCorporateListAssignedForUser(int userId) {
        return serviceGet.getCorporateListAssignedForUser(userId);
    }

    @Override
    public List<Trade> getTradeListAssignedForUser(int userId, int corporateId) {
        return serviceGet.getTradeListAssignedForUser(userId, corporateId);
    }

    @Override
    public List<Branch> getBranchListAssignedForUser(int userId, int tradeId) {
        return serviceGet.getBranchListAssignedForUser(userId, tradeId);
    }

    @Override
    public List<Branch> getAllCorporateTradeBranch() {
        return serviceGet.getAllCorporateTradeBranch();
    }

    @Override
    public boolean removeCorporateAssignedForUser(int corporateId, int userId) {
        return serviceUpdate.removeCorporateAssignedForUser(corporateId, userId);
    }

    @Override
    public boolean removeTradeAssignedForUser(int tradeId, int userId) {
        return serviceUpdate.removeTradeAssignedForUser(tradeId, userId);
    }

    @Override
    public boolean removeBranchAssignedForUser(int branchId, int userId) {
        return serviceUpdate.removeBranchAssignedForUser(branchId, userId);
    }
    
}
