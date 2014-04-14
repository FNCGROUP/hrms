/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.admin.classes;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 *
 * @author jet
 */
public class AdvanceUserAccessControl implements TransactionListener {
    
    private static final long serialVersionUID = 1L;
    
    private static ThreadLocal<AdvanceUserAccessControl> instance = new ThreadLocal<AdvanceUserAccessControl>();
    
    private Application application;
    
    private Boolean mainMenu;
    private Boolean payrollMenu;
    private Boolean contributionsMenu;
    private Boolean calendarMenu;
    private Boolean loansMenu;
    
    public AdvanceUserAccessControl(Application application){
        this.application = application;
        // Set a value for the ThreadLocal to avoid any NPEs
        instance.set(this);
    }
    
    public static void setMainMenu(boolean mainMenu){
        instance.get().mainMenu = mainMenu;
    }
    
    public static Boolean getMainMenu(){
        return instance.get().mainMenu;
    }
    
    public static void setPayrollMenu(boolean payrollMenu){
        instance.get().payrollMenu = payrollMenu;
    }
    
    public static Boolean getPayrollMenu(){
        return instance.get().payrollMenu;
    }
    
    public static void setContributionsMenu(boolean contributionsMenu){
        instance.get().contributionsMenu = contributionsMenu;
    }
    
    public static Boolean getContributionsMenu(){
        return instance.get().contributionsMenu;
    }
    
    public static void setCalendarMenu(boolean calendarMenu){
        instance.get().calendarMenu = calendarMenu;
    }
    
    public static Boolean getCalendarMenu(){
        return instance.get().calendarMenu;
    }
    
    public static void setLoansMenu(boolean loansMenu){
        instance.get().loansMenu = loansMenu;
    }
    
    public static Boolean getLoansMenu(){
        return instance.get().loansMenu;
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
        AdvanceUserAccessControl appSettings = new AdvanceUserAccessControl(application);
        application.getContext().addTransactionListener(appSettings);
    }    
}
