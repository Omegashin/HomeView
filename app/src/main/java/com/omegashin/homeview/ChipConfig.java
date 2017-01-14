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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    int cursor_index=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chip_config);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button add = (Button) findViewById(R.id.add_button);
        final EditText reminderName = (EditText) findViewById(R.id.reminder_name);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds((new ComponentName(getApplication(), HomeViewWidgetProvider.class)));

                SharedPreferences.Editor editor = sharedPreferences.edit();

                for (int i = 1; i <= 5; i++) {
                    if (sharedPreferences.getString("reminder_" + 1, "").equals("")) {
                        cursor_index++;
                    }

                }
                editor.putString("reminder_"+cursor_index, reminderName.getText().toString());
                editor.putInt("cursorIndex", cursor_index);
                editor.apply();

                Log.e(HomeView.LOGTAG, "onClick: "+cursor_index );

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

                sendBroadcast(intent);

                finish();

            }
        });

    }

}
