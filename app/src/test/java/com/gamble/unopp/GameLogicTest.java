package com.gamble.unopp;

import com.gamble.unopp.logic.GameLogic;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.NumberCard;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.game.Turn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.*;

/**
 * Created by Roland on 19.05.2015.
 */
public class GameLogicTest {
    private GameSession gameSession;
    private GameRound gameRound;
    private GameState gameState;
    private GameLogic gameLogic;
    private Player player;
    private NumberCard card;
    private Turn turn;

    @Before
    public void setUp() {
        gameSession = new GameSession(1, "Test", player);
        gameRound = new GameRound(1, gameSession);
        gameState = new GameState(gameRound);
        gameLogic = new GameLogic(gameState);
    }

    @After
    public void tearDown() {
        gameSession = null;
        gameRound = null;
        gameState = null;
        gameLogic = null;
        player = null;
        card = null;
        turn = null;
    }

    @Test
    public void testCheckTurnDraw() {
        player = new Player(1, "Test");
        player.setHasToChooseColor(false);

        turn = new Turn(1, Turn.TurnType.DRAW, player);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnPlayCorrectCard() {
        ArrayList<Card> playedStack = new ArrayList();
        playedStack.add(new NumberCard(1, 2, null, UnoColor.BLUE));

        gameState.setPlayedStack(playedStack);

        player = new Player(1, "Test");
        card = new NumberCard(1, 2, null, UnoColor.RED);

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnPlayWrongCard() {
        ArrayList<Card> playedStack = new ArrayList();
        playedStack.add(new NumberCard(1, 2, null, UnoColor.BLUE));

        gameState.setPlayedStack(playedStack);

        player = new Player(1, "Test");
        card = new NumberCard(1, 5, null, UnoColor.RED);

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        assertFalse(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnChooseColor() {
        player = new Player(1, "Test");
        player.setHasToChooseColor(true);

        turn = new Turn(1, Turn.TurnType.CHOOSE_COLOR, player);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnCallUno() {
        ArrayList<Card> hand = new ArrayList();
        hand.add(new NumberCard(1, 3, null, UnoColor.GREEN));

        player = new Player(1, "Test");
        player.setHand(hand);

        turn = new Turn(1, Turn.TurnType.CALL_UNO, player);

        assertTrue(gameLogic.checkTurn(turn));
    }
}

