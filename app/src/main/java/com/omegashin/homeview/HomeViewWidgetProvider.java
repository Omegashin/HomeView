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
import android.os.Bundle;
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
    public static final String ACTION_REMOVE_REMINDER_1 = "com.omegashin.homeview.ACTION_REMOVE_REMINDER_1";
    public static final String ACTION_REMOVE_REMINDER_2 = "com.omegashin.homeview.ACTION_REMOVE_REMINDER_2";
    public static final String ACTION_REMOVE_REMINDER_3 = "com.omegashin.homeview.ACTION_REMOVE_REMINDER_3";
    public static final String ACTION_REMOVE_REMINDER_4 = "com.omegashin.homeview.ACTION_REMOVE_REMINDER_4";
    public static final String ACTION_REMOVE_REMINDER_5 = "com.omegashin.homeview.ACTION_REMOVE_REMINDER_5";
    public static final String ACTION_SETTINGS = "com.omegashin.homeview.ACTION_SETTINGS";

    Context context;
    int[] appWidgetIds;
    AppWidgetManager appWidgetManager;
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
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_view_interface);
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;


        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);

        remoteViews.setTextViewText(R.id.next_alarm, "Next Alarm: " + nextAlarm);

        remoteViews.setTextViewText(R.id.reminder_label_1, sharedPreferences.getString("reminder_1", ""));
        remoteViews.setTextViewText(R.id.reminder_label_2, sharedPreferences.getString("reminder_2", ""));
        remoteViews.setTextViewText(R.id.reminder_label_3, sharedPreferences.getString("reminder_3", ""));
        remoteViews.setTextViewText(R.id.reminder_label_4, sharedPreferences.getString("reminder_4", ""));
        remoteViews.setTextViewText(R.id.reminder_label_5, sharedPreferences.getString("reminder_5", ""));

        for (int i = 1; i <= 5; i++) {
            hideRevealChips(remoteViews, context, i);
        }

        // Get all ids
        //ComponentName thisWidget = new ComponentName(context,HomeViewWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            Log.e(HomeView.LOGTAG, "Updated Widget: " + String.valueOf(widgetId));
            // Set the text

            // Register an onClickListeners

            setOnClickIntent(remoteViews, R.id.search, ACTION_CLICK);
            setOnClickIntent(remoteViews, R.id.next_alarm, ACTION_CLOCK);
            setOnClickIntent(remoteViews, R.id.device_calender, ACTION_CALENDER);
            setOnClickIntent(remoteViews, R.id.add_reminder, ACTION_REMINDER);
            setOnClickIntent(remoteViews, R.id.device_settings, ACTION_SETTINGS);
            setOnClickIntent(remoteViews, R.id.reminder_entry_1, ACTION_REMOVE_REMINDER_1);
            setOnClickIntent(remoteViews, R.id.reminder_entry_2, ACTION_REMOVE_REMINDER_2);
            setOnClickIntent(remoteViews, R.id.reminder_entry_3, ACTION_REMOVE_REMINDER_3);
            setOnClickIntent(remoteViews, R.id.reminder_entry_4, ACTION_REMOVE_REMINDER_4);
            setOnClickIntent(remoteViews, R.id.reminder_entry_5, ACTION_REMOVE_REMINDER_5);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }

    public void setOnClickIntent(RemoteViews remoteViews, int resId, String intentActionName) {

        Intent intent = new Intent(context, HomeViewWidgetProvider.class);
        intent.setAction(intentActionName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(resId, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_view_interface);

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

            case ACTION_REMOVE_REMINDER_1:
                editor.putString("reminder_" + 1, "");
                editor.putInt("cursorIndex", (sharedPreferences.getInt("cursorIndex", 0) - 1));
                editor.apply();

                hideRevealChips(remoteViews, context, 1);

                break;

            case ACTION_REMOVE_REMINDER_2:
                editor.putString("reminder_" + 2, "");
                editor.putInt("cursorIndex", (sharedPreferences.getInt("cursorIndex", 0) - 1));
                editor.apply();

                hideRevealChips(remoteViews, context, 2);

                break;

            case ACTION_REMOVE_REMINDER_3:
                editor.putString("reminder_" + 3, "");
                editor.putInt("cursorIndex", (sharedPreferences.getInt("cursorIndex", 0) - 1));
                editor.apply();

                hideRevealChips(remoteViews, context, 3);

                break;

            case ACTION_REMOVE_REMINDER_4:
                editor.putString("reminder_" + 4, "");
                editor.putInt("cursorIndex", (sharedPreferences.getInt("cursorIndex", 0) - 1));
                editor.apply();

                hideRevealChips(remoteViews, context, 4);

                break;

            case ACTION_REMOVE_REMINDER_5:
                editor.putString("reminder_" + 5, "");
                editor.putInt("cursorIndex", (sharedPreferences.getInt("cursorIndex", 0) - 1));
                editor.apply();

                hideRevealChips(remoteViews, context, 5);

                break;

            case ACTION_SETTINGS:
                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(settingsIntent);
                break;
        }

        Bundle extras = intent.getExtras();
        if (extras != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), HomeViewWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
    }

    public void hideRevealChips(RemoteViews remoteViews, Context context, int i) {

        remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.VISIBLE);

        if (i > sharedPreferences.getInt("cursorIndex", 0)) {
            Log.e(HomeView.LOGTAG, "rem1: " + sharedPreferences.getString("reminder_1", ""));
            Log.e(HomeView.LOGTAG, "rem2: " + sharedPreferences.getString("reminder_2", ""));
            Log.e(HomeView.LOGTAG, "rem3: " + sharedPreferences.getString("reminder_3", ""));
            Log.e(HomeView.LOGTAG, "rem4: " + sharedPreferences.getString("reminder_4", ""));
            Log.e(HomeView.LOGTAG, "rem5: " + sharedPreferences.getString("reminder_5", ""));

            remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.GONE);
        }
    }
}