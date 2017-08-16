package com.omegashin.homeview;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

public class WidgetSettings extends PreferenceActivity {

    public static List<ApplicationInfo> applicationInfoList;

    static final int PICK_TZ_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Banner Ad

            //MobileAds.initialize(getActivity(), "ca-app-pub-8920354294521666~4415936833");
            //NativeExpressAdView adView = (NativeExpressAdView) getActivity().findViewById(R.id.adView);
            //adView.loadAd(new AdRequest.Builder().build());

            Preference prefTimezone = findPreference("pref_timezone");
            Preference prefShortcuts = findPreference("pref_shortcuts");
            Preference prefTheme = findPreference("pref_theme");
            Preference prefDev = findPreference("pref_dev");

            prefTimezone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(getActivity(), Activity_HomeViewConfig.class);
                    intent.putExtra("IntentCaller", "Activity_WidgetSettings");
                    startActivityForResult(intent, PICK_TZ_REQUEST);

                    return true;
                }
            });

            prefShortcuts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(getActivity(), Activity_AppIconsList.class);
                    startActivityForResult(intent, PICK_TZ_REQUEST);

                    return true;
                }
            });

            prefTheme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    // custom dialog
                    final Dialog dialog = new Dialog(getActivity());
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

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    RadioButton radioButtonDark = dialog.findViewById(R.id.theme_dark);
                    RadioButton radioButtonLight = dialog.findViewById(R.id.theme_light);
                    RadioButton radioButtonTransparent = dialog.findViewById(R.id.theme_transparent);

                    switch (sharedPreferences.getString("theme", "dark")) {
                        case "dark":
                            radioButtonDark.setChecked(true);
                            break;
                        case "light":
                            radioButtonLight.setChecked(true);
                            break;
                        case "transparent":
                            radioButtonTransparent.setChecked(true);
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
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                            sharedPreferences.edit().putString("theme", radioButton.getText().toString().toLowerCase().split(" ")[0]).apply();

                            Intent updateIntent = new Intent(getActivity(), HomeViewWidgetProvider.class);
                            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            int ids[] = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(), HomeViewWidgetProvider.class));
                            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                            getActivity().sendBroadcast(updateIntent);

                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return true;
                }
            });

            prefDev.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(getActivity(), Activity_DeveloperInfo.class);
                    startActivityForResult(intent, PICK_TZ_REQUEST);

                    return true;
                }
            });
        }
    }

}