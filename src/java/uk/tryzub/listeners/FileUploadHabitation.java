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
import java.util.ArrayList;
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
public class FileUploadHabitation implements Serializable {

    private boolean isshowed = true;

    private ArrayList<UploadedFile> filesList;
    private ArrayList<byte[]> imageContents;


    private byte[] imageContent;

    @ManagedProperty(value = "#{organizationHelper}")
    private OrganizationHelper organizationHelper;

    public OrganizationHelper getOrganizationHelper() {
        return organizationHelper;
    }

    public void setOrganizationHelper(OrganizationHelper organizationHelper) {
        this.organizationHelper = organizationHelper;
    }

    @ManagedProperty(value = "#{habitationHelper}")
    private HabitationHelper habitationHelper;

    public HabitationHelper getHabitationHelper() {
        return habitationHelper;
    }

    public void setHabitationHelper(HabitationHelper habitationHelper) {
        this.habitationHelper = habitationHelper;
    }

    public void handleFileUpload(FileUploadEvent event) {

        UploadedFile upFile = event.getFile();

        if (filesList.size() < 5) {
            filesList.add(upFile);
            imageContent = upFile.getContents();
            imageContents.add(imageContent);
            setIsshowed(false);
        }

    }

    //придумать на жилье
    public void upload1() {
    }

    /*this part for organization*/
    public void upload() {
        if (filesList != null && filesList.size() > 0) {

            save();
            //delete ();  удалить из базы старый файл, чтобы не засорял
        }
    }

    public void save() {
        //when will be hosting - change folder
        Path folder = Paths.get("F:/MyProjects/tryzub/photo/habitation");
        StringBuilder photosPath = new StringBuilder();

        for (UploadedFile file : filesList) {

            /* getting filename with extension from path-fullname*/
            String fileName = file.getFileName().replaceAll(".*[\\\\/]|\\.[^\\.]*$‌​", "").split("\\.")[0];
            /*getContentType return String like image/jpeg  */
            String extention = file.getContentType().split("\\/")[1];

            try (InputStream input = file.getInputstream()) {
                Path newFile = Files.createTempFile(folder, fileName + "-", "." + extention);
                Files.copy(input, newFile, StandardCopyOption.REPLACE_EXISTING); //переделать в стрим, чтобы было быстрее, найди самые быстрые способы

                /*newFile to be writen in database*/
                photosPath.append("/habitation/");
                photosPath.append(newFile.getFileName());
                photosPath.append(" ");  //не допустить пробелов в имени файла, а то нарушится вся конструкция

                FacesMessage message = new FacesMessage("Succesful, files is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);

            } catch (IOException e) {
                e.printStackTrace();  // Show faces message?

            }
        }
        /*newFiles to be writen in database*/
        habitationHelper.setNewPhotosPath(photosPath.toString());

    }

    public void deleteHabitationsPhotos() {

        /*!!!!!!!Folder need to be specify after hosting */
        File oldFile = new File("F:/MyProjects/tryzub/photo" + organizationHelper.getSelectedOrgStrd().getPhoto());

        /*delete old file from folder to save disk space*/
        if (oldFile.delete()) {
            //file = null;//чтобы потом не загрузили это же фото на следующей организации
        }
    }

    public void setNewFilesList() {
        this.filesList = new ArrayList<>();
        this.imageContents = new ArrayList<>();

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

    public ArrayList<UploadedFile> getFilesList() {
        return filesList;
    }

    public void setFilesList(ArrayList<UploadedFile> filesList) {
        this.filesList = filesList;
    }

  

    public ArrayList<byte[]> getImageContents() {
        return imageContents;
    }

    public void setImageContents(ArrayList<byte[]> imageContents) {
        this.imageContents = imageContents;
    }
}
