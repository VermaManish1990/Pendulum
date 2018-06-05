package com.pend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.pendulum.ui.IScreen;

public abstract class BaseFragment extends Fragment implements IScreen{

    /**
     *
     * @return BaseActivity
     */
    protected BaseActivity getBaseActivity() {
        FragmentActivity activity = getActivity();
        if (!(activity instanceof BaseActivity) || activity.isFinishing()) {
            return null;
        }
        return (BaseActivity) activity;
    }

    /**
     * Method is used to initialize view.
     */
    protected void initUI(View view) {

    }

    /**
     * Method is used to set Initial Data.
     */
    protected void setInitialData() {

    }
}
