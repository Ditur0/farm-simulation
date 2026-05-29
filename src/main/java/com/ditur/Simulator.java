package com.ditur;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Simulator extends Application {

    private Board board;
    private Canvas canvas;
    private static final int CELL_SIZE = 25; // Size of one field in pixels

    @Override
    public void start(Stage stage) {
        // 1. Board init
        board = new Board(20, 20);

        board.getField(2, 2).setCrop(CropType.CARROT);
        board.getField(5, 5).setCrop(CropType.POTATO);
        board.getField(8, 3).setCrop(CropType.WHEAT);

        // 2. Left side of Simulator (Controllers)
        VBox leftPanel = new VBox(15); // Space between elements
        leftPanel.setPadding(new Insets(15));
        leftPanel.setPrefWidth(220);
        leftPanel.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: #CCCCCC; -fx-border-width: 0 1 0 0;");

        Label titleLabel = new Label("Farm Simulator");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Button btnStart = new Button("START");
        Button btnPause = new Button("PAUZA");
        btnStart.setMaxWidth(Double.MAX_VALUE);
        btnPause.setMaxWidth(Double.MAX_VALUE);

        Label speedLabel = new Label("Simulation speed (ms):");
        Slider speedSlider = new Slider(100, 1000, 500);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);

        Label statsLabel = new Label("STATISTICS:");
        statsLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        Label lblTicks = new Label("Simulation step: 0");
        Label lblCrops = new Label("Harvested crops: 0");

        // Add elements to left panel side
        leftPanel.getChildren().addAll(titleLabel, btnStart, btnPause, speedLabel, speedSlider, statsLabel, lblTicks, lblCrops);

        // 3. Right side
        int canvasWidth = board.getWidth() * CELL_SIZE;
        int canvasHeight = board.getHeight() * CELL_SIZE;
        canvas = new Canvas(canvasWidth, canvasHeight);

        VBox canvasContainer = new VBox(canvas);
        canvasContainer.setPadding(new Insets(10));
        canvasContainer.setStyle("-fx-background-color: #2B2B2B;");

        // 4. Main container
        HBox mainLayout = new HBox(leftPanel, canvasContainer);

        renderBoard();

        // 6. WINDOW LAUNCH
        Scene scene = new Scene(mainLayout);
        stage.setTitle("Farm Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void renderBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Field field = board.getField(x, y);

                // We choose the color of the field depending on what is growing in it
                if (field.getFieldState().equals("growing") || field.getFieldState().equals("maturely")) {
                    switch (field.getCropType()) {
                        case CARROT -> gc.setFill(Color.ORANGE);
                        case POTATO -> gc.setFill(Color.BROWN);
                        case WHEAT -> gc.setFill(Color.GOLD);
                        default -> gc.setFill(Color.DARKGREEN);
                    }
                } else {
                    // Empty field color
                    gc.setFill(Color.web("#557A46"));
                }

                // Draw a square field
                gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);

                gc.setStroke(Color.web("#3A4D39"));
                gc.setLineWidth(1);
                gc.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
