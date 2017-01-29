package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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
    ReminderIconGridAdapter reminderIconGridAdapter;
    int IconSelectPosition = -1;

    int[] imageId = {
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_phone_black_24dp,
            R.drawable.ic_delete_black_24dp,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.ic_explore_black_24dp,
            R.drawable.ic_get_app_black_24dp,
            R.drawable.ic_lightbulb_outline_black_24dp,
            R.drawable.ic_markunread_mailbox_black_24dp,
            R.drawable.ic_print_black_24dp,
            R.drawable.ic_question_answer_black_24dp,
            R.drawable.ic_shopping_basket_black_24dp,
            R.drawable.ic_shopping_cart_black_24dp,
            R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_new_releases_black_24dp,
            R.drawable.ic_mail_black_24dp,
            R.drawable.ic_dialpad_black_24dp,
            R.drawable.ic_place_black_24dp,
            R.drawable.ic_photo_black_24dp,
            R.drawable.ic_headset_black_24dp,
            R.drawable.ic_games_black_24dp,
            R.drawable.ic_laptop_mac_black_24dp,
            R.drawable.ic_tv_black_24dp,
            R.drawable.ic_picture_as_pdf_black_24dp,
            R.drawable.ic_local_dining_black_24dp,
            R.drawable.ic_local_laundry_service_black_24dp,
            R.drawable.ic_book_black_24dp,
            R.drawable.ic_local_movies_black_24dp,
            R.drawable.ic_group_black_24dp,
            R.drawable.ic_share_black_24dp,
            R.drawable.ic_home_black_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chip_config);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_button);
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
