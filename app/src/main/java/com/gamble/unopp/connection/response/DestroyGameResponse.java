package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by PeterLuca on 14.05.2015.
 */
public class DestroyGameResponse extends Response
{
    @Override
    public void parseXML(String xmlResponse) throws Exception {

        Document dom = this.getDomElement(xmlResponse);
        Node result = dom.getElementsByTagName("DestroyGameResult").item(0);

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement((Element) result, ResponseResult.class));
    }
}