/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.global;

import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public abstract class AbstractTableProperties extends Table {

    public AbstractTableProperties() {
        setSizeFull();
        setImmediate(true);
        setSelectable(true);
        setColumnCollapsingAllowed(true);
        addStyleName("employees-table-layout");
    }
    
    protected abstract void tableGenerateRenderer();
    
}
