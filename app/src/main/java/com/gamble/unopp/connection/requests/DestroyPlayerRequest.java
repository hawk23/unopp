package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.DestroyPlayerResponse;

/**
 * Created by PeterLuca on 14.05.2015.
 */
public class DestroyPlayerRequest extends Request
{
    private static final String SOAP_ACTION         = "http://tempuri.org/DestroyPlayer";
    private static final String SOAP_METHOD         = "DestroyPlayer";

    private int gameId;
    private int playerId;

    public DestroyPlayerRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new DestroyPlayerResponse();
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
