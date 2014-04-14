/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 *
 * @author jet
 */
public class GlobalVariables implements TransactionListener{
    
    private static final long serialVersionUID = 1L;
    
    private String employmentWageEntry;
    private Double employmentWage;
    private String username;
    private String userRole;
    
    private static ThreadLocal<GlobalVariables> instance = new ThreadLocal<GlobalVariables>();
    
    private Application application;
    
    public GlobalVariables(Application application) {
        this.application = application;
        // Set a value for the ThreadLocal to avoid any NPEs
        instance.set(this);
    }
    
    public static void setUsername(String username){
        instance.get().username = username;
    }
    
    public static String getUsername(){        
        return instance.get().username;
    }
    
    public static void setUserRole(String userRole){
        instance.get().userRole = userRole;
    }
    
    public static String getUserRole(){
        return instance.get().userRole;
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
        GlobalVariables appSettings = new GlobalVariables(application);
        application.getContext().addTransactionListener(appSettings);
    }
    
}
