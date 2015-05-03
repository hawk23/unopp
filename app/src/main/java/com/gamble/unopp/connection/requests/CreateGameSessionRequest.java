package com.gamble.unopp.connection.requests;

/**
 * Created by Mario on 03.05.2015.
 */
public class CreateGameSessionRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/CreateGame";
    private static final String SOAP_METHOD         = "CreateGame";

    private String gameName;
    private String creatorName;
    private double latitude;
    private double longitude;

    public CreateGameSessionRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
    }


}
