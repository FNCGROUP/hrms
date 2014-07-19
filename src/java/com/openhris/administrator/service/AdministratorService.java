/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator.service;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.administrator.model.User;
import com.openhris.administrator.model.UserAdvanceAccess;
import com.openhris.administrator.model.UserToolbarMenuAccess;
import java.util.List;

/**
 *
 * @author jet
 */
public interface AdministratorService {
    
    public List<UserAccessControl> getUserAccessControl(String username);
    
    public List<UserToolbarMenuAccess> getListOfUserToolbarMenuAccess();
    
    public List<UserAdvanceAccess> getListOfUserAdvanceAccess();
    
    public List<User> getUserList();
    
    public boolean insertNewUser(String username, 
            String password, 
            String role, 
            String employeeId);    
    
    public boolean checkUsernameIfExist(String username);   
    
    public boolean updateUserRole(int id, String role);
    
    public boolean updateUserPassword(int userId, String password);
    
    public boolean removeUser(int id);
        
    public boolean insertNewCorporateForAccessToUser(int userId, int corporateId);
    
    public boolean insertNewTradeForAccessToUser(int userId, 
            int corporateId, 
            int tradeId);
    
    public boolean insertNewBranchForAccessToUser(int userId, 
            int tradeId, 
            int branchId);
    
    public boolean checkBranchAssignedToUserIfExist(int branchId, int userId);
    
    public boolean checkTradeAssignedToUserIfExist(int tradeId, int userId);
    
    public boolean checkCorporateAssignedToUserIfExist(int corporateId, int userId);
    
    public int getUserId(String username);
    
    public boolean allowAccessOfUserToolbarMenu(int userId, String menu, boolean isAllowed);
    
    public boolean allowAccessOfUserAdvanceAccess(int userId, String menu, boolean isAllowed);
    
    public boolean checkEnteredPasswordIfCorrect(int userId, String password);
}
