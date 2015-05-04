/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.beans.BranchBean;
import com.hrms.beans.CorporateNameBean;
import com.hrms.beans.TradeNameBean;
import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.TradeName;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.queries.GetSQLQueryUpdate;
import com.openhris.model.Branch;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.service.CompanyService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Window.CloseEvent;
import java.sql.Connection;

/**
 *
 * @author jet
 */
public class CompanyModule extends VerticalLayout{
    
    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    CorporateNameBean corporateNameBean = new CorporateNameBean();
    TradeNameBean tradeNameBean = new TradeNameBean();
    BranchBean branchBean = new BranchBean();
    CorporateName corporateNames = new CorporateName();
    TradeName tradeNames = new TradeName();
    BranchName branchNames = new BranchName();
    Table companyTbl = new Table();
    
    CompanyService companyService = new CompanyServiceImpl();
    
    private String userRole;
    private Integer corporateId;
    private Integer tradeId;
    
    public CompanyModule(String userRole){
        
        this.userRole = userRole;
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
        setSpacing(true);        
        companyTable();
        
        addComponent(companyTbl);
        
    }
    
    public final Table companyTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        companyTbl.setSizeFull();
        companyTbl.setPageLength(20);
        companyTbl.setSelectable(true);
        companyTbl.setImmediate(true);
        
        companyTbl.setStyleName("striped");
        companyTbl.removeAllItems();
        companyTbl.addContainerProperty("corporate name", String.class, null);
        companyTbl.addContainerProperty("trade name", String.class, null);
        companyTbl.addContainerProperty("branch", String.class, null);
        companyTbl.addContainerProperty("address", String.class, null);
        
        int i = 0;
        for(Branch b : companyService.getAllCorporateTradeBranch()){
            companyTbl.addItem(new Object[]{
                b.getCompanyName().toUpperCase(), 
                b.getTradeName().toUpperCase(), 
                b.getBranchName().toUpperCase(), 
                b.getBranchAdress()
            }, new Integer(i));
            i++;
        }
                
