package com.omegashin.homeview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Activity_IconReturn extends AppCompatActivity {

    //ArrayList<Integer> ResIds = new ArrayList<>();
    ArrayList<String> ResNames = new ArrayList<>();
    GridView gridView;
    int position;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_return);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        gridView = (GridView) findViewById(R.id.icon_return_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Activity_IconReturn.this.position = position;
                ReturnIcon();
            }
        });

        LoadIconsTask loadIconsTask = new LoadIconsTask();
        loadIconsTask.execute();

    }

    public void ReturnIcon() {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("icon", ResIds.get(position));
        returnIntent.putExtra("iconName", ResNames.get(position));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    private class LoadIconsTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            Field[] drawablesFields = R.drawable.class.getFields();

            for (Field field : drawablesFields) {
                try {
                    if (field.getName().startsWith("app_")) {

                        //ResIds.add(field.getInt(null));
                        ResNames.add(field.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);

            Collections.sort(ResNames, new AlphaCompare());

            Adapter_IconReturn adapterAddReminder = new Adapter_IconReturn(getBaseContext(), ResNames);

            gridView.setAdapter(adapterAddReminder);

            progressBar.setVisibility(View.GONE);
        }
    }

    private class AlphaCompare implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }

}
