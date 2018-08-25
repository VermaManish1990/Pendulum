package com.pend.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.pend.R;
import com.pend.util.LoggerUtil;
import com.pendulum.ui.IScreen;


public class ContestType2DialogFragment extends DialogFragment implements IScreen, View.OnClickListener {
    private static final String TAG = ContestType2DialogFragment.class.getSimpleName();

    public static ContestType2DialogFragment newInstance() {
        ContestType2DialogFragment fragment = new ContestType2DialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contest_type2_dialog, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        initUI(view);
        setInitialData();
        return view;
    }

    private void setInitialData() {

    }

    private void initUI(View view) {

        view.findViewById(R.id.bt_okay).setOnClickListener(this);
        view.findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }

    @Override
    public void onAuthError() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_okay:
                this.dismiss();
                break;

            case R.id.bt_cancel:
                this.dismiss();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
