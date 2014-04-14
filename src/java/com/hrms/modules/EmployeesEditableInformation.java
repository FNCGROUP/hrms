/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.beans.*;
import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.GlobalVariables;
import com.hrms.classes.TradeName;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeesEditableInformation extends VerticalLayout {
    
    ConvertionUtilities conUtil = new ConvertionUtilities();    
    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();    
    private String userRole;;
    
    EmployeesInfoBean employeesInfoBean = new EmployeesInfoBean();
    EmployeeAddressBean employeeAddressBean = new EmployeeAddressBean();
    EmployeeCharacterReferencesBean characterReferencesBean = new EmployeeCharacterReferencesBean();
    EmployeeDependentBean employeeDependentBean = new EmployeeDependentBean();
    EmployeeEducationalBackgroundBean educationalBackgroundBean = new EmployeeEducationalBackgroundBean();
    EmployeePositionHistoryBean positionHistoryBean = new EmployeePositionHistoryBean();
    EmploymentRecordsBean employmentRecordsBean = new EmploymentRecordsBean();
    
    Table employeeAddressTable = new Table(); 
    Table employeeCharacterReferencesTable = new Table();
    Table employeeDependentTable = new Table();
    Table employeeEducationalBackgroundTable = new Table();
    Table employeePositionHistoryTable = new Table();
    Table employmentRecordTable = new Table();
    
    CorporateName corporateName = new CorporateName();
    TradeName tradeName = new TradeName();
    BranchName branchName = new BranchName();
    
    public EmployeesEditableInformation(String userRole){
        this.userRole = userRole;
    }
    
    public ComponentContainer viewEmployeeInformation(String id){        
        boolean rowStatus = query.checkEmployeesStatus(id);
        Window subWindow = new Window();
        subWindow.setWidth("650px");
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        ts.addComponent(personalInformation(id, rowStatus, subWindow));
        ts.addComponent(employeesAddress(id, rowStatus, subWindow));
        ts.addComponent(employeesCharacterReferences(id, rowStatus, subWindow));
        ts.addComponent(employeesDependents(id, rowStatus, subWindow));
        ts.addComponent(employeesEducationalBackground(id, rowStatus, subWindow));
        ts.addComponent(employeesPositionHistory(id, rowStatus, subWindow));
        ts.addComponent(employeesEmploymentRecord(id, rowStatus, subWindow));
        ts.addComponent(salaryInformation(id, rowStatus, subWindow));
        
        subWindow.addComponent(ts);
        return subWindow;
    }
    
    ComponentContainer personalInformation(final String id, Boolean rowStatus, final Window subWindow){
        String status = "Employed";
        if(rowStatus == false){
            status = "Resigned";
        }
        subWindow.setCaption("Employee's Perosnal Information - Employment Status: "+status);
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        final EmployeesPersonalInformationBean personalInformation = new EmployeesPersonalInformationBean();
        boolean hasRows = personalInformation.getEmployeePersonalInformation(id);
        String firstname = null;
        String middlename = null;
        String lastname = null;
                
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                firstname = rs.getString("firstname");
                middlename = rs.getString("middlename");
                lastname = rs.getString("lastname");
            }            
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        GridLayout grid = new GridLayout(5, 18);
        grid.setCaption("Personal Information");
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        Panel imagePanel = new Panel();
        imagePanel.setStyleName("light");
        AbstractLayout panelLayout = (AbstractLayout) imagePanel.getContent();
        panelLayout.setMargin(false);
        imagePanel.setSizeFull();
        
        Embedded logo = new Embedded(null, new ThemeResource("../myTheme/img/fnc.jpg"));
        logo.setImmediate(true);
        logo.setWidth("100%");
        imagePanel.addComponent(logo);
        
        grid.addComponent(logo, 0, 0, 1, 5);
        grid.setComponentAlignment(imagePanel, Alignment.MIDDLE_CENTER);
        
        Label fnLabel = new Label("Firstname: ");
        grid.addComponent(fnLabel, 2, 0);
        grid.setComponentAlignment(fnLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editFn = new CheckBox("Edit");
        editFn.setEnabled(rowStatus);
        editFn.setImmediate(true);
        grid.addComponent(editFn, 4, 0);
        grid.setComponentAlignment(editFn, Alignment.BOTTOM_LEFT);
        
        final TextField fnField = new TextField();
        fnField.setWidth("100%");
        fnField.setValue(firstname);
        fnField.setImmediate(true);
        fnField.setEnabled(false);
        editFn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                fnField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        fnField.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(fnField.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("FIRSTNAME FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateFirstname(fnField.getValue().toString().trim(), id);
                if(result == true){
                    fnField.setEnabled(false);
                    editFn.setValue(false);
                    subWindow.getWindow().showNotification("UPDATED FIRSTNAME!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR FIRSTNAME!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        fnField.setImmediate(true);
        grid.addComponent(fnField, 3, 0);
        grid.setComponentAlignment(fnField, Alignment.MIDDLE_LEFT);
        
        Label mnLabel = new Label("Middlename: ");
        grid.addComponent(mnLabel, 2, 1);
        grid.setComponentAlignment(mnLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editMn = new CheckBox("Edit");
        editMn.setEnabled(rowStatus);
        editMn.setImmediate(true);
        grid.addComponent(editMn, 4, 1);
        grid.setComponentAlignment(editMn, Alignment.BOTTOM_LEFT);
        
        final TextField mnField = new TextField();
        mnField.setWidth("100%");
        mnField.setValue(middlename);
        mnField.setImmediate(true);
        mnField.setEnabled(false);
        editMn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                mnField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        mnField.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(mnField.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("MIDDLENAME FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = employeesInfoBean.updateMiddlename(mnField.getValue().toString().trim(), id);
                if(result == true){
                    mnField.setEnabled(false);
                    editMn.setValue(false);
                    subWindow.getWindow().showNotification("UPDATED MIDDLENAME!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR MIDDLENAME!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        mnField.setImmediate(true);
        grid.addComponent(mnField, 3, 1);
        grid.setComponentAlignment(mnField, Alignment.MIDDLE_LEFT);
        
        Label lnLabel = new Label("Lastname: ");
        grid.addComponent(lnLabel, 2, 2);
        grid.setComponentAlignment(lnLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editLn = new CheckBox("Edit");
        editLn.setEnabled(rowStatus);
        editLn.setImmediate(true);
        grid.addComponent(editLn, 4, 2);
        grid.setComponentAlignment(editLn, Alignment.BOTTOM_LEFT);
        
        final TextField lnField = new TextField();
        lnField.setWidth("100%");
        lnField.setValue(lastname);
        lnField.setImmediate(true);
        lnField.setEnabled(false);
        editLn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                lnField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        lnField.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(lnField.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("LASTNAME FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = employeesInfoBean.updateLastname(lnField.getValue().toString().trim(), id);
                if(result == true){
                    lnField.setEnabled(false);
                    editLn.setValue(false);
                    subWindow.getWindow().showNotification("UPDATED LASTNAME!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR LASTNAME!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        lnField.setImmediate(true);
        grid.addComponent(lnField, 3, 2);
        grid.setComponentAlignment(lnField, Alignment.MIDDLE_LEFT);
        
        Label companyIdLabel = new Label("Company ID: ");
        grid.addComponent(companyIdLabel, 2, 3);
        grid.setComponentAlignment(companyIdLabel, Alignment.MIDDLE_RIGHT);
        
        final CheckBox editCompanyId = new CheckBox("Edit");
        editCompanyId.setEnabled(rowStatus);
        editCompanyId.setImmediate(true);
        grid.addComponent(editCompanyId, 4, 3);
        grid.setComponentAlignment(editCompanyId, Alignment.BOTTOM_LEFT);
        
        final TextField companyIdField = new TextField();
        companyIdField.setWidth("100%");
        companyIdField.setValue(id);
        companyIdField.setImmediate(true);
        companyIdField.setEnabled(false);
        editCompanyId.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                companyIdField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        companyIdField.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(companyIdField.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("COMPANY ID FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean checkId = query.checkEmployeeIdIfExist(companyIdField.getValue().toString().trim());
                
                if(checkId == false){                    
                    subWindow.getWindow().showNotification("COMPANY ID ALREADY EXIST!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = employeesInfoBean.updateCompanyId(companyIdField.getValue().toString().trim(), id);
                
                if(result == true){
                    companyIdField.setEnabled(false);
                    editCompanyId.setValue(false);
                    subWindow.getWindow().showNotification("UPDATED COMPANY ID!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR COMPANY ID!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        companyIdField.setImmediate(true);
        grid.addComponent(companyIdField, 3, 3);
        grid.setComponentAlignment(companyIdField, Alignment.MIDDLE_RIGHT);
        
        Label dobLabel = new Label("Birth Date: ");
        grid.addComponent(dobLabel, 2, 4);
        grid.setComponentAlignment(dobLabel, Alignment.MIDDLE_RIGHT);
        
        final PopupDateField dobField = new PopupDateField();
        dobField.addStyleName("mydate");
        if(personalInformation.getDob() == null){
            dobField.setValue(new Date());
        }else{
            dobField.setValue(conUtil.parsingDate(personalInformation.getDob()));
        }        
        dobField.setDateFormat("yyyy-MM-dd");
        dobField.setWidth("100%");
        dobField.setResolution(DateField.RESOLUTION_DAY);
        grid.addComponent(dobField, 3, 4, 4, 4);
        grid.setComponentAlignment(dobField, Alignment.MIDDLE_LEFT);
        
        Label pobLabel = new Label("Birth Place: ");
        grid.addComponent(pobLabel, 2, 5);
        grid.setComponentAlignment(pobLabel, Alignment.MIDDLE_RIGHT);
        
        final TextField pobField = new TextField();
        pobField.setWidth("100%");
        pobField.setValue(personalInformation.getPob());
        pobField.setNullSettingAllowed(true);
        grid.addComponent(pobField, 3, 5, 4, 5);
        grid.setComponentAlignment(pobField, Alignment.MIDDLE_LEFT);
        
        Label genderLabel = new Label("Gender: ");
        grid.addComponent(genderLabel, 0, 6);
        grid.setComponentAlignment(genderLabel, Alignment.MIDDLE_RIGHT);
        
        final ComboBox genderBox = new ComboBox();
        genderBox.setWidth("100px");
        Object genderItemId = genderBox.addItem();
        genderBox.setItemCaption(genderItemId, personalInformation.getGender());
        genderBox.setValue(genderItemId);
        genderBox.addItem("male");
        genderBox.addItem("female");
        genderBox.setNullSelectionAllowed(false);
        genderBox.setImmediate(true);        
        grid.addComponent(genderBox, 1, 6);
        grid.setComponentAlignment(genderBox, Alignment.MIDDLE_LEFT);
        
        Label civilStatusLabel = new Label("Civil Status: ");
        grid.addComponent(civilStatusLabel, 2, 6);
        grid.setComponentAlignment(civilStatusLabel, Alignment.MIDDLE_RIGHT);
        
        final ComboBox civilStatusBox = new ComboBox();
        civilStatusBox.setWidth("100px");
        Object civilStatusItemId = civilStatusBox.addItem();
        civilStatusBox.setItemCaption(civilStatusItemId, personalInformation.getCivilStatus());
        civilStatusBox.setValue(civilStatusItemId);
        civilStatusBox.addItem("single");
        civilStatusBox.addItem("married");
        civilStatusBox.addItem("widow");
        civilStatusBox.addItem("separated");
        civilStatusBox.setNullSelectionAllowed(false);
        civilStatusBox.setImmediate(true);        
        grid.addComponent(civilStatusBox, 3, 6);
        grid.setComponentAlignment(civilStatusBox, Alignment.MIDDLE_LEFT);
        
        Label heightLabel = new Label("Height: ");
        grid.addComponent(heightLabel, 0, 7);
        grid.setComponentAlignment(heightLabel, Alignment.MIDDLE_RIGHT);
        
        final TextField heightField = new TextField();
        heightField.setWidth("100px");
        heightField.setValue(personalInformation.getHeight());
        grid.addComponent(heightField, 1, 7);
        grid.setComponentAlignment(heightField, Alignment.MIDDLE_LEFT);
        
        Label weightLabel = new Label("Weight: ");
        grid.addComponent(weightLabel, 2, 7);
        grid.setComponentAlignment(weightLabel, Alignment.MIDDLE_RIGHT);
        
        final TextField weightField = new TextField();
        weightField.setWidth("100px");
        weightField.setValue(personalInformation.getWeight());
        grid.addComponent(weightField, 3, 7);
        grid.setComponentAlignment(weightField, Alignment.MIDDLE_LEFT);
                
        final TextField religionField = new TextField("Religion");
        religionField.setWidth("100%");
        religionField.setValue(personalInformation.getReligion());
        grid.addComponent(religionField, 0, 8, 2, 8);
        grid.setComponentAlignment(religionField, Alignment.MIDDLE_LEFT);
                
        final TextField citizenshipField = new TextField("Citizenship: ");
        citizenshipField.setWidth("100%");
        citizenshipField.setValue(personalInformation.getCitizenship());
        grid.addComponent(citizenshipField, 3, 8, 4, 8);
        grid.setComponentAlignment(citizenshipField, Alignment.MIDDLE_LEFT);   
                
        final TextField spouseNameField = new TextField("Spouse Name: ");
        spouseNameField.setWidth("100%");
        spouseNameField.setValue(personalInformation.getSpouseName());
        grid.addComponent(spouseNameField, 0, 9, 2, 9);
        grid.setComponentAlignment(spouseNameField, Alignment.MIDDLE_LEFT);
        
        final TextField spouseOccupationField = new TextField("Spouse Occupation: ");
        spouseOccupationField.setWidth("100%");
        spouseOccupationField.setValue(personalInformation.getSpouseOccupation());
        grid.addComponent(spouseOccupationField, 3, 9, 4, 9);
        grid.setComponentAlignment(spouseOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField spouseOfficeAddressField = new TextField("Spouse Office Address: ");
        spouseOfficeAddressField.setWidth("100%");
        spouseOfficeAddressField.setValue(personalInformation.getSpouseOfficeAddress());
        grid.addComponent(spouseOfficeAddressField, 0, 10, 4, 10);
        grid.setComponentAlignment(spouseOfficeAddressField, Alignment.MIDDLE_LEFT);
        
        final TextField fathersNameField = new TextField("Father's Name: ");
        fathersNameField.setWidth("100%");
        fathersNameField.setValue(personalInformation.getFathersName());
        grid.addComponent(fathersNameField, 0, 11, 2, 11);
        grid.setComponentAlignment(fathersNameField, Alignment.MIDDLE_LEFT);
        
        final TextField fathersOccupationField = new TextField("Father's Occupation: ");
        fathersOccupationField.setWidth("100%");
        fathersOccupationField.setValue(personalInformation.getFathersOccupation());
        grid.addComponent(fathersOccupationField, 3, 11, 4, 11);
        grid.setComponentAlignment(fathersOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField mothersNameField = new TextField("Mothers's Name: ");
        mothersNameField.setWidth("100%");
        mothersNameField.setValue(personalInformation.getMothersName());
        grid.addComponent(mothersNameField, 0, 12, 2, 12);
        grid.setComponentAlignment(mothersNameField, Alignment.MIDDLE_LEFT);
        
        final TextField mothersOccupationField = new TextField("Mother's Occupation: ");
        mothersOccupationField.setWidth("100%");
        mothersOccupationField.setValue(personalInformation.getMothersOccupation());
        grid.addComponent(mothersOccupationField, 3, 12, 4, 12);
        grid.setComponentAlignment(mothersOccupationField, Alignment.MIDDLE_LEFT);
        
        final TextField parentsAddressField = new TextField("Parents Address: ");
        parentsAddressField.setWidth("100%");
        parentsAddressField.setValue(personalInformation.getParentsAddress());
        grid.addComponent(parentsAddressField, 0, 13, 4, 13);
        grid.setComponentAlignment(parentsAddressField, Alignment.MIDDLE_LEFT);
        
        final TextField dialectSpeakWriteField = new TextField("Language or Dialect you can speak or write: ");
        dialectSpeakWriteField.setWidth("100%");
        dialectSpeakWriteField.setValue(personalInformation.getDialectSpeakWrite());
        grid.addComponent(dialectSpeakWriteField, 0, 14, 4, 14);
        grid.setComponentAlignment(dialectSpeakWriteField, Alignment.MIDDLE_LEFT);
        
        final TextField contactPersonField = new TextField("Person to be contacted in case of emergency: (Name, Address, Tel. No.) ");
        contactPersonField.setWidth("100%");
        contactPersonField.setValue(personalInformation.getContactPerson());
        grid.addComponent(contactPersonField, 0, 15, 4, 15);
        grid.setComponentAlignment(contactPersonField, Alignment.MIDDLE_LEFT);
        
        final TextField skillsField = new TextField("Skills: ");
        skillsField.setWidth("100%");
        skillsField.setValue(personalInformation.getSkills());
        grid.addComponent(skillsField, 0, 16, 2, 16);
        grid.setComponentAlignment(skillsField, Alignment.MIDDLE_LEFT);
        
        final TextField hobbyField = new TextField("Hobbies: ");
        hobbyField.setWidth("100%");
        hobbyField.setValue(personalInformation.getHobby()); 
        grid.addComponent(hobbyField, 3, 16, 4, 16);
        grid.setComponentAlignment(hobbyField, Alignment.MIDDLE_LEFT);
        
        Button cancelButton = new Button("CANCEL/CLOSE WINDOW");
        cancelButton.setWidth("100%");
        cancelButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                (subWindow.getParent()).removeWindow(subWindow);
            }
        });
        grid.addComponent(cancelButton, 0, 17, 1, 17);
        
        Button saveButton = new Button("UPDATE EMPLOYEE's INFORMATION");
        saveButton.setWidth("100%");
        saveButton.setEnabled(rowStatus);
        saveButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean result = personalInformation.getEmployeePersonalInformation(id);
                boolean resultInsert;
                boolean resultUpdate;
                                
                if(heightField.getValue() == null){
                    subWindow.getWindow().showNotification("NULL/Empty is not allowed for Height Field!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }else{
                    if(conUtil.checkInputIfDouble(heightField.getValue().toString().trim()) == false){
                        subWindow.getWindow().showNotification("Value for height is not allowed!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                
                if(weightField.getValue() == null || weightField.getValue().toString().isEmpty()){
                    subWindow.getWindow().showNotification("NULL/Empty is not allowed for Weight Field!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }else{
                    if(conUtil.checkInputIfDouble(weightField.getValue().toString().trim()) == false){
                        subWindow.getWindow().showNotification("Value for weight is not allowed!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                
                if(genderBox.getValue() == null){
                    subWindow.getWindow().showNotification("Select Employee's GENDER!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(civilStatusBox.getValue() == null){
                    subWindow.getWindow().showNotification("Select Employee's CIVIL STATUS!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(pobField.getValue() == null ||religionField.getValue() == null || citizenshipField.getValue() == null ||  
                        spouseNameField.getValue() == null || spouseOccupationField.getValue() == null || spouseOccupationField.getValue() == null ||  
                        spouseOfficeAddressField.getValue() == null || fathersNameField.getValue() == null || fathersOccupationField.getValue() == null || 
                        mothersNameField.getValue() == null || mothersOccupationField.getValue() == null || parentsAddressField.getValue() == null || 
                        dialectSpeakWriteField.getValue() == null || contactPersonField.getValue() == null || skillsField.getValue() == null || hobbyField.getValue() == null){
                    subWindow.getWindow().showNotification("COMPLETE ALL FIELDS!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(pobField.getValue().toString().trim().isEmpty() ||religionField.getValue().toString().trim().isEmpty() || 
                        citizenshipField.getValue().toString().trim().isEmpty() || spouseNameField.getValue().toString().trim().isEmpty() || 
                        spouseOccupationField.getValue().toString().trim().isEmpty() || spouseOccupationField.getValue().toString().trim().isEmpty() ||  
                        spouseOfficeAddressField.getValue().toString().trim().isEmpty() || fathersNameField.getValue().toString().trim().isEmpty() || 
                        fathersOccupationField.getValue().toString().trim().isEmpty() || mothersNameField.getValue().toString().trim().isEmpty() || 
                        mothersOccupationField.getValue().toString().trim().isEmpty() || parentsAddressField.getValue().toString().trim().isEmpty() || 
                        dialectSpeakWriteField.getValue().toString().trim().isEmpty() || contactPersonField.getValue().toString().trim().isEmpty() || 
                        skillsField.getValue().toString().trim().isEmpty() || hobbyField.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("EMPTY FIELDS is NOT ALLOWED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                personalInformation.setDob(conUtil.convertDateFormat(dobField.getValue().toString()));
                personalInformation.setPob(pobField.getValue().toString().trim());
                personalInformation.setGender(genderBox.getValue().toString());
                personalInformation.setCivilStatus(civilStatusBox.getValue().toString());
                personalInformation.setHeight(conUtil.convertStringToDouble(heightField.getValue().toString().trim()));
                personalInformation.setWeight(conUtil.convertStringToDouble(weightField.getValue().toString().trim()));
                personalInformation.setReligion(religionField.getValue().toString().trim());
                personalInformation.setCitizenship(citizenshipField.getValue().toString().trim());
                personalInformation.setSpouseName(spouseNameField.getValue().toString().trim());
                personalInformation.setSpouseOccupation(spouseOccupationField.getValue().toString().trim());
                personalInformation.setSpouseOfficeAddress(spouseOfficeAddressField.getValue().toString().trim());
                personalInformation.setFathersName(fathersNameField.getValue().toString().trim());
                personalInformation.setFathersOccupation(fathersOccupationField.getValue().toString().trim());
                personalInformation.setMothersName(mothersNameField.getValue().toString().trim());
                personalInformation.setMothersOccupation(mothersOccupationField.getValue().toString().trim());
                personalInformation.setParentsAddress(parentsAddressField.getValue().toString().trim());
                personalInformation.setDialectSpeakWrite(dialectSpeakWriteField.getValue().toString().trim());
                personalInformation.setContactPerson(contactPersonField.getValue().toString().trim());
                personalInformation.setSkills(skillsField.getValue().toString().trim());
                personalInformation.setHobby(hobbyField.getValue().toString().trim());
                
                if(result == true){
                    resultUpdate = personalInformation.updateEmployeePersonalInformation(id);
                    if(resultUpdate = true){
                        (subWindow.getParent()).removeWindow(subWindow);
                    }else{
                        getWindow().showNotification("QUERY ERROR ON PERSONAL INFORMATION UPDATE!", Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                }else{
                    resultInsert = personalInformation.insertEmployeePersonalInformation(id);
                    if(resultInsert = true){
                        (subWindow.getParent()).removeWindow(subWindow);
                    }else{
                        getWindow().showNotification("QUERY ERROR ON PERSONAL INFORMATION INSERT!", Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                }
            }
            
        });
        grid.addComponent(saveButton, 2, 17, 4, 17);
                        
        grid.setColumnExpandRatio(0, .10f);
        //grid.setColumnExpandRatio(1, .10f);
        grid.setColumnExpandRatio(2, .15f);
        grid.setColumnExpandRatio(3, .30f);
        
        return grid;
    }
    
    ComponentContainer employeesAddress(final String id, Boolean rowStatus, final Window subWindow){
        GridLayout grid = new GridLayout(4, 3);
        grid.setCaption("Address");
        grid.setSpacing(true);          
        grid.setSizeFull();
                
        final TextField street = new TextField("Street:");
        street.setWidth("100%");
        grid.addComponent(street, 0, 0, 1, 0);
        
        final TextField city = new TextField("City/Town: ");
        city.setWidth("100%");
        grid.addComponent(city, 2, 0, 3, 0);
        
        final TextField province = new TextField("Province: ");
        province.setWidth("100%");
        grid.addComponent(province, 0, 1);
        
        final TextField zipCode = new TextField("Zip Code: ");
        zipCode.setWidth("100%");
        grid.addComponent(zipCode, 1, 1);
        
        final ComboBox addressType = new ComboBox("Address Type:");
        addressType.setWidth("100%");
        addressType.addItem("city");
        addressType.addItem("provincial");
        addressType.setNullSelectionAllowed(false);
        addressType.setImmediate(true);
        grid.addComponent(addressType, 2, 1);
        
        Button saveButton = new Button("ADD ADDRESS");
        saveButton.setWidth("100%");
        saveButton.setEnabled(rowStatus);
        saveButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(street.getValue() == null || street.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter STREET!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(city.getValue() == null || city.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter CITY/TOWN!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(province.getValue() == null || province.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter PROVINCE!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(zipCode.getValue() == null || zipCode.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter ZIP CODE!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(addressType.getValue() == null){
                    subWindow.getWindow().showNotification("Select ADDRESS TYPE!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                employeeAddressBean.setEmployeeId(id);
                employeeAddressBean.setType(addressType.getValue().toString().toLowerCase());
                employeeAddressBean.setStreet(street.getValue().toString().trim().toLowerCase());
                employeeAddressBean.setCity(city.getValue().toString().trim().toLowerCase());
                employeeAddressBean.setProvince(province.getValue().toString().trim().toLowerCase()); 
                employeeAddressBean.setZipcode(zipCode.getValue().toString().trim().toLowerCase());
                
                Boolean result = employeeAddressBean.addNewAddress();
                if(result == true){
                    employeeAddressTbl(id, subWindow);                    
                }else{
                    subWindow.getWindow().showNotification("Cannot add new ADDRESS!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(saveButton, 3, 1);
        grid.setComponentAlignment(saveButton, Alignment.BOTTOM_CENTER);
        
        employeeAddressTbl(id,subWindow);
        grid.addComponent(employeeAddressTable, 0, 2, 3, 2);
        
        return grid;
    }
    
    ComponentContainer employeesCharacterReferences(final String id, Boolean rowStatus, final Window subWindow){
        GridLayout grid = new GridLayout(3, 3);
        grid.setCaption("Character References");
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        final TextField name = new TextField("Name: ");
        name.setWidth("100%");
        grid.addComponent(name, 0, 0);
        
        final TextField occupation = new TextField("Occupation: ");
        occupation.setWidth("100%");
        grid.addComponent(occupation, 1, 0);
        
        final TextField contactNo = new TextField("Contact No: ");
        contactNo.setWidth("100%");
        grid.addComponent(contactNo, 2, 0);
        
        final TextField address = new TextField("Address: ");
        address.setWidth("100%");
        grid.addComponent(address, 0, 1, 1, 1);
        
        Button addReference = new Button("Add Reference: ");
        addReference.setWidth("100%");
        addReference.setEnabled(rowStatus);
        addReference.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(name.getValue() == null || name.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter NAME!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(occupation.getValue() == null || occupation.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter OCCUPATION!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(address.getValue() == null || address.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter ADDRESS!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                characterReferencesBean.setEmployeeId(id);
                characterReferencesBean.setName(name.getValue().toString().trim().toLowerCase());
                characterReferencesBean.setOccupation(occupation.getValue().toString().trim().toLowerCase());
                characterReferencesBean.setAddress(address.getValue().toString().trim().toLowerCase());
                characterReferencesBean.setContactNo(contactNo.getValue().toString().trim().toLowerCase());
                
                Boolean result = characterReferencesBean.insertNewCharacterReference();
                if(result == true){
                    employeeCharacterReferencesTbl(id, subWindow);                    
                }else{
                    subWindow.getWindow().showNotification("Cannot ADD new Reference!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        grid.addComponent(addReference, 2, 1);
        grid.setComponentAlignment(addReference, Alignment.BOTTOM_CENTER);
        
        employeeCharacterReferencesTbl(id, subWindow);
        grid.addComponent(employeeCharacterReferencesTable, 0, 2, 2, 2);
        
        return grid;
    }
    
    ComponentContainer employeesDependents(final String id, Boolean rowStatus, final Window subWindow){
        GridLayout grid = new GridLayout(3, 2);
        grid.setCaption("Dependents");
        grid.setSpacing(true);          
        grid.setSizeFull();
                
        final TextField name = new TextField("Name: ");
        name.setWidth("100%");
        grid.addComponent(name, 0, 0);
        
        final PopupDateField dobField = new PopupDateField("Date of Birth: ");
        dobField.addStyleName("mydate");   
        dobField.setDateFormat("yyyy-MM-dd");
        dobField.setWidth("100%");
        dobField.setResolution(DateField.RESOLUTION_DAY);
        grid.addComponent(dobField, 1, 0);
        
        Button addDependent = new Button("ADD DEPENDENT");
        addDependent.setWidth("100%");
        addDependent.setEnabled(rowStatus);
        addDependent.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(name.getValue() == null || name.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter NAME!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(dobField.getValue() == null || dobField.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter Date of Birth!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                employeeDependentBean.setEmployeeId(id);
                employeeDependentBean.setName(name.getValue().toString().trim().toLowerCase());
                employeeDependentBean.setDob(conUtil.convertDateFormat(dobField.getValue().toString().trim()));
                
                Boolean result = employeeDependentBean.insertNewDependent();
                if(result = true){
                    employeeDependentTbl(id, subWindow);
                }else{
                    subWindow.getWindow().showNotification("Cannot ADD Dependent!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(addDependent, 2, 0);
        grid.setComponentAlignment(addDependent, Alignment.BOTTOM_CENTER);
        
        employeeDependentTbl(id, subWindow);
        grid.addComponent(employeeDependentTable, 0, 1, 2, 1);
        
        return grid;
    }
    
    ComponentContainer employeesEducationalBackground(final String id, Boolean rowStatus, final Window subWindow){
        GridLayout grid = new GridLayout(3, 3);
        grid.setCaption("Educational Background");
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        final TextField course = new TextField("Course: ");
        course.setWidth("100%");
        grid.addComponent(course, 0, 0, 1, 0);
        
        final TextField year = new TextField("Year Graduated: ");
        year.setWidth("100%");
        grid.addComponent(year, 2, 0);
        
        final TextField school = new TextField("School: ");
        school.setWidth("100%");
        grid.addComponent(school, 0, 1, 1, 1);
        
        Button addEducation = new Button("ADD EDUCATION");
        addEducation.setWidth("100%");
        addEducation.setEnabled(rowStatus);
        addEducation.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(course.getValue() == null || course.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter COURSE!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(school.getValue() == null || school.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter SCHOOL!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(year.getValue() == null || year.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter YEAR GRADUATED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                educationalBackgroundBean.setEmployeeId(id);
                educationalBackgroundBean.setCourse(course.getValue().toString().trim().toLowerCase());
                educationalBackgroundBean.setSchool(school.getValue().toString().trim().toLowerCase());
                educationalBackgroundBean.setYearGraduated(year.getValue().toString().trim().toLowerCase());
                
                Boolean result = educationalBackgroundBean.insertNewEducationalBackground();
                if(result == true){
                    employeeEducationalBackground(id, subWindow);                    
                }else{
                    subWindow.getWindow().showNotification("Cannot ADD Educational Background!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(addEducation, 2, 1);
        grid.setComponentAlignment(addEducation, Alignment.BOTTOM_CENTER);
        
        employeeEducationalBackground(id, subWindow);
        grid.addComponent(employeeEducationalBackgroundTable, 0, 2, 2, 2);
        
        return grid;
    }
    
    ComponentContainer employeesPositionHistory(final String id, Boolean rowStatus, final Window subWindow){
        GridLayout grid = new GridLayout(2, 5);
        grid.setCaption("Position History");
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        corporateName.setUserRole(GlobalVariables.getUserRole());
        final NativeSelect corporate = new NativeSelect("Corporate: ");
        corporate.setWidth("100%");
        corporateName.getCorporateName(corporate);
        grid.addComponent(corporate, 0, 0);
        
        tradeName.setUserRole(GlobalVariables.getUserRole());
        final NativeSelect trade = new NativeSelect("Trade: ");
        trade.setWidth("100%");
        corporate.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(corporate.getValue() == null){            
                }else{
                    tradeName.getTradeName(trade, corporate.getValue().toString());
                }
            }
            
        });                
        grid.addComponent(trade, 1, 0);
        
        branchName.setUserRole(GlobalVariables.getUserRole());
        final NativeSelect branch = new NativeSelect("Branch: ");
        branch.setWidth("100%");
        trade.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(trade.getValue() == null){                    
                }else{
                    branchName.getBranchName(branch, trade.getValue().toString(), corporate.getValue().toString());
                }
            }
            
        });
        grid.addComponent(branch, 0, 1);
        
        final TextField department = new TextField("Department: ");
        department.setWidth("100%");
        grid.addComponent(department, 1, 1);
        
        final TextField position = new TextField("Position: ");
        position.setWidth("100%");
        grid.addComponent(position, 0, 2);
        
        final PopupDateField entryDate = new PopupDateField("Entry Date: ");
        entryDate.addStyleName("mydate");   
        entryDate.setDateFormat("yyyy-MM-dd");
        entryDate.setWidth("100%");
        entryDate.setResolution(DateField.RESOLUTION_DAY);
        grid.addComponent(entryDate, 1, 2);
        
        Button updatePosition = new Button("UPDATE POSITION in the COMPANY");
        updatePosition.setWidth("100%");
        updatePosition.setEnabled(rowStatus);
        updatePosition.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(corporate.getValue() == null || trade.getValue() == null || branch.getValue() == null){
                    subWindow.getWindow().showNotification("SELECT Corporate/Trade/Branch!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(department.getValue() == null || department.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Department!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(position.getValue() == null || position.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Position!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(entryDate.getValue() == null || entryDate.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Entry Date!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                Integer branchId = query.getBranchId(branch.getValue().toString(), trade.getValue().toString(), corporate.getValue().toString());
                
                positionHistoryBean.setEmployeeId(id);
                positionHistoryBean.setBranchId(branchId);
                positionHistoryBean.setDepartment(department.getValue().toString().trim().toLowerCase());
                positionHistoryBean.setPosition(position.getValue().toString().trim().toLowerCase());
                positionHistoryBean.setEntryDate(conUtil.convertDateFormat(entryDate.getValue().toString().trim().toLowerCase()));
                
                Boolean result = positionHistoryBean.updatePosition();
                if(result == true){
                    employeePositionHistory(id, subWindow);
                }else{
                    subWindow.getWindow().showNotification("Cannot UPDATE Position!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
            }
            
        });
        grid.addComponent(updatePosition, 1, 3);
        grid.setComponentAlignment(updatePosition, Alignment.BOTTOM_CENTER);
        
        employeePositionHistory(id, subWindow);
        grid.addComponent(employeePositionHistoryTable, 0, 4, 1, 4);
        
        return grid;
    }
    
    ComponentContainer employeesEmploymentRecord(final String id, Boolean rowStatus, final Window subWindow){
        GridLayout grid = new GridLayout(2, 4);
        grid.setCaption("Employment Record");
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        final PopupDateField startDate = new PopupDateField("Start Date: ");
        startDate.addStyleName("mydate");   
        startDate.setDateFormat("yyyy-MM-dd");
        startDate.setWidth("100%");
        startDate.setResolution(DateField.RESOLUTION_DAY);
        grid.addComponent(startDate, 0, 0);
        
        final PopupDateField endDate = new PopupDateField("End Date: ");
        endDate.addStyleName("mydate");   
        endDate.setDateFormat("yyyy-MM-dd");
        endDate.setWidth("100%");
        endDate.setResolution(DateField.RESOLUTION_DAY);
        grid.addComponent(endDate, 1, 0);
        
        final TextField position = new TextField("Position: ");
        position.setWidth("100%");
        grid.addComponent(position, 0, 1);
        
        final TextField company = new TextField("Company: ");
        company.setWidth("100%");
        grid.addComponent(company, 1, 1);
        
        Button addRecord = new Button("ADD EMPLOYMENT RECORD");
        addRecord.setWidth("100%");
        addRecord.setEnabled(rowStatus);
        addRecord.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(startDate.getValue() == null || startDate.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter Start Date!", Window.Notification.TYPE_WARNING_MESSAGE);
                } 
                
                if(endDate.getValue() == null || endDate.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter End Date!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
                
                if(position.getValue() == null || position.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter Position!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
                
                if(company.getValue() == null || company.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Enter Company!", Window.Notification.TYPE_WARNING_MESSAGE);
                }
                
                employmentRecordsBean.setEmployeeId(id);
                employmentRecordsBean.setDateStart(conUtil.convertDateFormat(startDate.getValue().toString()));
                employmentRecordsBean.setDateEnd(conUtil.convertDateFormat(endDate.getValue().toString()));
                employmentRecordsBean.setPosition(position.getValue().toString().trim().toLowerCase());
                employmentRecordsBean.setCompany(company.getValue().toString().trim().toLowerCase());
                
                Boolean result = employmentRecordsBean.addEmploymentRecords();
                if(result == true){
                    employmentRecord(id, subWindow);
                }else{
                    subWindow.getWindow().showNotification("Cannot ADD Employment Record!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(addRecord, 1, 2);
        
        employmentRecord(id, subWindow);
        grid.addComponent(employmentRecordTable, 0, 3, 1, 3);
        
        return grid;
    }
    
    ComponentContainer salaryInformation(final String id, Boolean rowStatus, final Window subWindow){
        Boolean result = employeesInfoBean.getSalaryInfo(id);
        GridLayout grid = new GridLayout(4, 17);
        grid.setCaption("Salary Information");
        grid.setSpacing(true);    
        
        // Check box on setting HDMF TextField to TRUE 
        final CheckBox editHdmf = new CheckBox("Edit");
        editHdmf.setEnabled(rowStatus);
        editHdmf.setImmediate(true);
        grid.addComponent(editHdmf, 1, 0);
        grid.setComponentAlignment(editHdmf, Alignment.BOTTOM_LEFT);
        
        // Text field in editting HDMF ID #
        final TextField hdmfNo = new TextField("HDMF #: ");
        hdmfNo.setWidth("225px");
        hdmfNo.setValue(employeesInfoBean.getHdmfNo());
        hdmfNo.setEnabled(false);
        hdmfNo.setImmediate(true);        
        editHdmf.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                hdmfNo.setEnabled(event.getButton().booleanValue());
            }
            
        });
        hdmfNo.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(hdmfNo.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("HDMF FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateHdmf(hdmfNo.getValue().toString().trim(), id);
                if(result == true){
                    hdmfNo.setEnabled(false);
                    editHdmf.setValue(false);
                    subWindow.getWindow().showNotification("HDMF # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR HDMF ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(hdmfNo, 0, 0);
        
        // Check box on setting SSS TextField to TRUE 
        final CheckBox editSss = new CheckBox("Edit");
        editSss.setEnabled(rowStatus);
        editSss.setImmediate(true);
        grid.addComponent(editSss, 3, 0);
        grid.setComponentAlignment(editSss, Alignment.BOTTOM_LEFT);
        
        
        // Text field in editting SSS ID #
        final TextField sssNo = new TextField("SSS #: ");
        sssNo.setWidth("225px");
        sssNo.setValue(employeesInfoBean.getSssNo());
        sssNo.setEnabled(false);
        sssNo.setImmediate(true);        
        editSss.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                sssNo.setEnabled(event.getButton().booleanValue());
            }
            
        });
        sssNo.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(sssNo.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("SSS FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateSss(sssNo.getValue().toString().trim(), id);
                if(result == true){
                    sssNo.setEnabled(false);
                    editSss.setValue(false);
                    subWindow.getWindow().showNotification("SSS # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR SSS ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(sssNo, 2, 0);
        
        // Check box on setting TIN TextField to TRUE
        final CheckBox editTin = new CheckBox("Edit");
        editTin.setEnabled(rowStatus);
        editTin.setImmediate(true);
        grid.addComponent(editTin, 1, 1);
        grid.setComponentAlignment(editTin, Alignment.BOTTOM_LEFT);
        
        // Text field in editting TIN ID #
        final TextField tinNo = new TextField("Tin #: ");
        tinNo.setWidth("100%");
        tinNo.setValue(employeesInfoBean.getTinNo());
        tinNo.setEnabled(false);
        tinNo.setImmediate(true);        
        editTin.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                tinNo.setEnabled(event.getButton().booleanValue());
            }
            
        });
        tinNo.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(tinNo.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("SSS FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateTin(tinNo.getValue().toString().trim(), id);
                if(result == true){
                    tinNo.setEnabled(false);
                    editTin.setValue(false);
                    subWindow.getWindow().showNotification("TIN # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR TIN ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(tinNo, 0, 1);
        
        // Check box on setting Phic TextField to TRUE
        final CheckBox editPhilhealth = new CheckBox("Edit");
        editPhilhealth.setEnabled(rowStatus);
        editPhilhealth.setImmediate(true);
        grid.addComponent(editPhilhealth, 3, 1);
        grid.setComponentAlignment(editPhilhealth, Alignment.BOTTOM_LEFT);
        
        // Text field in editting Phic ID #
        final TextField philhealthNo = new TextField("Philhealth #: ");
        philhealthNo.setWidth("100%");
        philhealthNo.setValue(employeesInfoBean.getPhilhealthNo());
        philhealthNo.setEnabled(false);
        philhealthNo.setImmediate(true);        
        editPhilhealth.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                philhealthNo.setEnabled(event.getButton().booleanValue());
            }
            
        });
        philhealthNo.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(philhealthNo.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("PHILHEALTH FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updatePhilhealth(philhealthNo.getValue().toString().trim(), id);
                if(result == true){
                    philhealthNo.setEnabled(false);
                    editPhilhealth.setValue(false);
                    subWindow.getWindow().showNotification("PHILHEALTH # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR PHILHEALTH ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(philhealthNo, 2, 1);
        
        // Check box on setting Employement Status Combo Box to TRUE
        final CheckBox editEmploymentStatus = new CheckBox("Edit");
        editEmploymentStatus.setEnabled(rowStatus);
        editEmploymentStatus.setImmediate(true);
        grid.addComponent(editEmploymentStatus, 1, 2);
        grid.setComponentAlignment(editEmploymentStatus, Alignment.BOTTOM_LEFT);
        
        // COmbo Box in editting Employement Status
        final ComboBox employmentStatus = new ComboBox();
        employmentStatus.setCaption("Employment Status: "+employeesInfoBean.getEmploymentStatus().toUpperCase());
        employmentStatus.setWidth("100%");
        employmentStatus.setNullSelectionAllowed(false);
        employmentStatus.addItem("regular");
        employmentStatus.addItem("contractual");
        employmentStatus.setEnabled(false);
        employmentStatus.setImmediate(true);        
        editEmploymentStatus.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employmentStatus.setEnabled(event.getButton().booleanValue());
            }
            
        });
        employmentStatus.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {                
                Boolean result = employeesInfoBean.updateEmploymentStatus(employmentStatus.getValue().toString(), id);
                if(result == true){
                    employmentStatus.setCaption("Employment Status: "+employmentStatus.getValue().toString().toUpperCase());
                    employmentStatus.setEnabled(false);
                    editEmploymentStatus.setValue(false);
                    subWindow.getWindow().showNotification("EMPLOYMENT STATUS UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR EMPLOYMENT STATUS ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }                
            }
            
        });
        grid.addComponent(employmentStatus, 0, 2);
        
        // Check box on setting Employement Wage Status Combo Box to TRUE
        final CheckBox editEmploymentWageStatus = new CheckBox("Edit");
        editEmploymentWageStatus.setEnabled(rowStatus);
        editEmploymentWageStatus.setImmediate(true);
        grid.addComponent(editEmploymentWageStatus, 3, 2);
        grid.setComponentAlignment(editEmploymentWageStatus, Alignment.BOTTOM_LEFT);
        
        // COmbo Box in editting Employement Wage Status
        final ComboBox employmentWageStatus = new ComboBox();
        employmentWageStatus.setCaption("Employment Wage Status: "+employeesInfoBean.getEmploymentWageStatus().toUpperCase());
        employmentWageStatus.setWidth("100%");
        employmentWageStatus.setNullSelectionAllowed(false);
        employmentWageStatus.addItem("regular");
        employmentWageStatus.addItem("minimum");
        employmentWageStatus.setEnabled(false);
        employmentWageStatus.setImmediate(true);         
        editEmploymentWageStatus.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employmentWageStatus.setEnabled(event.getButton().booleanValue());
            }
            
        });
        employmentWageStatus.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean result = employeesInfoBean.updateEmploymentWageStatus(employmentWageStatus.getValue().toString(), id);
                if(result == true){
                    employmentWageStatus.setCaption("Employment Wage Status: "+employmentWageStatus.getValue().toString().toUpperCase());
                    employmentWageStatus.setEnabled(false);
                    editEmploymentWageStatus.setValue(false);
                    subWindow.getWindow().showNotification("EMPLOYMENT WAGE STATUS UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR EMPLOYMENT WAGE STATUS ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(employmentWageStatus, 2, 2);
        
        // Check box on setting Employement Wage Entry Combo Box to TRUE
        final CheckBox editEmploymentWageEntry = new CheckBox("Edit");
        editEmploymentWageEntry.setEnabled(rowStatus);
        editEmploymentWageEntry.setImmediate(true);
        grid.addComponent(editEmploymentWageEntry, 1, 3);
        grid.setComponentAlignment(editEmploymentWageEntry, Alignment.BOTTOM_LEFT);
        
        // COmbo Box in editting Employement Wage Entry
        final ComboBox employmentWageEntry = new ComboBox();
        employmentWageEntry.setCaption("Employment Wage Entry: "+employeesInfoBean.getEmploymentWageEntry().toUpperCase());
        employmentWageEntry.setWidth("100%");
        employmentWageEntry.setNullSelectionAllowed(false);
        employmentWageEntry.addItem("monthly");
        employmentWageEntry.addItem("daily");
        employmentWageEntry.setEnabled(false);
        employmentWageEntry.setImmediate(true);        
        editEmploymentWageEntry.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employmentWageEntry.setEnabled(event.getButton().booleanValue());
            }
            
        });
        employmentWageEntry.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean result = employeesInfoBean.updateEmploymentWageEntry(employmentWageEntry.getValue().toString(), id);
                if(result == true){
                    employmentWageEntry.setCaption("Employment Wage Entry: "+employmentWageEntry.getValue().toString().toUpperCase());
                    employmentWageEntry.setEnabled(false);
                    editEmploymentWageEntry.setValue(false);
                    subWindow.getWindow().showNotification("EMPLOYMENT WAGE ENTRY UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR EMPLOYMENT WAGE ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(employmentWageEntry, 0, 3);
        
        // Check box on setting Employement Wage Text field to TRUE
        final CheckBox editEmploymentWage= new CheckBox("Edit");
        //if(GlobalVariables.getUserRole().equals("administrator")){editEmploymentWage.setVisible(true);}else{editEmploymentWage.setVisible(false);}         
        if(GlobalVariables.getUserRole().equals("accounting")){editEmploymentWage.setVisible(true);}else{editEmploymentWage.setVisible(false);} 
        editEmploymentWage.setEnabled(rowStatus);
        editEmploymentWage.setImmediate(true);
        grid.addComponent(editEmploymentWage, 3, 3);
        grid.setComponentAlignment(editEmploymentWage, Alignment.BOTTOM_LEFT);
        
        // Text field in editting Employement Wage
        final TextField employmentWage = new TextField("Salary");
        employmentWage.setWidth("100%");
        employmentWage.setValue(employeesInfoBean.getEmploymentWage());
        //if(GlobalVariables.getUserRole().equals("administrator")){employmentWage.setVisible(true);}else{employmentWage.setVisible(false);}        
        if(GlobalVariables.getUserRole().equals("accounting")){employmentWage.setVisible(true);}else{employmentWage.setVisible(false);}
        employmentWage.setEnabled(false);
        employmentWage.setImmediate(true);
        editEmploymentWage.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employmentWage.setEnabled(event.getButton().booleanValue());
            }
            
        });
        employmentWage.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(employmentWage.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("EMPLOYMENT WAGE IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateEmploymentWage(employmentWage.getValue().toString().trim(), id);
                if(result == true){
                    employmentWage.setEnabled(false);
                    editEmploymentWage.setValue(false);
                    subWindow.getWindow().showNotification("EMPLOYMENT WAGE UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR EMPLOYMENT WAGE ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(employmentWage, 2, 3);                
        
        // Check box on setting Allowance Text field to TRUE
        final CheckBox editAllowance = new CheckBox("Edit");
        editAllowance.setEnabled(rowStatus);
        editAllowance.setImmediate(true);
        grid.addComponent(editAllowance, 1, 4);
        grid.setComponentAlignment(editAllowance, Alignment.BOTTOM_LEFT);
        
        // Text field in editting Allowance
        final TextField employmentAllowanceEntry = new TextField("Allowance: ");
        employmentAllowanceEntry.setWidth("100%");
        employmentAllowanceEntry.setValue(employeesInfoBean.getAllowanceEntry());
        employmentAllowanceEntry.setEnabled(false);
        employmentAllowanceEntry.setImmediate(true);
        editAllowance.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employmentAllowanceEntry.setEnabled(event.getButton().booleanValue());
            }
            
        });
        employmentAllowanceEntry.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(employmentAllowanceEntry.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("EMPLOYMENT WAGE IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateAllowanceEntry(employmentAllowanceEntry.getValue().toString().trim(), id);
                if(result == true){
                    employmentAllowanceEntry.setEnabled(false);
                    editAllowance.setValue(false);
                    subWindow.getWindow().showNotification("ALLOWANCE ENTRY UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR ALLOWANCE ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(employmentAllowanceEntry, 0, 4);
        
        // Check box on setting Allowance Status Combo box to TRUE
        final CheckBox editAllowanceStatus = new CheckBox("Edit");
        editAllowanceStatus.setEnabled(rowStatus);
        editAllowanceStatus.setImmediate(true);
        grid.addComponent(editAllowanceStatus, 3, 4);
        grid.setComponentAlignment(editAllowanceStatus, Alignment.BOTTOM_LEFT);
        
        // Combo box in editting Allowance Status
        final ComboBox employmentAllowanceStatus = new ComboBox();
        employmentAllowanceStatus.setCaption("Allowance Status: "+employeesInfoBean.getAllowanceEntry().toUpperCase());
        employmentAllowanceStatus.setWidth("100%");
        employmentAllowanceStatus.setNullSelectionAllowed(false);
        employmentAllowanceStatus.addItem("monthly");
        employmentAllowanceStatus.addItem("daily");
        employmentAllowanceStatus.addItem("N/A");
        employmentAllowanceStatus.setImmediate(true); 
        employmentAllowanceStatus.setEnabled(false);
        editAllowanceStatus.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                employmentAllowanceStatus.setEnabled(event.getButton().booleanValue());
            }
        });
        employmentAllowanceStatus.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean result = employeesInfoBean.updateAllowanceEntry(employmentAllowanceStatus.getValue().toString(), id);
                if(result == true){
                    employmentAllowanceStatus.setCaption("Allowance Status: "+employmentAllowanceStatus.getValue().toString().toUpperCase());
                    employmentAllowanceStatus.setEnabled(false);
                    editAllowanceStatus.setValue(false);
                    subWindow.getWindow().showNotification("ALLOWANCE STATUS UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR ALLOWANCE STATUS ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(employmentAllowanceStatus, 2, 4);         
        
        // Check box on setting Allowance Status Text field to TRUE
        final CheckBox editAccountNo = new CheckBox("Edit");
        editAccountNo.setEnabled(rowStatus);
        editAccountNo.setImmediate(true);
        grid.addComponent(editAccountNo, 1, 5);
        grid.setComponentAlignment(editAccountNo, Alignment.BOTTOM_LEFT);
        
        // Text field in editting Bank Account #
        final TextField bankAccountNo = new TextField("Bank Account #: ");
        bankAccountNo.setWidth("100%");
        bankAccountNo.setValue(employeesInfoBean.getBankAccountNo());
        bankAccountNo.setEnabled(false);
        bankAccountNo.setImmediate(true);
        editAccountNo.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                bankAccountNo.setEnabled(event.getButton().booleanValue());
            }
            
        });
        bankAccountNo.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(bankAccountNo.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("BANK ACCOUNT # is EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean result = employeesInfoBean.updateAccountNo(bankAccountNo.getValue().toString().trim(), id);
                if(result == true){
                    bankAccountNo.setEnabled(false);
                    editAccountNo.setValue(false);
                    subWindow.getWindow().showNotification("BANK ACCOUNT # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR BANK ACCOUNT # ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(bankAccountNo, 0, 5);
        
        // Check box on setting Allowance for Liquidation Text field to TRUE
        final CheckBox editAfl = new CheckBox("Edit");
        editAfl.setEnabled(rowStatus);
        editAfl.setImmediate(true);
        grid.addComponent(editAfl, 3, 5);
        grid.setComponentAlignment(editAfl, Alignment.BOTTOM_LEFT);
        
        // Text field in editting Allowance for Liquidation
        final TextField afl = new TextField("Allowance for Liquidation: ");
        afl.setWidth("100%");
        afl.setValue(employeesInfoBean.getAllowanceForLiquidation());
        afl.setEnabled(false);
        afl.setImmediate(true);
        editAfl.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                afl.setEnabled(event.getButton().booleanValue());
            }
            
        });
        afl.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(afl.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("Allowance for Liquidation is EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                Boolean checkValue = conUtil.checkInputIfDouble(afl.getValue().toString().trim());
                if(checkValue == false){
                    subWindow.getWindow().showNotification("INCORRECT ENTERED AMOUNT!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = employeesInfoBean.updateAllowanceForLiquidation(afl.getValue().toString().trim(), id);
                if(result == true){
                    afl.setEnabled(false);
                    editAfl.setValue(false);
                    subWindow.getWindow().showNotification("Allowance for Liquidation UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }else{
                    subWindow.getWindow().showNotification("ERROR Allowance for Liquidation ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(afl, 2, 5);
        
        // Specify employment status of employee
        String status;        
        if(rowStatus == true){
            status = "EMPLOYED";
        }else{
            status = employeesInfoBean.getEmployeeStatus().toUpperCase();
        }
        final Label labelStatus = new Label();        
        labelStatus.setWidth("100%");
        labelStatus.setValue("Employee Status: "+status.toUpperCase());
        grid.addComponent(labelStatus, 0, 6);
        grid.setComponentAlignment(labelStatus, Alignment.BOTTOM_CENTER);
        
        // Check box on setting employment status to RESIGNED
        final CheckBox editEmployeeStatus = new CheckBox("Resigned");
        editEmployeeStatus.setEnabled(rowStatus);
        if(GlobalVariables.getUserRole().equals("hr") || GlobalVariables.getUserRole().equals("administrator")){ 
            editEmployeeStatus.setVisible(true); 
        }else{ 
            editEmployeeStatus.setVisible(false); 
        }
        editEmployeeStatus.setImmediate(true);
        editEmployeeStatus.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean enabled = event.getButton().booleanValue();
                if(enabled == true){
                    Window newWindow = addDateResigned(id, labelStatus, editEmployeeStatus, subWindow);
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }                    
                    newWindow.setModal(true);
                    newWindow.center(); 
                }
            }
            
        });
        grid.addComponent(editEmployeeStatus, 1, 6);
        grid.setComponentAlignment(editEmployeeStatus, Alignment.BOTTOM_LEFT);
        
        final CheckBox editEntryDate = new CheckBox("Edit");  
        if(rowStatus == true){
            if(GlobalVariables.getUserRole().equals("hr") || GlobalVariables.getUserRole().equals("administrator")){ 
                editEntryDate.setEnabled(true); 
            }else{ 
                editEntryDate.setEnabled(false); 
            }        
        }else{
            editEntryDate.setEnabled(rowStatus);
        }
        editEntryDate.setImmediate(true);
        grid.addComponent(editEntryDate, 3, 6);
        grid.setComponentAlignment(editEntryDate, Alignment.BOTTOM_LEFT);
        
        final PopupDateField entryDateField = new PopupDateField("Date Employed: ");
        entryDateField.addStyleName("mydate");
        if(employeesInfoBean.getEntryDate() == null){
            entryDateField.setValue(new Date());
        }else{
            entryDateField.setValue(conUtil.parsingDate(employeesInfoBean.getEntryDate()));
        }        
        entryDateField.setDateFormat("yyyy-MM-dd");
        entryDateField.setWidth("100%");
        entryDateField.setEnabled(false);
        entryDateField.setImmediate(true);
        entryDateField.setResolution(DateField.RESOLUTION_DAY);
        editEntryDate.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                entryDateField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        entryDateField.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean result = employeesInfoBean.updateEmploymentEntryDate(conUtil.convertDateFormat(entryDateField.getValue().toString().trim()), id);
                if(result == true){
                    entryDateField.setEnabled(false);
                    editEntryDate.setValue(false);
                    subWindow.getWindow().showNotification("Entry Date UPDATED", Window.Notification.TYPE_TRAY_NOTIFICATION); 
                }else{
                    subWindow.getWindow().showNotification("ERROR Entry Date", Window.Notification.TYPE_ERROR_MESSAGE);
                }
                
            }
            
        });
        grid.addComponent(entryDateField, 2, 6);
        grid.setComponentAlignment(entryDateField, Alignment.MIDDLE_LEFT);
        
        Label removeEmployeeLabel = new Label("Permanently Remove from HRMS");
        removeEmployeeLabel.setWidth("100%");
        grid.addComponent(removeEmployeeLabel, 0, 7);
        grid.setComponentAlignment(removeEmployeeLabel, Alignment.BOTTOM_LEFT);
        
        final CheckBox removeEmployee = new CheckBox("Remove");
        if(GlobalVariables.getUserRole().equals("administrator") || 
                GlobalVariables.getUserRole().equals("hr") || 
                GlobalVariables.getUserRole().equals("accounting")){
            removeEmployee.setVisible(true);
            removeEmployeeLabel.setVisible(true);
        }else{
            removeEmployee.setVisible(false);
            removeEmployeeLabel.setVisible(false);
        }
        removeEmployee.setImmediate(true);
        removeEmployee.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean enabled = event.getButton().booleanValue();
                if(enabled == true){
                    Window newWindow = removeEmployeeFromHRMS(id, removeEmployee, subWindow);
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }                    
                    newWindow.setModal(true);
                    newWindow.center(); 
                }
            }
        });
        grid.addComponent(removeEmployee, 1, 7);
        grid.setComponentAlignment(removeEmployee, Alignment.BOTTOM_LEFT);        
        
        final CheckBox editDependents = new CheckBox("Edit");
        if(rowStatus == true){
            if(GlobalVariables.getUserRole().equals("administrator") || 
                    GlobalVariables.getUserRole().equals("hr") || 
                    GlobalVariables.getUserRole().equals("accounting")){
                editDependents.setVisible(true);
            }else{
                editDependents.setVisible(false);
            }
        }else{
            editDependents.setEnabled(rowStatus);
        }
        editDependents.setImmediate(true);
        grid.addComponent(editDependents, 3, 7);
        grid.setComponentAlignment(editDependents, Alignment.BOTTOM_LEFT);
        
        
        final ComboBox dependents = new ComboBox("# Of Dependent: "+employeesInfoBean.getDependent().toUpperCase());
        dependents.setNullSelectionAllowed(false);
        dependents.setWidth("100%");        
        dependents.addItem("s/me");
        dependents.addItem("s1/me1");
        dependents.addItem("s2/me2");
        dependents.addItem("s3/me3");
        dependents.addItem("s4/me4");
        dependents.setEnabled(false);
        dependents.setImmediate(true);        
        editDependents.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                dependents.setEnabled(event.getButton().booleanValue());
            }
            
        });
        dependents.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Boolean result = employeesInfoBean.updateEmployeeDependents(dependents.getValue().toString(), id);
                if(result == true){
                    dependents.setEnabled(false);
                    editDependents.setValue(false);
                    subWindow.getWindow().showNotification("Employee Dependents UPDATED", Window.Notification.TYPE_TRAY_NOTIFICATION); 
                }else{
                    subWindow.getWindow().showNotification("ERROR Employee Dependents", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(dependents, 2, 7);
                
        return grid;
    }
    
    private Window addDateResigned(final String id, final Label labelStatus, final CheckBox editEmployeeStatus, final Window newWindow){        
        final Window subWindow = new Window("REMOVE EMPLOYEE");
        subWindow.setWidth("250px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        final PopupDateField dateEnd = new PopupDateField();
        dateEnd.addStyleName("mydate");
        dateEnd.setDateFormat("yyyy-MM-dd");
        dateEnd.setWidth("100%");
        dateEnd.setResolution(DateField.RESOLUTION_DAY);
        vlayout.addComponent(dateEnd);
        
        Button removeEmployeeButton = new Button("REMOVE EMPLOYEE?");
        removeEmployeeButton.setWidth("100%");
        removeEmployeeButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                String endDate = conUtil.convertDateFormat(dateEnd.getValue().toString());
                Boolean result = employeesInfoBean.removeEmployeeFromCompany(endDate, id);
                if(result == true){
                    subWindow.getParent().showNotification("Removed Employee from PAYROLL!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    String status = query.getEmploymentStatus(id);
                    labelStatus.setValue("Employee Status: "+status.toUpperCase());
                    editEmployeeStatus.setEnabled(false);
                    (subWindow.getParent()).removeWindow(subWindow);
                    //(newWindow.getParent()).removeWindow(newWindow);                    
                }
            }
            
        });
        vlayout.addComponent(removeEmployeeButton);
        
        subWindow.addComponent(vlayout);
        
        return subWindow;
    }

    private Window removeEmployeeFromHRMS(final String id, final CheckBox removeEmployee, final Window parentWindow){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        final Window subWindow = new Window("PERMANENTLY REMOVE EMPLOYEE", vlayout);
        subWindow.setWidth("255px");
        
        subWindow.addComponent(new Label("This process will remove the Employee "
                +"temporarily from the list of Employees "
                + "for security reasons. To remove permanently, kindly contact your DB Administrator. ", Label.CONTENT_XHTML));        
        
        Button removeButton = new Button("REMOVE EMPLOYEE");
        removeButton.setWidth("100%");
        removeButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean result = queryUpdate.removeEmployee(id);
                if(result == true){
                    removeEmployee.setEnabled(false);
                    (subWindow.getParent()).removeWindow(subWindow);
                    (parentWindow.getParent()).removeWindow(parentWindow);
                }else{
                    subWindow.getParent().showNotification("Employee cannot be REMOVED!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeButton);
        
        return subWindow;
    }
    
    private Table employeeAddressTbl(final String id, final Window subWindow){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employeeAddressTable.removeAllItems();
        employeeAddressTable.setSizeFull();
        employeeAddressTable.setImmediate(true);
        employeeAddressTable.setSelectable(true);
        
        employeeAddressTable.addContainerProperty("id", String.class, null);
        employeeAddressTable.addContainerProperty("address", String.class, null);
        employeeAddressTable.addContainerProperty("address type", String.class, null);
        employeeAddressTable.addContainerProperty("address status", String.class, null);
        
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, CONCAT_WS(', ', street, city, province, zipcode) AS address, type, addressStatus "
                    + "FROM employee_address WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                employeeAddressTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("address"),
                    rs.getString("type"),
                    rs.getString("addressStatus")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(Object listener : employeeAddressTable.getListeners(ItemClickEvent.class)){
            employeeAddressTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employeeAddressTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeeAddressTable.getItem(itemId);
                
                final String rowId = item.getItemProperty("id").getValue().toString();
                
                if(event.getPropertyId().equals("id")){
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("REMOVE ADDRESS", vlayout);
                    newWindow.setWidth("250px");
                    
                    Button button = new Button("DELETE ADDRESS");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = employeeAddressBean.removeAddressStatus(rowId);
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employeeAddressTbl(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
                
                
                if(event.getPropertyId().equals("address status")){                    
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("INSERT STATUS", vlayout);
                    newWindow.setWidth("250px");

                    final ComboBox status = new ComboBox("Address Status: ");
                    status.setWidth("100%");
                    status.setNullSelectionAllowed(false);
                    status.addItem("current");
                    status.addItem("previous");
                    status.addItem("permanent");
                    status.setImmediate(true);
                    newWindow.addComponent(status);

                    Button button = new Button("ADD STATUS");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = employeeAddressBean.updateAddressStatus(rowId, status.getValue().toString());
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employeeAddressTbl(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
            }
            
        });
        
        return employeeAddressTable;
    }
    
    private Table employeeCharacterReferencesTbl(final String id, final Window subWindow){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employeeCharacterReferencesTable.removeAllItems();
        employeeCharacterReferencesTable.setSizeFull();
        employeeCharacterReferencesTable.setImmediate(true);
        employeeCharacterReferencesTable.setSelectable(true);
        
        employeeCharacterReferencesTable.addContainerProperty("id", String.class, null);
        employeeCharacterReferencesTable.addContainerProperty("name", String.class, null);
        employeeCharacterReferencesTable.addContainerProperty("occupation", String.class, null);
        employeeCharacterReferencesTable.addContainerProperty("address", String.class, null);
        employeeCharacterReferencesTable.addContainerProperty("contact no", String.class, null);
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, occupation, address, contactNo FROM employee_character_references "
                    + "WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                employeeCharacterReferencesTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("name").toUpperCase(), 
                    rs.getString("occupation").toUpperCase(), 
                    rs.getString("address").toUpperCase(), 
                    rs.getString("contactNo")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : employeeCharacterReferencesTable.getListeners(ItemClickEvent.class)){
            employeeCharacterReferencesTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employeeCharacterReferencesTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeeCharacterReferencesTable.getItem(itemId);
                
                final String rowId = item.getItemProperty("id").getValue().toString();
                
                if(event.getPropertyId().equals("id")){
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("REMOVE REFERENCE", vlayout);
                    newWindow.setWidth("250px");
                    
                    Button button = new Button("DELETE REFERENCE");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = characterReferencesBean.removeCharacterReference(rowId);
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employeeCharacterReferencesTbl(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
            }
            
        });
        
        return employeeCharacterReferencesTable;
    }

    private Table employeeDependentTbl(final String id, final Window subWindow){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employeeDependentTable.removeAllItems();
        employeeDependentTable.setSizeFull();
        employeeDependentTable.setImmediate(true);
        employeeDependentTable.setSelectable(true);
        
        employeeDependentTable.addContainerProperty("id", String.class, null);
        employeeDependentTable.addContainerProperty("name", String.class, null);
        employeeDependentTable.addContainerProperty("date of birth", String.class, null);
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, dob FROM employee_dependent WHERE employeeId = '"+id+"' ORDER BY dob ASC");
            while(rs.next()){
                employeeDependentTable.addItem(new Object[]{
                    rs.getString("id"),
                    rs.getString("name").toUpperCase(), 
                    rs.getString("dob").toUpperCase()
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : employeeDependentTable.getListeners(ItemClickEvent.class)){
            employeeDependentTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employeeDependentTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeeDependentTable.getItem(itemId);
                
                final String rowId = item.getItemProperty("id").getValue().toString();
                
                if(event.getPropertyId().equals("id")){
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("REMOVE DEPENDENT", vlayout);
                    newWindow.setWidth("250px");
                    
                    Button button = new Button("DELETE DEPENDENT");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = employeeDependentBean.removeDependent(rowId);
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employeeDependentTbl(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
            }
            
        });
        
        return employeeDependentTable;
    }
    
    private Table employeeEducationalBackground(final String id, final Window subWindow){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employeeEducationalBackgroundTable.removeAllItems();
        employeeEducationalBackgroundTable.setSizeFull();
        employeeEducationalBackgroundTable.setImmediate(true);
        employeeEducationalBackgroundTable.setSelectable(true);
        
        employeeEducationalBackgroundTable.addContainerProperty("id", String.class, null);
        employeeEducationalBackgroundTable.addContainerProperty("course", String.class, null);
        employeeEducationalBackgroundTable.addContainerProperty("school", String.class, null);
        employeeEducationalBackgroundTable.addContainerProperty("year graduated", String.class, null);
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, course, school, yearGraduated FROM employee_educational_background "
                    + "WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                employeeEducationalBackgroundTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("course").toUpperCase(), 
                    rs.getString("school").toUpperCase(), 
                    rs.getString("yearGraduated")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : employeeEducationalBackgroundTable.getListeners(ItemClickEvent.class)){
            employeeEducationalBackgroundTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employeeEducationalBackgroundTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeeEducationalBackgroundTable.getItem(itemId);
                
                final String rowId = item.getItemProperty("id").getValue().toString();
                
                if(event.getPropertyId().equals("id")){
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("REMOVE EDUCATION", vlayout);
                    newWindow.setWidth("250px");
                    
                    Button button = new Button("DELETE EDUCATION");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = educationalBackgroundBean.removeEducationalBackground(rowId);
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employeeEducationalBackground(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
            }
            
        });
        
        return employeeEducationalBackgroundTable;
    }

    private Table employeePositionHistory(final String id, final Window subWindow){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employeePositionHistoryTable.removeAllItems();
        employeePositionHistoryTable.setSizeFull();
        employeePositionHistoryTable.setImmediate(true);
        employeePositionHistoryTable.setSelectable(true);
        
        employeePositionHistoryTable.addContainerProperty("id", String.class, null);
        employeePositionHistoryTable.addContainerProperty("position", String.class, null);
        employeePositionHistoryTable.addContainerProperty("corporate", String.class, null);
        employeePositionHistoryTable.addContainerProperty("trade", String.class, null);
        employeePositionHistoryTable.addContainerProperty("branch", String.class, null);
        employeePositionHistoryTable.addContainerProperty("department", String.class, null);
        employeePositionHistoryTable.addContainerProperty("entry date", String.class, null);
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT eph.id AS id, eph.position AS position, cn.name AS corporate, tn.name AS trade, "
                    + "b.name AS branch, eph.department AS department, eph.entryDate AS entryDate FROM employee_position_history eph "
                    + "INNER JOIN branch b ON eph.branchId = b.id INNER JOIN trade_name tn ON b.tradeId = tn.id INNER JOIN "
                    + "corporate_name cn ON tn.corporateId = cn.id WHERE eph.employeeId = '"+id+"' ORDER BY eph.entryDate DESC");
            while(rs.next()){
                employeePositionHistoryTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("position").toUpperCase(), 
                    rs.getString("corporate").toUpperCase(), 
                    rs.getString("trade").toUpperCase(), 
                    rs.getString("branch").toUpperCase(), 
                    rs.getString("department").toUpperCase(), 
                    rs.getString("entryDate")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : employeePositionHistoryTable.getListeners(ItemClickEvent.class)){
            employeePositionHistoryTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employeePositionHistoryTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeePositionHistoryTable.getItem(itemId);
                
                final String rowId = item.getItemProperty("id").getValue().toString();
                
                if(event.getPropertyId().equals("id")){
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("REMOVE POSITION", vlayout);
                    newWindow.setWidth("250px");
                    
                    Button button = new Button("DELETE POSITION");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = positionHistoryBean.removePositionHistory(rowId);
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employeePositionHistory(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
            }
            
        });
        
        return employeePositionHistoryTable;
    }
    
    private Table employmentRecord(final String id, final Window subWindow){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        employmentRecordTable.removeAllItems();
        employmentRecordTable.setSizeFull();
        employmentRecordTable.setImmediate(true);
        employmentRecordTable.setSelectable(true);
        
        employmentRecordTable.addContainerProperty("id", String.class, null);
        employmentRecordTable.addContainerProperty("position", String.class, null);
        employmentRecordTable.addContainerProperty("company", String.class, null);
        employmentRecordTable.addContainerProperty("start date", String.class, null);
        employmentRecordTable.addContainerProperty("end date", String.class, null);
        
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employment_record WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                employmentRecordTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("position").toUpperCase(), 
                    rs.getString("company").toUpperCase(),
                    rs.getString("startDate"), 
                    rs.getString("endDate")
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesEditableInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : employmentRecordTable.getListeners(ItemClickEvent.class)){
            employmentRecordTable.removeListener(ItemClickEvent.class, listener);
        }
        
        employmentRecordTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employmentRecordTable.getItem(itemId);
                
                final String rowId = item.getItemProperty("id").getValue().toString();
                
                if(event.getPropertyId().equals("id")){
                    VerticalLayout vlayout = new VerticalLayout();
                    vlayout.setMargin(true);
                    vlayout.setSpacing(true);

                    final Window newWindow = new Window("REMOVE RECORD", vlayout);
                    newWindow.setWidth("250px");
                    
                    Button button = new Button("DELETE RECORD");
                    button.setWidth("100%");
                    button.addListener(new Button.ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent event) {
                            Boolean result = employmentRecordsBean.removeEmploymentRecord(rowId);
                            if(result == true){
                                (subWindow.getParent()).removeWindow(newWindow);
                                employmentRecord(id, subWindow);
                            }
                        }

                    });
                    newWindow.addComponent(button);
                    
                    if(newWindow.getParent() == null){
                        subWindow.getApplication().getMainWindow().addWindow(newWindow);
                    }
                    newWindow.setModal(true);
                    newWindow.center();
                }
            }
            
        });
        
        return employmentRecordTable;
    }
    
}
