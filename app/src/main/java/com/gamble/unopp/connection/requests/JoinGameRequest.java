package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.JoinGameResponse;
import com.gamble.unopp.connection.response.LeaveGameResponse;

/**
 * Created by Verena on 05.05.2015.
 */
public class JoinGameRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/JoinGame";
    private static final String SOAP_METHOD         = "JoinGame";

    private int gameId;
    private int playerId;

    public JoinGameRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new JoinGameResponse();
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
        this.requestParameters.put("gameId", String.valueOf(gameId));
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
        this.requestParameters.put("playerId", String.valueOf(playerId));
    }
}
