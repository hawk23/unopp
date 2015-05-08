package com.gamble.unopp.model.game;

import com.gamble.unopp.model.GameSession;

/**
 * Created by Albert on 05.05.2015.
 */
public class GameRound {

    private int ID;
    private GameSession gameSession;
    private GameState gamestate;

    public GameRound (int ID, GameSession gameSession, int[] shuffledCardIDs) {
        this.ID = ID;
        this.gameSession = gameSession;
        this.gamestate = new GameState(this, shuffledCardIDs);
    }
}
