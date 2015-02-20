/*
 * MainApp.java
 *
 * Created on July 9, 2012, 4:22 PM
 */
 
package com.hrms.main;           

import com.hrms.admin.classes.AdvanceUserAccessControl;
import com.hrms.admin.module.AdvanceUserAccessModule;
import com.hrms.admin.module.CreateNewUser;
import com.hrms.beans.LoginBean;
import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.AuthenticateLogin;
import com.hrms.modules.*;
import com.openhris.administrator.ChangePassword;
import com.openhris.administrator.UserAdvanceAccessMainUI;
import com.openhris.administrator.UserToolbarMenuAccessMainUI;
import com.openhris.administrator.UsersMainUI;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.administrator.service.AdministratorService;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.calendar.CalendarMainUI;
import com.openhris.commons.AboutHris;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.company.model.Branch;
import com.openhris.company.model.Company;
import com.openhris.company.model.Trade;
import com.openhris.company.service.CompanyService;
import com.openhris.company.serviceprovider.CompanyServiceImpl;
import com.openhris.contributions.ContributionComponentContainer;
import com.openhris.employee.EmployeeMainUI;
import com.openhris.employee.model.PositionHistory;
import com.openhris.employee.service.EmployeeService;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.PayrollMainUI;
import com.openhris.payroll.PayrollRegisterMainUI;
import com.openhris.timekeeping.TimekeepingMainUI;
import com.vaadin.Application;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
/** 
 *
 * @author jet
 * @version 
 */

public class MainApp extends Application {
        
    VerticalLayout main = new VerticalLayout();
    Window window = new Window("HRMS", main);
    
    boolean userAuthenticate = false;
    NativeButton mainButton;
    NativeButton aboutButton;
    NativeButton mainMenu;

    Button loginButton;
    Button logoutButton;
    Button settingsButton;
    CssLayout menu;
    CssLayout left;
    HorizontalSplitPanel hsplit;
    
    AdministratorService administratorService = new AdministratorServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    EmployeeService employeeService = new EmployeeServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    Tree tree = new Tree();
    List<Company> companyList;
    List<Trade> tradeList;
    List<Branch> branchList;
    String companyName;
    int companyId;
    int tradeId;
    int branchId = 0;;
    private int userId;
    
    CompanyModule companyModule;
    
    EmployeeMainUI employeeMainUI;
    PayrollRegisterMainUI payrollRegisterMainUI;
    TimekeepingMainUI timekeepingMainUI;
    PayrollMainUI payrollMainUI;
    UsersMainUI usersMainUI;
    UserToolbarMenuAccessMainUI userToolbarMenuAccessMainUI;
    UserAdvanceAccessMainUI userAdvanceAccessMainUI;    
    CalendarMainUI calendarMainUI;
    
    AdvanceUserAccessModule advanceUserAccess = new AdvanceUserAccessModule();
    CreateNewUser createNewUser;
    
    LoginBean login = new LoginBean();
    AuthenticateLogin authLog = new AuthenticateLogin();
    
    boolean mainMenuBar;
    boolean timekeepingMenuBar;
    boolean payrollMenuBar;
    boolean loansMenuBar;
    boolean eventsMenuBar;
    boolean contributionMenuBar;
    boolean administratorMenuBar;
    
    DropDownComponent dropDown = new DropDownComponent();
    
    @Override
    public void init() {
	
        GlobalVariables.initialize(this);
        UserAccessControl.initialize(this);
        AdvanceUserAccessControl.initialize(this);
        
        setTheme("myTheme");
        
        setMainWindow(window);
        main.setSizeFull();                    
        
        Window subWindow = loginWindow();
        subWindow.setWidth("160px");
        subWindow.setResizable(false);
        if (subWindow.getParent() != null){
            subWindow.getWindow().showNotification("Login Window is already open!");
        }else{
            window.getWindow().addWindow(subWindow);
        }
        
        initToolBarMenu(main, window);
    }
    
