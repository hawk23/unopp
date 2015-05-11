package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.management.UnoDatabase;
import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by Mario on 04.05.2015.
 */
public class CreateGameResponse extends Response {

    private GameSession gameSession;

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        // HACK hardcoded response
        xmlResponse = "<CreateGameResponse xmlns=\"http://tempuri.org/\">\n" +
                "    <CreateGameResult>\n" +
                "        <GameSession>\n" +
                "            <id>3</id>\n" +
                "            <maxPlayers>6</maxPlayers>\n" +
                "            <started>false</started>\n" +
                "            <name>New Game</name>\n" +
                "            <host>1</host>\n" +
                "        </GameSession>\n" +
                "    </CreateGameResult>\n" +
                "</CreateGameResponse>";
        // END HACK

        Document dom = this.getDomElement(xmlResponse);

        Node gameSessionNode = dom.getElementsByTagName("GameSession").item(0);
        Element gameSessionElement = (Element) gameSessionNode;

        this.gameSession = ModelParser.<GameSession>parseModelFromElement(gameSessionElement, GameSession.class);

        this.gameSession.addPlayer(UnoDatabase.getInstance().getLocalPlayer());
        this.gameSession.setHost(UnoDatabase.getInstance().getLocalPlayer());
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }
}
