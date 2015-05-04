/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Address;
import com.openhris.service.EmployeeAddressService;
import com.openhris.serviceprovider.EmployeeAddressServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
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
public class EmployeeAddress extends VerticalLayout {
    
    OpenHrisUtilities util = new OpenHrisUtilities();
    DropDownComponent dropDown = new DropDownComponent();
    EmployeeAddressService eaService = new EmployeeAddressServiceImpl();
    
    private String employeeId;
    GridLayout glayout;
    Table employeeAddressTbl = new Table();
    
    ComboBox addressType;
    TextField streetField;
    TextField cityField;
    TextField provinceField;
    TextField zipCodeField;
    
    Object addressTypeId;
    int classAddressId = 0;
    
    public EmployeeAddress(String employeeId){
        this.employeeId = employeeId;
        
        init();
        addComponent(layout());
        setComponentAlignment(glayout, Alignment.TOP_CENTER);
        employeeAddressTable();
        addComponent(employeeAddressTbl);
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
        glayout.setWidth("600px");
	glayout.setHeight("100%");
        
        addressType = dropDown.populateAddressType(new ComboBox());
        addressType.setWidth("200px");
        glayout.addComponent(addressType, 0, 0);
        
        streetField = createTextField("Street: ");
        streetField.setWidth("388px");
        glayout.addComponent(streetField, 1, 0, 2, 0);
        
        cityField = createTextField("City/Town: ");
        cityField.setWidth("200px");
        glayout.addComponent(cityField, 0, 1);
        
        provinceField = createTextField("Province: ");
        glayout.addComponent(provinceField, 1, 1);
        
        zipCodeField = createTextField("Zip Code: ");
        glayout.addComponent(zipCodeField, 2, 1);
                
        Button submitBtn = new Button("ADD/UPDATE");
        submitBtn.setWidth("100%");
        submitBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(getEmployeeId() == null){
                    getWindow().showNotification("You did not Select an Employee!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(addressType.getValue() == null){
                    getWindow().showNotification("Select Address type!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(streetField.getValue() == null || streetField.getValue().toString().trim().isEmpty() || 
                        cityField.getValue() == null || cityField.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Street/City/Town is Required!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                Address address = new Address();
                address.setAddressId(classAddressId);
                address.setEmployeeId(getEmployeeId());
                
                if(util.checkInputIfInteger(addressType.getValue().toString())){
                    address.setType(addressType.getItemCaption(addressTypeId));
                } else {
                    address.setType(addressType.getValue().toString().trim().toLowerCase());
                }
                
                
                address.setStreet(streetField.getValue().toString().trim().toLowerCase());
                address.setCity(cityField.getValue().toString().trim().toLowerCase());
                address.setProvince((provinceField.getValue() == null) ? " " : provinceField.getValue().toString().trim().toLowerCase());
                address.setZipcode((zipCodeField.getValue() == null) ? " " : zipCodeField.getValue().toString().trim().toLowerCase());
                
                boolean result = eaService.insertEmployeeAddress(address);
                if(result){
                    employeeAddressTable();
                    clearFields();
                } else {
                    getWindow().showNotification("Error on Employee Address SQL", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        glayout.addComponent(submitBtn, 1, 2, 2, 2);
        
        return glayout;
    }
    
    public Table employeeAddressTable(){   
        employeeAddressTbl.removeAllItems();
        employeeAddressTbl.setSizeFull();
        employeeAddressTbl.setSelectable(true);
        employeeAddressTbl.setImmediate(true);        
        employeeAddressTbl.setStyleName("employees-table-layout");
        
        employeeAddressTbl.addContainerProperty("id", String.class, null);
        employeeAddressTbl.addContainerProperty("type", String.class, null);
        employeeAddressTbl.addContainerProperty("address", String.class, null);
        
        List<Address> addressList = eaService.getEmployeeAddress(getEmployeeId());
        int i = 0;
        for(Address a : addressList){
            employeeAddressTbl.addItem(new Object[]{
                a.getAddressId(), 
                a.getType(), 
                a.getStreet()+", "+a.getCity()+", "+a.getProvince()+" "+a.getZipCode()
            }, i);
            i++;
        }
        employeeAddressTbl.setPageLength(3);
        
        for(Object listener : employeeAddressTbl.getListeners(ItemClickEvent.class)){
            employeeAddressTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        employeeAddressTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeeAddressTbl.getItem(itemId);
                
                int addressId = util.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                
                if(event.getPropertyId().equals("id")){
                    Window window = removeAddressWindow(addressId);
                    if(window.getParent() == null){
                        getWindow().addWindow(window);
                    } 
                    window.center();
                }
                
                if(event.getPropertyId().equals("type") || event.getPropertyId().equals("address")){
                    Address address = eaService.getEmployeeAddressbyId(addressId);
                    classAddressId = addressId;
                                        
                    addressType.removeAllItems();
                    addressTypeId = addressType.addItem();
                    addressType.setItemCaption(addressTypeId, address.getType());
                    addressType.setValue(addressTypeId);
                    if(address.getType().equals("city address")){
                        addressType.addItem("home address");
                    } else {
                        addressType.addItem("city address");
                    }
                    
                    streetField.setValue(address.getStreet());
                    cityField.setValue(address.getCity());
                    provinceField.setValue(address.getProvince());
                    zipCodeField.setValue(address.getZipCode());
                }
            }
        });
        
        return employeeAddressTbl;
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

    private Window removeAddressWindow(final int addressId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window window = new Window("REMOVE POSITION", vlayout);
        window.setWidth("300px");        
        
        Button removeBtn = new Button("Remove Address?");
        removeBtn.setWidth("100%");
        removeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = eaService.removeEmployeeAddress(addressId);
                if(result){
                    employeeAddressTable();
                    (window.getParent()).removeWindow(window);
                    clearFields();
                } else {
                    getWindow().showNotification("Cannot Remove Address, Contact your DBA!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        removeBtn.setImmediate(true);
        
        window.addComponent(removeBtn);
        
        return window;
    }
    
    private void clearFields(){
        streetField.setValue("");
        cityField.setValue("");
        provinceField.setValue("");
        zipCodeField.setValue("");
        classAddressId = 0;
    }
    
}
