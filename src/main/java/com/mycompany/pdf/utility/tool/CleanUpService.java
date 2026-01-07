/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;


import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


//import org.apache.pdfbox.pdmodel.graphics.PDImageXObject;

/**
 *
 * @author swast
 */
public class CleanUpService {
    
    private static String desktopAddress=System.getProperty("user.home") + File.separator + "Desktop";
    
    public static void RemoveBlankPages(File pdfFile)
    {
        PDDocument newDoc =new PDDocument();
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
//            PDPageTree pages = doc.getPages();
            int totalPages = doc.getNumberOfPages();
            PDFTextStripper stripper = new PDFTextStripper();
            
            for(int i =1;i <= totalPages; i++)
            {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                
                String pageText = stripper.getText(doc);
                
                if(pageText.trim().isBlank() && !pageHasVisualContent(doc , i))
                {
                    System.out.println("EMPTY PAGE "+ i);
                }else
                {
                    newDoc.importPage(doc.getPage(i-1));
                }
                
                
            }
            
            Path directoryPath = Paths.get(desktopAddress,"BlankPagesRemovedPDFS");
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            newDoc.save(Paths.get(directoryPath.toString(), pdfFile.getName()+"_NoBlankPages" +".pdf").toFile());
            newDoc.close();
            
        }catch(IOException e)
        {
            System.out.println("Exception from CleanUpService.RemoveBlankPages: "+ e.getMessage());
        }
    }
    
    
    public static boolean pageHasVisualContent(PDDocument doc, int pageIndex)
    {
        try{
              PDPage page = doc.getPage(pageIndex-1);
              PDResources resources = page.getResources();
//              System.out.println(resources.getXObjectNames());
              for(COSName name : resources.getXObjectNames())
              {
                PDXObject xObject = resources.getXObject(name);
                if(xObject instanceof PDImageXObject)
                {
                    return true;
                }
                
              }
        }catch(IOException ex)
        {
            System.getLogger(ContentExtractionService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
}
