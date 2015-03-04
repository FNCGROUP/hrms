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
public class HRISPopupDateField extends PopupDateField {

    private String caption;
    
    public HRISPopupDateField(String caption) {
        this.caption = caption;
        setCaption(getPreferedCaption());
        addStyleName("mydate");
        setValue(new Date());
        setDateFormat("yyyy-MM-dd");
        setWidth("100%");
        setResolution(DateField.RESOLUTION_DAY);
    }
    
    String getPreferedCaption(){
        return caption;
    }
}
