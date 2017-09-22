package com.example.facebookexample.utility;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 * Created by sarabjjeet on 9/22/17.
 */

public class AppInitials extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
    }
}
