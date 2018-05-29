
/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Me. to use this code.
 *
 */

package com.pendulum.application;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.pend.BuildConfig;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class holds some application-global instances.
 */
public abstract class BaseApplication extends MultiDexApplication {

    private static final String LOG_TAG = "BaseApplication";
    public static boolean IS_LOG_ENABLE = true;
//    private DBHelper mBaseDbHelper;
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean mAppInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;
    public boolean isCollectionFilterApply = false;
    private static HashSet<Integer> mRestaurantBookmarkList = new HashSet<>();
    public int position=0;
    public String rating="";

    public static HashSet<Integer> getmRestaurantBookmarkList() {
        return mRestaurantBookmarkList;
    }

    public static void setmRestaurantBookmarkList(HashSet<Integer> mRestaurantBookmarkList) {
        BaseApplication.mRestaurantBookmarkList = mRestaurantBookmarkList;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public boolean isCollectionFilterApply() {
        return isCollectionFilterApply;
    }

    public void setCollectionFilterApply(boolean collectionFilterApply) {
        isCollectionFilterApply = collectionFilterApply;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        com.taxi.application.Foreground.init(this);
        this.initialize();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "onCreate()");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * @return the mIsAppInBackground
     */
    public boolean isAppInBackground() {
        return mAppInBackground;
    }

    /**
     * @param isAppInBackground value to set for mIsAppInBackground
     */
    public void setAppInBackground(boolean isAppInBackground) {
        mAppInBackground = isAppInBackground;
        onAppStateSwitched(isAppInBackground);
    }

    /**
     * @param isAppInBackground
     * @note subclass can override this method for this callback
     */
    protected void onAppStateSwitched(boolean isAppInBackground) {
        // nothing to do here in base application class
    }

    /**
     * should be called from onResume of each activity of application
     */
    public void onActivityResumed() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }
        setAppInBackground(false);
    }

    /**
     * should be called from onPause of each activity of app
     */
    public void onActivityPaused() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                setAppInBackground(true);
                if (BuildConfig.DEBUG) {
                    Log.i(LOG_TAG, "None of our activity is in foreground.");
                }
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask, MAX_ACTIVITY_TRANSITION_TIME_MS);
    }


    protected abstract void initialize();
}