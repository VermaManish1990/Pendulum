package com.pend.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.OtherUtil;

import java.util.ArrayList;

public class ExitPollAdapter extends RecyclerView.Adapter<ExitPollAdapter.ViewHolder> {

    private final String TAG = ExitPollAdapter.class.getSimpleName();

    private final Context mContext;
    private ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> mExitPollList;

    public ExitPollAdapter(Context context, ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> exitPollList) {
        mContext = context;
        mExitPollList = exitPollList;
    }

    public void setExitPollList(ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> exitPollList) {
        this.mExitPollList = exitPollList;
    }

    @NonNull
    @Override
    public ExitPollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exit_poll_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExitPollAdapter.ViewHolder holder, int position) {

        final GetExitPollListResponseModel.GetExitPollListDetails exitPollListDetails = mExitPollList.get(position);

        final int max = getMax(exitPollListDetails.pollAdmirePer, exitPollListDetails.pollHatePer, exitPollListDetails.pollCantSayPer);


        holder.rlPollPerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = (int) (holder.rlPollPerView.getWidth() * (0.7));
                holder.tvAdmireView.setWidth(exitPollListDetails.pollAdmirePer * (width / max));
                holder.tvHateView.setWidth(exitPollListDetails.pollHatePer * (width / max));
                holder.tvCanTSayView.setWidth(exitPollListDetails.pollCantSayPer * (width / max));

                holder.tvAdmireView.setText(String.valueOf(exitPollListDetails.pollAdmirePer));
                holder.tvHateView.setText(String.valueOf(exitPollListDetails.pollHatePer));
                holder.tvCanTSayView.setText(String.valueOf(exitPollListDetails.pollCantSayPer));

            }
        });

        holder.tvTitle.setText(exitPollListDetails.exitPollText != null ? exitPollListDetails.exitPollText : "");
        holder.tvShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Method is used to get max percentage value.
     *
     * @param pollAdmirePer  pollAdmirePer
     * @param pollHatePer    pollHatePer
     * @param pollCantSayPer pollCantSayPer
     * @return int
     */
    private int getMax(int pollAdmirePer, int pollHatePer, int pollCantSayPer) {

        if (pollAdmirePer >= pollHatePer) {
            if (pollAdmirePer >= pollCantSayPer) {
                return pollAdmirePer;
            } else {
                return pollCantSayPer;
            }
        } else {
            if (pollHatePer >= pollCantSayPer) {
                return pollHatePer;
            } else {
                return pollCantSayPer;
            }
        }
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
        private final View rlPollPerView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            rlPollPerView = itemView.findViewById(R.id.rl_poll_per_view);
            tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAdmireView = itemView.findViewById(R.id.tv_admire_view);
            tvHateView = itemView.findViewById(R.id.tv_hate_view);
            tvCanTSayView = itemView.findViewById(R.id.tv_can_t_say_view);
        }
    }
}