        for(Object listener : companyTbl.getListeners(ItemClickEvent.class)){
            companyTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        companyTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = companyTbl.getItem(itemId);
                String address = item.getItemProperty("address").toString();                
                
                if(event.getPropertyId().equals("corporate name")){
                    if(userRole.equals("administrator") || userRole.equals("hr")){
                        int tradeId = query.getCoporateId(item.getItemProperty("corporate name").toString().toLowerCase());
                        Window subWindow = updateCorporateName(tradeId, item.getItemProperty("corporate name").toString().toLowerCase());
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.setModal(true);
                        subWindow.setResizable(false);
                        subWindow.center();
                        companyTbl.setSelectable(false);
                        subWindow.addListener(new Window.CloseListener() {

                            @Override
                            public void windowClose(CloseEvent e) {
                                companyTbl.setSelectable(true);
                            }
                        });
                    }
                }
                
                if(event.getPropertyId().equals("trade name")){
                    if(userRole.equals("administrator") || userRole.equals("hr")){
                        int tradeId = query.getTradeId(item.getItemProperty("trade name").toString().toLowerCase(), 
                                item.getItemProperty("corporate name").toString().toLowerCase());
                        Window subWindow = updateTradeContributionIdNo(tradeId, item.getItemProperty("trade name").toString().toLowerCase());
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.setModal(true);
                        subWindow.setResizable(false);
                        subWindow.center();
                        companyTbl.setSelectable(false);
                        subWindow.addListener(new Window.CloseListener() {

                            @Override
                            public void windowClose(CloseEvent e) {
                                companyTbl.setSelectable(true);
                            }
                        });
                    }                    
                }
                
                if(event.getPropertyId().equals("branch")){
                    /*if(address != null){
                        getWindow().showNotification("Address already EXIST!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }*/
                    
                    String corporateName = item.getItemProperty("corporate name").getValue().toString();
                    String tradeName = item.getItemProperty("trade name").getValue().toString();
                    String branchName = item.getItemProperty("branch").getValue().toString();
                    String branchAddress = item.getItemProperty("address").toString();
                    int branchId = query.getBranchId(branchName, tradeName, corporateName);
                    
                    Window subWindow = updateBranchWindow(branchId, branchName, branchAddress);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.setResizable(false);
                    subWindow.center();
                    companyTbl.setSelectable(false);
                    subWindow.addListener(new Window.CloseListener() {

                        @Override
                        public void windowClose(CloseEvent e) {
                            companyTbl.setSelectable(true);
                        }
                    });
                }
                
            }
            
        });
        companyTbl.setPageLength(25);
        
        return companyTbl;
    } 
    
    public Window corporateSubwindow(){        
        VerticalLayout subWindowLayout = new VerticalLayout();
        subWindowLayout.setSpacing(true);
        subWindowLayout.setMargin(true);
        
        final Window subWindow = new Window("Corporate Name", subWindowLayout);
        subWindow.setWidth("300px");
                
        final TextField corporateName = new TextField();
        corporateName.setNullSettingAllowed(false);
        corporateName.setWidth("100%");
        subWindow.addComponent(corporateName);
        
        Button save = new Button("Save");
        save.setWidth("100%");        
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean checkResult = query.checkCorporateIfExist(corporateName.getValue().toString().trim());
                
                if(checkResult == false){
                    getWindow().showNotification("CORPORATE name already EXIST!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                    return;
                }
                
                corporateNameBean.setCompanyName(corporateName.getValue().toString().trim());
                String name = corporateNameBean.getCompanyName();
                boolean result = query.saveCorporateName(name);
                if(result == true){
                    getWindow().showNotification("New CORPORATE has been Added!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("SQL Error");
                }
            }
            
        });
        subWindow.addComponent(save);
        
        return subWindow;
    }
    
    public Window tradeSubwindow(){
        final Window subWindow = new Window("Trade Name");
        subWindow.setWidth("300px");
        
        VerticalLayout subWindowLayout = new VerticalLayout();
        subWindowLayout.setSpacing(true);
        
        final NativeSelect corporateName = new NativeSelect();
        corporateName.setWidth("100%");
        corporateNames.getCorporateName(corporateName);
        subWindowLayout.addComponent(corporateName);
        
        final TextField tradeName = new TextField();
        tradeName.setNullSettingAllowed(false);
        tradeName.setWidth("100%");
        subWindowLayout.addComponent(tradeName);
        
        Button save = new Button("Save");
        save.setWidth("100%");
        subWindowLayout.addComponent(save);
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                tradeNameBean.setTradeName(tradeName.getValue().toString().trim());
                corporateNameBean.setCompanyName(corporateName.getValue().toString());
                String trade_name = tradeNameBean.getTradeName();
                String corporate_name = corporateNameBean.getCompanyName();
                boolean result = query.saveTradeName(corporate_name, trade_name);
                if(result == true){
                    getWindow().showNotification("Added TRADE on "+corporate_name.toUpperCase(), Window.Notification.TYPE_HUMANIZED_MESSAGE);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("SQL Error");
                }
            }
            
        });
        
        subWindow.addComponent(subWindowLayout);
        return subWindow;
    }
    
    public Window branchSubwindow(){
        final Window subWindow = new Window("Trade Name");
        subWindow.setWidth("300px");
        
        VerticalLayout subWindowLayout = new VerticalLayout();
        subWindowLayout.setSpacing(true);
        
        final NativeSelect corporateName = new NativeSelect();
        corporateName.setWidth("100%");
        corporateNames.getCorporateName(corporateName);
        subWindowLayout.addComponent(corporateName);
        
        final NativeSelect tradeName = new NativeSelect();
        tradeName.setWidth("100%");
        corporateName.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(corporateName.getValue() == null){                    
                }else{
                    corporateId = query.getCoporateId(corporateName.getValue().toString());
                    tradeNames.getTradeName(tradeName, corporateName.getValue().toString());
                }                
            }
            
        });
        tradeName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(tradeName.getValue() == null){                    
                }else{
                    tradeId = query.getTradeId(tradeName.getValue().toString(), corporateName.getValue().toString());
                }
            }
            
        });
        subWindowLayout.addComponent(tradeName);
        
        final TextField branchName = new TextField();
        branchName.setNullSettingAllowed(false);
        branchName.setWidth("100%");
        subWindowLayout.addComponent(branchName);
        
        Button save = new Button("Save");
        save.setWidth("100%");
        subWindowLayout.addComponent(save);
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
//                branchBean.setBranchName(branchName.getValue().toString().trim());
//                tradeNameBean.setTradeName(tradeName.getValue().toString());
//                corporateNameBean.setCompanyName(corporateName.getValue().toString());
//                String branch_name = branchBean.getBranchName();
//                String trade_name = tradeNameBean.getTradeName();
//                String corporate_name = corporateNameBean.getCompanyName();
                boolean result = query.saveBranchName(tradeId, branchName.getValue().toString().trim());
                if(result == true){
                    companyTable();
                    (subWindow.getParent()).removeWindow(subWindow);                    
                }else{
                    getWindow().showNotification("SQL Error");
                }
            }
            
        });
        
        subWindow.addComponent(subWindowLayout);
        return subWindow;
    }
    
    private Window updateBranchWindow(final int branchId, final String name, String address){
        final Window subWindow = new Window("Add Branch Address to "+name.toUpperCase());
        subWindow.setWidth("300px");        
        
        GridLayout glayout = new GridLayout(2, 3);
        glayout.setSizeFull();
        glayout.setSpacing(true);
        
        final CheckBox editBranchName = new CheckBox("Edit");
        editBranchName.setImmediate(true);
        glayout.addComponent(editBranchName, 1, 0);
        glayout.setComponentAlignment(editBranchName, Alignment.BOTTOM_RIGHT);        
        
        final TextField branchName = new TextField("Branch: ");
        branchName.setWidth("210px");
        branchName.setValue(name);
        branchName.setEnabled(false);
        branchName.setImmediate(true);        
        editBranchName.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                branchName.setEnabled(event.getButton().booleanValue());
            }
        });
        branchName.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(branchName.getValue() == null || branchName.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Branch name.", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = companyService.updateBranchName(branchId, 
                        branchName.getValue().toString().trim().toLowerCase());
                if(result){
                    (subWindow.getParent()).removeWindow(subWindow);
                    companyTable();
                }
            }
        });
        glayout.addComponent(branchName, 0, 0);
        
        final CheckBox editBranchAddress = new CheckBox("Edit");
        editBranchAddress.setImmediate(true);
        glayout.addComponent(editBranchAddress, 1, 1);
        glayout.setComponentAlignment(editBranchAddress, Alignment.BOTTOM_RIGHT);
        
        final TextField branchAddress = new TextField("Address: ");
        branchAddress.setWidth("210px");
        branchAddress.setValue(address);
        branchAddress.setEnabled(false);
        branchAddress.setImmediate(true);        
        editBranchAddress.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                branchAddress.setEnabled(event.getButton().booleanValue());
            }
        });        
        branchAddress.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(branchAddress.getValue() == null || branchAddress.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Branch address.", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = companyService.updateBranchAddress(branchId, 
                        branchAddress.getValue().toString().trim().toLowerCase());
                if(result){
                    (subWindow.getParent()).removeWindow(subWindow);
                    companyTable();
                }
            }
        });
        glayout.addComponent(branchAddress, 0 , 1);
        
        Button save = new Button("SAVE");
        save.setWidth("100%");
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {                
                boolean result = queryUpdate.updateBranchAddress(branchId, branchAddress.toString().trim().toLowerCase());
                if(result == true){
                    companyTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    getWindow().showNotification("SQL Error");
                }
            }
            
        });
