/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.admin.module;

import com.hrms.admin.AdminApp;
import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.EmployeesListPerBranch;
import com.hrms.classes.TradeName;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class CreateNewUser extends VerticalLayout {
    
    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities convertUtil = new ConvertionUtilities();
    CorporateName corporateNames = new CorporateName();
    TradeName tradeNames = new TradeName();
    BranchName branchNames = new BranchName();
    EmployeesListPerBranch employeesListPerBranch = new EmployeesListPerBranch();
    NativeSelect corporateName;
    NativeSelect tradeName;
    NativeSelect branchName;
    NativeSelect employeesName;
    Table userTbl = new Table();
    Table branchTbl = new Table();
    Table tradeTbl = new Table();
    Table corporateTbl = new Table();
    
    private Integer corporateId;
    private Integer tradeId;
    private Integer branchId;
    private String employeeId;
    private String userRole;
    private Integer userId;
    private String usersName;
    
    NativeSelect corporateName1;
    NativeSelect corporateName2;
    NativeSelect corporateName3;
    NativeSelect tradeName1;
    NativeSelect tradeName2;
    NativeSelect branchName1;
    
    public CreateNewUser(String userRole){
        
        this.userRole = userRole;
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        
        VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(32, Sizeable.UNITS_PERCENTAGE);
        
        GridLayout comboBoxGrid = new GridLayout(3, 3);
        comboBoxGrid.setMargin(true);
        comboBoxGrid.setSpacing(true);
        comboBoxGrid.setWidth(90, Sizeable.UNITS_PERCENTAGE);
        
        vsplit.setFirstComponent(comboBoxGrid);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        corporateName = new NativeSelect("Select Corporate Name:");
        corporateName.setWidth("270px");
        corporateNames.getCorporateName(corporateName);
        comboBoxGrid.addComponent(corporateName, 0, 0);
        
        tradeName = new NativeSelect("Select Trade Name:"); 
        tradeName.setWidth("270px");
        corporateName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                tradeNames.getTradeName(tradeName, corporateName.getValue().toString());
            }
            
        });        
        comboBoxGrid.addComponent(tradeName, 1, 0);
        
        branchName = new NativeSelect("Select Branch:");
        branchName.setWidth("270px");        
        tradeName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tradeName.getValue() == null){                    
                }else{
                    branchNames.getBranchName(branchName, tradeName.getValue().toString(), corporateName.getValue().toString());
                }                
            }
        });
        branchName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branchName.getValue() == null){                    
                }else{
                    branchId = query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), 
                            corporateName.getValue().toString());
                }                
            }
            
        });
        comboBoxGrid.addComponent(branchName, 2, 0);
        
        employeesName = new NativeSelect("Select Employee: ");
        employeesName.setWidth("270px");
        branchName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branchName.getValue() == null){
                    getWindow().showNotification("Select a Branch!");
                }else{
                    employeesListPerBranch.getEmployeesListPerBranch(employeesName, 
                            query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), 
                            corporateName.getValue().toString()));
                }
            }
            
        });
        employeesName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                employeeId = query.getEmployeesId(employeesName.getValue().toString());
            }
            
        });
        comboBoxGrid.addComponent(employeesName, 0, 1);
        
        final TextField username = new TextField("Username: ");
        username.setWidth("270px");
        username.setNullSettingAllowed(false);
        comboBoxGrid.addComponent(username, 1, 1);
        
        final PasswordField password1 = new PasswordField("Password: ");
        password1.setWidth("270px");
        password1.setNullSettingAllowed(false);
        comboBoxGrid.addComponent(password1, 2, 1);
        
        final PasswordField password2 = new PasswordField("Confirm Password: ");
        password2.setWidth("270px");
        password2.setNullSettingAllowed(false);
        comboBoxGrid.addComponent(password2, 2, 2);
        
        Button save = new Button("Create New User");
        save.setWidth("270px");
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean usernameResult = query.checkUsernameIfExist(username.getValue().toString().trim()), result;
                if(usernameResult == false){
                    getWindow().showNotification("Username already exist!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(username.getValue().toString().trim().isEmpty() || password1.getValue().toString().trim().isEmpty() || 
                        password1.getValue().toString().trim().isEmpty() || corporateName.getValue() == null || 
                        tradeName.getValue() == null || branchName.getValue() == null || employeesName.getValue() == null){
                    getWindow().showNotification("Empty Field is not allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(!password1.getValue().toString().trim().equals(password2.getValue().toString().trim())){
                    getWindow().showNotification("Password do not Match!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                result = query.saveNewUser(username.getValue().toString().trim(), password1.getValue().toString().trim(), employeeId);
                if(result = true){
                    userTable();
                }else{
                    getWindow().showNotification("ERROR SQL!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        comboBoxGrid.addComponent(save, 1, 2);
        comboBoxGrid.setComponentAlignment(save, Alignment.BOTTOM_LEFT);
        
        userTable();
        vsplit.setSecondComponent(userTbl);
    }
    
    public final void userTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        userTbl.removeAllItems();
        userTbl.setSizeFull();
        userTbl.setImmediate(true);
        userTbl.setSelectable(true);
        
        userTbl.addContainerProperty("id", String.class, null);
        userTbl.addContainerProperty("name", String.class, null);
        userTbl.addContainerProperty("trade", String.class, null);
        userTbl.addContainerProperty("branch", String.class, null);
        userTbl.addContainerProperty("username", String.class, null);
        userTbl.addContainerProperty("role", String.class, null);
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select * FROM user_access_control WHERE role != 'administrator' ");
            while(rs.next()){
                userTbl.addItem(new Object[]{
                    rs.getString("userId"),
                    rs.getString("name").toUpperCase(),
                    rs.getString("trade").toUpperCase(),
                    rs.getString("branch").toUpperCase(), 
                    rs.getString("username"), 
                    rs.getString("role")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CreateNewUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : userTbl.getListeners(ItemClickEvent.class)){
            userTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        userTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = userTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    userId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    usersName = item.getItemProperty("name").toString();
                    Window subWindow = removeUser(userId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("name")){
                    userId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    usersName = item.getItemProperty("name").toString();
                    Window subWindow = addAccessToUser();
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("username")){
                    userId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    Window subWindow = changeUserPassword(userId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("role")){
                    String userId = item.getItemProperty("id").getValue().toString();
                    Window subWindow = editRole(userId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
            
        });
    }
    
    private Window addAccessToUser(){                
        final Window subWindow = new Window("ADD Access to "+usersName.toUpperCase());
        subWindow.setWidth("400px");
        
        corporateTable();
        tradeTable();
        branchTable();
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        corporateName2 = new NativeSelect("Select Corporate Name:");
        corporateName2.setWidth("100%");
        corporateName2.setNullSelectionAllowed(false);
        corporateNames.getCorporateAssignedToUser(corporateName2, userId);
        
        corporateName3 = new NativeSelect("Select Corporate Name:");
        corporateName3.setWidth("100%");
        corporateName3.setNullSelectionAllowed(false);
        corporateNames.getCorporateAssignedToUser(corporateName3, userId);
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        corporateName1 = new NativeSelect("Select Corporate Name:");
        corporateName1.setWidth("100%");
        corporateName1.setNullSelectionAllowed(false);
        corporateNames.getCorporateName(corporateName1);
        vlayout.setCaption("Corporate");
        vlayout.addComponent(corporateName1);
        vlayout.addComponent(corporateTbl);
        
        Button addCorporate = new Button("Add Corporate to "+usersName.toUpperCase());
        addCorporate.setWidth("100%");
        addCorporate.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(corporateName1 == null){
                    getWindow().showNotification("Select Corporate Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;                    
                }
                corporateId = query.getCoporateId(corporateName1.getValue().toString());
                
                Boolean checkResult = query.checkCorporateIfExistForUser(corporateId, userId);
                
                if(checkResult == false){
                    getWindow().showNotification("Corporate Name already exist for "+usersName.toUpperCase(), Window.Notification.TYPE_WARNING_MESSAGE);
                    return; 
                }                
                
                Boolean result = query.addCorporateToUser(userId, corporateId);                
                
                if(result == true){
                    corporateTable();
                    corporateNames.getCorporateName(corporateName1);
                    corporateNames.getCorporateAssignedToUser(corporateName2, userId);
                    corporateNames.getCorporateAssignedToUser(corporateName3, userId);
                    //(subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("Corporate SQL Error!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
                
            }
            
        });
        vlayout.addComponent(addCorporate);
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);        
        
        tradeName1 = new NativeSelect("Select Trade Name:"); 
        tradeName1.setWidth("100%");
        corporateName2.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporateName2.getValue() == null){  
                    tradeName1.removeAllItems();
                }else{
                    tradeNames.getTradeName(tradeName1, corporateName2.getValue().toString());
                }                
            }
            
        });
        vlayout.setCaption("Trade");
        vlayout.addComponent(corporateName2);
        vlayout.addComponent(tradeName1);
        vlayout.addComponent(tradeTbl);
        
        Button addTrade  = new Button("Add Trade to "+usersName.toUpperCase());
        addTrade.setWidth("100%");
        addTrade.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(tradeName1 == null){
                    getWindow().showNotification("Select Trade Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;                    
                }
                
                corporateId = query.getCoporateId(corporateName2.getValue().toString());
                tradeId = query.getTradeId(tradeName1.getValue().toString(), corporateName2.getValue().toString());
                Boolean result = query.addTradeToUser(userId, corporateId, tradeId);
                
                if(result == true){
                    tradeTable();
                }else{
                    getWindow().showNotification("Trade SQL Error/Corporate Name is not in Users LISTS!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        vlayout.addComponent(addTrade);
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);        
        
        tradeName2 = new NativeSelect("Select Trade Name:"); 
        tradeName2.setWidth("100%");
        corporateName3.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporateName3.getValue() == null){  
                    tradeName2.removeAllItems();
                }else{
                    tradeNames.getTradeName(tradeName2, corporateName3.getValue().toString());
                }                
            }
            
        });
        branchName1 = new NativeSelect("Select Branch:");
        branchName1.setWidth("100%"); 
        tradeName2.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tradeName2.getValue() == null){  
                    branchName1.removeAllItems();
                }else{
                    branchNames.getBranchName(branchName1, tradeName2.getValue().toString(), corporateName3.getValue().toString());
                }                
            }
        });
        branchName1.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(branchName1.getValue() == null){                    
                }else{
                    branchId = query.getBranchId(branchName1.getValue().toString(), tradeName2.getValue().toString(), corporateName3.getValue().toString());
                }                
            }
            
        });
        vlayout.setCaption("Branch");
        vlayout.addComponent(corporateName3);
        vlayout.addComponent(tradeName2);
        vlayout.addComponent(branchName1);
        vlayout.addComponent(branchTbl);
        
        Button addBranch = new Button("Add Branch to "+usersName.toUpperCase());
        addBranch.setWidth("100%");
        addBranch.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(corporateName3.getValue() == null){
                    getWindow().showNotification("Select a Corporate!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(tradeName2.getValue() == null){
                    getWindow().showNotification("Select a Trade!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(branchName1.getValue() == null){
                    getWindow().showNotification("Select a Branch!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                tradeId = query.getTradeId(tradeName2.getValue().toString(), corporateName3.getValue().toString());
                Boolean result = query.addBranchToUser(userId, tradeId, branchId);
                if(result == true){
                    branchTable();
                }else{
                    getWindow().showNotification("Branch SQL Error/Trade Name is not in Users LISTS!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });        
        vlayout.addComponent(addBranch);
        ts.addComponent(vlayout);
        
        subWindow.addComponent(ts);       
        
        return subWindow;
    }
    
    private Window editRole(final String id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("Edit Role", vlayout);
        subWindow.setWidth("200px");
        
        String[] roleArray = {"encoder", "accounting", "auditor", "hr"};
        
        final ComboBox role = new ComboBox("Select Role: ");
        role.setWidth("100%");
        role.setNullSelectionAllowed(false);
        for(int i = 0; i < roleArray.length; i++){
            role.addItem(roleArray[i]);
        }
        role.setImmediate(true);
        subWindow.addComponent(role);
        
        Button save = new Button("APPLY");
        save.setWidth("100%");
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(role.getValue() == null){
                    getWindow().showNotification("Select a Role!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = queryUpdate.updateUserRole(id, role.getValue().toString());
                if(result == true){
                    userTable();
                    AdminApp adminApp = new AdminApp();
                    adminApp.userAccessTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        subWindow.addComponent(save);
        
        return subWindow;
    }    
    
    private Table branchTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
                
        branchTbl.removeAllItems();
        branchTbl.setWidth("100%");
        branchTbl.setSelectable(true);
        branchTbl.setImmediate(true);
        
        branchTbl.addContainerProperty("id", String.class, null);
        branchTbl.addContainerProperty("branch", String.class, null);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT uba.id AS id, b.name AS name FROM user_branch_access uba "
                    + "INNER JOIN branch b ON uba.branchId = b.id WHERE uba.userId = '"+userId+"' ");
            while(rs.next()){
                branchTbl.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("name")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CreateNewUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        branchTbl.setPageLength(20);
        
        for(Object listener : branchTbl.getListeners(ItemClickEvent.class)){
            branchTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        branchTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = branchTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    String id = item.getItemProperty("id").toString();
                    Window subWindow = removeBranch(id);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
            
        });
        
        return branchTbl;
    }
    
    private Window removeBranch(final String id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE BRANCH", vlayout);
        subWindow.setWidth("200px");
        
        Button removeBranchButton = new Button("REMOVE BRANCH?");
        removeBranchButton.setWidth("100%");
        removeBranchButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean result = queryUpdate.removeBranchFromUser(id);
                if(result == true){
                    branchTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("Cannot remove BRANCH!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeBranchButton);
        
        return subWindow;
    }
    
    private Table tradeTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
                
        tradeTbl.removeAllItems();
        tradeTbl.setWidth("100%");
        tradeTbl.setSelectable(true);
        tradeTbl.setImmediate(true);
        
        tradeTbl.addContainerProperty("id", String.class, null);
        tradeTbl.addContainerProperty("name", String.class, null);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT uta.id AS id, tn.name AS name FROM user_trade_access uta INNER JOIN trade_name tn "
                    + "ON uta.tradeId = tn.id WHERE uta.userId = '"+userId+"' ");
            while(rs.next()){
                tradeTbl.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("name")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CreateNewUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        tradeTbl.setPageLength(tradeTbl.size());
        
        for(Object listener : tradeTbl.getListeners(ItemClickEvent.class)){
            tradeTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        tradeTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = tradeTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    String id = item.getItemProperty("id").toString();
                    Window subWindow = removeTrade(id);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
            
        });
        
        return tradeTbl;
    }
    
    private Window removeTrade(final String id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE TRADE", vlayout);
        subWindow.setWidth("200px");
        
        Button removeTradeButton = new Button("REMOVE TRADE?");
        removeTradeButton.setWidth("100%");
        removeTradeButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean result = queryUpdate.removeTradeFromUser(id);
                if(result == true){
                    tradeTable();
                    branchTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("Cannot remove TRADE!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeTradeButton);
        
        return subWindow;
    }
    
    private Table corporateTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
                
        corporateTbl.removeAllItems();
        corporateTbl.setWidth("100%");
        corporateTbl.setSelectable(true);
        corporateTbl.setImmediate(true);
        
        corporateTbl.addContainerProperty("id", String.class, null);
        corporateTbl.addContainerProperty("name", String.class, null);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT uca.id AS id, cn.name AS name FROM user_corporate_access uca "
                    + "INNER JOIN corporate_name cn ON uca.corporateId = cn.id WHERE uca.userId = '"+userId+"' ");
            while(rs.next()){
                corporateTbl.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("name")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CreateNewUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        corporateTbl.setPageLength(corporateTbl.size());
        
        for(Object listener : corporateTbl.getListeners(ItemClickEvent.class)){
            corporateTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        corporateTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = corporateTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    String id = item.getItemProperty("id").toString();
                    Window subWindow = removeCorporate(id);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
            
        });
        
        return corporateTbl;
    }

    private Window removeCorporate(final String id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE CORPORATE", vlayout);
        subWindow.setWidth("200px");
        
        Button removeCorporateButton = new Button("REMOVE CORPORATE?");
        removeCorporateButton.setWidth("100%");
        removeCorporateButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean result = queryUpdate.removeCorporateFromUser(id);
                if(result == true){
                    corporateTable();
                    tradeTable();
                    branchTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("Cannot remove CORPORATE!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeCorporateButton);
        
        return subWindow;
    }
    
    private Window removeUser(final int id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE USER ACCOUNT", vlayout);
        subWindow.setWidth("300px");
        
        Button removeUserButton = new Button("REMOVE USER "+usersName.toUpperCase()+"?");
        removeUserButton.setWidth("100%");
        removeUserButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean result = queryUpdate.removeUserAccount(id);
                if(result == true){
                    userTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("Cannot remove CORPORATE!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeUserButton);
        
        return subWindow;
    }
    
    private Window changeUserPassword(final int id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE USER ACCOUNT", vlayout);
        subWindow.setWidth("300px");
        
        final PasswordField newPassword1 = new PasswordField("Password: ");
        newPassword1.setWidth("100%");
        newPassword1.setNullSettingAllowed(false);
        vlayout.addComponent(newPassword1);
        
        final PasswordField newPassword2 = new PasswordField("Confirm Password: ");
        newPassword2.setWidth("100%");
        newPassword2.setNullSettingAllowed(false);
        vlayout.addComponent(newPassword2);
        
        Button saveNewPassword = new Button("SAVE NEW PASSWORD?");
        saveNewPassword.setWidth("100%");
        saveNewPassword.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(!newPassword1.getValue().toString().trim().equals(newPassword2.getValue().toString().trim())){
                    getWindow().showNotification("Password do not Match!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(newPassword1.getValue().toString().isEmpty() || newPassword2.getValue().toString().isEmpty()){
                    getWindow().showNotification("Enter new Password!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                Boolean result = queryUpdate.updateUserPassword(newPassword1.getValue().toString().trim(), id);
                if(result == true){
                    userTable();
                    getWindow().showNotification("PASSWORD CHANGED!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("Cannot remove CORPORATE!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(saveNewPassword);
        
        return subWindow;
    }
}
