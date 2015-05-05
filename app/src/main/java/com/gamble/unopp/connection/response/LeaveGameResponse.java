package com.gamble.unopp.connection.response;

import com.gamble.unopp.model.parsing.ModelParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Verena on 05.05.2015.
 */
public class LeaveGameResponse extends Response {

    @Override
    public void parseXML(String xmlResponse) throws Exception {

        // HACK hardcoded response
        xmlResponse = "<LeaveGameResponse xmlns=\"http://tempuri.org/\">\n" +
                "      <LeaveGameResult>\n" +
                "        <Result>\n" +
                "            <status>true</status>\n" +
                "            <message>Erfogreich verlassen.</message>\n" +
                "        </Result>\n" +
                "      </LeaveGameResult>\n" +
                "</LeaveGameResponse>";
        // END HACK
        Document dom = this.getDomElement(xmlResponse);
        Element result = dom.getElementById("Result");

        super.setResponseResult(ModelParser.<ResponseResult>parseModelFromElement(result, ResponseResult.class));
    }
}