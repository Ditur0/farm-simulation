package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Settings;
import com.ditur.Simulator;
import com.ditur.crops.CropType;
import com.ditur.Field;

import java.util.List;
import java.util.Random;

// Reprezentuje agenta typu Szkodnik
// Szkodniki zuzywaja energie w kazdym kroku, szukaja najblizszych upraw w swoim polu widzenia
// Zjadaja je w celu odzyskania energii oraz rozmnazja sie po osiagnieciu odpowiedniego poziomu energi
public class Pest extends Agent{

    private final Random random = new Random();
    private boolean isDead = false;

    public Pest(int id, int x, int y, Board board, int energy, String name, String type) {
        super(id, x, y, board, energy, name, type);
    }

    // Zachowanie szkodnika w jendym kroku symulacji
    @Override
    public void step() {
        if (isDead) return;

        Field currentField = board.getField(x, y);

        // Sprawdzanie kontaktu z pestycydem
        if (currentField.isPesticideActive()) {
           this.isDead = true;
            return;
        }

        // Spadek energii
        this.energy--;
        if (energy <= 0) {
            isDead = true;
            return;
        }

        // Zjadanie upraw gdy znajduje sie na polu z roslina
        if (currentField.getFieldState().equals("growing") || currentField.getFieldState().equals("maturely")) {

            CropType cropConsumed = currentField.getCropType();

            if (cropConsumed != CropType.NONE) {
                int energyGained = 0;

                // Dosyaje odpwoeinia ilosc energi z kazdej uprawy
                switch (cropConsumed) {
                    case CARROT -> energyGained = Settings.ENERGY_GAINED_CARROT;
                    case POTATO -> energyGained = Settings.ENERGY_GAINED_POTATO;
                    case WHEAT -> energyGained = Settings.ENERGY_GAINED_WHEAT;
                }
                this.energy += energyGained;
            }
            currentField.consumeCrop();
        }


        // Rozmnażanie, dodanie z procentowa szansa na rozmanzanie, zeby nie bylo takie szybkie
        if (this.energy >= Settings.ENERGY_TO_MULTIPLICATION && this.offspring == null && random.nextDouble() < Settings.RANDOM_FACTOR_REPRODUCTION) {
            this.energy -= Settings.ENERGY_TAKEN_MULTIPLICATION; // Zabranie energi po rozmnozneiu

            this.offspring = AgentFactory.createPest(
                    random.nextInt(1000),
                    this.x +2, // Przesueniecie na inne pole zeb nie byly na tym samym polu po rozmnoznieiu
                    this.y +2,
                    this.board,
                    Settings.BABYPEST_START_ENERGY,
                    "baby_pest" // nazwa baby pest zostaje, nie zmieniaj mi jej
            );
            Simulator.pestsBorn++;
        }

        // Targeted movement
        Field targetCrop = findNearsetCrop(Settings.PEST_VIEW_RADIUS); // Zasieg wzroku robala

        int moveX = this.x;
        int moveY = this.y;

        if (targetCrop != null) {
            int diffX = targetCrop.getX() - this.x;
            int diffY = targetCrop.getY() - this.y;

            if (diffX >= board.getWidth() / 2.0) diffX -= board.getWidth();
            else if (diffX <= -board.getWidth() / 2.0) diffX += board.getWidth();

            if (diffY >= board.getHeight() / 2.0) diffY -= board.getHeight();
            else if (diffY <= -board.getHeight() / 2.0) diffY += board.getHeight();

            if (diffX > 0) moveX++;
            else if (diffX < 0) moveX--;

            if (diffY > 0) moveY++;
            else if (diffY < 0) moveY--;

            // 30% szans za losownie zachowanie robala
            if (random.nextDouble() < 0.30) {
                moveX += (random.nextInt(3) - 1);
                moveY += (random.nextInt(3) - 1);
            }

        } else {
            // Jesli nie ma jedzenia w zasiegu - losowy ruch
            moveX += (random.nextInt(3) - 1);
            moveY += (random.nextInt(3) - 1);
        }

        moveX = (moveX + board.getWidth()) % board.getWidth();
        moveY = (moveY + board.getHeight()) % board.getHeight();

        // Zabezpieczenie gdy szkodnik sie nie ruszal - wymuszenie ruchu
        if (moveX == this.x && moveY == this.y) {
            moveX = (this.x + random.nextInt(3) - 1 + board.getWidth()) % board.getWidth();
            moveY = (this.y + random.nextInt(3) - 1 + board.getHeight()) % board.getHeight();
        }

        moveTo(moveX, moveY);
    }

    public boolean isDead() {
        return isDead;
    }

    // Finding closest crops
    private Field findNearsetCrop(int viewRadius){
        Field closestCrop = null;
        int minDistance = Integer.MAX_VALUE;

        List<Field> neighbors = board.getNeighbors(this.x, this.y, viewRadius);

        for(Field field : neighbors){
            if(field.getFieldState().equals("growing") || field.getFieldState().equals("maturely")){

                int diffX = Math.abs(field.getX() - this.x);
                int diffY = Math.abs(field.getY() - this.y);

                if(diffX > board.getWidth() /2) diffX = board.getWidth() - diffX;
                if(diffY > board.getHeight() /2) diffY = board.getHeight() - diffY;

                int distance = diffX + diffY;

                if(distance < minDistance){
                    minDistance = distance;
                    closestCrop = field;
                }
            }
        }
        return closestCrop;
    }

    // Usmiercenie przez farmera
    public void kill(){
        this.isDead = true;
    }
}