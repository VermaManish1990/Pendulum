package com.pend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pend.R;
import com.pend.models.UserProfileResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileViewPagerAdapter extends PagerAdapter {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private IProfileViewPagerAdapterCallBack mIProfileViewPagerAdapterCallBack;
    private ArrayList<UserProfileResponseModel.ImageDetails> mImageDetailsList;

    public ProfileViewPagerAdapter(Context context, ArrayList<UserProfileResponseModel.ImageDetails> imageDetailsList) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mImageDetailsList = imageDetailsList;
        mIProfileViewPagerAdapterCallBack = (IProfileViewPagerAdapterCallBack) context;
    }

    public void setImageDetailsList(ArrayList<UserProfileResponseModel.ImageDetails> imageDetailsList) {
        this.mImageDetailsList = imageDetailsList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        UserProfileResponseModel.ImageDetails imageDetails = mImageDetailsList.get(position);
        View itemView = mLayoutInflater.inflate(R.layout.profile_view_pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.iv_profile);

        Picasso.with(mContext)
                .load(imageDetails.imageURL)
//                .placeholder(R.drawable.ic_placeholder)   // optional
//                .error(R.drawable.ic_error_fallback)      // optional
//                .resize(250, 200)                        // optional
//                .rotate(90)                             // optional
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIProfileViewPagerAdapterCallBack.onImageClick(position);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return mImageDetailsList != null ? mImageDetailsList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);

    }

    public interface IProfileViewPagerAdapterCallBack{
        void onImageClick(int position);
    }
}
