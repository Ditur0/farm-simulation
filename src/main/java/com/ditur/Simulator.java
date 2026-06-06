package com.ditur;

import com.ditur.builder.*;
import com.ditur.crops.CropGenerator;
import com.ditur.crops.CropType;
import com.ditur.ui.SimulationView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator extends Application {

    /*
        TODO:
            - add eliminate pest from farmer
            - Pesticide class for Farmer
            - Harvest class for Farmer
            - GRAPHS !!!!
            - Repair slider speed !!!
            - add RESET BUTTON
            - make pest multiplication !!!!!
            - hwo fast crops lose water
            - setting class
     */

    /*
        DOING:

     */

    /*
        DONE:
            - add Agents abstract class
            - add Bee class
            - add agents builder factory class
            - Optional class for drawing UI
            - add Pest class
            - add random method to generate randomly crops to all cells
            - add Farmer class
            - add better colors for crops and field and optimal growing time
     */

    private Board board;
    private SimulationView view;
    private Timeline timeline;

    // Simulation counters
    private int tickCount = 0;
    public static int harvestedCrops = 0;

    private List<Agent> agents;
    private Random random;
    private static final int CELL_SIZE = 35; // Size of one field in pixels
    private CropGenerator cropGenerator;

    @Override
    public void start(Stage stage) {
        init();

        // 1. Board init
        board = new Board(20, 22);

        cropGenerator.generateCrops(board, 25);

        // 2. UI init
        view = new SimulationView(board.getWidth(), board.getHeight(), CELL_SIZE);

        // 3. Clock
        simulationClock();

        // 4. Merge UI with logic
        view.getBtnStart().setOnAction(e -> timeline.play());
        view.getBtnPause().setOnAction(e -> timeline.pause());

        actionOffButtons();

        // 6. WINDOW LAUNCH
        Scene scene = new Scene(view.getMainLayout());
        stage.setTitle("Farm Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void init() {
        agents = new ArrayList<>();
        random = new Random();
        cropGenerator = new CropGenerator();
    }

    private void actionOffButtons() {
        view.getBtnSpawnBees().setOnAction(e -> {
            try {
                int count = Integer.parseInt(view.getTfBeeCount().getText());
                for (int i = 0; i < count; i++) {
                    Bee newBee = AgentFactory.createBee(
                            agents.size(),
                            random.nextInt(board.getWidth()),
                            random.nextInt(board.getHeight()),
                            board,
                            "Bee " + i
                    );
                    agents.add(newBee);
                }
                view.render(board, agents);
            } catch (NumberFormatException ex) {
                view.getTfBeeCount().setText("Enter a number!");
            }
        });

        view.getBtnSpawnPest().setOnAction(e -> {
            try {
                int count = Integer.parseInt(view.getTfPestCount().getText());
                for (int i = 0; i < count; i++) {
                    Pest newPest = AgentFactory.createPest(
                            agents.size(),
                            random.nextInt(board.getWidth()),
                            random.nextInt(board.getHeight()),
                            board,
                            50, // przykładoway stan energii początkowej
                            "Pest" + i
                    );
                    agents.add(newPest);
                }
                view.render(board, agents);
            } catch (NumberFormatException ex) {
                view.getTfPestCount().setText("Enter a number!");
            }
        });

        view.getBtnSpawnFarmer().setOnAction(e -> {
            try {
                int count = Integer.parseInt(view.getTfFarmerCount().getText());
                for (int i = 0; i < count; i++) {
                    Farmer newFarmer = AgentFactory.createFarmer(
                            agents.size(),
                            random.nextInt(board.getWidth()),
                            random.nextInt(board.getHeight()),
                            board,
                            100, // przykładoway stan energii początkowej
                            "Farmer" + i
                    );
                    agents.add(newFarmer);
                }
                view.render(board, agents);
            } catch (NumberFormatException ex) {
                view.getTfPestCount().setText("Enter a number!");
            }
        });

        view.getSpeedSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean wasRunning = timeline.getStatus() == Animation.Status.RUNNING;
            timeline.stop();
            KeyFrame newFrame = new KeyFrame(Duration.millis(newValue.doubleValue()), event -> performSimulationStep());
            timeline.getKeyFrames().setAll(newFrame);
            if (wasRunning) timeline.play();
        });

        view.getBtnGenerateCrops().setOnAction(e -> {
            try {
                int percentage = Integer.parseInt(view.getTfCropPercentage().getText());
                if (percentage < 0 || percentage > 100) {
                    view.getTfCropPercentage().setText("0-100!");
                    return;
                }

                cropGenerator.generateCrops(board, percentage);

                view.render(board, agents);
            } catch (NumberFormatException ex) {
                view.getTfCropPercentage().setText("Enter a number!");
            }
        });

        view.render(board, agents);
    }

    private void simulationClock() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(view.getSpeedSlider().getValue()),
                event -> performSimulationStep());
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE); // Simulation endless loop
    }

    private void performSimulationStep() {
        tickCount++;
        view.updateStats(tickCount, harvestedCrops);

        List<Agent> babies = new ArrayList<>();

        // Step for every living agent
        for (int i = agents.size() - 1; i >= 0; i--) {
            Agent a = agents.get(i);
            a.step();

//            If bee is dead remove from list OPTIONAL
//            if (a instanceof Bee && ((Bee) a).isDead()) {
//                agents.remove(i);
//            }
            if(a.getOffspring() != null){
                agents.addAll(babies);
            }
            if (a instanceof Pest && ((Pest) a).isDead()) {
               agents.remove(i);
            }

        }

        // Go through all the fields on the board and update their logical states
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                board.getField(x, y).updateState();
            }
        }

        // After updating the logical data draw the image again
        view.render(board, agents);
    }

    public static void main(String[] args) {
        launch();
    }
}
