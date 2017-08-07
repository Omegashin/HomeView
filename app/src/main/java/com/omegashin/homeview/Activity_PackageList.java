package com.omegashin.homeview;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

import static com.omegashin.homeview.DashDock.applicationInfoList;

public class Activity_PackageList extends AppCompatActivity {

    ListView listView;
    //List<ApplicationInfo> applicationInfoList;
    Adapter_PackageList appsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.apps_list);

        if (applicationInfoList == null) {
            //applicationInfoList = getPackageManager().getInstalledApplications(0);
            LoadPackageListTask loadPackageListTask = new LoadPackageListTask();
            loadPackageListTask.execute();
        } else {
            //Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));
            appsListAdapter = new Adapter_PackageList(Activity_PackageList.this, new ArrayList<>(applicationInfoList));
            listView.setAdapter(appsListAdapter);
        }

    }

    // The definition of our task class
    private class LoadPackageListTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            applicationInfoList = getPackageManager().getInstalledApplications(0);
            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Collections.sort(applicationInfoList, new ApplicationInfo.DisplayNameComparator(getPackageManager()));
            appsListAdapter = new Adapter_PackageList(Activity_PackageList.this, new ArrayList<>(applicationInfoList));
            listView.setAdapter(appsListAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_package_list, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                appsListAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
