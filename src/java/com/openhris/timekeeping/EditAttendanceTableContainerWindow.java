/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.payroll.ProcessPayrollComputation;
import com.openhris.timekeeping.model.Timekeeping;
import com.openhris.timekeeping.service.TimekeepingService;
import com.openhris.timekeeping.serviceprovider.TimekeepingServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class EditAttendanceTableContainerWindow extends Window {

    OpenHrisUtilities utilities = new OpenHrisUtilities();
    TimekeepingComputation computation = new TimekeepingComputation();
    TimekeepingService timekeepingService = new TimekeepingServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();
    
    private String name;
    private List dateList;
    private String employeeId;
    private String payrollPeriod;
    private String payrollDate;
    private String attendancePeriodFrom;
    private String attendancePeriodTo;
    private String employmentWageEntry;
    private double employmentWage;
    private int branchId;
    private int payrollId;
    
    private double premiumRate;
    DecimalFormat df = new DecimalFormat("0.00");
    
    public EditAttendanceTableContainerWindow(String name, 
            List dateList, 
            String employeeId, 
            String payrollPeriod, 
            String payrollDate, 
            String attendancePeriodFrom, 
            String attendancePeriodTo, 
            String employmentWageEntry, 
            double employmentWage, 
            int branchId, 
            int payrollId) {
    
        this.name = name;
        this.dateList = dateList;
        this.employeeId = employeeId;
        this.payrollPeriod = payrollPeriod;
        this.payrollDate = payrollDate;
        this.attendancePeriodFrom = attendancePeriodFrom;
        this.attendancePeriodTo = attendancePeriodTo;
        this.employmentWageEntry = employmentWageEntry;
        this.employmentWage = employmentWage;
        this.branchId = branchId;
        this.payrollId = payrollId;
        
        setCaption("ATTENDANCE TABLE for "+getName());
        setSizeFull();
        setModal(true);
        center();
        
        addComponent(generateAttendanceTable());
    }

    VerticalLayout generateAttendanceTable(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSizeFull();
        vlayout.setSpacing(true);
        
        final Table table = new Table();
        table.removeAllItems();
        table.setEnabled(true);
        table.setWidth("100%");
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
                
        table.addContainerProperty("edit", CheckBox.class, null);
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("policy", String.class, null);
        table.addContainerProperty("holidays", String.class, null); 
        table.addContainerProperty("premium", CheckBox.class, null); 
        table.addContainerProperty("lates", TextField.class, null);  
        table.addContainerProperty("undertime", TextField.class, null); 
        table.addContainerProperty("overtime", TextField.class, null);        
        table.addContainerProperty("night differential", TextField.class, null);  
        table.addContainerProperty("l/min", Double.class, null); 
        table.addContainerProperty("u/min", Double.class, null); 
        table.addContainerProperty("o/min", Double.class, null);
        table.addContainerProperty("nd/min", Double.class, null);
        table.addContainerProperty("lholiday", Double.class, null);
        table.addContainerProperty("sholiday", Double.class, null);
        table.addContainerProperty("wdo", Double.class, null);
        table.addContainerProperty("psday", Double.class, null); //paid non-working holiday
        
        table.setColumnAlignment("edit", Table.ALIGN_CENTER);
        table.setColumnAlignment("date", Table.ALIGN_CENTER);
        table.setColumnAlignment("policy", Table.ALIGN_CENTER);
        table.setColumnAlignment("premium", Table.ALIGN_CENTER);
        table.setColumnAlignment("lates", Table.ALIGN_CENTER);
        table.setColumnAlignment("undertime", Table.ALIGN_CENTER);
        table.setColumnAlignment("overtime", Table.ALIGN_CENTER);
        table.setColumnAlignment("night differential", Table.ALIGN_CENTER);
        table.setColumnAlignment("l/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("u/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("o/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("nd/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("lholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("sholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        table.setColumnAlignment("psday", Table.ALIGN_RIGHT);
                
        final String[] holidayList = {"legal-holiday", "special-holiday"};
        for(int i = 0; i < getDateList().size(); i++){ 
            Object itemId = new Integer(i);
            
            final CheckBox edit = new CheckBox();
            edit.setData(i);
            edit.setImmediate(true);
            
            final CheckBox premium = new CheckBox();
            premium.setData(itemId);
            premium.setEnabled(false);
            premium.setImmediate(true);
            
            final TextField lates = new TextField();
            lates.setWidth("50px");
            lates.setValue("0");
            lates.setEnabled(true);
            lates.setData(itemId);
            lates.setEnabled(false);
            lates.setImmediate(true);
                        
            final TextField undertime = new TextField();
            undertime.setWidth("50px");
            undertime.setValue("0");
            undertime.setEnabled(true);
            undertime.setData(itemId);
            undertime.setEnabled(false);
            undertime.setImmediate(true);
                        
            final TextField overtime = new TextField();
            overtime.setWidth("50px");
            overtime.setValue("0");
            overtime.setEnabled(true);
            overtime.setData(itemId);
            overtime.setEnabled(false);
            overtime.setImmediate(true);
            
            final TextField nightDifferential = new TextField();
            nightDifferential.setWidth("50px");
            nightDifferential.setValue("0");
            nightDifferential.setEnabled(true);
            nightDifferential.setData(itemId);
            nightDifferential.setEnabled(false);
            nightDifferential.setImmediate(true);
                       
            edit.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    premium.setEnabled(event.getButton().booleanValue());
                    lates.setEnabled(event.getButton().booleanValue());
                    undertime.setEnabled(event.getButton().booleanValue());
                    overtime.setEnabled(event.getButton().booleanValue());
                    nightDifferential.setEnabled(event.getButton().booleanValue());
                }
            });
            
            premium.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                    
                    lates.setValue("0");
                    undertime.setValue("0");
                    overtime.setValue("0");
                    nightDifferential.setValue("0");                    
                    
                    item.getItemProperty("l/min").setValue(0.0);
                    item.getItemProperty("u/min").setValue(0.0);
                    item.getItemProperty("o/min").setValue(0.0);
                    item.getItemProperty("nd/min").setValue(0.0);
                    
                    if(event.getButton().booleanValue() == true){
                        premiumRate = 0.1;                        
                    } else {
                        premiumRate = 0.0;
                    }
                    
                    item.getItemProperty("wdo").setValue(
                        df.format(Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) * premiumRate))
                    );
                    
                    item.getItemProperty("lholiday").setValue(
                        df.format(Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) * premiumRate))
                    );
                    
                    item.getItemProperty("sholiday").setValue(
                        df.format(Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) * premiumRate))
                    );
                }
            });
            
            lates.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double lateDeduction;
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for lates!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        lateDeduction = computation.processEmployeesLates(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), getEmploymentWage());
                        item.getItemProperty("l/min").setValue(df.format(lateDeduction + (lateDeduction*premiumRate)));
                    }else{
                        item.getItemProperty("l/min").setValue(0.0);
                    }
                    
                }
            });
            
            undertime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double undertimeDeduction;
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        undertimeDeduction = computation.processEmployeesUndertime(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), getEmploymentWage());
                        item.getItemProperty("u/min").setValue(df.format(undertimeDeduction + (undertimeDeduction*premiumRate)));
                    }else{
                        item.getItemProperty("u/min").setValue(0.0);
                    }
                }
            });
            
            overtime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double overtimeAddition;
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        overtimeAddition = computation.processEmployeesOvertime(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), getEmploymentWage());
                        item.getItemProperty("o/min").setValue(df.format(overtimeAddition + (overtimeAddition*premiumRate)));
                    }else{
                        item.getItemProperty("o/min").setValue(0.0);
                    }
                }
            });
            
            nightDifferential.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double nightDifferentialAddition;
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        nightDifferentialAddition = computation.processEmployeesNightDifferential(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), getEmploymentWage());
                        item.getItemProperty("nd/min").setValue(df.format(nightDifferentialAddition + (nightDifferentialAddition*premiumRate)));
                    }else{
                        item.getItemProperty("nd/min").setValue(0.0);
                    }
                }
            });
            
            List<Timekeeping> timekeepingListByRowData = timekeepingService.getTimekeepingRowData(
                utilities.convertDateFormat(getDateList().get(i).toString()), 
                payrollId);
            for(Timekeeping t : timekeepingListByRowData){
                String policy;
                String holiday;
                if(!t.getPolicy().equals("null")){
                    policy = t.getPolicy();
                } else {
                    policy = "";
                }
                    
                if(!t.getHoliday().equals("null")){
                    holiday = t.getHoliday();
                } else {
                    holiday = "";
                }
                    
                premium.setValue(t.isPremium());
                    
                lates.setValue(t.getLates());
                undertime.setValue(t.getUndertime());
                overtime.setValue(t.getOvertime());
                nightDifferential.setValue(t.getNightDifferential());
                    
                table.addItem(new Object[]{
                    edit, 
                    utilities.convertDateFormat(dateList.get(i).toString()), 
                    policy, holiday, 
                    premium, lates, 
                    undertime, overtime, 
                    nightDifferential, 
                    t.getLateDeduction(), 
                    t.getUndertimeDeduction(), 
                    t.getOvertimePaid(), 
                    t.getNightDifferentialPaid(), 
                    t.getLegalHolidayPaid(), 
                    t.getSpecialHolidayPaid(), 
                    t.getWorkingDayOffPaid(), 
                    t.getNonWorkingHolidayPaid()
                }, new Integer(i));
            }                       
        }                
        table.setPageLength(table.size());        
        
        for(Object listener : table.getListeners(ItemClickEvent.class)){
            table.removeListener(ItemClickEvent.class, listener);
        }
        
        table.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                final Object itemId = event.getItemId();
                final Item item = table.getItem(itemId);
                
                Boolean editRow = Boolean.valueOf(item.getItemProperty("edit").getValue().toString());
                
                if(event.getPropertyId().equals("policy")){
                    if(editRow){
//                        final Window newWindow = policyWindow(holidayList, item);
//                        if(newWindow.getParent() == null){
//                            getApplication().getMainWindow().addWindow(newWindow);
//                        }      
                        
                        Window sub = new AttendancePolicyWindow(holidayList, 
                                item, 
                                getEmploymentWageEntry(), 
                                getEmploymentWage());
                        if(sub.getParent() == null){
                            getApplication().getMainWindow().addWindow(sub);
                        }
                    }else{
                        getWindow().showNotification("Click Edit!", Window.Notification.TYPE_WARNING_MESSAGE);
                    }                    
                }
            }
        });
        
        vlayout.addComponent(table);
        
        final Button button = new Button();
        button.setCaption("Save Attendance Data");
        
        
        for(Object listener : button.getListeners(Button.ClickListener.class)){
            button.removeListener(Button.ClickListener.class, listener);
        }
        
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {                
                try{                                       
                    Collection attendanceTableCollection = table.getContainerDataSource().getItemIds();
                    List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
                    for(int i = 0; i < attendanceTableCollection.size(); i++){
                        String str = table.getItem(i).toString();
                        String[] attStr = str.split(" ");
                        List<String> tkeepList = new ArrayList<String>(Arrays.asList(attStr));
			
                        Timekeeping t = new Timekeeping();
                        t.setAttendanceDate(utilities.parsingDate(tkeepList.get(1)));
                        t.setPolicy(tkeepList.get(2));
                        t.setHoliday(tkeepList.get(3));
                        t.setPremium(utilities.convertStringToBoolean(tkeepList.get(4)));
                        t.setLates(utilities.convertStringToDouble(tkeepList.get(5)));
                        t.setUndertime(utilities.convertStringToDouble(tkeepList.get(6)));
                        t.setOvertime(utilities.convertStringToDouble(tkeepList.get(7)));
                        t.setNightDifferential(utilities.convertStringToDouble(tkeepList.get(8)));
                        t.setLateDeduction(utilities.convertStringToDouble(tkeepList.get(9)));
                        t.setUndertimeDeduction(utilities.convertStringToDouble(tkeepList.get(10)));
                        t.setOvertimePaid(utilities.convertStringToDouble(tkeepList.get(11)));
                        t.setNightDifferentialPaid(utilities.convertStringToDouble(tkeepList.get(12)));
                        t.setLegalHolidayPaid(utilities.convertStringToDouble(tkeepList.get(13)));
                        t.setSpecialHolidayPaid(utilities.convertStringToDouble(tkeepList.get(14)));
                        t.setWorkingDayOffPaid(utilities.convertStringToDouble(tkeepList.get(15)));
                        t.setNonWorkingHolidayPaid(utilities.convertStringToDouble(tkeepList.get(16)));
                        attendanceList.add(t);
                    }
                    ProcessPayrollComputation processPayroll = new ProcessPayrollComputation(getEmployeeId(), getBranchId());
                    processPayroll.initVariables();
                    processPayroll.initVariablesForComputation(attendanceList);
                    boolean result = processPayroll.processPayrollComputation(payrollDate, 
                            payrollPeriod, 
                            attendancePeriodFrom, 
                            attendancePeriodTo, 
                            getPayrollId());
                    if(result){
                        close();  
                    } else {
                        getWindow().showNotification("SQL ERROR");
                    }
                }catch(Exception e){
                    e.getMessage();
                }
            }
            
        });
               
        vlayout.addComponent(button);
        
        return vlayout;
    }
    
