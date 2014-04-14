/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.contributions.model.Phic;
import com.openhris.contributions.model.Sss;
import com.openhris.contributions.model.Tax;
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
}
