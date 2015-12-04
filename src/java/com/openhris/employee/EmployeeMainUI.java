/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.model.Employee;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.List;

/**
 *
 * @author jet
 */
public class EmployeeMainUI extends VerticalLayout {
    
    Table employeesTbl = new Table();
            
    EmployeeService employeeService = new EmployeeServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();    
    int corporateId;
    int tradeId;
    int branchId;
    
    ComboBox corporation;
    ComboBox trade;
    ComboBox branch;
    ComboBox employeesName = new ComboBox();
    
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    private String userRole; 
    HorizontalSplitPanel hsplit;
    VerticalLayout vlayout = new VerticalLayout();
    Label errorLabel = new Label("<p style=\"color: red\">*Duplicate Entry!</p>", Label.CONTENT_RAW);
    
    public EmployeeMainUI(){}
    
    public EmployeeMainUI(final String userRole, int branchId){
        
        this.userRole = userRole;
        this.branchId = branchId;
                
        setSpacing(true);
	setSizeFull();
        
        employeesTable(getEmployeeList(getBranchId()));
        
	hsplit = new HorizontalSplitPanel();        
        hsplit.addStyleName("small blue white");
        hsplit.setLocked(true);
        hsplit.setSplitPosition(33, Sizeable.UNITS_PERCENTAGE);
	hsplit.setFirstComponent(employeesTbl);
	hsplit.setSecondComponent(new EmployeeInformationUI(null, null));
	
	hsplit.setSizeFull();
        addComponent(hsplit);
        setExpandRatio(hsplit, 1.0f);
                                
    }
        
    public void employeesTable(List<Employee> employeeList){
        employeesTbl.removeAllItems();
        employeesTbl.setSizeFull();
        employeesTbl.setSelectable(true);
	employeesTbl.addStyleName("employees-table-layout");
        
        employeesTbl.addContainerProperty("employee id", String.class, null);
        employeesTbl.addContainerProperty("name", String.class, null);
        
        int i = 0; 
        for(Employee e : employeeList){
            String name = e.getLastname()+", "+e.getFirstname()+" "+e.getMiddlename();
            employeesTbl.addItem(new Object[]{
                e.getEmployeeId(), 
                name.toUpperCase() 
            }, i);
            i++;
        }
        employeesTbl.setPageLength(25);
        
        for(Object listener : employeesTbl.getListeners(ItemClickEvent.class)){
            employeesTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        employeesTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeesTbl.getItem(itemId);
                
                hsplit.setSecondComponent(new EmployeeInformationUI(item.getItemProperty("employee id").getValue().toString(), getApplication()));                
            }
        });
        
        employeesTbl.setImmediate(true);
    }
        
    public int getBranchId(){
        return branchId;
    }
    
    public String getUserRole(){
        return userRole;
    }
    
    public List<Employee> getEmployeeList(int branchId){
        return employeeService.getEmployeePerBranchMainView(branchId);
    }    
}
