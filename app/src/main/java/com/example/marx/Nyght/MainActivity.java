package com.example.marx.Nyght;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    private String facebook_id, first_name, middle_name, last_name, full_name, profile_gender, profile_image, profile_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences profile_info = getSharedPreferences(getString(R.string.user_details), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = profile_info.edit();

        Log.d(getString(R.string.MainActivity_log).toString(), "Starting Main Activity");
        // Check for first time log-in
        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }

        Log.d(getString(R.string.MainActivity_log).toString(), "User logged in, proceeding to initialize profile");
        initializeProfile(profile_info, editor);
        Toast.makeText(MainActivity.this, "Welcome! " + first_name, Toast.LENGTH_SHORT).show();

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ClubFragment()).commit();
        mBottomBar.setItemsFromMenu(R.menu.menu_icons, new OnMenuTabSelectedListener() {

            @Override
            public void onMenuItemSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.BottomMenuIconOneHome) {
                    ClubFragment f = new ClubFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (menuItemId == R.id.BottomMenuIconTwoSearch) {
                    SearchFragment f = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (menuItemId == R.id.BottomMenuIconThreeChat) {
                    ChatFragment f = new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (menuItemId == R.id.BottomMenuIconFourAccount) {
                    Bundle account_bundle = prepareAccountBundle();
                    AccountFragment f = new AccountFragment();
                    f.setArguments(account_bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                }
            }
        });
    }

    private Bundle prepareAccountBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.profile_image), this.profile_image);
        return bundle;
    }

    private void initializeProfile(SharedPreferences profile_info, SharedPreferences.Editor editor) {

        // Check if type is facebook info bundle.
        Bundle extras = this.getIntent().getExtras();
        if (extras != null && extras.get(getString(R.string.bundle_name)).equals(getString(R.string.facebook_bundle_name))){
            // Only get here if facebook bundle is passed in. For first time logged in users.
            // REMINDER UPDATE conditional statement
            Log.d(getString(R.string.MainActivity_log), extras.getString(getString(R.string.bundle_name)));
            this.facebook_id = extras.getString(getString(R.string.facebook_id));
            this.first_name = extras.getString(getString(R.string.first_name));
            this.middle_name = extras.getString(getString(R.string.middle_name));
            this.last_name = extras.getString(getString(R.string.last_name));
            this.full_name = extras.getString(getString(R.string.full_name));
            this.profile_email = extras.getString(getString(R.string.profile_email));
            this.profile_gender = extras.getString(getString(R.string.profile_gender));
            this.profile_image = extras.getString(getString(R.string.profile_image));
            saveProfile(editor);
            Log.d(getString(R.string.MainActivity_log), "Profile of " + full_name + " is initialized, profile is saved");
        }
        else {
            retrieveProfile(profile_info);
            Log.d(getString(R.string.MainActivity_log), "Profile of " + full_name + " is already initialized");
        }
    }

    private void retrieveProfile(SharedPreferences profile_info) {
        this.facebook_id = profile_info.getString(getString(R.string.facebook_id), null);
        this.first_name = profile_info.getString(getString(R.string.first_name), null);
        this.middle_name = profile_info.getString(getString(R.string.middle_name), null);
        this.last_name = profile_info.getString(getString(R.string.last_name), null);
        this.full_name = profile_info.getString(getString(R.string.full_name), null);
        this.profile_email = profile_info.getString(getString(R.string.profile_email), null);
        this.profile_gender = profile_info.getString(getString(R.string.profile_gender), null);
        this.profile_image = profile_info.getString(getString(R.string.profile_image), null);
        Log.d(getString(R.string.MainActivity_log), "Profile retrieved success");

    }

    // Saves the profile information of first time logged in users.
    private void saveProfile(SharedPreferences.Editor editor) {
        // committing profile information into sharedPreferences
        editor.putString(getString(R.string.facebook_id), facebook_id);
        editor.putString(getString(R.string.first_name), first_name);
        editor.putString(getString(R.string.middle_name), middle_name);
        editor.putString(getString(R.string.last_name), last_name);
        editor.putString(getString(R.string.full_name), full_name);
        editor.putString(getString(R.string.profile_email), profile_email);
        editor.putString(getString(R.string.profile_gender), profile_gender);
        editor.putString(getString(R.string.profile_image), profile_image);
        editor.commit();
        return;
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        SharedPreferences profile_info = getSharedPreferences(getString(R.string.user_details), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = profile_info.edit();
        LoginManager.getInstance().logOut();
        editor.clear();
        Log.d(getString(R.string.MainActivity_log), "Cleared current user info in sharedPreferences");
        goLoginScreen();
    }
    protected String getProfileImageUrl() {
        return this.profile_image;
    }
}
