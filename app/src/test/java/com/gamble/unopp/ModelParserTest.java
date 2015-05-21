package com.gamble.unopp.tests;

import com.gamble.unopp.connection.response.GetGameResponse;
import com.gamble.unopp.connection.response.Response;
import com.gamble.unopp.model.cards.Card;
import com.gamble.unopp.model.game.DeckGenerator;
import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.parsing.ModelParser;

import junit.framework.Assert;

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by Verena on 18.05.2015.
 */
public class ModelParserTest {
    private ModelParser modelParser = new ModelParser();
    private Response response = new GetGameResponse();

    public void parsePlayer() throws Exception {
        String xml = "<CreatePlayerResult>\n" +
                "        <id>2</id>\n" +
                "        <name>Verena</name>\n" +
                "        <uno>false</uno>\n" +
                "        <Location>\n" +
                "          <latitude>1.0</latitude>\n" +
                "          <longitude>1.4</longitude>\n" +
                "        </Location>\n" +
                "        <Cards>\n" +
                "          <Card>\n" +
                "            <id>5</id>\n" +
                "          </Card>\n" +
                "          <Card>\n" +
                "            <id>6</id>\n" +
                "          </Card>\n" +
                "        </Cards>\n" +
                "     </CreatePlayerResult>";


        Document dom = response.getDomElement(xml);
        Player player = modelParser.parseModelFromElement(dom.getDocumentElement(), Player.class);

        Assert.assertNotNull(player);
        Assert.assertEquals(player.getID(), 2);
        Assert.assertEquals(player.getName(), "Verena");
        Assert.assertEquals(player.isUno(), false);
        Assert.assertEquals(player.getHandCount(), 2);
        // parse PlayerPlayer host = new Player().set
        // Assert.assertEquals(gameSession.getHost(), );
        ArrayList<Card> deck = DeckGenerator.createDeck(0);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(deck.get(5));
        cards.add(deck.get(6));
        Assert.assertEquals(player.getHand(), cards);
    }

    public void parseGameSession() throws Exception {
        String xml = "<GameSession>\n" +
            "          <id>1</id>\n" +
            "          <maxPlayers>6</maxPlayers>\n" +
            "          <started>false</started>\n" +
            "          <name>Spiel1</name>\n" +
            "          <hostID>4</hostID>\n" +
            "          <GameRounds>\n" +
            "            <GameRound xsi:nil=\"true\" />\n" +
            "          </GameRounds>\n" +
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
    }
}
