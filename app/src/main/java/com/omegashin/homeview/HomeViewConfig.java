package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextClock;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;
import static java.security.AccessController.getContext;

public class HomeViewConfig extends AppCompatActivity {

    int mAppWidgetId;
    ListView timeZoneSelectionList;
    ArrayAdapter timeZoneSelectionListAdapter;
    SharedPreferences sharedPreferences;
    String[] timeZoneSelectionListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view_config);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        timeZoneSelectionList = (ListView) findViewById(R.id.time_zone_selection_list);

        timeZoneSelectionListData = getResources().getStringArray(R.array.timeZoneIds);
        timeZoneSelectionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeZoneSelectionListData);

        timeZoneSelectionList.setAdapter(timeZoneSelectionListAdapter);

        timeZoneSelectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foreignTimeZone", timeZoneSelectionListData[position]);
                editor.apply();

                Intent intent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds((new ComponentName(getApplication(), HomeViewWidgetProvider.class)));

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

                sendBroadcast(intent);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();

            }
        });
    }

}