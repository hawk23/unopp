package com.gamble.unopp.connection.requests;

/**
 * Created by Mario on 02.05.2015.
 */
public class CreatePlayerRequest extends Request {

    private String name;

    public CreatePlayerRequest() {
        super();
        this.setCmd("createPlayer");
        this.service = LOBBY_SERVICE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.parameters.put("name", name);
    }
}
