package com.example.fasih.dukaanadmin.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanadmin.R;
import com.example.fasih.dukaanadmin.activities.AdminActivity;
import com.example.fasih.dukaanadmin.adapters.MyOrdersListAdapter;

/**
 * Created by Fasih on 01/16/19.
 */

public class OrdersListFragment extends Fragment {

    private RecyclerView ordersContainer;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        setupFragmentWidgets(view);
        setupRecyclerView();
        return view;
    }

    private void setupFragmentWidgets(View view) {
        ordersContainer = view.findViewById(R.id.ordersContainer);
        toolbar = view.findViewById(R.id.topToolBar);
        try {

            ((AdminActivity) getActivity()).setSupportActionBar(toolbar);
        } catch (NullPointerException exc) {
            Log.d("TAG1234", "setupFragmentWidgets: NullPointerException" + exc.getMessage());
        }


    }

    private void setupRecyclerView() {
        ordersContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyOrdersListAdapter ordersListAdapter = new MyOrdersListAdapter(getActivity());
        ordersContainer.setAdapter(ordersListAdapter);

    }
}
