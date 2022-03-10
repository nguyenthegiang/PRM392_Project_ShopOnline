package com.example.prm392_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    //Views
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    ProgressBar progressBar;
    Button btnSignup;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //get views
        editTextEmail = findViewById(R.id.editTextEmail2);
        editTextPassword = findViewById(R.id.editTextPassword2);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btnSignUp2);

        //create firebase
        mAuth = FirebaseAuth.getInstance();
    }
    public void btnSignUpClick(View view) {
        //Show ProgressBar, disable button
        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setEnabled(false);

        //Get data from editText
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        //Check value empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        //check confirm password
        else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show();
        }
        else {
            //create account with Firebase
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Hide ProgressBar, show button
                    progressBar.setVisibility(View.GONE);
                    btnSignup.setEnabled(true);

                    //check result
                    if (task.isSuccessful()) {
                        //success: notify & go to Login Activity
                        Toast.makeText(SignUpActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                        //close this activity
                        finish();
                    } else {
                        //fail: notify
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void btnLoginInsteadClick(View view) {
        //Change to MainActivity (Login)
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}