package com.carl.fyplogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.AsyncTask;


/**
 * Created by CNAUG_000 on 06/03/2015.
 */
public class SplashScreen extends Activity {

    TextView resultView;

public static ArrayList<Integer> ID = new ArrayList();
public static ArrayList<String> Owner = new ArrayList();
public static ArrayList<Integer> SSID = new ArrayList();
public static ArrayList<Integer> Sectors = new ArrayList();
public static ArrayList<Double> Latitude = new ArrayList();
public static ArrayList<Double> Longitude = new ArrayList();


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        new PrefetchData().execute();
    }

    public static ArrayList<String> getOwner() {
        return Owner;
    }

    public static ArrayList<Double> getLatitude() {
        return Latitude;
    }

    public static ArrayList<Double> getLongitude() {
        return Longitude;
    }

    public static ArrayList<Integer> getSSID() {
        return SSID;
    }

    public static ArrayList<Integer> getID() {
        return ID;
    }

    public static ArrayList<Integer> getSectors() {
        return Sectors;
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String result = "";
            InputStream isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://danu6.it.nuigalway.ie/wbbsl/getAllCustomers.php");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse response = httpclient.execute(httpPost);
                Log.d("HTTPResponse", "Executing httpclient response");
                HttpEntity entity = response.getEntity();
                Log.d("HTTPEntity", "Retrieving entity");
                isr = entity.getContent();
            }
            catch(Exception e) {
                Log.e("Log_tag", "Error in http connection " + e.toString());
                resultView.setText("Could not connect to database");
            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\\n");
                }
                result=sb.toString();
                isr.close();

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result "+ e.toString());
            }

            //Parsing JSON data
            try {
                JSONArray jArray = new JSONArray(result);
                Log.d("JSONArray", "Created JSON array");

                for (int i = 0; i < jArray.length(); i++) { //For every JSON object
                    JSONObject json = jArray.getJSONObject(i); //One object for every JSON object

                    Log.d("JSONContents", "ID for first object: " +json.getString("ID"));

                    ID.add(Integer.parseInt(json.getString("ID")));
                    Owner.add(json.getString("Owner"));
                    SSID.add(Integer.parseInt(json.getString("SSID")));
                    Sectors.add(Integer.parseInt(json.getString("Sectors")));
                    Latitude.add(Double.parseDouble(json.getString("Latitude")));
                    Longitude.add(Double.parseDouble(json.getString("Longitude")));

                    Log.d("Parsing JSON to arrays", "Param: " +Latitude.get(i));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }




            //Creating db if non existent
            SQLiteDatabase db = openOrCreateDatabase("basestations", Context.MODE_PRIVATE, null);

            db.execSQL("DROP TABLE IF EXISTS basestations");
            db.execSQL("CREATE TABLE IF NOT EXISTS  basestations ("
                    + "ID INTEGER PRIMARY KEY,"
                    + "Owner VARCHAR,"
                    + "SSID INTEGER,"
                    + "Sectors INTEGER,"
                    + "Latitude DOUBLE,"
                    + "Longitude DOUBLE)");




                for (int i = 0; i < ID.size(); i++) {
                    db.execSQL("INSERT INTO basestations (id, Owner, SSID, Sectors, Latitude, Longitude) " +
                            "VALUES ('" + ID.get(i) + "','" + Owner.get(i) + "','" + SSID.get(i) + "','"
                            + Sectors.get(i) + "','" +Latitude.get(i) + "','" + Longitude.get(i)+"')");

                    Log.d("DB INSERT", "Inserted values: " + ID.get(i));
                    Log.d("DB INSERT", "Inserted values: " + Owner.get(i));
                    Log.d("DB INSERT", "Inserted values: " + SSID.get(i));
                    Log.d("DB INSERT", "Inserted values: " + Sectors.get(i));
                    Log.d("DB INSERT", "Inserted values: " + Latitude.get(i));
                    Log.d("DB INSERT", "Inserted values: " + Longitude.get(i));
                }



            return null;
    }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreen.this, Login.class);
            startActivity(i);
        }

}

}
