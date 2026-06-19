package com.ditur;

import com.ditur.crops.CropType;

/**
 * Reprezentuje pojedyncze pole na planszy.
 * Odpowiada za cykl zycia uprawy oraz stan pestycydow.
 */
public class Field {
    /** Wspolrzedna X pola na planszy. */
    private final int x;

    /** Wspolrzedna Y pola na planszy. */
    private final int y;

    /** Poziom nawodnienia pola. */
    private int hydrationLevel;

    /** Stan pola (np. empty, growing, maturely). */
    private String fieldState; // empty, growing, maturely

    /** Czas pozostaly do pelnego wzrostu rosliny. */
    private int growthTime;

    /** Typ uprawy posadzonej na polu. */
    private CropType cropType;

    // Logika pestycodow
    /** Czy pestycyd jest aktywny na tym polu. */
    private boolean isPesticideActive;

    /** Liczba cykli (tickow) pozostala do konca dzialania pestycydu. */
    private int pesticideTicksLeft = 0;

    /** Maksymalny czas trwania pestycydu po zaaplikowaniu. */
    private int pesticideMaxDuration; // czas trwania

    /** Maksymalny mozliwy poziom nawodnienia pola. */
    private static final int MAX_HYDRATION = 100;

    /**
     * Konstruktor klasy Field inicjalizujacy domyslny stan pola.
     * @param x wspolrzedna X nowego pola
     * @param y wspolrzedna Y nowego pola
     */
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        this.hydrationLevel = MAX_HYDRATION;
        this.fieldState = "empty";
        this.growthTime = 0;
        this.isPesticideActive = false;
        this.cropType = CropType.NONE;
    }

    /**
     * Glowna metoda aktualizujaca stan pola, wywolywana z kazdym tickiem symulacji.
     * Odpowiada za proces wzrostu rosliny oraz obsluge suszy.
     */
    public void updateState() {
        // Logika wzrostu upraw
        if (fieldState.equals("growing")) {
            if (hydrationLevel > 0) { // The plant only grows when there is water
                if (growthTime > 0) {
                    growthTime--;
                }
                // Jesli czas wzortu dobielg konca roslina staje sie dojrzala
                if (growthTime == 0) {
                    fieldState = "maturely";
                }
            } else {
                // If the field dries out completely, the plant dies after some time.
                consumeCrop();
            }
        }
    }

    /**
     * Sadzi okreslony typ uprawy na polu i ustawia poczatkowy czas wzrostu.
     * @param type typ rosliny do posadzenia (lub CropType.NONE do wyczyszczenia)
     */
    public void setCrop(CropType type) {
        if (type == CropType.NONE) {
            consumeCrop();
        } else {
            this.cropType = type;
            this.fieldState = "growing";
            this.growthTime = type.getBaseGrowthTime();
        }
    }

    /**
     * Przywraca maksymalny poziom nawodnienia pola (podlewanie).
     */
    public void waterField() {
        this.hydrationLevel = MAX_HYDRATION;
    }

    /**
     * Czysci pole i resetuje jego stan po zjedzeniu, zebraniu plonu lub jego obumarciu.
     */
    public void consumeCrop() {
        this.cropType = CropType.NONE;
        this.fieldState = "empty";
        this.growthTime = 0;
    }

    /**
     * Aplikuje pestycyd na pole z okreslona maksymalna liczba cykli trwania.
     * @param maxDuration czas trwania pestycydu wyrazony w tickach
     */
    public void applyPesticide(int maxDuration) {
        this.pesticideMaxDuration = maxDuration;
        this.pesticideTicksLeft = maxDuration;
    }

    /**
     * Sprawdza, czy na polu wciaz jest aktywny pestycyd.
     * @return true, jesli pestycyd wciaz dziala; false w przeciwnym razie
     */
    public boolean hasPesticide() {
        return pesticideTicksLeft > 0;
    }

    /**
     * Zwraca liczbe tickow pozostala do konca dzialania pestycydu.
     * @return pozostaly czas pestycydu
     */
    public int getPesticideTicksLeft() {
        return pesticideTicksLeft;
    }

    /**
     * Zwraca maksymalny zdefiniowany czas trwania zaaplikowanego pestycydu.
     * @return maksymalny czas pestycydu
     */
    public int getPesticideMaxDuration() {
        return pesticideMaxDuration;
    }

    /**
     * Aktualizuje licznik czasu dzialania pestycydu, zmniejszajac go o jeden cykl.
     */
    public void updatePesticide() {
        if (pesticideTicksLeft > 0) {
            pesticideTicksLeft--;
        }
    }

    /**
     * Pobiera wspolrzedna X pola.
     * @return wartosc X
     */
    public int getX() { return x; }

    /**
     * Pobiera wspolrzedna Y pola.
     * @return wartosc Y
     */
    public int getY() { return y; }

    /**
     * Pobiera aktualny poziom nawodnienia pola.
     * @return poziom nawodnienia
     */
    public int getHydrationLevel() { return hydrationLevel; }

    /**
     * Pobiera aktualny stan logiczny pola (np. empty, growing, maturely).
     * @return lancuch znakow reprezentujacy stan pola
     */
    public String getFieldState() { return fieldState; }

    /**
     * Sprawdza, czy flagowy stan pestycydu jest aktywny.
     * @return stan isPesticideActive
     */
    public boolean isPesticideActive() { return pesticideTicksLeft > 0; }

    /**
     * Pobiera aktualny typ rosliny znajdujacej sie na polu.
     * @return obiekt CropType
     */
    public CropType getCropType() { return cropType; }
}