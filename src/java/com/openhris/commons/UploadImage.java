/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.commons;

import com.openhris.service.PersonalInformationService;
import com.openhris.serviceprovider.PersonalInformationServiceImpl;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class UploadImage extends Window implements Upload.StartedListener, Upload.SucceededListener, Upload.FailedListener{
    
    Upload upload;
    Panel imagePanel;         // Root element for contained components.
    Embedded avatar;          // Embedded that contains the uploaded image.
    File  file;             // File to write to.
    Panel panel = new Panel();
    MyReceiver receiver = new MyReceiver();   
    
    String employeeId;    

    public UploadImage(Panel imagePanel, Embedded avatar, String employeeId){
        this.imagePanel = imagePanel;
        this.avatar = avatar;
        this.employeeId = employeeId;
        
        imagePanel = new Panel("Upload Component");
        imagePanel.setWidth("400px");
        addComponent(imagePanel);       
        
        // Create the Upload component.
        upload = new Upload("Upload the Image file here", receiver);
        // Use a custom button caption instead of plain "Upload".
        upload.setButtonCaption("Upload Now");
        
        // Listen for events regarding the success of upload. 
        upload.addListener((Upload.StartedListener) this);
        upload.addListener((Upload.SucceededListener) this);
        upload.addListener((Upload.FailedListener) this);
        
        panel.setWidth("100%");
        panel.addComponent(new Label("Click 'Choose file' to " + 
                "select a file and then click 'Upload Now'. " + 
                "image should have the same width and height so " +
                "that image will not look deformed when viewed."));
        
        imagePanel.addComponent(upload);
        imagePanel.addComponent(panel);
        
        avatar = new Embedded();
        avatar.setImmediate(true);
        avatar.setWidth(90, Sizeable.UNITS_PIXELS);
        avatar.setHeight(90, Sizeable.UNITS_PIXELS);
        imagePanel.addComponent(avatar);             
    }
    
    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        if(file == null){
            getWindow().showNotification("Choose an Image file to Upload.", Window.Notification.TYPE_WARNING_MESSAGE);
            return;
        }
        upload.submitUpload();
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Log the upload on screen.
        imagePanel.addComponent(new Label("File " + event.getFilename()
                + " of type '" + event.getMIMEType()
                + "' uploaded."));
        
        PersonalInformationService piService = new PersonalInformationServiceImpl();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UploadImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //insert image to DB
        Boolean result = piService.uploadImageForEmployee(inputStream, file, getEmployeeId());
        if(!result){
            getWindow().showNotification("Cannot upload Image.", Window.Notification.TYPE_ERROR_MESSAGE);
            return;
        } 
        
        // Display the uploaded file in the image panel.
        final FileResource imageResource = new FileResource(file, getApplication());
        avatar.setSource(imageResource);
        getParent().removeWindow(this);
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        getWindow().showNotification("Upload Failed", Window.Notification.TYPE_ERROR_MESSAGE);
    }
    
    public class MyReceiver implements Upload.Receiver {
    
        private String fileName;
        private String mtype;
        private boolean sleep;
        private int total = 0;

        public OutputStream receiveUpload(String filename, String mimeType) {
            fileName = filename;
            mtype = mimeType;
            //File file  = new File(filename);
            file  = new File(filename);
            
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(file);
            }catch(Exception e){
                //e.getMessage();
                System.err.print("This causes error "+e.getMessage());
            }
            return fos;
        }
        
        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mtype;
        }

        public void setSlow(boolean value) {
            sleep = value;
        }
    
    }
    
    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }
    
    public String getEmployeeId(){
        return employeeId;
    }
    
}
