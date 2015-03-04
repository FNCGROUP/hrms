/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISPopupDateField;
import com.openhris.commons.HRISTextField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.company.serviceprovider.CompanyServiceImpl;
import com.openhris.employee.model.PositionHistory;
import com.openhris.employee.service.PositionHistoryService;
import com.openhris.employee.serviceprovider.PositionHistoryServiceImpl;
import com.openhris.company.service.CompanyService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class EmployeePositionHistory extends VerticalLayout{
        
    PositionHistoryService phService = new PositionHistoryServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    DropDownComponent dropDown = new DropDownComponent();
    
    String employeeId;
    int corporateId;
    int tradeId;
    int branchId;
    GridLayout glayout;
    Table positionHistoryTbl = new PositionHistoryTable();
    
    public EmployeePositionHistory(){        
    }
    
    public EmployeePositionHistory(String employeeId){
        this.employeeId = employeeId;
        
        init();
        addComponent(layout());
        setComponentAlignment(glayout, Alignment.TOP_CENTER);
        addComponent(positionHistoryTable());
        addComponent(layout2());
    }
    
    public void init(){
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
    }
    
    public ComponentContainer layout(){
	glayout = new GridLayout(3, 3);
        glayout.setSpacing(true);          
        glayout.setWidth("100%");
	glayout.setHeight("100%");
        
        final ComboBox corporate = dropDown.populateCorporateComboBox(new ComboBox());
        corporate.setWidth("250px");
        glayout.addComponent(corporate, 0, 0);
        
        final ComboBox trade = new ComboBox("Trade: ");
        trade.setWidth("200px");
        corporate.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){                    
                } else {
                    corporateId = companyService.getCorporateId(corporate.getValue().toString());
                    dropDown.populateTradeComboBox(trade, corporateId);
                }
                
            }
        });
        glayout.addComponent(trade, 1, 0);
        
        final ComboBox branch = new ComboBox("Branch: ");
        branch.setWidth("180px");
        trade.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){                    
                } else {
                    tradeId = companyService.getTradeId(trade.getValue().toString(), corporateId);
                    dropDown.populateBranchComboBox(branch, tradeId, corporateId);
                }
                
            }
        });
        branch.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){                    
                } else {
                    branchId = companyService.getBranchId(tradeId, branch.getValue().toString());
                }                
            }
        });
        glayout.addComponent(branch, 2, 0);
        
        final TextField department = new HRISTextField("Department: ");
        glayout.addComponent(department, 0, 1);
        
        final TextField position = new HRISTextField("Position: ");
        glayout.addComponent(position, 1, 1);
        
        final PopupDateField entryDate = new HRISPopupDateField("Entry Date:");
        glayout.addComponent(entryDate, 2, 1);
        
        Button updateBtn = new Button("UPDATE EMPLOYEE's POSITION");
        updateBtn.setWidth("100%");
        updateBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(corporate.getValue() == null){
                    getWindow().showNotification("Select a Company!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(trade.getValue() == null){
                    getWindow().showNotification("Select a Trade!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(branch.getValue() == null){
                    getWindow().showNotification("Select a Branch!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(department.getValue() == null || department.getValue().toString().isEmpty()){
                    getWindow().showNotification("Add a Department!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(position.getValue() == null || position.getValue().toString().isEmpty()){
                    getWindow().showNotification("Add a Position!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(entryDate.getValue() == null || entryDate.getValue().toString().isEmpty()){
                    getWindow().showNotification("Add an Entry Date!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                PositionHistory positionHistory = new PositionHistory();
                positionHistory.setPosition(position.getValue().toString().toLowerCase());
                positionHistory.setCompany(corporate.getValue().toString());
                positionHistory.setTrade(trade.getValue().toString());
                positionHistory.setBranch(branch.getValue().toString());
                positionHistory.setDepartment(department.getValue().toString().toLowerCase());
                positionHistory.setEntryDate((Date) entryDate.getValue());
                positionHistory.setBranchId(branchId);
                
                boolean result = phService.updatePositionHistory(employeeId, positionHistory);
                if(result){
                    positionHistoryTable();
                } else {
                    getWindow().showNotification("Error on Position History SQL", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        glayout.addComponent(updateBtn, 1, 2, 2, 2);
        
        return glayout;
    }
    
    public Table positionHistoryTable(){   
        positionHistoryTbl.removeAllItems();        
        List<PositionHistory> positionList = phService.getPositionHistory(getEmployeeId());
        int i = 0;
        for(PositionHistory p: positionList){
            positionHistoryTbl.addItem(new Object[]{
                p.getPositionId(), 
                p.getPosition().toLowerCase(), 
                p.getCompany(), 
                p.getTrade(), 
                p.getBranch(), 
                p.getDepartment(), 
                util.convertDateFormat(p.getEntryDate().toString())
            }, i);
            i++;
        }
        positionHistoryTbl.setPageLength(7);
        
        for(Object listener : positionHistoryTbl.getListeners(ItemClickEvent.class)){
            positionHistoryTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        positionHistoryTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = positionHistoryTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    int positionId = util.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    Window window = new RemovePositionWindow(positionId);
                    if(window.getParent() == null){
                        getWindow().addWindow(window);
                    }
                    window.center();
                    window.addListener(subWindowCloseListener);
                }                
            }
        });
        
        return positionHistoryTbl;
    }
        
    public ComponentContainer layout2(){
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSpacing(true);          
        hlayout.setWidth("100%");
                              
        GridLayout glayout2 = new GridLayout(2, 1);
        glayout2.setSpacing(true);
        
        final PopupDateField endDate = new HRISPopupDateField("Exit Date: ");
        endDate.setWidth("150px");
        glayout2.addComponent(endDate, 0, 0);
        glayout2.setComponentAlignment(endDate, Alignment.BOTTOM_LEFT);
        
        Button endDateBtn = new Button("RESIGN");
        endDateBtn.setWidth("150px");
        endDateBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(endDate.getValue() == null || endDate.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter End Date.", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Window window = new ConfirmResignWindow(getEmployeeId(), util.convertDateFormat(endDate.getValue().toString().trim().toLowerCase()));
                if(window.getParent() == null){
                    getWindow().addWindow(window);
                }
                window.setModal(true);
                window.center();
            }
        });
        glayout2.addComponent(endDateBtn, 1, 0);
        glayout2.setComponentAlignment(endDateBtn, Alignment.BOTTOM_LEFT);
                                
        hlayout.addComponent(glayout2);
        hlayout.setComponentAlignment(glayout2, Alignment.MIDDLE_RIGHT);
        
        return hlayout;
    }
    
    private String getEmployeeId(){
        return employeeId;
    }
        
    Window.CloseListener subWindowCloseListener = new Window.CloseListener() {

        @Override
        public void windowClose(Window.CloseEvent e) {
            positionHistoryTable();
        }
    };
}
