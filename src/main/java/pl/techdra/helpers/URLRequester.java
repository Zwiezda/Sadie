package pl.techdra.helpers;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.xml.GenericXml;

import java.io.IOException;
import java.util.Map;


/**
 * Class prepared for  requests to plugin's services
 */


public class URLRequester {
    private HttpRequestFactory requestFactory;

    /**
     * Class constructor
     */
    public URLRequester() {
        requestFactory = new NetHttpTransport().createRequestFactory();
    }


    /**
     *
     * @param url url for request
     * @param method request's method
     * @see RequstMethod
     * @param headers request's headers map
     * @param params request's parameters
     * @param body request's body
     * @return response
     * @throws IOException if  anny problems establishing a connection
     */
    public HttpResponse makeRequest(String url, RequstMethod method, Map<String, String> headers, Map<String, String> params, HttpContent body) throws IOException {
        HttpRequest request = null;
        GenericUrl genericUrl = new GenericUrl(url);

        if (params != null) {
            genericUrl.putAll(params);
        }

        HttpHeaders httpHeaders = new HttpHeaders();

        if (headers != null) {
            httpHeaders.putAll(headers);
        }

        switch (method) {
            case GET:
                request = requestFactory.buildGetRequest(genericUrl);
                break;
            case POST:
                request = requestFactory.buildPostRequest(genericUrl, body);
                break;
            case PUT:
                request = requestFactory.buildPutRequest(genericUrl, body);
                break;
            case HEAD:
                request = requestFactory.buildHeadRequest(genericUrl);
                break;
            case PATCH:
                request = requestFactory.buildPatchRequest(genericUrl, body);
                break;
            case DELETE:
                request = requestFactory.buildDeleteRequest(genericUrl);
                break;
        }


        request.setHeaders(httpHeaders);
        return request.execute();

    }

    /**
     *
     * @param url url for request
     * @param method request's method
     * @see RequstMethod
     * @param headers request's headers map
     * @param params request's parameters
     * @param body request's body (in JSON format)
     * @return response
     * @throws IOException if  anny problems establishing a connection
     */
    public GenericJson makeJSONRequest(String url, RequstMethod method, Map<String, String> headers, Map<String, String> params, HttpContent body) throws IOException {
        return makeRequest(url, method, headers, params, body).parseAs(GenericJson.class);
    }

    /**
     *
     * @param url url for request
     * @param method request's method
     * @see RequstMethod
     * @param headers request's headers map
     * @param params request's parameters
     * @param body request's body (in XML format)
     * @return response
     * @throws IOException if  anny problems establishing a connection
     */
    public GenericXml makeXMLRequest(String url, RequstMethod method, Map<String, String> headers, Map<String, String> params, HttpContent body) throws IOException {
        return makeRequest(url, method, headers, params, body).parseAs(GenericXml.class);
    }


}
