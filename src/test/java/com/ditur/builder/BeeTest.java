package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeeTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(5, 5);
    }

    /**
     * TEST: Sprawdza, czy pszczola unika wejścia na pola,
     * na ktorych znajduje się pestycyd.
     */
    @Test
    void testBeeAvoidsPesticideFields() {
        int startX = 2;
        int startY = 2;
        Bee bee = AgentFactory.createBee(1, startX, startY, board, "Maja");

        Field rightField = board.getField(3, 2);
        Field downField = board.getField(2, 3);

        rightField.applyPesticide(10);
        downField.applyPesticide(10);

        assertTrue(rightField.isPesticideActive(), "Pole (3,2) powinno miec aktywny pestycyd");
        assertTrue(downField.isPesticideActive(), "Pole (2,3) powinno miec aktywny pestycyd");

        for (int i = 0; i < 20; i++) {
            bee.step();

            assertFalse(bee.getX() == 3 && bee.getY() == 2,
                    "BLAD: Pszczola weszla na skazone pole (3,2)!");
            assertFalse(bee.getX() == 2 && bee.getY() == 3,
                    "BLAD: Pszczola weszla na skazone pole (2,3)!");
        }
    }
}