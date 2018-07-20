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

public class MirrorSearchAdapter extends RecyclerView.Adapter<MirrorSearchAdapter.ViewHolder> {

    private static final String TAG = MirrorSearchAdapter.class.getSimpleName();
    private final Context mContext;
    private IMirrorSearchAdapterCallBack mIMirrorSearchAdapterCallBack;
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mSearchDataList;

    public MirrorSearchAdapter(Context context,IMirrorSearchAdapterCallBack mirrorSearchAdapterCallBack, ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> searchDataList) {
        mContext = context;
        mSearchDataList = searchDataList;
        mIMirrorSearchAdapterCallBack = mirrorSearchAdapterCallBack;
    }

    public void setSearchDataList(ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> searchDataList) {
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
    public void onBindViewHolder(@NonNull MirrorSearchAdapter.ViewHolder holder, final int position) {
        GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails mirrorDetails = mSearchDataList.get(position);

        holder.tvName.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");

        if (mirrorDetails.imageURL != null && !mirrorDetails.imageURL.equals("")) {

            Picasso.with(mContext)
                    .load(mirrorDetails.imageURL != null ? mirrorDetails.imageURL : "")
                    .into(holder.ivProfile);
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
        private final TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface IMirrorSearchAdapterCallBack{
        void onMirrorClick(int position);
    }
}
