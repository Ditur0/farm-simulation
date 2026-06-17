package com.ditur;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje dwuwymiarowa plansze symulacji.
 * Klasa implementuje torusa (zawijanie krawedzi planszy) oraz algorytm wyszukiwania sasiadow.
 */
public class Board {
    /** Szerokosc planszy (liczba kolumn). */
    private final int width;

    /** Wysokosc planszy (liczba wierszy). */
    private final int height;

    /** Dwuwymiarowa siatka przechowujaca obiekty pol (Field). */
    private final Field[][] grid;

    /**
     * Konstruktor klasy Board inicjalizujacy wymiary planszy oraz siatke pol.
     * @param width szerokosc planszy
     * @param height wysokosc planszy
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Field[width][height];
        initializeGrid();
    }

    /**
     * Wypelnia plansze nowymi, czystymi obiektami typu Field.
     */
    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Field(x, y);
            }
        }
    }

    /**
     * Pobiera pole o podanych wspolrzednych.
     * Implementuje torusa - przeniesienie na przeciwna strone planszy w przypadku przekroczenia zakresu.
     * @param x wspolrzedna X (kolumna)
     * @param y wspolrzedna Y (wiersz)
     * @return obiekt Field znajdujacy sie na podanych (i odpowiednio zawinietych) wspolrzednych
     */
    public Field getField(int x, int y) {
        int wrappedX = (x % width + width) % width;
        int wrappedY = (y % height + height) % height;
        return grid[wrappedX][wrappedY];
    }

    /**
     * Zwraca liste pol wewnatrz podanego promienia (sasiadztwo Moore'a z uwzglednieniem torusa).
     * Wykorzystywane przez agentow do okreslania pola widzenia lub aplikacji pestycydow.
     * @param centerX wspolrzedna X srodka obszaru
     * @param centerY wspolrzedna Y srodka obszaru
     * @param radius promien wyszukiwania sasiadow
     * @return lista obiektow Field znajdujacych sie w zadanym promieniu
     */
    public List<Field> getNeighbors(int centerX, int centerY, int radius) {
        List<Field> neighbors = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                // We add a field (secured by a torus) to the neighbor list
                neighbors.add(getField(centerX + dx, centerY + dy));
            }
        }
        return neighbors;
    }

    /**
     * Zlicza wszystkie pola na planszy, na których znajduje sie roslina (w stanie wzrostu lub dojrzała).
     * Metoda uzywana glownie do generowania danych do wykresu.
     * @return liczba posadzonych upraw na planszy
     */
    public int countPlantedCrops() {
        int count = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String state = getField(x, y).getFieldState();
                // Zliczamy pola, które aktualnie rosną lub są dojrzałe
                if (state.equals("growing") || state.equals("mature") || state.equals("maturely")) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Pobiera szerokosc planszy.
     * @return szerokosc planszy
     */
    public int getWidth() { return width; }

    /**
     * Pobiera wysokosc planszy.
     * @return wysokosc planszy
     */
    public int getHeight() { return height; }
}