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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.email.setText("Email: " + shopsDataList.get(position).getEmail());
        holder.brandName.setText("Shop Name: " + shopsDataList.get(position).getUser_name());
        holder.shopID.setText("Shop ID : " + shopsDataList.get(position).getUser_id());
        holder.expandableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(shopsDataList.get(position).getUser_id());
            }
        });

//        ImageLoader.getInstance().displayImage(shopsDataList.get(position).getProfile_image_url(), holder.shopProfileImage);
    }

    private void updateData(String ID) {
        DatabaseReference myref;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        myref.child(mContext.getString(R.string.db_shop_profile_settings_node)).child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("admin_approved").setValue("true");
//                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
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
        public RelativeLayout expandableView;


        public MyViewHolder(View itemView) {
            super(itemView);
            expandableView = itemView.findViewById(R.id.expandableView);
            shopID = itemView.findViewById(R.id.shopID);
            brandName = itemView.findViewById(R.id.brandName);
            email = itemView.findViewById(R.id.email);
        }
    }
}
