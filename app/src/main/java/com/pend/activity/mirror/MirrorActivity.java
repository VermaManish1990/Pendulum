package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.FragmentViewPagerAdapter;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.fragments.FollowingMirrorFragment;
import com.pend.fragments.IntroducedMirrorFragment;
import com.pend.fragments.TrendingMirrorFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IMirrorFragmentCallBack;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;

public class MirrorActivity extends BaseActivity implements View.OnClickListener,IMirrorFragmentCallBack {

    private static final String TAG = MirrorActivity.class.getSimpleName();
    private View mRootView;
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mMirrorList;
    private ViewPager mViewPagerMirror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);

        findViewById(R.id.custom_search_view).setOnClickListener(this);

        TabLayout tabLayoutMirror = findViewById(R.id.tab_layout_mirror);
        mViewPagerMirror = findViewById(R.id.view_pager_mirror);

        setupViewPager(mViewPagerMirror);
        tabLayoutMirror.setupWithViewPager(mViewPagerMirror, true);
    }

    @Override
    protected void setInitialData() {

        mMirrorList = new ArrayList<>();
    }

    /**
     * Method is used to setting up View Pager
     */
    private void setupViewPager(ViewPager viewPager) {
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TrendingMirrorFragment(), getString(R.string.trending));
        adapter.addFragment(new FollowingMirrorFragment(), getString(R.string.following));
        adapter.addFragment(new IntroducedMirrorFragment(), getString(R.string.introduced));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }

    @Override
    public void onAuthError() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_search_view:
                Intent intent = new Intent(MirrorActivity.this, SearchInNewsFeedActivity.class);
                intent.putExtra(Constants.MIRROR_FRAGMENT_POSITION,mViewPagerMirror.getCurrentItem());
                startActivity(intent);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onCreateMirrorClick() {
        DialogFragment createMirrorDialogFragment = new CreateMirrorDialogFragment();
        createMirrorDialogFragment.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
    }
}
