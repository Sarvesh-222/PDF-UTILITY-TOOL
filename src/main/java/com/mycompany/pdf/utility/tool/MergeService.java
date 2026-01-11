/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.io.MemoryUsageSetting;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author swast
 */
public class MergeService {
    
    private static String desktopAddress=System.getProperty("user.home") + File.separator + "Desktop";
    
    public static void MergeTWOPDFS(File pdf1, File pdf2,String outputName) throws IOException
    {
        //Handling Missing Files
        if (!pdf1.exists() || !pdf2.exists()) {
            throw new IOException("One or both input files do not exist.");
        }

        PDFMergerUtility merger = new PDFMergerUtility();
        
        merger.addSource(pdf1);
        merger.addSource(pdf2);
        
        merger.setDestinationFileName(desktopAddress +File.separator + outputName +".pdf");
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        
//            System.out.println("Files Not Found: "+e.getMessage());
 
    }
    
    public static void MergePDFs(List<File> files, String outputName) throws IOException
    {
        //Empty List
        if(files==null || files.isEmpty())
        {
            System.out.println("No PDF files found");
            return;
        }
        
        //Handling Missing Files
        for(File file : files)
        {
            if(!file.exists())
            {
                throw new IOException("FILE "+ file.getName()+" Does Not Exists");   
            }
        }
        
        //Main Execution
        PDFMergerUtility merger = new PDFMergerUtility();
        
        for(File file : files)
        {
            merger.addSource(file);
        }
        
        merger.setDestinationFileName(desktopAddress +File.separator + outputName +".pdf");
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        
        // SUCCESS POPUP
        Alert successAlert = new Alert(AlertType.INFORMATION);
        successAlert.setTitle("Merge Complete");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Merged PDF successfully created at:\n" + desktopAddress +File.separator+ outputName +".pdf");
        successAlert.showAndWait();
        
    }
}
