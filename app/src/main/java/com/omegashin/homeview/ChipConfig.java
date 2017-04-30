package com.omegashin.homeview;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ChipConfig extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    GridView chipIconsGrid;
    ReminderIconGridAdapter chipIconGridAdapter;
    int IconSelectPosition = -1;

    int[] imageId = {
            R.drawable.ic_person_black_24dp,
            R.drawable.ic_phone_black_24dp,
            R.drawable.ic_delete_black_24dp,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.ic_explore_black_24dp,
            R.drawable.ic_get_app_black_24dp,
            R.drawable.ic_lightbulb_outline_black_24dp,
            R.drawable.ic_markunread_mailbox_black_24dp,
            R.drawable.ic_print_black_24dp,
            R.drawable.ic_question_answer_black_24dp,
            R.drawable.ic_shopping_basket_black_24dp,
            R.drawable.ic_shopping_cart_black_24dp,
            R.drawable.ic_music_note_black_24dp,
            R.drawable.ic_new_releases_black_24dp,
            R.drawable.ic_mail_black_24dp,
            R.drawable.ic_dialpad_black_24dp,
            R.drawable.ic_place_black_24dp,
            R.drawable.ic_photo_black_24dp,
            R.drawable.ic_headset_black_24dp,
            R.drawable.ic_games_black_24dp,
            R.drawable.ic_laptop_mac_black_24dp,
            R.drawable.ic_tv_black_24dp,
            R.drawable.ic_picture_as_pdf_black_24dp,
            R.drawable.ic_local_dining_black_24dp,
            R.drawable.ic_local_laundry_service_black_24dp,
            R.drawable.ic_book_black_24dp,
            R.drawable.ic_local_movies_black_24dp,
            R.drawable.ic_group_black_24dp,
            R.drawable.ic_share_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_mode_edit_black_24dp,
            R.drawable.ic_chrome_reader_mode_black_24dp,
            R.drawable.ic_adb_black_24dp,
            R.drawable.ic_account_balance_wallet_black_24dp,
            R.drawable.ic_announcement_black_24dp,
            R.drawable.ic_bug_report_black_24dp,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chip_config);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_chip_button);
        final EditText reminderName = (EditText) findViewById(R.id.chip_label);

        reminderName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    add.performClick();
                }
                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IconSelectPosition == -1) {
                    Toast.makeText(getBaseContext(), "Specify an Icon!!", Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(getBaseContext(), HomeViewWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                    int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds((new ComponentName(getApplication(), HomeViewWidgetProvider.class)));

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    for (int i = 1; i <= 5; i++) {

                        if (sharedPreferences.getString("chip_label_" + i, "").equals("")) {

                            editor.putString("chip_label_" + i, reminderName.getText().toString().trim());
                            editor.apply();

                            if (sharedPreferences.getInt("chip_icon_" + i, R.drawable.ic_android_black_24dp) == R.drawable.ic_android_black_24dp) {

                                editor.putInt("chip_icon_" + i, imageId[IconSelectPosition]);
                                editor.apply();

                            }

                            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

                            sendBroadcast(intent);

                            break;
                        }
                        if (sharedPreferences.getInt("chip_icon_" + i, R.drawable.ic_add_alert_black_24dp) == R.drawable.ic_add_alert_black_24dp) {
                            editor.putInt("chip_icon_" + i, imageId[IconSelectPosition]);
                            editor.apply();
                        }
                    }

                    finish();

                }
            }
        });

        chipIconsGrid = (GridView) findViewById(R.id.reminder_icon_grid_view);
        chipIconGridAdapter = new ReminderIconGridAdapter(this, imageId);

        chipIconsGrid.setAdapter(chipIconGridAdapter);

        chipIconsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                IconSelectPosition = position;
                chipIconsGrid.getSelectedItem();

            }
        });
    }

}
