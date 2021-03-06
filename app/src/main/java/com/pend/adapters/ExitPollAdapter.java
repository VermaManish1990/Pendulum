package com.pend.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.pend.R;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.OtherUtil;

import java.util.ArrayList;

public class ExitPollAdapter extends RecyclerView.Adapter<ExitPollAdapter.ViewHolder> {

    private final String TAG = ExitPollAdapter.class.getSimpleName();

    private final Context mContext;
    private IExitPollAdapterCallBack mIExitPollAdapterCallBack;
    private ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> mExitPollList;

    public ExitPollAdapter(Context context, ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> exitPollList) {
        mContext = context;
        mIExitPollAdapterCallBack = (IExitPollAdapterCallBack) context;
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
    public void onBindViewHolder(@NonNull final ExitPollAdapter.ViewHolder holder, final int position) {

        final GetExitPollListResponseModel.GetExitPollListDetails exitPollListDetails = mExitPollList.get(position);

        holder.tvTitle.setText(exitPollListDetails.exitPollText != null ? exitPollListDetails.exitPollText : "");

        final int max = getMax(exitPollListDetails.pollAdmirePer, exitPollListDetails.pollHatePer, exitPollListDetails.pollCantSayPer);
        holder.rlPollPerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = (int) (holder.rlPollPerView.getWidth() * (0.7));

                if (max > 0) {

                    if (exitPollListDetails.pollAdmirePer != 0) {

                        holder.tvAdmireView.setWidth(exitPollListDetails.pollAdmirePer * (width / max));
                        holder.tvAdmireView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_green_view));
                    } else {
                        holder.tvAdmireView.setWidth(width / 4);
                        holder.tvAdmireView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                        holder.tvAdmireView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                    }

                    if (exitPollListDetails.pollHatePer != 0) {

                        holder.tvHateView.setWidth(exitPollListDetails.pollHatePer * (width / max));
                        holder.tvHateView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_red_view));
                    } else {
                        holder.tvHateView.setWidth(width / 4);
                        holder.tvHateView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                        holder.tvHateView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                    }

                    if (exitPollListDetails.pollCantSayPer != 0) {

                        holder.tvCanTSayView.setWidth(exitPollListDetails.pollCantSayPer * (width / max));
                        holder.tvCanTSayView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_yellow_view));
                    } else {
                        holder.tvCanTSayView.setWidth(width / 4);
                        holder.tvCanTSayView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                        holder.tvCanTSayView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                    }
                } else {
                    holder.tvAdmireView.setWidth(width / 4);
                    holder.tvAdmireView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    holder.tvAdmireView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

                    holder.tvHateView.setWidth(width / 4);
                    holder.tvHateView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    holder.tvHateView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

                    holder.tvCanTSayView.setWidth(width / 4);
                    holder.tvCanTSayView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    holder.tvCanTSayView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                }

                holder.tvAdmireView.setText(String.valueOf(exitPollListDetails.pollAdmirePer + "%"));
                holder.tvHateView.setText(String.valueOf(exitPollListDetails.pollHatePer + "%"));
                holder.tvCanTSayView.setText(String.valueOf(exitPollListDetails.pollCantSayPer + "%"));
            }
        });

        holder.tvShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);// initialize facebook shareDialog.

                if (ShareDialog.canShow(SharePhotoContent.class)) {

                    Bitmap bitmap = OtherUtil.loadBitmapFromView(holder.rootView);
                    if (bitmap != null) {

                        try {

                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(bitmap)
//                                    .setImageUrl(Uri.parse(postsDetails.imageURL))
                                    .setCaption(exitPollListDetails.exitPollText)
                                    .build();

                            ShareContent content = new SharePhotoContent.Builder()
                                    .addPhoto(photo)
                                    .build();

                            shareDialog.show(content);  // Show facebook ShareDialog
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.facebook_error_message), mContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIExitPollAdapterCallBack.onViewClick(position);
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
        private final ImageView tvShareOnFacebook;
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

    public interface IExitPollAdapterCallBack {
        void onViewClick(int position);
    }
}
