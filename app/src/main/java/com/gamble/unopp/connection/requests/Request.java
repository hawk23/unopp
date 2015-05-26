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
import java.io.StringWriter;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Albert on 02.05.2015.
 */
public abstract class Request
{
    //protected static final String           MAIN_SERVICE_URL        = "http://game.administrator.at/UnoPP.asmx";
    protected static final String           MAIN_SERVICE_URL        = "https://unopp.azurewebsites.net/Unopp.asmx";
    protected static final String           NAMESPACE               = "http://tempuri.org/";

    protected String                        soapAction;
    protected String                        soapMethod;
    protected HashMap<String, Object>       requestParameters;
    protected Response                      response;

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
            ht.debug = true;
            ht.call(this.soapAction, envelope);

            // prints out the xml request with new lines
            Log.d("Unopp Request: " , prettyFormat(ht.requestDump));

            String                  xmlResponse     = ht.responseDump;

            // parse response and build objects
            if (this.response != null) {
                Log.d("Unopp Response: " ,prettyFormat(ht.responseDump));
                this.response.parseXML(xmlResponse);
            }
            else {
                throw new Exception("Response must be created in constructor of request and connot be null here");
            }
        }
        catch (SocketTimeoutException t)
        {
            t.printStackTrace();
            this.response    = null;
        }
        catch (IOException i)
        {
            i.printStackTrace();
            this.response    = null;
        }
        catch (Exception q)
        {
            q.printStackTrace();
            this.response    = null;
        }

        return this.response;
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

    public static String prettyFormat(String input) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException("Error printing xml: " + e.getMessage());
        }
    }
}
