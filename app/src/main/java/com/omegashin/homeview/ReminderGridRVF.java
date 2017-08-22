package com.omegashin.homeview;

/**
 * Created by gdesi on 10-Jun-17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class ReminderGridRVF implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;
    private ArrayList<Reminder> data;

    ReminderGridRVF(Context context) {
        this.context = context;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Reminder>>() {
        }.getType();

        data = gson.fromJson(sharedPreferences.getString("remindersData", "[]"), type);
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return (data.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_w_reminder);

        if (position != getCount()) {

            remoteViews.setImageViewResource(R.id.reminder_icon, context.getResources().getIdentifier(data.get(position).getIconName(), "drawable", context.getPackageName()));

            remoteViews.setTextViewText(R.id.label, data.get(position).getLabel());
            Bundle extras = new Bundle();
            extras.putInt(HomeViewWidgetProvider.REMINDER_POSITION, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.reminder_item, fillInIntent);

            Bundle extras2 = new Bundle();
            extras2.putInt(HomeViewWidgetProvider.REMINDER_POSITION, position);
            extras2.putString(context.getPackageName()+".type", "delete");
            Intent fillInIntent2 = new Intent();
            fillInIntent2.putExtras(extras2);
            remoteViews.setOnClickFillInIntent(R.id.delete, fillInIntent2);
        }

        return (remoteViews);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Reminder>>() {
        }.getType();

        data = gson.fromJson(sharedPreferences.getString("remindersData", "[]"), type);

    }
}
