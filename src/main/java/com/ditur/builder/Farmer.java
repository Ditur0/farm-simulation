package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Settings;
import com.ditur.crops.CropType;
import com.ditur.Field;
import com.ditur.Simulator;


import java.util.List;
import java.util.Random;

public class Farmer extends Agent{
    public Farmer(int id, int x, int y, Board board, int energy, String name, String type) {
        super(id, x, y, board, energy, name, type);
        this.maxEnergy = energy;
    }

    private List<Agent> allAgents;

    public void setAllAgents(List<Agent> allAgents) {
        this.allAgents = allAgents;
    }

    private final Random random = new Random();

    private int pesticideCooldown = 0;

    private int maxEnergy;
    private boolean isResting = false;


    @Override
    public void step() {

        if(isResting){
            this.energy += Settings.ENERGY_RECOVER;
            if(energy >= maxEnergy){
                this.energy = maxEnergy;
                isResting = false;
            }
            return;
        }
        this.energy--;
        if (energy <= 0) {
            isResting = true;
            return;
        }

        Field currentField = board.getField(x, y);

        if (pesticideCooldown > 0) {
            pesticideCooldown--;
        }

        if (pesticideCooldown == 0 && Simulator.allowPesticide) {
            applyPesticideAround();
        }

        if(currentField.getFieldState().equals("maturely") || currentField.getFieldState().equals("mature")) {
            currentField.consumeCrop();
            Simulator.harvestedCrops++;
        }
        if(currentField.getFieldState().equals("growing")&& currentField.getHydrationLevel() <4){
            currentField.waterField();
        }
        if(currentField.getFieldState().equals("empty") && currentField.getCropType() == CropType.NONE) {
            int randomSeed = random.nextInt(3);

            CropType seedToPlant = CropType.NONE;
            switch (randomSeed){
                case 0 -> seedToPlant = CropType.CARROT;
                case 1 -> seedToPlant = CropType.POTATO;
                case 2 -> seedToPlant = CropType.WHEAT;
            }
            currentField.setCrop(seedToPlant);
            Simulator.plantedCrops++;
        }

        Field targetCrop = findFieldTarget(Settings.FARMER_VIEW_RADIUS); // Przykładowy zasięg wzroku farmera

        int moveX = this.x;
        int moveY = this.y;

        if (targetCrop != null) {
            int diffX = targetCrop.getX() - this.x;
            int diffY = targetCrop.getY() - this.y;

            if (diffX > board.getWidth() / 2) diffX -= board.getWidth();
            else if (diffX < -board.getWidth() / 2) diffX += board.getWidth();

            if (diffY > board.getHeight() / 2) diffY -= board.getHeight();
            else if (diffY < -board.getHeight() / 2) diffY += board.getHeight();

            // Random movement of farmer
            if (diffX != 0 && diffY != 0) {
                if (random.nextBoolean()) {
                    moveX += (diffX > 0) ? 1 : -1;
                } else {
                    moveY += (diffY > 0) ? 1 : -1;
                }
            } else {
                if (diffX > 0) moveX++;
                else if (diffX < 0) moveX--;

                if (diffY > 0) moveY++;
                else if (diffY < 0) moveY--;
            }

            // 30% that farmer will go off track
            if (random.nextDouble() < Settings.FARMER_GO_OFF_TRACK) {
                moveX += (random.nextInt(3) - 1);
                moveY += (random.nextInt(3) - 1);
            }

        } else {
            // Random move when there is no target
            moveX += (random.nextInt(3) - 1);
            moveY += (random.nextInt(3) - 1);
        }
        moveTo(moveX, moveY);

        if (this.allAgents != null) {
            eliminatePests(this.allAgents);
        }
    }

    private Field findFieldTarget(int viewRadius) {
        Field fieldTarget = null;
        int minDistance = Integer.MAX_VALUE;

        List<Field> neighbors = board.getNeighbors(this.x, this.y, viewRadius);

        for (Field field : neighbors) {
            if (field.getFieldState().equals("growing") && field.getHydrationLevel() <4 || field.getFieldState().equals("maturely")|| field.getFieldState().equals("mature") || field.getFieldState().equals("empty")){

                int diffX = Math.abs(field.getX() - this.x);
                int diffY = Math.abs(field.getY() - this.y);

                if (diffX > board.getWidth() / 2) diffX = board.getWidth() - diffX;
                if (diffY > board.getHeight() / 2) diffY = board.getHeight() - diffY;

                int distance = diffX + diffY;

                if (distance < minDistance) {
                    minDistance = distance;
                    fieldTarget = field;
                }
            }
        }
        return fieldTarget;
    }

    private void applyPesticideAround() {
        int fx = this.getX();
        int fy = this.getY();

        // Get the pesticide duration set in the interface by the Simulator class
        int duration = Simulator.pesticideGlobalDuration;

        // Spreads 1 square in each direction (radius 1)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int targetX = fx + dx;
                int targetY = fy + dy;

                if (targetX >= 0 && targetX < board.getWidth() && targetY >= 0 && targetY < board.getHeight()) {
                    board.getField(targetX, targetY).applyPesticide(duration);
                }
            }
        }

        this.pesticideCooldown = Simulator.pesticideGlobalCooldown;
    }

    public void eliminatePests(List<Agent> allAgents){
        for (Agent agent : allAgents){
            if(agent instanceof Pest){
                Pest pest = (Pest) agent;

                if(!pest.isDead()){
                    int diffX = Math.abs(pest.getX() - this.x);
                    int diffY = Math.abs(pest.getY() - this.y);

                    if (diffX > board.getWidth() / 2) diffX = board.getWidth() - diffX;
                    if (diffY > board.getHeight() / 2) diffY = board.getHeight() - diffY;

                    int distance = diffX + diffY;

                    if(distance <2){
                        pest.kill();
                        Simulator.pestsKilledByFarmers++;
                    }
                }
            }
        }
    }
    public boolean isResting() {
        return this.isResting;
    }
}
