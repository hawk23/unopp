package com.gamble.unopp.connection.requests;

/**
 * Created by Mario on 03.05.2015.
 */
public class GetAvailableGameSessionsRequest extends Request
{
    private static final String SOAP_ACTION         = "http://www.w3schools.com/webservices/FahrenheitToCelsius";
    private static final String SOAP_METHOD         = "FahrenheitToCelsius";

    private double latitude;
    private double longitude;
    private double maxdistance;

    public GetAvailableGameSessionsRequest()
    {
        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.requestParameters.put("x", Double.toString(latitude));
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.requestParameters.put("y", Double.toString(longitude));
    }

    public void setMaxdistance(double maxdistance) {
        this.maxdistance = maxdistance;
        this.requestParameters.put("maxdistance", Double.toString(maxdistance));
    }
}
