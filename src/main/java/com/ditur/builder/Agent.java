package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;

// Abstrakcyjna klasa bazowa reprezentujaca agenta
// Dostarcza wspolne wlasciwosci taki ejak  ID, pozycja, pozim energi ...
public abstract class Agent {

    protected int id;
    protected int x;
    protected int y;
    protected Board board;
    protected int energy;
    protected String name;
    protected String type;

    // Przechowuje nowo narodzonego agenta w danym cyklu symulacji
    protected Agent offspring = null;

    public Agent(int id, int x, int y, Board board, int energy, String name, String type) {
        this.id = id;
        this.board = board;
        this.energy = energy;
        this.name = name;
        this.type = type;

        moveTo(x, y);
    }

    // Abstrakcyjna metoda definiujaca zachowanie agenta w pojedyncym korku symulacji
    public abstract void step();

    // Przemieszcza agenta na nowe wspolrzedne, zgodnie z topologia torusa
    public void moveTo(int newX, int newY) {
        Field wrappedField = board.getField(newX, newY);
        this.x = wrappedField.getX();
        this.y = wrappedField.getY();
    }

    // --- Sekcja zarzadania potomstwem ---

    // Zwraca referencje do nowego agenta stworzeonego przez tego szkodnika
    public Agent getOffspring() {return offspring; }

    // Czysci referencje po tym jka zostal dodany do symualcji
    public void clearOffspring() {
        this.offspring = null;
    }


    // Geters
    public int getX() { return x; }
    public int getY() { return y; }
}