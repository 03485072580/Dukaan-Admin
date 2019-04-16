package com.example.fasih.dukaanadmin.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanadmin.Models.ShopProfileSettings;
import com.example.fasih.dukaanadmin.R;
import com.example.fasih.dukaanadmin.activities.AdminActivity;
import com.example.fasih.dukaanadmin.activities.HomeActivity;
import com.example.fasih.dukaanadmin.fragments.OrdersListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Fasih on 01/04/19.
 */

public class FirebaseMethods {


    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private ProgressBar updateProgress;
    private String activityName;
    private Context mContext;

    public FirebaseMethods(Context context, String activityName) {
        this.mContext = context;
        this.activityName = activityName;
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
    }


    public void authenticateUser(final String username
            , final String email
            , final String password
            , final String fragmentName
            , final String scope
            , final LinearLayout login) {


        if (TextUtils.isEmpty(username)) {
            //Mean Email is provided by the User
            if (updateProgress != null) {
                updateProgress.setVisibility(View.VISIBLE);
                myRef
                        .orderByChild(mContext.getString(R.string.db_admins_node))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    setupAdminLoginForFirstTime(email, password, scope, login);


                                } else {
                                    updateProgress.setVisibility(View.GONE);
                                    login.setAlpha(1f);
                                    login.setEnabled(true);
                                    setupSubsequentAdminLogin();

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                updateProgress.setVisibility(View.GONE);
                                login.setAlpha(1f);
                                login.setEnabled(true);
                            }
                        });
            } else {
                login.setEnabled(true);
                login.setAlpha(1f);
                Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setupSubsequentAdminLogin() {
    }

    private void setupAdminLoginForFirstTime(final String email, final String password, final String scope, final LinearLayout login) {

        Query query = myRef
                .child(mContext.getString(R.string.db_admins_node))
                .orderByChild(mContext.getString(R.string.db_field_email))
                .equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) ds.getValue();
                        String pass = (String) hashMap.get("password");
                        if (pass.equals(password)) {
                            mAuth.fetchSignInMethodsForEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                            if (!task.getResult().getSignInMethods().isEmpty()) {
                                                //Code without user Creation. Now Admin Logged In
                                                updateProgress.setVisibility(View.GONE);
                                                login.setAlpha(1f);
                                                login.setEnabled(true);

                                                ((AdminActivity) mContext).startActivity(new Intent(mContext, HomeActivity.class));
//                                                ((AdminActivity) mContext).getSupportFragmentManager()
//                                                        .popBackStackImmediate();
//                                                ((AdminActivity) mContext).getSupportFragmentManager()
//                                                        .beginTransaction()
//                                                        .replace(R.id.containerFragments, new OrdersListFragment(), mContext.getString(R.string.fragmentOrdersList))
//                                                        .commit();

                                                return;
                                            } else {
                                                mAuth.createUserWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    updateAdmin(task, email, password, scope, login);

                                                                } else {
                                                                    login.setAlpha(1f);
                                                                    login.setEnabled(true);
                                                                    updateProgress.setVisibility(View.GONE);
                                                                    Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            }

                                                        });
                                            }
                                        }
                                    });
                        } else {
                            updateProgress.setVisibility(View.GONE);
                            login.setAlpha(1f);
                            login.setEnabled(true);
                            Toast.makeText(mContext, R.string.bad_username_password, Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    updateProgress.setVisibility(View.GONE);
                    login.setAlpha(1f);
                    login.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                updateProgress.setVisibility(View.GONE);
                login.setAlpha(1f);
                login.setEnabled(true);
            }
        });
    }


    private void updateAdmin(@NonNull Task<AuthResult> task, String email, String password, String scope, final LinearLayout login) {
        String uid = task.getResult().getUser().getUid();
        HashMap hashMap1 = new HashMap<String, Object>();
        hashMap1.put(mContext.getString(R.string.db_field_email), email);
        hashMap1.put(mContext.getString(R.string.db_field_password), password);
        hashMap1.put(mContext.getString(R.string.db_field_user_id), uid);
        hashMap1.put(mContext.getString(R.string.db_field_scope), scope);

        myRef
                .child(mContext.getString(R.string.db_admins_node))
                .child("-LWMr52_PtC18p_hsgcj")
                .updateChildren(hashMap1)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        updateProgress.setVisibility(View.GONE);
                        login.setAlpha(1f);
                        login.setEnabled(true);
                        ((AdminActivity) mContext).getSupportFragmentManager()
                                .popBackStackImmediate();
                        ((AdminActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.containerFragments, new OrdersListFragment(), mContext.getString(R.string.fragmentOrdersList))
                                .commit();

                    }
                });
    }

    public void updateProgress(ProgressBar registerProgress) {

        if (registerProgress != null) {
            this.updateProgress = registerProgress;
        }
    }

    public void searchQueryTextByUsername(Boolean query) {
        Log.d("TAG1234", "searchQueryTextByUsername: " + query);
        Query queryDB = myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_admin_approved))
                .equalTo(query);
        queryDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //there's a shop matching with particular shop name
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    Collection<Object> objectCollection = hashMap.values();

                    Log.v("DATASNAPSHOT:", dataSnapshot.toString());
                    ShopProfileSettings shopProfileSettings = null;

