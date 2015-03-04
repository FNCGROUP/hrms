/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons;

import com.vaadin.ui.DateField;
import com.vaadin.ui.PopupDateField;
import java.util.Date;

/**
 *
 * @author jetdario
 */
public class DateSelector extends PopupDateField {

    public DateSelector(String caption) {
        setCaption(caption);
        addStyleName("mydate");
        setValue(new Date());
        setWidth("100%");
        setDateFormat("EEE - MMM dd, yyyy");
        setLenient(true);
        setResolution(DateField.RESOLUTION_DAY);
    }
    
}
