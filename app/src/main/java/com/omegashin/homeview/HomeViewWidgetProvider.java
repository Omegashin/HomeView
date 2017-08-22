package com.omegashin.homeview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeViewWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_CLICK_APP = "com.omegashin.homeview.APP_OPEN";
    public static final String ACTION_ADD_REMINDER = "com.omegashin.homeview.ADD_REMINDER";
    public static final String ACTION_CLICK_REMINDER = "com.omegashin.homeview.CLICK_REMINDER";
    public static final String APP_POSITION = "com.omegashin.homeview.APP_POSITION";
    public static final String REMINDER_POSITION = "com.omegashin.homeview.REMINDER_POSITION";
    public static final String ACTION_SHOW_ALARMS = "com.omegashin.homeview.ACTION_SHOW_ALARMS";
    public static final String ACTION_CALENDER = "com.omegashin.homeview.ACTION_CALENDER";

    Context context;
    int[] appWidgetIds;
    AppWidgetManager appWidgetManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        this.context = context;


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.hvi_dark);

        if (sharedPreferences.getString("theme", "dark").equals("dark")) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.hvi_dark);
        } else if (sharedPreferences.getString("theme", "dark").equals("light")) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.hvi_light);
        } else if (sharedPreferences.getString("theme", "dark").equals("transparent")) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.hvi_clear);
        }

        ComponentName thisWidget = new ComponentName(context, HomeViewWidgetProvider.class);
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;

        //Set Next Alarm Info

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager.getNextAlarmClock() != null) {
            long nextAlarmTime = alarmManager.getNextAlarmClock().getTriggerTime();
            Date nextAlarmDate = new Date(nextAlarmTime);
            SimpleDateFormat fmtOut = new SimpleDateFormat("EEE @ h:mm a", Locale.ENGLISH);
            fmtOut.format(nextAlarmDate);
            //String nextAlarm = Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
            remoteViews.setTextViewText(R.id.alarm_parent, "Alarm - " + fmtOut.format(nextAlarmDate));
        } else {
            remoteViews.setTextViewText(R.id.alarm_parent, context.getResources().getString(R.string.label_no_alarms));
        }
        remoteViews.setTextViewText(R.id.label_home, TimeZone.getDefault().getDisplayName());

        //Set Foreign Clock Time
        String foreignTimeZonePref = sharedPreferences.getString("foreignTimeZone", "");
        remoteViews.setTextViewText(R.id.foreign_label,foreignTimeZonePref.substring(foreignTimeZonePref.indexOf("/")+1));

        remoteViews.setString(R.id.clock_foreign_main, "setTimeZone", foreignTimeZonePref);
        remoteViews.setString(R.id.clock_foreign_meridiem, "setTimeZone", foreignTimeZonePref);
        remoteViews.setString(R.id.clock_foreign_date, "setTimeZone", foreignTimeZonePref);

        //final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        //final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
//
        //Palette palette = Palette.generate(((BitmapDrawable) (wallpaperDrawable)).getBitmap());
//
        //Log.e("color: ", String.valueOf(palette.getDarkMutedColor(context.getResources().getColor(R.color.blue_300,context.getTheme()))));
//
        //int darkVibrant = palette.getVibrantColor(context.getResources().getColor(R.color.blue_300,context.getTheme()));
        //int lightVibrant = palette.getDarkMutedColor(context.getResources().getColor(R.color.blue_300,context.getTheme()));
        //int col = palette.getVibrantColor(context.getResources().getColor(R.color.blue_300,context.getTheme()));