//                    for (Iterator<Object> it = objectCollection.iterator(); it.hasNext(); ) {
//                        /**
//                         *
//                         * Username is always unique.
//                         * So it's always going to return 1 result only
//                         */
//                        HashMap map = (HashMap) it.next();
//                        shopProfileSettings = new ShopProfileSettings(
//                                (String) map.get(mContext.getString(R.string.db_field_user_id))
//                                , (String) map.get(mContext.getString(R.string.db_field_first_name))
//                                , (String) map.get(mContext.getString(R.string.db_field_last_name))
//                                , (String) map.get(mContext.getString(R.string.db_field_user_name))
//                                , (String) map.get(mContext.getString(R.string.db_field_email))
//                                , (String) map.get(mContext.getString(R.string.db_field_scope))
//                                , (String) map.get(mContext.getString(R.string.db_field_shop_address))
//                                , (String) map.get(mContext.getString(R.string.db_field_city))
//                                , (String) map.get(mContext.getString(R.string.db_field_country))
//                                , (Boolean) map.get(mContext.getString(R.string.db_field_admin_approved))
//                                , (String) map.get(mContext.getString(R.string.db_field_shop_category))
//                                , (String) map.get(mContext.getString(R.string.db_field_profile_image_url))
//                                , (String) map.get(mContext.getString(R.string.db_field_mall_id))
//                        );
//                    }
//                    SearchUsernameFragment searchFragment = new SearchUsernameFragment();
//                    searchFragment.setDataSet(shopProfileSettings);
//
//                    if (activityName.equals(mContext.getString(R.string.shopsListFragment))) {
//                        ((FragmentActivity) mContext).getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.fragmentContainer, searchFragment, mContext.getString(R.string.searchUsernameFragment))
//                                .commitAllowingStateLoss();
//
//                    }
                } else {
                    //No Results Found

//                    SearchUsernameFragment searchFragment = new SearchUsernameFragment();
//                    if (activityName.equals(mContext.getString(R.string.shopsListFragment))) {
//                        ((FragmentActivity) mContext).getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.fragmentContainer, searchFragment, mContext.getString(R.string.searchUsernameFragment))
//                                .commitAllowingStateLoss();
//
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


/**
 * mAuth
 * .signInWithEmailAndPassword(email, password)
 * .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
 *
 * @Override public void onSuccess(AuthResult authResult) {
 * <p>
 * if (authResult.getUser().isEmailVerified()) {
 * updateProgress.setVisibility(View.GONE);
 * login.setEnabled(true);
 * login.setAlpha(1f);
 * Boolean isPopped = ((AdminActivity) mContext).getSupportFragmentManager()
 * .popBackStackImmediate();
 * ((AdminActivity) mContext).getSupportFragmentManager()
 * .beginTransaction()
 * .replace(R.id.containerFragments, new OrdersListFragment(), mContext.getString(R.string.fragmentOrdersList))
 * .commit();
 * if (isPopped) {
 * Log.d("TAG1234", "onSuccess: Popped");
 * }
 * <p>
 * } else {
 * updateProgress.setVisibility(View.GONE);
 * login.setEnabled(true);
 * login.setAlpha(1f);
 * authResult.getUser().sendEmailVerification();
 * Toast.makeText(mContext, mContext.getString(R.string.email_verification_required), Toast.LENGTH_SHORT).show();
 * }
 * }
 * })
 * .addOnFailureListener(new OnFailureListener() {
 * @Override public void onFailure(@NonNull Exception e) {
 * updateProgress.setVisibility(View.GONE);
 * login.setEnabled(true);
 * login.setAlpha(1f);
 * Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
 * }
 * });
 */

