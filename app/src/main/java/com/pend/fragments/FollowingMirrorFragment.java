package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.pend.BaseFragment;
import com.pend.R;
import com.pend.adapters.FollowingMirrorAdapter;


public class FollowingMirrorFragment extends BaseFragment {

    private Context mContext;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowingMirrorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingMirrorFragment newInstance(String param1, String param2) {
        FollowingMirrorFragment fragment = new FollowingMirrorFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
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
        return view;
    }

    @Override
    protected void initUI(View view) {
        GridView gridViewFollowingMirror = view.findViewById(R.id.grid_view_following_mirror);
        gridViewFollowingMirror.setAdapter(new FollowingMirrorAdapter(mContext));
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
