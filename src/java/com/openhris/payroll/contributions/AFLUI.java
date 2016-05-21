/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.openhris.commons.HRISPopupDateField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.payroll.containers.AFLContainer;
import com.openhris.payroll.tables.AFLTableProperties;
import com.openhris.payroll.tables.TaxTableProperties;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jetdario
 */
public class AFLUI extends VerticalLayout {
    
    CompanyService cs = new CompanyServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    AFLTableProperties afl = new AFLTableProperties();    
    private int branchId;
    private int tradeId;
    private int corporateId;

    public AFLUI(int branchId) {
        this.branchId = branchId;
        
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(true, true, false, false));
        
        HorizontalLayout h = new HorizontalLayout();
        h.setWidth("100%");
        h.setMargin(true);
        h.setSpacing(true);
        
        final PopupDateField payrollDateField = new HRISPopupDateField("Payroll Month and Year");
        payrollDateField.setWidth("200px");
        h.addComponent(payrollDateField);
        
        Button generateBtn = new Button("GENERATE AFL");
        generateBtn.setWidth("200px");
        generateBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {    
                tradeId = cs.getTradeIdByBranchId(getBranchId());
                corporateId = cs.getCorporateIdByTradeId(tradeId);
                
                afl.setContainerDataSource(
                        new AFLContainer(
                                corporateId, 
                                util.parsingDate(util.convertDateFormat(payrollDateField.getValue().toString()))));
            }
        });
        h.addComponent(generateBtn);  
        h.setComponentAlignment(generateBtn, Alignment.BOTTOM_LEFT);
                
        Button exportTableToExcel = new Button("EXPORT TO EXCEL");
	exportTableToExcel.setWidth("200px");
        exportTableToExcel.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = -73954695086117200L;
            private ExcelExport excelExport;
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                excelExport = new ExcelExport(afl, "AFL Remitance");
		excelExport.excludeCollapsedColumns();
                excelExport.setReportTitle(cs.getCorporateById(corporateId).toUpperCase()+" AFL Remitances");
		excelExport.setExportFileName(
                        cs.getCorporateById(corporateId).replace(",", " ").toUpperCase()
                        +"-AFL-Remitance-"+util.convertDateFormat(payrollDateField.getValue().toString())+".xls");
		excelExport.export();
            }
            
        });
        h.addComponent(exportTableToExcel);
        h.setComponentAlignment(exportTableToExcel, Alignment.BOTTOM_LEFT);
        h.setExpandRatio(exportTableToExcel, 2);
        
        addComponent(h);
        addComponent(afl);
        setExpandRatio(afl, 2);
    }        

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

}
