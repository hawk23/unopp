package com.gamble.unopp.connection.requests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Albert on 02.05.2015.
 */
public abstract class Request {

    public static String BASE_URL       = "uno.administrator.at/";
    public static String LOBBY_SERVICE  = "LobbyService.php";

    private String cmd;
    protected String service;

    protected HashMap<String, String> parameters;

    protected Request() {
        this.parameters                 = new HashMap<String, String>();
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
        this.parameters.put("cmd", cmd);
    }

    public String getUrl () {
        String url      = "";
        url             += BASE_URL;
        url             += this.service;

        boolean first   = true;
        for (Entry<String, String> entry : this.parameters.entrySet())
        {
            url         += first ? "?" : "&";
            url         +=  entry.getKey() +"=";
            url         +=  entry.getValue();

            first       = false;
        }

        return url;
    }
}
