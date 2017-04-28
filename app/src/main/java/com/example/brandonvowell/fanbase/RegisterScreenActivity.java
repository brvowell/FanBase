package com.example.brandonvowell.fanbase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegisterScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginScreenActivity";
    private EditText firstnameTextField;
    private EditText lastnameTextField;
    private EditText emailTextField;
    private EditText passwordTextField;
    private EditText passwordConfirmTextField;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //Grab UI Elements
        final Button btnCreateAccount = (Button) findViewById(R.id.create_account_button);
        firstnameTextField = (EditText) findViewById(R.id.firstname_text_field);
        lastnameTextField = (EditText) findViewById(R.id.lastname_text_field);
        emailTextField = (EditText) findViewById(R.id.email_reg_textfield);
        passwordTextField = (EditText) findViewById(R.id.password_reg_textfield);
        passwordConfirmTextField = (EditText) findViewById(R.id.passwordconfirm_text_field);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = firstnameTextField.getText().toString();
                lastName = lastnameTextField.getText().toString();
                email = emailTextField.getText().toString();
                password = passwordTextField.getText().toString();
                passwordConfirmation = passwordConfirmTextField.getText().toString();

                if (verifyAccountDetails()) {
                    createAccount(email, password);
                } else {
                    Toast.makeText(RegisterScreenActivity.this, "Invalid account details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private boolean verifyAccountDetails() {
        //if user account input is valid, return true. else return false
        if ((!firstName.isEmpty()) && (!lastName.isEmpty()) && (!email.isEmpty()) && (!password.isEmpty())
                && (!passwordConfirmation.isEmpty()) && (password.equals(passwordConfirmation))) {
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterScreenActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        transitionMainActivity();
                    }
                });
    }

    private void transitionMainActivity() {
        Intent nextScreen = new Intent(this, MainActivity.class);
        startActivityForResult(nextScreen, 0);
    }
}