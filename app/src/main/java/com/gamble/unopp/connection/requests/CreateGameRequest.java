package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.CreateGameResponse;

/**
 * Created by Mario on 03.05.2015.
 */
public class CreateGameRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/CreateGame";
    private static final String SOAP_METHOD         = "CreateGame";

    private String gameName;
    private int playerID;
    private double latitude;
    private double longitude;
    private int maxPlayers;

    public CreateGameRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new CreateGameResponse();
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
        this.requestParameters.put("gameName", gameName);
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
        this.requestParameters.put("hostID", playerID);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.requestParameters.put("latitude", Double.toString(latitude));
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.requestParameters.put("longitude", Double.toString(longitude));
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.requestParameters.put("maxPlayers", Integer.toString(maxPlayers));
    }
}