    private void initToolBarMenu(VerticalLayout vlayout, final Window window){
        CssLayout toolbar = new CssLayout();
        toolbar.setWidth("100%");
        toolbar.addStyleName("toolbar-invert");
        
        left = new CssLayout();
        left.setSizeUndefined();
        left.addStyleName("left");
        toolbar.addComponent(left);
        
        Label title = new Label("F&C Group of Companies");
        title.addStyleName("h1");
        left.addComponent(title);
                      
        aboutButton = new NativeButton("About HRMS");
        aboutButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = aboutHrms();
//                AboutHris aboutHris = new AboutHris();
//                Window subWindow = aboutHris.aboutHris();
                if(subWindow.getParent() == null){
                    window.getWindow().addWindow(subWindow);
                }
                subWindow.setModal(true);
                subWindow.center();
            }
            
        });
        left.addComponent(aboutButton);
                
        CssLayout right = new CssLayout();
        right.setSizeUndefined();
        right.addStyleName("right");
        toolbar.addComponent(right);       
                
        loginButton = new Button("Login");
        loginButton.addStyleName("borderless");
        loginButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = loginWindow();
                subWindow.setWidth("160px");
                subWindow.setResizable(false);
                if (subWindow.getParent() != null){
                    subWindow.getWindow().showNotification("Login Window is already open!");
                }else{
                    window.getWindow().addWindow(subWindow);
                }
            }
            
        });
        right.addComponent(loginButton);        
        
        logoutButton = new Button("Logout");
        logoutButton.addStyleName("borderless");
        logoutButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                authenticateLogin(false);
                close();
            }
            
        });
        right.addComponent(logoutButton);
        
        settingsButton = new Button("Settings");
        settingsButton.setStyleName("borderless");
        settingsButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final Window subWindow = new ChangePassword(getUserId());
                if(window.getParent() == null){
                    window.getWindow().addWindow(subWindow);
                }
                subWindow.setModal(true);
                subWindow.center();                
                
                subWindow.addListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        if(GlobalVariables.isLogoutAfterPasswordChange()){
                            authenticateLogin(false);
                            close();
                        }
                    }
                });
            }
        });
        right.addComponent(settingsButton);
        
        vlayout.addComponent(toolbar);
        
        hsplit = new HorizontalSplitPanel();        
        hsplit.addStyleName("small blue white");
        hsplit.setLocked(true);
        hsplit.setSplitPosition(23, Sizeable.UNITS_PERCENTAGE);
	hsplit.setSizeFull();
	
        vlayout.addComponent(hsplit);
        vlayout.setExpandRatio(hsplit, 1);         
        
        menu = new CssLayout();
        menu.addStyleName("menu");
        menu.setWidth("100%");
        
        authenticateLogin(userAuthenticate);
        setAccountVisibleFalse(); 
    }    
    
    private void enableToolBar(){
        Button.ClickListener change = new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                
                ComponentContainer p = (ComponentContainer) event.getButton().getParent();
                for(Iterator iterator = p.getComponentIterator(); iterator.hasNext();){
                    ((AbstractComponent) iterator.next()).removeStyleName("selected");
                }
                event.getButton().addStyleName("selected");
                hsplit.setSecondComponent((Component) event.getButton().getData());
                
                if(event.getButton().getCaption().equals("MAIN")){
                    mainMenuBar = true;
                } else if (event.getButton().getCaption().equals("TIMEKEEPING")) {
                    timekeepingMenuBar = true;
                } else if (event.getButton().getCaption().equals("PAYROLL")) {
                    payrollMenuBar = true;
                } else if (event.getButton().getCaption().equals("LOANS")) {
                    loansMenuBar = true;
                } else if (event.getButton().getCaption().equals("CALENDAR")) {
                    eventsMenuBar = true;
                } else if (event.getButton().getCaption().equals("ADMINISTRATOR")) {
                    contributionMenuBar = true;
                } else {
                    administratorMenuBar = true;
                }
                setBooleanToolbar();
            }
            
        };
        
        mainButton = new NativeButton("MAIN", change); 
        mainButton.setStyleName("selected");
        mainButton.setData(buildEmployeesComponent());
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(UserAccessControl.isMainMenu());
        }
        left.addComponent(mainButton);
        
        mainButton = new NativeButton("TIMEKEEPING", change);
        mainButton.setStyleName("seleclted");
        mainButton.setData(buildTimekeepingComponent());
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(UserAccessControl.isTimekeepingMenu());
        }
        left.addComponent(mainButton);
        
        mainButton = new NativeButton("PAYROLL", change);
        mainButton.setStyleName("selected");
        mainButton.setData(buildPayrollComponent());
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(UserAccessControl.isPayrollMenu());
        }
        left.addComponent(mainButton);
        
        mainButton = new NativeButton("LOANS");
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(UserAccessControl.isLoansMenu());
        }
        left.addComponent(mainButton);
        
        mainButton = new NativeButton("CALENDAR", change);
        mainButton.setStyleName("selected");
        mainButton.setData(buildCalendarComponent());
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(UserAccessControl.isEventsMenu());
        }
        left.addComponent(mainButton);
        
        mainButton = new NativeButton("CONTRIBUTIONS", change);
        mainButton.setStyleName("selected");
        mainButton.setData(new ContributionComponentContainer().contributionComponentContainer());
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(UserAccessControl.isContributionsMenu());
        }
        left.addComponent(mainButton);
        
        mainButton = new NativeButton("ADMINISTRATOR", change);
        mainButton.setStyleName("selected");
        mainButton.setData(buildAdministratorComponent());
        if(GlobalVariables.getUserRole().equals("administrator")){
            mainButton.setVisible(true);
        } else {
            mainButton.setVisible(false);
        }
        left.addComponent(mainButton);
    }
    
    private void initMainMenu(){ 
        menu = new CssLayout();
        menu.addStyleName("menu");
        menu.setWidth("100%");
                
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr") || 
                GlobalVariables.getUserRole().equals("audit")){
            companyList = companyService.getAllCorporation();
        } else {
            companyList = companyService.getCorporateListAssignedForUser(getUserId());
        }        
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        
        Label treeLabel = new Label("F&C Group of Companies");
        treeLabel.setStyleName("section");
        vlayout.addComponent(treeLabel);
        
        tree.setImmediate(true);
	tree.addStyleName("tree-layout");
        for(Company c : companyList){							
            tree.addItem(c.getCompanyName());
                
            if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr") || 
                GlobalVariables.getUserRole().equals("audit")){
                tradeList = companyService.getTradeByCorporateId(c.getCompanyId());
            } else {
                tradeList = companyService.getTradeListAssignedForUser(getUserId(), c.getCompanyId());
            }                
		                
            for(Trade t : tradeList){
                tree.addItem(t.getTradeName());
                tree.setParent(t.getTradeName(), c.getCompanyName());                
            }
	}
        
        tree.addListener(new Tree.ExpandListener() {

            @Override
            public void nodeExpand(Tree.ExpandEvent event) {                
                if(tree.getParent(event.getItemId()) != null){
                    companyId = companyService.getCorporateId(tree.getParent(event.getItemId()).toString());
                    tradeId = companyService.getTradeId(event.getItemId().toString(), companyId);
                        
                    if(GlobalVariables.getUserRole().equals("administrator") || 
                        GlobalVariables.getUserRole().equals("hr") || 
                        GlobalVariables.getUserRole().equals("audit")){
                        branchList = companyService.getBranchByTrade(tradeId, companyId);
                    } else {
                        branchList = companyService.getBranchListAssignedForUser(getUserId(), tradeId);
                    }
                    
                    branchList = companyService.getBranchByTrade(tradeId, companyId);
                    String tradeName = companyService.getTradeById(tradeId);
                    for(Branch b : branchList){       
                        if(util.checkForDuplicatedBranch(b.getBranchName())){
                            tree.addItem(b.getBranchName()+" - "+b.getBranchId());
                            tree.setParent(b.getBranchName()+" - "+b.getBranchId(), tradeName);
                            tree.setChildrenAllowed(b.getBranchName()+" - "+b.getBranchId(), false);
                        } else {
                            tree.addItem(b.getBranchName());
                            tree.setParent(b.getBranchName(), tradeName);
                            tree.setChildrenAllowed(b.getBranchName(), false);
                        }                        
                    }
                } 
            }
        });
                
        tree.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {    
                if(tree.getParent(event.getItemId()) == null){                    
                } else if(tree.getParent(tree.getParent(event.getItemId())) == null){   
                } else {
                    companyId = companyService.getCorporateId(String.valueOf(tree.getParent(tree.getParent(event.getItemId()))));
                    tradeId = companyService.getTradeId(String.valueOf(tree.getParent(event.getItemId())), companyId);
                                        
                    String[] split = event.getItemId().toString().split(" - ");
                    if(util.checkForDuplicatedBranch(split[0])){
                        branchId = companyService.getBranchId(tradeId, split[0]);                        
                    } else {
                        branchId = companyService.getBranchId(tradeId, event.getItemId().toString());
                    }                                 
                    
                    if(mainMenuBar){
                        employeeMainUI.employeesTable(getEmployeeList(branchId));
                    } 
                    
                    timekeepingMainUI.timekeepingTable(branchId, null);
                    timekeepingMainUI.employeeComboBox(branchId);
                    
                    payrollMainUI.employeeComboBox(branchId); 
                    payrollRegisterMainUI.setBranchId(branchId);
                    
                    usersMainUI.employeeComboBox(branchId);
                    usersMainUI.setBranchId(branchId);
                }
                
            }
        });        
        vlayout.addComponent(tree);
        
        menu.addComponent(vlayout);
        
        NativeButton otherMenu = new NativeButton("ADD NEW EMPLOYEE");
        otherMenu.setStyleName("selected");
        otherMenu.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = employeeMainUI.openNewEmployeeWindow(null);
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    window.getWindow().addWindow(subWindow);
                }
                subWindow.center();
            }
        });
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr")){
            otherMenu.setVisible(true);
        } else {
            otherMenu.setVisible(false);
        }
        menu.addComponent(otherMenu);
        
        otherMenu = new NativeButton("ADD NEW CORPORATION");
        otherMenu.setStyleName("selected");
        otherMenu.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = companyModule.corporateSubwindow();
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    window.getWindow().addWindow(subWindow);
                }
                subWindow.center();
            }
        });
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr")){
            otherMenu.setVisible(true);
        } else {
            otherMenu.setVisible(false);
        }
        menu.addComponent(otherMenu);
        
        otherMenu = new NativeButton("ADD NEW TRADE");
        otherMenu.setStyleName("selected");
        otherMenu.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = companyModule.tradeSubwindow();
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    window.getWindow().addWindow(subWindow);
                }
                subWindow.center();
            }
        });
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr")){
            otherMenu.setVisible(true);
        } else {
            otherMenu.setVisible(false);
        }
        menu.addComponent(otherMenu);
        
        otherMenu = new NativeButton("ADD NEW BRANCH");
        otherMenu.setStyleName("selected");  
        otherMenu.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = companyModule.branchSubwindow();
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    window.getWindow().addWindow(subWindow);
                }
                subWindow.center();
            }
        });
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr")){
            otherMenu.setVisible(true);
        } else {
            otherMenu.setVisible(false);
        }
        menu.addComponent(otherMenu);       
        
        otherMenu = new NativeButton("REFRESH / VIEW ALL EMPLOYEE");
        otherMenu.setStyleName("selected");   
        otherMenu.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employeeMainUI.employeesTable(getEmployeeList(0));
            }
        });
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr")){
            otherMenu.setVisible(true);
        } else {
            otherMenu.setVisible(false);
        }
        menu.addComponent(otherMenu);
        
        hsplit.setFirstComponent(menu);
        
    }
    
    ComponentContainer buildEmployeesComponent(){
        employeeMainUI = new EmployeeMainUI(GlobalVariables.getUserRole(), branchId);
        companyModule = new CompanyModule(GlobalVariables.getUserRole());
//        calendarModule = new CalendarModule(GlobalVariables.getUsername(), GlobalVariables.getUserRole());
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
                
        VerticalLayout mainMenuGrid = new VerticalLayout();
        mainMenuGrid.setCaption("Employees");
        mainMenuGrid.addComponent(employeeMainUI);
	mainMenuGrid.setSizeFull();
        ts.addComponent(mainMenuGrid);
        
        mainMenuGrid = new VerticalLayout();
        mainMenuGrid.setSizeFull();
        mainMenuGrid.setCaption("Companies");
        mainMenuGrid.addComponent(companyModule);
	mainMenuGrid.setSizeFull();
        ts.addComponent(mainMenuGrid);
        
        return ts;
    }
    
    ComponentContainer buildTimekeepingComponent(){
        timekeepingMainUI = new TimekeepingMainUI(branchId);
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        GridLayout payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Timekeeping Summary");
        payrollMenuGrid.addComponent(timekeepingMainUI);
        ts.addComponent(payrollMenuGrid);        
        
        return ts;
    }
    
    ComponentContainer buildPayrollComponent(){
        payrollMainUI = new PayrollMainUI(branchId);
        payrollRegisterMainUI = new PayrollRegisterMainUI(branchId);
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        GridLayout payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Payroll");
        payrollMenuGrid.addComponent(payrollMainUI);
        ts.addComponent(payrollMenuGrid);
        
        payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Payroll Register");
        payrollMenuGrid.addComponent(payrollRegisterMainUI);
        ts.addComponent(payrollMenuGrid);
        
        payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("201 Summary");
//        payrollMenuGrid.addComponent(form201);
        ts.addComponent(payrollMenuGrid);
        
        return ts;
    }
    
    ComponentContainer buildLoansComponent(){
//        loansModule = new LoansModule(GlobalVariables.getUserRole());
//        ledgerModule = new LedgerModule(GlobalVariables.getUserRole());
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        GridLayout payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Loans");
//        payrollMenuGrid.addComponent(loansModule);
        ts.addComponent(payrollMenuGrid);
        
        payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Ledger");
//        payrollMenuGrid.addComponent(ledgerModule);
        ts.addComponent(payrollMenuGrid);
                
        return ts;
    }
        
    ComponentContainer buildCalendarComponent(){
        calendarMainUI = new CalendarMainUI();
        InlineDateField sample = new InlineDateField();
        sample.setValue(new Date());
        sample.setImmediate(true);
        
        TabSheet ts = new TabSheet();
//        ts.setSizeFull();
        ts.addStyleName("bar");
        
        VerticalLayout calendarMenuGrid = new VerticalLayout();
        calendarMenuGrid.setSizeFull();
        calendarMenuGrid.setCaption("Calendar");
        calendarMenuGrid.addComponent(calendarMainUI);      
        ts.addComponent(calendarMenuGrid);
                
        return ts;
    }
    
    ComponentContainer buildAdministratorComponent(){
        usersMainUI = new UsersMainUI(branchId);
        userToolbarMenuAccessMainUI = new UserToolbarMenuAccessMainUI();
        userAdvanceAccessMainUI = new UserAdvanceAccessMainUI();
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        VerticalLayout adminMenuGrid = new VerticalLayout();
        adminMenuGrid.setSizeFull();
        adminMenuGrid.setCaption("Create New User");
        adminMenuGrid.addComponent(usersMainUI);
        ts.addComponent(adminMenuGrid);
        
        adminMenuGrid = new VerticalLayout();
        adminMenuGrid.setSizeFull();
        adminMenuGrid.setCaption("User Toolbar Access Control");
        adminMenuGrid.addComponent(userToolbarMenuAccessMainUI);
        ts.addComponent(adminMenuGrid);
        
        adminMenuGrid = new VerticalLayout();
        adminMenuGrid.setSizeFull();
        adminMenuGrid.setCaption("User Advance Access Control");
        adminMenuGrid.addComponent(userAdvanceAccessMainUI);
        ts.addComponent(adminMenuGrid);
        
        ts.addListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                userToolbarMenuAccessMainUI.userToolbarMenuAccessTable();
                userAdvanceAccessMainUI.userAdvanceAccessTable();
            }
        });
        
        return ts;
    }
    
    private Window loginWindow(){
        final Window subWindow = new Window("Login");
        subWindow.setWidth("160px");
        subWindow.setModal(true);
        
        VerticalLayout layout = (VerticalLayout) subWindow.getContent();
        layout.setSpacing(true);
        layout.setMargin(true);
        
        final TextField username = new TextField();
        username.setValue("username");
        username.setWidth("100%");
        subWindow.addComponent(username);
        
        final PasswordField password = new PasswordField();
        password.setValue("password");
        password.setWidth("100%");
        subWindow.addComponent(password);
                
        final Button loginButtonSubWindow = new Button();
        loginButtonSubWindow.setCaption("Login");
        loginButtonSubWindow.setWidth("100%");
        loginButtonSubWindow.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean result = authLog.login(username.getValue().toString(), 
                        password.getValue().toString());
                if(result == true){
                    userAuthenticate = true;
                    authenticateLogin(userAuthenticate);
                    setAccountVisibleTrue();
                    loginButtonSubWindow.setVisible(false); 
                    enableToolBar();
                    initMainMenu();
                    (subWindow.getParent()).removeWindow(subWindow);
                    userId = administratorService.getUserId(username.getValue().toString().toLowerCase().trim());
                    hsplit.setSecondComponent(buildCalendarComponent());
                }else{
                    (subWindow.getParent()).showNotification("Incorrect username/password!");
                    return;
                } 
            }
            
        });        
        subWindow.addComponent(loginButtonSubWindow);
        
        return subWindow;
    }
    
    private Window aboutHrms(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("About HRMS", vlayout);
        subWindow.setWidth("300px");
                
        Label version = new Label("Version: <b>1.0</b>");
        version.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(version);
        
        Label title = new Label("Title: <b>Human Resource Information System</b>");
        title.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(title);
        
        Label developer = new Label("Developed By: <b>Engr. Godfrey D. Beray</b>");
        developer.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(developer);
        
        Label framework = new Label("Framework: <b>VAADIN - Sept2012</b>");
        framework.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(framework);
        
        return subWindow;
    }
    
    private void authenticateLogin(boolean result){
        if(userAuthenticate == false){
            aboutButton.setEnabled(false);
            hsplit.setEnabled(false);
        }else{
            aboutButton.setEnabled(true);
            hsplit.setEnabled(true);
        }
    }
    
    private void setAccountVisibleTrue(){
        loginButton.setVisible(false);
        logoutButton.setVisible(true);
        settingsButton.setVisible(true);
    }
    
    private void setAccountVisibleFalse(){
        loginButton.setVisible(true);
        logoutButton.setVisible(false);
        settingsButton.setVisible(false);
    }
    
    private void setBooleanToolbar(){
        if(mainMenuBar == true){            
            timekeepingMenuBar = false;
            payrollMenuBar = false;
            loansMenuBar = false;
            eventsMenuBar = false;
            contributionMenuBar = false;
            administratorMenuBar = false;
            initMainMenu();
        } else if(timekeepingMenuBar == true) {
            mainMenuBar = false;
            payrollMenuBar = false;
            loansMenuBar = false;
            eventsMenuBar = false;
            contributionMenuBar = false;
            administratorMenuBar = false;
            initMainMenu();
        } else if (payrollMenuBar = true) {
            timekeepingMenuBar = false;
            mainMenuBar = false;
            loansMenuBar = false;
            eventsMenuBar = false;
            contributionMenuBar = false;
            initMainMenu();
        } else if (loansMenuBar == true) {
            timekeepingMenuBar = false;
            payrollMenuBar = false;
            mainMenuBar = false;
            eventsMenuBar = false;
            contributionMenuBar = false;
            administratorMenuBar = false;
            initMainMenu();
        } else if (eventsMenuBar == true) {
            timekeepingMenuBar = false;
            payrollMenuBar = false;
            loansMenuBar = false;
            mainMenuBar = false;
            contributionMenuBar = false;
            administratorMenuBar = false;
            initMainMenu();
        } else if (contributionMenuBar == true ){
            timekeepingMenuBar = false;
            payrollMenuBar = false;
            loansMenuBar = false;
            eventsMenuBar = false;
            mainMenuBar = false;
            initMainMenu();
        } else {
            timekeepingMenuBar = false;
            payrollMenuBar = false;
            loansMenuBar = false;
            eventsMenuBar = false;
            mainMenuBar = false;
            contributionMenuBar = false;
            initMainMenu();
        }
    }
    
    public List<PositionHistory> getEmployeeList(int branchId){
        return employeeService.getEmployeePerBranch(branchId);
    }
    
    public int getUserId(){
        return userId;
    }
}
