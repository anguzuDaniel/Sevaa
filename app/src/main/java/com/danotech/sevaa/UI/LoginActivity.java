package com.danotech.sevaa.UI;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.danotech.sevaa.MainActivity;
import com.danotech.sevaa.Model.Savings;
import com.danotech.sevaa.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private Button signInButton;
    private TextView welcomeBackMessage;
    private TextView signupPage;
    private EditText signInEmail, signInPassword;
    FirebaseAuth firebaseAuth;
    private TextView signInMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.btn_sign_in);
        welcomeBackMessage = findViewById(R.id.welcome_message);
        firebaseAuth = FirebaseAuth.getInstance();
        signupPage = findViewById(R.id.signup_page);

        signInEmail = findViewById(R.id.sign_in_email);
        signInPassword = findViewById(R.id.sign_in_password);
        signInMessage = findViewById(R.id.sign_in_message);

        // set on click listener for sign in button
        // when clicked, send user to sign up page
        // TODO: 9/30/21 add sign in functionality
        signupPage.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // verify a user email address
        verifyGoogleEmailVerification();

        // Sets listener for sign in button
        // verifies that a user has entered correct email and password
        // if so, send user to home page
        signInButton.setOnClickListener(v -> {
            String email = signInEmail.getText().toString();
            String password = signInPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                signInEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                signInPassword.setError("Password is required");
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user.isEmailVerified() ) {
                        // User's email is verified, proceed with login
                        // Start the next activity or perform other operations here
                        updateUI(user);
                    } else {
                        // User's email is not verified, show an error message
                        Toast.makeText(getApplicationContext(), "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    signInMessage.setText("Please verify that the email is correct.");
                    // An error occurred, show an error message
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void updateUI(Object object) {
        if (object instanceof FirebaseUser) {
            // Update UI for signed-in Firebase user
            FirebaseUser user = (FirebaseUser) object;

            // User is signed in, update UI accordingly
            // Show a welcome message
            String welcomeMessage = "Welcome, " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
            welcomeBackMessage.setText(welcomeMessage);


            Savings savings = new Savings();
            savings.update(3000.0, 500.0);

            // Hide the sign-in button
            signInButton.setEnabled(false);
            // Show the sign-out button
            // signOutButton.setVisibility(View.VISIBLE);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (object instanceof GoogleSignInAccount) {
            GoogleSignInAccount account = (GoogleSignInAccount) object;
            // User is signed in, update UI accordingly
            // Show a welcome message
            String welcomeMessage = "Welcome, " + account.getDisplayName();
            welcomeBackMessage.setText(welcomeMessage);
            // Hide the sign-in button
            signInButton.setVisibility(View.GONE);
            // Show the sign-out button
//            signOutButton.setVisibility(View.VISIBLE);
        } else {
            // User is signed out, update UI accordingly
            // Show a message asking the user to sign in
            welcomeBackMessage.setText("Please sign in to continue");
            // Show the sign-in button
            signInButton.setVisibility(View.VISIBLE);
            // Hide the sign-out button
//            signOutButton.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void verifyGoogleEmailVerification() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        // Get the ID token and the auth code from the GoogleSignInAccount object
        if (account != null) {
            String idToken = account.getIdToken();
            String authCode = account.getServerAuthCode();


            // Create a GoogleAuthProvider credential using the ID token and auth code
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, authCode);

            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

            findViewById(R.id.sign_in_button).setOnClickListener((View.OnClickListener) this);
            updateUI(account);
            // rest of your code here
        } else {
            // handle the case where the account is null
        }
//        This will ensure that the getIdToken() method is only called if the GoogleSignInAccount object is not null.
    }
}