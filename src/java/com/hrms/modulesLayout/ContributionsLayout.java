/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modulesLayout;

import com.hrms.modules.PhilhealthModule;
import com.hrms.modules.SSSModule;
import com.hrms.modules.TaxTableModule;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jet
 */
public class ContributionsLayout extends VerticalLayout {

    private Accordion accordion = new Accordion();
    private Tab philhealthTab;
    private Tab sssTab;
    private Tab taxTab;
    
    PhilhealthModule philhealth = new PhilhealthModule();
    SSSModule sss = new SSSModule();
    TaxTableModule tax = new TaxTableModule();
    
    public ContributionsLayout(){
        
        setSpacing(true);
        accordion.setSizeFull();
        philhealthTab = accordion.addTab(philhealth, "Philhealth", null);
        sssTab = accordion.addTab(sss, "Social Security Service", null);
        taxTab = accordion.addTab(tax, "Tax Table", null);
        
        addComponent(accordion);
    }
    
}
