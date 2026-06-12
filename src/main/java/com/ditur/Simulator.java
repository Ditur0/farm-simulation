package com.ditur;

import com.ditur.builder.*;
import com.ditur.crops.CropGenerator;
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

// Glowna klasa kontrolujaca przebieg symylacji
// Odpowiada za petle czasu, zarzadzanie agentami i synchronizuje logike z UI
public class Simulator extends Application {

    // Konfiguracja renderowania i czasu
    private static final int CELL_SIZE = Settings.CELL_SIZE; // Size of one field in pixels
    private static final double MAX_DELAY_OFFSET = 1100.0;

    // Obiekty symulacji
    private Board board;
    private SimulationView view;
    private Timeline timeline;
    private List<Agent> agents;
    private Random random;
    private CropGenerator cropGenerator;

    private int tickCount = 0;
    private int nextAgentId = 0; // Unikalne ID dla kazdego agenta

    // Simulation counters
    public static int harvestedCrops = 0;
    public static int pollinatedCrops = 0;
    public static int plantedCrops = 0;
    public static int pestsKilledByFarmers = 0;
    public static int pestsKilledByPesticide = 0;
    public static int pestsBorn = 0;

    // Parametry dotyczace pestycydow
    public static int pesticideGlobalCooldown = Settings.PESTICIDE_COOLDOWN;
    public static int pesticideGlobalDuration = Settings.PESTICIDE_DURATION;
    public static boolean allowPesticide = true;

    @Override
    public void start(Stage stage) {
        initSimulation();

        // Tworzenie planszy i poczatkowe zalesienie
        board = new Board(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);
        cropGenerator.generateCrops(board, Settings.START_FORESTATION); // Wygenerowanie losowego zalesienia planszy

        // UI init
        view = new SimulationView(board.getWidth(), board.getHeight(), CELL_SIZE);

        // Konfigutacja mechanik sterowania
        setupSimulationClock();
        setupControlButtons(); // Start, Stop, Pause
        setupActionOfButtons(); // Merge UI with logic

        // 6. WINDOW LAUNCH
        Scene scene = new Scene(view.getMainLayout());
        stage.setTitle("Farm Simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    public void initSimulation() {
        agents = new ArrayList<>();
        random = new Random();
        cropGenerator = new CropGenerator();
    }

    // Podpiecie akcji pod glowne przyciski kontroli symulacji
    private void setupControlButtons() {
        view.getBtnStart().setOnAction(e -> timeline.play());
        view.getBtnPause().setOnAction(e -> timeline.pause());
        view.getBtnReset().setOnAction(e -> resetSimulation());
    }

    // Inicjalizacja obslugi zdarzen UI (przyciski dodania agentow, suwak predkosci...)
    private void setupActionOfButtons() {
        spawnBeesButton();
        spawnPestButton();
        spawnFarmersButton();

        generateCropsButton();

        // Suwak predkosci symulacji
        view.getSpeedSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean wasRunning = timeline.getStatus() == Animation.Status.RUNNING;
            timeline.stop();

            // Im wyzsza wartosc suwaka tym szybsza symulacja
            double reversedDelay = MAX_DELAY_OFFSET - newValue.doubleValue();
            KeyFrame newFrame = new KeyFrame(Duration.millis(reversedDelay), event -> performSimulationStep());
            timeline.getKeyFrames().setAll(newFrame);

            if (wasRunning) timeline.play();
        });

