/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.openhris.model.Phic;
import com.openhris.model.Sss;
import com.openhris.model.Tax;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.service.ContributionService;
import java.util.List;

/**
 *
 * @author jet
 */
public class ContributionServiceImpl implements ContributionService {

    ServiceGetDAO serviceGet = new ServiceGetDAO();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    
    @Override
    public List<Sss> getSssContributionList() {
        return serviceGet.getSssContributionList();
    }

    @Override
    public List<Phic> getPhicContributionList() {
        return serviceGet.getPhicContributionList();
    }

    @Override
    public List<Tax> getTaxContributionList() {
        return serviceGet.getTaxContributionList();
    }

    @Override
    public boolean updateSssTableData(List<Sss> sssList) {
        return serviceUpdate.updateSssTableData(sssList);
    }

    @Override
    public boolean updatePhicTableData(List<Phic> phicList) {
        return serviceUpdate.updatePhicTableData(phicList);
    }

    @Override
    public boolean updateTaxTableData(List<Tax> taxList) {
        return serviceUpdate.updateTaxTableData(taxList);
    }

    @Override
    public boolean insertNewSssData(List<Sss> sssList) {
        return serviceInsert.insertNewSssData(sssList);
    }

    @Override
    public boolean insertNewPhicData(List<Phic> phicList) {
        return serviceInsert.insertNewPhicData(phicList);
    }

    @Override
    public double getSssTableLastRowMaxSalary() {
        return serviceGet.getSssTableLastRowMaxSalary();
    }

    @Override
    public double getPhicTableLastRowMaxSalary() {
        return serviceGet.getPhicTableLastRowMaxSalary();
    }
    
}
