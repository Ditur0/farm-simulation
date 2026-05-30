package com.ditur;

public abstract class Agent {
    protected int id;
    protected int x;
    protected int y;
    protected Board board;
    protected int energy;
    protected String name;
    protected String type;

    public static class Builder{
        private int id;
        private int x;
        private int y;
        private int energy;
        private Board board;
        private String name;
        private String type;

        public Builder setId(int id) { this.id = id; return this; }
        public Builder setX(int x) { this.x = x; return this; }
        public Builder setY(int y) { this.y = y; return this; }
        public Builder setName(String name) { this.name = name; return this; }
        public Builder setType(String type) { this.type = type; return this; }
        public Builder setBoard(Board board) { this.board = board; return this; }
        public Builder setEnergy(int energy) { this.energy = energy; return this; }
    }

    protected Agent(Builder builder){
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
        this.x = newX;
        this.y = newY;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getType() { return type; }
    public int getEnergy() { return energy; }

}
