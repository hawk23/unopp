package com.gamble.unopp.connection;

import com.gamble.unopp.connection.response.Response;

/**
 * Created by Mario on 03.05.2015.
 */
public interface RequestProcessorCallback {

    void requestFinished (Response response);
}
