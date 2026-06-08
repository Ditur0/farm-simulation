package com.ditur.builder;

import com.ditur.Board;
import com.ditur.crops.CropType;
import com.ditur.Field;

import java.util.List;
import java.util.Random;

public class Pest extends Agent{
    public Pest(int id, int x, int y, Board board, int energy, String name, String type) {
        super(id, x, y, board, energy, name, type);
    }
    private final Random random = new Random();
    private boolean isDead = false;

    @Override
    public void step() {
        if (isDead) return;
        Field currentField = board.getField(x, y);

        if (currentField.isPesticideActive()) {
           this.isDead = true;
            return;
        }

        this.energy--;
        if (energy <= 0) {
            isDead = true;
            return;
        }


        if (currentField.getFieldState().equals("growing") || currentField.getFieldState().equals("maturely")) {

            CropType cropConsumed = currentField.getCropType();

            if (cropConsumed != CropType.NONE) {
                int energyGained = 0;

                switch (cropConsumed) {
                    case CARROT -> energyGained = 5;
                    case POTATO -> energyGained = 5;
                    case WHEAT -> energyGained = 5;
                }
                this.energy += energyGained;

            }

            currentField.consumeCrop();
        }


        // Rozmnażanie, dodanie z procentowa szansa na rozmanzanie, zeby nie bylo takie szybki
        if (this.energy >= 50 && this.offspring == null && random.nextDouble() < 0.40) {
            this.energy -= 20;

            this.offspring = AgentFactory.createPest(
                    random.nextInt(1000),
                    this.x +2,
                    this.y +2,
                    this.board,
                    30,
                    "baby_pest" // nazwa baby pest zostaje, nie zmieniaj mi jej
            );
        }


        // Targeted movement

        Field targetCrop = findNearsetCrop(5); // Zasieg wzroku robala

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

            if (random.nextDouble() < 0.30) {
                moveX += (random.nextInt(3) - 1);
                moveY += (random.nextInt(3) - 1);
            }

        } else {
            moveX += (random.nextInt(3) - 1);
            moveY += (random.nextInt(3) - 1);
        }

        moveX = (moveX + board.getWidth()) % board.getWidth();
        moveY = (moveY + board.getHeight()) % board.getHeight();

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



    public void kill(){
        this.isDead = true;
    }
}