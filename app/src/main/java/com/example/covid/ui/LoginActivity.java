package com.example.covid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.covid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private TextInputLayout text_email;
    private TextInputLayout text_username;
    private TextInputLayout text_password;
    private Button btn_login;
    private Button btn_reg;
    private ProgressBar progress_login;
    InputMethodManager imm;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        v = findViewById(android.R.id.content);
        text_email = findViewById(R.id.input_email);
        text_username = findViewById(R.id.input_name);
        text_password = findViewById(R.id.input_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_reg = findViewById(R.id.btn_register);
        progress_login = findViewById(R.id.progress_login);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_login.setVisibility(View.VISIBLE);
                login(text_email.getEditText().getText().toString(), text_password.getEditText().getText().toString());
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_username.getVisibility() == View.VISIBLE){
                    progress_login.setVisibility(View.VISIBLE);
                    register(text_email.getEditText().getText().toString(), text_password.getEditText().getText().toString());
                }else {
                    text_username.setVisibility(View.VISIBLE);
                    text_username.requestFocus();
                    imm.showSoftInput(text_username, InputMethodManager.SHOW_IMPLICIT);
                    Toast.makeText(LoginActivity.this, "New user. Please enter your user name", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validateForm(boolean needName) {
        boolean valid = true;

        String email = text_email.getEditText().getText().toString();
        if (TextUtils.isEmpty(email)) {
            text_email.setError("Required.");
            valid = false;
        } if (!isEmailValid(email)){
            text_email.setError("Wrong format.");
            valid = false;
        } else {
            text_email.setError(null);
        }

        String password = text_password.getEditText().getText().toString();
        if (TextUtils.isEmpty(password)) {
            text_password.setError("Required.");
            text_password.setErrorIconDrawable(null);
            valid = false;
        } else if (password.length() < 8){
            text_password.setError("Password must be longer than 8 characters");
            text_password.setErrorIconDrawable(null);
            valid = false;
        } else{
            text_password.setError(null);
        }

        if (needName){
            String name = text_username.getEditText().getText().toString();
            if (TextUtils.isEmpty(password)) {
                text_username.setError("Required.");
                valid = false;
            } else{
                text_password.setError(null);
            }
        }
        return valid;
    }

    private void login(String email, String password){

        if (!validateForm(false)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Snackbar.make(v, "Login failed. Please try again or register an new account", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            text_password.setError("");
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        progress_login.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void register(String email, String password) {
        if (!validateForm(true)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.reload();
                            if (!user.isEmailVerified()) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Email sent.");
                                                    Snackbar.make(v, "Email verification need. Please check your email.", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    Intent intent = new Intent(LoginActivity.this, WaitEmailActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Snackbar.make(v, "Registration failed.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        progress_login.setVisibility(View.INVISIBLE);
                    }
                });

    }

    @Override
    public void onBackPressed(){
    //Do nothing
    }

}
