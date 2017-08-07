package com.omegashin.homeview;

/**
 * Created by gdesi on 03-Jun-17.
 */

class AppIcon {

    private String name = "DashDock";
    private String appPackage = "com.omegashin.homeview";
    private int icon = R.drawable.app_android;
    private String IconName = "app_android";

    String getIconName() {
        return IconName;
    }

    void setIconName(String iconName) {
        IconName = iconName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    String getAppPackage() {
        return appPackage;
    }

    void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }
}
