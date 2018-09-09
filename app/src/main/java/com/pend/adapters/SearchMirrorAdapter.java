package com.pend.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchMirrorAdapter extends RecyclerView.Adapter<SearchMirrorAdapter.ViewHolder> {

    private ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> mSearchDataList;
    private Context mContext;
    private ISearchMirrorAdapterCallBack mISearchMirrorAdapterCallBack;
    private final String TAG = TrendingAndIntroducedMirrorAdapter.class.getSimpleName();

    public SearchMirrorAdapter(Context context, ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> searchDataList) {
        mContext = context;
        mSearchDataList = searchDataList;
        mISearchMirrorAdapterCallBack = (ISearchMirrorAdapterCallBack) context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SearchMirrorResponseModel.SearchMirrorDetails searchMirrorDetails = mSearchDataList.get(position);

        holder.tvName.setText(searchMirrorDetails.mirrorName != null ? searchMirrorDetails.mirrorName : "");

        final String wikiLink;
        if (searchMirrorDetails.mirrorWikiLink != null &&
                !searchMirrorDetails.mirrorWikiLink.equals("") && !searchMirrorDetails.mirrorWikiLink.equals("NA")) {


            wikiLink = "<a href=" + searchMirrorDetails.mirrorWikiLink + ">Wiki link</a>";
            holder.tvLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(searchMirrorDetails.mirrorWikiLink));
                    mContext.startActivity(intent);
                }
            });
        } else {
            wikiLink = "NA";
        }
        holder.tvLink.setText(Html.fromHtml(wikiLink));

        if (searchMirrorDetails.imageUrl != null && !searchMirrorDetails.imageUrl.equals("")) {

            Picasso.with(mContext)
                    .load(searchMirrorDetails.imageUrl)
                    .resize(250, 250)
                    .into(holder.ivProfile);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mISearchMirrorAdapterCallBack.onMirrorClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchDataList != null ? mSearchDataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvLink;
        private final ImageView ivProfile;
        private final View rootView;
        private final RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLink = itemView.findViewById(R.id.tv_link);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }

    public interface ISearchMirrorAdapterCallBack {
        void onMirrorClick(int position);
    }
}
