package com.omegashin.homeview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeViewWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_SEARCH = "com.omegashin.homeview.ACTION_SEARCH";
    public static final String ACTION_SHOW_ALARMS = "com.omegashin.homeview.ACTION_SHOW_ALARMS";
    public static final String ACTION_MAPS = "com.omegashin.homeview.ACTION_MAPS";
    public static final String ACTION_MUSIC = "com.omegashin.homeview.ACTION_MUSIC";
    public static final String ACTION_STORE = "com.omegashin.homeview.ACTION_STORE";
    public static final String ACTION_CALC = "com.omegashin.homeview.ACTION_CALC";
    public static final String ACTION_GALLERY = "com.omegashin.homeview.ACTION_GALLERY";
    public static final String ACTION_G_MAIL = "com.omegashin.homeview.ACTION_G_MAIL";

    public static final String ACTION_CALENDER = "com.omegashin.homeview.ACTION_CALENDER";
    public static final String ACTION_ADD_CHIP = "com.omegashin.homeview.ACTION_ADD_CHIP";
    public static final String ACTION_SETTINGS = "com.omegashin.homeview.ACTION_SETTINGS";

    public static final String ACTION_REMOVE_CHIP_1 = "com.omegashin.homeview.ACTION_REMOVE_CHIP_1";
    public static final String ACTION_REMOVE_CHIP_2 = "com.omegashin.homeview.ACTION_REMOVE_CHIP_2";
    public static final String ACTION_REMOVE_CHIP_3 = "com.omegashin.homeview.ACTION_REMOVE_CHIP_3";
    public static final String ACTION_REMOVE_CHIP_4 = "com.omegashin.homeview.ACTION_REMOVE_CHIP_4";
    public static final String ACTION_REMOVE_CHIP_5 = "com.omegashin.homeview.ACTION_REMOVE_CHIP_5";

    Context context;
    int[] appWidgetIds;
    AppWidgetManager appWidgetManager;
    SharedPreferences sharedPreferences;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        this.context = context;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_view_interface);

        ComponentName thisWidget = new ComponentName(context, HomeViewWidgetProvider.class);
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;

        //Set Next Alarm Info
        String nextAlarm = Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
        remoteViews.setTextViewText(R.id.next_alarm, "Next Alarm: " + nextAlarm);

        //Set Foreign Clock Time
        String foreignTimeZonePref = sharedPreferences.getString("foreignTimeZone", "");
        remoteViews.setString(R.id.text_clock_foreign, "setTimeZone", foreignTimeZonePref);


        //Set chip labels
        remoteViews.setTextViewText(R.id.reminder_label_1, sharedPreferences.getString("chip_label_" + 1, ""));
        remoteViews.setTextViewText(R.id.reminder_label_2, sharedPreferences.getString("chip_label_" + 2, ""));
        remoteViews.setTextViewText(R.id.reminder_label_3, sharedPreferences.getString("chip_label_" + 3, ""));
        remoteViews.setTextViewText(R.id.reminder_label_4, sharedPreferences.getString("chip_label_" + 4, ""));
        remoteViews.setTextViewText(R.id.reminder_label_5, sharedPreferences.getString("chip_label_" + 5, ""));
        //Set chip icons
        remoteViews.setImageViewResource(R.id.reminder_icon_1, sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_add_alert_black_24dp));
        remoteViews.setImageViewResource(R.id.reminder_icon_2, sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_add_alert_black_24dp));
        remoteViews.setImageViewResource(R.id.reminder_icon_3, sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_add_alert_black_24dp));
        remoteViews.setImageViewResource(R.id.reminder_icon_4, sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_add_alert_black_24dp));
        remoteViews.setImageViewResource(R.id.reminder_icon_5, sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_add_alert_black_24dp));


        /*if (sharedPreferences.getInt("chip_icon_5", R.drawable.ic_add_alert_black_24dp) == R.drawable.ic_add_alert_black_24dp) {
            remoteViews.setViewVisibility(R.id.add_reminder, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.add_reminder, View.GONE);
        }*/

        for (int i = 1; i <= 5; i++) {

            remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.VISIBLE);

            if (sharedPreferences.getString("chip_label_" + i, "").equals("")) {

                remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.GONE);
            }
        }

        // Get all ids
        //ComponentName thisWidget = new ComponentName(context,HomeViewWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            Log.e(HomeView.LOGTAG, "Updated Widget: " + String.valueOf(widgetId));
            // Set the text

            // Register an onClickListeners

            setOnClickIntent(remoteViews, R.id.search, ACTION_SEARCH);
            setOnClickIntent(remoteViews, R.id.next_alarm, ACTION_SHOW_ALARMS);
            setOnClickIntent(remoteViews, R.id.app_calender, ACTION_CALENDER);
            setOnClickIntent(remoteViews, R.id.add_reminder, ACTION_ADD_CHIP);
            setOnClickIntent(remoteViews, R.id.app_settings, ACTION_SETTINGS);
            setOnClickIntent(remoteViews, R.id.reminder_entry_1, ACTION_REMOVE_CHIP_1);
            setOnClickIntent(remoteViews, R.id.reminder_entry_2, ACTION_REMOVE_CHIP_2);
            setOnClickIntent(remoteViews, R.id.reminder_entry_3, ACTION_REMOVE_CHIP_3);
            setOnClickIntent(remoteViews, R.id.reminder_entry_4, ACTION_REMOVE_CHIP_4);
            setOnClickIntent(remoteViews, R.id.reminder_entry_5, ACTION_REMOVE_CHIP_5);

            setOnClickIntent(remoteViews, R.id.app_maps, ACTION_MAPS);
            setOnClickIntent(remoteViews, R.id.app_music, ACTION_MUSIC);
            setOnClickIntent(remoteViews, R.id.app_store, ACTION_STORE);
            setOnClickIntent(remoteViews, R.id.app_calc, ACTION_CALC);
            setOnClickIntent(remoteViews, R.id.app_gallery, ACTION_GALLERY);
            setOnClickIntent(remoteViews, R.id.app_gmail, ACTION_G_MAIL);

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

        super.onReceive(context, intent);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_view_interface);

        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

        final PackageManager pm = context.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);

        switch (intent.getAction()) {
            case ACTION_SEARCH:

                Intent dhintent = new Intent(context, Search.class);
                dhintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(dhintent);
                break;

            case ACTION_SHOW_ALARMS:
                Intent i = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;

            case ACTION_CALENDER:
                Intent calenderIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/"));
                calenderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(calenderIntent);
                break;

            case ACTION_ADD_CHIP:
                Intent reminderIntent = new Intent(context, ChipConfig.class);
                reminderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(reminderIntent);
                break;

            case ACTION_REMOVE_CHIP_1:
                editor.putString("chip_label_" + 1, "");
                editor.putInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp);
                editor.apply();

                hideRevealChips(remoteViews, context, 1);

                for (int h = 1; h <= 5; h++) {
                    editor.putString("chip_label_" + (h), sharedPreferences.getString("chip_label_" + (h + 1), ""));
                    editor.putInt("chip_icon_" + (h), sharedPreferences.getInt("chip_icon_" + (h + 1), R.drawable.ic_android_black_24dp));
                    editor.apply();
                }

                Log.e(HomeView.LOGTAG, "rem1: " + sharedPreferences.getString("chip_label_" + 1, ""));
                Log.e(HomeView.LOGTAG, "rem2: " + sharedPreferences.getString("chip_label_" + 2, ""));
                Log.e(HomeView.LOGTAG, "rem3: " + sharedPreferences.getString("chip_label_" + 3, ""));
                Log.e(HomeView.LOGTAG, "rem4: " + sharedPreferences.getString("chip_label_" + 4, ""));
                Log.e(HomeView.LOGTAG, "rem5: " + sharedPreferences.getString("chip_label_" + 5, ""));

                Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));


                Bundle extras = intent.getExtras();
                if (extras != null) {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName thisAppWidget = new ComponentName(context.getPackageName(), HomeViewWidgetProvider.class.getName());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                    onUpdate(context, appWidgetManager, appWidgetIds);
                }

                break;

            case ACTION_REMOVE_CHIP_2:
                editor.putString("chip_label_" + 2, "");
                editor.putInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp);
                editor.apply();

                hideRevealChips(remoteViews, context, 2);

                for (int h = 2; h <= 5; h++) {
                    editor.putString("chip_label_" + (h), sharedPreferences.getString("chip_label_" + (h + 1), ""));
                    editor.putInt("chip_icon_" + (h), sharedPreferences.getInt("chip_icon_" + (h + 1), R.drawable.ic_android_black_24dp));
                    editor.apply();
                }

                Log.e(HomeView.LOGTAG, "rem1: " + sharedPreferences.getString("chip_label_" + 1, ""));
                Log.e(HomeView.LOGTAG, "rem2: " + sharedPreferences.getString("chip_label_" + 2, ""));
                Log.e(HomeView.LOGTAG, "rem3: " + sharedPreferences.getString("chip_label_" + 3, ""));
                Log.e(HomeView.LOGTAG, "rem4: " + sharedPreferences.getString("chip_label_" + 4, ""));
                Log.e(HomeView.LOGTAG, "rem5: " + sharedPreferences.getString("chip_label_" + 5, ""));

                Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));

                updateWidget(context, intent);

                break;

            case ACTION_REMOVE_CHIP_3:
                editor.putString("chip_label_" + 3, "");
                editor.putInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp);
                editor.apply();

                hideRevealChips(remoteViews, context, 3);

                for (int h = 3; h <= 5; h++) {
                    editor.putString("chip_label_" + (h), sharedPreferences.getString("chip_label_" + (h + 1), ""));
                    editor.putInt("reminderIcon_" + (h), sharedPreferences.getInt("chip_icon_" + (h + 1), R.drawable.ic_android_black_24dp));
                    editor.apply();
                }

                Log.e(HomeView.LOGTAG, "rem1: " + sharedPreferences.getString("chip_label_" + 1, ""));
                Log.e(HomeView.LOGTAG, "rem2: " + sharedPreferences.getString("chip_label_" + 2, ""));
                Log.e(HomeView.LOGTAG, "rem3: " + sharedPreferences.getString("chip_label_" + 3, ""));
                Log.e(HomeView.LOGTAG, "rem4: " + sharedPreferences.getString("chip_label_" + 4, ""));
                Log.e(HomeView.LOGTAG, "rem5: " + sharedPreferences.getString("chip_label_" + 5, ""));

                Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));

                updateWidget(context, intent);

                break;

            case ACTION_REMOVE_CHIP_4:
                editor.putString("chip_label_" + 4, "");
                editor.putInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp);
                editor.apply();

                hideRevealChips(remoteViews, context, 4);

                for (int h = 4; h <= 5; h++) {
                    editor.putString("chip_label_" + (h), sharedPreferences.getString("chip_label_" + (h + 1), ""));
                    editor.putInt("chip_icon_" + (h), sharedPreferences.getInt("chip_icon_" + (h + 1), R.drawable.ic_android_black_24dp));
                    editor.apply();
                }

                Log.e(HomeView.LOGTAG, "rem1: " + sharedPreferences.getString("chip_label_" + 1, ""));
                Log.e(HomeView.LOGTAG, "rem2: " + sharedPreferences.getString("chip_label_" + 2, ""));
                Log.e(HomeView.LOGTAG, "rem3: " + sharedPreferences.getString("chip_label_" + 3, ""));
                Log.e(HomeView.LOGTAG, "rem4: " + sharedPreferences.getString("chip_label_" + 4, ""));
                Log.e(HomeView.LOGTAG, "rem5: " + sharedPreferences.getString("chip_label_" + 5, ""));

                Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));

                updateWidget(context, intent);

                break;

            case ACTION_REMOVE_CHIP_5:
                editor.putString("chip_label_" + 5, "");
                editor.putInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp);
                editor.apply();

                hideRevealChips(remoteViews, context, 5);

                for (int h = 5; h <= 5; h++) {
                    editor.putString("chip_label_" + (h), sharedPreferences.getString("chip_label_" + (h + 1), ""));
                    editor.putInt("chip_icon_" + (h), sharedPreferences.getInt("chip_icon_" + (h + 1), R.drawable.ic_android_black_24dp));
                    editor.apply();
                }

                Log.e(HomeView.LOGTAG, "rem1: " + sharedPreferences.getString("chip_label_" + 1, ""));
                Log.e(HomeView.LOGTAG, "rem2: " + sharedPreferences.getString("chip_label_" + 2, ""));
                Log.e(HomeView.LOGTAG, "rem3: " + sharedPreferences.getString("chip_label_" + 3, ""));
                Log.e(HomeView.LOGTAG, "rem4: " + sharedPreferences.getString("chip_label_" + 4, ""));
                Log.e(HomeView.LOGTAG, "rem5: " + sharedPreferences.getString("chip_label_" + 5, ""));

                Log.e(HomeView.LOGTAG, "icon1: " + sharedPreferences.getInt("chip_icon_" + 1, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon2: " + sharedPreferences.getInt("chip_icon_" + 2, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon3: " + sharedPreferences.getInt("chip_icon_" + 3, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon4: " + sharedPreferences.getInt("chip_icon_" + 4, R.drawable.ic_android_black_24dp));
                Log.e(HomeView.LOGTAG, "icon5: " + sharedPreferences.getInt("chip_icon_" + 5, R.drawable.ic_android_black_24dp));

                updateWidget(context, intent);

                break;

            case ACTION_SETTINGS:
                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingsIntent);
                break;

            case ACTION_MAPS:

                openApp(context, "com.google.android.apps.maps");

                break;


            case ACTION_MUSIC:

                openApp(context, "com.google.android.music");

                break;

            case ACTION_STORE:

                openApp(context, "com.android.vending");

                break;

            case ACTION_CALC:


                for (PackageInfo pi : packs) {
                    if (pi.packageName.toLowerCase().contains("calcul")) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("appName", pi.applicationInfo.loadLabel(pm));
                        map.put("packageName", pi.packageName);
                        items.add(map);
                    }
                }

                if (items.size() >= 1) {
                    String packageName = (String) items.get(0).get("packageName");
                    Intent calcIntent = pm.getLaunchIntentForPackage(packageName);
                    if (calcIntent != null)
                        context.startActivity(calcIntent);
                } else {
                    Toast.makeText(context, "No Applications Found!", Toast.LENGTH_LONG).show();
                }

                break;

            case ACTION_GALLERY:

                for (PackageInfo pi : packs) {
                    if (pi.packageName.toLowerCase().contains("gallery")) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("appName", pi.applicationInfo.loadLabel(pm));
                        map.put("packageName", pi.packageName);
                        items.add(map);
                    }
                }

                if (items.size() >= 1) {
                    String packageName = (String) items.get(0).get("packageName");
                    Intent calcIntent = pm.getLaunchIntentForPackage(packageName);
                    if (calcIntent != null)
                        context.startActivity(calcIntent);
                } else {
                    Toast.makeText(context, "No Applications Found!", Toast.LENGTH_LONG).show();
                }

                break;

            case ACTION_G_MAIL:

                openApp(context, "com.google.android.gm");

                break;
        }

    }

    public void hideRevealChips(RemoteViews remoteViews, Context context, int i) {

        remoteViews.setViewVisibility(context.getResources().getIdentifier("reminder_entry_" + i, "id", context.getPackageName()), View.GONE);
    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void updateWidget(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), HomeViewWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

}