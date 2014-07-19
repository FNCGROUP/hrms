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
import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.administrator.service.AdministratorService;
import java.util.List;

/**
 *
 * @author jet
 */
public class AdministratorServiceImpl implements AdministratorService {

    ServiceGetDAO serviceGet = new ServiceGetDAO();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    AdministratorDAO adminDAO = new AdministratorDAO();
    
    @Override
    public List<UserAccessControl> getUserAccessControl(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getUserList() {
        return serviceGet.getUserList();
    }

    @Override
    public boolean insertNewUser(String username, String password, String role, String employeeId) {
        return serviceInsert.insertNewUser(username, password, role, employeeId);
    }

    @Override
    public boolean checkUsernameIfExist(String username) {
        return serviceInsert.checkUsernameIfExist(username);
    }

    @Override
    public boolean updateUserRole(int id, String role) {
        return serviceUpdate.updateUserRole(id, role);
    }

    @Override
    public boolean updateUserPassword(int userId, String password) {
        return adminDAO.updateUserPassword(userId, password);
    }

    @Override
    public boolean removeUser(int id) {
        return serviceUpdate.removeUser(id);
    }

    @Override
    public boolean insertNewBranchForAccessToUser(int userId, int tradeId, int branchId) {
        return serviceInsert.insertNewBranchForAccessToUser(userId, tradeId, branchId);
    }

    @Override
    public boolean checkBranchAssignedToUserIfExist(int branchId, int userId) {
        return serviceGet.checkBranchAssignedToUserIfExist(branchId, userId);
    }

    @Override
    public boolean insertNewCorporateForAccessToUser(int userId, int corporateId) {
        return serviceInsert.insertNewCorporateForAccessToUser(userId, corporateId);
    }

    @Override
    public boolean insertNewTradeForAccessToUser(int userId, int corporateId, int tradeId) {
        return serviceInsert.insertNewTradeForAccessToUser(userId, corporateId, tradeId);
    }

    @Override
    public boolean checkTradeAssignedToUserIfExist(int tradeId, int userId) {
        return serviceGet.checkTradeAssignedToUserIfExist(tradeId, userId);
    }

    @Override
    public boolean checkCorporateAssignedToUserIfExist(int corporateId, int userId) {
        return serviceGet.checkCorporateAssignedToUserIfExist(corporateId, userId);
    }

    @Override
    public int getUserId(String username) {
        return serviceGet.getUserId(username);
    }

    @Override
    public List<UserToolbarMenuAccess> getListOfUserToolbarMenuAccess() {
        return serviceGet.getListOfUserToolbarMenuAccess();
    }

    @Override
    public List<UserAdvanceAccess> getListOfUserAdvanceAccess() {
        return serviceGet.getListOfUserAdvanceAccess();
    }

    @Override
    public boolean allowAccessOfUserToolbarMenu(int userId, String menu, boolean isAllowed) {
        return serviceUpdate.allowAccessOfUserToolbarMenu(userId, menu, isAllowed);
    }

    @Override
    public boolean allowAccessOfUserAdvanceAccess(int userId, String menu, boolean isAllowed) {
        return serviceUpdate.allowAccessOfUserAdvanceAccess(userId, menu, isAllowed);
    }

    @Override
    public boolean checkEnteredPasswordIfCorrect(int userId, String password) {
        return adminDAO.checkEnteredPasswordIfCorrect(userId, password);
    }
    
}
