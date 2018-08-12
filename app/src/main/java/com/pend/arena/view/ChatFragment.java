package com.pend.arena.view;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.pend.R;
import com.pend.arena.model.recent_chat.RecentChatsResponse;
import com.pend.arena.model.recent_chat.ResponseData;
import com.pend.arena.presenter.ArenaHomePresenter;
import com.pend.util.ProgressBarHandler;
import com.pend.util.RecyclerItemClickListener;
import com.pend.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;


public class ChatFragment extends Fragment implements ArenaHomePresenter.ArenaHomePresenterListener {

    private RecyclerView recyclerView;
    private List<ResponseData> myDataset = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;
    private ArenaHomePresenter arenaHomePresenter;
    private Integer userID;
    private boolean hasNext;
    private int PAGE_COUNT=1;
    int visibleItemCount, totalItemCount,pastVisiblesItems;
    private boolean loading = true;
    private ProgressBarHandler progressBarHandler;
    private String searchString;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment,container,false);

        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        progressBarHandler= new ProgressBarHandler(getActivity());


        myDataset= new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(myAdapter);

        try{
            userID= Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        }catch (Exception e){
            e.printStackTrace();
            userID = -1;
        }

        arenaHomePresenter = new ArenaHomePresenter(this,getActivity());

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                        ResponseData data = myDataset.get(position);

                        Intent intent = new Intent(getActivity(),ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Bundle b = new Bundle();
                        b.putInt("chatRoomId",data.getChatRoomID());
                        b.putInt("selectedUserId",data.getUserID());
                        intent.putExtras(b);
                        startActivity(intent);

                    }


                })
        );

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
                                arenaHomePresenter.getRecentChats(userID,
                                        ++PAGE_COUNT,searchString);

                            }


                        }
                    }
                }
            }
        });


        return view;
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

        arenaHomePresenter.getRecentChats(userID,PAGE_COUNT,searchString);
        progressBarHandler.show();
    }

    @Override
    public void getRecentChats(RecentChatsResponse recentChatsResponse) {

        progressBarHandler.hide();

        if(recentChatsResponse !=null&& recentChatsResponse.isStatus())
        {
            myDataset.addAll(recentChatsResponse.getData().getResponseData());
            myAdapter.notifyDataSetChanged();

            if(recentChatsResponse.getData().hasNextPage)
                loading=true;

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        progressBarHandler.hide();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<ResponseData> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView name,count;
            public ImageView profileImage;
            public ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.user_name);
                count=v.findViewById(R.id.user_count);
                profileImage=v.findViewById(R.id.user_image);
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
                    .inflate(R.layout.chat_row_item, parent, false);

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

            if(mDataset.get(position).getUnRead()==null)
            {
                holder.count.setVisibility(View.GONE);
            }
            else {
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText(mDataset.get(position).getUnRead() + "");
            }

            if(mDataset.get(position).getImageURL()!=null){

                Picasso.with(getActivity()).load(mDataset.get(position).getImageURL())
                        .placeholder(R.drawable.profile).into(holder.profileImage);
            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }


}
