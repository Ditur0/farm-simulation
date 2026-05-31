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
}