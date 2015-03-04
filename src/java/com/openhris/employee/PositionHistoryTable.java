/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.openhris.commons.OpenHrisUtilities;
import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class PositionHistoryTable extends Table {

    OpenHrisUtilities utilities = new OpenHrisUtilities();
    
    public PositionHistoryTable() {
        setSizeFull();
        setSelectable(true);
        setImmediate(true);        
        setStyleName("employees-table-layout");   
        
        addContainerProperty("id", String.class, null);
        addContainerProperty("position", String.class, null);
        addContainerProperty("company", String.class, null);
        addContainerProperty("trade", String.class, null);
        addContainerProperty("branch", String.class, null);
        addContainerProperty("department", String.class, null);
        addContainerProperty("entry date", String.class, null);
    }    
}
