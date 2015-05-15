package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by Verena on 05.05.2015.
 */
public class LeaveGameResponse extends Response {

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        Document dom = this.getDomElement(xmlResponse);
        Node result = dom.getElementsByTagName("LeaveGameResult").item(0);

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement((Element) result, ResponseResult.class));
    }
}
