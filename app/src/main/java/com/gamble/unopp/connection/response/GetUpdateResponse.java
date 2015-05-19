package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.game.GameUpdate;
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

            NodeList updates = dom.getElementsByTagName("Update");

            for (int i = 0; i < updates.getLength(); i++) {

                Element update = (Element) updates.item(i);
                Element updateData = (Element) update.getElementsByTagName("update").item(0);

                String updateString = updateData.getTextContent();

                GameUpdate gameUpdate = GameUpdate.deserializeUpdate(updateString);
                gameUpdates.add(gameUpdate);
            }
        }
    }

    public ArrayList<GameUpdate> getGameUpdates() {
        return gameUpdates;
    }
}
