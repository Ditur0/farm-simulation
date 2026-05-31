package com.ditur.builder;

import com.ditur.Field;

import java.util.Random;

public class Bee extends Agent {
    private final Random random = new Random();
    private boolean isDead = false;

    // Własny builder dedykowany dla Pszczoły
    public static class BeeBuilder extends Agent.Builder<BeeBuilder> {
        @Override
        protected BeeBuilder self() { return this; }

        @Override
        public Bee build() {
            this.type = "BEE";
            return new Bee(this);
        }
    }

    private Bee(BeeBuilder builder) {
        super(builder);
    }

    @Override
    public void step() {
        if (isDead) return;

        // 1. Sprawdzenie czy pole, na którym stoi, ma aktywny pestycyd
        Field currentField = board.getField(x, y);
        if (currentField.isPesticideActive()) {
            isDead = true;
            return;
        }

        // 2. Jeśli pole rośnie – zapylaj (skróć czas wzrostu o 2 kroki)
        if (currentField.getFieldState().equals("growing")) {
            // Logika skrócenia czasu wzrostu (symulacja zapylania)
            currentField.updateState(); // przyspieszamy wzrost sztucznie o dodatkowy krok
        }

        // 3. Wykonaj losowy ruch (Sąsiedztwo Moore'a)
        int moveX = x + (random.nextInt(3) - 1); // -1, 0 lub 1
        int moveY = y + (random.nextInt(3) - 1); // -1, 0 lub 1
        moveTo(moveX, moveY);
    }

    public boolean isDead() {
        return isDead;
    }
}
