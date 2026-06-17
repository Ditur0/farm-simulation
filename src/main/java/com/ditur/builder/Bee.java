package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;
import com.ditur.Settings;
import com.ditur.Simulator;

import java.util.Random;

/**
 * Reprezentuje agenta typu Bee (Pszczola).
 * Pszczoly poruszaja sie losowo po planszy, unikaja pol z aktywnym pestycydem,
 * przyspieszaja wzrost upraw roslin poprzez zapylenie oraz nie umieraja w trakcie symulacji.
 */
public class Bee extends Agent {

    /** Obiekt klasy Random sluzacy do generowania losowych kierunkow lotu pszczoly. */
    private final Random random = new Random();

    /**
     * Konstruktor klasy Bee inicjalizujacy podstawowe parametry pszczoly.
     * @param id unikalny identyfikator agenta
     * @param x poczatkowa wspolrzedna X
     * @param y poczatkowa wspolrzedna Y
     * @param board obiekt planszy symulacji
     * @param energy poziom energii agenta
     * @param name unikalna nazwa tekstowa pszczoly
     */
    public Bee(int id, int x, int y, Board board, int energy, String name) {
        super(id, x, y, board, energy, name, "BEE");
    }

    /**
     * Zachowanie pszczoly w pojedynczym kroku symulacji.
     * Odpowiada za proces zapylania (przyspieszania wzrostu uprawy na biezacym polu)
     * oraz wykonanie ruchu z uwzglednieniem omijania pol skazonych pestycydami.
     */
    @Override
    public void step() {
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
}