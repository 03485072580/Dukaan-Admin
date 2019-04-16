package com.example.fasih.dukaanadmin.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanadmin.R;

/**
 * Created by Fasih on 01/16/19.
 */

public class ManageUsersFragment extends Fragment {


    RecyclerView approvel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_users, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        approvel = view.findViewById(R.id.approvel);

    }
}
