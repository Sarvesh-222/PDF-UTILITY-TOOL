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
import javafx.scene.control.Alert;
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
    
    public static void ImageToPDF(
        List<File> imageFiles,
        String outputFileName,
        int inputFormat
) {

    float margin = 50;
    float spacing = 15;

    try (PDDocument doc = new PDDocument()) {

        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        PDRectangle pageSize = page.getMediaBox();
        float pageWidth  = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();

        float currentY = pageHeight - margin;

        PDPageContentStream contentStream =
                new PDPageContentStream(doc, page);

        for (File imageFile : imageFiles) {

            BufferedImage bufferedImage = ImageIO.read(imageFile);
            if (bufferedImage == null) continue;

            PDImageXObject pdImage = switch (inputFormat) {
                case 1 -> JPEGFactory.createFromImage(doc, bufferedImage, 0.75f);
                default -> LosslessFactory.createFromImage(doc, bufferedImage);
            };

            float imgWidth  = pdImage.getWidth();
            float imgHeight = pdImage.getHeight();
            
            
            float maxWidth = pageWidth - 2 * margin;

            // If not enough vertical space at all, start a new page FIRST
            if (currentY <= margin) {
                contentStream.close();

                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                contentStream = new PDPageContentStream(doc, page);
                currentY = pageHeight - margin;
            }

            // Now maxHeight is guaranteed positive
            float maxHeight = currentY - margin;

            float scale = Math.min(
                    maxWidth / imgWidth,
                    maxHeight / imgHeight
            );

            // Prevent upscaling
            scale = Math.min(scale, 1.0f);

            float drawWidth  = imgWidth  * scale;
            float drawHeight = imgHeight * scale;


            // New page if needed
            if (currentY - drawHeight < margin) {
                contentStream.close();

                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                contentStream = new PDPageContentStream(doc, page);
                currentY = pageHeight - margin;
            }

            contentStream.drawImage(
                    pdImage,
                    margin,
                    currentY - drawHeight,
                    drawWidth,
                    drawHeight
            );

            currentY -= drawHeight + spacing;
        }

        contentStream.close();

        Path directoryPath = switch (inputFormat) {
            case 1 -> Paths.get(desktopAddress, "JPEG_PDF");
            default -> Paths.get(desktopAddress, "PNG_PDF");
        };

        Files.createDirectories(directoryPath);

        doc.save(directoryPath.resolve(outputFileName + ".pdf").toFile());
        
        // SUCCESS POPUP
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("PDF Generation Complete");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Images PDF is created at:\n" + directoryPath.toString() );
        successAlert.showAndWait();

    } catch (IOException ex) {
        ex.printStackTrace(); // NEVER swallow exceptions
    }
}

}
