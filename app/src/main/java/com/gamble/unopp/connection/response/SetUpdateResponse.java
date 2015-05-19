package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by Mario on 19.05.2015.
 */
public class SetUpdateResponse extends Response {

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        Document        dom                         = this.getDomElement(xmlResponse);

        Node            setUpdateResult             = dom.getElementsByTagName("SetUpdateResult").item(0);
        Element         setUpdateResultElement      = (Element) setUpdateResult;

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement(setUpdateResultElement, ResponseResult.class));
    }
}