package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.LeaveGameResponse;

/**
 * Created by Verena on 05.05.2015.
 */
public class LeaveGameRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/LeaveGame";
    private static final String SOAP_METHOD         = "LeaveGame";

    private int gameId;
    private int playerId;

    public LeaveGameRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new LeaveGameResponse();
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
        this.requestParameters.put("gameID", String.valueOf(gameId));
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
        this.requestParameters.put("playerID", String.valueOf(playerId));
    }
}
