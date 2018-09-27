package com.pend.arena.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pend.R;
import com.pend.arena.model.Message;
import com.pend.arena.model.get_chat_room.GetChatRoomResponse;
import com.pend.arena.model.user_chat.ResponseData;
import com.pend.arena.model.user_chat.SendMessageResponse;
import com.pend.arena.model.user_chat.UserData;
import com.pend.arena.presenter.ChatPresenter;
import com.pend.interfaces.Constants;
import com.pend.util.ProgressBarHandler;
import com.pend.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChatActivity extends Activity implements ChatPresenter.ChatPresenterListener {
    private RecyclerView recyclerView;
    private EditText editText;
    private Button sendButton;
    private List<ResponseData> myDataset = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;
    private boolean hasNext;
    private int PAGE_COUNT = 1;
    int visibleItemCount, totalItemCount, pastVisiblesItems;
    private boolean loading = true;
    private Integer userID, chatRoomID, selectedUserID;
    private ProgressBarHandler progressBarHandler;
    private ChatPresenter chatPresenter;
    private String imageURL;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        editText = (EditText) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.send_button);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.parent_layout);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });

        recyclerView.setHasFixedSize(true);
        progressBarHandler = new ProgressBarHandler(this);

        myDataset = new ArrayList<>();

        getNewMessageThread();

        userID = Integer.valueOf(SharedPrefUtils.getUserId(ChatActivity.this));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            if(bundle.containsKey(Constants.SELECTED_USER_ID)){
                selectedUserID = bundle.getInt(Constants.SELECTED_USER_ID,-1);
            }
            if(bundle.containsKey(Constants.CHAT_ROOM_ID)){
                chatRoomID = bundle.getInt(Constants.CHAT_ROOM_ID,-1);
            }
        }

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

       /* myDataset.add(new ChatDataItem(true,"anderson dev","Lorem ipsum has been the industry's dummy standard text","https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"));
        myDataset.add(new ChatDataItem(false,"anderson dev","Lorem ipsum ","https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"));
        myDataset.add(new ChatDataItem(false,"anderson dev","Lorem ipsum has been the industry's dummy standard text since 1500s","https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"));
        myDataset.add(new ChatDataItem(true,"anderson dev","Lorem ipsum has been the industry's dummy standard text","https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"));*/
        myAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(myAdapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0)// check for scroll up
                {

                    Log.d("Is AT TOP", "IS AT tOP");
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {

                        Log.d("Loading", "Loading");
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.d("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            if (hasNext) {
                                Log.d("...", "Loading more !");
                                chatPresenter.getUserChat(userID, selectedUserID, chatRoomID
                                        , ++PAGE_COUNT);

                            }


                        }
                    }

                }
            }
        });


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBarHandler.show();
        getData();

    }

    @Override
    public void getUserChat(UserData userData) {

        progressBarHandler.hide();

        if (userData != null && userData.isStatus()) {
            myDataset.addAll(userData.getData().getResponseData());
            myAdapter.notifyDataSetChanged();

            hasNext = userData.getData().hasNextPage;
            if (hasNext)
                loading = true;
        }
    }

    @Override
    public void sendMessage(SendMessageResponse userData) {

        if (userData.isStatus())
            getData();

    }

    @Override
    public void getNewMessage(UserData userData) {


        if (userData != null && userData.isStatus()) {
            if (userData.getStatusCode().equals("200")) {
                getData();
            }
        }
    }

    @Override
    public void getChatRoomID(GetChatRoomResponse getChatRoomResponse) {

        Log.d("chat rrom ", "chat rro");

        if (getChatRoomResponse != null && getChatRoomResponse.isStatus()) {

            chatRoomID = getChatRoomResponse.getData().getResponseData().getChatRoomID();
            chatPresenter.getUserChat(userID, selectedUserID, chatRoomID
                    , PAGE_COUNT);

        }


    }

    public class MyAdapter extends RecyclerView.Adapter {
        private List<ResponseData> mDataset;


        class ViewHolder0 extends RecyclerView.ViewHolder {

            public TextView name, message, time,date;
            public ImageView profileImage;

            public ViewHolder0(View v) {
                super(v);
                name = v.findViewById(R.id.sender_name);
                message = v.findViewById(R.id.sender_message);
                profileImage = v.findViewById(R.id.sender_image);
                time = v.findViewById(R.id.time);
                date=v.findViewById(R.id.date);
            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder {
            public TextView message, time,date;
            public ImageView profileImage;

            public ViewHolder2(View v) {
                super(v);
                message = v.findViewById(R.id.receiver_message);
                profileImage = v.findViewById(R.id.receiver_image);
                time = v.findViewById(R.id.time);
                date=v.findViewById(R.id.date);
            }
        }

        @Override
        public int getItemViewType(int position) {
            // Just as an example, return 0 or 2 depending on position
            // Note that unlike in ListView adapters, types don't have to be contiguous

            ResponseData chatDataItem = mDataset.get(position);
            if (chatDataItem.getReceiverID() == userID) {
                return 0;
            } else
                return 2;
            //return position % 2 * 2;
        }


        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<ResponseData> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.sender_row_item, parent, false);
                    return new ViewHolder0(v);

                case 2:
                    View v1 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.receiver_row_item, parent, false);
                    return new ViewHolder2(v1);
                default:
                    return null;

            }


        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ResponseData item = mDataset.get(position);

            switch (holder.getItemViewType()) {
                case 0:

                    ((ViewHolder0) holder).name.setText(mDataset.get(position).getUserFullName());
                    ((ViewHolder0) holder).message.setText(String.valueOf(mDataset.get(position).getMessageText()));
                    ((ViewHolder0) holder).time.setText(formatTime(
                            mDataset.get(position).getMessageDateTime()));

                    if (mDataset.get(position).getImageURL() != null && !mDataset.get(position).getImageURL().equals("")) {

                        Picasso.with(ChatActivity.this).load(mDataset.get(position).getImageURL())
                                .placeholder(R.drawable.profile).into(((ViewHolder0) holder).profileImage);
                    }

                    ((ViewHolder0) holder).date.setText(formatDate(mDataset.get(position).getMessageDateTime()));

                    if(isFirstDate(position,mDataset.get(position).getMessageDateTime()))
                        ((ViewHolder0) holder).date.setVisibility(View.VISIBLE);
                    else

                        ((ViewHolder0) holder).date.setVisibility(View.GONE);


                    break;

                case 2:

                    imageURL = mDataset.get(position).getImageURL();

                    ((ViewHolder2) holder).time.setText(formatTime(
                            mDataset.get(position).getMessageDateTime()));
                    ((ViewHolder2) holder).message.setText(String.valueOf(mDataset.get(position).getMessageText()));

                    if (mDataset.get(position).getImageURL() != null && !mDataset.get(position).getImageURL().equals("")) {

                        Picasso.with(ChatActivity.this).load(mDataset.get(position).getImageURL())
                                .placeholder(R.drawable.profile).into(((ViewHolder2) holder).profileImage);
                    }
                    ((ViewHolder2) holder).date.setText(formatDate(mDataset.get(position).getMessageDateTime()));

                    if(isFirstDate(position,mDataset.get(position).getMessageDateTime()))
                        ((ViewHolder2) holder).date.setVisibility(View.VISIBLE);
                    else

                        ((ViewHolder2) holder).date.setVisibility(View.GONE);

                    break;
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public void sendMessage() {

        if (editText.getText().length() > 0) {

            Message message = new Message(chatRoomID, userID, selectedUserID, editText.getText().toString());

            chatPresenter.sendMessage(message);

            ResponseData data = new ResponseData();
            data.setChatRoomID(chatRoomID);
            data.setMessageText(editText.getText().toString());
            data.setSenderID(userID);
            data.setReceiverID(selectedUserID);
            data.setImageURL(imageURL);
            data.setMessageDateTime("now");

            myDataset.add(0, data);
            myAdapter.notifyItemInserted(0);
            editText.setText("");

            scrollMyListViewToBottom();

        }
    }

    private void scrollMyListViewToBottom() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }


    public void getNewMessageThread() {


        final int delay = 10000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                Log.d("background thread", "backgrund thread");
                ;
                chatPresenter.getNewMessage(userID, selectedUserID, chatRoomID);
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    public void getData() {
        myDataset.clear();
        myAdapter.notifyDataSetChanged();
        PAGE_COUNT = 1;
        loading = true;

        chatPresenter = new ChatPresenter(this, this);
/*
        if(chatRoomID!=null)
        chatPresenter.getUserChat(userID,selectedUserID,chatRoomID
                ,PAGE_COUNT);

        else*/
        chatPresenter.getChatRoomID(userID, selectedUserID);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    String formatTime(String timeString) {

        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse(timeString);

            //DateFormat df = new SimpleDateFormat(" hh:mm a, dd/MMM/yy ");
            DateFormat df = new SimpleDateFormat(" hh:mm a");

            // Get the date today using Calendar object.

            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            String reportDate = df.format(date1);
            System.out.println(timeString + "\t" + date1);
            return reportDate;
        } catch (ParseException e) {

            return null;
        }


    }


    String formatDate(String timeString) {

        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse(timeString);

            //DateFormat df = new SimpleDateFormat(" hh:mm a, dd/MMM/yy ");
            DateFormat df = new SimpleDateFormat(" dd MMM,yyyy");

            // Get the date today using Calendar object.

            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            String reportDate = df.format(date1);
            System.out.println(timeString + "\t" + date1);
            return reportDate;
        } catch (ParseException e) {

            return null;
        }


    }


    boolean isFirstDate( int objectIndex,String date)
    {

        List<String> time = getTimeList();
        int lastIndex = time.lastIndexOf(date);
        Log.d("last index",lastIndex+"");
        for(ResponseData rs:myDataset) {
            if (rs.getMessageDateTime().equals(date)&&lastIndex==objectIndex) {
                return true;
            } else {
                return false;
            }

        }

        return false;


    }

    List<String> getTimeList()
    {
        List<String> list = new ArrayList<>();
        for(ResponseData r:myDataset)
           list.add(r.getMessageDateTime());
       return list;
     }

}



