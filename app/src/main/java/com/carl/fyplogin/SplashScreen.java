package com.carl.fyplogin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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


/**
 * Created by CNAUG_000 on 06/03/2015.
 */
public class SplashScreen extends ActionBarActivity {

    TextView resultView;
    String[] ID, SSID, Owner, Sectors;
    String text;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        Thread startTimer = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        startTimer.start();

        getData();

        //Creating db if non existent
        SQLiteDatabase db = openOrCreateDatabase("basestations", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS  basestations ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Owner VARCHAR,"
                + "SSID INTEGER,"
                + "Sectors INTEGER)");

        for (int i = 0; i < ID.length; i++)
            db.execSQL("INSERT INTO basestations (id, Owner, SSID, Sectors) " +
                    "VALUES ('" + ID[i] + "','" + Owner[i] + "','" + SSID[i] + "','" + Sectors[i] + "')");
    }

    public void getData() {
        String result = "";
        InputStream isr = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://danu6.it.nuigalway.ie/wbbsl/getAllCustomers.php");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }
        catch(Exception e) {
            Log.e("Log_tag", "Error in http connection " + e.toString());
            resultView.setText("Could not connect to database");
        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
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

            for (int i = 0; i < jArray.length(); i++) { //For every JSON object
                JSONObject json = jArray.getJSONObject(i); //One object for every JSON object


                ID[i] = "" +json.getInt("ID");
                Owner[i] = "" +json.getString("Owner");
                SSID[i] = "" +json.getInt("SSID");
                Sectors[i] = "" + json.getInt("Sectors");

        }


    } catch (JSONException e) {
            e.printStackTrace();
        }


    /*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */
    }

}
