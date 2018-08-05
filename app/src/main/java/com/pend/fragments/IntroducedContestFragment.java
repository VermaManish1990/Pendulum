package com.pend.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.BaseFragment;
import com.pend.R;
import com.pend.activity.contest.CreateContestType1Activity;
import com.pend.activity.contest.CreateContestType2Activity;
import com.pend.adapters.ContestAdapter;
import com.pend.interfaces.Constants;
import com.pend.models.ContestResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;

public class IntroducedContestFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = IntroducedContestFragment.class.getSimpleName();
    private View mRootView;
    private TextView mTvDataNotAvailable;
    private RecyclerView mRecyclerViewIntroduced;
    private BaseActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduced_contest, container, false);

        initUI(view);
        setInitialData();

        return view;
    }

    @Override
    protected void initUI(View view) {

        mRootView = view.findViewById(R.id.root_view);
        mTvDataNotAvailable = view.findViewById(R.id.tv_data_not_available);
        mRecyclerViewIntroduced = view.findViewById(R.id.recycler_view_introduced);
        view.findViewById(R.id.bt_create_contest).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        mRecyclerViewIntroduced.setVisibility(View.VISIBLE);
        mTvDataNotAvailable.setVisibility(View.GONE);
        ArrayList<ContestResponseModel.ContestDetails> contestDetailsList = new ArrayList<>();
        contestDetailsList.add(new ContestResponseModel.ContestDetails(1, 40, 60, 0));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(2, 40, 25, 35));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(1, 70, 30, 0));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(2, 20, 50, 30));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(2, 60, 10, 30));

        contestDetailsList.addAll(contestDetailsList);
        contestDetailsList.addAll(contestDetailsList);
        contestDetailsList.addAll(contestDetailsList);

        mRecyclerViewIntroduced.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewIntroduced.setAdapter(new ContestAdapter(mContext, contestDetailsList));
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
            case R.id.bt_create_contest:
                selectContestDialog();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    /**
     * Method is used to show an Alert Dialog for select Contest.
     */
    private void selectContestDialog() {
        final CharSequence[] items = new String[]{getString(R.string.type_1_contest), getString(R.string.type_2_contest), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.create_contest));

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (items[which].toString()) {
                    case "TYPE 1 Contest":
                        Intent intentContest1 = new Intent(mContext, CreateContestType1Activity.class);
                        startActivity(intentContest1);
                        break;

                    case "TYPE 2 Contest":
                        Intent intentContest2 = new Intent(mContext, CreateContestType2Activity.class);
                        startActivity(intentContest2);
                        break;

                    case "Cancel":
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }
}
