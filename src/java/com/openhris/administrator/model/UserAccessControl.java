/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator.model;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 *
 * @author jet
 */
public class UserAccessControl implements TransactionListener {
    
    private static final long serialVersionUID = 1L;
    
    private static ThreadLocal<UserAccessControl> instance = new ThreadLocal<UserAccessControl>();
    
    private Application application;
    
    private boolean timekeeping;
    private boolean contributions;
    private boolean cashBond;
    private boolean advances;
    private boolean adjustment;
    private boolean payroll;
    private boolean editEmployeesInfo;
    private boolean addEvents;
    private boolean adjustPayroll;
    private boolean mainMenu;
    private boolean timekeepingMenu;
    private boolean payrollMenu;
    private boolean loansMenu;
    private boolean contributionsMenu;
    private boolean eventsMenu;
    public String username;
        
    public UserAccessControl(Application application){
        this.application = application;
        // Set a value for the ThreadLocal to avoid any NPEs
        instance.set(this);
    }

    public Application getApplication() {
        return instance.get().application;
    }

    public static boolean isTimekeeping() {
        return instance.get().timekeeping;
    }

    public static boolean isContributions() {
        return instance.get().contributions;
    }

    public static boolean isCashBond() {
        return instance.get().cashBond;
    }

    public static boolean isAdvances() {
        return instance.get().advances;
    }

    public static boolean isAdjustment() {
        return instance.get().adjustment;
    }

    public static boolean isPayroll() {
        return instance.get().payroll;
    }

    public static boolean isEditEmployeesInfo() {
        return instance.get().editEmployeesInfo;
    }

    public static boolean isAddEvents() {
        return instance.get().addEvents;
    }

    public static boolean isAdjustPayroll() {
        return instance.get().adjustPayroll;
    }

    public static boolean isMainMenu() {
        return instance.get().mainMenu;
    }

    public static boolean isTimekeepingMenu() {
        return instance.get().timekeepingMenu;
    }

    public static boolean isPayrollMenu() {
        return instance.get().payrollMenu;
    }

    public static boolean isLoansMenu() {
        return instance.get().loansMenu;
    }

    public static boolean isContributionsMenu() {
        return instance.get().contributionsMenu;
    }

    public static boolean isEventsMenu() {
        return instance.get().eventsMenu;
    }

    public static void setTimekeeping(boolean timekeeping) {
        instance.get().timekeeping = timekeeping;
    }

    public static void setContributions(boolean contributions) {
        instance.get().contributions = contributions;
    }

    public static void setCashBond(boolean cashBond) {
        instance.get().cashBond = cashBond;
    }

    public static void setAdvances(boolean advances) {
        instance.get().advances = advances;
    }

    public static void setAdjustment(boolean adjustment) {
        instance.get().adjustment = adjustment;
    }

    public static void setPayroll(boolean payroll) {
        instance.get().payroll = payroll;
    }

    public static void setEditEmployeesInfo(boolean editEmployeesInfo) {
        instance.get().editEmployeesInfo = editEmployeesInfo;
    }

    public static void setAddEvents(boolean addEvents) {
        instance.get().addEvents = addEvents;
    }

    public static void setAdjustPayroll(boolean adjustPayroll) {
        instance.get().adjustPayroll = adjustPayroll;
    }

    public static void setMainMenu(boolean mainMenu) {
        instance.get().mainMenu = mainMenu;
    }

    public static void setTimekeepingMenu(boolean timekeepingMenu) {
        instance.get().timekeepingMenu = timekeepingMenu;
    }

    public static void setPayrollMenu(boolean payrollMenu) {
        instance.get().payrollMenu = payrollMenu;
    }

    public static void setLoansMenu(boolean loansMenu) {
        instance.get().loansMenu = loansMenu;
    }

    public static void setContributionsMenu(boolean contributionsMenu) {
        instance.get().contributionsMenu = contributionsMenu;
    }

    public static void setEventsMenu(boolean eventsMenu) {
        instance.get().eventsMenu = eventsMenu;
    }    
    
    private boolean convertStringToBoolean(String string){
        return Boolean.parseBoolean(string);
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        // Set the thread local instance
        if (this.application == application) {
            instance.set(this);
        }
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        // Clear thread local instance at the end of the transaction
        if (this.application == application) {
            instance.set(null);
        }
    }
    
    public static void initialize(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application may not be null");
        }
        UserAccessControl appSettings = new UserAccessControl(application);
        application.getContext().addTransactionListener(appSettings);
    }
}
