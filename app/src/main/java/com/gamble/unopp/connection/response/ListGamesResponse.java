package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Vector;

/**
 * Created by Mario on 04.05.2015.
 */
public class ListGamesResponse extends Response {

    private Vector<GameSession> gameSessions;

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        this.gameSessions               = new Vector<>();

        Document    dom                 = this.getDomElement(xmlResponse);
        NodeList    gameSessionsList    = dom.getElementsByTagName("GameSession");

        for (int i = 0; i < gameSessionsList.getLength(); i++) {

            Element gameSessionElement          = (Element) gameSessionsList.item(i);
            GameSession gameSession             = ModelParser.<GameSession>parseModelFromElement(gameSessionElement, GameSession.class);

            this.gameSessions.add(gameSession);
        }
    }

    public Vector<GameSession> getGameSessions() {
        return gameSessions;
    }
}
