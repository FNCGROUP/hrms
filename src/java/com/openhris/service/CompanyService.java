/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.model.Branch;
import com.openhris.model.Company;
import com.openhris.model.Trade;
import java.util.ArrayList;
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
    
    public ArrayList<ArrayList<ArrayList<String>>> getNestedListOfCorporations();
    
    public List<Branch> getBranchByTradeId(int tradeId);
    
    public boolean updateBranchName(int branchId, String name);
    
    public boolean updateBranchAddress(int branchId, String address);
    
    public String getCorporateNameByBranchId(int branchId);
    
    public List<Branch> getBranchListForUser(int userId);
    
    public boolean removeBranchFromUser(int rowId);
    
    public List<Trade> getTradeListForUser(int userId);
    
    public boolean removeTradeFromUser(int rowId, int userId);
    
    public List<Company> getCorporateListForUser(int userId);
    
    public boolean removeCorporateFromUser(int rowId, int userId);
}
