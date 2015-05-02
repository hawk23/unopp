package com.gamble.unopp.model;

/**
 * Created by Albert on 02.05.2015.
 */
public class Player {

    private int ID;
    private String name;
    private GameSession gameSession;

    public Player(int ID, String name) {
        this.ID = ID;
        this.name = name;
        this.gameSession = null;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }
}
