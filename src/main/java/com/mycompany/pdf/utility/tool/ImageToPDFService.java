/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author swast
 */
public class ImageToPDFService {
    
    private static String desktopAddress=System.getProperty("user.home") + File.separator + "Desktop";
    
    public static void ImageToPDF(List<File> imageFiles, String outputFileName,int inputFormat)
    {
        try(PDDocument doc = new PDDocument())
        {
            for(File imageFile : imageFiles)
            {
                BufferedImage bimage = ImageIO.read(imageFile);
                if(bimage==null) continue;
                
                PDPage page = new PDPage(new PDRectangle(bimage.getWidth(), bimage.getHeight()));
                doc.addPage(page);
                
                PDImageXObject pdImage = null;
                switch(inputFormat)
                {
                    case 0 -> pdImage = LosslessFactory.createFromImage(doc, bimage);
                    case 1 -> pdImage = JPEGFactory.createFromImage(doc, bimage,0.75f);
                }
                
                
//                PDImageXObject pdIdage = JPEGFactory.createFromImage(doc, bimage,0.75f);
                try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) 
                {
                    contentStream.drawImage(pdImage, 0, 0,bimage.getWidth(),bimage.getHeight());
                }
                
            }
            
            Path directoryPath = null;
            switch(inputFormat)
            {
                case 0 -> directoryPath = Paths.get(desktopAddress,"PNG_PDF");
                case 1 -> directoryPath = Paths.get(desktopAddress,"JPEG_PDF");
            }
             
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            doc.save(Paths.get(directoryPath.toString(), outputFileName +".pdf").toFile());
            doc.close();
            

        }catch(IOException ex)
        {
            
        }
    }
}
