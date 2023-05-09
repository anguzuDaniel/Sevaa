package com.danotech.sevaa.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.danotech.sevaa.Model.Savings;
import com.danotech.sevaa.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private TextView signInPage;
    private TextInputEditText signupName, signupEmail, signupPassword, signupPasswordConfirm;
    private Button signupButton;
    private TextView signupMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // button that redirects user to sign in page
        signInPage = findViewById(R.id.sign_in_page);

        // button that signs up user
        signupButton = findViewById(R.id.signup_button);

        // text input fields for user to enter name, email, password, and confirm password
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupPasswordConfirm = findViewById(R.id.signup_password_confirm);


        // set on click listener for sign in button
        // when clicked, send user to sign up page
        // TODO: 9/30/21 add sign in functionality
        signInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // set on click listener for sign up button
        // when clicked, send user to sign up page
        // performs error checking to make sure all fields are filled out
        // TODO: 9/30/21 add sign up functionality
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String passwordConfirm = signupPasswordConfirm.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    signupName.setError("Please enter your name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    signupEmail.setError("Please enter an Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    signupPassword.setError("Please enter a password");
                    return;
                }

                if (TextUtils.isEmpty(passwordConfirm)) {
                    signupPasswordConfirm.setError("Please confirm your password");
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    signupPasswordConfirm.setError("Passwords do not match");
                    return;
                }

                firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                sendEmailVerification(user);

                                Savings savings = new Savings();
                                savings.update(3000.0, 500.0);
                            } else {
                                signupEmail.setError("Account was not created Successfully");
                                Toast.makeText(SignUpActivity.this, "Account creation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                signupMessage.setText("Please check your email for verification");
                startActivity(intent);
            } else {
                Toast.makeText(SignUpActivity.this, "Failed to send email verification: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}