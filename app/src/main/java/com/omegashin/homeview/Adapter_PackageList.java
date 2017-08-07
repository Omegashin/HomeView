package com.omegashin.homeview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gdesi on 09-Jun-17.
 */

class Adapter_PackageList extends ArrayAdapter<ApplicationInfo> {

    private Context context;
    private ArrayList<ApplicationInfo> mArrayList;
    private ArrayList<ApplicationInfo> mFilteredList = new ArrayList<>();
    private PackageManager packageManager;

    Adapter_PackageList(Context context, ArrayList<ApplicationInfo> applicationInfoList) {
        super(context, 0, applicationInfoList);
        this.context = context;
        mArrayList = applicationInfoList;
        mFilteredList = applicationInfoList;
    }

    @Nullable
    @Override
    public ApplicationInfo getItem(int position) {
        return mFilteredList.get(position);
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        //super.getView(position,convertView,parent);
        packageManager = context.getPackageManager();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_package, parent, false);
        }

        ImageView appIcon = convertView.findViewById(R.id.app_icon);
        TextView appName = convertView.findViewById(R.id.app_name);
        TextView appUri = convertView.findViewById(R.id.app_uri);

        final String name = packageManager.getApplicationLabel(mFilteredList.get(position)).toString();
        final String uri = mFilteredList.get(position).packageName;
        final Drawable icon = packageManager.getApplicationIcon(mFilteredList.get(position));

        appIcon.setImageDrawable(icon);
        appName.setText(name);
        appUri.setText(uri);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                ApplicationInfo applicationInfo = mFilteredList.get(position);
                returnIntent.putExtra("appInfoFromList", applicationInfo);
                ((Activity) context).setResult(Activity.RESULT_OK, returnIntent);
                ((Activity) context).finish();

            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<ApplicationInfo> filteredList = new ArrayList<>();

                    for (ApplicationInfo applicationInfo : mArrayList) {
                        if (packageManager.getApplicationLabel(applicationInfo).toString().toLowerCase().contains(charString.toLowerCase())) {
                            Log.e("ff", packageManager.getApplicationLabel(applicationInfo).toString());
                            filteredList.add(applicationInfo);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ApplicationInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}