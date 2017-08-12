package com.omegashin.homeview;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Activity_WidgetSettings extends AppCompatActivity {

    static final int PICK_TZ_REQUEST = 1;
    TextView currentTZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        currentTZ = (TextView) findViewById(R.id.current_tz);
        String currentTZText = sharedPreferences.getString("foreignTimeZone", "");
        currentTZ.setText(currentTZText.substring(currentTZText.indexOf("/") + 1));

    }

    public void OpenTZList(View view) {
        Intent intent = new Intent(this, Activity_HomeViewConfig.class);
        intent.putExtra("IntentCaller", "Activity_WidgetSettings");
        startActivityForResult(intent, PICK_TZ_REQUEST);
    }

    public void OpenAppIconsList(View view) {
        Intent intent = new Intent(this, Activity_AppIconsList.class);
        startActivityForResult(intent, PICK_TZ_REQUEST);
    }

    @Override
    protected void onPause() {
        super.onPause();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int ids[] = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), HomeViewWidgetProvider.class));
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(updateIntent);
            }
        }, 1000);

    }

    public void OpenThemeDialog(View view) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.theme_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        assert window!=null;
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        Button cancel = dialog.findViewById(R.id.cancel);
        Button apply = dialog.findViewById(R.id.apply);
        final RadioGroup radioGroup = dialog.findViewById(R.id.theme_radio_group);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        RadioButton radioButtonDark = dialog.findViewById(R.id.theme_dark);
        RadioButton radioButtonLight = dialog.findViewById(R.id.theme_light);
        RadioButton radioButtonClear = dialog.findViewById(R.id.theme_clear);

        switch (sharedPreferences.getString("theme", "dark")) {
            case "dark":
                radioButtonDark.setChecked(true);
                break;
            case "light":
                radioButtonLight.setChecked(true);
                break;
            case "clear":
                radioButtonClear.setChecked(true);
                break;
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                sharedPreferences.edit().putString("theme", radioButton.getText().toString().toLowerCase().split(" ")[0]).apply();

                Intent updateIntent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int ids[] = AppWidgetManager.getInstance(getBaseContext()).getAppWidgetIds(new ComponentName(getApplicationContext(), HomeViewWidgetProvider.class));
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(updateIntent);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_TZ_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                currentTZ.setText(data.getStringExtra("tz_selection"));
            }
        }
    }
}