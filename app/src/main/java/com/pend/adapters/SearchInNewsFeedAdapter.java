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
import com.pend.models.SearchInNewsFeedResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchInNewsFeedAdapter extends RecyclerView.Adapter<SearchInNewsFeedAdapter.ViewHolder> {

    private static final String TAG = SearchInNewsFeedAdapter.class.getSimpleName();
    private final Context mContext;
    private IMirrorSearchAdapterCallBack mIMirrorSearchAdapterCallBack;
    private ArrayList<SearchInNewsFeedResponseModel.MirrorDetails> mSearchDataList;

    public SearchInNewsFeedAdapter(Context context, ArrayList<SearchInNewsFeedResponseModel.MirrorDetails> searchDataList) {
        mContext = context;
        mSearchDataList = searchDataList;
        mIMirrorSearchAdapterCallBack = (IMirrorSearchAdapterCallBack) context;
    }

    public void setSearchDataList(ArrayList<SearchInNewsFeedResponseModel.MirrorDetails> searchDataList) {
        this.mSearchDataList = searchDataList;
    }

    @NonNull
    @Override
    public SearchInNewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mirror_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchInNewsFeedAdapter.ViewHolder holder, final int position) {
        SearchInNewsFeedResponseModel.MirrorDetails mirrorDetails = mSearchDataList.get(position);

        holder.tvName.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");

        if (mirrorDetails.imageURL != null && !mirrorDetails.imageURL.equals("")) {

            Picasso.with(mContext)
                    .load(mirrorDetails.imageURL != null ? mirrorDetails.imageURL : "")
                    .into(holder.ivProfile);
        }

        if (mirrorDetails.mirrorAdmire || mirrorDetails.mirrorHate || mirrorDetails.mirrorCantSay) {
            holder.ivVote.setVisibility(View.VISIBLE);
        } else {
            holder.ivVote.setVisibility(View.GONE);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIMirrorSearchAdapterCallBack.onMirrorClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchDataList != null ? mSearchDataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final ImageView ivProfile;
        private final ImageView ivVote;
        private final TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            ivVote = itemView.findViewById(R.id.iv_vote);
        }
    }

    public interface IMirrorSearchAdapterCallBack {
        void onMirrorClick(int position);
    }
}
