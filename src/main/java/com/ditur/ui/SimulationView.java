package com.ditur.ui;

import com.ditur.Board;
import com.ditur.Field;
import com.ditur.builder.Agent;
import com.ditur.builder.Bee;
import com.ditur.builder.Pest;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class SimulationView {

    private final Canvas canvas;
    private final Label lblTicks;
    private final Label lblCrops;

    private final Button btnStart;
    private final Button btnPause;
    private final Button btnSpawnBees;
    private final Slider speedSlider;
    private final TextField tfBeeCount;

    private final Button btnSpawnPest;
    private final TextField tfPestCount;

    private final HBox mainLayout;
    private final int cellSize;

    public SimulationView(int boardWidth, int boardHeight, int cellSize) {
        this.cellSize = cellSize;

        // 1. Left side of Simulator
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(220);
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
        statsLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");

        lblTicks = new Label("Simulation step: 0");
        lblCrops = new Label("Harvested crops: 0");

        Label beeLabel = new Label("Initial Bee count:");
        tfBeeCount = new TextField("5");

        btnSpawnBees = new Button("Spawn Bees");
        btnSpawnBees.setMaxWidth(Double.MAX_VALUE);

        Label pestLabel = new Label("Initial Pest count:");
        tfPestCount = new TextField("5");

        btnSpawnPest = new Button("Spawn Pests");
        btnSpawnPest.setMaxWidth(Double.MAX_VALUE);

        leftPanel.getChildren().addAll(titleLabel, btnStart, btnPause, speedLabel, speedSlider, statsLabel, lblTicks, lblCrops, beeLabel, tfBeeCount, btnSpawnBees, pestLabel, tfPestCount, btnSpawnPest);
        // 2. Right panel
        int canvasWidth = boardWidth * cellSize;
        int canvasHeight = boardHeight * cellSize;
        canvas = new Canvas(canvasWidth, canvasHeight);

        VBox canvasContainer = new VBox(canvas);
        canvasContainer.setPadding(new Insets(10));
        canvasContainer.setStyle("-fx-background-color: #2B2B2B;");

        // Merge do one layout
        mainLayout = new HBox(leftPanel, canvasContainer);
    }

    // Method that draws the current state of the board and agents
    public void render(Board board, List<Agent> agents) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Drawing grid squares
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Field field = board.getField(x, y);

                if (field.getFieldState().equals("growing")) {
                    switch (field.getCropType()) {
                        case CARROT -> gc.setFill(Color.web("#D27613")); // pomaranczowy
                        case POTATO -> gc.setFill(Color.web("#8A624A")); // jasny braz
                        case WHEAT -> gc.setFill(Color.web("#D1BC6A"));  // zloto
                        default -> gc.setFill(Color.DARKGREEN);
                    }
                } else if (field.getFieldState().equals("maturely") || field.getFieldState().equals("mature")) {
                    switch (field.getCropType()) {
                        case CARROT -> gc.setFill(Color.ORANGE);
                        case POTATO -> gc.setFill(Color.web("#5C4033")); // ciemny, brąz
                        case WHEAT -> gc.setFill(Color.GOLD);
                        default -> gc.setFill(Color.GREEN);
                    }
                } else {
                    if (field.getHydrationLevel() > 0) {
                        gc.setFill(Color.web("#557A46")); // zielen/gleba
                    } else {
                        gc.setFill(Color.web("#A0A2A0")); // wysuszona, szara ziemia
                    }
                }

                // Draw a square field
                gc.fillRect(x * cellSize, y * cellSize, cellSize - 1, cellSize - 1);

                gc.setStroke(Color.web("#3A4D39"));
                gc.setLineWidth(1);
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        // Draw bees
        for (Agent a : agents) {
            if (a instanceof Bee) {
                gc.setFill(Color.YELLOW);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1);
                gc.fillOval(a.getX() * cellSize + 4, a.getY() * cellSize + 4, cellSize - 8, cellSize - 8);
                gc.strokeOval(a.getX() * cellSize + 4, a.getY() * cellSize + 4, cellSize - 8, cellSize - 8);
            } else if ( a instanceof Pest){
                gc.setFill(Color.RED);
                gc.setStroke(Color.RED);
                gc.setLineWidth(1);
                gc.fillOval(a.getX() * cellSize + 4, a.getY() * cellSize + 4, cellSize - 8, cellSize - 8);
                gc.strokeOval(a.getX() * cellSize + 4, a.getY() * cellSize + 4, cellSize - 8, cellSize - 8);
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
}
