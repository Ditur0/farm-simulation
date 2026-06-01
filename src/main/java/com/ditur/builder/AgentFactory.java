package com.ditur.builder;

import com.ditur.Board;

public class AgentFactory {

    public static Bee createBee(int id, int x, int y, Board board, String name) {
        return new Bee(
                id,
                x,
                y,
                board,
                100,
                name
        );
    }
    public static Pest createPest(int id, int x, int y, Board board, int energy, String name){
        return new Pest(
                id,
                x,
                y,
                board,
                energy,
                name,
                "PEST"
        );
    }
    public static Farmer createFarmer(int id, int x, int y, Board board, int energy, String name){
        return new Farmer(
                id,
                x,
                y,
                board,
                energy,
                name,
                "FARMER"
        );
    }

}