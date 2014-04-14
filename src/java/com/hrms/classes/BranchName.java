/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.hrms.queries.GetSQLQuery;
import com.vaadin.ui.NativeSelect;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jet
 */
public class BranchName {
    
    GetSQLQuery query = new GetSQLQuery();
    private String userRole;
    private String username = System.getProperty("username");
    
    public void setUserRole(String userRole){
        this.userRole = userRole;
    }
    
    public NativeSelect getBranchName(NativeSelect branchName, String tradeName, String corporateName){
        branchName.removeAllItems();
        branchName.setNullSelectionAllowed(false);
        int tradeId = query.getTradeId(tradeName, corporateName);
        if(userRole.equals("encoder")){
            List<String> branchLists = query.getBranchListForUser(tradeId);
            for(int i = 0; i < branchLists.size(); i++){
                branchName.addItem(branchLists.get(i).toUpperCase());
            }
        }else{            
            List<String> branchLists = query.getBranchLists(tradeId);
            for(int i = 0; i < branchLists.size(); i++){
                branchName.addItem(branchLists.get(i).toUpperCase());
            }
        }
        /* List<String> branchLists = query.getBranchLists(tradeName);
        for(int i = 0; i < branchLists.size(); i++){
            branchName.addItem(branchLists.get(i).toUpperCase());
        } */
        branchName.setImmediate(true);
        return branchName;
    }
    
}
