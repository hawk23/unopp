package com.gamble.unopp.connection.requests;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Created by Albert on 02.05.2015.
 */
public abstract class Request
{
    protected static final String           MAIN_SERVICE_URL        = "http://www.w3schools.com/webservices/tempconvert.asmx";
    protected static final String           NAMESPACE               = "http://www.w3schools.com/webservices/";

    protected String                        soapAction;
    protected String                        soapMethod;
    protected HashMap<String, Object>       requestParameters;

    protected Request()
    {
        this.requestParameters                                      = new HashMap<String, Object>();
    }

    public String send ()
    {
        String                      response        = null;
        SoapObject                  request         = new SoapObject(NAMESPACE, this.soapMethod);

        // add properties to SOAP request.
        for (Entry<String, Object> entry : this.requestParameters.entrySet())
        {
            request.addProperty(entry.getKey(), entry.getValue());
        }

        SoapSerializationEnvelope   envelope        = getSoapSerializationEnvelope(request);
        HttpTransportSE             ht              = getHttpTransportSE();

        try {
            ht.call(this.soapAction, envelope);

            SoapPrimitive           resultsString   = (SoapPrimitive)envelope.getResponse();
            response                                = resultsString.toString();
        }
        catch (SocketTimeoutException t)
        {
            t.printStackTrace();
        }
        catch (IOException i)
        {
            i.printStackTrace();
        }
        catch (Exception q)
        {
            q.printStackTrace();
        }

        // TODO: parse response XML and build a model object.
        return response;
    }

    private final HttpTransportSE getHttpTransportSE ()
    {
        HttpTransportSE ht  = new HttpTransportSE(Proxy.NO_PROXY, MAIN_SERVICE_URL, 60000);
        ht.debug            = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");

        return ht;
    }

    private final SoapSerializationEnvelope getSoapSerializationEnvelope (SoapObject request)
    {
        SoapSerializationEnvelope envelope  = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet                     = true;
        envelope.implicitTypes              = true;
        envelope.setAddAdornments (false);
        envelope.setOutputSoapObject (request);

        return envelope;
    }
}
