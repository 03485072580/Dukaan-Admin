package com.example.fasih.dukaanadmin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.fasih.dukaanadmin.R;

/**
 * Created by Fasih on 01/18/19.
 */

public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder> {

    private Context mContext;

    public MyOrdersListAdapter(Context context) {
        mContext = context;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        try {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_single_order_row, parent, false);
            return new MyViewHolder(view);
        } catch (NullPointerException exc) {
            Log.d("TAG1234", "onCreateViewHolder:MyOrdersListAdapter NullPointerException" + exc.getMessage());
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout expandableView, orderDetails;

        public MyViewHolder(View itemView) {
            super(itemView);
            expandableView = itemView.findViewById(R.id.expandableView);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            expandableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderDetails.getVisibility() == View.VISIBLE)
                        orderDetails.setVisibility(View.GONE);
                    else
                        orderDetails.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
