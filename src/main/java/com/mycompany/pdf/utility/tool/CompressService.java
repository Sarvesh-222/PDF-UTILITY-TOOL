/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.CCITTFactory;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;

public class CompressService {
    private static String desktopAddress=System.getProperty("user.home") + File.separator + "Desktop";
    
/**
 *Compresses the Given PDF File
 * @param pdfFile   Input PDF File
 * @param blackNwhite   TRUE: BLACK AND WHITE, FALSE: COLOR IMAGES
 */
    public static void CompressPDF(File pdfFile,boolean blackNwhite)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            for(PDPage page : doc.getPages())
            {
                PDResources resources = page.getResources();
                for (COSName name : resources.getXObjectNames()) 
                {
                    PDXObject xObject = resources.getXObject(name);

                    if (xObject instanceof PDImageXObject) {
                        PDImageXObject image = (PDImageXObject) xObject;
                        
                         // Extract BufferedImage
                        BufferedImage bImage = image.getImage();

                        // Create compressed JPEG image (lower quality)
                            if(blackNwhite)
                            {
                                BufferedImage biLevelImage = new BufferedImage(
                                    bImage.getWidth(), 
                                    bImage.getHeight(), 
                                    BufferedImage.TYPE_BYTE_BINARY // convert to 1-bit B&W
                                );
                                Graphics2D g = biLevelImage.createGraphics();
                                g.drawImage(bImage, 0, 0, null);
                                g.dispose();
                                PDImageXObject compressedImage = CCITTFactory.createFromImage(doc, biLevelImage);
                                resources.put(name, compressedImage);
                                
                            }else
                            {
                                PDImageXObject compressedImage = JPEGFactory.createFromImage(doc, bImage, 0f);
                                resources.put(name, compressedImage);
                                
                            }
                        
                    }
                }
            }
            
            Path directoryPath = Paths.get(desktopAddress,"CompressedPDFS");
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            doc.save(Paths.get(directoryPath.toString(), pdfFile.getName()+"_Compressed" +".pdf").toFile());
        }catch(IOException ex)
        {
            System.getLogger(ContentExtractionService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
}
