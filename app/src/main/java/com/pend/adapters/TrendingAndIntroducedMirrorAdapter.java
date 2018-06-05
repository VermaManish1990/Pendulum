package com.pend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrendingAndIntroducedMirrorAdapter extends RecyclerView.Adapter<TrendingAndIntroducedMirrorAdapter.ViewHolder> {
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mMirrorList;
    private Context mContext;
    private final String TAG = TrendingAndIntroducedMirrorAdapter.class.getSimpleName();

    public TrendingAndIntroducedMirrorAdapter(Context context,
                                              ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mirrorList) {
        mContext = context;
        mMirrorList = mirrorList;
    }

    public void setMirrorList(ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mirrorList) {
        this.mMirrorList = mirrorList;
    }

    public ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> getMirrorList() {
        return mMirrorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tranding_and_introduced_mirror_item, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails mirrorDetails = mMirrorList.get(position);

        Picasso.with(mContext)
                .load(mirrorDetails.imageURL)
                .resize(480,480)
                .into(holder.ivProfile);

        holder.tvName.setText(mirrorDetails.mirrorName);
        holder.tvPostCount.setText(mirrorDetails.newPost);
    }

    @Override
    public int getItemCount() {
        return mMirrorList != null ? mMirrorList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final ImageView ivProfile;
        private final TextView tvName;
        private final TextView tvPostCount;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPostCount = itemView.findViewById(R.id.tv_post_count);
        }
    }
}
