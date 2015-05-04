package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.Player;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Created by Mario on 04.05.2015.
 */
public class CreatePlayerResponse extends Response {

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void parseXML (String xmlResponse) throws Exception {

        // HACK hardcoded response
        xmlResponse = "<CreatePlayerResponse xmlns=\"http://tempuri.org/\">\n" +
                "    <CreatePlayerResult>\n" +
                "        <Player>\n" +
                "            <name>Mario</name>\n" +
                "            <id>1</id>\n" +
                "        </Player>\n" +
                "    </CreatePlayerResult>\n" +
                "</CreatePlayerResponse>";
        // END HACK

        Document    dom     = this.getDomElement(xmlResponse);

        Node        playerNode          = dom.getElementsByTagName("Player").item(0);
        Element     playerElement       = (Element) playerNode;

        int         id                  = Integer.parseInt(playerElement.getElementsByTagName("id").item(0).getTextContent());
        String      name                = playerElement.getElementsByTagName("name").item(0).getTextContent();

        this.player                     = new Player(id, name);
    }
}
