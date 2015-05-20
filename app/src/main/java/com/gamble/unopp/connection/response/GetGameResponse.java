package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.game.GameSession;
import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Mario on 13.05.2015.
 */
public class GetGameResponse extends Response {

    private ResponseResult responseResult;
    private GameSession gameSession;

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        Document    dom                            = this.getDomElement(xmlResponse);
        Node        resultNode                     = dom.getElementsByTagName("Result").item(0);
        Node        gameSessionNode                = dom.getElementsByTagName("GameSession").item(0);

        this.responseResult = ModelParser.<ResponseResult>parseModelFromElement((Element) resultNode, ResponseResult.class);

        if (responseResult.isStatus() && gameSessionNode != null) {
            this.gameSession = ModelParser.<GameSession>parseModelFromElement((Element) gameSessionNode, GameSession.class);
        }
    }

    @Override
    public ResponseResult getResponseResult() {
            return responseResult;
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}
