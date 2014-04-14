/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.hrms.queries.GetSQLQuery;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import java.util.List;

/**
 *
 * @author jet
 */
public class TradeName{
    
    GetSQLQuery query = new GetSQLQuery();
    private String userRole;
    private String username = System.getProperty("username");
    
    public void setUserRole(String userRole){
        this.userRole = userRole;
    }
    
    public NativeSelect getTradeName(NativeSelect tradeName, String corporateName){
        int userId = query.getUserId(username);
        tradeName.removeAllItems();         
        tradeName.setNullSelectionAllowed(false);
        if(userRole.equals("encoder")){
            List<String> tradeLists = query.getTradeNameListForUser(corporateName);
            for(int i = 0; i < tradeLists.size(); i++){
                tradeName.addItem(tradeLists.get(i).toUpperCase());
            }
        }else{
            List<String> tradeLists = query.getTradeLists(corporateName);
            for(int i = 0; i < tradeLists.size(); i++){
                tradeName.addItem(tradeLists.get(i).toUpperCase());
            }
        }
        /*List<String> tradeLists = query.getTradeLists(corporateName);
        for(int i = 0; i < tradeLists.size(); i++){
            tradeName.addItem(tradeLists.get(i).toUpperCase());
        }*/
        tradeName.setImmediate(true);
        return tradeName;
    }
    
}
