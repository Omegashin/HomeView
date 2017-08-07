package com.omegashin.homeview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gdesi on 11-Jun-17.
 */

class Adapter_AppIcons extends RecyclerView.Adapter<Adapter_AppIcons.AppIconsVH> implements ItemTouchInterface {
    private List<AppIcon> AppIcons;
    private Context context;

    Adapter_AppIcons(Context context, ArrayList<AppIcon> appIcons) {
        this.AppIcons = appIcons;
        this.context = context;
    }

    @Override
    public AppIconsVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app_icons, viewGroup, false);

        return new AppIconsVH(view);
    }

    @Override
    public void onBindViewHolder(AppIconsVH appIconsVH, int i) {

        AppIcon appIcon = AppIcons.get(i);
        //appIconsVH.name.setText(appIcon.getName());
        //appIconsVH.uri.setText(appIcon.getAppPackage());
        //appIconsVH.icon.setImageResource(appIcon.getIcon());
        appIconsVH.icon.setImageResource(context.getResources().getIdentifier(appIcon.getIconName(), "drawable", context.getPackageName()));

    }

    @Override
    public int getItemCount() {
        return (null != AppIcons ? AppIcons.size() : 0);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(AppIcons, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(AppIcons, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        AppIcons.remove(position);
        notifyItemRemoved(position);
    }

    class AppIconsVH extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView icon;
        //TextView name;
        //TextView uri;

        AppIconsVH(View appItem) {
            super(appItem);

            cardView = appItem.findViewById(R.id.cardView);
            icon = appItem.findViewById(R.id.icon);
            //name = appItem.findViewById(R.id.name);
            //uri = appItem.findViewById(R.id.uri);

        }
    }
}
