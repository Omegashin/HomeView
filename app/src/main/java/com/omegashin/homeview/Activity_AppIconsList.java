package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Activity_AppIconsList extends AppCompatActivity {

    public static final int ADD_APP_ICON_REQUEST = 1;
    Adapter_AppIcons adapterAppIcons;

    ArrayList<AppIcon> appIcons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_icons_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_app_icon_fab);
        FloatingActionButton save = (FloatingActionButton) findViewById(R.id.save_app_icon_fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAppIcon();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<AppIcon>>() {
        }.getType();
        if (!(sharedPreferences.getString("appIconsData", "")).equals("")) {
            appIcons = gson.fromJson(sharedPreferences.getString("appIconsData", ""), type);
        }

        AutoFitRecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recyclerView);
        adapterAppIcons = new Adapter_AppIcons(getBaseContext(), appIcons);
        //recyclerView.setLayoutManager(new GridLayoutManager(this,0));
        recyclerView.setAdapter(adapterAppIcons);

        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapterAppIcons);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }

    public void AddAppIcon() {
        Intent intent = new Intent(this, Activity_AddAppIcon.class);
        startActivityForResult(intent, ADD_APP_ICON_REQUEST);
    }

    public void SaveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("appIconsData", gson.toJson(appIcons));
        editor.apply();

        Intent updateIntent = new Intent(this, HomeViewWidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(getApplicationContext(), HomeViewWidgetProvider.class));
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(updateIntent);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_APP_ICON_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                PackageManager packageManager = getPackageManager();

                AppIcon appIcon = new AppIcon();

                ApplicationInfo applicationInfo = data.getExtras().getParcelable("appInfo");
                //int icon = data.getIntExtra("icon", 0);
                String iconName = data.getStringExtra("iconName");

                if (applicationInfo != null) {
                    appIcon.setName(packageManager.getApplicationLabel(applicationInfo).toString());
                    appIcon.setAppPackage(applicationInfo.packageName);
                }

                /*if(icon!=0) {
                    appIcon.setIcon(icon);
                }*/

                if (!iconName.equals("")) {
                    //appIcon.setIcon(getResources().getIdentifier(iconName, "drawable", getPackageName()));
                    appIcon.setIconName(iconName);
                } else {
                    //appIcon.setIcon(R.drawable.app_android);
                    appIcon.setIconName(iconName);
                }

                appIcons.add(appIcon);
                adapterAppIcons.notifyDataSetChanged();
            }
        }
    }
}