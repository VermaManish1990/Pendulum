package com.pend.arena.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.pend.R;
import com.pend.activity.login.ProfileActivity;
import com.pend.arena.model.reflections.ReflectionsResponse;
import com.pend.arena.model.reflections.ResponseData;
import com.pend.arena.presenter.ReflectionPresenter;
import com.pend.util.ProgressBarHandler;
import com.squareup.picasso.Picasso;


public class ReflectionFragment extends Fragment implements ReflectionPresenter.ReflectionPresenterListener{


    private RecyclerView recyclerView;
    private List<ResponseData> myDataset;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;
    private Integer userID;
    private ReflectionPresenter reflectionPresenter;
    private boolean hasNext;
    private int PAGE_COUNT=1;
    int visibleItemCount, totalItemCount,pastVisiblesItems;
    private boolean loading = true;
    private ProgressBarHandler progressBarHandler;
    private String searchString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment,container,false);

        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        progressBarHandler= new ProgressBarHandler(getActivity());


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        userID= pref.getInt("userId",0);

        reflectionPresenter = new ReflectionPresenter(this,getContext());


        myDataset= new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            if(hasNext)
                            {
                                reflectionPresenter.getReflections(userID,++PAGE_COUNT,searchString);

                            }


                        }
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void getReflections(ReflectionsResponse reflectionsResponse) {

        progressBarHandler.hide();

        if(reflectionsResponse !=null&& reflectionsResponse.isStatus())
        {
            myDataset.addAll(reflectionsResponse.getData().getResponseData());
            myAdapter.notifyDataSetChanged();

            if(reflectionsResponse.getData().hasNextPage)
                loading=true;



        }

    }

    @Override
    public void onResume() {
        super.onResume();

        myDataset.clear();
        myAdapter.notifyDataSetChanged();
        PAGE_COUNT=1;
        loading=true;

        ArenaActivity act = (ArenaActivity)getActivity();

        searchString = act.getSearchText();

        reflectionPresenter.getReflections(userID,PAGE_COUNT,searchString);
        progressBarHandler.show();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<ResponseData> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView name,count;
            public ImageView profileImage,chat;
            public RelativeLayout relLayout;
            public ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.user_name);
                count=v.findViewById(R.id.user_count);
                profileImage=v.findViewById(R.id.user_image);
                chat=v.findViewById(R.id.chat);
                relLayout=v.findViewById(R.id.row_layout);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<ResponseData> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            // create a new view
            View v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reflection_row_item, parent, false);

            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final ResponseData item =mDataset.get(position);
            holder.name.setText(mDataset.get(position).getUserFullName());
            holder.count.setText("("+mDataset.get(position).getMirrorCount()+" common mirrors)");

            if(mDataset.get(position).getImageURL()!=null){

                Picasso.with(getActivity()).load(mDataset.get(position).getImageURL())
                        .placeholder(R.drawable.profile).into(holder.profileImage);
            }

            /*holder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("selectedUserId",item.getUserID());
                    intent.putExtras(b);
                    startActivity(intent);

                }
            });*/


            holder.profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                }
            });

            holder.relLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("selectedUserId",item.getUserID());
                    intent.putExtras(b);
                    startActivity(intent);

                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}

