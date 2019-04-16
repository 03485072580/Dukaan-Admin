package com.example.fasih.dukaanadmin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fasih.dukaanadmin.Models.ShopProfileSettings;
import com.example.fasih.dukaanadmin.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApprovelAdapter extends RecyclerView.Adapter<ApprovelAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ShopProfileSettings> shopsDataList;

    public ApprovelAdapter(Context context) {
        mContext = context;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        try {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_single_shop_row, parent, false);
//            setupUniversalImageLoader(UniversalImageLoader.getConfiguration(mContext.getApplicationContext()));
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
        holder.email.setText("Email: " + shopsDataList.get(position).getEmail());
        holder.brandName.setText("Shop Name: " + shopsDataList.get(position).getUser_name());
        holder.shopID.setText("Shop ID : " + shopsDataList.get(position).getUser_id());
//        ImageLoader.getInstance().displayImage(shopsDataList.get(position).getProfile_image_url(), holder.shopProfileImage);
    }

    @Override
    public int getItemCount() {
        return shopsDataList.size();
    }

//    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
//        ImageLoader.getInstance().init(config);
//    }

    public void setDataSet(ArrayList<ShopProfileSettings> shopDataList) {
        this.shopsDataList = shopDataList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView shopProfileImage;
        private TextView shopID, brandName, email;


        public MyViewHolder(View itemView) {
            super(itemView);
            shopProfileImage = itemView.findViewById(R.id.shopProfileImage);
            shopID = itemView.findViewById(R.id.shopID);
            brandName = itemView.findViewById(R.id.brandName);
            email = itemView.findViewById(R.id.email);
        }
    }
}
