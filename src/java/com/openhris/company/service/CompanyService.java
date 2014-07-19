/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.company.service;

import com.openhris.company.model.Branch;
import com.openhris.company.model.Company;
import com.openhris.company.model.Trade;
import java.util.List;

/**
 *
 * @author jet
 */
public interface CompanyService {
    
    public List<Branch> getAllCorporateTradeBranch();
    
    public List<Company> getAllCorporation();
    
    public int getCorporateId(String corporateName);
    
    public int getCorporateIdByTradeId(int tradeId);
    
    public String getCorporateById(int corporateId);
    
    public List<Company> getCorporateListAssignedForUser(int userId);
    
    public List<Trade> getTradeByCorporateId(int corporateId);
    
    public int getTradeId(String tradeName, int corporateId);
    
    public String getTradeById(int tradeId);
    
    public int getTradeIdByBranchId(int branchId);
    
    public List<Trade> getTradeListAssignedForUser(int userId, int corporateId);
    
    public List<Branch> getBranchByTrade(int tradeId, int corporateId);
        
    public int getBranchId(int tradeId, String branchName);
    
    public String getBranchById(int branchId);
    
    public List getBranchListAssignedForUser(int userId, int tradeId);
    
    public boolean removeCorporateAssignedForUser(int corporateId, int userId);
    
    public boolean removeTradeAssignedForUser(int tradeId, int userId);
    
    public boolean removeBranchAssignedForUser(int branchId, int userId);
}