        view.render(board, agents);
    }

    private void generateCropsButton() {
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
    }

    private void spawnFarmersButton() {
        view.getBtnSpawnFarmer().setOnAction(e -> {
            try {
                int count = Integer.parseInt(view.getTfFarmerCount().getText());
                for (int i = 0; i < count; i++) {
                    int randomMaxEnergy = 30 + random.nextInt(100);
                    Farmer newFarmer = AgentFactory.createFarmer(
                            nextAgentId++,
                            random.nextInt(board.getWidth()),
                            random.nextInt(board.getHeight()),
                            board,
                            randomMaxEnergy,
                            "Farmer" + i
                    );
                    agents.add(newFarmer);
                }
                view.render(board, agents);
            } catch (NumberFormatException ex) {
                view.getTfPestCount().setText("Enter a number!");
            }
        });
    }

    private void spawnPestButton() {
        view.getBtnSpawnPest().setOnAction(e -> {
            try {
                int count = Integer.parseInt(view.getTfPestCount().getText());
                for (int i = 0; i < count; i++) {
                    Pest newPest = AgentFactory.createPest(
                            nextAgentId++,
                            random.nextInt(board.getWidth()),
                            random.nextInt(board.getHeight()),
                            board,
                            Settings.PEST_START_ENERGY,
                            "Pest" + i
                    );
                    agents.add(newPest);
                }
                view.render(board, agents);
            } catch (NumberFormatException ex) {
                view.getTfPestCount().setText("Enter a number!");
            }
        });
    }

    private void spawnBeesButton() {
        view.getBtnSpawnBees().setOnAction(e -> {
            try {
                int count = Integer.parseInt(view.getTfBeeCount().getText());
                for (int i = 0; i < count; i++) {
                    Bee newBee = AgentFactory.createBee(
                            nextAgentId++,
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
    }

    // Konfiguracja petli czasowej
    private void setupSimulationClock() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(view.getSpeedSlider().getValue()),
                event -> performSimulationStep());
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE); // Simulation endless loop
    }

    // Metoda wykonywana z kazdym tickiem symulacji
    // Zajmuje sie zdazeniami agentow, rozmnazaniem szkodnikow, pestycydy
    private void performSimulationStep() {
        tickCount++;
        updatePesticideSettings(); // Pobranie aktualnych ustawien pestycodw z UI

        List<Agent> babies = new ArrayList<>(); // 'Zlobek' dla nowo narodzonych agentow

        // Step for every living agent, od tylu abe usuwac martwych w locie
        for (int i = agents.size() - 1; i >= 0; i--) {
            Agent agent = agents.get(i);

            // Farmer potrzebuje wiedzy o innych agentach, by moc na nich reagowac
            if (agent instanceof Farmer farmer) {
                farmer.setAllAgents(agents);
            }

            // Wykonanie unikalnej logiki (ruchu/akcji) dla danego agenta
            agent.step();

            // Jesli szkodnik jest na polu z pestycydem usuwa go
            if (agent instanceof Pest && board.getField(agent.getX(), agent.getY()).hasPesticide()) {
                agents.remove(i);
                pestsKilledByPesticide++;
                continue;
            }

            // Rozmnazanie szkodnikow
            if (agent.getOffspring() != null) {
                babies.add(agent.getOffspring()); // Dodajemy baby do 'zlobka'
                agent.clearOffspring();
            }
        }

        // Usuniecie skzodnikow jesli nie maja energi
        for (int i = 0; i < agents.size(); i++) {
            Agent a = agents.get(i);
            if (a instanceof Pest pest && pest.isDead()) {
                agents.remove(i);
                i--;
            }
        }

        // Dodanie 'baby pest' do glownej listy w symulacji
        if (!babies.isEmpty()) {
            agents.addAll(babies);
        }

        // Go through all the fields on the board and update their logical states
        updateBoardFields();

        // Odswiezenie wykresow i danych statystyk na ekranie
        updateChartsAndUI();
    }

    private void updateChartsAndUI() {
        view.updateStats(tickCount, harvestedCrops, pollinatedCrops, plantedCrops, pestsKilledByFarmers, pestsKilledByPesticide, pestsBorn);

        // Wykres dla roslin
        int currentPlantedCrops = board.countPlantedCrops();
        view.addCropDataPoint(tickCount, currentPlantedCrops);

        // Wykres dla szodnikow
        long currentPestCount = agents.stream().filter(a -> a instanceof Pest).count();
        view.addPestDataPoint(tickCount, (int) currentPestCount);

        // Przerysowanie calej planszy
        view.render(board, agents);
    }

    private void updateBoardFields() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                board.getField(x, y).updateState();
                board.getField(x, y).updatePesticide();
            }
        }
    }

    private void updatePesticideSettings() {
        try {
            pesticideGlobalCooldown = Integer.parseInt(view.getTfPesticideCooldown().getText());
            pesticideGlobalDuration = Integer.parseInt(view.getTfPesticideDuration().getText());
        } catch (NumberFormatException e) {
            pesticideGlobalCooldown = Settings.PESTICIDE_COOLDOWN;
            pesticideGlobalDuration = Settings.PESTICIDE_DURATION;
        }
        allowPesticide = view.getCbAllowPesticide().isSelected();
    }

    private void resetSimulation() {
        if (timeline != null) {
            timeline.stop();
        }

        tickCount = 0;
        nextAgentId = 0;
        harvestedCrops = 0;
        pollinatedCrops = 0;
        plantedCrops = 0;
        pestsKilledByFarmers =0;
        pestsKilledByPesticide = 0;
        pestsBorn = 0;

        view.updateStats(tickCount, harvestedCrops, pollinatedCrops, plantedCrops, pestsKilledByFarmers, pestsKilledByPesticide, pestsBorn);
        agents.clear(); // Czyszczenie listy agentow

        // Wygenerowanie swiata na nowo
        board = new Board(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);
        cropGenerator.generateCrops(board, Settings.START_FORESTATION);

        view.clearChart();
        view.render(board, agents);
    }

    public static void main(String[] args) {
        launch();
    }
}
