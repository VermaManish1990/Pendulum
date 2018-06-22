package com.pend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.UserTimeSheetResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;

public class TimeSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = TimeSheetAdapter.class.getSimpleName();
    private static final int LOADING = 1;
    private static final int ITEM = 2;
    private Context mContext;
    private boolean isLoadingAdded = false;
    private ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> mTimeSheetDetailsList;

    public TimeSheetAdapter(Context context, ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> timeSheetDetailsList) {
        mTimeSheetDetailsList = timeSheetDetailsList;
        mContext = context;
    }

    public void setTimeSheetDetailsList(ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> timeSheetDetailsList) {
        this.mTimeSheetDetailsList = timeSheetDetailsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");

        if (viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_time_sheet_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_progress_item, parent, false);
            return new ProgressViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserTimeSheetResponseModel.UserTimeSheetDetails timeSheetDetails = mTimeSheetDetailsList.get(position);

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.tvMessage.setText(timeSheetDetails.mirrorName != null ? timeSheetDetails.mirrorName : "");
        }
    }

    @Override
    public int getItemCount() {
        return mTimeSheetDetailsList != null ? mTimeSheetDetailsList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mTimeSheetDetailsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final View rootView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_message);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View itemView) {
            super(itemView);

        }
    }
}
