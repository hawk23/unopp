package com.gamble.unopp;

import com.gamble.unopp.connection.response.GetGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.connection.response.ResponseResult;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.Deck;
import com.gamble.unopp.model.game.DeckGenerator;
import com.gamble.unopp.model.game.GameRound;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.GameState;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.parsing.ModelParser;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by Verena on 18.05.2015.
 */
public class ModelParserTest {

    private ModelParser modelParser = new ModelParser();
    private Response response = new GetGameResponse();

    @Test
    public void parseResponseResult() {
        String xml ="<Result>\n" +
                "       <status>true</status>\n" +
                "       <message>Spiel mit ID: 0 erfolgreich entfernt!</message>\n" +
                "   </Result>";


        Document dom = response.getDomElement(xml);
        ResponseResult responseResult = modelParser.parseResponseResultFromElement(dom.getDocumentElement());

        Assert.assertNotNull(responseResult);
        Assert.assertTrue(responseResult.isStatus());
        Assert.assertEquals(responseResult.getMessage(), "Spiel mit ID: 0 erfolgreich entfernt!");
    }


    @Test
    public void parsePlayer() throws Exception {
        String xml = "<CreatePlayerResult>\n" +
                "        <id>2</id>\n" +
                "        <name>Verena</name>\n" +
                "        <uno>false</uno>\n" +
                "        <Location>\n" +
                "          <latitude>1.0</latitude>\n" +
                "          <longitude>1.4</longitude>\n" +
                "        </Location>\n" +
                "        <Cards/>\n" +
                "     </CreatePlayerResult>";


        Document dom = response.getDomElement(xml);
        Player player = modelParser.parseModelFromElement(dom.getDocumentElement(), Player.class);

        Assert.assertNotNull(player);
        Assert.assertEquals(player.getID(), 2);
        Assert.assertEquals(player.getName(), "Verena");
        Assert.assertEquals(player.isUno(), false);
        Assert.assertEquals(player.getHandCount(), 0);
    }


    @Test
    public void parseGameSession() throws Exception {
        String xml = "<GameSession>\n" +
            "          <id>1</id>\n" +
            "          <maxPlayers>6</maxPlayers>\n" +
            "          <started>false</started>\n" +
            "          <name>Spiel1</name>\n" +
            "          <hostID>4</hostID>\n" +
            "          <mode>2</mode>\n" +
            "          <GameRounds/>\n" +
            "          <Location>\n" +
            "            <latitude>1.0</latitude>\n" +
            "            <longitude>4.0</longitude>\n" +
            "          </Location>\n" +
            "          <Players>\n" +
            "            <Player xsi:nil=\"true\" />\n" +
            "            <Player xsi:nil=\"true\" />\n" +
            "          </Players>\n" +
            "        </GameSession>";


        Document dom = response.getDomElement(xml);
        GameSession gameSession = modelParser.parseModelFromElement(dom.getDocumentElement(), GameSession.class);

        Assert.assertNotNull(gameSession);
        Assert.assertEquals(gameSession.getID(), 1);
        Assert.assertEquals(gameSession.getMaxPlayers(), 6);
        Assert.assertEquals(gameSession.getCurrentPlayerCount(), 0);
        Assert.assertNotNull(gameSession.getPlayers());
        Assert.assertTrue(gameSession.getPlayers().isEmpty());
        Assert.assertNull(gameSession.getActualGameRound());
        Assert.assertEquals(gameSession.isStarted(), false);
        Assert.assertEquals(gameSession.getName(), "Spiel1");
        Assert.assertEquals(gameSession.getMode(), 2);
    }

    @Test
    public void parseGameRound() {
        // TODO
    }

    @Test
    public void parseDeck() {
        // TODO
    }

    @Test
    public void parseGameState() {
        // TODO
    }
}
