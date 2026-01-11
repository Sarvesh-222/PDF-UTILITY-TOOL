package com.mycompany.pdf.utility.tool;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFUTILITYTOOL extends Application {

    private Stage mainStage;
    private Scene mainScene;
    
    private final List<File> mergePdfFiles = new ArrayList<>();
    private Button mergeBtn;

    @Override
    public void start(Stage stage) {

        this.mainStage = stage;

        VBox mainRoot = createHomePage();
        mainScene = new Scene(mainRoot, 1000, 500);

        stage.setScene(mainScene);
        stage.setTitle("PDF Utility Tool");
        stage.show();
    }

    // --- Step 1: Home Page (Feature Categories) ---
    private VBox createHomePage() {
        Label title = new Label("I Heart PDF Too :)");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

//        StackPane mergeCard = createCard("Merge PDF", "Combine multiple PDFs", Color.web("#ff4d4d"), "merge.png",
//                () -> mainScene.setRoot(createFeaturePage("Merge PDF")));
        StackPane mergeCard = createCard(
        "Merge PDF",
        "Combine multiple PDFs",
        Color.web("#ff4d4d"),
        "merge.png",
        () -> mainScene.setRoot(createMergeWorkspace())
        );
        StackPane splitCard = createCard("Split PDF", "Split PDFs into parts", Color.web("#ff9933"), "split.png",
                () -> mainScene.setRoot(createFeaturePage("Split PDF")));
        StackPane compressCard = createCard("Compress PDF", "Reduce PDF size", Color.web("#33cc33"), "compress.png",
                () -> mainScene.setRoot(createFeaturePage("Compress PDF")));
        StackPane extractCard = createCard("Extract", "Extract contents", Color.web("#3399ff"), "convert.png",
                () -> mainScene.setRoot(createFeaturePage("Extract PDF")));
        StackPane cleanupCard = createCard("Clean Up", "Remove unwanted contents", Color.web("#ff66cc"), "convert.png",
                () -> mainScene.setRoot(createFeaturePage("Clean Up PDF")));

        HBox cardRow = new HBox(20, mergeCard, splitCard, compressCard, extractCard, cleanupCard);
        cardRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(40, title, cardRow);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }

    // --- Step 2: Feature Page (All options under a feature) ---
    private VBox createFeaturePage(String featureName) {
    Label title = new Label(featureName);
    title.setFont(Font.font("Arial", 28));
    title.setStyle("-fx-font-weight: bold;");

    List<String> options = new ArrayList<>();
    List<Node> optionsCards = new ArrayList<>();

    switch(featureName) {
        case "Merge PDF":
            options.add("Merge Two PDFs");
            options.add("Merge Multiple PDFs");
            break;
        case "Split PDF":
            options.add("Split PDF Into Pages");
            options.add("Split PDF At A Page");
            options.add("Split PDF By Range");
            options.add("Split All Odd Number Pages");
            options.add("Split All Even Number Pages");
            options.add("Split First Page");
            options.add("Split Last Page");
            break;
        case "Compress PDF":
            options.add("Normal Compression\n(Color)");
            options.add("Heavy Compression\n(Black & White)");
            break;
        case "Extract PDF":
            options.add("Extract Images");
            options.add("Extract Text");
            break;
        case "Clean Up PDF":
            options.add("Remove Blank\nPages");
            break;
    }

    // Create cards for each option
    for (String optionName : options) {
        StackPane optionN = createCard(optionName, "", Color.web("#ff6666"), "merge.png",
                () -> mainScene.setRoot(createFinalPage(optionName)));
        optionsCards.add(optionN);
    }

    // Convert List<Node> to Node[] correctly
    HBox cardRow = new HBox(20, optionsCards.toArray(new Node[0]));
    cardRow.setAlignment(Pos.CENTER);

    Button backBtn = new Button("← Back");
    backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

    VBox root = new VBox(40, title, cardRow, backBtn);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(40));
    root.setStyle("-fx-background-color: #f2f2f2;");
    options = null;
    optionsCards = null;

    return root;
}


    // --- Step 3: Final Page (Work Page Placeholder) ---
    private VBox createFinalPage(String optionName) {
        Label title = new Label(optionName);
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        Label desc = new Label("This is a placeholder page for " + optionName);
        desc.setFont(Font.font("Arial", 18));

        Button backBtn = new Button("← MainPage");
        backBtn.setOnAction(e -> mainScene.setRoot(createHomePage())); // Back to feature page

        VBox root = new VBox(30, title, desc, backBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }

    // --- Card Creator ---
    private StackPane createCard(String text, String description, Color color, String iconFileName, Runnable onClick) {
        StackPane card = new StackPane();
        card.setPrefSize(200, 200);
        card.setStyle("-fx-background-color: " + toRgbString(color) + "; -fx-background-radius: 15;");
        card.setEffect(new DropShadow(10, Color.gray(0.3)));

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/" + iconFileName)));
        icon.setFitWidth(50);
        icon.setFitHeight(50);

        Label label = new Label(text);
        label.setFont(Font.font("Comic Sans MS", 17));
        label.setTextFill(Color.WHITE);

        Label des = new Label(description);
        des.setFont(Font.font("Comic Sans MS", 12));
        des.setAlignment(Pos.CENTER);
        des.setTextFill(Color.WHITE);

        VBox content = new VBox(10, icon, label, des);
        content.setAlignment(Pos.CENTER);
        card.getChildren().add(content);

        // Hover animation
        ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
        card.setOnMouseEntered(e -> { st.setToX(1.05); st.setToY(1.05); st.playFromStart(); });
        card.setOnMouseExited(e -> { st.setToX(1.0); st.setToY(1.0); st.playFromStart(); });

        // Click action
        card.setOnMouseClicked(e -> onClick.run());

        return card;
    }

    private String toRgbString(Color c) {
        return String.format("rgb(%d, %d, %d)", (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
    }
    
    
   private StackPane createPdfPreviewCard(Image preview, int pageCount, File sourceFile, FlowPane parent) {

        ImageView iv = new ImageView(preview);
        iv.setFitWidth(140);
        iv.setPreserveRatio(true);

        Label pages = new Label(pageCount + " pages");
        pages.setFont(Font.font(14));
        pages.setTextFill(Color.WHITE);

        VBox content = new VBox(10, iv, pages);
        content.setAlignment(Pos.CENTER);

        // Delete button
        Button deleteBtn = new Button("✕");
        deleteBtn.setPrefSize(22, 22);
        deleteBtn.setMinSize(22, 22);
        deleteBtn.setMaxSize(22, 22);

        deleteBtn.setStyle("""
            -fx-background-color: rgba(0, 0, 0, 0.55);
            -fx-background-radius: 11;
            -fx-text-fill: white;
            -fx-font-size: 12;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            -fx-padding: 0;
        """);

        StackPane.setAlignment(deleteBtn, Pos.TOP_RIGHT);
//        StackPane.setMargin(deleteBtn, new Insets(2));

        StackPane card = new StackPane(content, deleteBtn);
        card.setPrefSize(180, 220);
        card.setStyle("""
            -fx-background-color: #333;
            -fx-background-radius: 15;
        """);

        card.setEffect(new DropShadow(10, Color.gray(0.4)));

        // Delete behavior
        deleteBtn.setOnAction(e -> {
            parent.getChildren().remove(card);
            mergePdfFiles.remove(sourceFile);
            updateMergeButtonState();
        });

        return card;
    }

    
    private Image renderFirstPage(PDDocument doc) throws Exception {

        PDFRenderer renderer = new PDFRenderer(doc);
        BufferedImage img = renderer.renderImageWithDPI(0, 120);

        return SwingFXUtils.toFXImage(img, null);
    }
    
    private void openPdfChooser(FlowPane pdfContainer) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Select PDF");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = fc.showOpenDialog(mainStage);
        if (file == null) return;
        mergePdfFiles.add(file);
        updateMergeButtonState();

        try (PDDocument doc = PDDocument.load(file)) {

            int pageCount = doc.getNumberOfPages();
            Image preview = renderFirstPage(doc);

            StackPane card = createPdfPreviewCard(preview, pageCount, file, pdfContainer);
            pdfContainer.getChildren().add(card);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private StackPane createAddPdfButton(FlowPane pdfContainer) {

        Label plus = new Label("+");
        plus.setFont(Font.font(30));
        plus.setTextFill(Color.WHITE);

        StackPane button = new StackPane(plus);
        button.setPrefSize(50, 50);
        button.setStyle("""
            -fx-background-color: #ff4d4d;
            -fx-background-radius: 25;
            -fx-cursor: hand;
        """);

        button.setEffect(new DropShadow(10, Color.gray(0.3)));

        button.setOnMouseClicked(e -> openPdfChooser(pdfContainer));

        return button;
    }
    
    
    private VBox createMergeWorkspace() {

    // --- Title ---
    Label title = new Label("Merge PDF");
    title.setFont(Font.font("Arial", 28));
    title.setStyle("-fx-font-weight: bold;");

    // --- PDF Cards Container ---
    FlowPane pdfContainer = new FlowPane(20, 20);
    pdfContainer.setPadding(new Insets(20));
    pdfContainer.setAlignment(Pos.CENTER_LEFT);

    // --- + Add PDF Button ---
    StackPane addButton = createAddPdfButton(pdfContainer);

    // --- Top Bar ---
    BorderPane topBar = new BorderPane();
    topBar.setLeft(title);
    topBar.setRight(addButton);
    topBar.setPadding(new Insets(10));

    // --- Output file name input ---
    Label outputLabel = new Label("Output File Name:");
    outputLabel.setFont(Font.font(14));

    TextField outputField = new TextField("MergedFiles");
    // Remove extension if user typed it
    String text = outputField.getText();
    outputField.setPrefWidth(200);

    HBox outputBox = new HBox(10, outputLabel, outputField);
    outputBox.setAlignment(Pos.CENTER_LEFT);
    outputBox.setPadding(new Insets(0, 0, 10, 20));

    // --- Buttons (Bottom Right) ---
    VBox bottomRightControls = createRightSideControls(pdfContainer, outputField);

    // --- Base Layout ---
    BorderPane root = new BorderPane();
    root.setTop(topBar);
    root.setCenter(pdfContainer);
    root.setBottom(bottomRightControls);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #f2f2f2;");

    // --- Include outputBox above buttons ---
    VBox bottomBox = new VBox(10, outputBox, bottomRightControls);
    root.setBottom(bottomBox);

    return new VBox(root);
}
    
    private VBox createRightSideControls(FlowPane pdfContainer, TextField outputField) {
        Button backBtn = new Button("← Back");
    backBtn.setOnAction(e -> {
        mergePdfFiles.clear();
        pdfContainer.getChildren().clear();
        updateMergeButtonState();
        mainScene.setRoot(createHomePage());
    });

    mergeBtn = new Button("Merge PDFs");
    mergeBtn.setDisable(true);
    mergeBtn.setStyle("""
        -fx-background-color: #ff4d4d;
        -fx-text-fill: white;
        -fx-font-size: 16;
        -fx-font-weight: bold;
        -fx-padding: 10 25;
    """);

    mergeBtn.setOnAction(e -> {
            try {
                MergeService.MergePDFs(mergePdfFiles, outputField.getText());
            } catch (IOException ex) {
                System.getLogger(PDFUTILITYTOOL.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

    Button clearBtn = new Button("Clear All");
    clearBtn.setStyle("""
        -fx-background-color: #777;
        -fx-text-fill: white;
        -fx-padding: 8 20;
    """);

    clearBtn.setOnAction(e -> {
        mergePdfFiles.clear();
        pdfContainer.getChildren().clear();
        updateMergeButtonState();
    });

    VBox box = new VBox(10, backBtn, mergeBtn, clearBtn);
    box.setAlignment(Pos.BOTTOM_RIGHT);

    return box;
    }

    
    
    
    private void updateMergeButtonState() {
     mergeBtn.setDisable(mergePdfFiles.size() < 2);
    }


    public static void main(String[] args) {
        launch(args);
    }


   
    
//
//    public static void main(String[] args) throws IOException {
//        Scanner s= new Scanner(System.in);
////        
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
//        File pdfFile= new File("C:\\Users\\swast\\Downloads\\sample.pdf");
//        File pdfFile= new File("C:\\Users\\swast\\Downloads\\Vigneshwar Holidays Winter Season - Final.pdf");
//        MergeService.MergeTWOPDFS(ad, add, "MERGED");
//        SplitService.SplitIntoPages(pdfFile);
//        SplitService.SplitPDFAtPage(pdfFile, 2);
//        SplitService.SplitFromTo(pdfFile, 2, 4);

//        SplitService.ExtractOddPages(pdfFile);
//        SplitService.ExtractEvenPages(pdfFile);
//        int[] pgs={1,5,2};
//          SplitService.ExtractPages(pdfFile,pgs );


//        CleanUpService.RemoveBlankPages(pdfFile);
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
        
//    }
    
    
}
