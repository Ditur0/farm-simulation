package com.ditur;

public class Settings {

    public Settings() {}

    // --- USTAWIENIA PLANSZY ---
    public static final int CELL_SIZE = 35;
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 22;
    public static final int START_FORESTATION = 25;

    // --- PSZCZOLY (BEES) ---
    public static final int BOOST_POLLINATE = 3;

    // --- SZKODNIKI (PESTS) ---
    public static final int PEST_VIEW_RADIUS = 5;
    public static final int PEST_START_ENERGY = 50;
    public static final int BABYPEST_START_ENERGY = 30;
    public static final int ENERGY_GAINED_CARROT = 5;
    public static final int ENERGY_GAINED_POTATO = 5;
    public static final int ENERGY_GAINED_WHEAT = 5;
    public static final int ENERGY_TO_MULTIPLICATION = 50;
    public static final int ENERGY_TAKEN_MULTIPLICATION = 20;
    public static final double RANDOM_FACTOR_REPRODUCTION = 0.4;

    // --- ROLNICY (FARMERS) ---
    public static final int PESTICIDE_COOLDOWN = 100;
    public static final int PESTICIDE_DURATION = 20;
    public static final int ENERGY_RECOVER = 5;
    public static final int FARMER_VIEW_RADIUS = 10;
    public static final double FARMER_GO_OFF_TRACK = 0.3;

    // --- PLONY ---
    public static final int CARROT_GROWTH_TIME = 12;
    public static final int POTATO_GROWTH_TIME = 18;
    public static final int WHEAT_GROWTH_TIME = 25;
    public static final int NONE = 0;
}
