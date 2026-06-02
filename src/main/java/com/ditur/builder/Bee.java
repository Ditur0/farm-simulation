package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;

import java.util.Random;

public class Bee extends Agent {

    private final Random random = new Random();
    private boolean isDead = false;

    public Bee(int id, int x, int y, Board board, int energy, String name) {
        super(id, x, y, board, energy, name, "BEE");
    }

    @Override
    public void step() {
        if (isDead) return;

        // 1. Check pesticide
        Field currentField = board.getField(x, y);
//        TODO: make bees avoid this filed when pesticide is active
//        if (currentField.isPesticideActive()) {
//            this.isDead = true;
//            return;
//        }

        // 2. Pollination (if the field grows, we speed it up one extra step)
        // TODO: speed up process
        if (currentField.getFieldState().equals("growing")) {
            currentField.updateState();
        }

        // 3. Random move
        int moveX = x + (random.nextInt(3) - 1);
        int moveY = y + (random.nextInt(3) - 1);
        moveTo(moveX, moveY);
    }

    public boolean isDead() {
        return isDead;
    }
}
