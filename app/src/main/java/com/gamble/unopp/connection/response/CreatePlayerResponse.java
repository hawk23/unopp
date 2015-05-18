package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.parsing.ModelParser;

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

        Document    dom                 = this.getDomElement(xmlResponse);

        Node        playerNode          = dom.getElementsByTagName("CreatePlayerResult").item(0);
        Element     playerElement       = (Element) playerNode;

        this.player                     = ModelParser.<Player>parseModelFromElement(playerElement, Player.class);
    }
}
