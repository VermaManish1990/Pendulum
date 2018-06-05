package com.pend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.squareup.picasso.Picasso;

public class FollowingMirrorAdapter extends BaseAdapter {
    private Context mContext;

    public FollowingMirrorAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            TextView tvName = grid.findViewById(R.id.tv_name);
            ImageView ivProfile = grid.findViewById(R.id.iv_profile);

            tvName.setText("");
            Picasso.with(mContext)
                    .load("")
                    .resize(250,250)
                    .into(ivProfile);
        } else {
            grid = convertView;
        }
        return grid;
    }
}
