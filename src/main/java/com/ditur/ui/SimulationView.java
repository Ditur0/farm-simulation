package com.ditur.ui;

import com.ditur.Board;
import com.ditur.Field;
import com.ditur.builder.Agent;
import com.ditur.builder.Bee;
import com.ditur.builder.Pest;
import com.ditur.builder.Farmer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

// Odpowiada za UI aplikacji
// Tworzy uklad okna w JavaFX, laduje tekstury, rysuje siatke pol i agentow
// Aktualizuje statystyki i wykresy
public class SimulationView {

    private Canvas canvas;
    private BorderPane mainLayout;
    private final int cellSize;

    // --------------------------------------------   UI
    private Label lblTicks;
    private Label lblCrops;
    private Label lblPollinated;
    private Label lblPlanted;

    private Label lblkilledByFarmers;
    private Label lblkilledByPesticide;
    private Label lblpestsBorn;

    private Button btnStart;
    private Button btnPause;
    private Button btnSpawnBees;
    private Slider speedSlider;
    private TextField tfBeeCount;

    private Button btnSpawnPest;
    private TextField tfPestCount;

    private Button btnSpawnFarmer;
    private TextField tfFarmerCount;

    private TextField tfCropPercentage;
    private Button btnGenerateCrops;

    private TextField tfPesticideCooldown;
    private TextField tfPesticideDuration;

    private Button btnReset;
    private CheckBox cbAllowPesticide;

    // --- Wykresy ---
    private LineChart<Number, Number> cropChart;
    private XYChart.Series<Number, Number> cropSeries;

    private LineChart<Number, Number> pestChart;
    private XYChart.Series<Number, Number> pestSeries;

    // --------------------------------------------   TEXTURES
    private Image imgCarrotGrowing;
    private Image imgCarrotMature;
    private Image imgPotatoGrowing;
    private Image imgPotatoMature;
    private Image imgWheatGrowing;
    private Image imgWheatMature;
    private Image imgBee;
    private Image imgFarmer;
    private Image imgPest;
    private Image imgFarmerResting;

    public SimulationView(int boardWidth, int boardHeight, int cellSize) {
        this.cellSize = cellSize;
        loadAllTextures();
        canvas = buildInterface(boardWidth, boardHeight, cellSize);
    }

    // Glowna metoda rysujaca caly swiat symuacji
    // Czysci ekrna, ansoi plansze i agentow
    public void render(Board board, List<Agent> agents) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Czyszczenie poprzedniej klatki symulacji
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Rysowanie siatki i roslin
        drawGridAndFields(board, gc);

