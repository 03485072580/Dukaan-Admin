package com.example.fasih.dukaanadmin.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.dukaanadmin.R;
import com.example.fasih.dukaanadmin.fragments.AdminLoginFragment;


public class AdminActivity extends AppCompatActivity {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupAdminLoginScreen();
    }

    private void setupAdminLoginScreen() {
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.containerFragments
                        , new AdminLoginFragment()
                        , getString(R.string.fragmentAdminLogin))
                .addToBackStack(getString(R.string.fragmentAdminLogin))
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment adminLoginFragment = manager.findFragmentByTag(getString(R.string.fragmentAdminLogin));

        if (adminLoginFragment != null && !adminLoginFragment.isVisible()) {
            manager.beginTransaction()
                    .replace(R.id.containerFragments
                            , adminLoginFragment)
                    .commit();
        } else {
            finish();
        }
    }
}
