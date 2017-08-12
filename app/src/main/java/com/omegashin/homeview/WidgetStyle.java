package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class WidgetStyle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_style);

    }

    public void setViewShow(SharedPreferences sp, int widgetId, int id, String name) {

        CheckBox view = (CheckBox) findViewById(id);

        if (view.isChecked()) {
            sp.edit().putBoolean("show" + name + widgetId, true).apply();
        } else {
            sp.edit().putBoolean("show" + name + widgetId, false).apply();
        }
    }

    public void save(View view) {

        CheckBox clock = (CheckBox) findViewById(R.id.box_clock);
        CheckBox apps = (CheckBox) findViewById(R.id.box_apps);
        CheckBox reminders = (CheckBox) findViewById(R.id.box_reminders);

        if(clock.isChecked() || apps.isChecked() || reminders.isChecked()) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            int widgetId = getIntent().getIntExtra("widgetId", 0);

            setViewShow(sharedPreferences, widgetId, R.id.box_clock, "Clock");
            setViewShow(sharedPreferences, widgetId, R.id.box_apps, "Apps");
            setViewShow(sharedPreferences, widgetId, R.id.box_reminders, "Reminders");

            Intent updateIntent = new Intent(this, HomeViewWidgetProvider.class);
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(getApplicationContext(), HomeViewWidgetProvider.class));
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(updateIntent);

            finish();
        }
        else{
            Toast.makeText(this, "Please select at least one item",Toast.LENGTH_LONG).show();
        }
    }

}
