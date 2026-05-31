package com.ditur.builder;

import com.ditur.Board;
import com.ditur.CropType;
import com.ditur.Field;

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

//        if (currentField.isPesticideActive()) {
//            this.isDead = true;
//            return;
//        }

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
                    case POTATO -> energyGained = 10;
                    case WHEAT -> energyGained = 15;
                }
                this.energy += energyGained;

            }

            currentField.consumeCrop();
        }

        // Rozmnażanie
 /*
        if(this.energy >=60 && this.offspring == null){

            this.energy -=30;

            this.offspring = AgentFactory.createPest(
                    random.nextInt(1000),
                    this.x,
                    this.y,
                    this.board,
                    30,
                    "baby_pest" // nazwa baby pest zostaje, nie zmieniaj mi jej

            );
        }
*/

        int moveX = x + (random.nextInt(3) - 1);
        int moveY = y + (random.nextInt(3) - 1);
        moveTo(moveX, moveY);

    }
    public boolean isDead() {
        return isDead;
    }
}
