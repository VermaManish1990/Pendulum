package com.pend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

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
}
