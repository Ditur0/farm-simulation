package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;

/**
 * Abstrakcyjna klasa bazowa reprezentujaca agenta w symulacji.
 * Dostarcza wspolne wlasciwosci takie jak ID, pozycja, poziom energii oraz tozsamosc agenta.
 */
public abstract class Agent {

    /** Unikalny identyfikator agenta. */
    protected int id;

    /** Aktualna wspolrzedna X pozycji agenta na planszy. */
    protected int x;

    /** Aktualna wspolrzedna Y pozycji agenta na planszy. */
    protected int y;

    /** Referencja do obiektu planszy symulacji. */
    protected Board board;

    /** Biezacy poziom energii agenta. */
    protected int energy;

    /** Unikalna nazwa tekstowa przypisana do agenta. */
    protected String name;

    /** Typ okreslajacy role agenta (np. BEE, PEST, FARMER). */
    protected String type;

    /** Przechowuje referencje do nowo narodzonego agenta utworzonego w biezacym cyklu symulacji. */
    protected Agent offspring = null;

    /**
     * Konstruktor klasy bazowej Agent inicjalizujacy podstawowe parametry i ustawiajacy pozycje na planszy.
     * @param id unikalny identyfikator agenta
     * @param x poczatkowa wspolrzedna X
     * @param y poczatkowa wspolrzedna Y
     * @param board obiekt planszy symulacji
     * @param energy poczatkowy poziom energii
     * @param name nazwa agenta
     * @param type typ agenta
     */
    public Agent(int id, int x, int y, Board board, int energy, String name, String type) {
        this.id = id;
        this.board = board;
        this.energy = energy;
        this.name = name;
        this.type = type;

        moveTo(x, y);
    }

    /**
     * Abstrakcyjna metoda definiujaca zachowanie agenta w pojedynczym kroku symulacji.
     * Kazda klasa pochodna musi zaimplementowac wlasna logike zyciowa.
     */
    public abstract void step();

    /**
     * Przemieszcza agenta na nowe wspolrzedne, korzystajac z bezpiecznego zawijania krawedzi (topologia torusa).
     * @param newX docelowa wspolrzedna X przed zawinieciem
     * @param newY docelowa wspolrzedna Y przed zawinieciem
     */
    public void moveTo(int newX, int newY) {
        Field wrappedField = board.getField(newX, newY);
        this.x = wrappedField.getX();
        this.y = wrappedField.getY();
    }

    // --- Sekcja zarzadania potomstwem ---

    /**
     * Zwraca referencje do nowego agenta stworzonego przez tego agenta (np. przez szkodnika).
     * @return obiekt nowego agenta potomnego, lub null jesli potomek nie zostal powolany
     */
    public Agent getOffspring() {return offspring; }

    /**
     * Czysci referencje do potomka po tym, jak zostal on pomyslnie dodany do glownych struktur symulacji.
     */
    public void clearOffspring() {
        this.offspring = null;
    }

    /**
     * Pobiera aktualna wspolrzedna X pozycji agenta.
     * @return wartosc X
     */
    public int getX() { return x; }

    /**
     * Pobiera aktualna wspolrzedna Y pozycji agenta.
     * @return wartosc Y
     */
    public int getY() { return y; }

    public int getEnergy() { return this.energy; }
}