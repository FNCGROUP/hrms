/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator.serviceprovider;

import com.openhris.administrator.dao.AdministratorDAO;
import com.openhris.administrator.model.User;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.administrator.model.UserAdvanceAccess;
import com.openhris.administrator.model.UserToolbarMenuAccess;
import com.openhris.administrator.service.AdministratorService;
import java.util.List;

/**
 *
 * @author jet
 */
public class AdministratorServiceImpl implements AdministratorService {

    AdministratorDAO adminDAO = new AdministratorDAO();
    
    @Override
    public List<UserAccessControl> getUserAccessControl(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getUserList() {
        return adminDAO.getUserList();
    }

    @Override
    public boolean insertNewUser(String username, String password, String role, String employeeId) {
        return adminDAO.insertNewUser(username, password, role, employeeId);
    }

    @Override
    public boolean checkUsernameIfExist(String username) {
        return adminDAO.checkUsernameIfExist(username);
    }

    @Override
    public boolean updateUserRole(int id, String role) {
        return adminDAO.updateUserRole(id, role);
    }

    @Override
    public boolean updateUserPassword(int userId, String password) {
        return adminDAO.updateUserPassword(userId, password);
    }

    @Override
    public boolean removeUser(int id) {
        return adminDAO.removeUser(id);
    }

    @Override
    public boolean insertNewBranchForAccessToUser(int userId, int tradeId, int branchId) {
        return adminDAO.insertNewBranchForAccessToUser(userId, tradeId, branchId);
    }

    @Override
    public boolean checkBranchAssignedToUserIfExist(int branchId, int userId) {
        return adminDAO.checkBranchAssignedToUserIfExist(branchId, userId);
    }

    @Override
    public boolean insertNewCorporateForAccessToUser(int userId, int corporateId) {
        return adminDAO.insertNewCorporateForAccessToUser(userId, corporateId);
    }

    @Override
    public boolean insertNewTradeForAccessToUser(int userId, int corporateId, int tradeId) {
        return adminDAO.insertNewTradeForAccessToUser(userId, corporateId, tradeId);
    }

    @Override
    public boolean checkTradeAssignedToUserIfExist(int tradeId, int userId) {
        return adminDAO.checkTradeAssignedToUserIfExist(tradeId, userId);
    }

    @Override
    public boolean checkCorporateAssignedToUserIfExist(int corporateId, int userId) {
        return adminDAO.checkCorporateAssignedToUserIfExist(corporateId, userId);
    }

    @Override
    public int getUserId(String username) {
        return adminDAO.getUserId(username);
    }

    @Override
    public List<UserToolbarMenuAccess> getListOfUserToolbarMenuAccess() {
        return adminDAO.getListOfUserToolbarMenuAccess();
    }

    @Override
    public List<UserAdvanceAccess> getListOfUserAdvanceAccess() {
        return adminDAO.getListOfUserAdvanceAccess();
    }

    @Override
    public boolean allowAccessOfUserToolbarMenu(int userId, String column, boolean isAllowed) {
        return adminDAO.allowAccessOfUserToolbarMenu(userId, column, isAllowed);
    }

    @Override
    public boolean allowAccessOfUserAdvanceAccess(int userId, String column, boolean isAllowed) {
        return adminDAO.allowAccessOfUserAdvanceAccess(userId, column, isAllowed);
    }

    @Override
    public boolean checkEnteredPasswordIfCorrect(int userId, String password) {
        return adminDAO.checkEnteredPasswordIfCorrect(userId, password);
    }

    @Override
    public boolean getUserAdvanceAccess(int userId, String column) {
        return adminDAO.getUserAdvanceAccess(userId, column);
    }
    
}
