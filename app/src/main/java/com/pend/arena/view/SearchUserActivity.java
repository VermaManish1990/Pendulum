package com.pend.arena.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.arena.model.search_user.SearchUserResponse;
import com.pend.arena.model.search_user.User;
import com.pend.arena.presenter.SearchUserPresenter;
import com.pend.interfaces.Constants;
import com.pend.util.ProgressBarHandler;
import com.pend.util.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SearchUserActivity extends Activity implements SearchUserPresenter.SearchUserPresenterListener {

    private RecyclerView recyclerView;
    private boolean hasNext;
    private int PAGE_COUNT = 1;
    int visibleItemCount, totalItemCount, pastVisiblesItems;
    private boolean loading = true;
    private ProgressBarHandler progressBarHandler;
    private List<User> myDataset;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;
    private Integer userID;
    private SearchUserPresenter searchUserPresenter;
    private String area, sex;
    private Integer ageTo, ageFrom, distanceTo, distanceFrom;
    private Double latitude, longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        progressBarHandler = new ProgressBarHandler(this);


        myDataset = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      /*  myDataset.add(new ChatItem("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&h=650&w=940","Walter B.Steele","2"));
        myDataset.add(new ChatItem("https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&h=650&w=940","Marcus C. Clark","12"));
        myDataset.add(new ChatItem("https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&h=650&w=940","Margaret J .Freeman","2"));
        */
        myAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(myAdapter);

        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        userID = pref.getInt("userId", 0);

        Bundle b = getIntent().getExtras();
        ageTo = b.getInt("ageTo");
        ageFrom = b.getInt("ageFrom");
        //area =b.getString("area");
        sex = b.getString("sex");

        distanceTo = b.getInt("distanceTo");
        distanceFrom = b.getInt("distanceFrom");
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");


        searchUserPresenter = new SearchUserPresenter(this, this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            if (hasNext) {
                                searchUserPresenter.searchUser(userID,
                                        ++PAGE_COUNT, latitude, longitude,
                                        distanceFrom, distanceTo, ageTo, ageFrom, sex);

                            }


                        }
                    }
                }
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever

                        User data = myDataset.get(position);

                        Intent intent = new Intent(SearchUserActivity.this, ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Bundle bundle = new Bundle();
                        //  b.putInt("chatRoomId",data.getChatRoomID());
                        bundle.putInt(Constants.SELECTED_USER_ID, data.getUserID());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }


                })
        );
    }


    @Override
    public void searchUser(SearchUserResponse searchUserResponse) {
        progressBarHandler.hide();

        if (searchUserResponse != null && searchUserResponse.isStatus())
            myDataset.addAll(searchUserResponse.getData().userList);
        myAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();

        myDataset.clear();
        myAdapter.notifyDataSetChanged();
        PAGE_COUNT = 1;
        loading = true;

        searchUserPresenter.searchUser(userID, PAGE_COUNT, latitude, longitude, distanceFrom, distanceTo, ageTo, ageFrom, sex);
        progressBarHandler.show();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<User> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView name, age, gender, address;
            public ImageView profileImage;

            public ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.user_name);
                profileImage = v.findViewById(R.id.user_image);
                age = v.findViewById(R.id.age);
                gender = v.findViewById(R.id.gender);
                address = v.findViewById(R.id.address);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<User> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_user_item, parent, false);

            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final User item = mDataset.get(position);
            holder.name.setText(item.getUserFullName());
            holder.age.setText(item.getUserAge() + " years");
            holder.gender.setText(item.getUserGender() + "");
            holder.address.setText(item.getAddress() + "");

            if (mDataset.get(position).getImageURL() != null && !mDataset.get(position).getImageURL().equals("")) {

                Picasso.with(getApplicationContext()).load(mDataset.get(position).
                        getImageURL()).placeholder(R.drawable.profile).into(holder.profileImage);
            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}
