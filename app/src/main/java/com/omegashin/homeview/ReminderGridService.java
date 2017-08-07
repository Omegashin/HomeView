package com.omegashin.homeview;

/**
 * Created by gdesi on 10-Jun-17.
 */

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class ReminderGridService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ReminderGridRVF(this.getApplicationContext()));
    }
}