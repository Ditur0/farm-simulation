package com.ditur.builder;

import com.ditur.Board;

/**
 * Realizuje wzorzec projektowy Factory (Fabryka).
 * Odpowiada za tworzenie konkretnych instancji obiektow roznych typow agentow.
 */
public class AgentFactory {

    /**
     * Tworzy i inicjalizuje nowa instancje agenta typu Bee (Pszczola).
     * @param id unikalny identyfikator agenta
     * @param x poczatkowa wspolrzedna X na planszy
     * @param y poczatkowa wspolrzedna Y na planszy
     * @param board obiekt planszy symulacji
     * @param name unikalna nazwa tekstowa pszczoly
     * @return nowa instancja klasy Bee z domyslnym poziomem energii (100)
     */
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

    /**
     * Tworzy i inicjalizuje nowa instancje agenta typu Pest (Szkodnik).
     * @param id unikalny identyfikator agenta
     * @param x poczatkowa wspolrzedna X na planszy
     * @param y poczatkowa wspolrzedna Y na planszy
     * @param board obiekt planszy symulacji
     * @param energy biezacy lub poczatkowy poziom energii szkodnika
     * @param name unikalna nazwa tekstowa szkodnika
     * @return nowa instancja klasy Pest z typem ustawionym na "PEST"
     */
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

    /**
     * Tworzy i inicjalizuje nowa instancje agenta typu Farmer (Rolnik).
     * @param id unikalny identyfikator agenta
     * @param x poczatkowa wspolrzedna X na planszy
     * @param y poczatkowa wspolrzedna Y na planszy
     * @param board obiekt planszy symulacji
     * @param energy poczatkowy maksymalny poziom energii rolnika
     * @param name unikalna nazwa tekstowa rolnika
     * @return nowa instancja klasy Farmer z typem ustawionym na "FARMER"
     */
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