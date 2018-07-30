package com.pend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetFollowingMirrorResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingMirrorAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mMirrorList;

    public FollowingMirrorAdapter(Context context, ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mirrorList) {
        mContext = context;
        mMirrorList = mirrorList;
    }

    public void setMirrorList(ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mirrorList) {
        this.mMirrorList = mirrorList;
    }

    @Override
    public int getCount() {
        return mMirrorList != null ? mMirrorList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mMirrorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        if (convertView == null) {
            grid = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_following_mirror_item, parent, false);
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
        GetFollowingMirrorResponseModel.GetFollowingMirrorDetails mirrorDetails = mMirrorList.get(position);

        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvCount = view.findViewById(R.id.tv_count);
        ImageView ivProfile = view.findViewById(R.id.iv_profile);
        ImageView flProfile = view.findViewById(R.id.profile_border);

        tvName.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");
        tvCount.setText(String.valueOf(mirrorDetails.activeUsers));

        if (mirrorDetails.imageURL != null && !mirrorDetails.imageURL.equals("")) {

//            if (mirrorDetails.mirrorAdmire || mirrorDetails.mirrorHate || mirrorDetails.mirrorCantSay) {
//                flProfile.setImageResource(R.color.green);
//            }

            Picasso.with(mContext)
                    .load(mirrorDetails.imageURL)
                    .into(ivProfile);
        }

    }
}
