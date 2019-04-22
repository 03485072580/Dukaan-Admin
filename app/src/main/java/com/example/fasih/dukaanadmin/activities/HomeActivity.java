package com.example.fasih.dukaanadmin.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.fasih.dukaanadmin.R;
import com.example.fasih.dukaanadmin.fragments.Chat_user;
import com.example.fasih.dukaanadmin.fragments.ManageUsersFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ArrayList<String> mylist;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);
        mylist = new ArrayList<String>();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        replaceFragment(new ManageUsersFragment(), "Home");

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    replaceFragment(new ManageUsersFragment(), "Home");
//                    mTextMessage.setText(R.string.title_home);
                    return true;
//                case R.id.navigation_dashboard:
////                    replaceFragment(new Chat_Fragment(), "Profile");
////                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
                case R.id.navigation_notifications:
//                    startActivity(new Intent(HomeActivity.this,Chat_user.class));
                    replaceFragment(new Chat_user(), "chat");
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment targetFragment, String name) {

//        drawer.closeDrawers();

//        titletxt.setText(name);
        mylist.add(name);


        View view = this.getCurrentFocus();
        FragmentManager fm = getFragmentManager();
        if (view != null) {

            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }


//        Log.d("TAG", targetFragment.getTag());
//        Addparent.setVisibility(View.GONE);
//        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fm.beginTransaction()
                .replace(R.id.container, targetFragment, "fragment")
                .addToBackStack(name)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        Log.i("MYLIST", mylist.toString());
    }
}
