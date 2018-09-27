package com.pend.arena.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chaudhary on 3/11/2018.
 */

public class ArenaActivity extends FragmentActivity implements View.OnClickListener {


    private EditText mEtSearch;
    private android.support.design.widget.FloatingActionButton fab;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG = "ArenaActivity";
    private TabLayout tabLayout;
    private View mRootView;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arena_layout);

        setInitialData();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        // Add Fragments to adapter one by one
        adapter.addFragment(new ChatFragment(), "Recent Chat");
        adapter.addFragment(new ReflectionFragment(), "Reflection");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);


        tabLayout.setupWithViewPager(viewPager);

        reduceMarginsInTabs(tabLayout, 0);


        View view = findViewById(R.id.custom_search_view);
        mEtSearch = view.findViewById(R.id.et_search);
        ImageView ivSearch = view.findViewById(R.id.iv_search);

//        searchText = (EditText) findViewById(R.id.search_text);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_add);
        fab.setImageResource(R.drawable.gps);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // openDialog();

                FilterDialogFragment dialogFragment = new FilterDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "filter");


            }
        });


     /*   searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {



                    if (searchText.getText().length() > 0)
                        searchTextString = searchText.getText().toString();
                    else
                        searchTextString = null;

                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    viewPager.getAdapter().notifyDataSetChanged();

                return true;
            }
        });*/

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                 /*   if (searchText.getText().length() > 0)
                        searchTextString = searchText.getText().toString();
                    else
                        searchTextString = null;*/

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);


                    if(viewPager.getAdapter()!=null){
                        viewPager.getAdapter().notifyDataSetChanged();
                    }

                    changeTabsFont(tabLayout);
                    wrapTabIndicatorToTitle(tabLayout, 100, 100);

                    return true;
                }
                return false;
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if(viewPager.getAdapter()!=null){
                    viewPager.getAdapter().notifyDataSetChanged();
                }

                changeTabsFont(tabLayout);
                wrapTabIndicatorToTitle(tabLayout, 100, 100);
            }
        });

    }

    private void setInitialData() {
        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        ((ImageView) quarterView.findViewById(R.id.iv_arena)).setImageDrawable(getResources().getDrawable(R.drawable.home));
        ((TextView) quarterView.findViewById(R.id.tv_arena)).setText(String.valueOf(getResources().getString(R.string.home)));
        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);
        quarterView.findViewById(R.id.fl_arena).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        changeTabsFont(tabLayout);
        wrapTabIndicatorToTitle(tabLayout, 100, 100);
    }


    public String getSearchText() {
        return mEtSearch.getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_profile:
                hideReveal();
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(this, ContestActivity.class);
                startActivity(intentContest);
                finish();
                break;

            case R.id.fl_arena:
                hideReveal();
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                break;

            case R.id.fl_menu_view:
                mFlMenuView.setVisibility(View.GONE);
                showReveal();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
    }

    void openDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.arena_filter_layout, null);
        EditText txtLocation = (EditText) dialogView.findViewById(R.id.txtLocation);
        dialogBuilder.setView(dialogView);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(ArenaActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        /*searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();



                Intent intent =new Intent(getApplicationContext(),SearchMirrorActivity.class);

                if(editText.getText().toString().length()>0)
                    intent.putExtra("searchText",editText.getText().toString());
                startActivity(intent);
            }
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {

        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void changeTabsFont(TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface type = Typeface.createFromAsset(getAssets(), "font/montserrat.ttf");
                    ((TextView) tabViewChild).setTypeface(type, Typeface.NORMAL);
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTextSize(14);

                }
            }
        }
    }


    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }


            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void showReveal() {
        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, 0, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRlQuarterView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void hideReveal() {

        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRlQuarterView.setVisibility(View.GONE);
                mFlMenuView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (mRlQuarterView.getVisibility() == View.VISIBLE) {
            hideReveal();
        } else {
            Intent intentHome = new Intent(this, HomeActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentHome);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Rect outRect = new Rect();
        mFlQuarterBlackView.getGlobalVisibleRect(outRect);

        if (mRlQuarterView.getVisibility() == View.VISIBLE && !outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
            hideReveal();
        }
        return super.dispatchTouchEvent(event);
    }
}

