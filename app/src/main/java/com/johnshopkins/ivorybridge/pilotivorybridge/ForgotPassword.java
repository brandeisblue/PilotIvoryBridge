package com.johnshopkins.ivorybridge.pilotivorybridge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    private EditText inputEmail;
    private Button btnRetrievePassword;
    private LinearLayout activityForgot;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Setup Views
        inputEmail = (EditText) findViewById(R.id.forgotpassword_email_edittext);
        btnRetrievePassword = (Button) findViewById(R.id.forgotpassword_btn_change_password);
        activityForgot = (LinearLayout) findViewById(R.id.forgotpassword_activity);

        btnRetrievePassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgotpassword_btn_change_password:
                resetPassword(inputEmail.getText().toString());
        }
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Snackbar snackbar = Snackbar.make(activityForgot,
                                    "New password was sent to your e-mail",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }
}
