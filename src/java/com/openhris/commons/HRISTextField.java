/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons;

import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author jetdario
 */
public class HRISTextField extends TextField {

    private String caption;
    
    public HRISTextField(String caption) {
        this.caption = caption;
        setCaption(getPreferedCaption());
	setWidth("100%");
        setNullSettingAllowed(true);
    }
    
    String getPreferedCaption(){
        return caption;
    }
}
