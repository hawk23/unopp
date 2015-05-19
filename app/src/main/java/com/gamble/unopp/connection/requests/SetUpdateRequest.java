package com.gamble.unopp.connection.requests;

import com.gamble.unopp.connection.response.SetUpdateResponse;
import com.gamble.unopp.connection.response.StartGameResponse;

/**
 * Created by Mario on 19.05.2015.
 */
public class SetUpdateRequest extends Request {

    private static final String SOAP_ACTION         = "http://tempuri.org/SetUpdate";
    private static final String SOAP_METHOD         = "SetUpdate";

    private int gameID;
    private int updateID;
    private String update;

    public SetUpdateRequest () {

        super();

        this.soapAction = SOAP_ACTION;
        this.soapMethod = SOAP_METHOD;
        this.response   = new SetUpdateResponse();
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
        this.requestParameters.put("gameID", gameID);
    }

    public void setUpdateID(int updateID) {
        this.updateID = updateID;
        this.requestParameters.put("updateID", updateID);
    }

    public void setUpdate(String update) {
        this.update = update;
        this.requestParameters.put("update", update);
    }
}