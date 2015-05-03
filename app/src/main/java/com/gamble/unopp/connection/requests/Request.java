package com.gamble.unopp.connection.requests;

import android.util.Log;
import android.util.Xml;

import com.gamble.unopp.connection.response.Response;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Albert on 02.05.2015.
 */
public abstract class Request
{
    protected static final String           MAIN_SERVICE_URL        = "http://game.administrator.at/UnoPP.asmx";
    protected static final String           NAMESPACE               = "http://tempuri.org/";

    protected String                        soapAction;
    protected String                        soapMethod;
    protected HashMap<String, Object>       requestParameters;

    protected Request()
    {
        this.requestParameters                                      = new HashMap<String, Object>();
    }

    public Response send ()
    {
        Response                    response        = null;
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

            String                  xmlResponse     = ht.responseDump;
            Document                dom             = getDomElement(xmlResponse);

            // TODO: create proper response Object
            response                                = new Response();
            response.setResponse(xmlResponse);
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

    public Document getDomElement(String xml){

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        // return DOM
        return doc;
    }
}
