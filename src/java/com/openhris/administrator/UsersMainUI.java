/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator;

import com.openhris.administrator.model.User;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Branch;
import com.openhris.model.Company;
import com.openhris.model.Trade;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.model.Employee;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.administrator.service.AdministratorService;
import com.openhris.service.CompanyService;
import com.openhris.service.EmployeeService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;

/**
 *
 * @author jet
 */
public class UsersMainUI extends VerticalLayout {
    
    EmployeeService employeeService = new EmployeeServiceImpl();
    AdministratorService administratorService = new AdministratorServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    int branchId;
    String employeeId;
    
    ComboBox employeesName = new ComboBox("Employees: ");
    Table usersTbl = new Table();
    Table corporateAssignedToUserTbl = new Table();
    Table tradeByCorporateAssignedToUserTbl = new Table();
    Table branchByTradeAssignedToUserTbl = new Table();    
    TextField username;
    PasswordField password1;
    PasswordField password2;
    ComboBox userRole;
    
    //for addAccessToUser method    
    ComboBox corporate_1 = new ComboBox("Corporate: ");
    int corporate_id_1;
    
    ComboBox corporate_2 = new ComboBox("Corporate: ");
    ComboBox trade_2 = new ComboBox("Trade: ");
    int corporate_id_2;    
    int trade_id_2;
    
    ComboBox corporate_3 = new ComboBox("Corporate: ");
    ComboBox trade_3 = new ComboBox("Trade: ");
    ComboBox branch_3 = new ComboBox("Branch: ");
    int corporate_id_3;    
    int trade_id_3;
    int branch_id_3;
    
    public UsersMainUI(int branchId){
        this.branchId = branchId;
        
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        
        VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(23, Sizeable.UNITS_PERCENTAGE);
        
        GridLayout glayout = new GridLayout(3, 2);
        glayout.setMargin(true);
        glayout.setSpacing(true);
        glayout.setWidth(80, Sizeable.UNITS_PERCENTAGE);
        
        vsplit.setFirstComponent(glayout);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        employeeComboBox(branchId);
        glayout.addComponent(employeesName, 0, 0);
        
        username = new TextField("Username: ");
        username.setWidth("100%");
        username.setNullSettingAllowed(false);
        username.setStyleName(Reindeer.TEXTFIELD_SMALL);
        glayout.addComponent(username, 1, 0);
        
        userRole = dropDown.populateUserRoleList(new ComboBox());
        glayout.addComponent(userRole, 2, 0);
        
        password1 = new PasswordField("Password: ");
        password1.setWidth("100%");
        password1.setNullSettingAllowed(false);
        password1.setStyleName(Reindeer.TEXTFIELD_SMALL);
        glayout.addComponent(password1, 0, 1);
        
        password2 = new PasswordField("Confirm Password: ");
        password2.setWidth("100%");
        password2.setNullSettingAllowed(false);
        password2.setStyleName(Reindeer.TEXTFIELD_SMALL);
        glayout.addComponent(password2, 1, 1);
        
        Button saveNewUserButton = new Button("ADD NEW USER");
        saveNewUserButton.setWidth("100%");
        saveNewUserButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean checkUsernameIfExist = administratorService.checkUsernameIfExist(username.getValue().toString().trim());
                if(checkUsernameIfExist){
                    getWindow().showNotification("Username already exist!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(!password1.getValue().toString().trim().equals(password2.getValue().toString().trim())){
                    getWindow().showNotification("Password do not Match!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(username.getValue().toString().trim().isEmpty() || password1.getValue().toString().trim().isEmpty() || 
                        password1.getValue().toString().trim().isEmpty() || employeesName.getValue() == null){
                    getWindow().showNotification("Empty Field is not allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = administratorService.insertNewUser(username.getValue().toString().trim(), 
                        password1.getValue().toString().trim(), 
                        userRole.getValue().toString(), 
                        employeeId);
                if(result){
                    usersTable();
                    clearTextField();
                } else {
                    getWindow().showNotification("Cannot Create New User!", Window.Notification.TYPE_WARNING_MESSAGE);
                }                
            }
        });
        glayout.addComponent(saveNewUserButton, 2, 1);
        glayout.setComponentAlignment(saveNewUserButton, Alignment.BOTTOM_CENTER);
        
        usersTable();
        vsplit.setSecondComponent(usersTbl);
    }
    
