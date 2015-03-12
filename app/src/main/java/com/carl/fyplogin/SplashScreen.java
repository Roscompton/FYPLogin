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
    String[] s;
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

        db.execSQL("INSERT INTO basestations (id, Owner, SSID, Sectors) " +
                "VALUES('"+s[0] +s[1] +s[2] +s[3]
                          +s[4] +s[5] +s[6] +s[7]
                          +s[8] +s[9] +s[10] +s[11]
                          +s[12] +s[13] +s[14] +s[15]');

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
            int x = 0;

            for (int i = 0; i < jArray.length(); i++) { //For every JSON object
                JSONObject json = jArray.getJSONObject(i); //One object for every JSON object


                s[x] ="ID: " + json.getInt("ID");
                s[x+1]="Owner: " + json.getString("Owner");
                s[x+2]="SSID: " + json.getInt("SSID");
                s[x+3]="Number of sectors: "+ json.getInt("Sectors");
                x+=4;
              //  System.out.println(jArray.getJSONObject(i));



        //    resultView.setText(s);
        //    text = resultView.getText().toString();


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
