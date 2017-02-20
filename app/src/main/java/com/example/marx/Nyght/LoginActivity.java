package com.example.marx.Nyght;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Marx on 2/13/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String facebook_id, first_name, middle_name, last_name, full_name, profile_gender, profile_image, profile_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Facebook set up
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // initialize login button.
        loginButton = (LoginButton) findViewById(R.id.loginButton);

        // collect profile of user.
        retrieveProfile(loginButton);
    }

    private void retrieveProfile(LoginButton loginButton) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(getString(R.string.LoginActivity_log), "Login Success. Retrieving profile info...");
                // Retrieve information
                Profile userProfile = Profile.getCurrentProfile();
                Log.d(getString(R.string.LoginActivity_log), "Fetching Profile info from: " + userProfile.getFirstName());

                // initialize user details
                facebook_id = userProfile.getId();
                first_name = userProfile.getFirstName();
                middle_name = userProfile.getMiddleName();
                last_name = userProfile.getLastName();
                full_name = userProfile.getName();
                profile_image = userProfile.getProfilePictureUri(400, 400).toString();

                // Retrieve graph to get more details
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    profile_email = object.getString("email");
                                    profile_gender = object.getString("gender");
                                    full_name = object.getString("name");
                                } catch (JSONException e) {
                                    Log.d(getString(R.string.LoginActivity_log), e.toString());
                                }
                            }
                        });
                goMainScreen();
            }

            @Override
            public void onCancel() {
                Log.d(getString(R.string.LoginActivity_log), "login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(getString(R.string.LoginActivity_log), "error logging in");
            }
        });
    }


    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        // Pass retrieved information to MainActivity
        intent.putExtra(getString(R.string.bundle_name), getString(R.string.facebook_bundle_name));
        intent.putExtra(getString(R.string.facebook_id), facebook_id);
        intent.putExtra(getString(R.string.first_name), first_name);
        intent.putExtra(getString(R.string.middle_name), middle_name);
        intent.putExtra(getString(R.string.last_name), last_name);
        intent.putExtra(getString(R.string.full_name), full_name);
        intent.putExtra(getString(R.string.profile_gender), profile_gender);
        intent.putExtra(getString(R.string.profile_image), profile_image);
        intent.putExtra(getString(R.string.profile_email), profile_email);
        Log.d(getString(R.string.LoginActivity_log), "Passing intent to MainActivity");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
        callbackManager.onActivityResult(requestCode, resultCode, Data);
    }
}
