/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.listeners;

/**
 *
 * @author tszin
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.bytecode.stackmap.BasicBlock;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import uk.tryzub.controllers.OrganizationHelper;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.omnifaces.cdi.GraphicImageBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import uk.tryzub.controllers.HabitationHelper;

@ManagedBean
@ViewScoped
public class FileUploadView implements Serializable {

    private boolean isshowed = true;
    private UploadedFile file;
    private byte[] imageContent;

    @ManagedProperty(value = "#{organizationHelper}")
    private OrganizationHelper organizationHelper;

    public OrganizationHelper getOrganizationHelper() {
        return organizationHelper;
    }

    public void setOrganizationHelper(OrganizationHelper organizationHelper) {
        this.organizationHelper = organizationHelper;
    }
    
    
   
    

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload(FileUploadEvent event) {

        setFile(event.getFile());
        imageContent = file.getContents();
        setIsshowed(false);

    }

    
    //придумать на жилье
    public void upload1() {}
    
    
    
    
    
    
    /*this part for organization*/
    public void upload() {
        if (file != null) {

            save();
            //delete ();  удалить из базы старый файл, чтобы не засорял
        }
    }

    public void save() {
        //when will be hosting - change folder
        Path folder = Paths.get("F:/MyProjects/tryzub/photo/organization");

        /* getting filename with extension from path-fullname*/
        String fileName = file.getFileName().replaceAll(".*[\\\\/]|\\.[^\\.]*$‌​", "").split("\\.")[0];
        /*getContentType return String like image/jpeg  */
        String extention = file.getContentType().split("\\/")[1];

        try (InputStream input = file.getInputstream()) {
            Path newFile = Files.createTempFile(folder, fileName + "-", "." + extention);
            Files.copy(input, newFile, StandardCopyOption.REPLACE_EXISTING);

            /*!!!!!!!Folder need to be specify after hosting */
            File oldFile = new File("F:/MyProjects/tryzub/photo"+organizationHelper.getSelectedOrgStrd().getPhoto());
            
            /*newFile to be writen in database*/
            organizationHelper.setNewPhoto("/organization/" + newFile.getFileName());
            
            /*delete old file from folder to save disk space*/
            if (oldFile.delete()){
            file = null;//чтобы потом не загрузили это же фото на следующей организации
            }

             
            FacesMessage message = new FacesMessage("Succesful, file is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            e.printStackTrace();  // Show faces message?

        }
    }

    public boolean isIsshowed() {
        return isshowed;
    }

    public void setIsshowed(boolean isshowed) {
        this.isshowed = isshowed;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

}
