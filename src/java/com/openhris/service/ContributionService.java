/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.model.AFLSchedule;
import com.openhris.model.HdmfSchedule;
import com.openhris.model.Phic;
import com.openhris.model.PhicSchedule;
import com.openhris.model.Sss;
import com.openhris.model.SssSchedule;
import com.openhris.model.Tax;
import com.openhris.model.TaxSchedule;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public interface ContributionService {
    
    public List<Sss> getSssContributionList();
    
    public List<Phic> getPhicContributionList();
    
    public List<Tax> getTaxContributionList();
    
    public boolean updateSssTableData(List<Sss> sssList);
    
    public boolean updatePhicTableData(List<Phic> phicList);
    
    public boolean updateTaxTableData(List<Tax> taxList);
    
    public boolean insertNewSssData(List<Sss> sssList);
    
    public boolean insertNewPhicData(List<Phic> phicList);
    
    public double getSssTableLastRowMaxSalary();
    
    public double getPhicTableLastRowMaxSalary();
    
//    public List<SssSchedule> getSssContribution(int branchId, int month, int year);
    
    public List<SssSchedule> getSssContribution(int branchId, Date payrollDate);
    
    public List<PhicSchedule> getPhicContribution(int branchId, Date payrollDate);
    
    public List<HdmfSchedule> getHdmfContribution(int branchId, Date payrollDate);
    
    public List<TaxSchedule> getTaxContribution(int corporateId, Date payrollDate); 
    
    public List<AFLSchedule> findAFLByCompany(int corporateId, Date payrollDate);
}
