/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
/**
 *
 * DOES NOT HANDLE NEW PAGE GENERATION
 */

public class TextWriter {
    private static int leadingAmount=14;
    public static boolean writeText(PDDocument document,PDPage page,String text, PDFont font, float fontSize,float margin,float yStart,float maxWidth) throws IOException
    {
        List<String> lines = TextWrapper.wrapText(text,font, fontSize, maxWidth);
            try(PDPageContentStream content = new PDPageContentStream(document,page))
            {
                
                content.beginText();
                content.setFont(font,fontSize);
                content.setLeading(leadingAmount);
                content.newLineAtOffset(margin, yStart);
                
                for(String line : lines)
                {
                    content.showText(line);
                    content.newLine();
                }
                content.endText();
                return true;
            }catch(Exception e)
            {
                System.out.println("Exception From wtiteText: "+e.getMessage());
                return false;
            }
    }
    
    /**
     * Sets the Spacing Between Line
     * Default Value = 14
     * @param amt New Line Spacing
     */
    public static void SetLeadingAmount(int amt)
    {
        TextWriter.leadingAmount=amt;
    }
}
