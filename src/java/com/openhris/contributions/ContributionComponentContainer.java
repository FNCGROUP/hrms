/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.contributions;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jet
 */
public class ContributionComponentContainer {
    
    public ComponentContainer contributionComponentContainer(){
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        VerticalLayout main = new VerticalLayout();
        main.setSizeFull();
        main.setCaption("Social Security System");
        main.addComponent(new SssContributionMainUI());
        ts.addComponent(main);
        
        main = new VerticalLayout();
        main.setSizeFull();
        main.setCaption("Philhealth");
        main.addComponent(new PhicContributionMainUI());
        ts.addComponent(main);
        
        main = new VerticalLayout();
        main.setSizeFull();
        main.setCaption("Tax Table");
        main.addComponent(new TaxContributionMainUI());
        ts.addComponent(main);
        
        return ts;
    }
    
}


