package com.omegashin.homeview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeView extends AppCompatActivity {

    public static String LOGTAG = "HOME_VIEW_LOG ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void OpenDevInfo(View view){
        Intent intent=new Intent(this, DeveloperInfo.class);
        startActivity(intent);
    }

}
