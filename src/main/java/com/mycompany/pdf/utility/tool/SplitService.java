/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.Alert;
/**
 *
 * @author swast
 */
public class SplitService {
    private static String desktopAddress=System.getProperty("user.home") + File.separator + "Desktop";
    
    
    
    public static void SplitPagesViaOptions(File pdfFile, int opt) 
    {
        switch(opt)
        {
            case 0:
                SplitIntoPages(pdfFile);
                break;
            case 1:
                ExtractFirstPage(pdfFile);
                break;
            case 2:
                ExtractLastPage(pdfFile);
                break;
            case 3:
                ExtractEvenPages(pdfFile);
                break;
            case 4:
                ExtractOddPages(pdfFile);
                break;
            default:
                
        }
    }
    
    /**
     * Splits the Given PDF Into Pages and saves the result on the Desktop.
     * 
     * @param pdfFile    Input PDF file
     */
    private static void SplitIntoPages(File pdfFile)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            Splitter splitter= new Splitter();
            
            List<PDDocument> pages = splitter.split(doc);
            int pgNo=1;
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName()+"_Pages");
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            for (PDDocument pageDoc : pages)
            {
                pageDoc.save(Paths.get(directoryPath.toString(),"page_"+ pgNo +".pdf").toFile());
                pageDoc.close();
                pgNo++;
            }
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Splitted Pages successfully created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
//            pages=null;
        }catch(IOException e)
        {
            System.out.println("Exception from SplitService.SplitIntoPages: "+ e.getMessage());
        }
    }
    
    /**
     * Splits the Given PDF After given Page Number and saves the result on the Desktop.
     * Indexing Starts From 1
     * @param pdfFile    Input PDF file
     * @param index       Page Number After which pdf is Splitted (INCLUSIVE)
     */
    public static void SplitPDFAtPage( File pdfFile, int index)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            Splitter splitter= new Splitter();
            splitter.setSplitAtPage(index);
            
            List<PDDocument> pdfs = splitter.split(doc);
