package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.GetGameResponse;

/**
 * Created by Mario on 13.05.2015.
 */
public class GetGameRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/GetGame";
    private static final String SOAP_METHOD         = "GetGame";

    private int gameID;

    public GetGameRequest() {

        super();

        this.soapAction                             = SOAP_ACTION;
        this.soapMethod                             = SOAP_METHOD;
        this.response                               = new GetGameResponse();
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
        this.requestParameters.put("gameID", gameID);
    }
}
