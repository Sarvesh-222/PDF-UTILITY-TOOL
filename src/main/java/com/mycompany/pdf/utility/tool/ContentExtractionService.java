/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pdf.utility.tool;

//import static com.mycompany.pdf.utility.tool.TextWriter.leadingAmount;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author swast
 */
public class ContentExtractionService {
    
    private static final String desktopAddress=System.getProperty("user.home") + File.separator + "Desktop";
    
    /**
     * Extracts The Images from Give PDF & returns a List<BufferedImage> images
     * @param pdfFile   Input PDF File
     * @return          List of BufferedImage
    */
    public static List<BufferedImage> ExtractImages(File pdfFile)
    {
        List<BufferedImage> images = new ArrayList<>();
        
        try(PDDocument doc = PDDocument.load(pdfFile))
        {
            for(PDPage page : doc.getPages() )
            {
                PDResources resources = page.getResources();

                for(COSName name : resources.getXObjectNames())
                {
                    PDXObject xObject = resources.getXObject(name);
                    if(xObject instanceof PDImageXObject)
                    {

                        PDImageXObject src = (PDImageXObject) xObject;
                        BufferedImage bImage = src.getImage();
                        if(bImage!=null){
                            images.add(bImage);
                        }   
                    }
                }
            }
        }
        catch(IOException ex)
        {
            System.getLogger(ContentExtractionService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        return images;
    }
    
    
    /**
     * Converts The images from Give List of BufferedImage and 
     * saves Them in a PDF in a folder on Desktop
     * @param fileName   Output PDF File Name
     * @param images     List of BufferedImage
     * @param type       Type of images(PNG:0,JPEG:1) in PDF
    */
    public static void MakeImagePDF(String fileName, List<BufferedImage> images,int type) {
        try (PDDocument imagePDF = new PDDocument()) {

            float margin = 10;
            PDPage page = new PDPage();
            imagePDF.addPage(page);

            PDPageContentStream content = new PDPageContentStream(imagePDF, page);

            float y = page.getMediaBox().getHeight() - margin;

            for (BufferedImage bi : images) {
                PDImageXObject image;
                
                switch (type) {
                case 1 -> image = JPEGFactory.createFromImage(imagePDF, bi, 0.75f);
                default -> image = LosslessFactory.createFromImage(imagePDF, bi);
                }
                
                
                float imgHeight = image.getHeight();

                if (y - imgHeight < margin) {
                    content.close();
                    page = new PDPage();
                    imagePDF.addPage(page);
                    content = new PDPageContentStream(imagePDF, page);
                    y = page.getMediaBox().getHeight() - margin;
                }

                content.drawImage(image, margin, y - imgHeight);
                y -= imgHeight + 15;
            }

            content.close();

            Path directoryPath = Paths.get(desktopAddress, "ExtractedImages");
            Files.createDirectories(directoryPath);

            imagePDF.save(directoryPath.resolve(fileName + "_ExtractedImages.pdf").toFile());

        } catch (IOException e) {
            System.getLogger(ContentExtractionService.class.getName())
                  .log(System.Logger.Level.ERROR, "PDF creation failed", e);
        }
    }
    
    /**
     * Converts The images from Give List of BufferedImage in given Type and Saves them,
     * separately in a folder on Desktop
     * @param fileName   Output PDF File Name
     * @param images     List of BufferedImage 
     * @param type       Type of images(PNG:0,JPEG:1) in PDF
    */
    public static void SaveImages(String fileName, List<BufferedImage> images,int type)
    {
        String savePath = null;
        Path directoryPath=null;
        Integer imgNo=1;
        try {
            directoryPath = Paths.get(desktopAddress, "ExtractedImages", fileName+"_Images");
            Files.createDirectories(directoryPath);
            
        } catch (IOException ex) {
            System.getLogger(ContentExtractionService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        for(BufferedImage bi : images)
        {
            savePath = Paths.get(directoryPath.toString(), File.separator,imgNo.toString()).toString();
            
            try{
                
                switch (type) {
                case 1 -> ImageIO.write(bi,"JPEG",new File(savePath+".jpeg"));
                default -> ImageIO.write(bi,"PNG",new File(savePath+".png"));
                }
            }catch (IOException ex)
            {
                System.getLogger(ContentExtractionService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            imgNo++;
        }
    }
    
        public static void ExtractTextPageWise(
            File pdfFile,
            PDFont font,
            float fontSize,
            float margin,
            boolean saveInMultipleFiles,
            String format) // "pdf" or "txt", only used when saveInMultipleFiles=true
        {
        try (PDDocument doc = PDDocument.load(pdfFile)) {

            int totalPages = doc.getNumberOfPages();
            PDFTextStripper stripper = new PDFTextStripper();

            // Directory to save results
            Path directoryPath = Paths.get(desktopAddress, "ExtractedTextPDFS");
            if (Files.notExists(directoryPath)) {
                Files.createDirectory(directoryPath);
            }

            if (!saveInMultipleFiles) {
                // ---------------- Single File ----------------
                PDDocument textPDF = new PDDocument();

                for (int i = 1; i <= totalPages; i++) {
                    stripper.setStartPage(i);
                    stripper.setEndPage(i);
                    String pageText = stripper.getText(doc).trim();
                    if (pageText.isBlank()) continue;

                    PDPage newPage = new PDPage();
                    textPDF.addPage(newPage);

                    float yStart = newPage.getMediaBox().getHeight() - margin;
                    float maxWidth = newPage.getMediaBox().getWidth() - 2 * margin;

                    TextWriter.writeText(textPDF, newPage, pageText, font, fontSize, margin, yStart, maxWidth);
                }

                Path outPath = Paths.get(directoryPath.toString(),
                        pdfFile.getName() + "_ExtractedText(PageWise).pdf");
                textPDF.save(outPath.toFile());
                textPDF.close();

            } else {
                // ---------------- Multiple Files ----------------
                for (int i = 1; i <= totalPages; i++) {
                    stripper.setStartPage(i);
                    stripper.setEndPage(i);
                    String pageText = stripper.getText(doc).trim();
                    if (pageText.isBlank()) continue;

                    if (format.equalsIgnoreCase("txt")) {
                        // Save each page as TXT
//                        Path textDirectortPath = Paths.get(directoryPath.toString(),pdfFile.getName()+"_TXT");
                        Path outPath = Paths.get(directoryPath.toString(),
                                pdfFile.getName() + "_Page" + i + ".txt");
                        Files.writeString(outPath, pageText);

                    } else {
                        // Save each page as PDF
                        PDDocument singlePDF = new PDDocument();
                        PDPage newPage = new PDPage();
                        singlePDF.addPage(newPage);

                        float yStart = newPage.getMediaBox().getHeight() - margin;
                        float maxWidth = newPage.getMediaBox().getWidth() - 2 * margin;

                        TextWriter.writeText(singlePDF, newPage, pageText, font, fontSize, margin, yStart, maxWidth);

                        Path outPath = Paths.get(directoryPath.toString(),
                                pdfFile.getName() + "_Page" + i + ".pdf");
                        singlePDF.save(outPath.toFile());
                        singlePDF.close();
                    }
                }
            }

        } catch (IOException ex) {
            System.getLogger(ContentExtractionService.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    
//    public static void ExtractTextPageWise(File pdfFile, PDFont font, float fontSize, float margin, boolean saveInMultipleFiles)
//    {
//        PDDocument textPDF = new PDDocument();
//        
//        try(PDDocument doc = PDDocument.load(pdfFile))
//        {
//            int totalPages = doc.getNumberOfPages();
//            PDFTextStripper stripper = new PDFTextStripper();
//            
//            for(int i =1; i <= totalPages; i++)
//            {
//                stripper.setStartPage(i);
//                stripper.setEndPage(i);
//                
//                String pageText = stripper.getText(doc);
//                pageText = pageText.trim();
//                
//                if(pageText.trim().isBlank())
//                {
////                    System.out.println("EMPTY PAGE "+ i);
//                    continue;
//                }
//
//                
//                PDPage newPage = new PDPage();
//                textPDF.addPage(newPage);
//                
//                float yStart = newPage.getMediaBox().getHeight() - margin;
//                float maxWidth = newPage.getMediaBox().getWidth() - 2 * margin;
//                
//                TextWriter.writeText(textPDF, newPage, pageText, font, fontSize, margin, yStart, maxWidth);       
//            }
//            
//            Path directoryPath = Paths.get(desktopAddress,"ExtractedTextPDFS");
//            if (Files.notExists(directoryPath)) 
//            {
//                Files.createDirectory(directoryPath);
//            }
//            
//            textPDF.save(Paths.get(directoryPath.toString(), pdfFile.getName()+"_ExtractedText(PageWise)" +".pdf").toFile());
//            textPDF.close();
//        }
//        catch(IOException ex)
//        {
//            System.getLogger(ContentExtractionService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//        }
//    }
}


