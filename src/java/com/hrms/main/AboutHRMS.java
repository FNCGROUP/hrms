/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.main;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class AboutHRMS extends Window {

    public AboutHRMS() {        
        setCaption("About HRMS");
        setWidth("300px");
                
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        Label version = new Label("Version: <b>1.1</b>");
        version.setContentMode(Label.CONTENT_XHTML);
        vlayout.addComponent(version);
        
        Label title = new Label("Title: <b>Human Resource Information System</b>");
        title.setContentMode(Label.CONTENT_XHTML);
        vlayout.addComponent(title);
        
        Label developer = new Label("Developed By: <b>Engr. Godfrey D. Beray</b>");
        developer.setContentMode(Label.CONTENT_XHTML);
        vlayout.addComponent(developer);
        
        Label framework = new Label("Framework: <b>VAADIN - Sept2012</b>");
        framework.setContentMode(Label.CONTENT_XHTML);
        vlayout.addComponent(framework);
        
        addComponent(vlayout);
    }
        
}