//    private Window policyWindow(final String[] holidayList, final Item item){
//        VerticalLayout vlayout = new VerticalLayout();
//        vlayout.setSpacing(true);
//        vlayout.setMargin(true);
//        
//        final Window subWindow = new Window("CHANGE POLICY", vlayout);
//        subWindow.setWidth("225px");
//                
//        final ComboBox policy = dropDown.populateAttendancePolicyDropDownList(new ComboBox());
//        policy.setWidth("100%");
//        policy.setNullSelectionAllowed(true);              
//        policy.setImmediate(true);
//        subWindow.addComponent(policy);
//        
//        final ComboBox holidayType = new ComboBox("Type: ");
//        holidayType.setWidth("100%");
//        holidayType.setNullSelectionAllowed(false);
//        holidayType.setVisible(false);
//        holidayType.setImmediate(true);
//        for(String temp : holidayList){
//            holidayType.addItem(temp);
//        }
//        policy.addListener(new Property.ValueChangeListener() {
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                if(event.getProperty().getValue() == null){  
//                    holidayType.setVisible(false);
//                    holidayType.removeAllItems();
//                    for(String temp : holidayList){
//                        holidayType.addItem(temp);
//                    }
//                } else if(event.getProperty().getValue().toString().equals("holiday") || 
//                        event.getProperty().getValue().toString().equals("working-holiday")){
//                    holidayType.setVisible(true);
//                } else if(event.getProperty().getValue().toString().equals("working-day-off")){
//                    double additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
//                    item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
//                } else{
//                    holidayType.removeAllItems();
//                    for(String temp : holidayList){
//                        holidayType.addItem(temp);
//                    }
//                    holidayType.setVisible(false);
//                }
//            }
//            
//        });
//        holidayType.addListener(new Property.ValueChangeListener() {
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                double additionalHolidayPay;
//                if(policy.getValue() == null){                    
//                } else if(policy.getValue().equals("working-holiday")){
//                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
//                        additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
//                        item.getItemProperty("lholiday").setValue(new Double(df.format(additionalHolidayPay)));
//                        item.getItemProperty("sholiday").setValue(0.0);
//                    }else{
//                        additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
//                        item.getItemProperty("sholiday").setValue(new Double(df.format(additionalHolidayPay)));
//                        item.getItemProperty("lholiday").setValue(0.0);
//                    }
//                }else if(policy.getValue().equals("holiday")){
//                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
//                        additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
//                        item.getItemProperty("psday").setValue(new Double(df.format(additionalHolidayPay)));                            
//                    }else{
//                        item.getItemProperty("psday").setValue(0.0);
//                    }
//                }
//            }
//            
//        });
//        subWindow.addComponent(holidayType);
//        
//        Button button = new Button("UPDATE POLICY");
//        button.setWidth("100%");
//        button.addListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                String policyStr;
//                if(policy.getValue() == null || policy.getValue().toString().isEmpty()){
//                    policyStr = "";
//                }else{
//                    policyStr = policy.getValue().toString();
//                }
//                if(policyStr.equals("holiday") || policyStr.equals("working-holiday")){
//                    if(holidayType.getValue() == null){
//                        getWindow().showNotification("Select a Holiday type!", Window.Notification.TYPE_ERROR_MESSAGE);
//                        return;
//                    }
//                }
//                item.getItemProperty("policy").setValue(policyStr);
//                item.getItemProperty("holidays").setValue(holidayType.getValue());                
//                
//                (subWindow.getParent()).removeWindow(subWindow);
//            }
//            
//        });
//        subWindow.addComponent(button);
//        
//        return subWindow;
//    }
    
    public List getDateList() {
        return dateList;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPayrollPeriod() {
        return payrollPeriod;
    }

    public String getPayrollDate() {
        return payrollDate;
    }

    public String getAttendancePeriodFrom() {
        return attendancePeriodFrom;
    }

    public String getAttendancePeriodTo() {
        return attendancePeriodTo;
    }

    public String getEmploymentWageEntry() {
        return employmentWageEntry;
    }

    public double getEmploymentWage() {
        return employmentWage;
    }

    public int getBranchId() {
        return branchId;
    }
    
    public int getPayrollId(){
        return payrollId;
    }
}
