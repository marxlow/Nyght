package com.example.marx.Nyght;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Marx on 2/13/2017.
 */

public class AccountFragment extends Fragment {

    private CircleImageView profile_image;
    private TextView profile_fullname, profile_gender, profile_email;
    private String  full_name, gender, email;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(getString(R.string.AccountFragment_Log),"In account Page now");
        // De-permit strict mode for connection.getInputStream()
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // initialize view
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        initView(v);

        return v;
    }

    private void initView(View v) {

        // Initialize profile_image
        profile_image = (CircleImageView) v.findViewById(R.id.profile_image);
        profile_image.setImageBitmap(getBitMap(getArguments().getString(getString(R.string.profile_image))));
        profile_image.setBorderWidth(10);
        Log.d(getString(R.string.AccountFragment_Log),"Initialized ProfileImage");

        // Initializing profile details
        this.full_name = getArguments().getString(getString(R.string.full_name));
        profile_fullname = (TextView) v.findViewById(R.id.profile_fullname);
        profile_fullname.setText(this.full_name);
        Log.d(getString(R.string.AccountFragment_Log),"Initialized profile name: " + this.full_name);

        this.gender = getArguments().getString(getString(R.string.profile_gender));
        profile_gender = (TextView) v.findViewById(R.id.profile_gender);
        profile_gender.setText(this.gender);
        Log.d(getString(R.string.AccountFragment_Log),"Initialized profile gender: " + this.gender);

        this.email = getArguments().getString(getString(R.string.profile_email));
        profile_gender = (TextView) v.findViewById(R.id.profile_email);
        profile_gender.setText(this.email);
        Log.d(getString(R.string.AccountFragment_Log),"Initialized profile email: " + this.email);

    }

    private Bitmap getBitMap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
