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
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFUTILITYTOOL extends Application {
    
    private enum SplitMode {
        ODD_PAGES,
        EVEN_PAGES,
        FIRST_PAGE,
        LAST_PAGE
    }

    private Stage mainStage;
    private Scene mainScene;
    
    private final List<File> addedPdfFiles = new ArrayList<>();
    private Button mergeBtn;
    private Button splitBtn;
    
    private boolean isMergePage =false;
    private boolean isSplitIntoPagesPage =false;
    private boolean isSplitAtPagesPage =false;
    private boolean isSplitByRangePage =false;

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
        addedPdfFiles.clear();
        isMergePage=false;
        isSplitIntoPagesPage = false;
        isSplitAtPagesPage = false;
        isSplitByRangePage=false;
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
                () -> mainScene.setRoot(createCompressWorkspace()));
        
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

        case "Split PDF":
            options.add("Split PDF Into Pages");
            options.add("Split PDF At A Page");
            options.add("Split PDF By Range");
            break;
        
        case "Extract PDF":
            options.add("Extract Text");
            options.add("Extract Images");
            break;
        case "Clean Up PDF":
            options.add("Remove Blank\nPages");
            break;
    }

    // Create cards for each option
    StackPane optionN= null;
    for (String optionName : options) {
        
        switch(optionName)
        {
            case "Split PDF Into Pages":
                optionN = createCard(optionName, "", Color.web("#ff6666"), "merge.png",
                () -> mainScene.setRoot(createSplitIntoPagesWorkspace()));
//                 optionsCards.add(optionN);
                break;
            case "Split PDF At A Page":
                optionN = createCard(optionName, "", Color.web("#ff6666"), "merge.png",
                () -> mainScene.setRoot(createSplitAtPageWorkspace()));
//                optionsCards.add(optionN);
                break;
            case "Split PDF By Range":
                optionN = createCard(optionName, "", Color.web("#ff6666"), "merge.png",
                () -> mainScene.setRoot(createSplitByRangeWorkspace()));
//                optionsCards.add(optionN);
                break;
            case "Extract Text":
                optionN = createCard(optionName, "", Color.web("#ff6666"), "merge.png",
                () -> mainScene.setRoot(createExtractTextWorkspace()));
//                optionsCards.add(optionN);
                break;
            case "Extract Images":
                optionN = createCard(optionName, "", Color.web("#ff6666"), "merge.png",
                () -> mainScene.setRoot(createExtractImagesWorkspace()));
//                optionsCards.add(optionN);
                break;
                
        }
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
            -fx-background-color: #f2f2f2;
            -fx-background-radius: 15;
        """);

        card.setEffect(new DropShadow(10, Color.gray(0.4)));

        // Delete behavior
        deleteBtn.setOnAction(e -> {
            parent.getChildren().remove(card);
            addedPdfFiles.remove(sourceFile);
            if(isMergePage) updateMergeButtonState();
            if(isSplitIntoPagesPage || isSplitAtPagesPage || isSplitByRangePage) UpdateSplitPDFButtonState();
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
        addedPdfFiles.add(file);
//        System.out.println(isSplitIntoPagesPage);
        if(isMergePage)updateMergeButtonState();
        if(isSplitIntoPagesPage || isSplitAtPagesPage || isSplitByRangePage)UpdateSplitPDFButtonState();

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

        DropShadow shadow = new DropShadow(10, Color.gray(0.3));
        button.setEffect(shadow);

        // --- Hover animation ---
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(120), button);
        scaleIn.setToX(1.08);
        scaleIn.setToY(1.08);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(120), button);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        button.setOnMouseEntered(e -> {
            shadow.setRadius(16);
            scaleIn.playFromStart();
        });

        button.setOnMouseExited(e -> {
            shadow.setRadius(10);
            scaleOut.playFromStart();
        });

        button.setOnMouseClicked(e -> openPdfChooser(pdfContainer));

        return button;
    }
    
    
    private BorderPane createMergeWorkspace() {
    isMergePage=true;
    // ---------- Title ----------
    Label title = new Label("Merge PDF");
    title.setFont(Font.font("Arial", 28));
    title.setAlignment(Pos.CENTER);
    title.setStyle("-fx-font-weight: bold;");

    // ---------- PDF Cards ----------
    FlowPane pdfContainer = new FlowPane(20, 20);
    pdfContainer.setPadding(new Insets(20));
    pdfContainer.setAlignment(Pos.TOP_LEFT);

    ScrollPane scrollPane = new ScrollPane(pdfContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setPrefWidth(750);

    // ---------- Add Button ----------
    StackPane addButton = createAddPdfButton(pdfContainer);

    // ---------- Top Bar ----------
    BorderPane topBar = new BorderPane();
    topBar.setCenter(title);
    topBar.setPadding(new Insets(10));
    topBar.setStyle("""
        -fx-background-color: #eaeaea;
        -fx-border-color: #ccc;
        -fx-border-width: 0 0 0 1;
    """);

    // ---------- Output Name ----------
    Label outputLabel = new Label("Output File Name:");
    TextField outputField = new TextField("MergedFiles");
    outputField.setPrefWidth(200);
   
    HBox outputBox = new HBox(10, outputLabel, outputField);
    outputBox.setPadding(new Insets(10));
    outputBox.setAlignment(Pos.CENTER_LEFT);
    topBar.setBottom(outputBox);

    VBox topSection = new VBox(10, topBar);

    // ---------- Right Controls ----------
    VBox rightControls = createMergePDFRightSideControls(pdfContainer, outputField);
    rightControls.setAlignment(Pos.BOTTOM_CENTER);

    // ---------- Right Sidebar ----------
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    VBox rightSidebar = new VBox(20);
    rightSidebar.setPadding(new Insets(20));
    rightSidebar.setPrefWidth(220);
    rightSidebar.setAlignment(Pos.TOP_CENTER);

    rightSidebar.setStyle("""
        -fx-background-color: #eaeaea;
        -fx-border-color: #ccc;
        -fx-border-width: 0 0 0 1;
    """);

    // order matters
    rightSidebar.getChildren().addAll(
        addButton,    // top
        spacer,       // stretches vertically
createMergePDFRightSideControls(pdfContainer, outputField) // bottom
    );

    // ---------- Main Split ----------
    HBox mainContent = new HBox(scrollPane, rightSidebar);
    HBox.setHgrow(scrollPane, Priority.ALWAYS);

    // ---------- Root ----------
    BorderPane root = new BorderPane();
    root.setTop(topSection);
    root.setCenter(mainContent);
    root.setStyle("-fx-background-color: #f2f2f2;");

    return root;
}

    private BorderPane createSplitIntoPagesWorkspace() {
        isSplitIntoPagesPage = true;
        // ---------- Title ----------
        Label title = new Label("Split PDF");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        BorderPane topBar = new BorderPane();
        topBar.setCenter(title);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #dddddd;
            -fx-border-width: 0 0 1 0;
        """);

        // ---------- PDF Card Container ----------
        FlowPane pdfContainer = new FlowPane(20, 20);
        pdfContainer.setAlignment(Pos.CENTER);
        pdfContainer.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(pdfContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // ---------- Add Button ----------
        StackPane addButton = createAddPdfButton(pdfContainer);

        // Disable add button once one PDF is added
        pdfContainer.getChildren().addListener((javafx.collections.ListChangeListener<Node>) c -> {
            addButton.setDisable(pdfContainer.getChildren().size() >= 1);
            addButton.setOpacity(addButton.isDisable() ? 0.5 : 1.0);
        });

        // ---------- Right Sidebar ----------
        VBox rightSidebar = createSplitIntoPagesRightPanel(pdfContainer);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(20, addButton, spacer, rightSidebar);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(260);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("""
            -fx-background-color: #eaeaea;
            -fx-border-color: #ccc;
            -fx-border-width: 0 0 0 1;
        """);

        // ---------- Main Content ----------
        HBox centerContent = new HBox(scrollPane, sidebar);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerContent);
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }
    
    private VBox createSplitIntoPagesRightPanel(FlowPane pdfContainer) {

        Label modeLabel = new Label("Split Options");
        modeLabel.setFont(Font.font(16));
        modeLabel.setStyle("-fx-font-weight: bold;");

        ToggleGroup splitGroup = new ToggleGroup();

        RadioButton firstBtn = new RadioButton("Split First Page");
        RadioButton lastBtn = new RadioButton("Split Last Page");
        RadioButton evenBtn = new RadioButton("Split All Even Number Pages");
        RadioButton oddBtn = new RadioButton("Split All Odd Number Pages");
        
        firstBtn.setToggleGroup(splitGroup);
        lastBtn.setToggleGroup(splitGroup);
        oddBtn.setToggleGroup(splitGroup);
        evenBtn.setToggleGroup(splitGroup);
        
        firstBtn.setUserData(SplitMode.FIRST_PAGE);
        lastBtn.setUserData(SplitMode.LAST_PAGE);
        evenBtn.setUserData(SplitMode.EVEN_PAGES);
        oddBtn.setUserData(SplitMode.ODD_PAGES);
        
        addDeselectSupport(firstBtn, splitGroup);
        addDeselectSupport(lastBtn, splitGroup);
        addDeselectSupport(evenBtn, splitGroup);
        addDeselectSupport(oddBtn, splitGroup);

        splitBtn = new Button("Split PDF");
        splitBtn.setDisable(true);
        splitBtn.setStyle("""
            -fx-background-color: #ff4d4d;
            -fx-text-fill: white;
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-padding: 10 20;
        """);
        

       splitBtn.setOnAction(e -> {
            int opt=-1;
            Toggle selected = splitGroup.getSelectedToggle();
            if (selected == null) {
                opt=0;
                SplitService.SplitPagesViaOptions(addedPdfFiles.getFirst(), opt);
                return;
            }

            SplitMode mode = (SplitMode) selected.getUserData();
            System.out.println(mode);
            switch (mode) {
                case FIRST_PAGE -> opt = 1;
                case LAST_PAGE -> opt = 2;
                case EVEN_PAGES -> opt = 3;
                case ODD_PAGES -> opt = 4;
            }
            SplitService.SplitPagesViaOptions(addedPdfFiles.getFirst(), opt);
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

        VBox box = new VBox(12,
                modeLabel,
                oddBtn,
                evenBtn,
                firstBtn,
                lastBtn,
                splitBtn,
                backBtn
        );
        box.setAlignment(Pos.BOTTOM_LEFT);

        return box;
    }
    
    private void addDeselectSupport(RadioButton rb, ToggleGroup group) {
        rb.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (rb.isSelected()) {
                group.selectToggle(null);
                e.consume(); // prevents default re-select behavior
            }
        });
    }
    
    private BorderPane createSplitByRangeWorkspace() {

        isSplitByRangePage=true;
        // ---------- Title ----------
        Label title = new Label("Split PDF by Range");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        BorderPane topBar = new BorderPane();
        topBar.setCenter(title);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #dddddd;
            -fx-border-width: 0 0 1 0;
        """);

        // ---------- PDF Container ----------
        FlowPane pdfContainer = new FlowPane(20, 20);
        pdfContainer.setAlignment(Pos.CENTER);
        pdfContainer.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(pdfContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // ---------- Add Button ----------
        StackPane addButton = createAddPdfButton(pdfContainer);

        pdfContainer.getChildren().addListener(
            (javafx.collections.ListChangeListener<Node>) c -> {
                boolean disable = pdfContainer.getChildren().size() >= 1;
                addButton.setDisable(disable);
                addButton.setOpacity(disable ? 0.5 : 1.0);
            }
        );

        // ---------- Right Sidebar ----------
        VBox rightPanel = createSplitByRangeRightPanel(pdfContainer);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(20, addButton, spacer, rightPanel);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(300);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("""
            -fx-background-color: #eaeaea;
            -fx-border-color: #ccc;
            -fx-border-width: 0 0 0 1;
        """);

        // ---------- Layout ----------
        HBox center = new HBox(scrollPane, sidebar);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(center);
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }
    
    private VBox createSplitByRangeRightPanel(FlowPane pdfContainer) {

        Label sectionTitle = new Label("Split Options");
        sectionTitle.setFont(Font.font(16));
        sectionTitle.setStyle("-fx-font-weight: bold;");

        // ---------- Toggle ----------
        CheckBox customRangeCheck = new CheckBox("Custom Range");

        // ---------- Fixed Range ----------
        Label fixedLabel = new Label("Split into page ranges of:");
        TextField fixedField = new TextField();
        fixedField.setPromptText("e.g. 5");
        fixedField.setMaxWidth(120);

        // ---------- Custom Range ----------
        Label fromLabel = new Label("From Page:");
        TextField fromField = new TextField();
        fromField.setPromptText("e.g. 1");
        fromField.setMaxWidth(100);

        Label toLabel = new Label("To Page:");
        TextField toField = new TextField();
        toField.setPromptText("e.g. 10");
        toField.setMaxWidth(100);

        // Numeric-only enforcement
        enforceNumeric(fixedField);
        enforceNumeric(fromField);
        enforceNumeric(toField);

        // ---------- Visibility Control ----------
        fixedLabel.managedProperty().bind(customRangeCheck.selectedProperty().not());
        fixedField.managedProperty().bind(customRangeCheck.selectedProperty().not());
        fixedLabel.visibleProperty().bind(customRangeCheck.selectedProperty().not());
        fixedField.visibleProperty().bind(customRangeCheck.selectedProperty().not());

        fromLabel.managedProperty().bind(customRangeCheck.selectedProperty());
        fromField.managedProperty().bind(customRangeCheck.selectedProperty());
        toLabel.managedProperty().bind(customRangeCheck.selectedProperty());
        toField.managedProperty().bind(customRangeCheck.selectedProperty());

        fromLabel.visibleProperty().bind(customRangeCheck.selectedProperty());
        fromField.visibleProperty().bind(customRangeCheck.selectedProperty());
        toLabel.visibleProperty().bind(customRangeCheck.selectedProperty());
        toField.visibleProperty().bind(customRangeCheck.selectedProperty());

        // ---------- Split Button ----------
        splitBtn = new Button("Split PDF");
        splitBtn.setDisable(true);
        splitBtn.setStyle("""
            -fx-background-color: #ff4d4d;
            -fx-text-fill: white;
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-padding: 10 20;
        """);

        // Enable logic
        Runnable updateState = () -> {
            boolean hasPdf = !pdfContainer.getChildren().isEmpty();
            boolean valid;

            if (customRangeCheck.isSelected()) {
                valid = !fromField.getText().isBlank() && !toField.getText().isBlank();
            } else {
                valid = !fixedField.getText().isBlank();
            }

            splitBtn.setDisable(!(hasPdf && valid));
        };

        fixedField.textProperty().addListener((a,b,c)->updateState.run());
        fromField.textProperty().addListener((a,b,c)->updateState.run());
        toField.textProperty().addListener((a,b,c)->updateState.run());
        customRangeCheck.selectedProperty().addListener((a,b,c)->updateState.run());

        splitBtn.setOnAction(e ->{
            
            if(customRangeCheck.isSelected())
            {
                SplitService.SplitFromTo(addedPdfFiles.getFirst(), Integer.parseInt(fromField.getText())  ,Integer.parseInt(toField.getText()) );
            }else
            {
                SplitService.SplitPDFAtPage(addedPdfFiles.getFirst(), Integer.parseInt(fixedField.getText()));
            }
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

        VBox box = new VBox(12,
            sectionTitle,
            customRangeCheck,
            fixedLabel,
            fixedField,
            fromLabel,
            fromField,
            toLabel,
            toField,
            splitBtn,
            backBtn
        );

        box.setAlignment(Pos.BOTTOM_LEFT);
        return box;
    }


    
    private VBox createMergePDFRightSideControls(FlowPane pdfContainer, TextField outputField) {
        Button backBtn = new Button("← Back");
    backBtn.setOnAction(e -> {
        addedPdfFiles.clear();
        isMergePage = false;
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
                MergeService.MergePDFs(addedPdfFiles, outputField.getText());
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
        addedPdfFiles.clear();
        pdfContainer.getChildren().clear();
        updateMergeButtonState();
    });

    VBox box = new VBox(10,mergeBtn, clearBtn, backBtn);
    box.setAlignment(Pos.BOTTOM_RIGHT);

    return box;
    }
    
    
    private BorderPane createSplitAtPageWorkspace() {
        isSplitAtPagesPage = true;
        // ---------- Title ----------
        Label title = new Label("Split PDF at Page");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        BorderPane topBar = new BorderPane();
        topBar.setCenter(title);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #dddddd;
            -fx-border-width: 0 0 1 0;
        """);

        // ---------- PDF Container ----------
        FlowPane pdfContainer = new FlowPane(20, 20);
        pdfContainer.setAlignment(Pos.CENTER);
        pdfContainer.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(pdfContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // ---------- Add Button ----------
        StackPane addButton = createAddPdfButton(pdfContainer);

        // Disable add button once a PDF is added
        pdfContainer.getChildren().addListener(
            (javafx.collections.ListChangeListener<Node>) c -> {
                boolean disable = pdfContainer.getChildren().size() >= 1;
                addButton.setDisable(disable);
                addButton.setOpacity(disable ? 0.5 : 1.0);
            }
        );

        // ---------- Right Sidebar ----------
        VBox rightSidebar = createSplitAtPageRightPanel(pdfContainer);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(20, addButton, spacer, rightSidebar);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(280);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("""
            -fx-background-color: #eaeaea;
            -fx-border-color: #ccc;
            -fx-border-width: 0 0 0 1;
        """);

        // ---------- Center Content ----------
        HBox centerContent = new HBox(scrollPane, sidebar);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerContent);
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }
    
    private VBox createSplitAtPageRightPanel(FlowPane pdfContainer) {

        Label sectionTitle = new Label("Split Options");
        sectionTitle.setFont(Font.font(16));
        sectionTitle.setStyle("-fx-font-weight: bold;");

        Label pageLabel = new Label("Page Number:");

        TextField pageField = new TextField();
        pageField.setPromptText("e.g. 3");
        pageField.setMaxWidth(120);

        // Numeric input only
        pageField.textProperty().addListener((obs, old, val) -> {
            if (!val.matches("\\d*")) {
                pageField.setText(val.replaceAll("[^\\d]", ""));
            }
        });

        Label tip = new Label("Tip: Page number is inclusive");
        tip.setWrapText(true);
        tip.setStyle("""
            -fx-font-size: 11;
            -fx-text-fill: #555;
        """);

        splitBtn = new Button("Split PDF");
        splitBtn.setDisable(true);
        splitBtn.setStyle("""
            -fx-background-color: #ff4d4d;
            -fx-text-fill: white;
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-padding: 10 20;
        """);

        // Enable split button only when:
        // - one PDF exists
        // - page number entered
        pageField.textProperty().addListener((obs, o, n) ->
            splitBtn.setDisable(
                pdfContainer.getChildren().isEmpty() || n.isBlank()
            )
        );

        splitBtn.setOnAction(e -> {
        PDDocument pdf=null;
            try {
                pdf = PDDocument.load(addedPdfFiles.getFirst());
            } catch (IOException ex) {
                System.getLogger(PDFUTILITYTOOL.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        int totalPages = pdf.getNumberOfPages();
        SplitService.SplitFromTo(addedPdfFiles.getFirst(),1,Integer.parseInt(pageField.getText()));
        SplitService.SplitFromTo(addedPdfFiles.getFirst(),Integer.parseInt(pageField.getText()) +1 ,totalPages );
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

        VBox box = new VBox(12,
                sectionTitle,
                pageLabel,
                pageField,
                tip,
                splitBtn,
                backBtn
        );
        box.setAlignment(Pos.BOTTOM_LEFT);

        return box;
    }
    
    private BorderPane createCompressWorkspace() {

        // ---------- Title ----------
        Label title = new Label("Compress PDF");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        BorderPane topBar = new BorderPane();
        topBar.setCenter(title);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #dddddd;
            -fx-border-width: 0 0 1 0;
        """);

        // ---------- PDF Container ----------
        FlowPane pdfContainer = new FlowPane(20, 20);
        pdfContainer.setAlignment(Pos.CENTER);
        pdfContainer.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(pdfContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // ---------- Add Button ----------
        StackPane addButton = createAddPdfButton(pdfContainer);



        // ---------- Right Sidebar ----------
        VBox rightSidebar = createCompressRightSidebar(pdfContainer);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(20, addButton, spacer, rightSidebar);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(280);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("""
            -fx-background-color: #eaeaea;
            -fx-border-color: #ccc;
            -fx-border-width: 0 0 0 1;
        """);

        // ---------- Center Content ----------
        HBox centerContent = new HBox(scrollPane, sidebar);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerContent);
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }
    
    private VBox createCompressRightSidebar(FlowPane pdfContainer) {

    Label sectionTitle = new Label("Compression Options");
    sectionTitle.setFont(Font.font(16));
    sectionTitle.setStyle("-fx-font-weight: bold;");

    CheckBox heavyCompression = new CheckBox("Heavy Compression (Black & White)");
    heavyCompression.setWrapText(true);

    Label tip = new Label(
        "Tip: Heavy compression converts the PDF to black and white and reduces file size significantly."
    );
    tip.setWrapText(true);
    tip.setStyle("""
        -fx-font-size: 11;
        -fx-text-fill: #555;
    """);

    Button compressBtn = new Button("Compress PDF");
    compressBtn.setDisable(true);
    compressBtn.setStyle("""
        -fx-background-color: #ff4d4d;
        -fx-text-fill: white;
        -fx-font-size: 15;
        -fx-font-weight: bold;
        -fx-padding: 10 20;
    """);

    // Enable only when at least one PDF is added
    compressBtn.disableProperty().bind(
        javafx.beans.binding.Bindings.isEmpty(pdfContainer.getChildren())
    );

    compressBtn.setOnAction(e -> {
        boolean isHeavy = heavyCompression.isSelected();

        // Placeholder for compression logic
        System.out.println(
            isHeavy
                ? "Heavy compression selected (B/W)"
                : "Normal compression selected"
        );
        
        if(addedPdfFiles.size()>1)
        {
            for(File pdf : addedPdfFiles)
            {
                CompressService.CompressPDF(pdf, isHeavy);
            }
        }else
        {
            CompressService.CompressPDF(addedPdfFiles.getFirst(), isHeavy);
        }

        // CompressService.compress(addedPdfFiles.getFirst(), isHeavy);
    });
    
    Button clearBtn = new Button("Clear All");
    clearBtn.setStyle("""
        -fx-background-color: #777;
        -fx-text-fill: white;
        -fx-padding: 8 20;
    """);

    clearBtn.setOnAction(e -> {
        addedPdfFiles.clear();
        pdfContainer.getChildren().clear();
    });

    Button backBtn = new Button("← Back");
    backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

    VBox box = new VBox(
        12,
        sectionTitle,
        heavyCompression,
        tip,
        compressBtn,
        clearBtn,
        backBtn
    );

    box.setAlignment(Pos.BOTTOM_LEFT);

    return box;
}

    
    private BorderPane createExtractTextWorkspace() {

    // ---------- Title ----------
    Label title = new Label("Extract Text");
    title.setFont(Font.font("Arial", 28));
    title.setStyle("-fx-font-weight: bold;");

    BorderPane topBar = new BorderPane();
    topBar.setCenter(title);
    topBar.setPadding(new Insets(10));
    topBar.setStyle("""
        -fx-background-color: #ffffff;
        -fx-border-color: #dddddd;
        -fx-border-width: 0 0 1 0;
    """);

    // ---------- PDF Cards ----------
    FlowPane pdfContainer = new FlowPane(20, 20);
    pdfContainer.setAlignment(Pos.CENTER);
    pdfContainer.setPadding(new Insets(20));

    ScrollPane scrollPane = new ScrollPane(pdfContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    // ---------- Add Button (NO LOCKING) ----------
    StackPane addButton = createAddPdfButton(pdfContainer);
     // Disable add button once a PDF is added
        pdfContainer.getChildren().addListener(
            (javafx.collections.ListChangeListener<Node>) c -> {
                boolean disable = pdfContainer.getChildren().size() >= 1;
                addButton.setDisable(disable);
                addButton.setOpacity(disable ? 0.5 : 1.0);
            }
        );

    // ---------- Right Controls ----------
    VBox rightControls = createExtractTextRightSidebar(pdfContainer);

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    // ---------- Right Sidebar ----------
    VBox sidebar = new VBox(20, addButton, spacer, rightControls);
    sidebar.setPadding(new Insets(20));
    sidebar.setPrefWidth(280);
    sidebar.setAlignment(Pos.TOP_CENTER);
    sidebar.setStyle("""
        -fx-background-color: #eaeaea;
        -fx-border-color: #ccc;
        -fx-border-width: 0 0 0 1;
    """);

    // ---------- Center Content ----------
    HBox centerContent = new HBox(scrollPane, sidebar);
    HBox.setHgrow(scrollPane, Priority.ALWAYS);

    // ---------- Root ----------
    BorderPane root = new BorderPane();
    root.setTop(topBar);
    root.setCenter(centerContent);
    root.setStyle("-fx-background-color: #f2f2f2;");

    return root;
}

    
    private VBox createExtractTextRightSidebar(FlowPane pdfContainer) {

        Label sectionTitle = new Label("Extract Options");
        sectionTitle.setFont(Font.font(16));
        sectionTitle.setStyle("-fx-font-weight: bold;");

        CheckBox extractToMultipleFile = new CheckBox("Extract to multiple file");
        extractToMultipleFile.setWrapText(true);

        // ---------- Output Format (Radio Buttons) ----------
        Label formatLabel = new Label("Output Format:");
        formatLabel.setFont(Font.font(13));
        formatLabel.setStyle("-fx-font-weight: bold;");

        RadioButton pdfRadio = new RadioButton("PDF");
        RadioButton txtRadio = new RadioButton("TXT");

        ToggleGroup formatGroup = new ToggleGroup();
        pdfRadio.setToggleGroup(formatGroup);
        txtRadio.setToggleGroup(formatGroup);

        txtRadio.setSelected(true); // sensible default

        VBox formatBox = new VBox(6, formatLabel, pdfRadio, txtRadio);
        formatBox.setPadding(new Insets(0, 0, 0, 10));

        // Hidden by default
        formatBox.setVisible(false);
        formatBox.setManaged(false);

        // Show only when "Extract to single file" is checked
        extractToMultipleFile.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            formatBox.setVisible(isSelected);
            formatBox.setManaged(isSelected);
        });

        Label tip = new Label(
            "Tip: Extracted text will be saved on your Desktop."
        );
        tip.setWrapText(true);
        tip.setStyle("""
            -fx-font-size: 11;
            -fx-text-fill: #555;
        """);

        Button extractBtn = new Button("Extract Text");
        extractBtn.setDisable(true);
        extractBtn.setStyle("""
            -fx-background-color: #ff4d4d;
            -fx-text-fill: white;
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-padding: 10 20;
        """);

        // Enable when at least one PDF exists
        extractBtn.disableProperty().bind(
            javafx.beans.binding.Bindings.isEmpty(pdfContainer.getChildren())
        );

        extractBtn.setOnAction(e -> {
            boolean multipleFiles = extractToMultipleFile.isSelected();
            String outputFormat = "PDF";

            if (multipleFiles && formatGroup.getSelectedToggle() != null) {
                outputFormat = ((RadioButton) formatGroup.getSelectedToggle()).getText();
            }

            // Placeholder
            System.out.println(
                "Extract Text | multipleFiles=" + multipleFiles + " | format=" + outputFormat
            );
            ContentExtractionService.ExtractTextPageWise(addedPdfFiles.getFirst(), PDType1Font.HELVETICA, 12, 50, multipleFiles, outputFormat.toLowerCase());

        });
        

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

        VBox box = new VBox(
            12,
            sectionTitle,
            extractToMultipleFile,
            formatBox,
            tip,
            extractBtn,
            backBtn
        );

        return box;
    }

    private BorderPane createExtractImagesWorkspace() {

        // ---------- Title ----------
        Label title = new Label("Extract Images");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        BorderPane topBar = new BorderPane();
        topBar.setCenter(title);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #dddddd;
            -fx-border-width: 0 0 1 0;
        """);

        // ---------- PDF Cards ----------
        FlowPane pdfContainer = new FlowPane(20, 20);
        pdfContainer.setAlignment(Pos.CENTER);
        pdfContainer.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(pdfContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // ---------- Add Button ----------
        StackPane addButton = createAddPdfButton(pdfContainer);

        // Disable add button after one PDF
        pdfContainer.getChildren().addListener(
            (javafx.collections.ListChangeListener<Node>) c -> {
                boolean disable = pdfContainer.getChildren().size() >= 1;
                addButton.setDisable(disable);
                addButton.setOpacity(disable ? 0.5 : 1.0);
            }
        );

        // ---------- Right Sidebar ----------
        VBox rightControls = createExtractImagesRightSidebar(pdfContainer);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(20, addButton, spacer, rightControls);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(280);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("""
            -fx-background-color: #eaeaea;
            -fx-border-color: #ccc;
            -fx-border-width: 0 0 0 1;
        """);

        // ---------- Center Content ----------
        HBox centerContent = new HBox(scrollPane, sidebar);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        // ---------- Root ----------
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerContent);
        root.setStyle("-fx-background-color: #f2f2f2;");

        return root;
    }
    
    private VBox createExtractImagesRightSidebar(FlowPane pdfContainer) {

    Label sectionTitle = new Label("Extract Options");
    sectionTitle.setFont(Font.font(16));
    sectionTitle.setStyle("-fx-font-weight: bold;");

    /* =====================================================
       SAVE MODE OPTIONS
       ===================================================== */

    Label saveAsLabel = new Label("Save Images As:");
    saveAsLabel.setFont(Font.font(13));
    saveAsLabel.setStyle("-fx-font-weight: bold;");

    RadioButton saveAsPdf = new RadioButton("PDF");
    RadioButton saveAsImages = new RadioButton("Image Files");

    ToggleGroup saveModeGroup = new ToggleGroup();
    saveAsPdf.setToggleGroup(saveModeGroup);
    saveAsImages.setToggleGroup(saveModeGroup);

    saveAsImages.setSelected(true); // sensible default

    VBox saveModeBox = new VBox(6, saveAsLabel, saveAsPdf, saveAsImages);
    saveModeBox.setPadding(new Insets(0, 0, 0, 10));

    /* =====================================================
       PDF IMAGE TYPE OPTIONS (PNG / JPEG)
       ===================================================== */

    Label pdfTypeLabel = new Label("Images inside PDF:");
    pdfTypeLabel.setFont(Font.font(13));
    pdfTypeLabel.setStyle("-fx-font-weight: bold;");

    RadioButton pdfPng = new RadioButton("PNG");
    RadioButton pdfJpeg = new RadioButton("JPEG");
    pdfPng.setUserData(0);
    pdfJpeg.setUserData(1);

    ToggleGroup pdfTypeGroup = new ToggleGroup();
    pdfPng.setToggleGroup(pdfTypeGroup);
    pdfJpeg.setToggleGroup(pdfTypeGroup);

    pdfPng.setSelected(true);

    VBox pdfTypeBox = new VBox(6, pdfTypeLabel, pdfPng, pdfJpeg);
    pdfTypeBox.setPadding(new Insets(0, 0, 0, 25));

    // Hidden by default
    pdfTypeBox.setVisible(false);
    pdfTypeBox.setManaged(false);

    /* =====================================================
       IMAGE FILE TYPE OPTIONS (PNG / JPG)
       ===================================================== */

    Label imageTypeLabel = new Label("Image File Type:");
    imageTypeLabel.setFont(Font.font(13));
    imageTypeLabel.setStyle("-fx-font-weight: bold;");

    RadioButton imgPng = new RadioButton("PNG");
    RadioButton imgJpeg = new RadioButton("JPEG");
    imgPng.setUserData(0);
    imgJpeg.setUserData(1);

    ToggleGroup imageTypeGroup = new ToggleGroup();
    imgPng.setToggleGroup(imageTypeGroup);
    imgJpeg.setToggleGroup(imageTypeGroup);

    imgPng.setSelected(true);

    VBox imageTypeBox = new VBox(6, imageTypeLabel, imgPng, imgJpeg);
    imageTypeBox.setPadding(new Insets(0, 0, 0, 25));

    /* =====================================================
       VISIBILITY LOGIC
       ===================================================== */

    saveModeGroup.selectedToggleProperty().addListener((obs, old, selected) -> {
        boolean pdfSelected = selected == saveAsPdf;

        pdfTypeBox.setVisible(pdfSelected);
        pdfTypeBox.setManaged(pdfSelected);

        imageTypeBox.setVisible(!pdfSelected);
        imageTypeBox.setManaged(!pdfSelected);
    });

    /* =====================================================
       TIP
       ===================================================== */

    Label tip = new Label(
        "Tip: Extracted images will be saved on your Desktop."
    );
    tip.setWrapText(true);
    tip.setStyle("""
        -fx-font-size: 11;
        -fx-text-fill: #555;
    """);

    /* =====================================================
       EXTRACT BUTTON
       ===================================================== */

    Button extractBtn = new Button("Extract Images");
    extractBtn.setDisable(true);
    extractBtn.setStyle("""
        -fx-background-color: #ff4d4d;
        -fx-text-fill: white;
        -fx-font-size: 15;
        -fx-font-weight: bold;
        -fx-padding: 10 20;
    """);

    extractBtn.disableProperty().bind(
        javafx.beans.binding.Bindings.isEmpty(pdfContainer.getChildren())
    );

    extractBtn.setOnAction(e -> {

        boolean saveAsPdfSelected = saveAsPdf.isSelected();

        Integer imageType;
        if (saveAsPdfSelected) {
            
            imageType = (Integer)(pdfTypeGroup.getSelectedToggle()).getUserData();
        } else {
            
            imageType = (Integer)(imageTypeGroup.getSelectedToggle()).getUserData();
        }

        System.out.println(
            "Extract Images | saveAsPdf=" + saveAsPdfSelected +
            " | imageType=" + imageType
        );
        
        File pdfFile = addedPdfFiles.getFirst();
        List<BufferedImage> imgs =  ContentExtractionService.ExtractImages(pdfFile);
        
        if(saveAsPdfSelected)
        {
            ContentExtractionService.MakeImagePDF(getFileNameWithoutExtension(pdfFile), imgs, imageType);
        }else
        {
            ContentExtractionService.SaveImages(getFileNameWithoutExtension(pdfFile), imgs, imageType);
        }

        // Example future calls:
        // ImageExtractionService.extractToPdf(pdfFile, imageType);
        // ImageExtractionService.extractAsImages(pdfFile, imageType);
    });

    /* =====================================================
       BACK BUTTON
       ===================================================== */

    Button backBtn = new Button("← Back");
    backBtn.setOnAction(e -> mainScene.setRoot(createHomePage()));

    return new VBox(
        12,
        sectionTitle,
        saveModeBox,
        pdfTypeBox,
        imageTypeBox,
        tip,
        extractBtn,
        backBtn
    );
}

    public static String getFileNameWithoutExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot == -1) ? name : name.substring(0, lastDot);
    }





    
    
    private void enforceNumeric(TextField field) {
        field.textProperty().addListener((obs, old, val) -> {
            if (!val.matches("\\d*")) {
                field.setText(val.replaceAll("[^\\d]", ""));
            }
        });
    }

    
    private void updateMergeButtonState() {
     mergeBtn.setDisable(addedPdfFiles.size() < 2);
    }
    
    private void UpdateSplitPDFButtonState() {
     splitBtn.setDisable(addedPdfFiles.size() != 1 );
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