//            System.out.println(pdfs.size());
            
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName()+"_Splitted");
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            int partNo=1;
            for (PDDocument pdfDoc : pdfs)
            {
                pdfDoc.save(Paths.get(directoryPath.toString(),"part_"+ partNo +".pdf").toFile());
                pdfDoc.close();
                partNo++;
            }  
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Splitted PDFs successfully created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
        }catch(IOException e)
        {
            System.out.println("Exception from SplitService.SplitSplitPDFAtPage: "+ e.getMessage());
        }
    }
    
    /**
     * Splits the Given PDF and extracts a pdf with pages between given page Numbers and saves the result on the Desktop.
     * Indexing Starts From 1
     * @param pdfFile    Input PDF file
     * @param startPageNumber       Page Number From which the spitted PDF starts (INCLUSIVE)
     * @param endPageNumber         Page Number At which the spitted PDF Ends (INCLUSIVE)
     */
    public static void SplitFromTo(File pdfFile, int startPageNumber,int endPageNumber)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            Splitter splitter= new Splitter();
            splitter.setStartPage(startPageNumber);
            splitter.setEndPage(endPageNumber);
            splitter.setSplitAtPage(endPageNumber-startPageNumber+1);
            List<PDDocument> splittedPDF = splitter.split(doc);
            
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName());
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            splittedPDF.get(0).save(Paths.get(directoryPath.toString(),"Page"+startPageNumber+ "-"+ endPageNumber +".pdf").toFile());
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Splitted PDF successfully created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
        }catch(IOException e)
        {
            System.out.println("Exception from SplitService.SplitFromTo: "+ e.getMessage());
        }
    }
    
    /**
     * Extracts all the odd number Pages from Given PDF saves the result on the Desktop.
     * @param pdfFile    Input PDF file
     */
    private static void ExtractOddPages(File pdfFile)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            int totalPages= doc.getNumberOfPages();
            
            PDDocument oddPagesPDF = new PDDocument();
            for(int i = 1; i <= totalPages; i+=2)
            {
                oddPagesPDF.importPage(doc.getPage(i-1));
            }
            
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName());
            if (Files.notExists(directoryPath))
            {
                Files.createDirectory(directoryPath);
            }
            oddPagesPDF.save(Paths.get(directoryPath.toString(),"All_OddPages"+".pdf").toFile());
            oddPagesPDF.close();
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Odd Pages created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
            
        }catch(IOException e)
        {
            System.out.println("Exception from SplitService.ExtractOddPages: "+ e.getMessage());
        }
    }
    
    /**
     * Extracts all the even number Pages from Given PDF saves the result on the Desktop.
     * @param pdfFile    Input PDF file
     */
    private static void ExtractEvenPages(File pdfFile)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            int totalPages= doc.getNumberOfPages();
            
            PDDocument oddPagesPDF = new PDDocument();
            for(int i = 1; i <= totalPages; i+=2)
            {
                oddPagesPDF.importPage(doc.getPage(i));
            }
            
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName());
            if (Files.notExists(directoryPath))
            {
                Files.createDirectory(directoryPath);
            }
            oddPagesPDF.save(Paths.get(directoryPath.toString(),"All_EvenPages"+".pdf").toFile());
            oddPagesPDF.close();
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Even Pages created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
        }catch(IOException e)
        {
            System.out.println("Exception from SplitService.ExtractOddPages: "+ e.getMessage());
        }
    }
    
    /**
     * Extracts the very FIRST Page Of Given PDF saves the result on the Desktop.
     * @param pdfFile    Input PDF file
     * @param pages     Array(Int) of Page numbers (Indexing=1)
     */
    public static void ExtractPages(File pdfFile, int[] pages){
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            int totalPages= doc.getNumberOfPages();
            
            PDDocument selectedPagesPDF = new PDDocument();
            for(int i = 1; i <= totalPages; i++)
            {
                for(int pgNo : pages)
                {
                    if(pgNo==i)
                    {
                        selectedPagesPDF.importPage(doc.getPage(i-1));
                    }
                }
                
            }
            
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName());
            if (Files.notExists(directoryPath))
            {
                Files.createDirectory(directoryPath);
            }
            selectedPagesPDF.save(Paths.get(directoryPath.toString(),"SelectedPages"+".pdf").toFile());
            selectedPagesPDF.close();
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Pages: "+ pages +" created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
            
        }catch(IOException e)
        {
            System.out.println("Exception from SplitService.ExtractOddPages: "+ e.getMessage());
        }
    }
    
        
    
    
    /**
     * Extracts the very FIRST Page Of Given PDF saves the result on the Desktop.
     * @param pdfFile    Input PDF file
     */
    private static void ExtractFirstPage(File pdfFile)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            Splitter splitter= new Splitter();
            splitter.setStartPage(1);
            splitter.setEndPage(1);
            
            List<PDDocument> pages = splitter.split(doc);
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName());
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            pages.get(0).save(Paths.get(directoryPath.toString(),"First_Page"+ ".pdf").toFile());
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("First Page PDF created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
        }catch (IOException e) {
            System.getLogger("Exception from SplitService.ExtractFirstPage: "+ e.getMessage());
        }
    }
    
    /**
     * Extracts the very LAST Page Of Given PDF saves the result on the Desktop.
     * @param pdfFile    Input PDF file
     */
    private static void ExtractLastPage(File pdfFile)
    {
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            Splitter splitter= new Splitter();
            splitter.setStartPage(doc.getNumberOfPages());
            splitter.setEndPage(doc.getNumberOfPages());
            
            List<PDDocument> pages = splitter.split(doc);
            Path directoryPath = Paths.get(desktopAddress, pdfFile.getName());
            if (Files.notExists(directoryPath)) 
            {
                Files.createDirectory(directoryPath);
            }
            
            pages.get(0).save(Paths.get(directoryPath.toString(),"Last_Page"+ ".pdf").toFile());
            
            // SUCCESS POPUP
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Split Complete");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Last Page PDF created at:\n" + directoryPath.toString() );
            successAlert.showAndWait();
            
        }catch (IOException e) {
            System.getLogger("Exception from SplitService.ExtractFirstPage: "+ e.getMessage());
        }
    }
}
