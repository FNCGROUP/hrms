/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.commons;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class AboutHris extends Window {
    
    public AboutHris(){
        setCaption("About HRIS");
        setWidth("300x");
    }
    
    public Window aboutHris(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("About HRMS", vlayout);
        subWindow.setWidth("300px");
                
        Label version = new Label("Version: <b>1.0</b>");
        version.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(version);
        
        Label title = new Label("Title: <b>Human Resource Information System</b>");
        title.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(title);
        
        Label developer = new Label("Developed By: <b>Engr. Godfrey D. Beray</b>");
        developer.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(developer);
        
        Label framework = new Label("Framework: <b>VAADIN - Sept2012</b>");
        framework.setContentMode(Label.CONTENT_XHTML);
        subWindow.addComponent(framework);
        
        return subWindow;
    }
    
}
