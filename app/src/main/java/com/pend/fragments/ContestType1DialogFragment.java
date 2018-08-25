package com.pend.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.pend.R;
import com.pend.util.LoggerUtil;


public class ContestType1DialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = ContestType2DialogFragment.class.getSimpleName();


    public static ContestType1DialogFragment newInstance() {
        ContestType1DialogFragment fragment = new ContestType1DialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contest_type1_dialog, container, false);
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
