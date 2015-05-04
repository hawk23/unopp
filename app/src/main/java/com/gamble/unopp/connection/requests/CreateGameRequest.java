package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.CreateGameResponse;

/**
 * Created by Mario on 03.05.2015.
 */
public class CreateGameRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/CreateGame";
    private static final String SOAP_METHOD         = "CreateGame";

    private String gameName;
    private String creatorName;
    private double latitude;
    private double longitude;

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

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        this.requestParameters.put("creatorName", creatorName);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.requestParameters.put("x", Double.toString(latitude));
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.requestParameters.put("y", Double.toString(longitude));
    }
}
