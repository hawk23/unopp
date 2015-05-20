package com.gamble.unopp.connection;

import android.os.AsyncTask;

import com.gamble.unopp.connection.requests.Request;
import com.gamble.unopp.connection.response.Response;

/**
 * Created by Mario on 03.05.2015.
 */
public class  RequestProcessor extends AsyncTask<Request, Void, Response> {

    private RequestProcessorCallback callback;

    public RequestProcessor(RequestProcessorCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Response doInBackground (Request... params) {

        if (params.length != 1) {
            return null;
        }

        return params[0].send();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled(Response s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Response s) {
        super.onPostExecute(s);

        if (this.callback != null) {
            callback.requestFinished(s);
        }
    }
}
