/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author jetdario
 */
public class EmployeeCharacterReference extends VerticalLayout {
    
    private String employeeId;
    GridLayout glayout;
    Table characterReferenceTbl = new Table();
    
    TextField nameField;
    TextField occupationField;
    TextField contactNoField;
    TextField addressField;
    
    public EmployeeCharacterReference(){}
    
    public EmployeeCharacterReference(String employeeId){
        this.employeeId = employeeId;
        
        init();
        addComponent(layout());
        setComponentAlignment(glayout, Alignment.TOP_CENTER);
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
//        nameField.setWidth("100%");
        glayout.addComponent(nameField, 0, 0);
        
        occupationField = createTextField("Occupation: ");
//        occupationField.setWidth("100%");
        glayout.addComponent(occupationField, 1, 0);
        
        contactNoField = createTextField("Contact No: ");
//        contactNoField.setWidth("100%");
        glayout.addComponent(contactNoField, 2, 0);
        
        addressField = createTextField("Address: ");
//        addressField.setWidth("100%");
        glayout.addComponent(addressField, 0, 1, 1, 1);
        
        Button addReferenceBtn = new Button("Add Reference: ");
        addReferenceBtn.setWidth("100%");
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
}
