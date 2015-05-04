package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.CreatePlayerResponse;

/**
 * Created by Mario on 02.05.2015.
 */
public class CreatePlayerRequest extends Request
{
    private static final String SOAP_ACTION         = "http://tempuri.org/CreatePlayer";
    private static final String SOAP_METHOD         = "CreatePlayer";

    private String name;

    public CreatePlayerRequest()
    {
        super();

        this.soapAction     = SOAP_ACTION;
        this.soapMethod     = SOAP_METHOD;
        this.response       = new CreatePlayerResponse();
    }

    public void setName(String name) {
        this.name = name;
        this.requestParameters.put("name", name);
    }
}
