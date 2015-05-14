package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.DestroyGameResponse;

/**
 * Created by PeterLuca on 14.05.2015.
 */
public class DestroyGameRequest extends Request
{
    private static final String SOAP_ACTION         = "http://tempuri.org/LeaveGame";
    private static final String SOAP_METHOD         = "DestroyGame";

    private int gameId;
    private int hostId;

    public DestroyGameRequest()
    {
        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new DestroyGameResponse();
    }

    public void setGameId(int gameId)
    {
        this.gameId = gameId;
        this.requestParameters.put("gameID", String.valueOf(gameId));
    }

    public void setPlayerId(int hostId)
    {
        this.hostId = hostId;
        this.requestParameters.put("hostID", String.valueOf(hostId));
    }
}
