package com.example.prm392_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextEmail2);
        editTextPassword = findViewById(R.id.editTextPassword2);
    }
    public void btnSignUpClick(View view) {

    }

    public void btnLoginInsteadClick(View view) {
        //Change to MainActivity (Login)
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}