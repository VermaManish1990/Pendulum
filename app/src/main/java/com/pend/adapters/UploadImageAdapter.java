package com.pend.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pend.R;
import com.pend.models.AddUserImageResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {
    private static final String TAG = UploadImageAdapter.class.getSimpleName();
    private ArrayList<AddUserImageResponseModel.AddUserImageDetails> mImageDetailsList;
    private IUploadImageAdapterCallback mIUploadImageAdapterCallback;
    private Context mContext;

    public UploadImageAdapter(Context context, ArrayList<AddUserImageResponseModel.AddUserImageDetails> imageDetailsList) {
        mContext = context;
        mImageDetailsList = imageDetailsList;
        mIUploadImageAdapterCallback = (IUploadImageAdapterCallback) context;
    }

    public void setImageDetailsList(ArrayList<AddUserImageResponseModel.AddUserImageDetails> imageDetailsList) {
        mImageDetailsList = mImageDetailsList;
    }

    public ArrayList<AddUserImageResponseModel.AddUserImageDetails> getImageDetailsList() {
        return mImageDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_upload_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        AddUserImageResponseModel.AddUserImageDetails imageDetails = mImageDetailsList.get(position);

        Picasso.with(mContext)
                .load(imageDetails.imageUrl)
                .resize(250, 250)
                .into(holder.ivProfile);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIUploadImageAdapterCallback.onProfileViewClick(position);
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mIUploadImageAdapterCallback.onProfileViewLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageDetailsList != null ? mImageDetailsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProfile;
        private final View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public interface IUploadImageAdapterCallback {
        void onProfileViewClick(int position);

        void onProfileViewLongClick(int position);
    }
}
