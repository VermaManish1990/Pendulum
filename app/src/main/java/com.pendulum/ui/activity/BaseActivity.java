package com.pendulum.ui.activity;///*
// *
// *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
// *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
// *
// */
//
//package com.com.taxi.ui.activity;
//
//import android.app.Activity;
//import android.app.Application;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.Window;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResult;
//import com.google.android.gms.location.LocationSettingsStates;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.com.taxi.application.BaseApplication;
//import com.com.taxi.persistence.SharedPrefsUtils;
//import com.com.taxi.ui.IScreen;
//import com.com.taxi.volley.ext.RequestManager;
//
//import java.io.File;
//
//
///**
// * This class is used as base-class for application-base-activity.
// */
//public abstract class BaseActivity extends AppCompatActivity implements IScreen, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    private String LOG_TAG = getClass().getSimpleName();
//    private double mLatitude, mLongitude;
//    public static Location mLastLocation;
//    private GoogleApiClient mGoogleApiClient;
//    private boolean isFirstTime = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AppsFlyerLib.getInstance().setCustomerUserId("4V8YNgiwcdDpMVj5MtuuG6");
//        Genie.getInstance().onActivityStart(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        AppEventsLogger.activateApp(this);
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onResume()");
//        }
//
//        Application application = this.getApplication();
//        if (application instanceof BaseApplication) {
//            BaseApplication baseApplication = (BaseApplication) application;
//            if (baseApplication.isAppInBackground()) {
//                onAppResumeFromBackground();
//            }
//            baseApplication.onActivityResumed();
//        }
//        invalidateOptionsMenu();
//    }
//
//    /**
//     * This callback will be called after onResume if application is being
//     * resumed from background. <br/>
//     * <p/>
//     * Subclasses can override this method to get this callback.
//     */
//    protected void onAppResumeFromBackground() {
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onAppResumeFromBackground()");
//        }
//    }
//
//    /**
//     * This method should be called to force app assume itself not in
//     * background.
//     */
//    public final void setAppNotInBackground() {
//        Application application = this.getApplication();
//        if (application instanceof BaseApplication) {
//            BaseApplication baseApplication = (BaseApplication) application;
//            baseApplication.setAppInBackground(false);
//        }
//    }
//
//    public void replaceFragment(final Fragment fragment, final Bundle bundle, final boolean isAddToBackStack) {
//        if (bundle != null) {
//            fragment.setArguments(bundle);
//        }
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    String backStateName = fragment.getClass().getName();  // new fragment
//                    // check exists or not
//                    boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
//                    Fragment fragmentLocal = getSupportFragmentManager().findFragmentById(R.id.content_frame); // existing fragment
////                    if (fragmentLocal != null && fragmentLocal.getTag().equalsIgnoreCase(backStateName)) {
////                        Log.e("tag", "true");
////                    }
//                    if (!fragmentPopped) { // fragment not in back stack, create
//                        // it.
//                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.content_frame, fragment);
//                        ft.addToBackStack(backStateName);
//                        ft.commit();
//                    }
//
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//
//
////                String tag = fragment.getClass().getSimpleName();
////                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////                Fragment fragmentLocal = getSupportFragmentManager().findFragmentById(R.id.content_frame);
////                if (fragmentLocal != null && fragmentLocal.getTag().equalsIgnoreCase(tag)) {
////                    //((BaseFragment) fragmentLocal).refreshFragment(bundle);
////                    return;
////                }
////
////                ft.replace(R.id.content_frame, fragment, tag);
////                fragment.setRetainInstance(true);
////                if (isAddToBackStack) {
////                    ft.addToBackStack(tag);
////                }
////                try {
////                    ft.commit();
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                    try {
////                        ft.commitAllowingStateLoss();
////                    } catch (Exception e) {
////                    }
////                }
//
//            }
//
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        AppEventsLogger.deactivateApp(this);
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onPause()");
//        }
//
//        Application application = this.getApplication();
//        if (application instanceof BaseApplication) {
//            BaseApplication baseApplication = (BaseApplication) application;
//            baseApplication.onActivityPaused();
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onNewIntent()");
//        }
//    }
//
//    /**
//     * Subclass should over-ride this method to update the UI with response,
//     * this base class promises to call this method from UI thread.
//     *
//     * @param serviceResponse
//     */
//    public abstract void updateUi(final boolean status, final int action, final Object serviceResponse);
//
//    // ////////////////////////////// show and hide ProgressDialog
//
//    private ProgressDialog mProgressDialog;
//
//    /**
//     * Shows a simple native progress dialog<br/>
//     * Subclass can override below two methods for custom dialogs- <br/>
//     * 1. showProgressDialog <br/>
//     * 2. removeProgressDialog
//     *
//     * @param bodyText
//     */
//    public void showProgressDialog(String bodyText, final boolean isRequestCancelable) {
//        try {
//            if (isFinishing()) {
//                return;
//            }
//            if (mProgressDialog == null) {
//                mProgressDialog = new ProgressDialog(BaseActivity.this);
//                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                // mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                mProgressDialog.setCancelable(isRequestCancelable);
//                //mProgressDialog.setCanceledOnTouchOutside(false);
//                mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
//                            return true; //
//                        } else if (keyCode == KeyEvent.KEYCODE_BACK && isRequestCancelable) {
//                            Log.e("Ondailogback", "cancel dailog");
//                            RequestManager.cancelRequest();
//                            dialog.dismiss();
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//            }
//            mProgressDialog.setMessage(bodyText);
//
//            if (!mProgressDialog.isShowing()) {
//                mProgressDialog.show();
//                //mProgressDialog.setContentView(new ProgressBar(this));
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    /**
//     * check android version if its marshmallow
//     */
//    public boolean checkAndroidVersion() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return true;
//        }
//        return false;
//    }
//
//    // check whether app having perimisson
//    public boolean checkAndroidPermission(String permission, int requestCode) {
//
//        boolean result = false;
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                permission)
//                != PackageManager.PERMISSION_GRANTED) {
//
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{permission},
//                    requestCode);
//
//            // Should we show an explanation?
////            // BEGIN_INCLUDE(camera_permission_request)
////            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
////                    Manifest.permission.CAMERA)) {
////                // Provide an additional rationale to the user if the permission was not granted
////                // and the user would benefit from additional context for the use of the permission.
////                // For example if the user has previously denied the permission.
////                Log.i(TAG,
////                        "Displaying camera permission rationale to provide additional context.");
////                Snackbar.make(mLayout, R.string.permission_camera_rationale,
////                        Snackbar.LENGTH_INDEFINITE)
////                        .setAction(R.string.ok, new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                ActivityCompat.requestPermissions(MainActivity.this,
////                                        new String[]{Manifest.permission.CAMERA},
////                                        REQUEST_CAMERA);
////                            }
////                        })
////                        .show();
////            } else {
////
////                // Camera permission has not been granted yet. Request it directly.
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
////                        REQUEST_CAMERA);
////            }
//
//
//            // Should we show an explanation?
////            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
////                    permission)) {
////
////                // Show an expanation to the user *asynchronously* -- don't block
////                // this thread waiting for the user's response! After the user
////                // sees the explanation, try again to request the permission.
////
////                //ToastUtils.showToast(this,"Pemission needed for login");
////
////                ///we can request the permission.
////                ActivityCompat.requestPermissions(this,
////                        new String[]{permission},
////                        requestCode);
////
////            } else {
////
////                // No explanation needed, we can request the permission.
////
////                ActivityCompat.requestPermissions(this,
////                        new String[]{permission},
////                        requestCode);
////
////                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
////                // app-defined int constant. The callback method gets the
////                // result of the request.
////            }
//        } else {
//            result = true;
//        }
//        return result;
//    }
//
//
//    public void showSnackBar(View view, String message) {
//        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
//    }
//
//
//    /**
//     * Removes the simple native progress dialog shown via showProgressDialog <br/>
//     * Subclass can override below two methods for custom dialogs- <br/>
//     * 1. showProgressDialog <br/>
//     * 2. removeProgressDialog
//     */
//    public void removeProgressDialog() {
//        try {
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void getLocation() {
//        if (mGoogleApiClient != null) {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                    mGoogleApiClient);
//
//            if (mLastLocation != null) {
//                mLatitude = (mLastLocation.getLatitude());
//                mLongitude = (mLastLocation.getLongitude());
//                if (isFirstTime) {
//                    SharedPrefsUtils.setSharedPrefString(this, Constant.PREF_FILE_NAME, Constant.PREF_LATITUDE, "" + mLatitude);
//                    SharedPrefsUtils.setSharedPrefString(this, Constant.PREF_FILE_NAME, Constant.PREF_LONGITUDE, "" + mLongitude);
//                }
//                SharedPrefsUtils.setSharedPrefString(this, Constant.PREF_FILE_NAME, Constant.PREF_CURRENT_LATITUDE, "" + mLatitude);
//                SharedPrefsUtils.setSharedPrefString(this, Constant.PREF_FILE_NAME, Constant.PREF_CURRENT_LONGITUDE, "" + mLongitude);
//                boolean value = HungerMafiaUtil.getAddressFromLocation(mLatitude, mLongitude, this, this);
//            } else {
//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this)
//                        .addApi(LocationServices.API)
//                        .build();
//                mGoogleApiClient.connect();
//
//            }
//        } else {
//
//        }
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        getLocation();
//    }
//
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//
//    public void getMarshMallowLocation() {
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//        mGoogleApiClient.connect();
//        ((SplashActivity) this).navigateToScreen();
//    }
//
//
//    public void getLocationDialog(final Activity activity, boolean isFirst) {
//        isFirstTime = isFirst;
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//        mGoogleApiClient.connect();
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(30 * 1000);
//        locationRequest.setFastestInterval(5 * 1000);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//        //**************************
//        builder.setAlwaysShow(true); //this is the key ingredient
//        //**************************
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can initialize location
//                        // requests here.
//                        Log.e("Manish", "inside success");
//                        if (activity instanceof SplashActivity)
//                            ((SplashActivity) activity).navigateToScreen();
//                        else if (activity instanceof SearchActivity) {
//                            // first check geocode value received....
//                            //((SearchActivity) activity).navigateToScreen(true);
//                        }
//
//
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//                        Log.e("Manish", "inside res required");
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(BaseActivity.this, Constant.GPS_DIALOG);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        Log.e("Manish", "inside Unavailable");
//                        break;
//                }
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
//
//        Log.e("on act result", " on act result base  " + requestCode + resultCode);
//        switch (requestCode) {
//
//            case Constant.GPS_DIALOG:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//
//                        // All required changes were successfully made
//                        if (mGoogleApiClient.isConnected()) {
//                            //startLocationUpdates();
//                        }
//                        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                                .addConnectionCallbacks(this)
//                                .addOnConnectionFailedListener(this)
//                                .addApi(LocationServices.API)
//                                .build();
//                        mGoogleApiClient.connect();
//
//                        if (this instanceof SplashActivity)
//                            ((SplashActivity) this).navigateToScreen();
//                        else if (this instanceof SearchActivity) {
//                            //   ((SearchActivity) this).navigateToScreen(true);
//                        }
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        // The user was asked to change settings, but chose not to
//                        // fetch location from network here
//                        if (this instanceof SplashActivity)
//                            ((SplashActivity) this).navigateToScreen();
//                        else if (this instanceof SearchActivity)
//                            ((SearchActivity) this).navigateToScreen(false);
//                        break;
//                    default:
//                        break;
//                }
//                break;
//        }
//    }
//
//    public void setProgressDialog(ProgressDialog dialog) {
//        this.mProgressDialog = dialog;
//    }
//    // ////////////////////////////// show and hide key-board
//
////    @Override
////    public boolean dispatchTouchEvent(MotionEvent event) {
////        View v = getCurrentFocus();
////        boolean ret = super.dispatchTouchEvent(event);
////
////        if (v instanceof EditText) {
////            View w = getCurrentFocus();
////            int scrcoords[] = new int[2];
////            w.getLocationOnScreen(scrcoords);
////            float x = event.getRawX() + w.getLeft() - scrcoords[0];
////            float y = event.getRawY() + w.getTop() - scrcoords[1];
////
////            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
////                KeypadUtils.hideSoftKeypad(this);
////            }
////        }
////        return ret;
////    }
//    //
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        System.out.println("Free Memory");
//        //deleteDirectoryTree(getCacheDir());
//    }
//
//    public static void deleteDirectoryTree(File fileOrDirectory) {
//        if (fileOrDirectory.isDirectory()) {
//            for (File child : fileOrDirectory.listFiles()) {
//                deleteDirectoryTree(child);
//            }
//        }
//
//        fileOrDirectory.delete();
//    }
//}