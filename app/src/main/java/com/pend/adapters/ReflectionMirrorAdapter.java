package com.pend.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.arena.view.ChatActivity;
import com.pend.interfaces.Constants;
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

    public void setUserDataList(ArrayList<GetReflectionUsersResponseModel.GetReflectionUsersDetails> userDataList) {
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
        final GetReflectionUsersResponseModel.GetReflectionUsersDetails reflectionUsersDetails = mUserDataList.get(position);

        View rootView = view.findViewById(R.id.root_view);
        TextView tvName = view.findViewById(R.id.tv_name);
        ImageView ivMessage = view.findViewById(R.id.iv_message);
        ImageView ivProfile = view.findViewById(R.id.iv_profile);

        if (reflectionUsersDetails.mirrorAdmire) {
            ivMessage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_green));
        } else if (reflectionUsersDetails.mirrorHate) {
            ivMessage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_red));
        } else if (reflectionUsersDetails.mirrorCantSay) {
            ivMessage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_yellow));
        } else {
            ivMessage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message));
        }

        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SELECTED_USER_ID, reflectionUsersDetails.userID);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        tvName.setText(reflectionUsersDetails.userFullName != null ? reflectionUsersDetails.userFullName : "");
        Picasso.with(mContext)
                .load("https://cdn.pixabay.com/photo/2016/06/18/17/42/image-1465348_960_720.jpg")
                .into(ivProfile);
    }
}
