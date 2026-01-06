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
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;


import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.cos.COSName;


//import org.apache.pdfbox.pdmodel.graphics.xobject.PDImageXObject;

/**
 *
 * @author swast
 */
public class CleanUpService {
    
    public static void RemoveBlankPages(File pdfFile)
    {
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
                System.out.println("\nPage "+(i)+ " :\n" +pageText );
                
                if(pageText.trim().isBlank())
                {
                    System.out.println("EMPTY PAGE "+ i);
                }
                
                
            }
            
            for (int i = 1; i <= totalPages; i++) {
           
            System.out.println("Page " + (i) + ": " + pageHasVisualContent(doc , i));
           
            }
        }catch(IOException e)
        {
            System.out.println("Exception from CleanUpService.RemoveBlankPages: "+ e.getMessage());
        }
    }
    
    
    public static boolean pageHasVisualContent(PDDocument doc, int pageIndex)
    {
        PDFRenderer renderer = new PDFRenderer(doc);
        try{
            BufferedImage image = renderer.renderImageWithDPI(pageIndex-1, 72);

            // Check for any non-white pixel
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    if ((image.getRGB(x, y) & 0xFFFFFF) != 0xFFFFFF) {
                        return true;
                    }
                }
            }
            
        }catch(IOException e)
        {
            
        }
        return false;
    }
}
