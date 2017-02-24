package com.example.marx.Nyght;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    // Firebase auths
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Facebook set up
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //Firebase set up, start listener
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    Log.d(getString(R.string.LoginActivity_log), "onAuthStateChanged:signed in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(getString(R.string.LoginActivity_log), "onAuthStateChanged:signed_out");
                }
            }
        };

        // initialize login button.
        loginButton = (LoginButton) findViewById(R.id.loginButton);

        // collect profile of user.
        retrieveProfile(loginButton);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void retrieveProfile(LoginButton loginButton) {
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // Retrieve information
                Profile userProfile = Profile.getCurrentProfile();
                Log.d(getString(R.string.LoginActivity_log), "Login Success. Fetching Profile info from: " + userProfile.getFirstName());

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
                handleFacebookAccessToken(loginResult.getAccessToken());
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

    protected void handleFacebookAccessToken(AccessToken accessToken) {
       // Log.d(getString(R.string.LoginActivity_log), "These are the permissions allowed access: " + AccessToken.getCurrentAccessToken().getPermissions().toString());
        Log.d(getString(R.string.LoginActivity_log), "Exchanging Facebook AccessToken to Firebase credentials");
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        Log.d(getString(R.string.LoginActivity_log), credential.toString());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(getString(R.string.LoginActivity_log), "signInWithCredential:onComplete:" + task.isSuccessful());
                        goMainScreen();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(getString(R.string.LoginActivity_log), "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
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
