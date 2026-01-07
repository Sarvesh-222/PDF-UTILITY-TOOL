/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pdf.utility.tool;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
///import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.util.Scanner;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
/**
 *
 * @author swast
 */


public class PDFUTILITYTOOL {
    
    
    
    

    public static void main(String[] args) throws IOException {
        Scanner s= new Scanner(System.in);
//        
//        String ad=s.nextLine();
//        String add=s.nextLine();
//        s.close();
//        List<File> files=new ArrayList<File>();
        
//        for(int i=0; i<5;i++)
//        {
//            String add=s.nextLine();
//            File file= new File(add);
//            files.add(file);
//        }
//        MergeService.MergePDFs(files, "Merged");
//        files=null;
//        s.close();
        File pdfFile= new File("C:\\Users\\swast\\Downloads\\sample.pdf");
//        File pdfFile= new File("C:\\Users\\swast\\Downloads\\Vigneshwar Holidays Winter Season - Final.pdf");
//        MergeService.MergeTWOPDFS(ad, add, "MERGED");
//        SplitService.SplitIntoPages(pdfFile);
//        SplitService.SplitPDFAtPage(pdfFile, 2);
//        SplitService.SplitFromTo(pdfFile, 2, 4);

//        SplitService.ExtractOddPages(pdfFile);
//        SplitService.ExtractEvenPages(pdfFile);
//        int[] pgs={1,5,2};
//          SplitService.ExtractPages(pdfFile,pgs );


        CleanUpService.RemoveBlankPages(pdfFile);
//        CompressService.CompressPDF(pdfFile,false);
//        CompressService
        
//        ContentExtractionService.ExtractImages(pdfFile);
//        float margin = 50;
//        float yStart = page.getMediaBox().getHeight() - margin;
//        float width = page.getMediaBox().getWidth() - 2 * margin;
//        ContentExtractionService.ExtractTextPageWise(pdfFile,PDType1Font.HELVETICA,12,margin);
            
//        System.out.println("Hello World!");
//        try(PDDocument document = new PDDocument())
//        {
//            
//    
//            PDPage page=new PDPage();
//            document.addPage(page);
//            float margin = 50;
//            float yStart = page.getMediaBox().getHeight() - margin;
//            float width = page.getMediaBox().getWidth() - 2 * margin;
//            
////            String text = "This is a long paragraph that must be wrapped properly "
////            + "inside the defined margins using Apache PDFBox.";
////            TextWriter.writeText(document, page, text, PDType1Font.HELVETICA, 12, margin, yStart, width);
////            document.save("SAMPLEE.pdf");
//               
//        }catch(Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
        
    }
}
