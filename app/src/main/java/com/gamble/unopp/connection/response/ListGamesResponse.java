package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.GameSession;
import com.gamble.unopp.model.Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

/**
 * Created by Mario on 04.05.2015.
 */
public class ListGamesResponse extends Response {

    private Vector<GameSession> gameSessions;

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        // HACK hardcoded response
        xmlResponse = "<ListGamesResponse xmlns=\"http://tempuri.org/\">\n" +
                "  <ListGamesResult>\n" +
                "    <GameSession>\n" +
                "        <id>1<id>\n" +
                "        <maxPlayers>6</maxPlayer>\n" +
                "        <started>false</started>\n" +
                "        <name>Mario's Spiel<name>\n" +
                "        <host>1<host>\n" +
                "        <Players>\n" +
                "            <Player>\n" +
                "                <name>Mario</name>\n" +
                "                <id>1</id>\n" +
                "            </Player>\n" +
                "            <Player>\n" +
                "                <name>Albert</name>\n" +
                "                <id>2</id>\n" +
                "            </Player>\n" +
                "            <Player>\n" +
                "                <name>Verena</name>\n" +
                "                <id>3</id>\n" +
                "            </Player>\n" +
                "        </Players>\n" +
                "    </GameSession>\n" +
                "    <GameSession>\n" +
                "        <id>2<id>\n" +
                "        <maxPlayers>6</maxPlayer>\n" +
                "        <started>false</started>\n" +
                "        <name>Peter's Spiel<name>\n" +
                "        <host>4<host>\n" +
                "        <Players>\n" +
                "            <Player>\n" +
                "                <name>Peter</name>\n" +
                "                <id>4</id>\n" +
                "            </Player>\n" +
                "        </Players>\n" +
                "    </GameSession>\n" +
                "  </ListGamesResult>\n" +
                "</ListGamesResponse>";
        // END HACK

        this.gameSessions               = new Vector<GameSession>();

        Document    dom                 = this.getDomElement(xmlResponse);
        NodeList    gameSessions        = dom.getElementsByTagName("GameSession");

        for (int i = 0; i < gameSessions.getLength(); i++) {

            Element gameSessionElement          = (Element) gameSessions.item(i);
            NodeList players                    = gameSessionElement.getElementsByTagName("Players");

            for (int j = 0; j < players.getLength(); j++) {

                Element playerElement           = (Element) players.item(j);

                // TODO parse!
            }
        }

        /*
        Element     playerElement       = (Element) playerNode;

        int         id                  = Integer.parseInt(playerElement.getElementsByTagName("id").item(0).getTextContent());
        String      name                = playerElement.getElementsByTagName("name").item(0).getTextContent();

        this.player                     = new Player(id, name);
        */
    }

    public Vector<GameSession> getGameSessions() {
        return gameSessions;
    }

    public void setGameSessions(Vector<GameSession> gameSessions) {
        this.gameSessions = gameSessions;
    }
}
