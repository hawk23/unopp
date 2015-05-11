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

        Document dom = this.getDomElement(xmlResponse);

        Node gameSessionNode = dom.getElementsByTagName("GameSession").item(0);
        Element gameSessionElement = (Element) gameSessionNode;

        this.gameSession = ModelParser.<GameSession>parseModelFromElement(gameSessionElement, GameSession.class);

        this.gameSession.setHost(UnoDatabase.getInstance().getLocalPlayer()); //TODO change to host id from response when it works
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }
}
