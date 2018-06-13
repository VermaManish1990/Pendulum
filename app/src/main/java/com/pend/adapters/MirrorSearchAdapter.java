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
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MirrorSearchAdapter extends RecyclerView.Adapter<MirrorSearchAdapter.ViewHolder> {

    private static final String TAG = MirrorSearchAdapter.class.getSimpleName();
    private final Context mContext;
    private ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> mSearchDataList;

    public MirrorSearchAdapter(Context context, ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> searchDataList) {
        mContext = context;
        mSearchDataList = searchDataList;
    }

    public void setSearchDataList(ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> searchDataList) {
        this.mSearchDataList = searchDataList;
    }

    @NonNull
    @Override
    public MirrorSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mirror_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MirrorSearchAdapter.ViewHolder holder, int position) {
        SearchMirrorResponseModel.SearchMirrorDetails mirrorDetails = mSearchDataList.get(position);

        holder.tvName.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");

        Picasso.with(mContext)
                .load(mirrorDetails.imageUrl != null ? mirrorDetails.imageUrl : "")
                .into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return mSearchDataList != null ? mSearchDataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final ImageView ivProfile;
        private final TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
