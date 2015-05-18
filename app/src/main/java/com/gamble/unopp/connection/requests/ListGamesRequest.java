package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.ListGamesResponse;

/**
 * Created by Mario on 03.05.2015.
 */
public class ListGamesRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/ListGames";
    private static final String SOAP_METHOD         = "ListGames";

    private double latitude;
    private double longitude;
    private double maxdistance;

    public ListGamesRequest() {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new ListGamesResponse();
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.requestParameters.put("latitude", Double.toString(latitude));
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.requestParameters.put("longitude", Double.toString(longitude));
    }

    public void setMaxdistance(double maxdistance) {
        this.maxdistance = maxdistance;
        this.requestParameters.put("maxDistance", Double.toString(maxdistance));
    }
}