//
//
        //remoteViews.setInt(R.id.alarm_parent, "setBackgroundColor", lightVibrant);
        //remoteViews.setInt(R.id.clock_parent, "setBackgroundColor", lightVibrant);
        //remoteViews.setInt(R.id.apps_gridview, "setBackgroundColor", lightVibrant);
        //remoteViews.setInt(R.id.reminders_gridview, "setBackgroundColor", lightVibrant);
        //remoteViews.setInt(R.id.add_reminder, "setBackgroundColor", lightVibrant);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int appWidgetId : allWidgetIds) {

            if (sharedPreferences.getBoolean("showClock" + appWidgetId, false)) {
                remoteViews.setViewVisibility(R.id.clock_parent, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.clock_parent, View.GONE);
            }

            if (sharedPreferences.getBoolean("showApps" + appWidgetId, false)) {
                remoteViews.setViewVisibility(R.id.apps_gridview, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.apps_gridview, View.GONE);
            }

            if (sharedPreferences.getBoolean("showReminders" + appWidgetId, false)) {
                remoteViews.setViewVisibility(R.id.reminders_parent, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.reminders_parent, View.GONE);
            }

            Intent svcIntent = new Intent(context, AppGridService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent svc2Intent = new Intent(context, ReminderGridService.class);
            svc2Intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svc2Intent.setData(Uri.parse(svc2Intent.toUri(Intent.URI_INTENT_SCHEME)));

            remoteViews.setRemoteAdapter(R.id.apps_gridview, svcIntent);

            Intent clickIntent = new Intent(context, HomeViewWidgetProvider.class);
            clickIntent.setAction(HomeViewWidgetProvider.ACTION_CLICK_APP);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            clickIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent clickPI = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
            remoteViews.setPendingIntentTemplate(R.id.apps_gridview, clickPI);

            remoteViews.setRemoteAdapter(R.id.reminders_gridview, svc2Intent);

            Intent reminderGridIntent = new Intent(context, HomeViewWidgetProvider.class);
            reminderGridIntent.setAction(HomeViewWidgetProvider.ACTION_CLICK_REMINDER);
            reminderGridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            reminderGridIntent.setData(Uri.parse(svc2Intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent ReminderClickPI = PendingIntent.getBroadcast(context, 0, reminderGridIntent, 0);
            remoteViews.setPendingIntentTemplate(R.id.reminders_gridview, ReminderClickPI);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.apps_gridview);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.reminders_gridview);

            setOnClickIntent(remoteViews, R.id.alarm_parent, ACTION_SHOW_ALARMS);
            setOnClickIntent(remoteViews, R.id.home_clock, ACTION_CALENDER);
            setOnClickIntent(remoteViews, R.id.add_reminder, ACTION_ADD_REMINDER);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public void setOnClickIntent(RemoteViews remoteViews, int resId, String intentActionName) {

        Intent intent = new Intent(context, HomeViewWidgetProvider.class);
        intent.setAction(intentActionName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(resId, pendingIntent);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        switch (intent.getAction()) {


            case ACTION_CLICK_APP:
                int viewIndex = intent.getIntExtra(APP_POSITION, 0);
                Type type = new TypeToken<List<AppIcon>>() {
                }.getType();
                ArrayList<AppIcon> list = gson.fromJson(sharedPreferences.getString("appIconsData", "[]"), type);

                openApp(context, list.get(viewIndex).getAppPackage());
                break;

            case ACTION_ADD_REMINDER:
                Intent reminderAddIntent = new Intent(context, Activity_AddReminder.class);
                reminderAddIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                reminderAddIntent.putExtra("isEdit", false);
                context.startActivity(reminderAddIntent);
                break;

            case ACTION_CLICK_REMINDER:
                if(intent.getExtras().getString(context.getPackageName()+".type",null)==null) {
                    Intent reminderClickIntent = new Intent(context, Activity_AddReminder.class);
                    reminderClickIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    reminderClickIntent.putExtra("isEdit", true);
                    reminderClickIntent.putExtra("index", intent.getIntExtra(REMINDER_POSITION, 0));
                    context.startActivity(reminderClickIntent);
                }
                else{

                    ArrayList<Reminder> reminders = new ArrayList<>();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Type type_reminder = new TypeToken<List<Reminder>>() {
                    }.getType();

                    if (!(sharedPreferences.getString("remindersData", "")).equals("")) {
                        reminders = gson.fromJson(sharedPreferences.getString("remindersData", ""), type_reminder);
                    }
                    reminders.remove(intent.getExtras().getInt(REMINDER_POSITION,0));
                    editor.putString("remindersData", gson.toJson(reminders));
                    editor.apply();

                    Intent updateIntent = new Intent(context, HomeViewWidgetProvider.class);
                    updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, HomeViewWidgetProvider.class));
                    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    context.sendBroadcast(updateIntent);
                }
                break;


            case ACTION_SHOW_ALARMS:
                Intent intent2 = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
                break;

            case ACTION_CALENDER:
                Intent calenderIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/"));
                calenderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(calenderIntent);
                break;

            case AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED:

                Intent intent1 = new Intent(context, HomeViewWidgetProvider.class);
                intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context.getApplicationContext(), HomeViewWidgetProvider.class));
                intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                context.sendBroadcast(intent1);
                break;
        }

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

    public void updateWidget(Context context) {
        Intent updateIntent = new Intent(context, HomeViewWidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context.getApplicationContext(), HomeViewWidgetProvider.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(updateIntent);
    }

}