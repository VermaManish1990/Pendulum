package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pend.BaseFragment;
import com.pend.R;
import com.pend.adapters.FollowingMirrorAdapter;
import com.pend.models.GetFollowingMirrorResponseModel;

import java.util.ArrayList;


public class FollowingMirrorFragment extends BaseFragment {

    private Context mContext;
    private View mRootView;
    private GridView mGridViewFollowingMirror;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FollowingMirrorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingMirrorFragment newInstance() {
        FollowingMirrorFragment fragment = new FollowingMirrorFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_following_mirror, container, false);

        initUI(view);
        setInitialData();
        return view;
    }

    @Override
    protected void initUI(View view) {
        mRootView = view.findViewById(R.id.root_view);
        mGridViewFollowingMirror = view.findViewById(R.id.grid_view_following_mirror);
    }

    @Override
    protected void setInitialData() {
        ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mirrorList = new ArrayList<>();
        mGridViewFollowingMirror.setAdapter(new FollowingMirrorAdapter(mContext,mirrorList));

        /*
          On Click event for Single Gridview Item
          */
        mGridViewFollowingMirror.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });

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
}
