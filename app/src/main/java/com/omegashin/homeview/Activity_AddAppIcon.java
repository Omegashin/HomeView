package com.omegashin.homeview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_AddAppIcon extends AppCompatActivity {

    static final int APP_PACKAGE_REQUEST = 1;
    static final int ICON_REQUEST = 2;
    TextView PackageName;
    TextView PackageUri;
    ImageView PackageIcon;
    int iconResId;
    String iconResName;
    ApplicationInfo applicationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app_icon);

        FloatingActionButton done =(FloatingActionButton) findViewById(R.id.done_app_icon_add);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnAppIcon();
            }
        });

        PackageName = (TextView) findViewById(R.id.package_name);
        PackageUri = (TextView) findViewById(R.id.package_uri);
        PackageIcon = (ImageView) findViewById(R.id.package_icon);

    }

    public void ReturnAppIcon(){

        if(!PackageName.getText().toString().equals(getResources().getString(R.string.label_app_name)) && !PackageIcon.getDrawable().equals(ContextCompat.getDrawable(getBaseContext(),R.drawable.app_android))) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("appInfo", applicationInfo);
            //returnIntent.putExtra("icon", iconResId);
            returnIntent.putExtra("iconName", iconResName);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else{
            Toast.makeText(getBaseContext(),"Please specify the app and icon!",Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenPackageList(View view){
        Intent intent = new Intent(this, Activity_PackageList.class);
        startActivityForResult(intent,APP_PACKAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_PACKAGE_REQUEST) {

            if (resultCode == RESULT_OK) {
                PackageManager packageManager = getPackageManager();

                applicationInfo = data.getParcelableExtra("appInfoFromList");

                PackageName.setText(packageManager.getApplicationLabel(applicationInfo).toString());
                PackageUri.setText(applicationInfo.packageName);

            }
        }

        if(requestCode == ICON_REQUEST){

            if (resultCode == RESULT_OK) {
                //iconResId = data.getIntExtra("icon", 0);
                iconResName = data.getStringExtra("iconName");
                PackageIcon.setImageResource(getResources().getIdentifier(iconResName, "drawable", getPackageName()));
            }
        }

    }

    public void OpenIconReturn(View view) {
        Intent intent = new Intent(this, Activity_IconReturn.class);
        startActivityForResult(intent, ICON_REQUEST);
    }
}
