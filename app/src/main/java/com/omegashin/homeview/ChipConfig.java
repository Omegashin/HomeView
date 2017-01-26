package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ChipConfig extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    GridView reminderIconsGrid;
    int[] reminderIconGridData;
    ReminderIconGridAdapter reminderIconGridAdapter;
    int IconSelectPosition = -1;

    int[] imageId = {
            R.drawable.ic_android_black_24dp,
            R.drawable.ic_play_circle_filled_black_24dp,
            R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_clear_black_24dp,
            R.drawable.ic_settings_black_24dp,
            R.drawable.ic_mail_black_24dp,
            R.drawable.ic_place_black_24dp,
            R.drawable.ic_format_list_numbered_black_24dp,
            R.drawable.ic_event_note_black_24dp,
            R.drawable.ic_photo_library_black_24dp,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chip_config);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final Button add = (Button) findViewById(R.id.add_button);
        final EditText reminderName = (EditText) findViewById(R.id.reminder_name);

        reminderName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.e(HomeView.LOGTAG, "Enter pressed");
                    add.performClick();
                }
                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IconSelectPosition == -1) {
                    Toast.makeText(getBaseContext(), "Specify an Icon!!", Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                    int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds((new ComponentName(getApplication(), HomeViewWidgetProvider.class)));

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    for (int i = 1; i <= 5; i++) {

                        if (sharedPreferences.getString("chip_label_" + i, "").equals("")) {

                            editor.putString("chip_label_" + i, reminderName.getText().toString());
                            editor.apply();

                            if (sharedPreferences.getInt("chip_icon_" + i, R.drawable.ic_android_black_24dp) == R.drawable.ic_android_black_24dp) {

                                editor.putInt("chip_icon_" + i, imageId[IconSelectPosition]);
                                editor.apply();

                            }

                            Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                            Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                            Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                            Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                            Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));

                            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

                            sendBroadcast(intent);

                            break;

                        }

                        if (sharedPreferences.getInt("chip_icon_" + i, R.drawable.ic_add_alert_black_24dp) == R.drawable.ic_add_alert_black_24dp) {

                            editor.putInt("chip_icon_" + i, imageId[IconSelectPosition]);
                            editor.apply();

                        }


                        Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                        Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                        Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                        Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                        Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));
                    }

                    finish();

                }
            }
        });

        reminderIconsGrid = (GridView) findViewById(R.id.reminder_icon_grid);

        reminderIconGridData = getResources().getIntArray(R.array.reminder_icon_drawables);
        reminderIconGridAdapter = new ReminderIconGridAdapter(this, imageId);

        reminderIconsGrid.setAdapter(reminderIconGridAdapter);

        reminderIconsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
/*
                SharedPreferences.Editor editor = sharedPreferences.edit();

                for (int i = 1; i <= 5; i++) {

                    if (sharedPreferences.getInt("reminderIcon_" + i, R.drawable.ic_android_black_24dp)==R.drawable.ic_android_black_24dp) {

                        editor.putInt("reminderIcon_" + i, imageId[position]);
                        editor.apply();

                    }

                    Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("reminderIcon_" + 1, R.drawable.ic_android_black_24dp));
                    Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("reminderIcon_" + 2, R.drawable.ic_android_black_24dp));
                    Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("reminderIcon_" + 3, R.drawable.ic_android_black_24dp));
                    Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("reminderIcon_" + 4, R.drawable.ic_android_black_24dp));
                    Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("reminderIcon_" + 5, R.drawable.ic_android_black_24dp));

                    Toast.makeText(getBaseContext(), "You Clicked at " + position, Toast.LENGTH_SHORT).show();


                }*/
                IconSelectPosition = position;
                reminderIconsGrid.getSelectedItem();

            }
        });
    }

}
