package com.example.fasih.dukaanadmin.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fasih.dukaanadmin.Models.Chat_Model;
import com.example.fasih.dukaanadmin.R;
import com.example.fasih.dukaanadmin.adapters.Chat_Adapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat_Fragment extends Activity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String currentUserID = null;
    private FirebaseAuth mAuth;


    private RelativeLayout login_button;
    private RecyclerView RecyclerView_home;
    private Chat_Adapter home_adapter;
    private List<Chat_Model> home_model;
    View view;

    TextView admin;
    ImageView button_chatbox_send;

    EditText edittext_chatbox;

    ProgressBar progressBar;
    String UserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_chat);

        init();
    }


    private void init() {

//        mAuth = FirebaseAuth.getInstance();
        admin = (TextView) findViewById(R.id.admin);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        Intent intent = getIntent();
        final Bundle args = intent.getExtras();
        if (args != null) {
            if (args.containsKey("CurrentUser")) {
                currentUserID = args.getString("CurrentUser", "");
                admin.setText(args.getString("Name", ""));
            }
        }
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            currentUserID = user.getUid();
//            UserName = user.getDisplayName();
//
//
//        }

        edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);
        home_model = new ArrayList<>();
        RecyclerView_home = findViewById(R.id.reyclerview_message_list);
        button_chatbox_send = (ImageView) findViewById(R.id.button_chatbox_send);


//        Log.v("NAMEUSER",UserName);
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertChat("T", "");
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Chat_Fragment.this, LinearLayoutManager.VERTICAL, false);
        RecyclerView_home.setLayoutManager(linearLayoutManager);
        RecyclerView_home.addItemDecoration(new GridSpacingItemDecoration(6, dpToPx(6), true));
        RecyclerView_home.setItemAnimator(new DefaultItemAnimator());
        RecyclerView_home.setHasFixedSize(true);
        RecyclerView_home.setNestedScrollingEnabled(true);
        RecyclerView_home.setItemViewCacheSize(20);
        home_adapter = new Chat_Adapter(Chat_Fragment.this, home_model);
        RecyclerView_home.setAdapter(home_adapter);


        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edittext_chatbox.getText().toString().trim().isEmpty()) {
                    sendTxtt();

                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getChat();


    }

    private void getChat() {

        Log.d("NAMEUSER", currentUserID);
//        Toast.makeText(getActivity(), UserName+"", Toast.LENGTH_SHORT).show();

        myRef = FirebaseDatabase.getInstance().getReference().child("chat").child(currentUserID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RecyclerView_home.removeAllViewsInLayout();
                RecyclerView_home.swapAdapter(home_adapter, true);
                home_model.clear();
                home_adapter.notifyDataSetChanged();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
//                    String areaName = areaSnapshot.getKey();
                    Chat_Model mos = areaSnapshot.getValue(Chat_Model.class);
                    Chat_Model model = new Chat_Model(mos.getTb_id(), mos.getTb_in_out(), mos.getTb_messaga(), mos.getTb_datetime());
                    home_model.add(model);
                    home_adapter.notifyDataSetChanged();
                    Log.i("response", areaSnapshot.toString());
//                    Toast.makeText(getActivity(), areaName, Toast.LENGTH_SHORT).show();
                }
                RecyclerView_home.scrollToPosition(home_model.size() - 1);
                home_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendTxtt() {
        button_chatbox_send.setEnabled(false);

        myRef = FirebaseDatabase.getInstance().getReference().child("chat").child(currentUserID).push();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a , EEE, d MMM yyyy ");
        final String currentDateandTime = sdf.format(new Date());
        final Chat_Model chat_model = new Chat_Model(myRef.getKey(), "admin", edittext_chatbox.getText().toString().trim(), currentDateandTime.trim().toString());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myRef.setValue(chat_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        edittext_chatbox.getText().clear();
                        button_chatbox_send.setEnabled(true);
                    }
                });
//                Toast.makeText(Add_hotel.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void InsertChat(final String TypeMedia, final String ImageString) {
        button_chatbox_send.setEnabled(false);


//        String USER_ID = sharedpreferences.getString("USER_ID", "en");


//        "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//        "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//        "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//        "yyMMddHHmmssZ"-------------------- 010704120856-0700
//        "K:mm a, z" ----------------------- 0:08 PM, PDT
//        "h:mm a" -------------------------- 12:08 PM
//        "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01

//        Log.d("DATE", currentDateandTime);


//        button_chatbox_send.setEnabled(true);
//        if (sts.equals("1")) {

//            loadMore(currentDateandTime, mimeType, ImageString);
//        edittext_chatbox.getText().clear();
//        home_adapter.notifyDataSetChanged();


//        }


    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


    }


    private int dpToPx(int i) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics()));
    }


}
