package com.omegashin.homeview;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

public class DashDock extends AppCompatActivity {

    public static List<ApplicationInfo> applicationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, "ca-app-pub-8920354294521666~4415936833");

        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);

        //adView.loadAd(new AdRequest.Builder().build());

    }

    public void OpenDevInfo(View view) {
        Intent intent = new Intent(this, Activity_DeveloperInfo.class);
        startActivity(intent);
    }

    public void OpenWidgetSettings(View view) {

        Intent intent = new Intent(this, Activity_WidgetSettings.class);
        startActivity(intent);

    }

}