//        subWindow.addComponent(save);
        
        Button delete = new Button("DELETE "+name.toUpperCase());
        delete.setWidth("100%");
        delete.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window window = removeBranch(branchId, name, subWindow);
                if(window.getParent() == null){
                    getWindow().addWindow(window);
                }
                window.setModal(true);
                window.setResizable(false);
                window.center();
            }
            
        });
        glayout.addComponent(delete, 0 ,2, 1, 2);
        subWindow.addComponent(glayout);
        
        return subWindow;
    }
    
    private Window removeBranch(final Integer id, String name, final Window window){
        final Window subWindow = new Window("REMOVE BRANCH "+name.toUpperCase());
        subWindow.setWidth("300px");        
                
        Button save = new Button("REMOVE BRANCH?");
        save.setWidth("100%");
        save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean checkIfEmployeesExistForBranch = queryUpdate.checkIfEmployeesExistForBranch(id);
                if(checkIfEmployeesExistForBranch == true){  
                    getWindow().showNotification("There are employees exist on this branch, Contact your DBA!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                Boolean result = queryUpdate.removeBranchFromTrade(id);
                if(result == true){
                    companyTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                    (window.getParent()).removeWindow(window);
                }else{
                    getWindow().showNotification("SQL Error");
                }
            }
            
        });
        subWindow.addComponent(save);
        
        return subWindow;
    }
    
    private Window updateTradeContributionIdNo(final int id, final String tradeName){
        tradeNameBean.getTradeContributionId(id);
        final Window subWindow = new Window("Add/Edit Trade's ID");
        subWindow.setWidth("300px");
        
        GridLayout grid = new GridLayout(2, 5);
        grid.setSizeFull();
        grid.setSpacing(true);
        
        final CheckBox editTradeName = new CheckBox("Edit");
        editTradeName.setImmediate(true);
        grid.addComponent(editTradeName, 1, 0);
        grid.setComponentAlignment(editTradeName, Alignment.BOTTOM_RIGHT);
        
        final TextField tradeField = new TextField("Trade Name: ");
        tradeField.setWidth("210px");
        tradeField.setValue(tradeName);
        tradeField.setEnabled(false);
        tradeField.setImmediate(true);
        editTradeName.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                tradeField.setEnabled(event.getButton().booleanValue());
            }
        });
        tradeField.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(tradeField.getValue() == null || tradeField.getValue().toString().isEmpty() || tradeField.getValue().toString().equals("null")){
                    subWindow.getWindow().showNotification("TRADE FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                boolean result = tradeNameBean.updateTradeName(tradeField.getValue().toString(), id);
                if(result == true){
                    tradeField.setEnabled(false);
                    editTradeName.setValue(false);
                    subWindow.getWindow().showNotification("TRADE NAME UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    companyTable();
                }else{
                    subWindow.getWindow().showNotification("ERROR TRADE ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        grid.addComponent(tradeField, 0, 0);
        
        
        final CheckBox editSss = new CheckBox("Edit");
        editSss.setImmediate(true);
        grid.addComponent(editSss, 1, 1);
        grid.setComponentAlignment(editSss, Alignment.BOTTOM_RIGHT);
        
        final TextField sssField = new TextField("SSS ID No: ");
        sssField.setWidth("210px");
        sssField.setValue(tradeNameBean.getSssId());
        sssField.setEnabled(false);
        sssField.setImmediate(true);
        editSss.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                sssField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        sssField.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(sssField.getValue() == null || sssField.getValue().toString().isEmpty() || sssField.getValue().toString().equals("null")){
                    subWindow.getWindow().showNotification("SSS FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = tradeNameBean.updateSssIdNo(id, sssField.getValue().toString().trim());
                if(result == true){
                    sssField.setEnabled(false);
                    editSss.setValue(false);
                    subWindow.getWindow().showNotification("SSS ID # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    companyTable();
                }else{
                    subWindow.getWindow().showNotification("ERROR SSS ID ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(sssField, 0, 1);
        
        final CheckBox editHdmf = new CheckBox("Edit");
        editHdmf.setImmediate(true);
        grid.addComponent(editHdmf, 1, 2);
        grid.setComponentAlignment(editHdmf, Alignment.BOTTOM_RIGHT);
        
        final TextField hdmfField = new TextField("HDMF ID No: ");
        hdmfField.setWidth("100%");
        hdmfField.setValue(tradeNameBean.getHdmfId());
        hdmfField.setEnabled(false);
        hdmfField.setImmediate(true);
        editHdmf.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                hdmfField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        hdmfField.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(hdmfField.getValue() == null || hdmfField.getValue().toString().isEmpty() || hdmfField.getValue().toString().equals("null")){
                    subWindow.getWindow().showNotification("HDMF FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = tradeNameBean.updateHdmfIdNo(id, hdmfField.getValue().toString().trim());
                if(result == true){
                    hdmfField.setEnabled(false);
                    editHdmf.setValue(false);
                    subWindow.getWindow().showNotification("HDMF ID # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    companyTable();
                }else{
                    subWindow.getWindow().showNotification("ERROR HDMF ID ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(hdmfField, 0, 2);
        
        final CheckBox editPhic = new CheckBox("Edit");
        editPhic.setImmediate(true);
        grid.addComponent(editPhic, 1, 3);
        grid.setComponentAlignment(editPhic, Alignment.BOTTOM_RIGHT);
        
        final TextField phicField = new TextField("Phic ID No: ");
        phicField.setWidth("100%");
        phicField.setValue(tradeNameBean.getPhicId());
        phicField.setEnabled(false);
        phicField.setImmediate(true);
        editPhic.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                phicField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        phicField.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(phicField.getValue() == null || phicField.getValue().toString().isEmpty() || phicField.getValue().toString().equals("null")){
                    subWindow.getWindow().showNotification("PHIC FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = tradeNameBean.updatePhicIdNo(id, phicField.getValue().toString().trim());
                if(result == true){
                    phicField.setEnabled(false);
                    editPhic.setValue(false);
                    subWindow.getWindow().showNotification("PHIC ID # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    companyTable();
                }else{
                    subWindow.getWindow().showNotification("ERROR PHIC ID ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(phicField, 0, 3);
        
        final CheckBox editTin = new CheckBox("Edit");
        editTin.setImmediate(true);
        grid.addComponent(editTin, 1, 4);
        grid.setComponentAlignment(editTin, Alignment.BOTTOM_RIGHT);
        
        final TextField tinField = new TextField("Tin ID No: ");
        tinField.setWidth("100%");
        tinField.setValue(tradeNameBean.getTinId());
        tinField.setEnabled(false);
        tinField.setImmediate(true);
        editTin.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                tinField.setEnabled(event.getButton().booleanValue());
            }
            
        });
        tinField.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(tinField.getValue() == null || tinField.getValue().toString().isEmpty() || tinField.getValue().toString().equals("null")){
                    subWindow.getWindow().showNotification("TIN FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = tradeNameBean.updateTinIdNo(id, tinField.getValue().toString().trim());
                if(result == true){
                    tinField.setEnabled(false);
                    editTin.setValue(false);
                    subWindow.getWindow().showNotification("TIN ID # UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    companyTable();
                }else{
                    subWindow.getWindow().showNotification("ERROR TIN ID ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        grid.addComponent(tinField, 0, 4);
        
        subWindow.addComponent(grid);
        
        return subWindow;
    }
    
    private Window updateCorporateName(final int id, String coporateName){
        final Window subWindow = new Window("Edit Corporate Name");
        subWindow.setWidth("300px");
        
        GridLayout grid = new GridLayout(2, 1);
        grid.setSizeFull();
        grid.setSpacing(true);
        
        final CheckBox editCorporateName = new CheckBox("Edit");
        editCorporateName.setImmediate(true);
        grid.addComponent(editCorporateName, 1, 0);
        grid.setComponentAlignment(editCorporateName, Alignment.BOTTOM_RIGHT);
        
        final TextField corporateField = new TextField("Corporate Name: ");
        corporateField.setWidth("210px");
        corporateField.setValue(coporateName);
        corporateField.setEnabled(false);
        corporateField.setImmediate(true);
        editCorporateName.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                corporateField.setEnabled(event.getButton().booleanValue());
            }
        });
        corporateField.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(corporateField.getValue() == null || corporateField.getValue().toString().isEmpty() || corporateField.getValue().toString().equals("null")){
                    subWindow.getWindow().showNotification("CORPORATE FIELD IS EMPTY!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                boolean result = corporateNameBean.updateCorporateName(corporateField.getValue().toString(), id);
                if(result == true){                    
                    subWindow.getWindow().showNotification("CORPORATE NAME UPDATED!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    (subWindow.getParent()).removeWindow(subWindow);
                    companyTable();
                }else{
                    subWindow.getWindow().showNotification("ERROR CORPORATE ENTRY!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        grid.addComponent(corporateField, 0, 0);
        
        subWindow.addComponent(grid);
        return subWindow;
    }
}
