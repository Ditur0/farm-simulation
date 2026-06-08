package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;

public abstract class Agent {

    protected int id;
    protected int x;
    protected int y;
    protected Board board;
    protected int energy;
    protected String name;
    protected String type;

    public Agent(int id, int x, int y, Board board, int energy, String name, String type) {
        this.id = id;
        this.board = board;
        this.energy = energy;
        this.name = name;
        this.type = type;

        moveTo(x, y);
    }

    public abstract void step();

    public void moveTo(int newX, int newY) {
        Field wrappedField = board.getField(newX, newY);
        this.x = wrappedField.getX();
        this.y = wrappedField.getY();
    }



    public int getX() { return x; }
    public int getY() { return y; }
    public String getType() { return type; }
    public int getEnergy() { return energy; }

    // do dzieciakow
     protected Agent offspring = null;
     public Agent getOffspring() {return offspring; }
     public void clearOffspring() {
        this.offspring = null;
    }


}