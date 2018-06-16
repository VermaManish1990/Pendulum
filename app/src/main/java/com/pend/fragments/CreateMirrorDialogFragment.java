package com.pend.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.pend.R;
import com.pend.activity.mirror.SearchMirrorListingActivity;
import com.pend.interfaces.Constants;
import com.pend.util.LoggerUtil;

public class CreateMirrorDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = CreateMirrorDialogFragment.class.getSimpleName();
    private EditText mETName;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search_mirror, container, false);

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        mETName = view.findViewById(R.id.et_name);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        view.findViewById(R.id.bt_search).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                getDialog().dismiss();
                break;

            case R.id.bt_search:
                getDialog().dismiss();
                String searchText = mETName.getText().toString().trim();
                Intent intent = new Intent(mContext, SearchMirrorListingActivity.class);
                intent.putExtra(Constants.SEARCH_TEXT_KEY, searchText);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
