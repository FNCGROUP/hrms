/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator.model;

/**
 *
 * @author jet
 */
public class UserToolbarMenuAccess extends User {
       
    private boolean mainMenu;
    private boolean timekeepingMenu;
    private boolean payrollMenu;
    private boolean loansMenu;
    private boolean contributionsMenu;
    private boolean eventsMenu;

    public boolean isMainMenu() {
        return mainMenu;
    }

    public boolean isTimekeepingMenu() {
        return timekeepingMenu;
    }

    public boolean isPayrollMenu() {
        return payrollMenu;
    }

    public boolean isLoansMenu() {
        return loansMenu;
    }

    public boolean isContributionsMenu() {
        return contributionsMenu;
    }

    public boolean isEventsMenu() {
        return eventsMenu;
    }

    public void setMainMenu(boolean mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void setTimekeepingMenu(boolean timekeepingMenu) {
        this.timekeepingMenu = timekeepingMenu;
    }

    public void setPayrollMenu(boolean payrollMenu) {
        this.payrollMenu = payrollMenu;
    }

    public void setLoansMenu(boolean loansMenu) {
        this.loansMenu = loansMenu;
    }

    public void setContributionsMenu(boolean contributionsMenu) {
        this.contributionsMenu = contributionsMenu;
    }

    public void setEventsMenu(boolean eventsMenu) {
        this.eventsMenu = eventsMenu;
    }
    
}
