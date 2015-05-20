package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.game.GameUpdate;
import com.gamble.unopp.model.game.Player;
import com.gamble.unopp.model.game.Turn;
import com.gamble.unopp.model.management.UnoDatabase;
import com.gamble.unopp.model.parsing.ModelParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Mario on 19.05.2015.
 */
public class GetUpdateResponse extends Response {

    private ArrayList<GameUpdate> gameUpdates;

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        Document dom                        = this.getDomElement(xmlResponse);

        Node getUpdateResult                = dom.getElementsByTagName("GetUpdateResult").item(0);
        Element getUpdateResultElement      = (Element) getUpdateResult;

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement(getUpdateResultElement, ResponseResult.class));

        if (this.getResponseResult().isStatus()) {

            NodeList updates    = dom.getElementsByTagName("Update");
            this.gameUpdates    = new ArrayList<GameUpdate>();

            for (int i = 0; i < updates.getLength(); i++) {

                Element update = (Element) updates.item(i);
                Element updateData = (Element) update.getElementsByTagName("update").item(0);

                String updateString = updateData.getTextContent();

                GameUpdate gameUpdate = GameUpdate.deserializeUpdate(updateString);

                // set references to local objects based on given id's
                if (gameUpdate instanceof Turn) {

                    Turn turn = (Turn) gameUpdate;

                    // set correct card
                    if (turn.getCard() != null) {
                        turn.setCard(UnoDatabase.getInstance().getDeck().get(turn.getCard().getID()));
                    }

                    // set correct player
                    if (turn.getPlayer() != null) {
                        for (Player player : UnoDatabase.getInstance().getCurrentGameSession().getPlayers()) {
                            if (player.getID() == turn.getPlayer().getID()) {
                                turn.setPlayer(player);
                                break;
                            }
                        }
                    }
                }

                gameUpdates.add(gameUpdate);
            }
        }
    }

    public ArrayList<GameUpdate> getGameUpdates() {
        return gameUpdates;
    }
}
