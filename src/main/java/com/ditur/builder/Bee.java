package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;
import com.ditur.Settings;
import com.ditur.Simulator;

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

        // 2. Pollination (if the field grows, we speed it up one extra step)
        if (currentField.getFieldState().equals("growing")) {
            int boostPower = Settings.BOOST_POLLINATE; // Dodanie 3 tickow rosniecia po zapyleniu
            for (int i = 0; i < boostPower; i++) {
                if (currentField.getFieldState().equals("growing")) {
                    currentField.updateState();
                    Simulator.pollinatedCrops++;
                }
            }
        }

        // Omijanie pestycydu
        int newX = this.getX() + random.nextInt(3) - 1; // ruch -1, 0, 1
        int newY = this.getY() + random.nextInt(3) - 1;

        if (newX >= 0 && newX < board.getWidth() && newY >= 0 && newY < board.getHeight()) {
            if (!board.getField(newX, newY).hasPesticide()) {
                moveTo(newX, newY);
            }
        }
    }

    public boolean isDead() {
        return isDead;
    }
}
