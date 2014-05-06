/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.vaadin.data.Property;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Date;

/**
 *
 * @author jet
 */
public class EmployeePersonalInformation extends VerticalLayout{
    
    GridLayout glayout;	
    String employeeId;	
	
    public EmployeePersonalInformation(){	    
    }	
    
    public EmployeePersonalInformation(String employeeId){
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
	glayout = new GridLayout(5, 18);
        glayout.setCaption("Personal Information");
        glayout.setSpacing(true);          
        glayout.setWidth("700px");
	glayout.setHeight("100%");
	
        Panel imagePanel = new Panel();
        imagePanel.setStyleName("light");
        AbstractLayout panelLayout = (AbstractLayout) imagePanel.getContent();
        panelLayout.setMargin(false);
        imagePanel.setSizeFull();
        
        Embedded logo = new Embedded(null, new ThemeResource("../myTheme/img/fnc.jpg"));
        logo.setImmediate(true);
        logo.setWidth("100%");
        imagePanel.addComponent(logo);
        
        glayout.addComponent(logo, 0, 0, 1, 5);
        glayout.setComponentAlignment(imagePanel, Alignment.MIDDLE_CENTER);
        
        Label fnLabel = new Label("Firstname: ");
        glayout.addComponent(fnLabel, 2, 0);
        glayout.setComponentAlignment(fnLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editFn = new CheckBox("Edit");
        editFn.setEnabled(true);
        editFn.setImmediate(true);
        glayout.addComponent(editFn, 4, 0);
        glayout.setComponentAlignment(editFn, Alignment.BOTTOM_LEFT);
        
        final TextField fnField = new TextField();
        fnField.setWidth("100%");
        fnField.setImmediate(true);
        fnField.setEnabled(false);
        
        fnField.setImmediate(true);
        glayout.addComponent(fnField, 3, 0);
        glayout.setComponentAlignment(fnField, Alignment.MIDDLE_LEFT);
        
        Label mnLabel = new Label("Middlename: ");
        glayout.addComponent(mnLabel, 2, 1);
        glayout.setComponentAlignment(mnLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editMn = new CheckBox("Edit");
        editMn.setEnabled(true);
        editMn.setImmediate(true);
        glayout.addComponent(editMn, 4, 1);
        glayout.setComponentAlignment(editMn, Alignment.BOTTOM_LEFT);
        
        final TextField mnField = new TextField();
        mnField.setWidth("100%");
        mnField.setImmediate(true);
        mnField.setEnabled(false);
        
        glayout.addComponent(mnField, 3, 1);
        glayout.setComponentAlignment(mnField, Alignment.MIDDLE_LEFT);
        
        Label lnLabel = new Label("Lastname: ");
        glayout.addComponent(lnLabel, 2, 2);
        glayout.setComponentAlignment(lnLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editLn = new CheckBox("Edit");
        editLn.setEnabled(true);
        editLn.setImmediate(true);
        glayout.addComponent(editLn, 4, 2);
        glayout.setComponentAlignment(editLn, Alignment.BOTTOM_LEFT);
        
        final TextField lnField = new TextField();
        lnField.setWidth("100%");
        lnField.setImmediate(true);
        lnField.setEnabled(false);
        
        glayout.addComponent(lnField, 3, 2);
        glayout.setComponentAlignment(lnField, Alignment.MIDDLE_LEFT);
        
        Label companyIdLabel = new Label("Company ID: ");
        glayout.addComponent(companyIdLabel, 2, 3);
        glayout.setComponentAlignment(companyIdLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editCompanyId = new CheckBox("Edit");
        editCompanyId.setEnabled(true);
        editCompanyId.setImmediate(true);
        glayout.addComponent(editCompanyId, 4, 3);
        glayout.setComponentAlignment(editCompanyId, Alignment.BOTTOM_LEFT);
        
        final TextField companyIdField = new TextField();
        companyIdField.setWidth("100%");
        companyIdField.setImmediate(true);
        companyIdField.setEnabled(false);
        
        glayout.addComponent(companyIdField, 3, 3);
        glayout.setComponentAlignment(companyIdField, Alignment.MIDDLE_RIGHT);
        
        Label dobLabel = new Label("Birth Date: ");
        glayout.addComponent(dobLabel, 2, 4);
        glayout.setComponentAlignment(dobLabel, Alignment.MIDDLE_RIGHT);
        
        final PopupDateField dobField = new PopupDateField();
        dobField.addStyleName("mydate");          
        dobField.setDateFormat("yyyy-MM-dd");
        dobField.setWidth("100%");
        dobField.setResolution(DateField.RESOLUTION_DAY);
        glayout.addComponent(dobField, 3, 4, 4, 4);
        glayout.setComponentAlignment(dobField, Alignment.MIDDLE_LEFT);
        
        Label pobLabel = new Label("Birth Place: ");
        glayout.addComponent(pobLabel, 2, 5);
        glayout.setComponentAlignment(pobLabel, Alignment.MIDDLE_RIGHT);
        
        final TextField pobField = new TextField();
        pobField.setWidth("100%");
        pobField.setNullSettingAllowed(true);
        glayout.addComponent(pobField, 3, 5, 4, 5);
        glayout.setComponentAlignment(pobField, Alignment.MIDDLE_LEFT);
        
        Label genderLabel = new Label("Gender: ");
        glayout.addComponent(genderLabel, 0, 6);
        glayout.setComponentAlignment(genderLabel, Alignment.MIDDLE_RIGHT);
        
        final ComboBox genderBox = new ComboBox();
        genderBox.setWidth("100px");
        Object genderItemId = genderBox.addItem();
        genderBox.setValue(genderItemId);
        genderBox.addItem("male");
        genderBox.addItem("female");
        genderBox.setNullSelectionAllowed(false);
        genderBox.setImmediate(true);        
        glayout.addComponent(genderBox, 1, 6);
        glayout.setComponentAlignment(genderBox, Alignment.MIDDLE_LEFT);
        
        Label civilStatusLabel = new Label("Civil Status: ");
        glayout.addComponent(civilStatusLabel, 2, 6);
        glayout.setComponentAlignment(civilStatusLabel, Alignment.MIDDLE_RIGHT);
        
        final ComboBox civilStatusBox = new ComboBox();
        civilStatusBox.setWidth("100px");
        Object civilStatusItemId = civilStatusBox.addItem();
        civilStatusBox.setValue(civilStatusItemId);
        civilStatusBox.addItem("single");
        civilStatusBox.addItem("married");
        civilStatusBox.addItem("widow");
        civilStatusBox.addItem("separated");
        civilStatusBox.setNullSelectionAllowed(false);
        civilStatusBox.setImmediate(true);        
        glayout.addComponent(civilStatusBox, 3, 6);
        glayout.setComponentAlignment(civilStatusBox, Alignment.MIDDLE_LEFT);
        
        Label heightLabel = new Label("Height: ");
        glayout.addComponent(heightLabel, 0, 7);
        glayout.setComponentAlignment(heightLabel, Alignment.MIDDLE_RIGHT);
        
        final TextField heightField = new TextField();
        heightField.setWidth("100px");
        glayout.addComponent(heightField, 1, 7);
        glayout.setComponentAlignment(heightField, Alignment.MIDDLE_LEFT);
        
        Label weightLabel = new Label("Weight: ");
        glayout.addComponent(weightLabel, 2, 7);
        glayout.setComponentAlignment(weightLabel, Alignment.MIDDLE_RIGHT);
        
        final TextField weightField = new TextField();
        weightField.setWidth("100px");
        glayout.addComponent(weightField, 3, 7);
        glayout.setComponentAlignment(weightField, Alignment.MIDDLE_LEFT);
                
        final TextField religionField = new TextField("Religion");
        religionField.setWidth("100%");
        glayout.addComponent(religionField, 0, 8, 2, 8);
        glayout.setComponentAlignment(religionField, Alignment.MIDDLE_LEFT);
                
        final TextField citizenshipField = new TextField("Citizenship: ");
        citizenshipField.setWidth("100%");
        glayout.addComponent(citizenshipField, 3, 8, 4, 8);
        glayout.setComponentAlignment(citizenshipField, Alignment.MIDDLE_LEFT);   
                
        final TextField spouseNameField = new TextField("Spouse Name: ");
        spouseNameField.setWidth("100%");
        glayout.addComponent(spouseNameField, 0, 9, 2, 9);
        glayout.setComponentAlignment(spouseNameField, Alignment.MIDDLE_LEFT);
        
        final TextField spouseOccupationField = new TextField("Spouse Occupation: ");
        spouseOccupationField.setWidth("100%");
        glayout.addComponent(spouseOccupationField, 3, 9, 4, 9);
        glayout.setComponentAlignment(spouseOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField spouseOfficeAddressField = new TextField("Spouse Office Address: ");
        spouseOfficeAddressField.setWidth("100%");
        glayout.addComponent(spouseOfficeAddressField, 0, 10, 4, 10);
        glayout.setComponentAlignment(spouseOfficeAddressField, Alignment.MIDDLE_LEFT);
        
        final TextField fathersNameField = new TextField("Father's Name: ");
        fathersNameField.setWidth("100%");
        glayout.addComponent(fathersNameField, 0, 11, 2, 11);
        glayout.setComponentAlignment(fathersNameField, Alignment.MIDDLE_LEFT);
        
        final TextField fathersOccupationField = new TextField("Father's Occupation: ");
        fathersOccupationField.setWidth("100%");
        glayout.addComponent(fathersOccupationField, 3, 11, 4, 11);
        glayout.setComponentAlignment(fathersOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField mothersNameField = new TextField("Mothers's Name: ");
        mothersNameField.setWidth("100%");
        glayout.addComponent(mothersNameField, 0, 12, 2, 12);
        glayout.setComponentAlignment(mothersNameField, Alignment.MIDDLE_LEFT);
        
        final TextField mothersOccupationField = new TextField("Mother's Occupation: ");
        mothersOccupationField.setWidth("100%");
        glayout.addComponent(mothersOccupationField, 3, 12, 4, 12);
        glayout.setComponentAlignment(mothersOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField parentsAddressField = new TextField("Parents Address: ");
        parentsAddressField.setWidth("100%");
        glayout.addComponent(parentsAddressField, 0, 13, 4, 13);
        glayout.setComponentAlignment(parentsAddressField, Alignment.MIDDLE_LEFT);
        
        final TextField dialectSpeakWriteField = new TextField("Language or Dialect you can speak or write: ");
        dialectSpeakWriteField.setWidth("100%");
        glayout.addComponent(dialectSpeakWriteField, 0, 14, 4, 14);
        glayout.setComponentAlignment(dialectSpeakWriteField, Alignment.MIDDLE_LEFT);
        
        final TextField contactPersonField = new TextField("Person to be contacted in case of emergency: (Name, Address, Tel. No.) ");
        contactPersonField.setWidth("100%");
        glayout.addComponent(contactPersonField, 0, 15, 4, 15);
        glayout.setComponentAlignment(contactPersonField, Alignment.MIDDLE_LEFT);
        
        final TextField skillsField = new TextField("Skills: ");
        skillsField.setWidth("100%");
        glayout.addComponent(skillsField, 0, 16, 2, 16);
        glayout.setComponentAlignment(skillsField, Alignment.MIDDLE_LEFT);
        
        final TextField hobbyField = new TextField("Hobbies: ");
        hobbyField.setWidth("100%");
        glayout.addComponent(hobbyField, 3, 16, 4, 16);
        glayout.setComponentAlignment(hobbyField, Alignment.MIDDLE_LEFT);
        
        Button cancelButton = new Button("CANCEL/CLOSE WINDOW");
        cancelButton.setWidth("100%");
        glayout.addComponent(cancelButton, 0, 17, 1, 17);
        
        Button saveButton = new Button("UPDATE EMPLOYEE's INFORMATION");
        saveButton.setWidth("100%");
        saveButton.setEnabled(true);        
        glayout.addComponent(saveButton, 2, 17, 4, 17);
                        
        glayout.setColumnExpandRatio(0, .10f);
        //grid.setColumnExpandRatio(1, .10f);
        glayout.setColumnExpandRatio(2, .15f);
        glayout.setColumnExpandRatio(3, .30f);
        
        return glayout;    
    }
    
    public String getEmployeeId(){
	return employeeId;    
    }
	
}
