package com.ditur;

import java.util.ArrayList;
import java.util.List;

// Reprezentuje dwuwymiarowa plansze symulacji
// Klasa implementuje torusa (zawijanie krawedzi planszy) oraz algorytm wyszukania sasiadow
public class Board {
    private final int width;
    private final int height;
    private final Field[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Field[width][height];
        initializeGrid();
    }

    // Fills the board with clean, new field objects
    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Field(x, y);
            }
        }
    }

    // Pobiera pole o podanyc wsolrzednych
    // Implementuje torusa - przeniesienie na przeciwna strone planszy
    public Field getField(int x, int y) {
        int wrappedX = (x % width + width) % width;
        int wrappedY = (y % height + height) % height;
        return grid[wrappedX][wrappedY];
    }

    // Gets a list of fields within a given radius (Moore neighborhood including a torus)
    // The farmer will use it for sight and for applying pesticide
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

    // Zlicza pola na ktorych znajduje sie roslina
    // Metoda uzwyana do generowania wykresu
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

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
