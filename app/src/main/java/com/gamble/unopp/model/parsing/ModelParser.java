package com.gamble.unopp.model.parsing;

import com.gamble.unopp.connection.response.ResponseResult;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.cards.UnoColor;
import com.gamble.unopp.model.game.Deck;
import com.gamble.unopp.model.game.DeckGenerator;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.game.UnoDirection;
import com.gamble.unopp.model.management.UnoDatabase;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Mario on 04.05.2015.
 */
public class ModelParser {

    public static <T> T parseModelFromElement (Element element, Class<T> type) {

        if (type.equals(Player.class)) {
            return (T) parsePlayerFromElement(element);
        }
        else if (type.equals(GameSession.class)) {
            return (T) parseGameSessionFromElement(element);
        }
        else if (type.equals(ResponseResult.class)) {
            return (T) parseResponseResultFromElement(element);
        }

        return null;
    }

    private static ResponseResult parseResponseResultFromElement(Element element) {

        try {
            boolean status = Boolean.parseBoolean(element.getElementsByTagName("status").item(0).getTextContent());
            String message = element.getElementsByTagName("message").item(0).getTextContent();

            return new ResponseResult(status, message);
        }
        catch (Exception ex) {
            return new ResponseResult(false, "Malformed Response");
        }
    }

    private static Player parsePlayerFromElement (Element element) {
        return parsePlayerFromElement(element, null);
    }

    private static Player parsePlayerFromElement (Element element, GameSession gameSession) {

        if (element.getElementsByTagName("id") != null && element.getElementsByTagName("id").item(0) != null ) {

            int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
            String name = element.getElementsByTagName("name").item(0).getTextContent();

            Player player = new Player(id, name);
            player.setGameSession(gameSession);

            /**
             * Parse Cards
             */

            Element             handCardsElement        = (Element) element.getElementsByTagName("Cards").item(0);
            NodeList            handCardsList           = handCardsElement.getElementsByTagName("Card");
            ArrayList<Card>     cards                   = UnoDatabase.getInstance().getDeck();
            ArrayList<Card>     hand                    = new ArrayList<Card>();

            for (int i = 0; i < handCardsList.getLength(); i++) {

                Element serverCard              = (Element) handCardsList.item(i);
                int     cardId                  = Integer.parseInt(serverCard.getElementsByTagName("id").item(0).getTextContent());

                Card    card                    = cards.get(cardId);
                hand.add(card);
            }

            player.addCardsToHand(hand);

            return player;
        }
        return null;
    }

    private static GameRound parseGameRoundFromElement (Element element, GameSession gameSession) {

        int         id                  = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
        boolean     finished            = Boolean.parseBoolean(element.getElementsByTagName("finished").item(0).getTextContent());

        GameRound   gameRound           = new GameRound (id, gameSession);
        gameRound.setFinished(finished);

        Element     gameStateElement    = (Element) element.getElementsByTagName("GameState").item(0);
        GameState   gameState           = parseGameStateFromElement(gameStateElement, gameRound, gameSession.getPlayers());

        gameRound.setGamestate(gameState);

        return gameRound;
    }

    private static GameState parseGameStateFromElement (Element element, GameRound gameRound, ArrayList<Player> players) {

        int         topCard             = Integer.parseInt(element.getElementsByTagName("topCard").item(0).getTextContent());
        int         drawCounter         = Integer.parseInt(element.getElementsByTagName("drawCounter").item(0).getTextContent());
        int         currentPlayerId     = Integer.parseInt(element.getElementsByTagName("currentPlayer").item(0).getTextContent());
        int         nextPlayerId        = Integer.parseInt(element.getElementsByTagName("nextPlayer").item(0).getTextContent());
        int         directionId         = Integer.parseInt(element.getElementsByTagName("direction").item(0).getTextContent());
        int         currentColorId      = Integer.parseInt(element.getElementsByTagName("currentColor").item(0).getTextContent());

        GameState   gameState           = new GameState(gameRound);
        gameState.setActualColor        (UnoColor.createById(currentColorId));
        gameState.setDirection          (UnoDirection.createById(directionId));
        gameState.setDrawCounter        (drawCounter);

        /**
         * Set deck
         */

        Element     deckElement         = (Element) element.getElementsByTagName("Deck").item(0);
        Deck        deck                = parseDeckFromElement(deckElement);

        gameState.setPlayedStack(deck.getPlayedStack());
        gameState.setStack(deck.getStack());

        /**
         * Set actual player
         */

        for (Player player : players) {

            if (player.getID() == currentPlayerId) {
                gameState.setActualPlayer(player);
                break;
            }
        }

        return gameState;
    }

    private static Deck parseDeckFromElement (Element element) {

        Deck                resultDeck              = new Deck();

        ArrayList<Card>     cards                   = UnoDatabase.getInstance().getDeck();
        Element             drawStackElement        = (Element) element.getElementsByTagName("drawStack").item(0);
        NodeList            serverCardsStack        = drawStackElement.getElementsByTagName("Card");
        Element             playedStackElement      = (Element) element.getElementsByTagName("playedStack").item(0);
        NodeList            serverCardsPlayedStack  = playedStackElement.getElementsByTagName("Card");

        for (int i = 0; i < serverCardsStack.getLength(); i++) {

            Element serverCard              = (Element) serverCardsStack.item(i);
            int     cardId                  = Integer.parseInt(serverCard.getElementsByTagName("id").item(0).getTextContent());

            Card    card                    = cards.get(cardId);
            resultDeck.getStack().add(card);
        }

        for (int j = 0; j < serverCardsPlayedStack.getLength(); j++) {

            Element serverCard              = (Element) serverCardsPlayedStack.item(j);
            int     cardId                  = Integer.parseInt(serverCard.getElementsByTagName("id").item(0).getTextContent());

            Card    card                    = cards.get(cardId);
            resultDeck.getPlayedStack().add(card);
        }

        return resultDeck;
    }

    private static GameSession parseGameSessionFromElement (Element element) {

        int         id                  = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
        String      name                = element.getElementsByTagName("name").item(0).getTextContent();
        int         hostID              = Integer.parseInt(element.getElementsByTagName("hostID").item(0).getTextContent());
        int         maxPlayers          = Integer.parseInt(element.getElementsByTagName("maxPlayers").item(0).getTextContent());
        int         mode                = Integer.parseInt(element.getElementsByTagName("mode").item(0).getTextContent());
        boolean     started             = Boolean.parseBoolean(element.getElementsByTagName("started").item(0).getTextContent());

        GameSession gameSession         = new GameSession(id, name);
        gameSession.setMaxPlayers(maxPlayers);
        gameSession.setMode(mode);
        gameSession.setStarted(started);

        /**
         * Parse players
         */
        NodeList players                = element.getElementsByTagName("Player");

        if (players != null) {
            for (int j = 0; j < players.getLength(); j++) {

                Element playerElement           = (Element) players.item(j);
                Player player                   = parsePlayerFromElement(playerElement, gameSession);

                if (player != null) {
                    gameSession.addPlayer(player);

                    if (hostID == player.getID()) {
                        gameSession.setHost(player);
                    }
                }
            }
        }

        /**
         * Parse GameRounds
         */

        NodeList gameRounds             = element.getElementsByTagName("GameRound");

        for (int i = 0; i < gameRounds.getLength(); i++) {

            GameRound   gameRound       = parseGameRoundFromElement((Element) gameRounds.item(i), gameSession);
            gameSession.getGameRounds().add(gameRound);

            if (!gameRound.isFinished()) {
                gameSession.setActualGameRound(gameRound);
            }
        }

        return gameSession;
    }
}