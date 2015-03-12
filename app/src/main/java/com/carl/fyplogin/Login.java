package com.carl.fyplogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;


public class Login extends Activity implements OnClickListener {

private EditText user, pass;
private Button bLogin;

//Progress Dialog
private ProgressDialog pDialog;

//JSON parser class
JSONParser jsonParser = new JSONParser();
private static final String LOGIN_URL = "http://danu6.it.nuigalway.ie/wbbsl/login.php";
private static final String TAG_SUCCESS = "success";
private static final String TAG_MESSAGE = "message";

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    user = (EditText)findViewById(R.id.username);
    pass = (EditText)findViewById(R.id.password);
    bLogin = (Button) findViewById(R.id.login);
    bLogin.setOnClickListener(this);

    }

    @Override
public void onClick(View v) {
        new AttemptLogin().execute();
    }


class AttemptLogin extends AsyncTask<String, String, String> {
    boolean failure = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Attempting Login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String[] args) {
        int success;
        String username = user.getText().toString();
        String password = pass.getText().toString();

        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));

            Log.d("Request", "starting");

            JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);


            Log.d("Login attempt", json.toString());

            success = json.getInt(TAG_SUCCESS);
            Log.d("Success Int", "" + TAG_SUCCESS.toString());


            if(success == 1) {
                Log.d("Login Successful", json.toString());

                Intent ii = new Intent(Login.this, OtherActivity.class);


                startActivity(ii);

                finish();
                return json.getString(TAG_MESSAGE);
            } else {
                return json.getString(TAG_MESSAGE);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String message) {
        pDialog.dismiss();
        if(message != null) {
            Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
        }
    }
}



}