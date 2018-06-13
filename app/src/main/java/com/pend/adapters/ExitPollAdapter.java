package com.pend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;

public class ExitPollAdapter extends RecyclerView.Adapter<ExitPollAdapter.ViewHolder> {

    private final String TAG = ExitPollAdapter.class.getSimpleName();

    private final Context mContext;
    private ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> mExitPollList;

    public ExitPollAdapter(Context context, ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> exitPollList) {
        mContext = context;
        mExitPollList = exitPollList;
    }

    @NonNull
    @Override
    public ExitPollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exit_poll_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExitPollAdapter.ViewHolder holder, int position) {

        GetExitPollListResponseModel.GetExitPollListDetails exitPollListDetails = mExitPollList.get(position);

        holder.tvTitle.setText(exitPollListDetails.exitPollText != null ? exitPollListDetails.exitPollText : "");
        holder.tvShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mExitPollList != null ? mExitPollList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final TextView tvShareOnFacebook;
        private final TextView tvAdmireView;
        private final TextView tvHateView;
        private final TextView tvCanTSayView;
        private final TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAdmireView = itemView.findViewById(R.id.tv_admire_view);
            tvHateView = itemView.findViewById(R.id.tv_hate_view);
            tvCanTSayView = itemView.findViewById(R.id.tv_can_t_say_view);
        }
    }
}
