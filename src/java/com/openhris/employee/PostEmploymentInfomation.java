/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISPopupDateField;
import com.openhris.commons.HRISTextField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.PostEmploymentInformationService;
import com.openhris.serviceprovider.PostEmploymentInformationServiceImpl;
import com.openhris.service.CompanyService;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
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
public class PostEmploymentInfomation extends VerticalLayout{
        
    PostEmploymentInformationService positionHistoryService = new PostEmploymentInformationServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    DropDownComponent dropDown = new DropDownComponent();
    EmployeeService employeeService = new EmployeeServiceImpl();
    
    ComboBox corporate;
    ComboBox trade;
    ComboBox branch;
    TextField position;
    TextField department;
    PopupDateField entryDate;
    CheckBox newPositionBtn = new CheckBox("Check to add new Position or to Transfer");
    Button updateBtn;
    boolean isEdit = false;
    
    String employeeId;
    int corporateId;
    int tradeId;
    int branchId;
    int positionId;
    GridLayout glayout;
    Table positionHistoryTbl = new PostEmploymentInformationTable();
    
    private static String BTN_CAPTION_1 = "UPDATE";
    private static String BTN_CAPTION_2 = "EDIT";
    
    public PostEmploymentInfomation(){        
    }
    
    public PostEmploymentInfomation(String employeeId){
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
        
        corporate = dropDown.populateCorporateComboBox(new ComboBox());
        corporate.setWidth("250px");
        glayout.addComponent(corporate, 0, 0);
        
        trade = new ComboBox("Trade: ");
        trade.setWidth("200px");
        corporate.addListener(corporateListener);
        glayout.addComponent(trade, 1, 0);
        
        branch = new ComboBox("Branch: ");
        branch.setWidth("180px");
        trade.addListener(tradeListener);
        branch.addListener(branchListener);
        glayout.addComponent(branch, 2, 0);
        
        department = new HRISTextField("Department: ");
        glayout.addComponent(department, 0, 1);
        
        position = new HRISTextField("Position: ");
        glayout.addComponent(position, 1, 1);
        
        entryDate = new HRISPopupDateField("Entry Date:");
        glayout.addComponent(entryDate, 2, 1);
                
        newPositionBtn.addListener(newBtnPositionListener);
        newPositionBtn.setValue(true);
        newPositionBtn.setImmediate(true);
        glayout.addComponent(newPositionBtn, 0, 2);
        
        updateBtn = new Button();
        updateBtn.setCaption(BTN_CAPTION_1);
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
                
                PostEmploymentInformationBean positionHistory = new PostEmploymentInformationBean();
                positionHistory.setPosition(position.getValue().toString().toLowerCase());
                
                if(utilities.checkInputIfInteger(corporate.getValue().toString())){
                    positionHistory.setCompany(corporate.getItemCaption(corporate.getValue()));
                } else {
                    positionHistory.setCompany(corporate.getValue().toString());                    
                }
                
                if(utilities.checkInputIfInteger(trade.getValue().toString())){
                    positionHistory.setTrade(trade.getItemCaption(trade.getValue()));
                } else {
                    positionHistory.setTrade(trade.getValue().toString());                    
                }
                
                if(utilities.checkInputIfInteger(branch.getValue().toString())){
                    positionHistory.setBranch(branch.getItemCaption(branch.getValue()));
                } else {
                    positionHistory.setBranch(branch.getValue().toString());
                }   
                
                positionHistory.setDepartment(department.getValue().toString().toLowerCase());
                positionHistory.setEntryDate((Date) entryDate.getValue());
                
                if(getBranchId() == 0){
                    corporateId = companyService.getCorporateId(positionHistory.getCompany());
                    tradeId = companyService.getTradeId(positionHistory.getTrade(), corporateId);
                    branchId = companyService.getBranchId(tradeId, positionHistory.getBranch());
                }
                
                positionHistory.setBranchId(getBranchId());
                positionHistory.setPositionId(getPositionId());
                
                if(updateBtn.getCaption().equals("EDIT") && getPositionId() == 0){
                    getWindow().showNotification("Select a row from the table to EDIT.");
                    return;
                }
                
                if(updateBtn.getCaption().equals("EDIT")){
                    isEdit = true;
                } else {
                    isEdit = false;
                }
                
                boolean result = positionHistoryService.updatePositionHistory(getEmployeeId(), positionHistory, isEdit, getPositionId());
                if(result){
                    positionHistoryTable();
                    getWindow().showNotification("Position has been updated.", Window.Notification.TYPE_TRAY_NOTIFICATION);
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
        List<PostEmploymentInformationBean> positionList = positionHistoryService.getPositionHistory(getEmployeeId());
        int i = 0;
        for(PostEmploymentInformationBean p: positionList){
            positionHistoryTbl.addItem(new Object[]{
                p.getPositionId(), 
                p.getPosition().toLowerCase(), 
                p.getCompany(), 
                p.getTrade(), 
                p.getBranch(), 
                p.getDepartment(), 
                utilities.convertDateFormat(p.getEntryDate().toString())
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
                
                newPositionBtn.setValue(false);
                updateBtn.setCaption(BTN_CAPTION_2);
                
                positionId = utilities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                setPositionId(positionId);                
                if(event.getPropertyId().equals("id")){                    
                    Window window = new RemovePositionWindow(getPositionId());
                    if(window.getParent() == null){
                        getWindow().addWindow(window);
                    }
                    window.center();
                    window.addListener(subWindowCloseListener);
                } else {
                    List<PostEmploymentInformationBean> positionListById = positionHistoryService.getPositionHistoryById(utilities.convertStringToInteger(item.getItemProperty("id").getValue().toString()));
                    for(PostEmploymentInformationBean history : positionListById){
                        Object corporateObjectId = corporate.addItem();
                        corporate.setItemCaption(corporateObjectId, history.getCompany());
                        corporate.setValue(corporateObjectId);
                        
                        Object tradeObjectId = trade.addItem();
                        trade.setItemCaption(tradeObjectId, history.getTrade());
                        trade.setValue(tradeObjectId);
                        
                        Object branchObjectId = branch.addItem();
                        branch.setItemCaption(branchObjectId, history.getBranch());
                        branch.setValue(branchObjectId);
                        
                        department.setValue(history.getDepartment());
                        position.setValue(history.getPosition());
                        entryDate.setValue(history.getEntryDate());
                    }
                }                
            }
        });
        
        return positionHistoryTbl;
    }
        
    public ComponentContainer layout2(){
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSpacing(true);          
        hlayout.setWidth("100%");
                              
        GridLayout glayout2 = new GridLayout(2, 2);
        glayout2.setSpacing(true);
        
        final PopupDateField endDate = new HRISPopupDateField("Exit Date: ");
        endDate.setWidth("250px");
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
                
                Window window = new ConfirmResignWindow(getEmployeeId(), utilities.convertDateFormat(endDate.getValue().toString().trim().toLowerCase()));
                if(window.getParent() == null){
                    getWindow().addWindow(window);
                }
            }
        });
        glayout2.addComponent(endDateBtn, 1, 0);
        glayout2.setComponentAlignment(endDateBtn, Alignment.BOTTOM_LEFT);
                                
        hlayout.addComponent(glayout2);
        hlayout.setComponentAlignment(glayout2, Alignment.MIDDLE_RIGHT);
        
        final PopupDateField entryDateFromEmp = new HRISPopupDateField("Entry Date from Employment: ");
        entryDateFromEmp.setWidth("250px");
        entryDateFromEmp.setValue(utilities.parsingDate(employeeService.getEmploymentEntryDate(getEmployeeId())));
        glayout2.addComponent(entryDateFromEmp, 0, 1);
        glayout2.setComponentAlignment(entryDateFromEmp, Alignment.BOTTOM_LEFT);
        
        Button entryDateFromEmpBtn = new Button("EDIT");
        entryDateFromEmpBtn.setWidth("150px");
        entryDateFromEmpBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(!GlobalVariables.getUserRole().equals("administrator")){
                    getWindow().showNotification("You need to be an ADMINISTRATOR to EDIT date entry of employment.", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Window sub = new EditEmploymentDateEntryWindow(getEmployeeId(), 
                utilities.convertDateFormat(entryDateFromEmp.getValue().toString()));
                if(sub.getParent() == null){
                    getWindow().addWindow(sub);
                }
            }
        });
        glayout2.addComponent(entryDateFromEmpBtn, 1, 1);
        glayout2.setComponentAlignment(entryDateFromEmpBtn, Alignment.BOTTOM_LEFT);
        
        return hlayout;
    }
    
    private String getEmployeeId(){
        return employeeId;
    }      
    
    int getPositionId(){
        return positionId;
    }
    
    void setPositionId(int positionId){
        this.positionId = positionId;
    }
    
    int getCorporateId(){
        return corporateId;
    }
    
    int getTradeId(){
        return tradeId;
    }
    
    int getBranchId(){
        return branchId;
    }
        
    void clearFields(){
        position.setValue("");
        department.setValue("");
        entryDate.setValue(new Date());
    }
    
    Window.CloseListener subWindowCloseListener = new Window.CloseListener() {

        @Override
        public void windowClose(Window.CloseEvent e) {
            positionHistoryTable();
        }
    };  
    
    Button.ClickListener newBtnPositionListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            newPositionBtn.setValue(event.getButton().booleanValue());
            if(event.getButton().booleanValue()){
                clearFields();
                updateBtn.setCaption(BTN_CAPTION_1);
            } else {
                updateBtn.setCaption(BTN_CAPTION_2);
            }
        }
    };
        
    Property.ValueChangeListener corporateListener = new Property.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                    
            } else {
                corporateId = companyService.getCorporateId(event.getProperty().getValue().toString());
                dropDown.populateTradeComboBox(trade, corporateId);
            }
        }
    };
    
    Property.ValueChangeListener tradeListener = new Property.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                    
            } else {
                tradeId = companyService.getTradeId(event.getProperty().getValue().toString(), corporateId);
                dropDown.populateBranchComboBox(branch, tradeId, corporateId);
            }
        }
    };
    
    Property.ValueChangeListener branchListener = new Property.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                    
            } else {
                branchId = companyService.getBranchId(tradeId, event.getProperty().getValue().toString());
            }
        }
    };

}
