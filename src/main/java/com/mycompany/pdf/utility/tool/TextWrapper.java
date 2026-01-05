/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;
import org.apache.pdfbox.pdmodel.font.PDFont;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author swast
 */
public class TextWrapper {
    
    public static List<String> wrapText(String text,PDFont font,float fontSize,float maxWidth) throws IOException
    {
        List<String> lines= new ArrayList<>();
        String[] words=text.split("\\s+");
        
        StringBuilder line= new StringBuilder();
        
        for(String word : words)
        {
//            System.out.println(line.length());
            String testLine= line + (line.length()==0? "":" ") + word;
            
            float lineSize = font.getStringWidth(testLine) / 1000 * fontSize;
            
            if(lineSize>maxWidth)
            {
                lines.add(line.toString());
                line= new StringBuilder(word);
            }else
            {
                line= new StringBuilder(testLine);
            }
            
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        
        return lines;
    }
}
