package com.carl.fyplogin;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class JSONParser {
    static InputStream is = null;
    static JSONObject jsonObj ;
    static String json = "";

    public JSONParser () {


    }

    public JSONObject getJSONFromURL(final String url) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();

        }

        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            StringBuilder str = new StringBuilder();
            String strLine;

            while ((strLine = reader.readLine()) != null) {
                str.append(strLine + "\n");
            }

            is.close();

            json = str.toString();
        } catch (Exception e) {
            Log.e("Error", "Error converting result" + e.toString());
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parsing", "" + e.toString());
        }

        return jsonObj;

    }

    public static JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

        try{

            if(method == "POST") {
                Log.d("HttpRequest", "Make http request function POST");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                Log.d("HttpRequest", "Message being sent: " + params.toString());
            }

            else if(method == "GET") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder str = new StringBuilder();
            String strLine;
            Log.d("JSONPARSER", json);
            while ((strLine = reader.readLine()) != null) {
                str.append(strLine + "\n");
                Log.d("JSONPARSER", "appending content " + strLine.toString());
            }
            is.close();
            Log.d("JSONPARSER", "Parsing complete, Stream  closed");
            json = str.toString();
        } catch (Exception e) {
            Log.e("Error", "Something went wrong with converting result " +e.toString());
        }

        try {
            Log.d("JSONObject", "Creating new json object");
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parsing", "" +e.toString());
        }

        Log.d("JSONObject", "Json Object returned");
        return jsonObj;


    }


}
