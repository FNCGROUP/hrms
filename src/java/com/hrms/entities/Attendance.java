/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.entities;

import com.hrms.utilities.ConvertionUtilities;

/**
 *
 * @author jet
 */
public class Attendance {
    
    private Integer id;
    public Integer salaryId;
    public String attendanceDate;
    public String policy;
    public String holiday;
    public Boolean latesPremium;
    public Double lates;
    public Boolean undertimePremium;
    public Double undertime;
    public Boolean overtimePremium;
    public Double overtime;
    public Double nightDifferential;
    public Double latesDeduction;
    public Double undertimeDeduction;
    public Double overtimePaid;
    public Double nightDifferentialPaid;
    public Double legalHolidayPaid;
    public Double specialHolidayPaid;
    public Double workingDayOffPaid;
    public Double psHolidayPaid; //Policy: Holiday, Holiday: Legal (DOUBLE PAY)
        
    public Attendance(){        
    }
    
    public Integer getId(){ return id; }
    public void setId(Integer id){ this.id = id; }
    
    public Integer getSalaryId(){ return salaryId; }
    public void setSalaryId(Integer salaryId){ this.salaryId = salaryId; }
    
    public String getAttendanceDate(){ return attendanceDate; }
    public void setAttendanceDate(String attendanceDate){ this.attendanceDate = attendanceDate; } 
    
    public String getPolicy(){ return policy; }
    public void setPolicy(String policy){ this.policy = policy; }
    
    public String getHoliday(){ return holiday; }
    public void setHoliday(String holiday){ this.holiday = holiday; }
    
    public Boolean getLatesPremium(){ return latesPremium; }
    public void setLatesPremium(Boolean latesPremium){ this.latesPremium = latesPremium; }
    
    public Double getLates(){ return lates; }
    public void setLates(Double lates){ this.lates = lates; }
    
    public Boolean getUndertimePremium(){ return undertimePremium; }
    public void setUndertimePremium(Boolean undertimePremium){ this.undertimePremium = undertimePremium; }
    
    public Double getUndertime(){ return undertime; }
    public void setUndertime(Double undertime){ this.undertime = undertime; }
    
    public Boolean getOvertimePremium(){ return overtimePremium; }
    public void setOvertimePremium(Boolean overtimePremium){ this.overtimePremium = overtimePremium; }
    
    public Double getOvertime(){ return overtime; }
    public void setOvertime(Double overtime){ this.overtime = overtime; }
    
    public Double getNightDifferential(){ return nightDifferential; }
    public void setNightDifferential(Double nightDifferential){ this.nightDifferential = nightDifferential; }
    
    public Double getLatesDeduction(){ return latesDeduction; }
    public void setLatesDeduction(Double latesDeduction){ this.latesDeduction = latesDeduction; }
    
    public Double getUndertimeDeduction(){ return undertimeDeduction; }
    public void setUndertimeDeduction(Double undertimeDeduction){ this.undertimeDeduction = undertimeDeduction; }
    
    public Double getOvertimePaid(){ return overtimePaid; }
    public void setOvertimePaid(Double overtimePaid){ this.overtimePaid = overtimePaid; }
    
    public Double getNightDifferentialPaid(){ return nightDifferentialPaid; }
    public void setNightDifferentialPaid(Double nightDifferentialPaid){ this.nightDifferentialPaid = nightDifferentialPaid; }
    
    public Double getlegalHolidayPaid(){ return legalHolidayPaid; }
    public void setLegalHolidayPaid(Double legalHolidayPaid){ this.legalHolidayPaid = legalHolidayPaid; }
    
    public Double getSpecialHolidayPaid(){ return specialHolidayPaid; }
    public void setSpecialHolidayPaid(Double specialHolidayPaid){ this.specialHolidayPaid = specialHolidayPaid; }
    
    public Double getWorkingDayOffPaid(){ return workingDayOffPaid; }
    public void setWorkingDayOffPaid(Double workingDayOffPaid){ this.workingDayOffPaid = workingDayOffPaid; }
    
    public Double getPsHolidayPaid(){ return psHolidayPaid; }
    public void setPsHolidayPaid(Double psHolidayPaid){ this.psHolidayPaid = psHolidayPaid; }
}
