package com.gamble.unopp;

import com.gamble.unopp.logic.GameLogic;
import com.gamble.unopp.model.cards.Action;
import com.gamble.unopp.model.cards.ActionCard;
import com.gamble.unopp.model.cards.ActionType;
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
    private ArrayList<Card> playedStack;
    private ArrayList<Card> stack;
    private ArrayList<Card> hand;
    private ActionType actionType1;
    private ActionType actionType2;

    @Before
    public void setUp() {
        gameSession = new GameSession(1, "Test", player);
        gameRound = new GameRound(1, gameSession);
        gameState = new GameState(gameRound);
        gameLogic = new GameLogic(gameState);
        player = new Player(1, "Test");
        playedStack = new ArrayList();
        stack = new ArrayList();
        hand = new ArrayList();
        actionType1 = new ActionType(0);
        actionType2 = new ActionType(0);
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
        playedStack = null;
        stack = null;
        hand = null;
        actionType1 = null;
        actionType2 = null;
    }

    @Test
    public void testCheckTurnDraw() {
        player.setHasToChooseColor(false);
        turn = new Turn(1, Turn.TurnType.DRAW, player);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnPlayCorrectCard() {
        playedStack.add(new NumberCard(1, 2, null, UnoColor.BLUE));
        gameState.setPlayedStack(playedStack);

        card = new NumberCard(1, 2, null, UnoColor.RED);

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnPlayWrongCard() {
        playedStack.add(new NumberCard(1, 2, null, UnoColor.BLUE));
        gameState.setPlayedStack(playedStack);

        card = new NumberCard(1, 5, null, UnoColor.RED);

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        assertFalse(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnChooseColor() {
        player.setHasToChooseColor(true);

        turn = new Turn(1, Turn.TurnType.CHOOSE_COLOR, player);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testCheckTurnCallUno() {
        hand.add(new NumberCard(1, 3, null, UnoColor.GREEN));
        player.setHand(hand);

        turn = new Turn(1, Turn.TurnType.CALL_UNO, player);

        assertTrue(gameLogic.checkTurn(turn));
    }

    @Test
    public void testDoTurnDraw() {
        turn = new Turn(1, Turn.TurnType.DRAW, player);

        gameSession.addPlayer(player);
        gameState.setDrawCounter(0);

        stack.add(new NumberCard(1, 3, null, UnoColor.GREEN));
        gameState.setStack(stack);
        gameLogic.doTurn(turn);

        assertEquals(player.getHandCount(), 1);
    }

    @Test
    public void testDoTurnChooseColor() {
        turn = new Turn(1, Turn.TurnType.CHOOSE_COLOR, player);
        turn.setColor(UnoColor.RED);

        gameSession.addPlayer(player);
        gameLogic.doTurn(turn);

        assertEquals(gameState.getActualColor(), UnoColor.RED);
        assertFalse(turn.getPlayer().hasToChooseColor());
    }

    @Test
    public void testDoTurnCallUno() {
        turn = new Turn(1, Turn.TurnType.CALL_UNO, player);

        gameLogic.doTurn(turn);

        assertEquals(turn.getPlayer().isUno(), true);
    }

    @Test
    public void testDoTurnPlayNumberCard() {
        Card card = new NumberCard(1, 5, null, UnoColor.RED);

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        gameSession.addPlayer(player);

        gameLogic.doTurn(turn);

        assertEquals(gameState.sizeOfPlayedStack(), 1);
        assertEquals(gameState.getActualColor(), UnoColor.RED);
    }

    @Test
    public void testDoTurnPlayActionCardTwoActions() {
        ActionCard card = new ActionCard(1, null, UnoColor.RED);

        card.addAction(new Action(actionType1));
        card.addAction(new Action(actionType2));

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        gameLogic.doTurn(turn);

        assertEquals(gameState.sizeOfPlayedStack(), 1);
        assertEquals(gameState.getDrawCounter(), 4);
        assertTrue(turn.getPlayer().hasToChooseColor());
    }

    @Test
    public void testDoTurnPlayActionCardAdd2() {
        ActionCard card = new ActionCard(1, null, UnoColor.RED);

        card.addAction(new Action(actionType1));

        turn = new Turn(1, Turn.TurnType.PLAY_CARD, player);
        turn.setCard(card);

        gameSession.addPlayer(player);
        gameLogic.doTurn(turn);

        assertEquals(gameState.sizeOfPlayedStack(), 1);
        assertEquals(gameState.getDrawCounter(), 2);
        assertEquals(gameState.getActualColor(), UnoColor.RED);
    }

    /*
    @Test
    public void testCheckCardNumberCard() {
        Card playedStackCard = new NumberCard(1, 5, null, UnoColor.RED);
        Card card = new NumberCard(1, 3, null, UnoColor.RED);

        playedStack.add(playedStackCard);

        gameState.setPlayedStack(playedStack);
        gameState.setActualColor(UnoColor.RED);

        assertTrue(gameLogic.checkCard(card));
    }

    @Test
    public void testCheckCardActionCard() {
        ActionCard playedStackCard = new ActionCard(1, null, UnoColor.RED);

        playedStackCard.addAction(new Action(actionType1));

        Card card = new NumberCard(1, 3, null, UnoColor.RED);

        playedStack.add(playedStackCard);

        gameState.setPlayedStack(playedStack);
        gameState.setActualColor(UnoColor.RED);

        assertTrue(gameLogic.checkCard(card));
    }
    */
}