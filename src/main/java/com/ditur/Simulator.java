package com.ditur;

import com.ditur.builder.Agent;
import com.ditur.builder.Bee;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator extends Application {

    /*
        TODO:
            - add random method to generate randomly crops to all cells
            - add Agents abstract class
            - add Farmer class
            - add Bee class
            - add Pest class
            - Pesticide class for Farmer
            - Harvest class for Farmer
            - Optional class for drawing UI
            - GRAPHS !!!!
            - Repair slider speed !!!
     */

    private Board board;
    private Canvas canvas;
    private Timeline timeline;

    private List<Agent> agents = new ArrayList<>();
    private Random random = new Random();

    // Simulation counters
    private int tickCount = 0;
    private int harvestedCrops = 0;

    // UI components
    private Label lblTicks;
    private Label lblCrops;
    private TextField tfBeeCount;

    private static final int CELL_SIZE = 35; // Size of one field in pixels

    @Override
    public void start(Stage stage) {
        // 1. Board init
        board = new Board(20, 20);

        // TODO: method for randomly fill board
        board.getField(2, 2).setCrop(CropType.CARROT);
        board.getField(5, 5).setCrop(CropType.POTATO);
        board.getField(8, 3).setCrop(CropType.WHEAT);
        board.getField(10, 10).setCrop(CropType.POTATO);

        // 2. Left side of Simulator
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

        lblTicks = new Label("Simulation step: 0");
        lblCrops = new Label("Harvested crops: 0");

        Label beeLabel = new Label("Initial Bee count:");
        tfBeeCount = new javafx.scene.control.TextField("5"); // Domyślnie 5 pszczół

        Button btnSpawnBees = new Button("Spawn Bees");
        btnSpawnBees.setMaxWidth(Double.MAX_VALUE);

        // Akcja po kliknięciu przycisku generowania pszczół
        btnSpawnBees.setOnAction(e -> {
            try {
                int count = Integer.parseInt(tfBeeCount.getText());
                for (int i = 0; i < count; i++) {
                    Bee newBee = new Bee.BeeBuilder()
                            .setId(agents.size())
                            .setX(random.nextInt(board.getWidth()))
                            .setY(random.nextInt(board.getHeight()))
                            .setBoard(board)
                            .setEnergy(100)
                            .setName("Bee " + i)
                            .build();
                    agents.add(newBee);
                }
                renderBoard(); // Przerysuj, by natychmiast zobaczyć zmiany
            } catch (NumberFormatException ex) {
                tfBeeCount.setText("Wpisz cyfrę!");
            }
        });

        // Add elements to left panel side
        leftPanel.getChildren().addAll(titleLabel, btnStart, btnPause, speedLabel, speedSlider, statsLabel, lblTicks, lblCrops, beeLabel, tfBeeCount, btnSpawnBees);

        // 3. Right side
        int canvasWidth = board.getWidth() * CELL_SIZE;
        int canvasHeight = board.getHeight() * CELL_SIZE;
        canvas = new Canvas(canvasWidth, canvasHeight);

        VBox canvasContainer = new VBox(canvas);
        canvasContainer.setPadding(new Insets(10));
        canvasContainer.setStyle("-fx-background-color: #2B2B2B;");

        HBox mainLayout = new HBox(leftPanel, canvasContainer);

        // 4. Clock configurations
        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedSlider.getValue()), event -> {
            performSimulationStep();
        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE); // Simulation endless loop

        // 5. Event handling
        btnStart.setOnAction(e -> timeline.play());
        btnPause.setOnAction(e -> timeline.pause());

        // Simulation speed slider
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean wasRunning = timeline.getStatus() == Animation.Status.RUNNING;
            timeline.stop();

            KeyFrame newFrame = new KeyFrame(Duration.millis(newValue.doubleValue()), event -> {
                performSimulationStep();
            });
            timeline.getKeyFrames().setAll(newFrame);

            if (wasRunning) {
                timeline.play();
            }
        });

        // Render empty board
        renderBoard();

        // 6. WINDOW LAUNCH
        Scene scene = new Scene(mainLayout);
        stage.setTitle("Farm Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void performSimulationStep() {
        tickCount++;
        lblTicks.setText("Simulation step: " + tickCount);

        // Go through all the fields on the board and update their logical states
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                board.getField(x, y).updateState();
            }
        }

        // Wykonaj krok dla każdego żywego agenta
        for (int i = agents.size() - 1; i >= 0; i--) {
            Agent a = agents.get(i);
            a.step();

            // Jeśli pszczoła zginęła przez pestycyd, usuń ją z listy
            if (a instanceof Bee && ((Bee) a).isDead()) {
                agents.remove(i);
            }
        }

        // After updating the logical data draw the image again
        renderBoard();
    }

    private void renderBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

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
                gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);

                gc.setStroke(Color.web("#3A4D39"));
                gc.setLineWidth(1);
                gc.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Rysowanie pszczół na planszy jako małe żółte kółka
        for (Agent a : agents) {
            if (a instanceof Bee) {
                gc.setFill(Color.YELLOW);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1);
                // Rysujemy kółko wewnątrz kwadratu pola
                gc.fillOval(a.getX() * CELL_SIZE + 4, a.getY() * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
                gc.strokeOval(a.getX() * CELL_SIZE + 4, a.getY() * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
