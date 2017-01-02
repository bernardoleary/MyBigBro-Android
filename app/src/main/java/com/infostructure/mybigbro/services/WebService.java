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

    DefaultHttpClient mHttpClient;
    HttpContext mLocalContext;
    private String mRet;
    HttpResponse mResponse = null;
    HttpPost mHttpPost = null;
    HttpGet mHttpGet = null;
    HttpPut mHttpPut = null;
    HttpDelete mHttpDelete = null;
    String mWebServiceUrl;

    //The serviceName should be the name of the Service you are going to be using.
    public WebService(String serviceName) {
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        this.mHttpClient = new DefaultHttpClient(myParams);
        this.mLocalContext = new BasicHttpContext();
        this.mWebServiceUrl = serviceName;
    }

    // POST
    // Use this method to do a HttpPost on a Web Service.
    public String webInvoke(String methodName, Map<String, String> params, JsonSerialisable data) {
    	String json = (data == null ? "" : data.serialiseToJson());
        return webInvoke(methodName, json, getQueryString(params), "application/json");
    }

    // POST
    private String webInvoke(String methodName, String data, String queryString, String contentType) {
        this.mRet = null;
        this.mHttpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        this.mHttpPost = new HttpPost(this.mWebServiceUrl + methodName + queryString);
        this.mResponse = null;
        StringEntity tmp = null;        
        //this.mHttpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        this.mHttpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        if (contentType != null) {
            this.mHttpPost.setHeader("Content-Type", contentType);
        } else {
            this.mHttpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        try {
            tmp = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("MyBigBro", "HttpUtils : UnsupportedEncodingException : " + e);
        }
        this.mHttpPost.setEntity(tmp);
        Log.d("MyBigBro", this.mWebServiceUrl + "&" + data);
        try {
            this.mResponse = this.mHttpClient.execute(this.mHttpPost, this.mLocalContext);
            if (this.mResponse != null) {
            	StatusLine statusLine = this.mResponse.getStatusLine();
            	this.mRet = statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase();
            	//this.mRet = EntityUtils.toString(this.mResponse.getEntity());
            }
        } catch (Exception e) {
            Log.e("MyBigBro", "HttpUtils: " + e);
        }
        return this.mRet;
    }

    // PUT
    // Use this method to do a HttpPut on a Web Service.
    public String webPut(String methodName, Map<String, String> params, JsonSerialisable data) {
    	String json = (data == null ? "" : data.serialiseToJson());
        return webPut(methodName, json, getQueryString(params), "application/json");
    }

    // PUT
    private String webPut(String methodName, String data, String queryString, String contentType) {
        this.mRet = null;
        this.mHttpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        this.mHttpPut = new HttpPut(this.mWebServiceUrl + methodName + queryString);
        this.mResponse = null;
        StringEntity tmp = null;        
        //this.mHttpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        this.mHttpPut.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        if (contentType != null) {
        	this.mHttpPut.setHeader("Content-Type", contentType);
        } else {
        	this.mHttpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        try {
            tmp = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("MyBigBro", "HttpUtils : UnsupportedEncodingException : " + e);
        }
        this.mHttpPut.setEntity(tmp);
        Log.d("MyBigBro", this.mWebServiceUrl + "&" + data);
        try {
            this.mResponse = this.mHttpClient.execute(this.mHttpPut, this.mLocalContext);

            if (this.mResponse != null) {
                this.mRet = EntityUtils.toString(this.mResponse.getEntity());
            }
        } catch (Exception e) {
            Log.e("MyBigBro", "HttpUtils: " + e);
        }
        return this.mRet;
    }
    
    // GET
    // Use this method to do a HttpGet/WebGet on the web service
    public String webGet(String methodName, Map<String, String> params) {  	
    	String url = this.mWebServiceUrl + methodName + getQueryString(params);
        this.mHttpGet = new HttpGet(url);
        Log.e("WebGetURL: ",url);
        try {
            this.mResponse = this.mHttpClient.execute(this.mHttpGet);
        } catch (Exception e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        // we assume that the this.mResponse body contains the error message
        try {
            this.mRet = EntityUtils.toString(this.mResponse.getEntity());
        } catch (IOException e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        return mRet;
    }
    
    // DELETE
    // Use this method to do a HttpGet/WebGet on the web service
    public String webDelete(String methodName, Map<String, String> params) {    	
    	String url = this.mWebServiceUrl + methodName + getQueryString(params);
        this.mHttpDelete = new HttpDelete(url);
        Log.e("WebDeleteURL: ",url);
        try {
            this.mResponse = this.mHttpClient.execute(this.mHttpDelete);
        } catch (Exception e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        // we assume that the this.mResponse body contains the error message
        try {
            this.mRet = EntityUtils.toString(this.mResponse.getEntity());
        } catch (IOException e) {
            Log.e("MyBigBro:", e.getMessage());
        }
        return this.mRet;
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
