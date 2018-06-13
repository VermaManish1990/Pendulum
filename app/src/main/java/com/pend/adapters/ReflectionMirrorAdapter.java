package com.pend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetReflectionUsersResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReflectionMirrorAdapter extends BaseAdapter {

    private ArrayList<GetReflectionUsersResponseModel.GetReflectionUsersDetails> mUserDataList;
    private final Context mContext;

    public ReflectionMirrorAdapter(Context context, ArrayList<GetReflectionUsersResponseModel.GetReflectionUsersDetails> userDataList) {
        mContext = context;
        mUserDataList = userDataList;
    }

    public void setUserDataList(ArrayList<GetReflectionUsersResponseModel.GetReflectionUsersDetails> userDataList){
        mUserDataList = userDataList;
    }

    @Override
    public int getCount() {
        return mUserDataList != null ? mUserDataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mUserDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        if (convertView == null) {
            grid = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_reflection_mirror_item, parent, false);
        } else {
            grid = convertView;
        }

        bindData(grid, position);

        return grid;
    }

    /**
     * Method is used to bind data with UI.
     *
     * @param view     view
     * @param position position
     */
    private void bindData(View view, int position) {
        GetReflectionUsersResponseModel.GetReflectionUsersDetails reflectionUsersDetails = mUserDataList.get(position);

        View rootView = view.findViewById(R.id.root_view);
        TextView tvName = view.findViewById(R.id.tv_name);
        ImageView ivMessage = view.findViewById(R.id.iv_message);
        ImageView ivProfile = view.findViewById(R.id.iv_profile);

        tvName.setText(reflectionUsersDetails.userFullName != null ? reflectionUsersDetails.userFullName : "");
//        Picasso.with(mContext)
//                .load("")
//                .resize(360,360)
//                .into(ivProfile);
    }
}
