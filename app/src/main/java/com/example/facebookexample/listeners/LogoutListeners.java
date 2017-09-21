package com.example.facebookexample.listeners;

import android.app.Activity;

/**
 * Created by sarabjjeet on 9/21/17.
 */

public interface LogoutListeners {
    void googleSignout(Activity activity);
    void facebookSignout();
    void twitterSignout();

}
