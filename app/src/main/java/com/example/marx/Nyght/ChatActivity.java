package com.example.marx.Nyght;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Marx on 3/10/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    FloatingActionButton send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d(getString(R.string.ChatActivity_Log), "In chat activity, with user: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());

        // Initialize buttons and layout
        initView();
    }

    private void initView() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://nyght-efd6b.firebaseio.com/");
        send_button = (FloatingActionButton) findViewById(R.id.messenger_send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.message_input);
                databaseReference.push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                input.setText("");
            }
        });
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message_list_item, databaseReference) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = (TextView)v.findViewById(R.id.message_text);
                messageUser = (TextView)v.findViewById(R.id.message_user);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
            }
        };
        listOfMessages.setAdapter(adapter);
        listOfMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listOfMessages.setStackFromBottom(true);
    }

}
