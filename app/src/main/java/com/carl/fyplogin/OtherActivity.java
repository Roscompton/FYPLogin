package com.carl.fyplogin;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.*;

/**
 * Created by CNAUG_000 on 18/02/2015.
 */
public class OtherActivity extends Activity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }



    public void showMap(View view){

        Intent i = new Intent(this, mapActivity.class);
        startActivity(i);

    }


}
