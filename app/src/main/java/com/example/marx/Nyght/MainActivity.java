package com.example.marx.Nyght;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ClubsFragment()).commit();
        mBottomBar.setItemsFromMenu(R.menu.menu_icons, new OnMenuTabSelectedListener() {

            @Override
            public void onMenuItemSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.BottomMenuIconOneHome) {
                    ClubsFragment f = new ClubsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (menuItemId == R.id.BottomMenuIconTwoSearch) {
                    SearchFragment f = new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (menuItemId == R.id.BottomMenuIconThreeChat) {
                    ChatFragment f = new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                } else if (menuItemId == R.id.BottomMenuIconFourAccount) {
                    AccountFragment f = new AccountFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                }
            }
        });
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
