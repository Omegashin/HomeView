package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Activity_AddReminder extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    int interstitialCount;

    public static final int ICON_REQUEST = 1;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    //int IconResId = R.drawable.app_android;
    String IconResName = "app_android";
    Intent intent;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        interstitialCount = sharedPreferences.getInt("interstitialCount", 0);

        final Button add = (Button) findViewById(R.id.add);
        final Button cancel = (Button) findViewById(R.id.cancel);
        final Button delete = (Button) findViewById(R.id.complete);
        final EditText reminderName = (EditText) findViewById(R.id.chip_label);
        imageView = (ImageView) findViewById(R.id.reminder_icon);

        intent = getIntent();

        if (intent.getExtras() != null) {
            if (intent.getBooleanExtra("isEdit", false)) {
                index = intent.getIntExtra("index", 0);

                Gson gson = new Gson();
                Type type = new TypeToken<List<Reminder>>() {
                }.getType();

                ArrayList<Reminder> reminders = new ArrayList<>();

                if (!(sharedPreferences.getString("remindersData", "")).equals("")) {
                    reminders = gson.fromJson(sharedPreferences.getString("remindersData", ""), type);
                }

                reminderName.setText(reminders.get(index).getLabel());
                imageView.setImageResource(getResources().getIdentifier(reminders.get(index).getIconName(), "drawable", getPackageName()));

                //IconResId=reminders.get(index).getIcon();
                IconResName = reminders.get(index).getIconName();

                delete.setVisibility(View.VISIBLE);
            }
        }

        reminderName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    add.performClick();
                }
                return false;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Reminder>>() {
                }.getType();

                if (reminderName.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Please include some text", Toast.LENGTH_LONG).show();
                } else if (IconResName.equals("app_android")) {
                    Toast.makeText(getBaseContext(), "Please choose an icon", Toast.LENGTH_LONG).show();
                } else {
                    if (intent.getExtras() != null) {
                        if (!intent.getBooleanExtra("isEdit", false)) {

                            Reminder reminder = new Reminder();
                            reminder.setLabel(reminderName.getText().toString());
                            //reminder.setIcon(IconResId);
                            reminder.setIconName(IconResName);

                            ArrayList<Reminder> reminders = new ArrayList<>();

                            if (!(sharedPreferences.getString("remindersData", "")).equals("")) {
                                reminders = gson.fromJson(sharedPreferences.getString("remindersData", ""), type);
                            }

                            reminders.add(reminder);
                            editor.putString("remindersData", gson.toJson(reminders));
                            editor.apply();

                        } else {

                            ArrayList<Reminder> reminders = new ArrayList<>();

                            if (!(sharedPreferences.getString("remindersData", "")).equals("")) {
                                reminders = gson.fromJson(sharedPreferences.getString("remindersData", ""), type);
                            }

                            Reminder reminder = new Reminder();
                            reminder.setLabel(reminderName.getText().toString());
                            //reminder.setIcon(IconResId);
                            reminder.setIconName(IconResName);
                            reminders.set(index, reminder);
                            editor.putString("remindersData", gson.toJson(reminders));
                            editor.apply();

                        }
                    }

                    Intent updateIntent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                    updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int ids[] = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), HomeViewWidgetProvider.class));
                    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    sendBroadcast(updateIntent);

                    finish();

                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                ArrayList<Reminder> reminders = new ArrayList<>();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Reminder>>() {
                }.getType();

                if (!(sharedPreferences.getString("remindersData", "")).equals("")) {
                    reminders = gson.fromJson(sharedPreferences.getString("remindersData", ""), type);
                }
                reminders.remove(index);
                editor.putString("remindersData", gson.toJson(reminders));
                editor.apply();

                Intent updateIntent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int ids[] = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), HomeViewWidgetProvider.class));
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(updateIntent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ICON_REQUEST) {

            if (resultCode == RESULT_OK) {

                //IconResId = data.getIntExtra("icon", 0);
                IconResName = data.getStringExtra("iconName");

                //imageView.setImageResource(IconResId);
                imageView.setImageResource(getResources().getIdentifier(IconResName, "drawable", getPackageName()));

            }
        }
    }

    public void OpenIconReturn(View view) {
        Intent intent = new Intent(this, Activity_IconReturn.class);
        startActivityForResult(intent, ICON_REQUEST);
    }

}
