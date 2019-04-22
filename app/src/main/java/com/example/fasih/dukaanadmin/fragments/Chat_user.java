package com.example.fasih.dukaanadmin.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fasih.dukaanadmin.R;
import com.example.fasih.dukaanadmin.activities.Chat_Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Chat_user extends Fragment {

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> Name = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chat_user, container, false);

        init();
        return view;
    }

    private void init() {
        usersList = (ListView) view.findViewById(R.id.usersList);
        noUsersText = (TextView) view.findViewById(R.id.noUsersText);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://dukaanapp-5038e.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                doOnSuccess(s);

                try {

                    JSONObject jObject = new JSONObject(s);
                    Iterator<String> keys = jObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
//                        Log.v("**********", "**********");
//                        Log.v("category key", key);
                        JSONObject innerJObject = jObject.getJSONObject(key);
                        al.add(key);
                        Name.add(innerJObject.getString("user_name"));
//                        Log.v("key = " + key, "value = " + innerJObject.getString("user_name"));
                        totalUsers++;

                    }


                    if (totalUsers <= 1) {
                        noUsersText.setVisibility(View.VISIBLE);
                        usersList.setVisibility(View.GONE);
                    } else {
                        noUsersText.setVisibility(View.GONE);
                        usersList.setVisibility(View.VISIBLE);
                        usersList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Name));
                    }

                    pd.dismiss();


//                    Log.v("OBJECT", String.valueOf(mainobject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Log.v("SAD", al.get(position));

//                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(getActivity(), Chat_Fragment.class).putExtra("CurrentUser", al.get(position)).putExtra("Name", Name.get(position)));
            }
        });
    }


    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);


            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {


                key = i.next().toString();

                Log.v("NAMES:", String.valueOf(key));
//                Log.v("NAMES:", String.valueOf(obj.get("user_name")));

//                if(!key.equals(UserDetails.username)) {
                al.add(key);
//                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
}