        // Rysowanie agentow
        drawAgents(agents, gc);
    }

    // Inicjalizuje, pozycjonuje i naklada styl na wsyztskie komponenty graficzne JavaFx
    private Canvas buildInterface(int boardWidth, int boardHeight, int cellSize) {
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Style CSS dla kafelkow bocznych
        String sectionBoxStyle = "-fx-background-color: #F9F9F9; -fx-border-color: #E0E0E0; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-border-width: 1px;";
        String sectionTitleStyle = "-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #333333;";
        String greenBtnStyle = "-fx-background-color: #008000; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 4px;";
        String btnStyle = "-fx-background-color: #EFEFEF; -fx-border-color: #CCCCCC; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-font-weight: bold; -fx-padding: 6 14 6 14;";

        // =========================================================================
        // CENTER PANEL: Plansza/Symulacja
        // =========================================================================
        int canvasWidth = boardWidth * cellSize;
        int canvasHeight = boardHeight * cellSize;
        canvas = new Canvas(canvasWidth, canvasHeight);

        VBox canvasContainer = new VBox(canvas);
        canvasContainer.setAlignment(Pos.CENTER);
        canvasContainer.setPadding(new Insets(3)); // Border wokol synulacji
        canvasContainer.setStyle("-fx-background-color: #232323;");
        mainLayout.setCenter(canvasContainer);

        // =========================================================================
        // 1. TOP: Logo
        // =========================================================================
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(10, 0, 25, 40));

        Label titleLabel = new Label();
        Image logoImage = new Image(getClass().getResourceAsStream("/panel/logotype.png"));
        ImageView logoView = new ImageView(logoImage);

        logoView.setFitHeight(35);
        logoView.setPreserveRatio(true);
        titleLabel.setGraphic(logoView);

        StackPane logoContainer = new StackPane(titleLabel);
        logoContainer.setPrefHeight(50);
        logoContainer.setStyle("-fx-background-color: #BDBDBD; -fx-background-radius: 6px; -fx-border-color: #000000; -fx-border-radius: 6px; -fx-border-width: 1px;");
        logoContainer.prefWidthProperty().bind(canvasContainer.widthProperty());

        topBar.getChildren().add(logoContainer);
        mainLayout.setTop(topBar);

        // =========================================================================
        // 2. LEFT PANEL
        // =========================================================================
        VBox leftPanel = new VBox(28); // Odstepy miedzy boxami
        leftPanel.setPadding(new Insets(0, 20, 0, 0));
        leftPanel.setPrefWidth(300);

        // Slider predkosci
        VBox speedBox = new VBox(6);
        Label speedLabel = new Label("Simulation speed (ms):");
        speedLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 12px; -fx-font-weight: bold;");
        speedSlider = new Slider(100, 1000, 500);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(300);
        speedSlider.setMinorTickCount(4);
        speedBox.getChildren().addAll(speedLabel, speedSlider);

        // START, PAUZA, RESET
        btnStart = new Button("START");
        btnPause = new Button("PAUZA");
        btnReset = new Button("RESET");
        btnStart.setStyle(btnStyle);
        btnPause.setStyle(btnStyle);
        btnReset.setStyle(btnStyle);
        HBox controlButtons = new HBox(10, btnStart, btnPause, btnReset);
        controlButtons.setPadding(new Insets(0, 0, 0, 20));

        // Box SPAWNERS
        VBox spawnersBox = new VBox(10);
        spawnersBox.setPadding(new Insets(12));
        spawnersBox.setStyle(sectionBoxStyle);

        Label spawnersTitle = new Label("Spawners");
        spawnersTitle.setStyle(sectionTitleStyle);

        GridPane spawnersGrid = new GridPane();
        spawnersGrid.setHgap(8);
        spawnersGrid.setVgap(10);

        Label farmerLabel = new Label("Initial Farmer count:");
        tfFarmerCount = new TextField("5");
        tfFarmerCount.setPrefWidth(50);
        btnSpawnFarmer = new Button("Spawn");
        btnSpawnFarmer.setStyle(greenBtnStyle);

        Label pestLabel = new Label("Initial Pest count:");
        tfPestCount = new TextField("5");
        tfPestCount.setPrefWidth(50);
        btnSpawnPest = new Button("Spawn");
        btnSpawnPest.setStyle(greenBtnStyle);

        Label beeLabel = new Label("Initial Bee count:");
        tfBeeCount = new TextField("5");
        tfBeeCount.setPrefWidth(50);
        btnSpawnBees = new Button("Spawn");
        btnSpawnBees.setStyle(greenBtnStyle);

        spawnersGrid.add(farmerLabel, 0, 0);
        spawnersGrid.add(tfFarmerCount, 1, 0);
        spawnersGrid.add(btnSpawnFarmer, 2, 0);
        spawnersGrid.add(pestLabel, 0, 1);
        spawnersGrid.add(tfPestCount, 1, 1);
        spawnersGrid.add(btnSpawnPest, 2, 1);
        spawnersGrid.add(beeLabel, 0, 2);
        spawnersGrid.add(tfBeeCount, 1, 2);
        spawnersGrid.add(btnSpawnBees, 2, 2);
        spawnersBox.getChildren().addAll(spawnersTitle, spawnersGrid);

        // Box PESTICIDE
        VBox pesticideBox = new VBox(10);
        pesticideBox.setPadding(new Insets(12));
        pesticideBox.setStyle(sectionBoxStyle);

        Label pesticideTitle = new Label("Pesticide Settings");
        pesticideTitle.setStyle(sectionTitleStyle);

        GridPane pesticideGrid = new GridPane();
        pesticideGrid.setHgap(8);
        pesticideGrid.setVgap(10);

        Label cooldownLabel = new Label("Pesticide Cooldown:");
        tfPesticideCooldown = new TextField("100");
        tfPesticideCooldown.setPrefWidth(50);

        Label durationLabel = new Label("Pesticide Duration:");
        tfPesticideDuration = new TextField("20");
        tfPesticideDuration.setPrefWidth(50);

        cbAllowPesticide = new CheckBox("Allow Farmers to use pesticide");
        cbAllowPesticide.setSelected(true);
        cbAllowPesticide.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        pesticideGrid.add(cooldownLabel, 0, 0);
        pesticideGrid.add(tfPesticideCooldown, 1, 0);
        pesticideGrid.add(durationLabel, 0, 1);
        pesticideGrid.add(tfPesticideDuration, 1, 1);
        pesticideGrid.add(cbAllowPesticide, 0, 2, 2, 1);

        pesticideBox.getChildren().addAll(pesticideTitle, pesticideGrid);

        // Box MAP
        VBox mapBox = new VBox(10);
        mapBox.setPadding(new Insets(12));
        mapBox.setStyle(sectionBoxStyle);

        Label mapTitle = new Label("Map");
        mapTitle.setStyle(sectionTitleStyle);

        GridPane mapGrid = new GridPane();
        mapGrid.setHgap(8);
        Label cropLabel = new Label("Crop coverage (%):");
        tfCropPercentage = new TextField("30");
        tfCropPercentage.setPrefWidth(50);
        btnGenerateCrops = new Button("Generate");
        btnGenerateCrops.setStyle(greenBtnStyle);

        mapGrid.add(cropLabel, 0, 0);
        mapGrid.add(tfCropPercentage, 1, 0);
        mapGrid.add(btnGenerateCrops, 2, 0);
        mapBox.getChildren().addAll(mapTitle, mapGrid);

        leftPanel.getChildren().addAll(speedBox, controlButtons, spawnersBox, pesticideBox, mapBox);
        mainLayout.setLeft(leftPanel);

        // =========================================================================
        // 4. RIGHT PANEL
        // =========================================================================
        VBox rightPanel = new VBox(28);
        rightPanel.setPadding(new Insets(0, 0, 0, 20));
        rightPanel.setPrefWidth(260);

        // Box STATISTICS
        VBox statsBox = new VBox(8);
        statsBox.setPadding(new Insets(12));
        statsBox.setStyle(sectionBoxStyle);

        Label statsLabel = new Label("STATISTICS:");
        statsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;");

        lblTicks = new Label("Simulation step: 0");
        lblCrops = new Label("Harvested crops: 0");
        lblPollinated = new Label("Pollinated: 0");
        lblPlanted = new Label("Planted: 0");
        lblkilledByFarmers = new Label("Pests killed by farmers: 0");
        lblkilledByPesticide = new Label("Pests killed by pesticide: 0");
        lblpestsBorn = new Label("Pests born: 0");
        String labelFont = "-fx-font-size: 13px; -fx-font-weight: normal;";
        lblTicks.setStyle(labelFont);
        lblCrops.setStyle(labelFont);
        lblPollinated.setStyle(labelFont);
        lblPlanted.setStyle(labelFont);
        lblkilledByFarmers.setStyle(labelFont);
        lblkilledByPesticide.setStyle(labelFont);
        lblpestsBorn.setStyle(labelFont);

        statsBox.getChildren().addAll(statsLabel, lblTicks, lblCrops, lblPollinated, lblPlanted, lblkilledByFarmers, lblkilledByPesticide,lblpestsBorn);

        // Wykresy
        VBox graphsBox = new VBox(12);
        Label graphsLabel = new Label("Graphs");
        graphsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        // Wykres 1
        VBox graph1Container = new VBox(4);
        Label lblGraph1Name = new Label("Crop Coverage History");
        lblGraph1Name.setAlignment(Pos.CENTER);
        lblGraph1Name.setMaxWidth(Double.MAX_VALUE);
        lblGraph1Name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;");

        // Osie
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Ticks");
        yAxis.setLabel("Crops");

        xAxis.setAnimated(false);
        yAxis.setAnimated(false);

        xAxis.setTickLabelsVisible(false); // Ukrycie liczb
        xAxis.setTickMarkVisible(false);
        xAxis.setOpacity(1);

        yAxis.setTickLabelsVisible(false);
        yAxis.setTickMarkVisible(false);
        yAxis.setOpacity(1);

        // Inicjalizacja wykresu
        cropChart = new LineChart<>(xAxis, yAxis);
        cropChart.setPrefSize(300, 210); // Wymiary wykresu
        cropChart.setAnimated(false);
        cropChart.setCreateSymbols(false);
        cropChart.setLegendVisible(false);

        cropSeries = new XYChart.Series<>();
        cropChart.getData().add(cropSeries);
        graph1Container.getChildren().addAll(lblGraph1Name, cropChart);

        // Wykres 2
        VBox graph2Container = new VBox(4);
        Label lblGraph2Name = new Label("Pest Population History");
        lblGraph2Name.setAlignment(Pos.CENTER);
        lblGraph2Name.setMaxWidth(Double.MAX_VALUE);
        lblGraph2Name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;");

        // Osie
        NumberAxis xAxisPest = new NumberAxis();
        NumberAxis yAxisPest = new NumberAxis();

        xAxisPest.setLabel("Ticks");
        yAxisPest.setLabel("Pest Count");

        xAxisPest.setAnimated(false);
        yAxisPest.setAnimated(false);

        xAxisPest.setTickLabelsVisible(false);
        xAxisPest.setTickMarkVisible(false);
        yAxisPest.setTickLabelsVisible(false);
        yAxisPest.setTickMarkVisible(false);

        // Inicjalizajca
        pestChart = new LineChart<>(xAxisPest, yAxisPest);
        pestChart.setPrefSize(250, 190);
        pestChart.setAnimated(false);
        pestChart.setCreateSymbols(false);
        pestChart.setLegendVisible(false);

        pestChart.getStyleClass().add("pest-chart");

        pestSeries = new XYChart.Series<>();
        pestChart.getData().add(pestSeries);
        graph2Container.getChildren().addAll(lblGraph2Name, pestChart);

        // Pobranie wygladu wykresu z pliku css
        try {
            String cssPath = getClass().getResource("/chart-style.css").toExternalForm();
            graph1Container.getStylesheets().add(cssPath);
        } catch (NullPointerException e) {
            System.err.println("Nie znaleziono pliku chart-style.css w folderze resources!");
        }

        graphsBox.getChildren().addAll(graphsLabel, graph1Container, graph2Container);
        rightPanel.getChildren().addAll(statsBox, graphsBox);
        mainLayout.setRight(rightPanel);

        return canvas;
    }

    // Bezpiecnzie laduje zasoby graficzne tekstur roslin i agentow
    private void loadAllTextures() {
        try {
            imgCarrotGrowing = new Image(getClass().getResourceAsStream("/carrot/tile002.png"));
            imgCarrotMature = new Image(getClass().getResourceAsStream("/carrot/tile005.png"));
            imgPotatoGrowing = new Image(getClass().getResourceAsStream("/potato/tile002.png"));
            imgPotatoMature = new Image(getClass().getResourceAsStream("/potato/tile005.png"));
            imgWheatGrowing = new Image(getClass().getResourceAsStream("/wheat/tile002.png"));
            imgWheatMature = new Image(getClass().getResourceAsStream("/wheat/tile005.png"));
            imgBee = new Image(getClass().getResourceAsStream("/agents/bee.png"));
            imgFarmer = new Image(getClass().getResourceAsStream("/agents/farmer.png"));
            imgPest = new Image(getClass().getResourceAsStream("/agents/duck.png"));
            imgFarmerResting= new Image(getClass().getResourceAsStream("/agents/farmer_resting.png"));

        } catch (Exception e) {
            System.out.println("Error while loading textures");
            e.printStackTrace();
        }
    }

    private void drawAgents(List<Agent> agents, GraphicsContext gc) {
        for (Agent a : agents) {
            int posX = a.getX() * cellSize;
            int posY = a.getY() * cellSize;

            switch (a) {
                case Bee bee -> gc.drawImage(imgBee, posX+5, posY+5, cellSize-10, cellSize-10);
                case Pest pest -> gc.drawImage(imgPest, posX+1, posY+1, cellSize-3, cellSize-3);
                case Farmer farmer ->{
                    if(farmer.isResting()){
                        gc.drawImage(imgFarmerResting, posX-3, posY-3, cellSize+3, cellSize+3);
                    }else{
                        gc.drawImage(imgFarmer, posX-3, posY-3, cellSize+3, cellSize+3);
                    }
                }
                default -> {
                }
            }
        }
    }

    // Iteruje przez plansze, rysuje kolor podloza, teksture rosliny oraz pestycyd
    private void drawGridAndFields(Board board, GraphicsContext gc) {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Field field = board.getField(x, y);

                int posX = x * cellSize;
                int posY = y * cellSize;

                if (field.getHydrationLevel() > 0) {
                    gc.setFill(Color.web("#557A46"));
                } else {
                    gc.setFill(Color.web("#A0A2A0"));
                }
                gc.fillRect(posX, posY, cellSize - 1, cellSize - 1);

                if (field.getFieldState().equals("growing")) {
                    switch (field.getCropType()) {
                        case CARROT ->  { gc.drawImage(imgCarrotGrowing, posX, posY, cellSize, cellSize); }
                        case POTATO -> { gc.drawImage(imgPotatoGrowing, posX, posY, cellSize, cellSize); }
                        case WHEAT -> { gc.drawImage(imgWheatGrowing, posX, posY, cellSize, cellSize); }
                        default -> { gc.setFill(Color.DARKGREEN); gc.fillRect(posX, posY, cellSize - 1, cellSize - 1); }
                    }
                } else if (field.getFieldState().equals("maturely") || field.getFieldState().equals("mature")) {
                    switch (field.getCropType()) {
                        case CARROT -> {gc.drawImage(imgCarrotMature, posX, posY, cellSize, cellSize);}
                        case POTATO -> { gc.drawImage(imgPotatoMature, posX, posY, cellSize, cellSize); }
                        case WHEAT -> { gc.drawImage(imgWheatMature, posX, posY, cellSize, cellSize); }
                        default -> { gc.setFill(Color.GREEN); gc.fillRect(posX, posY, cellSize - 1, cellSize - 1); }
                    }
                }

                // Animacja rochodzacego sie pestycydu
                if (field.hasPesticide()) {
                    int ticksLeft = field.getPesticideTicksLeft();
                    int maxDuration = field.getPesticideMaxDuration();
                    double progress = 1.0 - ((double) ticksLeft / maxDuration);
                    double pSize = cellSize * progress;
                    double pX = posX + (cellSize - pSize) / 2.0;
                    double pY = posY + (cellSize - pSize) / 2.0;

                    gc.setFill(Color.color(1.0, 0.0, 0.0, 0.3));
                    gc.fillRect(pX, pY, pSize, pSize);
                    gc.setStroke(Color.color(1.0, 0.0, 0.0, 0.6));
                    gc.setLineWidth(1.5);
                    gc.strokeRect(pX, pY, pSize, pSize);
                }

                gc.setStroke(Color.web("#3A4D39"));
                gc.setLineWidth(1);
                gc.strokeRect(posX, posY, cellSize, cellSize);
            }
        }
    }

    // Aktualizuje statystyki nowymi danymi
    public void updateStats(int ticks, int crops, int pollinated, int planted, int killedByFarmers, int killedByPesticide, int pestsBorn) {
        lblTicks.setText("Simulation step: " + ticks);
        lblCrops.setText("Harvested crops: " + crops);
        lblPollinated.setText("Pollinated: " + pollinated);
        lblPlanted.setText("Planted: " + planted);
        lblkilledByFarmers.setText("Pests killed by farmers: " +  killedByFarmers);
        lblkilledByPesticide.setText("Pests killed by pesticide: " +  killedByPesticide);
        lblpestsBorn.setText("Pests born: " +  pestsBorn);
    }

    // Wprowadza kolejny punkty danych do wykresu upraw
    public void addCropDataPoint(int tick, int cropCount) {
        javafx.application.Platform.runLater(() -> {
            cropSeries.getData().add(new XYChart.Data<>(tick, cropCount));
        });
    }

    // Wprowadza kolejny punkty danych do wykresu szkodikow
    public void addPestDataPoint(int tick, int pestCount) {
        javafx.application.Platform.runLater(() -> {
            pestSeries.getData().add(new XYChart.Data<>(tick, pestCount));
        });
    }

    public void clearChart() {
        cropSeries.getData().clear();
        pestSeries.getData().clear();
    }

    // Getters
    public BorderPane getMainLayout() { return mainLayout; }
    public Button getBtnStart() { return btnStart; }
    public Button getBtnPause() { return btnPause; }
    public Button getBtnSpawnBees() { return btnSpawnBees; }
    public Slider getSpeedSlider() { return speedSlider; }
    public TextField getTfBeeCount() { return tfBeeCount; }
    public TextField getTfPestCount() { return tfPestCount; }
    public Button getBtnSpawnPest() { return btnSpawnPest; }
    public TextField getTfFarmerCount() { return tfFarmerCount; }
    public Button getBtnSpawnFarmer() { return btnSpawnFarmer; }
    public TextField getTfCropPercentage() { return tfCropPercentage; }
    public Button getBtnGenerateCrops() { return btnGenerateCrops; }
    public TextField getTfPesticideCooldown() { return tfPesticideCooldown; }
    public TextField getTfPesticideDuration() { return tfPesticideDuration; }
    public Button getBtnReset() { return btnReset; }
    public CheckBox getCbAllowPesticide() { return cbAllowPesticide; }
}