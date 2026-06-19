package com.ditur.builder;

import com.ditur.Board;
import com.ditur.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PestTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(5, 5);
    }

    /**
     * TEST 1: Sprawdza, czy szkodnik natychmiast umiera,
     * gdy pole pod nim ma aktywny pestycyd.
     */
    @Test
    void testPestDiesWhenStandingOnActivePesticide() {
        Pest pest = AgentFactory.createPest(1, 2, 2, board, 10, "TestPest");

        Field currentField = board.getField(pest.getX(), pest.getY());

        currentField.applyPesticide(5);

        assertTrue(currentField.isPesticideActive(),
                "Metoda isPesticideActive() zwraca false zaraz po nalozeniu pestycydu!");

        pest.step();

        assertTrue(pest.isDead(), "Szkodnik powinien byc martwy, poniewaz na polu jest aktywny pestycyd!");
    }

    /**
     * TEST 2: Sprawdza, czy szkodnik traci 1 punkt energii w kazdym kroku
     * oraz czy umiera z glodu, gdy jego energia spadnie do zera.
     */
    @Test
    void testPestDiesFromStarvation() {
        int poczatkowaEnergia = 1;
        Pest pest = AgentFactory.createPest(2, 1, 1, board, poczatkowaEnergia, "GłodującySzkodnik");

        Field currentField = board.getField(pest.getX(), pest.getY());
        currentField.consumeCrop(); // Czyścimy pole do stanu "empty"

        pest.step();

        assertEquals(0, pest.getEnergy(), "Energia szkodnika powinna spasc do 0 po jednym kroku!");

        assertTrue(pest.isDead(), "Szkodnik powinien byc martwy, poniewaz skonczyla mu sie energia!");
    }
}