package com.ditur.ui;

import com.ditur.Board;
import com.ditur.Field;
import com.ditur.builder.Agent;
import com.ditur.builder.Bee;
import com.ditur.builder.Pest;
import com.ditur.builder.Farmer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class SimulationView {

    private final Canvas canvas;

    // --------------------------------------------   UI
    private Label lblTicks;
    private Label lblCrops;

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
    // --------------------------------------------   UI

    private HBox mainLayout;
    private final int cellSize;

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
    // --------------------------------------------   TEXTURES

    public SimulationView(int boardWidth, int boardHeight, int cellSize) {
        this.cellSize = cellSize;

        loadAllTextures();

        // Create and show all UI and hud stuff
        canvas = createLabels(boardWidth, boardHeight, cellSize);
    }

    public void render(Board board, List<Agent> agents) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawGridAndFields(board, gc);

        drawAgents(agents, gc);
    }

    private Canvas createLabels(int boardWidth, int boardHeight, int cellSize) {
        final Canvas canvas;

        GridPane leftPanel = new GridPane();
        leftPanel.setHgap(15);
        leftPanel.setVgap(12);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(440);
        leftPanel.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: #CCCCCC; -fx-border-width: 0 1 0 0;");

        Label titleLabel = new Label("Farm Simulator");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        btnStart = new Button("START");
        btnPause = new Button("PAUZA");
        btnStart.setMaxWidth(Double.MAX_VALUE);
        btnPause.setMaxWidth(Double.MAX_VALUE);

        Label speedLabel = new Label("Simulation speed (ms):");
        speedSlider = new Slider(100, 1000, 500);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);

        Label statsLabel = new Label("STATISTICS:");
        statsLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        lblTicks = new Label("Simulation step: 0");
        lblCrops = new Label("Harvested crops: 0");

        // 1st column
        Label col1Title = new Label("Spawners & Map");
        col1Title.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");

        Label beeLabel = new Label("Initial Bee count:");
        tfBeeCount = new TextField("5");
        btnSpawnBees = new Button("Spawn Bees");
        btnSpawnBees.setMaxWidth(Double.MAX_VALUE);

        Label pestLabel = new Label("Initial Pest count:");
        tfPestCount = new TextField("5");
        btnSpawnPest = new Button("Spawn Pests");
        btnSpawnPest.setMaxWidth(Double.MAX_VALUE);

        Label farmerLabel = new Label("Initial Farmer count:");
        tfFarmerCount = new TextField("5");
        btnSpawnFarmer = new Button("Spawn Farmers");
        btnSpawnFarmer.setMaxWidth(Double.MAX_VALUE);

        Label cropLabel = new Label("Crop coverage (%):");
        tfCropPercentage = new TextField("30");
        btnGenerateCrops = new Button("Generate Crops");
        btnGenerateCrops.setMaxWidth(Double.MAX_VALUE);
        btnGenerateCrops.setStyle("-fx-background-color: #A1CCD1; -fx-text-fill: black; -fx-font-weight: bold;");

        // 2nd column
        Label col2Title = new Label("Pesticide Settings");
        col2Title.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");

        Label cooldownLabel = new Label("Pesticide Cooldown:");
        tfPesticideCooldown = new TextField("100");

        Label durationLabel = new Label("Pesticide Duration:");
        tfPesticideDuration = new TextField("20");


        leftPanel.add(titleLabel, 0, 0, 2, 1);
        leftPanel.add(btnStart, 0, 1);
        leftPanel.add(btnPause, 1, 1);
        leftPanel.add(speedLabel, 0, 2, 2, 1);
        leftPanel.add(speedSlider, 0, 3, 2, 1);

        leftPanel.add(statsLabel, 0, 4, 2, 1);
        leftPanel.add(lblTicks, 0, 5);
        leftPanel.add(lblCrops, 1, 5);

        leftPanel.add(col1Title, 0, 6);
        leftPanel.add(beeLabel, 0, 7);
        leftPanel.add(tfBeeCount, 0, 8);
        leftPanel.add(btnSpawnBees, 0, 9);

        leftPanel.add(pestLabel, 0, 10);
        leftPanel.add(tfPestCount, 0, 11);
        leftPanel.add(btnSpawnPest, 0, 12);

        leftPanel.add(farmerLabel, 0, 13);
        leftPanel.add(tfFarmerCount, 0, 14);
        leftPanel.add(btnSpawnFarmer, 0, 15);

        leftPanel.add(cropLabel, 0, 16);
        leftPanel.add(tfCropPercentage, 0, 17);
        leftPanel.add(btnGenerateCrops, 0, 18);

        leftPanel.add(col2Title, 1, 6);
        leftPanel.add(cooldownLabel, 1, 7);
        leftPanel.add(tfPesticideCooldown, 1, 8);

        leftPanel.add(durationLabel, 1, 10);
        leftPanel.add(tfPesticideDuration, 1, 11);

        // 2. Right panel
        int canvasWidth = boardWidth * cellSize;
        int canvasHeight = boardHeight * cellSize;
        canvas = new Canvas(canvasWidth, canvasHeight);

        VBox canvasContainer = new VBox(canvas);
        canvasContainer.setPadding(new Insets(10));
        canvasContainer.setStyle("-fx-background-color: #2B2B2B;");

        // Merge do one layout
        mainLayout = new HBox(leftPanel, canvasContainer);
        return canvas;
    }

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
                case Farmer farmer -> gc.drawImage(imgFarmer, posX-3, posY-3, cellSize+3, cellSize+3);
                default -> {
                }
            }
        }
    }

    private void drawGridAndFields(Board board, GraphicsContext gc) {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Field field = board.getField(x, y);

                int posX = x * cellSize;
                int posY = y * cellSize;

                // 1st layout - DIRT
                if (field.getHydrationLevel() > 0) {
                    gc.setFill(Color.web("#557A46")); // zielen/gleba
                } else {
                    gc.setFill(Color.web("#A0A2A0")); // wysuszona, szara ziemia
                }
                gc.fillRect(posX, posY, cellSize - 1, cellSize - 1);


                // 2nd layout - CROPS
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

                // Pesticide Animation
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

                // GRID
                gc.setStroke(Color.web("#3A4D39"));
                gc.setLineWidth(1);
                gc.strokeRect(posX, posY, cellSize, cellSize);
            }
        }
    }

    // Texts update
    public void updateStats(int ticks, int crops) {
        lblTicks.setText("Simulation step: " + ticks);
        lblCrops.setText("Harvested crops: " + crops);
    }

    public HBox getMainLayout() { return mainLayout; }
    public Button getBtnStart() { return btnStart; }
    public Button getBtnPause() { return btnPause; }
    public Button getBtnSpawnBees() { return btnSpawnBees; }
    public Slider getSpeedSlider() { return speedSlider; }
    public TextField getTfBeeCount() { return tfBeeCount; }

    public TextField getTfPestCount() {return tfPestCount;}
    public Button getBtnSpawnPest() { return btnSpawnPest; }

    public TextField getTfFarmerCount() {return tfFarmerCount;}
    public Button getBtnSpawnFarmer() { return btnSpawnFarmer; }

    public TextField getTfCropPercentage() { return tfCropPercentage; }
    public Button getBtnGenerateCrops() { return btnGenerateCrops; }

    public TextField getTfPesticideCooldown() { return tfPesticideCooldown; }
    public TextField getTfPesticideDuration() { return tfPesticideDuration; }
}
