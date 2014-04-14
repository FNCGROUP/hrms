/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.openhris.administrator.model.UserAccessControl;
import com.hrms.company.events.BasicEventBean;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
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
public class CompanyEventModule extends VerticalLayout{
    
    BasicEventBean basicEvent = new BasicEventBean();
    GetSQLQuery query = new GetSQLQuery();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    Table eventTable = new Table();
    
    private Date dateStart;
    private Date dateEnd;
    private boolean useSecondResolution = true;
    private String username;
    private String userRole;
    
    public String queryEvent = "SELECT ce.id AS id, ce.eventType AS type, ce.caption AS caption, ce.description AS description, "
                    + "ce.location AS location, ce.startDate AS startDate, ce.endDate AS endDate, ce.color AS color, "
                    + "CONCAT_WS(' - ', DATE_FORMAT(ce.startDate, '%W, %D %M %Y'), DATE_FORMAT(ce.endDate, '%W, %D %M %Y')) AS date, "
                    + "CONCAT_WS(' - ', TIME(ce.startDate), TIME(ce.endDate)) AS time, CONCAT_WS(' ', e.firstname, e.lastname) AS postedBy, "
                    + "ce.datePosted AS datePosted FROM calendar_events ce INNER JOIN employee e ON ce.employeeId = e.employeeId  ";
    
    private String eventTypeSchedule;
    private String startDateSchedule;
    private String endDateSchedule;
    private String captionSchedule;
    private String locationSchedule;
    private String descriptionSchedule;
    private String styleNameSchedule;
    
