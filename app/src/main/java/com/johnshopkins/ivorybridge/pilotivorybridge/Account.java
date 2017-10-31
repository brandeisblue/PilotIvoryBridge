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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account extends AppCompatActivity implements View.OnClickListener {

    private EditText inputNewPassword, inputConfirmNewPass;
    private Button btnChangePassword, btnSignOut;
    private LinearLayout activityAccount;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Setting up Views
        inputNewPassword = (EditText) findViewById(R.id.account_new_password_edittext);
        inputConfirmNewPass = (EditText) findViewById(R.id.account_confirm_new_password_edittext);
        btnChangePassword = (Button) findViewById(R.id.account_btn_change_password);
        btnSignOut = (Button) findViewById(R.id.account_btn_signout);
        activityAccount = (LinearLayout) findViewById(R.id.account_activity);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_btn_change_password:
                changePassword(inputNewPassword.getText().toString(),
                        inputConfirmNewPass.getText().toString());
                break;
            case R.id.account_btn_signout:
                logoutUser();
                break;
        }
    }

    private void changePassword(String newPassword, String confirmPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (newPassword.equals(confirmPassword)) {
            user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(activityAccount,
                                "Password changed",
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(activityAccount,
                                "Password is not changed",
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(activityAccount,
                    "Passwords do not match. Please confirm your password",
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(Account.this, MainActivityLogIn.class));
            finish();
        }
    }

}
