package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.StartGameResponse;

/**
 * Created by Verena on 15.05.2015.
 */
public class StartGameRequest extends Request {
    private static final String SOAP_ACTION         = "http://tempuri.org/StartGame";
    private static final String SOAP_METHOD         = "StartGame";

    private int gameId;
    private int hostId;

    public StartGameRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new StartGameResponse();
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
        this.requestParameters.put("gameID", String.valueOf(gameId));
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
        this.requestParameters.put("hostID", String.valueOf(hostId));
    }
}
