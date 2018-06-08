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

public class SearchMirrorAdapter extends RecyclerView.Adapter<SearchMirrorAdapter.ViewHolder> {

    private ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> mSearchDataList;
    private Context mContext;
    private final String TAG = TrendingAndIntroducedMirrorAdapter.class.getSimpleName();

    public SearchMirrorAdapter(Context context, ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> searchDataList) {
        mContext = context;
        mSearchDataList = searchDataList;
    }

    public void setSearchDataList(ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> searchDataList) {
        this.mSearchDataList = searchDataList;
    }

    public ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> getSearchDataList() {
        return mSearchDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_mirror, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchMirrorResponseModel.SearchMirrorDetails searchMirrorDetails = mSearchDataList.get(position);

        holder.tvName.setText(searchMirrorDetails.mirrorName != null ? searchMirrorDetails.mirrorName : "");
        holder.tvLink.setText(searchMirrorDetails.mirrorWikiLink != null ? searchMirrorDetails.mirrorWikiLink : "");

        Picasso.with(mContext)
                .load(searchMirrorDetails.imageUrl != null ? searchMirrorDetails.imageUrl : "")
                .resize(250, 250)
                .into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return mSearchDataList != null ? mSearchDataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvLink;
        private final ImageView ivProfile;
        private final ImageView ivRadioButton;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvLink = itemView.findViewById(R.id.tv_link);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivRadioButton = itemView.findViewById(R.id.iv_radio_button);
        }
    }
}