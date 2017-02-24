package com.example.marx.Nyght;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Marx on 2/13/2017.
 */

public class ChatFragment extends Fragment {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        Log.d(getString(R.string.ChatFragment_Log), "In chat fragment");
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d(getString(R.string.ChatFragment_Log), FirebaseAuth.getInstance().getCurrentUser().toString());
        }
        return v;
    }
}
