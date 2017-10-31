package com.johnshopkins.ivorybridge.pilotivorybridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp, btnForgotPass;
    TextView btnLogin;
    EditText inputEmail, inputPassword;
    LinearLayout signUpActivity;

    private FirebaseAuth mAuth;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Setting up views from the layout
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnForgotPass = (Button) findViewById(R.id.btn_signup_forgot_password);
        btnLogin = (TextView) findViewById(R.id.link_login);
        inputEmail = (EditText) findViewById(R.id.signup_input_email);
        inputPassword = (EditText) findViewById(R.id.signup_input_password);
        signUpActivity = (LinearLayout) findViewById(R.id.signup_activity);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        // Initiate Firebase
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup_forgot_password:
                startActivity(new Intent(SignUp.this, ForgotPassword.class));
                finish();
                break;
            case R.id.link_login:
                startActivity(new Intent(SignUp.this, MainActivityLogIn.class));
                finish();
                break;
            case R.id.btn_sign_up:
                signUpUser(inputEmail.getText().toString(), inputPassword.getText().toString());
                break;
        }
    }

    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            snackbar = Snackbar.make(signUpActivity,
                                    "Error: " + task.getException(),
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            snackbar = Snackbar.make(signUpActivity, "Register Success!",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