    public CompanyEventModule(String username, String userRole){
        this.username = username;
        this.userRole = userRole;
        
        setSpacing(true);
        eventTbl(queryEvent);
        
        addComponent(eventTable);
        
        GridLayout grid = new GridLayout(6, 1);
        grid.setSpacing(true);
                
        Label filterEventTypeLabel = new Label("Filter by Event Type: ");
        grid.addComponent(filterEventTypeLabel, 0, 0);
        grid.setComponentAlignment(filterEventTypeLabel, Alignment.MIDDLE_CENTER);
        
        final Select eventType = createEventTypelect();
        eventType.setWidth("200px");
        eventType.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String queryFilter = "WHERE ce.eventType = '"+eventType.getValue().toString()+"' ORDER BY ce.startDate DESC ";
                String filteredQuery = queryEvent + queryFilter;
                eventTbl(filteredQuery);
            }
            
        });
        eventType.setImmediate(true);
        grid.addComponent(eventType, 1, 0);
        grid.setComponentAlignment(eventType, Alignment.MIDDLE_LEFT);
        
        Label filterDateLabel = new Label("Filter by Date(Month): ");
        grid.addComponent(filterDateLabel, 2, 0);
        grid.setComponentAlignment(filterDateLabel, Alignment.MIDDLE_CENTER);
        
        final PopupDateField dateFilter = new PopupDateField();
        dateFilter.addStyleName("mydate");
        dateFilter.setDateFormat("MMMM yyyy");
        dateFilter.setWidth("150px");
        dateFilter.setResolution(DateField.RESOLUTION_MONTH);
        grid.addComponent(dateFilter, 3, 0);
        grid.setComponentAlignment(dateFilter, Alignment.MIDDLE_LEFT);
        
        Button filterByMonth = new Button("FILTER");
        filterByMonth.setWidth("120px");
        filterByMonth.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                String queryFilter = "WHERE DATE_FORMAT(ce.startDate, '%Y-%m') = '"+conUtil.convertDateFormatYearMonth(dateFilter.getValue().toString()) +"' ";
                String filteredQuery = queryEvent + queryFilter;
                eventTbl(filteredQuery);
            }
            
        });
        grid.addComponent(filterByMonth, 4, 0);
        grid.setComponentAlignment(filterByMonth, Alignment.MIDDLE_LEFT);
        
        Button refreshButton = new Button("REFRESH");
        refreshButton.setWidth("250px");
        refreshButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                eventTbl(queryEvent);
            }
            
        });
        grid.addComponent(refreshButton, 5, 0);
        grid.setComponentAlignment(refreshButton, Alignment.MIDDLE_LEFT);
        
        addComponent(grid);
    }
    
    private Table eventTbl(String query){
        final GetSQLConnection getConnection = new GetSQLConnection();
        final Connection conn = getConnection.connection();
        eventTable.setSizeFull();
        eventTable.setPageLength(20);
        eventTable.setSelectable(true);
        eventTable.setImmediate(true);
        
        eventTable.setStyleName("striped");
        eventTable.removeAllItems();
        eventTable.addContainerProperty("id", String.class, null);
        eventTable.addContainerProperty("type", String.class, null);
        eventTable.addContainerProperty("caption", String.class, null);
        eventTable.addContainerProperty("description", String.class, null);
        eventTable.addContainerProperty("location", String.class, null);
        eventTable.addContainerProperty("date", String.class, null);
        eventTable.addContainerProperty("time", String.class, null);
        eventTable.addContainerProperty("posted by", String.class, null);
        eventTable.addContainerProperty("date posted", String.class, null);
        try {
            Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                eventTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("type"), 
                    rs.getString("caption"), 
                    rs.getString("description"), 
                    rs.getString("location"), 
                    rs.getString("date"), 
                    rs.getString("time"), 
                    rs.getString("postedBy"), 
                    rs.getString("datePosted")},
                    new Integer(i)
                );
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompanyEventModule.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyEventModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(Object listener : eventTable.getListeners(ItemClickEvent.class)){
            eventTable.removeListener(ItemClickEvent.class, listener);
        }
        
        eventTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = eventTable.getItem(itemId);
                Connection newConn = getConnection.connection();
                
                if(event.getPropertyId().equals("caption")){
                    if(userRole.equals("administrator")){
                        getWindow().showNotification("You are not allowed to add events", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(UserAccessControl.isAddEvents()== false){                       
                        getWindow().showNotification("You are not allowed to Edit Events!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    String rowId = item.getItemProperty("id").toString();
                    try {
                        Statement stmt = newConn.createStatement();
                        ResultSet rs = stmt.executeQuery(queryEvent+" WHERE ce.id = '"+rowId+"'");
                        while(rs.next()){
                            eventTypeSchedule = rs.getString("type");
                            startDateSchedule = rs.getString("startDate");
                            endDateSchedule = rs.getString("endDate");
                            captionSchedule = rs.getString("caption");
                            locationSchedule = rs.getString("location");
                            descriptionSchedule = rs.getString("description");
                            styleNameSchedule = rs.getString("color");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CompanyEventModule.class.getName()).log(Level.SEVERE, null, ex);
                    }finally{
                        try {
                            if(newConn != null || !newConn.isClosed()){
                                newConn.close();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(CompanyEventModule.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    Window subWindow = editScheduleEventWindow(rowId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.setResizable(false);
                    subWindow.center();
                    eventTable.setSelectable(false);
                    subWindow.addListener(new Window.CloseListener() {

                        @Override
                        public void windowClose(Window.CloseEvent e) {
                            eventTable.setSelectable(true);
                        }
                    });
                }
            }
            
        });
        
        return eventTable;
    }
    
    private Window editScheduleEventWindow(final String id){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        final Window subWindow = new Window("EDIT EVENT", layout);
        subWindow.setWidth("250px");

        final Select eventType = createEventTypelect();
        eventType.setCaption("Event Type: "+eventTypeSchedule);
        layout.addComponent(eventType);
        
        final DateField startDate = createDateField("Start Date: ");
        startDate.setDateFormat("yyyy-MM-dd HH:mm:ss");
        startDate.setValue(conUtil.parsingDateWithTime(startDateSchedule));
        layout.addComponent(startDate);
        
        final DateField endDate = createDateField("End Date: ");
        endDate.setDateFormat("yyyy-MM-dd HH:mm:ss");
        endDate.setValue(conUtil.parsingDateWithTime(endDateSchedule));
        layout.addComponent(endDate);
        
        final TextField caption = createTextField("Caption: ");
        caption.setWidth("100%");
        caption.setValue(captionSchedule);
        layout.addComponent(caption);
        
        final TextField location = createTextField("Where: ");
        location.setWidth("100%");
        location.setValue(locationSchedule);
        layout.addComponent(location);
        
        final TextArea description = new TextArea("Description: ");
        description.setWidth("100%");
        description.setValue(descriptionSchedule);
        description.setRows(3);
        layout.addComponent(description);
        
        final Select color = createStyleNameSelect(styleNameSchedule);
        layout.addComponent(color);
        
        Button saveEventButton = new Button("Apply", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                
                String employeeId = query.getEmployeesIdByUsername(username);
                CalendarModule calendarModule = new CalendarModule(username, null);
                                
                if(startDate.getValue() == null || endDate.getValue() == null){
                    subWindow.getWindow().showNotification("ADD Dates!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(caption.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Caption!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(location.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Location!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(description.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Description!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(eventType.getValue() == null){
                    subWindow.getWindow().showNotification("ADD Event Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(color.getValue() == null){
                    subWindow.getWindow().showNotification("ADD Color!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                basicEvent.setStartDate(conUtil.convertDateFormatWithTime(startDate.getValue().toString()));
                basicEvent.setEndDate(conUtil.convertDateFormatWithTime(endDate.getValue().toString()));
                basicEvent.setCaption(caption.getValue().toString());
                basicEvent.setLocation(location.getValue().toString());
                basicEvent.setDescription(description.getValue().toString());
                basicEvent.setStyleName(color.getValue().toString());
                basicEvent.setEventType(eventType.getValue().toString());
                basicEvent.setEmployeeId(employeeId);
                
                Boolean result = basicEvent.editEvent(id);
                if(result == true){
                    eventTbl(queryEvent);
                    calendarModule.calendarEvents();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("Cannot ADD new EVENT!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
                
            }
        });
                
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(saveEventButton);
        layout.addComponent(buttons);
        layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
                
        return subWindow;
    }
    
    private DateField createDateField(String caption) {
        DateField f = new DateField(caption);
        f.setSizeFull();
        if (useSecondResolution) {
            f.setResolution(DateField.RESOLUTION_SEC);
        } else {
            f.setResolution(DateField.RESOLUTION_MIN);
        }
            return f;
    }
    
    private Select createEventTypelect() {
        Select s = new Select();
        s.setWidth("100%");
        s.addItem("Legal Holiday");
        s.addItem("Special Holiday");
        s.addItem("Birthday");
        s.addItem("Meeting");
        s.setNullSelectionAllowed(false);
        
        return s;
    }
    
    private Select createStyleNameSelect(String styleName) {
        Select s = new Select("Color "+styleName);
        s.setWidth("100%");
        s.setNullSelectionAllowed(false);
        s.addContainerProperty("c", String.class, "");
        s.setItemCaptionPropertyId("c");
        Item i = s.addItem("color1");
        i.getItemProperty("c").setValue("Green");
        i = s.addItem("color2");
        i.getItemProperty("c").setValue("Blue");
        i = s.addItem("color3");
        i.getItemProperty("c").setValue("Red");
        i = s.addItem("color4");
        i.getItemProperty("c").setValue("Orange");
        s.setImmediate(true);
        return s;
    }
    
    private TextField createTextField(String caption) {
        TextField f = new TextField(caption);
        f.setWidth("100%");
        f.setNullRepresentation("");
        return f;
    }
}
