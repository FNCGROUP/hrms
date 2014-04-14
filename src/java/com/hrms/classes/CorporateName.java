/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.hrms.queries.GetSQLQuery;
import com.vaadin.ui.NativeSelect;
import java.util.List;

/**
 *
 * @author jet
 */
public class CorporateName{
    
    GetSQLQuery query = new GetSQLQuery();
    private String userRole;
    private String username = System.getProperty("username");
    
    public void setUserRole(String userRole){
        this.userRole = userRole;
    }
     
    
    public NativeSelect getCorporateName(NativeSelect corporateNames){
        int userId = query.getUserId(username);
        corporateNames.removeAllItems();
        corporateNames.setNullSelectionAllowed(false); 
        if(userRole.equals("encoder")){
            List<String> corporateLists = query.getCorporateNameListForUser(userId);
            for(int i = 0; i < corporateLists.size(); i++){
                corporateNames.addItem(corporateLists.get(i).toUpperCase());
            }
        }else{
            List<String> corporateLists = query.getCorporateLists();
            for(int i = 0; i < corporateLists.size(); i++){
                corporateNames.addItem(corporateLists.get(i).toUpperCase());
            }
        }          
        corporateNames.setImmediate(true); 
        return corporateNames;
    }
    
    public NativeSelect getCorporateAssignedToUser(NativeSelect corporateNames, int userId){
        corporateNames.removeAllItems();
        corporateNames.setNullSelectionAllowed(false);
        List<String> corporateLists = query.getCorporateNameListForUser(userId);
            for(int i = 0; i < corporateLists.size(); i++){
                corporateNames.addItem(corporateLists.get(i).toUpperCase());
            }
        corporateNames.setImmediate(true); 
        return corporateNames;
    }
}
