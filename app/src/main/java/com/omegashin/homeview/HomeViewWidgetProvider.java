package com.omegashin.homeview;

import java.util.ArrayList;
import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class HomeViewWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_CLICK = "com.omegashin.homeview.ACTION_CLICK";
    public static final String ACTION_CLOCK = "com.omegashin.homeview.ACTION_CLOCK";
    public static final String ACTION_CALENDER = "com.omegashin.homeview.ACTION_CALENDER";
    public static final String ACTION_REMINDER = "com.omegashin.homeview.ACTION_REMINDER";
    public static final String ACTION_SETTINGS = "com.omegashin.homeview.ACTION_SETTINGS";

    Context context;
    RemoteViews remoteViews;
    int[] appWidgetIds;
    SharedPreferences sharedPreferences;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        //RemoteViews updateViews = buildUpdate(context); // Update the view using the new data.
        ComponentName thisWidget = new ComponentName(context, HomeViewWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        //manager.updateAppWidget(thisWidget, updateViews);

        //init
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.home_view_interface);
        this.appWidgetIds = appWidgetIds;


        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);

        remoteViews.setTextViewText(R.id.next_alarm, "Next Alarm: " + nextAlarm);

        remoteViews.setTextViewText(R.id.reminder_label_1, sharedPreferences.getString("reminder_1", ""));
        remoteViews.setTextViewText(R.id.reminder_label_2, sharedPreferences.getString("reminder_2", ""));
        remoteViews.setTextViewText(R.id.reminder_label_3, sharedPreferences.getString("reminder_3", ""));
        remoteViews.setTextViewText(R.id.reminder_label_4, sharedPreferences.getString("reminder_4", ""));
        remoteViews.setTextViewText(R.id.reminder_label_5, sharedPreferences.getString("reminder_5", ""));

        for (int i = 1; i <= 5; i++) {
            if (sharedPreferences.getString("reminder_" + i, "").equals("")) {
                remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.GONE);
            } else {
                remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.VISIBLE);
            }
        }

        // Get all ids
        //ComponentName thisWidget = new ComponentName(context,HomeViewWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            Log.e(HomeView.LOGTAG, "Updated Widget: " + String.valueOf(widgetId));
            // Set the text

            // Register an onClickListeners

            setOnClickIntent(R.id.search, ACTION_CLICK);
            setOnClickIntent(R.id.next_alarm, ACTION_CLOCK);
            setOnClickIntent(R.id.device_calender, ACTION_CALENDER);
            setOnClickIntent(R.id.add_reminder, ACTION_REMINDER);
            setOnClickIntent(R.id.device_settings, ACTION_SETTINGS);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }

    public void setOnClickIntent(int resId, String intentActionName) {

        Intent intent = new Intent(context, HomeViewWidgetProvider.class);
        intent.setAction(intentActionName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(resId, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case ACTION_CLICK:

                Intent dhintent = new Intent(context, Search.class);
                dhintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(dhintent);
                break;

            case ACTION_CLOCK:
                Intent i = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;

            case ACTION_CALENDER:
                Intent calenderIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/"));
                calenderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(calenderIntent);
                break;

            case ACTION_REMINDER:
                Intent reminderIntent = new Intent(context, ChipConfig.class);
                reminderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(reminderIntent);
                break;

            case ACTION_SETTINGS:
                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(settingsIntent);
                break;
        }
        super.onReceive(context, intent);
    }
}