package com.gamble.unopp.model.parsing;

import com.gamble.unopp.model.GameSession;
import com.gamble.unopp.model.Player;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

        return null;
    }

    private static Player parsePlayerFromElement (Element element) {

        int         id                  = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
        String      name                = element.getElementsByTagName("name").item(0).getTextContent();

        return new Player(id, name);
    }

    private static GameSession parseGameSessionFromElement (Element element) {

        int         id                  = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
        String      name                = element.getElementsByTagName("name").item(0).getTextContent();
        int         hostID              = Integer.parseInt(element.getElementsByTagName("host").item(0).getTextContent());
        int         maxPlayers          = Integer.parseInt(element.getElementsByTagName("maxPlayers").item(0).getTextContent());

        GameSession gameSession         = new GameSession(id, name);
        gameSession.setMaxPlayers(maxPlayers);

        NodeList players                = element.getElementsByTagName("Players");

        if (players != null) {
            for (int j = 0; j < players.getLength(); j++) {

                Element playerElement           = (Element) players.item(j);
                Player player                   = parsePlayerFromElement(playerElement);

                gameSession.addPlayer(player);

                if (hostID == player.getID()) {
                    gameSession.setHost(player);
                }
            }
        }

        return gameSession;
    }
}
