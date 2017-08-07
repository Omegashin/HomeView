package com.omegashin.homeview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_DeveloperInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_info);
        CircleImageView logo= (CircleImageView) findViewById(R.id.round_logo);

        Animation LogoScaleAnim = AnimationUtils.loadAnimation(this, R.anim.fab_scale);
        LogoScaleAnim.setStartOffset(600);
        logo.startAnimation(LogoScaleAnim);
    }

    public void openGooglePlus(View view) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse("https://plus.google.com/116674624062929555467"));
        startActivity(webIntent);
    }

    public void openFacebook(View view) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse("https://www.facebook.com/arshdeepsingh.saini.14"));
        startActivity(webIntent);
    }
}