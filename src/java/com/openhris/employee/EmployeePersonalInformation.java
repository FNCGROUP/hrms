/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.employee.model.PersonalInformation;
import com.openhris.employee.service.PersonalInformationService;
import com.openhris.employee.serviceprovider.PersonalInformationServiceImpl;
import com.vaadin.terminal.Sizeable;
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
import com.vaadin.ui.themes.Reindeer;
import java.util.Date;

/**
 *
 * @author jet
 */
public class EmployeePersonalInformation extends VerticalLayout{
    
    GridLayout glayout;	
    String employeeId = null;	
    DropDownComponent dropDownComponent = new DropDownComponent();
    PersonalInformation personalInformation;
    PersonalInformationService piService = new PersonalInformationServiceImpl();
	
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
	glayout = new GridLayout(4, 18);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
	
        Panel imagePanel = new Panel();
        imagePanel.setStyleName("light");
        AbstractLayout panelLayout = (AbstractLayout) imagePanel.getContent();
        panelLayout.setMargin(false);
	imagePanel.setWidth("100px");
        
        Embedded logo = new Embedded(null, new ThemeResource("../myTheme/img/fnc.jpg"));
        logo.setImmediate(true);
	logo.setWidth(90, Sizeable.UNITS_PIXELS);
	logo.setHeight(90, Sizeable.UNITS_PIXELS);
        logo.addStyleName("logo-img");
        imagePanel.addComponent(logo);        
        glayout.addComponent(logo, 0, 0, 0, 1);
        glayout.setComponentAlignment(imagePanel, Alignment.MIDDLE_CENTER);
                        
	Button uploadPhotoBtn = new Button("UPDATE");
	uploadPhotoBtn.setWidth("100%");
	uploadPhotoBtn.setStyleName(Reindeer.BUTTON_SMALL);
	glayout.addComponent(uploadPhotoBtn, 0, 2);
	glayout.setComponentAlignment(uploadPhotoBtn, Alignment.MIDDLE_CENTER);
	
        final TextField fnField = createTextField("Firstname: ");
        glayout.addComponent(fnField, 1, 0);
        glayout.setComponentAlignment(fnField, Alignment.MIDDLE_LEFT);
                        
        final TextField mnField = createTextField("Middlename: ");
        glayout.addComponent(mnField, 2, 0);
        glayout.setComponentAlignment(mnField, Alignment.MIDDLE_LEFT);
        
        final TextField lnField = createTextField("Lastname: ");
        glayout.addComponent(lnField, 3, 0);
        glayout.setComponentAlignment(lnField, Alignment.MIDDLE_LEFT);
                        
        final TextField companyIdField = createTextField("Employee ID: ");
	companyIdField.setEnabled(false);
        glayout.addComponent(companyIdField, 1, 1, 2, 1);
        glayout.setComponentAlignment(companyIdField, Alignment.MIDDLE_LEFT);
                
        final PopupDateField dobField = new PopupDateField("Date of Birth: ");
        dobField.addStyleName("mydate");          
        dobField.setDateFormat("yyyy-MM-dd");
        dobField.setWidth("100%");
        dobField.setResolution(DateField.RESOLUTION_DAY);
        glayout.addComponent(dobField, 1, 2);
        glayout.setComponentAlignment(dobField, Alignment.MIDDLE_LEFT);
                
        final TextField pobField = createTextField("Birth Place: ");
        glayout.addComponent(pobField, 2, 2, 3, 2);
        glayout.setComponentAlignment(pobField, Alignment.MIDDLE_LEFT);
                
        final ComboBox genderBox = dropDownComponent.populateGenderList(new ComboBox());
	genderBox.setWidth("100%");
//        Object genderItemId = genderBox.addItem();
//        genderBox.setValue(genderItemId);     
        glayout.addComponent(genderBox, 1, 3);
        glayout.setComponentAlignment(genderBox, Alignment.MIDDLE_LEFT);
        
        final ComboBox civilStatusBox = dropDownComponent.populateCivilStatusList(new ComboBox());
	civilStatusBox.setWidth("100%");
//        Object civilStatusItemId = civilStatusBox.addItem();
//        civilStatusBox.setValue(civilStatusItemId);
        glayout.addComponent(civilStatusBox, 2, 3);
        glayout.setComponentAlignment(civilStatusBox, Alignment.MIDDLE_LEFT);
                
	final TextField citizenshipField = createTextField("Citizenship: ");
        glayout.addComponent(citizenshipField, 3, 3);
        glayout.setComponentAlignment(citizenshipField, Alignment.MIDDLE_LEFT);   
	
        final TextField heightField = createTextField("Height: ");
        glayout.addComponent(heightField, 1, 4);
        glayout.setComponentAlignment(heightField, Alignment.MIDDLE_LEFT);
                
        final TextField weightField = createTextField("Weight: ");
        glayout.addComponent(weightField, 2, 4);
        glayout.setComponentAlignment(weightField, Alignment.MIDDLE_LEFT);
                
        final TextField religionField = createTextField("Religion: ");
        glayout.addComponent(religionField, 3, 4);
        glayout.setComponentAlignment(religionField, Alignment.MIDDLE_LEFT);
                            
