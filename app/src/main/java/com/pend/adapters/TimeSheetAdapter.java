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

public class TimeSheetAdapter extends RecyclerView.Adapter<TimeSheetAdapter.ViewHolder> {

    private static final String TAG = TimeSheetAdapter.class.getSimpleName();
    private Context mContext;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_time_sheet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSheetAdapter.ViewHolder holder, int position) {
        UserTimeSheetResponseModel.UserTimeSheetDetails timeSheetDetails = mTimeSheetDetailsList.get(position);

        holder.tvMessage.setText(timeSheetDetails.mirrorName != null ? timeSheetDetails.mirrorName : "");
    }

    @Override
    public int getItemCount() {
        return mTimeSheetDetailsList != null ? mTimeSheetDetailsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_message);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }
}
