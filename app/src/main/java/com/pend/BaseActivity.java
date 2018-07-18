package com.pend;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.pend.fragments.QuarterViewFragmentDialog;
import com.pendulum.ui.IScreen;

public abstract class BaseActivity extends AppCompatActivity implements IScreen {
    private Dialog customProgressDialog;
    private QuarterViewFragmentDialog customQuarterDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
           /* requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        }
        super.onCreate(savedInstanceState);
        //opening transition animations
//        overridePendingTransitionEnter();
    }

    /**
     * Method is used to initialize view.
     */
    protected void initUI() {

    }

    /**
     * Method is used to set Initial Data.
     */
    protected void setInitialData() {

    }

    /**
     * Shows a simple native progress dialog<br/>
     * Subclass can override below two methods for custom dialogs- <br/>
     * 1. showProgressDialog <br/>
     * 2. removeProgressDialog
     */
    public void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (customProgressDialog == null) {
            customProgressDialog = new Dialog(this);
            customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            customProgressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            customProgressDialog.setContentView(R.layout.custom_progress_bar);
            customProgressDialog.setCancelable(false);
        }

        if (!customProgressDialog.isShowing()) {
            customProgressDialog.show();
        }
    }

    public void showQuarterViewDialog() {
        if (isFinishing()) {
            return;
        }
        if (customQuarterDialog == null) {
            customQuarterDialog = new QuarterViewFragmentDialog();
            customQuarterDialog.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
        }
    }

    public void hideQuarterViewDialog() {
        if (isFinishing()) {
            return;
        }
        if (customQuarterDialog != null) {
            customQuarterDialog.dismiss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean isOpenQuarterDialog(){
        return customQuarterDialog.isOpen();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openQuarterDialog(){
        customQuarterDialog.showReveal();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void closeQuarterDialog(){
        customQuarterDialog.hideReveal();
    }

    /**
     * Removes the simple native progress dialog shown via showProgressDialog <br/>
     * Subclass can override below two methods for custom dialogs- <br/>
     * 1. showProgressDialog <br/>
     * 2. removeProgressDialog
     */
    public void removeProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }


    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
   /* protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    *//**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     *//*
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
    protected void setUpToolBar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }*/


}
