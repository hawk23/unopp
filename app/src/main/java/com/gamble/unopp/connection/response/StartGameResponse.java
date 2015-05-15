package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by Verena on 15.05.2015.
 */
public class StartGameResponse extends Response {

    @Override
    public void parseXML(String xmlResponse) throws Exception {
        Document dom = this.getDomElement(xmlResponse);

        Node startGameResult = dom.getElementsByTagName("StartGameResult").item(0);
        Element startGameResultElement = (Element) startGameResult;

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement(startGameResultElement, ResponseResult.class));
    }
}
