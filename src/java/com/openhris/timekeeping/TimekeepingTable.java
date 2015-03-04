/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class TimekeepingTable extends Table {

    public TimekeepingTable() {
        setSizeFull();
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        addContainerProperty("id", Integer.class, null);
        addContainerProperty("from", String.class, null);
        addContainerProperty("to", String.class, null);
        addContainerProperty("lates", Double.class, null);
        addContainerProperty("undertime", Double.class, null);
        addContainerProperty("overtime", Double.class, null);
        addContainerProperty("night differential", Double.class, null);
        addContainerProperty("late (d)", Double.class, null);
        addContainerProperty("undertime (d)", Double.class, null);
        addContainerProperty("overtime (p)", Double.class, null);
        addContainerProperty("night differential (p)", Double.class, null);
        addContainerProperty("wLegal (p)", Double.class, null);
        addContainerProperty("wSecial (p)", Double.class, null);
        addContainerProperty("wdo (p)", Double.class, null);
        addContainerProperty("holiday (p)", Double.class, null);
    }
    
}
