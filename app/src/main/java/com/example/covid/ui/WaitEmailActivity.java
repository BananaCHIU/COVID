package com.example.covid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.covid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

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
//Declare the timer
        Timer t = new Timer();
//Set the schedule function and rate

            t.scheduleAtFixedRate(new TimerTask() {

                                      @Override
                                      public void run() {
                                          //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                          mAuth.getCurrentUser().reload();
                                          if (mAuth.getCurrentUser().isEmailVerified()) {
                                              t.cancel();
                                              WaitEmailActivity.this.finish();
                                          }
                                      }

                                  },
//Set how long before to start calling the TimerTask (in milliseconds)
                    0,
//Set the amount of time between each execution (in milliseconds)
                    5000);

        /*
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (!mAuth.getCurrentUser().isEmailVerified()) {
                    CountDownTimer cdt5 = new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(TAG, "1000 ms passed");
                        }

                        @Override
                        public void onFinish() {
                            Log.d(TAG, "user reloaded");
                            mAuth.getCurrentUser().reload();
                        }
                    }.start();
                }
            }

        });

        WaitEmailActivity.this.finish();

*/
    }

    @Override
    public void onBackPressed(){
        //Do nothing
    }
}
