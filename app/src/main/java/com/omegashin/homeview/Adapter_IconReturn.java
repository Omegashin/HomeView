package com.omegashin.homeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gdesi on 21-Jan-17.
 */

class Adapter_IconReturn extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> names;

    Adapter_IconReturn(Context c, ArrayList<String> names) {
        mContext = c;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_icon_return, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_image);
        imageView.setImageResource(mContext.getResources().getIdentifier(names.get(position), "drawable", mContext.getPackageName()));

        return convertView;
    }
}
