package com.example.covid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.covid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class WaitEmailActivity extends AppCompatActivity {

    private static final String TAG = "WaitEmailActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_email);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            public void run() {
                while (!mAuth.getCurrentUser().isEmailVerified()) {
                    mAuth.getCurrentUser().reload();
                }
                WaitEmailActivity.this.finish();
            }
        }).start();

    }

    @Override
    public void onBackPressed(){
        //Do nothing
    }
}