    public void usersTable(){
        usersTbl.removeAllItems();
        usersTbl.setSizeFull();
        usersTbl.setImmediate(true);
        usersTbl.setSelectable(true);
        usersTbl.addStyleName("hris-table-layout");
        
        usersTbl.addContainerProperty("id", String.class, null);
        usersTbl.addContainerProperty("name", String.class, null);
        usersTbl.addContainerProperty("trade", String.class, null);
        usersTbl.addContainerProperty("branch", String.class, null);
        usersTbl.addContainerProperty("username", String.class, null);
        usersTbl.addContainerProperty("role", String.class, null);
        
        List<User> userList = administratorService.getUserList();
        int i = 0;
        for(User u : userList){
            usersTbl.addItem(new Object[]{
                u.getId(),
                u.getName().toUpperCase(), 
                u.getTrade(), 
                u.getBranch(), 
                u.getUsername(), 
                u.getRole()
            }, new Integer(i));
            i++;
        }
        
        for(Object listener : usersTbl.getListeners(ItemClickEvent.class)){
            usersTbl.removeListener(ItemClickEvent.class, listener);
        }
        usersTbl.setPageLength(usersTbl.size());
        
        usersTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = usersTbl.getItem(itemId);
                
                int userId = Integer.parseInt(item.getItemProperty("id").getValue().toString());                
                
                if(event.getPropertyId().equals("id")){
                    String name = item.getItemProperty("name").toString().toLowerCase();
                    Window subWindow = removeUser(userId, name);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("username")){
                    Window subWindow = updateUserPassword(userId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("role")){
                    Window subWindow = editRole(userId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("name")){
                    String name = item.getItemProperty("name").toString();
                    Window subWindow = addAccessToUser(userId, name);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
        });
    }
    
    public void employeeComboBox(final int branchId){ 
        this.branchId = branchId;        
        employeesName.removeAllItems();
        employeesName.setWidth("100%");
        employeesName.setNullSelectionAllowed(false);
        employeesName.setStyleName("my-combobox");
//        employeesName.addStyleName("combobox-height-style");
        List<Employee> employeesList = employeeService.getEmployeePerBranchForDropDownList(branchId);        
        for(Employee e : employeesList){
            String name = e.getLastname()+ ", " + e.getFirstname() + " " + e.getMiddlename();
            employeesName.addItem(name.toUpperCase());
        }
        employeesName.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){                    
                } else {
                    employeeId = employeeService.getEmployeeId(employeesName.getValue().toString());
                }
            }
        });
        employeesName.setImmediate(true);
    }
    
    public void setBranchId(int branchId){
        this.branchId = branchId;
    }
    
    public int getBranchId(){
        return branchId;
    }
 
    public void clearTextField(){
        username.setValue("");
        password1.setValue("");
        password2.setValue("");
    }
    
    public Window removeUser(final int id, String username){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE USER ACCOUNT", vlayout);
        subWindow.setWidth("300px");
        
        Button removeUserButton = new Button("REMOVE USER "+username.toUpperCase()+"?");
        removeUserButton.setWidth("100%");
        removeUserButton.addListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = administratorService.removeUser(id);
                if(result){
                    usersTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                } else {
                    getWindow().showNotification("Cannot remove USER!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        removeUserButton.setImmediate(true);
        subWindow.addComponent(removeUserButton);
        
        return subWindow;
    }
    
    public Window updateUserPassword(final int id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("CHANGE USE PASSWORD", vlayout);
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
            public void buttonClick(Button.ClickEvent event) {
                if(!newPassword1.getValue().toString().trim().equals(newPassword2.getValue().toString().trim())){
                    getWindow().showNotification("Password do not Match!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(newPassword1.getValue().toString().isEmpty() || newPassword2.getValue().toString().isEmpty()){
                    getWindow().showNotification("Enter new Password!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = administratorService.updateUserPassword(id, newPassword1.getValue().toString().trim());
                if(result){
                    usersTable();
                    getWindow().showNotification("PASSWORD CHANGED!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else {
                    getWindow().showNotification("Cannot update user's password!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        saveNewPassword.setImmediate(true);
        subWindow.addComponent(saveNewPassword);
        
        return subWindow;
    }
    
    private Window editRole(final int id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("Edit Role", vlayout);
        subWindow.setWidth("200px");
        
        final ComboBox role = dropDown.populateUserRoleList(new ComboBox());
        subWindow.addComponent(role);
        
        Button save = new Button("APPLY");
        save.setWidth("100%");
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(role.getValue() == null){
                    getWindow().showNotification("Select a Role!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = administratorService.updateUserRole(id, role.getValue().toString());
                if(result){
                    usersTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                } else {
                    getWindow().showNotification("Cannot update user's role!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        save.setImmediate(true);
        subWindow.addComponent(save);
    
        return subWindow;
    }

    private void corporateAssignedToUserTable(final int userId){
        corporateAssignedToUserTbl.removeAllItems();
        corporateAssignedToUserTbl.setWidth("100%");
        corporateAssignedToUserTbl.setSelectable(true);
        corporateAssignedToUserTbl.setImmediate(true);
        
        corporateAssignedToUserTbl.addContainerProperty("id", String.class, null);
        corporateAssignedToUserTbl.addContainerProperty("name", String.class, null);
        
        List<Company> corporateList = companyService.getCorporateListForUser(userId);
        int i = 0;
        for(Company c : corporateList){
            corporateAssignedToUserTbl.addItem(new Object[]{
                c.getCompanyId(), 
                c.getCompanyName()
            }, new Integer(i));
            i++;
        }
        corporateAssignedToUserTbl.setPageLength(corporateAssignedToUserTbl.size());
        
        for(Object listener : corporateAssignedToUserTbl.getListeners(ItemClickEvent.class)){
            corporateAssignedToUserTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        corporateAssignedToUserTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = corporateAssignedToUserTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    int rowId = util.convertStringToInteger(item.getItemProperty("id").toString());                    
                    String name = item.getItemProperty("name").toString();
                    
                    Window subWindow = removeAssignedCorporateToUser(userId, rowId, name);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
        });        
    }
    
    private void tradeByCorporateAssignedToUserTable(final int userId){
        tradeByCorporateAssignedToUserTbl.removeAllItems();
        tradeByCorporateAssignedToUserTbl.setWidth("100%");
        tradeByCorporateAssignedToUserTbl.setSelectable(true);
        tradeByCorporateAssignedToUserTbl.setImmediate(true);
        
        tradeByCorporateAssignedToUserTbl.addContainerProperty("id", String.class, null);
        tradeByCorporateAssignedToUserTbl.addContainerProperty("name", String.class, null);
        
        List<Trade> tradeList = companyService.getTradeListForUser(userId);
        int i = 0;
        for(Trade t: tradeList){
            tradeByCorporateAssignedToUserTbl.addItem(new Object[]{
                t.getTradeId(), 
                t.getTradeName()
            }, new Integer(i));
            i++;
        }
        tradeByCorporateAssignedToUserTbl.setPageLength(tradeByCorporateAssignedToUserTbl.size());
        
        for(Object listener : tradeByCorporateAssignedToUserTbl.getListeners(ItemClickEvent.class)){
            tradeByCorporateAssignedToUserTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        tradeByCorporateAssignedToUserTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = tradeByCorporateAssignedToUserTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    int rowId = util.convertStringToInteger(item.getItemProperty("id").toString());                    
                    String name = item.getItemProperty("name").toString();
                    
                    Window subWindow = removeAssignedTradeToUser(userId, rowId, name);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
        });
    }

    private void branchByTradeAssignedToUserTable(final int userId){
        branchByTradeAssignedToUserTbl.removeAllItems();
        branchByTradeAssignedToUserTbl.setWidth("100%");
        branchByTradeAssignedToUserTbl.setSelectable(true);
        branchByTradeAssignedToUserTbl.setImmediate(true);
        
        branchByTradeAssignedToUserTbl.addContainerProperty("id", String.class, null);
        branchByTradeAssignedToUserTbl.addContainerProperty("name", String.class, null);
        
        List<Branch> branchList = companyService.getBranchListForUser(userId);
        int i = 0;
        for(Branch b : branchList){
            branchByTradeAssignedToUserTbl.addItem(new Object[]{
                b.getBranchId(), 
                b.getBranchName()
            }, new Integer(i));
            i++;
        }
        branchByTradeAssignedToUserTbl.setPageLength(branchByTradeAssignedToUserTbl.size());
        
        for(Object listener : branchByTradeAssignedToUserTbl.getListeners(ItemClickEvent.class)){
            branchByTradeAssignedToUserTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        branchByTradeAssignedToUserTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = branchByTradeAssignedToUserTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    int rowId = util.convertStringToInteger(item.getItemProperty("id").toString());                    
                    String name = item.getItemProperty("name").toString();
                    
                    Window subWindow = removeAssignedBranchToUser(rowId, name, userId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
        });        
    }
    
    private Window addAccessToUser(final int userId, String name){
        final Window subWindow = new Window("ADD Access to "+name.toUpperCase());
        subWindow.setWidth("400px");
        subWindow.setHeight("100%");
                
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true); 
        corporate_1 = dropDown.populateCorporateComboBox(new ComboBox());
        corporate_1.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporate_1.getValue() == null){                    
                } else {
                    corporate_id_1 = companyService.getCorporateId(corporate_1.getValue().toString());
                }
            }
        });
        vlayout.addComponent(corporate_1);
        
        Button addCorporateButton = new Button("ADD CORPORATE to "+name.toUpperCase());
        addCorporateButton.setWidth("100%");
        addCorporateButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(corporate_1.getValue() == null){
                     getWindow().showNotification("Select a Corporate Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                boolean checkIfCorporateAssignedForUserExist = administratorService.checkCorporateAssignedToUserIfExist(corporate_id_1, userId);
                if(checkIfCorporateAssignedForUserExist){
                    getWindow().showNotification("Corporate was already assigned to user!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                boolean result = administratorService.insertNewCorporateForAccessToUser(userId, corporate_id_1);
                if(result){
                    corporateAssignedToUserTable(userId);
                }
            }
        });
        vlayout.addComponent(addCorporateButton);
        
        corporateAssignedToUserTable(userId);
        vlayout.addComponent(corporateAssignedToUserTbl); 
        vlayout.setCaption("Corporate");
        ts.addComponent(vlayout);
        
        /** ADD NEW TRADE FOR USER */
        
        vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true); 
        corporate_2 = dropDown.populateCorporateListAssignedForUser(new ComboBox(), userId);
        corporate_2.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporate_2.getValue() == null){                    
                } else {
                    corporate_id_2 = companyService.getCorporateId(corporate_2.getValue().toString());
                    trade_2 = dropDown.populateTradeComboBox(trade_2, corporate_id_2);
                }
            }
        });
        vlayout.addComponent(corporate_2);
        
        trade_2.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(trade_2.getValue() == null){                    
                } else {
                    trade_id_2 = companyService.getTradeId(trade_2.getValue().toString(), corporate_id_2);
                }
            }
        });
        trade_2.setWidth("100%");
        vlayout.addComponent(trade_2);
        
        Button addTradeButton = new Button("ADD TRADE to "+name.toUpperCase());
        addTradeButton.setWidth("100%");
        addTradeButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(corporate_2.getValue() == null){
                     getWindow().showNotification("Select a Corporate Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                if(trade_2.getValue() == null){
                    getWindow().showNotification("Select a Trade Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                boolean checkIfTradeAssignedForUserExist = administratorService.checkTradeAssignedToUserIfExist(trade_id_2, userId);
                if(checkIfTradeAssignedForUserExist){
                    getWindow().showNotification("Trade was already assigned to user!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                boolean result = administratorService.insertNewTradeForAccessToUser(userId, corporate_id_2, trade_id_2);
                if(result){
                    tradeByCorporateAssignedToUserTable(userId);
                }
            }
        });
        vlayout.addComponent(addTradeButton);
        
        tradeByCorporateAssignedToUserTable(userId);
        vlayout.addComponent(tradeByCorporateAssignedToUserTbl); 
        vlayout.setCaption("Trade");
        ts.addComponent(vlayout);
        
        /** ADD NEW BRANCH TO USER */
        
        vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);                
        corporate_3 = dropDown.populateCorporateListAssignedForUser(new ComboBox(), userId);
        corporate_3.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporate_3.getValue() == null){                    
                } else {
                    corporate_id_3 = companyService.getCorporateId(corporate_3.getValue().toString());
                    trade_3 = dropDown.populateTradeListAssignedForUser(trade_3, userId, corporate_id_3);
                }
            }
        });
        vlayout.addComponent(corporate_3);
        
        trade_3.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(trade_3.getValue() == null){                    
                } else {
                    trade_id_3 = companyService.getTradeId(trade_3.getValue().toString(), corporate_id_3);
                    branch_3 = dropDown.populateBranchComboBox(branch_3, trade_id_3, corporate_id_3);
                }
            }
        });
        trade_3.setWidth("100%");
        vlayout.addComponent(trade_3);
        
        branch_3.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branch_3.getValue() == null){
                } else {
                    branch_id_3 = companyService.getBranchId(trade_id_3, branch_3.getValue().toString());
                }
            }
        });
        branch_3.setWidth("100%");
        vlayout.addComponent(branch_3);
        
        Button addBranchButton = new Button("ADD BRANCH to "+name.toUpperCase()); 
        addBranchButton.setWidth("100%");
        addBranchButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(corporate_3.getValue() == null){
                     getWindow().showNotification("Select a Corporate Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                if(trade_3.getValue() == null){
                    getWindow().showNotification("Select a Trade Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                     return;
                }
                
                if(branch_3.getValue() == null){
                    getWindow().showNotification("Select a Branch Name!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean checkIfBranchAssignedToUserExist = administratorService.checkBranchAssignedToUserIfExist(branch_id_3, userId);
                if(checkIfBranchAssignedToUserExist){
                    getWindow().showNotification("Branch was already assigned to user!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = administratorService.insertNewBranchForAccessToUser(userId, trade_id_3, branch_id_3);
                if(result){
                    branchByTradeAssignedToUserTable(userId);
                }
            }
        });
        vlayout.addComponent(addBranchButton);
        
        branchByTradeAssignedToUserTable(userId);
        vlayout.addComponent(branchByTradeAssignedToUserTbl); 
        vlayout.setCaption("Branch");
        ts.addComponent(vlayout);
        
        ts.addListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                corporate_2 = populateCorporateListAssignedForUser(corporate_2, userId);
                corporate_3 = populateCorporateListAssignedForUser(corporate_3, userId);
                corporateAssignedToUserTable(userId);
                tradeByCorporateAssignedToUserTable(userId);
                branchByTradeAssignedToUserTable(userId);
            }
        });
        
        subWindow.addComponent(ts);
        return subWindow;
    }

    private Window removeAssignedCorporateToUser(final int userId, final int rowId, String name){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE CORPORATE", vlayout);
        subWindow.setWidth("300px");
        
        Button removeCorporateButton = new Button("REMOVE CORPORATE from "+name.toUpperCase()+"?");
        removeCorporateButton.setWidth("100%");
        removeCorporateButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = companyService.removeCorporateFromUser(rowId);
                if(result){
                    corporateAssignedToUserTable(userId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else {
                    getWindow().showNotification("Cannot remove Corporate!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        subWindow.addComponent(removeCorporateButton);
        
        return subWindow;
    }
    
    private Window removeAssignedTradeToUser(final int userId, final int rowId, String name){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE TRADE", vlayout);
        subWindow.setWidth("300px");
        
        Button removeTradeButton = new Button("REMOVE TRADE from "+name.toUpperCase()+"?");
        removeTradeButton.setWidth("100%");
        removeTradeButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = companyService.removeTradeFromUser(rowId);
                if(result){
                    tradeByCorporateAssignedToUserTable(userId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else {
                    getWindow().showNotification("Cannot remove Trade!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        subWindow.addComponent(removeTradeButton);
        
        return subWindow;
    }
    
    private Window removeAssignedBranchToUser(final int rowId, String name, final int userId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE BRANCH", vlayout);
        subWindow.setWidth("300px");
        
        Button removeBranchButton = new Button("REMOVE BRANCH from "+name.toUpperCase()+"?");
        removeBranchButton.setWidth("100%");
        removeBranchButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = companyService.removeBranchFromUser(rowId);
                if(result){
                    branchByTradeAssignedToUserTable(userId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else {
                    getWindow().showNotification("Cannot remove Branch!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
        });
        subWindow.addComponent(removeBranchButton);
        
        return subWindow;
    }
    
    public ComboBox populateCorporateListAssignedForUser(ComboBox corporation, int userId){
        corporation.removeAllItems();;
        corporation.setWidth("100%");
        corporation.setCaption("Corporation: ");        
        corporation.setNullSelectionAllowed(false);
        List<Company> companyList = companyService.getCorporateListAssignedForUser(userId);
        for(Company c : companyList){
            corporation.addItem(c.getCompanyName());
        }
        corporation.setImmediate(true);
        
        return corporation;
    }    
}
