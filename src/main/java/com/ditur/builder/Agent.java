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

    // Abstract class for sub-builders
    public abstract static class Builder<T extends Builder<T>> {
        protected int id;
        protected int x;
        protected int y;
        protected int energy;
        protected Board board;
        protected String name;
        protected String type;

        protected abstract T self();

        public T setId(int id) { this.id = id; return self(); }
        public T setX(int x) { this.x = x; return self(); }
        public T setY(int y) { this.y = y; return self(); }
        public T setName(String name) { this.name = name; return self(); }
        public T setType(String type) { this.type = type; return self(); }
        public T setBoard(Board board) { this.board = board; return self(); }
        public T setEnergy(int energy) { this.energy = energy; return self(); }

        public abstract Agent build();
    }

    protected Agent(Builder<?> builder){
        this.id = builder.id;
        this.x = builder.x;
        this.y = builder.y;
        this.board = builder.board;
        this.energy = builder.energy;
        this.name = builder.name;
        this.type = builder.type;
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
}
