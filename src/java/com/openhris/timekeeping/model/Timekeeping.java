/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping.model;

import com.openhris.payroll.model.Payroll;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class Timekeeping extends Payroll {
    
    private Date attendanceDate;
    private String policy;
    private String holiday;
    private boolean premium;
    private double lates;
    private double undertime;
    private double overtime;
    private double nightDifferential;
    private double lateDeduction;
    private double undertimeDeduction;
    private double overtimePaid;
    private double nightDifferentialPaid;
    private double legalHolidayPaid;
    private double specialHolidayPaid;
    private double workingDayOffPaid;
    private double nonWorkingHolidayPaid;

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public String getPolicy() {
        return policy;
    }

    public String getHoliday() {
        return holiday;
    }

    public boolean isPremium() {
        return premium;
    }

    public double getLates() {
        return lates;
    }

    public double getUndertime() {
        return undertime;
    }

    public double getOvertime() {
        return overtime;
    }

    public double getNightDifferential(){
        return nightDifferential;
    }
    
    public double getLateDeduction() {
        return lateDeduction;
    }

    public double getUndertimeDeduction() {
        return undertimeDeduction;
    }

    public double getOvertimePaid() {
        return overtimePaid;
    }

    public double getNightDifferentialPaid() {
        return nightDifferentialPaid;
    }

    public double getLegalHolidayPaid() {
        return legalHolidayPaid;
    }

    public double getSpecialHolidayPaid() {
        return specialHolidayPaid;
    }

    public double getWorkingDayOffPaid() {
        return workingDayOffPaid;
    }

    public double getNonWorkingHolidayPaid() {
        return nonWorkingHolidayPaid;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setLates(double lates) {
        this.lates = lates;
    }

    public void setUndertime(double undertime) {
        this.undertime = undertime;
    }

    public void setOvertime(double overtime) {
        this.overtime = overtime;
    }
    
    public void setNightDifferential(double nightDifferential){
        this.nightDifferential = nightDifferential;
    }

    public void setLateDeduction(double lateDeduction) {
        this.lateDeduction = lateDeduction;
    }

    public void setUndertimeDeduction(double undertimeDeduction) {
        this.undertimeDeduction = undertimeDeduction;
    }

    public void setOvertimePaid(double overtimePaid) {
        this.overtimePaid = overtimePaid;
    }

    public void setNightDifferentialPaid(double nightDifferentialPaid) {
        this.nightDifferentialPaid = nightDifferentialPaid;
    }

    public void setLegalHolidayPaid(double legalHolidayPaid) {
        this.legalHolidayPaid = legalHolidayPaid;
    }

    public void setSpecialHolidayPaid(double specialHolidayPaid) {
        this.specialHolidayPaid = specialHolidayPaid;
    }

    public void setWorkingDayOffPaid(double workingDayOffPaid) {
        this.workingDayOffPaid = workingDayOffPaid;
    }

    public void setNonWorkingHolidayPaid(double nonWorkingHolidayPaid) {
        this.nonWorkingHolidayPaid = nonWorkingHolidayPaid;
    }    
}
