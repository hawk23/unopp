package com.gamble.unopp.connection;

import android.os.AsyncTask;

import com.gamble.unopp.connection.requests.Request;

/**
 * Created by Mario on 03.05.2015.
 */
public class  RequestProcessor extends AsyncTask<Request, Void, String> {

    private RequestProcessorCallback callback;

    public RequestProcessor(RequestProcessorCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground (Request... params) {

        if (params.length != 1) {
            return "";
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
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.requestFinished(s);
    }
}
