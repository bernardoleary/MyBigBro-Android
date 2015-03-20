package com.infostructure.mybigbro.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.infostructure.mybigbro.model.dto.JsonSerialisable;

public class WebService{

    DefaultHttpClient httpClient;
    HttpContext localContext;
    private String ret;
    HttpResponse response = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;
    HttpPut httpPut = null;
    HttpDelete httpDelete = null;
    String webServiceUrl;

    //The serviceName should be the name of the Service you are going to be using.
    public WebService(String serviceName) {
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        httpClient = new DefaultHttpClient(myParams);
        localContext = new BasicHttpContext();
        webServiceUrl = serviceName;
    }

    // POST
    // Use this method to do a HttpPost on a Web Service.
    public String webInvoke(String methodName, Map<String, String> params, JsonSerialisable data) {
    	String json = (data == null ? "" : data.serialiseToJson());
        return webInvoke(methodName, json, getQueryString(params), "application/json");
    }

    // POST
    private String webInvoke(String methodName, String data, String queryString, String contentType) {
        ret = null;
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        httpPost = new HttpPost(webServiceUrl + methodName + queryString);
        response = null;
        StringEntity tmp = null;        
        //httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        httpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        try {
            tmp = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("MyBigBro", "HttpUtils : UnsupportedEncodingException : " + e);
        }
        httpPost.setEntity(tmp);
        Log.d("MyBigBro", webServiceUrl + "&" + data);
        try {
            response = httpClient.execute(httpPost, localContext);
            if (response != null) {
            	StatusLine statusLine = response.getStatusLine();
            	ret = statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase();
            	//ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            Log.e("MyBigBro", "HttpUtils: " + e);
        }
        return ret;
    }

    // PUT
    // Use this method to do a HttpPut on a Web Service.
    public String webPut(String methodName, Map<String, String> params, JsonSerialisable data) {
    	String json = (data == null ? "" : data.serialiseToJson());
        return webPut(methodName, json, getQueryString(params), "application/json");
    }

    // PUT
    private String webPut(String methodName, String data, String queryString, String contentType) {
        ret = null;
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        httpPut = new HttpPut(webServiceUrl + methodName + queryString);
        response = null;
        StringEntity tmp = null;        
        //httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        httpPut.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        if (contentType != null) {
        	httpPut.setHeader("Content-Type", contentType);
        } else {
        	httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        try {
            tmp = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("MyBigBro", "HttpUtils : UnsupportedEncodingException : " + e);
        }
        httpPut.setEntity(tmp);
        Log.d("MyBigBro", webServiceUrl + "&" + data);
        try {
            response = httpClient.execute(httpPut, localContext);

            if (response != null) {
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            Log.e("MyBigBro", "HttpUtils: " + e);
        }
        return ret;
    }
    
    // GET
    // Use this method to do a HttpGet/WebGet on the web service
    public String webGet(String methodName, Map<String, String> params) {  	
    	String url = webServiceUrl + methodName + getQueryString(params); 	
        httpGet = new HttpGet(url);
        Log.e("WebGetURL: ",url);
        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        // we assume that the response body contains the error message
        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        return ret;
    }
    
    // DELETE
    // Use this method to do a HttpGet/WebGet on the web service
    public String webDelete(String methodName, Map<String, String> params) {    	
    	String url = webServiceUrl + methodName + getQueryString(params); 	
        httpDelete = new HttpDelete(url);
        Log.e("WebDeleteURL: ",url);
        try {
            response = httpClient.execute(httpDelete);
        } catch (Exception e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        // we assume that the response body contains the error message
        try {
            ret = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        return ret;
    }
    
    // Get the query string for this request.
    private String getQueryString(Map<String, String> params) {      	
    	String queryString = "";  	
    	int i = 0;
    	for (Map.Entry<String, String> param : params.entrySet())
    	{
    		if(i == 0){
    			queryString += "?";
    		}
    		else{
    			queryString += "&";
    		}
    		try {
    			queryString += param.getKey() + "=" + URLEncoder.encode(param.getValue(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		i++;
    	}  	
    	return queryString;
    }
}
