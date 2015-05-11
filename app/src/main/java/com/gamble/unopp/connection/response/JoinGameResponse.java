package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by Verena on 05.05.2015.
 */
public class JoinGameResponse extends Response {

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        Document dom = this.getDomElement(xmlResponse);
        NodeList result = dom.getElementsByTagName("Result");

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement((Element) result.item(0), ResponseResult.class));
    }
}
