package com.gamble.unopp.model.game;

import java.util.ArrayList;

/**
 * Created by Albert on 05.05.2015.
 */
public class GameRound {

    private int ID;
    private GameSession gameSession;
    private GameState gamestate;
    private boolean active;

    private int localUpdateID = 0;

    public GameRound (int ID, GameSession gameSession) {
        this.ID = ID;
        this.gameSession = gameSession;
        this.gamestate = new GameState(this);
        this.active = false;

    }

    public boolean doTurn(Turn turn) {
        return this.gamestate.doTurn(turn);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLocalUpdateID() {
        return localUpdateID;
    }

    public void setLocalUpdateID(int localUpdateID) {
        this.localUpdateID = localUpdateID;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameState getGamestate() {
        return gamestate;
    }

    public void setGamestate(GameState gamestate) {
        this.gamestate = gamestate;
    }

    public ArrayList<Player> getPlayers() {
        return this.gameSession.getPlayers();
    }
}
