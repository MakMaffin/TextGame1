package com.example.TextGame;

import java.io.Serializable;

public class GameSave implements Serializable {
    private String[][] map;
    private int[][] field;
    private int playerWater;
    private double playerRice;
    private int playerCells;
    private int playerHouses;
    private int playerPeasants;
    private int aiWater;
    private double aiRice;
    private int aiCells;
    private int aiHouses;
    private int aiPeasants;
    private boolean playerTurn;

    /**
     * Этот метод сохраняет игру
     * @param playerWater Количество воды у игрока
     * @param playerRice Количество риса у игрока
     * @param playerPeasants Количество крестьян у игрока
     * @param playerTurn Очередь хода
     * @param field Карта игры
     * @param map Карта игроков
     * @param playerHouses Количество домов у игрока
     * @param aiWater Количетсво воды AI
     * @param aiRice Количество риса AI
     * @param aiCells Количество клеток AI
     * @param aiHouses Количетсво домов AI
     * @param aiPeasants Количество крестьян игрока
     */
    public GameSave(int[][] field, int playerWater, double playerRice, int playerCells, int playerHouses, int playerPeasants,
                    int aiWater, double aiRice, int aiCells, int aiHouses, int aiPeasants, boolean playerTurn, String[][] map) {
        this.map = map;
        this.field = field;
        this.playerWater = playerWater;
        this.playerRice = playerRice;
        this.playerCells = playerCells;
        this.playerHouses = playerHouses;
        this.playerPeasants = playerPeasants;
        this.aiWater = aiWater;
        this.aiRice = aiRice;
        this.aiCells = aiCells;
        this.aiHouses = aiHouses;
        this.aiPeasants = aiPeasants;
        this.playerTurn = playerTurn;
    }
    /**
     * Этот метод возвращает карту игры
     * @return Карта игры
     */
    public String[][] getMap(){
        return map;
    }
    /**
     * Этот метод возвращает карту игры
     * @return Карта игры
     */
    public int[][] getField() {
        return field;
    }

    /**
     * Этот метод возвращает количество воды игрока
     * @return Количество воды у игрока
     */
    public int getPlayerWater() {
        return playerWater;
    }

    /**
     * Этот метод возвращает количество риса у игрока
     * @return Количество риса у игрока
     */
    public double getPlayerRice() {
        return playerRice;
    }

    /**
     * Этот метод возвращает количество клеток игрока
     * @return Количество клеток игрока
     */
    public int getPlayerCells() {
        return playerCells;
    }

    /**
     * Этот метод возвращает количество домов у игрока
     * @return Количество домов у игрока
     */
    public int getPlayerHouses() {
        return playerHouses;
    }

    /**
     * Этот метод возвращает количество крестьян у игрока
     * @return Количество крестьян у игрока
     */
    public int getPlayerPeasants() {
        return playerPeasants;
    }

    /**
     * Этот метод возвращает количество воды AI
     * @return Количество воды AI
     */
    public int getAiWater() {
        return aiWater;
    }

    /**
     * Этот метод возвращает количество риса AI
     * @return Количество риса AI
     */
    public double getAiRice() {
        return aiRice;
    }

    /**
     * Этот метод возвращает количество клеток AI
     * @return Количество клеток AI
     */
    public int getAiCells() {
        return aiCells;
    }

    /**
     * Этот метод возвращает количество домов AI
     * @return Количество домов AI
     */
    public int getAiHouses() {
        return aiHouses;
    }

    /**
     * Этот метод возвращает количество крестьян AI
     * @return Количество крестьян AI
     */
    public int getAiPeasants() {
        return aiPeasants;
    }

    /**
     * Этот метод возвращает очередь хода
     * @return Очередь хода
     */
    public boolean isPlayerTurn() {
        return playerTurn;
    }
}
