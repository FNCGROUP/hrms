/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.model.CharacterReference;
import com.openhris.employee.service.CharacterReferenceService;
import com.openhris.employee.serviceprovider.CharacterReferenceServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class EmployeeCharacterReference extends VerticalLayout {
    
    OpenHrisUtilities util = new OpenHrisUtilities();
    private String employeeId;
    GridLayout glayout;
    Table characterReferenceTbl = new Table();
    
    TextField nameField;
    TextField occupationField;
    TextField contactNoField;
    TextField addressField;
    
    CharacterReferenceService charRefService = new CharacterReferenceServiceImpl();
    int classCharacterRefId = 0;
    
    public EmployeeCharacterReference(){}
    
    public EmployeeCharacterReference(String employeeId){
        this.employeeId = employeeId;
        
        init();
        addComponent(layout());
        setComponentAlignment(glayout, Alignment.TOP_CENTER);
        characterReferenceTable();
        addComponent(characterReferenceTbl);        
    }
    
    public void init(){
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
    }
    
    public ComponentContainer layout(){
        glayout = new GridLayout(3, 3);
        glayout.setCaption("Character References");
        glayout.setSpacing(true);          
        glayout.setSizeFull();
        
        nameField = createTextField("Name: ");
        glayout.addComponent(nameField, 0, 0);
        
        occupationField = createTextField("Occupation: ");
        glayout.addComponent(occupationField, 1, 0);
        
        contactNoField = createTextField("Contact No: ");
        glayout.addComponent(contactNoField, 2, 0);
        
        addressField = createTextField("Address: ");
        glayout.addComponent(addressField, 0, 1, 1, 1);
        
        Button addReferenceBtn = new Button("Add/Update Reference: ");
        addReferenceBtn.setWidth("100%");
        addReferenceBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(nameField.getValue() == null || nameField.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Name of Reference!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(occupationField.getValue() == null || occupationField.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Occuation of Reference!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(contactNoField.getValue() == null || contactNoField.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Contact No of Reference!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(addressField.getValue() == null || addressField.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Address of Reference!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                CharacterReference charRef = new CharacterReference();
                charRef.setCharacterReferenceId(classCharacterRefId);
                charRef.setEmployeeId(getEmployeeId());
                charRef.setName(nameField.getValue().toString().trim().toLowerCase());
                charRef.setOccupation(occupationField.getValue().toString().trim().toLowerCase());
                charRef.setContactNo(contactNoField.getValue().toString().trim().toLowerCase());
                charRef.setAddress(addressField.getValue().toString().trim().toLowerCase());
                
                boolean result = charRefService.updateEmployeeCharacterReference(charRef);
                if(result){
                    characterReferenceTable();
                    clearFields();
                } else {
                    getWindow().showNotification("Error on Character Reference SQL, Contact your DBA!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        glayout.addComponent(addReferenceBtn);
        glayout.setComponentAlignment(addReferenceBtn, Alignment.BOTTOM_CENTER);
        
        return glayout;
    }
    
    public Table characterReferenceTable(){
        characterReferenceTbl.removeAllItems();
        characterReferenceTbl.setSizeFull();
        characterReferenceTbl.setSelectable(true);
        characterReferenceTbl.setImmediate(true);        
        characterReferenceTbl.setStyleName("employees-table-layout");
        
        characterReferenceTbl.addContainerProperty("id", String.class, null);
        characterReferenceTbl.addContainerProperty("name", String.class, null);
        characterReferenceTbl.addContainerProperty("address", String.class, null);
        characterReferenceTbl.addContainerProperty("occupation", String.class, null);
        characterReferenceTbl.addContainerProperty("contact no", String.class, null);
        
        List<CharacterReference> characterReferenceList = charRefService.getEmployeeCharacterReferenceList(getEmployeeId());
        int i = 0;
        for(CharacterReference cr : characterReferenceList){
            characterReferenceTbl.addItem(new Object[]{
                cr.getCharacterReferenceId(), 
                cr.getName(), 
                cr.getAddress(), 
                cr.getOccupation(), 
                cr.getContactNo()
            }, i);
            i++;
        }
        
        characterReferenceTbl.setPageLength(4);
        
        for(Object listener : characterReferenceTbl.getListeners(ItemClickEvent.class)){
            characterReferenceTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        characterReferenceTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = characterReferenceTbl.getItem(itemId);
                
                int referenceId = util.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                
                if(event.getPropertyId().equals("id")){
                    Window window = removeCharacterReferenceWindow(referenceId);
                    if(window.getParent() == null){
                        getWindow().addWindow(window);
                    } 
                    window.center();
                }
                
                if(event.getPropertyId().equals("name") || event.getPropertyId().equals("address")){
                    CharacterReference charRef = charRefService.getEmployeeCharacterReferenceById(referenceId);
                    classCharacterRefId = referenceId;
                    
                    nameField.setValue(charRef.getName());
                    occupationField.setValue(charRef.getOccupation());
                    contactNoField.setValue(charRef.getContactNo());
                    addressField.setValue(charRef.getAddress());
                }
            }
        });
        
        return characterReferenceTbl;
    }
    
    public String getEmployeeId(){
        return employeeId;
    }
    
    private TextField createTextField(String str){
	TextField t = new TextField();
	t.setCaption(str);
	t.setWidth("100%");
	t.setStyleName(Reindeer.TEXTFIELD_SMALL);
        t.setNullSettingAllowed(true);
        
	return t;
    }
    
    private Window removeCharacterReferenceWindow(final int characterReferenceId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window window = new Window("REMOVE REFERENCE", vlayout);
        window.setWidth("300px");        
        
        Button removeBtn = new Button("Remove Character Reference?");
        removeBtn.setWidth("100%");
        removeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = charRefService.removeEmployeeCharacterReferenceC(characterReferenceId);
                
                if(result){
                    characterReferenceTable();
                    (window.getParent()).removeWindow(window);
                    clearFields();
                } else {
                    getWindow().showNotification("Cannot Remove Reference, Contact your DBA!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        window.addComponent(removeBtn);
        
        return window;
    }
    
    public void clearFields(){
        nameField.setValue("");
        occupationField.setValue("");
        contactNoField.setValue("");
        addressField.setValue("");
        classCharacterRefId = 0;
    }
}
