package com.gamble.unopp.connection.response;

/**
 * Created by Mario on 04.05.2015.
 */
public class ResponseResult {

    private boolean status;
    private String message;

    public ResponseResult(boolean status, String message) {

        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
