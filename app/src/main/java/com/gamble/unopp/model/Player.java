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

    public void setID(int ID) {
        this.ID = ID;
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

    public boolean joinGameSession(GameSession gameSession) {
        return gameSession.addPlayer(this);
    }

    public boolean leaveGameSession() {
        return this.gameSession != null && this.gameSession.removePlayer(this);
    }
}
