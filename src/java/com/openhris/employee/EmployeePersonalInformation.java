/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.hrms.classes.GlobalVariables;
import com.hrms.utilities.ConvertionUtilities;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.UploadImage;
import com.openhris.model.Employee;
import com.openhris.model.PersonalInformation;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.EmployeeCurrentStatusService;
import com.openhris.service.PersonalInformationService;
import com.openhris.serviceprovider.EmployeeCurrentStatusServiceImpl;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.serviceprovider.PersonalInformationServiceImpl;
import com.openhris.service.EmployeeService;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class EmployeePersonalInformation extends VerticalLayout{
    
    GridLayout glayout;	
    private String employeeId = null;
    private Application application;
    
    DropDownComponent dropDownComponent = new DropDownComponent();
    PersonalInformation personalInformation;
    PersonalInformationService piService = new PersonalInformationServiceImpl();
    ConvertionUtilities convertionUtilities = new ConvertionUtilities();
    EmployeeCurrentStatusService employeeCurrentStatusService = new EmployeeCurrentStatusServiceImpl();    
    EmployeeService employeeService = new EmployeeServiceImpl();
	
    Embedded avatar;
    TextField fnField;
    TextField mnField;
    TextField lnField;
    TextField companyIdField;
    PopupDateField dobField;
    TextField pobField;
    ComboBox genderBox;
    ComboBox civilStatusBox;
    TextField citizenshipField;
    TextField heightField;
    TextField weightField;
    TextField religionField;
    TextField spouseNameField;
    TextField spouseOccupationField;
    TextField spouseOfficeAddressField;
    TextField fathersNameField;
    TextField fathersOccupationField;
    TextField mothersNameField;
    TextField mothersOccupationField;
    TextField parentsAddressField;
    TextField dialectSpeakWriteField;
    TextField contactPersonNameField;
    TextField contactPersonAddressField;
    TextField contactPersonNoField;
    TextField skillsField;
    TextField hobbyField;
    
    public EmployeePersonalInformation(){}	
    
    public EmployeePersonalInformation(String employeeId, Application application){
        this.employeeId = employeeId;	
        this.application = application;
	
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
	glayout = new GridLayout(4, 19);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
	
        final Panel imagePanel = new Panel();
        imagePanel.setStyleName("light");
        AbstractLayout panelLayout = (AbstractLayout) imagePanel.getContent();
        panelLayout.setMargin(false);
	imagePanel.setWidth("100px");
        
        avatar = new Embedded(null, new ThemeResource("../myTheme/img/fnc.jpg"));
        avatar.setImmediate(true);
	avatar.setWidth(90, Sizeable.UNITS_PIXELS);
	avatar.setHeight(90, Sizeable.UNITS_PIXELS);
        avatar.addStyleName("logo-img");
        imagePanel.addComponent(avatar);        
        glayout.addComponent(avatar, 0, 0, 0, 1);
        glayout.setComponentAlignment(imagePanel, Alignment.MIDDLE_CENTER);
                        
	Button uploadPhotoBtn = new Button("Upload..");
	uploadPhotoBtn.setWidth("100%");
	uploadPhotoBtn.setStyleName(Reindeer.BUTTON_SMALL);
        uploadPhotoBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(getEmployeeId() == null){
                    getWindow().showNotification("You did not select and Employee!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                Window uploadImage = new UploadImage(imagePanel, avatar, getEmployeeId());
                uploadImage.setWidth("450px");
                if(uploadImage.getParent() == null){
                    getWindow().addWindow(uploadImage);
                }
                uploadImage.setModal(true);
                uploadImage.center();
            }
        });
	glayout.addComponent(uploadPhotoBtn, 0, 2);
	glayout.setComponentAlignment(uploadPhotoBtn, Alignment.MIDDLE_CENTER);
	
        fnField = createTextField("Firstname: ");
        glayout.addComponent(fnField, 1, 0);
        glayout.setComponentAlignment(fnField, Alignment.MIDDLE_LEFT);
                        
        mnField = createTextField("Middlename: ");
        glayout.addComponent(mnField, 2, 0);
        glayout.setComponentAlignment(mnField, Alignment.MIDDLE_LEFT);
        
        lnField = createTextField("Lastname: ");
        glayout.addComponent(lnField, 3, 0);
        glayout.setComponentAlignment(lnField, Alignment.MIDDLE_LEFT);
                        
        companyIdField = createTextField("Employee ID: ");
	companyIdField.setEnabled(false);
        glayout.addComponent(companyIdField, 1, 1, 2, 1);
        glayout.setComponentAlignment(companyIdField, Alignment.MIDDLE_LEFT);
                
        dobField = new PopupDateField("Date of Birth: ");
        dobField.addStyleName("mydate");          
        dobField.setDateFormat("yyyy-MM-dd");
        dobField.setWidth("100%");
        dobField.setResolution(DateField.RESOLUTION_DAY);
        glayout.addComponent(dobField, 1, 2);
        glayout.setComponentAlignment(dobField, Alignment.MIDDLE_LEFT);
                
        pobField = createTextField("Birth Place: ");
        glayout.addComponent(pobField, 2, 2, 3, 2);
        glayout.setComponentAlignment(pobField, Alignment.MIDDLE_LEFT);
                
        genderBox = dropDownComponent.populateGenderList(new ComboBox());
	genderBox.setWidth("100%");    
        glayout.addComponent(genderBox, 1, 3);
        glayout.setComponentAlignment(genderBox, Alignment.MIDDLE_LEFT);
        
        civilStatusBox = dropDownComponent.populateCivilStatusList(new ComboBox());
	civilStatusBox.setWidth("100%");
        glayout.addComponent(civilStatusBox, 2, 3);
        glayout.setComponentAlignment(civilStatusBox, Alignment.MIDDLE_LEFT);
                
	citizenshipField = createTextField("Citizenship: ");
        glayout.addComponent(citizenshipField, 3, 3);
        glayout.setComponentAlignment(citizenshipField, Alignment.MIDDLE_LEFT);   
	
        heightField = createTextField("Height(cm):");
        heightField.setValue(0);
        glayout.addComponent(heightField, 1, 4);
        glayout.setComponentAlignment(heightField, Alignment.MIDDLE_LEFT);
                
        weightField = createTextField("Weight(kg): ");
        weightField.setValue(0);
        glayout.addComponent(weightField, 2, 4);
        glayout.setComponentAlignment(weightField, Alignment.MIDDLE_LEFT);
                
        religionField = createTextField("Religion: ");
        glayout.addComponent(religionField, 3, 4);
        glayout.setComponentAlignment(religionField, Alignment.MIDDLE_LEFT);
                            
        spouseNameField = createTextField("Spouse Name: ");
        glayout.addComponent(spouseNameField, 1, 5, 2, 5);
        glayout.setComponentAlignment(spouseNameField, Alignment.MIDDLE_LEFT);
        
        spouseOccupationField = createTextField("Spouse Occupation: ");
        glayout.addComponent(spouseOccupationField, 3, 5);
        glayout.setComponentAlignment(spouseOccupationField, Alignment.MIDDLE_LEFT);
        
        spouseOfficeAddressField = createTextField("Spouse Office Address: ");
        glayout.addComponent(spouseOfficeAddressField, 1, 6, 3, 6);
        glayout.setComponentAlignment(spouseOfficeAddressField, Alignment.MIDDLE_LEFT);
        
        fathersNameField = createTextField("Father's Name: ");
        glayout.addComponent(fathersNameField, 1, 7, 2, 7);
        glayout.setComponentAlignment(fathersNameField, Alignment.MIDDLE_LEFT);
        
        fathersOccupationField = createTextField("Father's Occupation: ");
        glayout.addComponent(fathersOccupationField, 3, 7);
        glayout.setComponentAlignment(fathersOccupationField, Alignment.MIDDLE_LEFT);
        
        mothersNameField = createTextField("Mother's Maiden Name: ");
        glayout.addComponent(mothersNameField, 1, 8, 2, 8);
        glayout.setComponentAlignment(mothersNameField, Alignment.MIDDLE_LEFT);
        
        mothersOccupationField = createTextField("Mother's Occupation: ");
        glayout.addComponent(mothersOccupationField, 3, 8);
        glayout.setComponentAlignment(mothersOccupationField, Alignment.MIDDLE_LEFT);
        
        parentsAddressField = createTextField("Parents Address");
        glayout.addComponent(parentsAddressField, 1, 9, 3, 9);
        glayout.setComponentAlignment(parentsAddressField, Alignment.MIDDLE_LEFT);
        
        dialectSpeakWriteField = createTextField("Language or Dialect you can speak or write: ");
        glayout.addComponent(dialectSpeakWriteField, 1, 10, 3, 10);
        glayout.setComponentAlignment(dialectSpeakWriteField, Alignment.MIDDLE_LEFT);
        
        contactPersonNameField = createTextField("Contact Person: ");
        glayout.addComponent(contactPersonNameField, 1, 11);
        glayout.setComponentAlignment(contactPersonNameField, Alignment.MIDDLE_LEFT);
        
        contactPersonAddressField = createTextField("Contact Person's Address: ");
        glayout.addComponent(contactPersonAddressField, 2, 11, 3, 11);
        glayout.setComponentAlignment(contactPersonAddressField, Alignment.MIDDLE_LEFT);
        
        contactPersonNoField = createTextField("Contact Person's Tel No: ");
        glayout.addComponent(contactPersonNoField, 1, 12);
        glayout.setComponentAlignment(contactPersonNoField, Alignment.MIDDLE_LEFT);
        
        skillsField = createTextField("Skills: ");
        glayout.addComponent(skillsField, 2, 12);
        glayout.setComponentAlignment(skillsField, Alignment.MIDDLE_LEFT);
        
        hobbyField = createTextField("Hobbies");
        glayout.addComponent(hobbyField, 3, 12);
        glayout.setComponentAlignment(hobbyField, Alignment.MIDDLE_LEFT);
        
	if(employeeId != null){	            
	    personalInformation = piService.getPersonalInformationData(employeeId);
            final byte[] image = personalInformation.getImage();
            if(image != null){
                StreamResource.StreamSource imageSource = new StreamResource.StreamSource(){

                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(image);
                    }
                        
                };
                
                StreamResource imageResource = new StreamResource(imageSource, personalInformation.getFirstname()+".jpg", getThisApplication());
                imageResource.setCacheTime(0);
                avatar.setSource(imageResource);
            } 
	    fnField.setValue(personalInformation.getFirstname().toUpperCase());
	    mnField.setValue(personalInformation.getMiddlename().toUpperCase());
	    lnField.setValue(personalInformation.getLastname().toUpperCase());
            companyIdField.setValue(employeeId);
            dobField.setValue(personalInformation.getDob());
            pobField.setValue(personalInformation.getPob());
            
            if(personalInformation.getCivilStatus() != null){
                Object civilStatusId = civilStatusBox.addItem();
                civilStatusBox.setItemCaption(civilStatusId, personalInformation.getCivilStatus());
                civilStatusBox.setValue(civilStatusId);
            }
                
            if(personalInformation.getGender() != null){
                Object genderId = genderBox.addItem();
                genderBox.setItemCaption(genderId, personalInformation.getGender());
                genderBox.setValue(genderId);
            }
            
            citizenshipField.setValue(personalInformation.getCitizenship());
            heightField.setValue(personalInformation.getHeight());
            weightField.setValue(personalInformation.getWeight());
            religionField.setValue(personalInformation.getReligion());
            spouseNameField.setValue(personalInformation.getSpouseName());
            spouseOccupationField.setValue(personalInformation.getSpouseOccupation());
            spouseOfficeAddressField.setValue(personalInformation.getSpouseOfficeAddress());
            fathersNameField.setValue(personalInformation.getFathersName());
            fathersOccupationField.setValue(personalInformation.getFathersOccupation());
            mothersNameField.setValue(personalInformation.getMothersName());
            mothersOccupationField.setValue(personalInformation.getMothersOccupation());
            parentsAddressField.setValue(personalInformation.getParentsAddress());
            dialectSpeakWriteField.setValue(personalInformation.getDialectSpeakWrite());
            contactPersonNameField.setValue(personalInformation.getContactPersonName());
            contactPersonAddressField.setValue(personalInformation.getContactPersonAddress());
            contactPersonNoField.setValue(personalInformation.getContactPersonNo());
            skillsField.setValue(personalInformation.getSkills());
            hobbyField.setValue(personalInformation.getHobby());	    
	}
	
        Button removeBtn = new Button("REMOVE EMPLOYEE");
        removeBtn.setWidth("100%");
        boolean visible = false;
        if(GlobalVariables.getUserRole()== null){
            visible = false;
        } else if (GlobalVariables.getUserRole().equals("hr") || GlobalVariables.getUserRole().equals("administrator")){
            visible = true;
        }
        removeBtn.setVisible(visible);
        removeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(!GlobalVariables.getUserRole().equals("administrator")){
                    getWindow().showNotification("You need to an ADMINISTRATOR to perform this ACTION.", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                Window window = getRemoveWindow(getEmployeeId());
                window.setModal(true);
                if(window.getParent() == null){
                    getWindow().addWindow(window);
                }
                window.center();
            }
        });
        glayout.addComponent(removeBtn, 1, 13);
        
        Button saveButton = new Button("UPDATE EMPLOYEE's INFORMATION");
        saveButton.setWidth("100%");
	saveButton.addListener(new Button.ClickListener() {

	    @Override
	    public void buttonClick(Button.ClickEvent event) {
                if(dobField.getValue() == null || dobField.getValue().toString().isEmpty()){
                    getWindow().showNotification("Date of Birth Required!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(heightField.getValue() == null || heightField.getValue().toString().isEmpty()){
                    getWindow().showNotification("Null/Empty Value for Height is not ALLOWED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                } else {
                    if(!convertionUtilities.checkInputIfDouble(heightField.getValue().toString())){
                        getWindow().showNotification("Enter a numeric format for Height!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                
                if(weightField.getValue() == null || weightField.getValue().toString().isEmpty()){
                    getWindow().showNotification("Null/Empty Value for Weight is not ALLOWED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                } else {
                    if(!convertionUtilities.checkInputIfDouble(weightField.getValue().toString())){
                        getWindow().showNotification("Enter a numeric format for Weight!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                
                if(genderBox.getValue() == null || genderBox.getValue().toString().isEmpty()){
                    getWindow().showNotification("Select a Gender!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(civilStatusBox.getValue() == null || civilStatusBox.getValue().toString().isEmpty()){
                    getWindow().showNotification("Select Civil Status!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
		personalInformation = new PersonalInformation();
		personalInformation.setFirstname(fnField.getValue().toString().toLowerCase().trim());
		personalInformation.setMiddlename(mnField.getValue().toString().toLowerCase().trim());
		personalInformation.setLastname(lnField.getValue().toString().toLowerCase().trim());
		personalInformation.setEmployeeId(employeeId);
		personalInformation.setDob((Date) dobField.getValue());
		personalInformation.setPob((pobField.getValue() == null) ? "N/A" : pobField.getValue().toString().toLowerCase().trim());                
                personalInformation.setHeight(convertionUtilities.convertStringToDouble(heightField.getValue().toString()));
                personalInformation.setWeight(convertionUtilities.convertStringToDouble(weightField.getValue().toString()));
                
                if(convertionUtilities.checkInputIfInteger(genderBox.getValue().toString())){
                    personalInformation.setGender(genderBox.getItemCaption(genderBox.getValue()));
                } else {
                    personalInformation.setGender(genderBox.getValue().toString());
                }
                
                if(convertionUtilities.checkInputIfInteger(civilStatusBox.getValue().toString())){
                    personalInformation.setCivilStatus(civilStatusBox.getItemCaption(civilStatusBox.getValue()));
                } else {
                    personalInformation.setCivilStatus(civilStatusBox.getValue().toString());
                }
                
                personalInformation.setCitizenship((citizenshipField.getValue() == null) ? "N/A" : citizenshipField.getValue().toString());
                personalInformation.setReligion((religionField.getValue() == null)? "N/A" : religionField.getValue().toString());
                personalInformation.setSpouseName((spouseNameField.getValue() == null) ? "N/A" : spouseNameField.getValue().toString());
                personalInformation.setSpouseOccupation((spouseOccupationField.getValue() == null) ? "N/A" : spouseOccupationField.getValue().toString());
                personalInformation.setSpouseOfficeAddress((spouseOfficeAddressField.getValue() == null) ? "N/A" : spouseOfficeAddressField.getValue().toString());
                personalInformation.setFathersName((fathersNameField.getValue() == null) ? "N/A" : fathersNameField.getValue().toString());
                personalInformation.setFathersOccupation((fathersOccupationField.getValue() == null) ? "N/A" : fathersOccupationField.getValue().toString());
                personalInformation.setMothersName((mothersNameField.getValue() == null) ? "N/A" : mothersNameField.getValue().toString());
                personalInformation.setMothersOccupation((mothersOccupationField.getValue() == null) ? "N/A" : mothersOccupationField.getValue().toString());
                personalInformation.setParentsAddress((parentsAddressField.getValue() == null) ? "N/A" : parentsAddressField.getValue().toString());
                personalInformation.setDialectSpeakWrite((dialectSpeakWriteField.getValue() == null) ? "N/A" : dialectSpeakWriteField.getValue().toString());
                personalInformation.setContactPersonName((contactPersonNameField.getValue() == null) ? "N/A" : contactPersonNameField.getValue().toString());
                personalInformation.setContactPersonAddress((contactPersonAddressField.getValue() == null) ? "N/A" : contactPersonAddressField.getValue().toString());
                personalInformation.setContactPersonNo((contactPersonNoField.getValue() == null) ? "N/A" : contactPersonNoField.getValue().toString());
                personalInformation.setSkills((skillsField.getValue() == null) ? "N/A" : skillsField.getValue().toString());
                personalInformation.setHobby((hobbyField.getValue() == null) ? "N/A" : hobbyField.getValue().toString());
		
		boolean result = piService.updatePersonalInformation(personalInformation);
		if(result){
		    getWindow().showNotification("Information Updated", Window.Notification.TYPE_TRAY_NOTIFICATION);
		} else {
		    getWindow().showNotification("SQL Error", Window.Notification.TYPE_ERROR_MESSAGE);
		}
	    }
	});
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr")){
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
        glayout.addComponent(saveButton, 2, 13, 3, 13);
                        
        glayout.setColumnExpandRatio(1, .10f);
        glayout.setColumnExpandRatio(2, .10f);
        glayout.setColumnExpandRatio(3, .10f);
        
        return glayout;    
    }
    
    private Window getRemoveWindow(String employeeId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        final Window window = new Window("REMOVE EMPLOYEE", vlayout);
        window.setWidth("350px");
        
        Button removeBtn = new Button("Are you sure your want to remove this Employee?");
        removeBtn.setWidth("100%");
        removeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {                
                EmployeeMainUI employeeMainUI = new EmployeeMainUI(GlobalVariables.getUserRole(), 0);
                boolean result = employeeCurrentStatusService.removeEmployee(getEmployeeId());
                if(result){
                    clearFields();
                    employeeMainUI.employeesTable(getEmployeeList(0));
                    (window.getParent()).removeWindow(window);
                } else {
                    getWindow().showNotification("Cannot Remove Employee, Contact your DBA!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        window.addComponent(removeBtn);
        
        return window;
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
	    
    public List<Employee> getEmployeeList(int branchId){
        return employeeService.getEmployeePerBranch(branchId);
    }
    
    public Application getThisApplication(){
        return application;
    }
    
    public void clearFields(){
        fnField.setValue("");
        mnField.setValue("");
        lnField.setValue("");
        companyIdField.setValue("");
        dobField.setValue(new Date());
        pobField.setValue("");
        
        genderBox.removeAllItems();
        genderBox = dropDownComponent.populateGenderList(genderBox);
        
        civilStatusBox.removeAllItems();
        civilStatusBox = dropDownComponent.populateCivilStatusList(civilStatusBox);
        
        citizenshipField.setValue("");
        heightField.setValue("");
        weightField.setValue("");
        religionField.setValue("");
        spouseNameField.setValue("");
        spouseOccupationField.setValue("");
        spouseOfficeAddressField.setValue("");
        fathersNameField.setValue("");
        fathersOccupationField.setValue("");
        mothersNameField.setValue("");
        mothersOccupationField.setValue("");
        parentsAddressField.setValue("");
        dialectSpeakWriteField.setValue("");
        contactPersonNameField.setValue("");
        contactPersonAddressField.setValue("");
        contactPersonNoField.setValue("");
        skillsField.setValue("");
        hobbyField.setValue("");
    }
}