        final TextField spouseNameField = createTextField("Spouse Name: ");
        glayout.addComponent(spouseNameField, 1, 5, 2, 5);
        glayout.setComponentAlignment(spouseNameField, Alignment.MIDDLE_LEFT);
        
        final TextField spouseOccupationField = createTextField("Spouse Occupation: ");
        glayout.addComponent(spouseOccupationField, 3, 5);
        glayout.setComponentAlignment(spouseOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField spouseOfficeAddressField = createTextField("Spouse Office Address: ");
        glayout.addComponent(spouseOfficeAddressField, 1, 6, 3, 6);
        glayout.setComponentAlignment(spouseOfficeAddressField, Alignment.MIDDLE_LEFT);
        
        final TextField fathersNameField = createTextField("Father's Name: ");
        glayout.addComponent(fathersNameField, 1, 7, 2, 7);
        glayout.setComponentAlignment(fathersNameField, Alignment.MIDDLE_LEFT);
        
        final TextField fathersOccupationField = createTextField("Father's Occupation: ");
        glayout.addComponent(fathersOccupationField, 3, 7);
        glayout.setComponentAlignment(fathersOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField mothersNameField = createTextField("Mother's Name: ");
        glayout.addComponent(mothersNameField, 1, 8, 2, 8);
        glayout.setComponentAlignment(mothersNameField, Alignment.MIDDLE_LEFT);
        
        final TextField mothersOccupationField = createTextField("Mother's Occupation: ");
        glayout.addComponent(mothersOccupationField, 3, 8);
        glayout.setComponentAlignment(mothersOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField parentsAddressField = createTextField("Parents Address");
        glayout.addComponent(parentsAddressField, 1, 9, 3, 9);
        glayout.setComponentAlignment(parentsAddressField, Alignment.MIDDLE_LEFT);
        
        final TextField dialectSpeakWriteField = createTextField("Language or Dialect you can speak or write: ");
        glayout.addComponent(dialectSpeakWriteField, 1, 10, 3, 10);
        glayout.setComponentAlignment(dialectSpeakWriteField, Alignment.MIDDLE_LEFT);
        
        final TextField contactPersonField = createTextField("Person to be contacted in case of emergency: (Name, Address, Tel. No.) ");
        glayout.addComponent(contactPersonField, 1, 11, 3, 11);
        glayout.setComponentAlignment(contactPersonField, Alignment.MIDDLE_LEFT);
        
        final TextField skillsField = createTextField("Skills: ");
        glayout.addComponent(skillsField, 1, 12, 1, 12);
        glayout.setComponentAlignment(skillsField, Alignment.MIDDLE_LEFT);
        
        final TextField hobbyField = createTextField("Hobbies");
        glayout.addComponent(hobbyField, 2, 12);
        glayout.setComponentAlignment(hobbyField, Alignment.MIDDLE_LEFT);
        
	if(employeeId != null){	    
	    personalInformation = piService.getPersonalInformationData(employeeId);
	    fnField.setValue(personalInformation.getFirstname().toUpperCase());
	    mnField.setValue(personalInformation.getMiddlename().toUpperCase());
	    lnField.setValue(personalInformation.getLastname().toUpperCase());
	    companyIdField.setValue(employeeId);
	}
	
        Button cancelButton = new Button("CANCEL/CLOSE WINDOW");
        cancelButton.setWidth("100%");
        glayout.addComponent(cancelButton, 1, 13);
        
        Button saveButton = new Button("UPDATE EMPLOYEE's INFORMATION");
        saveButton.setWidth("100%");
	saveButton.addListener(new Button.ClickListener() {

	    @Override
	    public void buttonClick(Button.ClickEvent event) {
		personalInformation = new PersonalInformation();
		personalInformation.setFirstname(fnField.getValue().toString().toLowerCase().trim());
		personalInformation.setMiddlename(mnField.getValue().toString().toLowerCase().trim());
		personalInformation.setLastname(lnField.getValue().toString().toLowerCase().trim());
		personalInformation.setEmployeeId(employeeId);
		personalInformation.setDob((Date) dobField.getValue());
		personalInformation.setPob(pobField.getValue().toString().toLowerCase().trim());
		
		boolean result = piService.updatePersonalInformation(personalInformation);
		if(result){
		    getWindow().showNotification("Information Updated", Window.Notification.TYPE_TRAY_NOTIFICATION);
		} else {
		    getWindow().showNotification("SQL Error", Window.Notification.TYPE_ERROR_MESSAGE);
		}
	    }
	});
        glayout.addComponent(saveButton, 2, 13, 3, 13);
                        
//        glayout.setColumnExpandRatio(0, .10f);
        glayout.setColumnExpandRatio(1, .10f);
        glayout.setColumnExpandRatio(2, .10f);
        glayout.setColumnExpandRatio(3, .10f);
        
        return glayout;    
    }
    
    public String getEmployeeId(){
	return employeeId;    
    }
    
    private TextField createTextField(String str){
	TextField t = new TextField();
	t.setCaption(str);
	t.setWidth("100%");
	t.setStyleName(Reindeer.TEXTFIELD_SMALL);
	
	return t;
    }
	
}
