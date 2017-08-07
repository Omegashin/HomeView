package com.omegashin.homeview;

/**
 * Created by gdesi on 10-Jun-17.
 */

import android.content.Intent;
import android.widget.RemoteViewsService;

public class AppGridService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new AppGridRVF(this.getApplicationContext()));
    }
}