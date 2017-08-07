package com.omegashin.homeview;

/**
 * Created by gdesi on 16-Jul-17.
 */

class Reminder {

    private String label = "";
    private int icon = R.drawable.app_android;
    private String iconName = "app_android";

    String getIconName() {
        return iconName;
    }

    void setIconName(String iconName) {
        this.iconName = iconName;
    }

    String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
