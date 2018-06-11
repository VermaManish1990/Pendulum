package com.pend.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.util.LoggerUtil;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
    }

    @Override
    protected void initUI() {

        findViewById(R.id.bt_profile).setOnClickListener(this);
        findViewById(R.id.bt_mirror).setOnClickListener(this);
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
            case R.id.bt_profile:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_mirror:
                Intent intentMirror = new Intent(HomeActivity.this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
