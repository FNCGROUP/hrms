/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator.model;

/**
 *
 * @author jet
 */
public class UserAdvanceAccess extends User {
    
    private boolean timekeeping;
    private boolean contributions;
    private boolean cashBond;
    private boolean advances;
    private boolean adjustment;
    private boolean payroll;
    private boolean editEmployeesInfo;
    private boolean addEvents;
    private boolean adjustPayroll;
    private boolean lockPayroll;
    private boolean editAttendance;
    private boolean editSss;
    private boolean editPhic;
    private boolean editHdmf;
    private boolean unlockPayroll;

    public boolean isTimekeeping() {
        return timekeeping;
    }

    public boolean isContributions() {
        return contributions;
    }

    public boolean isCashBond() {
        return cashBond;
    }

    public boolean isAdvances() {
        return advances;
    }

    public boolean isAdjustment() {
        return adjustment;
    }

    public boolean isPayroll() {
        return payroll;
    }

    public boolean isEditEmployeesInfo() {
        return editEmployeesInfo;
    }

    public boolean isAddEvents() {
        return addEvents;
    }

    public boolean isAdjustPayroll() {
        return adjustPayroll;
    }

    public boolean isLockPayroll(){
        return lockPayroll;
    }

    public boolean isEditAttendance() {
        return editAttendance;
    }

    public boolean isEditSss() {
        return editSss;
    }

    public boolean isEditPhic() {
        return editPhic;
    }

    public boolean isEditHdmf() {
        return editHdmf;
    }
    
    public boolean isUnlockedPayroll(){
        return unlockPayroll;
    }
    
    public void setTimekeeping(boolean timekeeping) {
        this.timekeeping = timekeeping;
    }

    public void setContributions(boolean contributions) {
        this.contributions = contributions;
    }

    public void setCashBond(boolean cashBond) {
        this.cashBond = cashBond;
    }

    public void setAdvances(boolean advances) {
        this.advances = advances;
    }

    public void setAdjustment(boolean adjustment) {
        this.adjustment = adjustment;
    }

    public void setPayroll(boolean payroll) {
        this.payroll = payroll;
    }

    public void setEditEmployeesInfo(boolean editEmployeesInfo) {
        this.editEmployeesInfo = editEmployeesInfo;
    }

    public void setAddEvents(boolean addEvents) {
        this.addEvents = addEvents;
    }

    public void setAdjustPayroll(boolean adjustPayroll) {
        this.adjustPayroll = adjustPayroll;
    }
    
    public void setLockPayroll(boolean lockPayroll){
        this.lockPayroll = lockPayroll;
    }

    public void setEditAttendance(boolean editAttendance) {
        this.editAttendance = editAttendance;
    }

    public void setEditSss(boolean editSss) {
        this.editSss = editSss;
    }

    public void setEditPhic(boolean editPhic) {
        this.editPhic = editPhic;
    }

    public void setEditHdmf(boolean editHdmf) {
        this.editHdmf = editHdmf;
    }
    
    public void setUnlockPayroll(boolean unlockPayroll){
        this.unlockPayroll = unlockPayroll;
    }
}
