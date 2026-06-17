package com.ditur;

/**
 * Klasa przechowujaca globalne ustawienia oraz stale konfiguracyjne symulacji.
 */
public class Settings {

    /**
     * Konstruktor domyslny klasy Settings.
     */
    public Settings() {}

    // --- USTAWIENIA PLANSZY ---

    /** Rozmiar pojedynczej komorki planszy w pikselach. */
    public static final int CELL_SIZE = 35;

    /** Szerokosc planszy wyrazona w liczbie komorek. */
    public static final int BOARD_WIDTH = 20;

    /** Wysokosc planszy wyrazona w liczbie komorek. */
    public static final int BOARD_HEIGHT = 22;

    /** Poczatkowy procentowy wskaznik zalesienia/zrodlenia planszy. */
    public static final int START_FORESTATION = 25;

    // --- PSZCZOLY (BEES) ---

    /** Wartosc przyspieszenia wzrostu rosliny po zapyleniu przez pszczole. */
    public static final int BOOST_POLLINATE = 3;

    // --- SZKODNIKI (PESTS) ---

    /** Promien widzenia szkodnika (zasieg wykrywania jedzenia). */
    public static final int PEST_VIEW_RADIUS = 5;

    /** Poczatkowa energia doroslego szkodnika. */
    public static final int PEST_START_ENERGY = 50;

    /** Poczatkowa energia nowo narodzonego szkodnika. */
    public static final int BABYPEST_START_ENERGY = 30;

    /** Energia zyskiwana przez szkodnika po zjedzeniu marchewki. */
    public static final int ENERGY_GAINED_CARROT = 5;

    /** Energia zyskiwana przez szkodnika po zjedzeniu ziemniaka. */
    public static final int ENERGY_GAINED_POTATO = 5;

    /** Energia zyskiwana przez szkodnika po zjedzeniu pszenicy. */
    public static final int ENERGY_GAINED_WHEAT = 5;

    /** Poziom energii wymagany, aby szkodnik mogl sie rozmnozyc. */
    public static final int ENERGY_TO_MULTIPLICATION = 50;

    /** Koszt energetyczny procesu rozmnozenia szkodnika. */
    public static final int ENERGY_TAKEN_MULTIPLICATION = 20;

    /** Losowy wspolczynnik wplywajacy na szanse reprodukcji. */
    public static final double RANDOM_FACTOR_REPRODUCTION = 0.4;

    // --- ROLNICY (FARMERS) ---

    /** Czas oczekiwania (cooldown) na ponowne uzycie pestycydu. */
    public static final int PESTICIDE_COOLDOWN = 100;

    /** Czas utrzymywania sie pestycydu na polu po jego uzyciu. */
    public static final int PESTICIDE_DURATION = 20;

    /** Ilosc energii, jaka rolnik odzyskuje podczas odpoczynku. */
    public static final int ENERGY_RECOVER = 5;

    /** Promien widzenia rolnika (zasieg wykrywania szkodnikow lub dojzalaego plonu). */
    public static final int FARMER_VIEW_RADIUS = 10;

    /** Szansa na to, ze rolnik zejdzie ze swojej optymalnej sciezki. */
    public static final double FARMER_GO_OFF_TRACK = 0.3;

    // --- PLONY ---

    /** Czas potrzebny do pelnego wzrostu marchewki. */
    public static final int CARROT_GROWTH_TIME = 12;

    /** Czas potrzebny do pelnego wzrostu ziemniaka. */
    public static final int POTATO_GROWTH_TIME = 18;

    /** Czas potrzebny do pelnego wzrostu pszenicy. */
    public static final int WHEAT_GROWTH_TIME = 25;

    /** Oznaczenie braku rosliny lub stanu pustego. */
    public static final int NONE = 0;
}