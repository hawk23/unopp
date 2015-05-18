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

        Document    dom                     = this.getDomElement(xmlResponse);

        /* TODO: server does not send result
        Node        resultNode              = dom.getElementsByTagName("Result").item(0);
        Element     resultElement           = (Element) resultNode;

        this.responseResult                 = ModelParser.<ResponseResult>parseModelFromElement(resultElement, ResponseResult.class);
        */

        Node        gameSessionNode                = dom.getElementsByTagName("GameSession").item(0);
        Node        resultNode                     = dom.getElementsByTagName("Result").item(0);

        if (gameSessionNode != null) {
            Element gameSessionElement = (Element) gameSessionNode;

            this.gameSession = ModelParser.<GameSession>parseModelFromElement(gameSessionElement, GameSession.class);
        } else if (resultNode != null) {
            this.responseResult = ModelParser.<ResponseResult>parseModelFromElement((Element) resultNode, ResponseResult.class);
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
