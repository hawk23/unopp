package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.GetUpdateResponse;

/**
 * Created by Mario on 19.05.2015.
 */
public class GetUpdateRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/GetUpdate";
    private static final String SOAP_METHOD         = "GetUpdate";

    private int gameID;
    private int lastKnownUpdateID;

    public GetUpdateRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new GetUpdateResponse();
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
        this.requestParameters.put("gameID", gameID);
    }

    public void setLastKnownUpdateID(int lastKnownUpdateID) {
        this.lastKnownUpdateID = lastKnownUpdateID;
        this.requestParameters.put("lastKnownUpdateID", lastKnownUpdateID);
    }
}
