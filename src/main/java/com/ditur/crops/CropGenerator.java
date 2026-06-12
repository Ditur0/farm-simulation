package com.ditur.crops;

import com.ditur.Board;
import com.ditur.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Odpowiada za generowanie oraz losowe rozmieszczenie upraw na planszy
// Uzywana przy uruchomieniu symulacji lub po resecie przez uzytkownika
public class CropGenerator {

    private final Random random = new Random();

    // Zasiewa okreslony procent powierzcnhi losowymi uprawami
    public void generateCrops(Board board, int percentage) {
        clearBoard(board); // Czyszczenie planszy

        // Oblsuga bledu
        if (percentage <= 0) return;
        if (percentage > 100) percentage = 100;

        // Obliczanie liczby pol do obsiania
        int totalFields = board.getWidth() * board.getHeight();
        int fieldsToFill = (int) Math.round((percentage / 100.0) * totalFields);

        // List of all available coordinates (x, y) on the board
        List<int[]> allCoordinates = new ArrayList<>();
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                allCoordinates.add(new int[]{x, y});
            }
        }

        // Mix the list of coordinates (unique coord)
        Collections.shuffle(allCoordinates, random);

        // 4. Sow as many fields as calculated from the percentage
        CropType[] allCrops = CropType.values();

        // List of actual crops only (no NONE)
        List<CropType> actualCrops = new ArrayList<>();
        for (CropType type : allCrops) {
            if (type != CropType.NONE) {
                actualCrops.add(type);
            }
        }

        // Obsiewanie wylosowanych wczesniej unikalnych wspolrzednych
        for (int i = 0; i < fieldsToFill; i++) {
            int[] coord = allCoordinates.get(i);
            int x = coord[0];
            int y = coord[1];

            // Losowanie typu roslin (CARROT, POTATO, WHEAT)
            CropType randomCrop = actualCrops.get(random.nextInt(actualCrops.size()));

            Field field = board.getField(x, y);
            field.setCrop(randomCrop);
        }
    }

    // Przywraca domsylyn stan wszystkim polom
    private void clearBoard(Board board) {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Field field = board.getField(x, y);
                field.setCrop(CropType.NONE);
            }
        }
    }
}
