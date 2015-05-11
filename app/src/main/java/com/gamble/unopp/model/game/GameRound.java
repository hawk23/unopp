package com.gamble.unopp.model.game;

import java.util.ArrayList;

/**
 * Created by Albert on 05.05.2015.
 */
public class GameRound {

    private int ID;
    private GameSession gameSession;
    private GameState gamestate;

    public GameRound (int ID, GameSession gameSession, int[] shuffledCardIDs, int[] playerOrder) {
        this.ID = ID;
        this.gameSession = gameSession;
        this.gamestate = new GameState(this, shuffledCardIDs, playerOrder);
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
