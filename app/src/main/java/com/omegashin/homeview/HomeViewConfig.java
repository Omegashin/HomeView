package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);

        timeZoneSelectionListData = getResources().getStringArray(R.array.timeZoneIds);
        timeZoneSelectionListAdapter = new ArrayAdapter<>(this, R.layout.region_list_item, timeZoneSelectionListData);

        timeZoneSelectionList.setAdapter(timeZoneSelectionListAdapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                HomeViewConfig.this.timeZoneSelectionListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        timeZoneSelectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("foreignTimeZone", ((TextView) v).getText().toString());
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