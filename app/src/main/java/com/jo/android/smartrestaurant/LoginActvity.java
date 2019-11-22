package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

import static com.jo.android.smartrestaurant.MainActivity.USER_EMAIL;
import static com.jo.android.smartrestaurant.MainActivity.USER_PASSWORD;

public class LoginActvity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String sentCode;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewNewAccount;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actvity);
        getSupportActionBar().hide();
        Paper.init(this);

        textViewNewAccount = findViewById(R.id.tv_new_account);
        buttonLogin = findViewById(R.id.btn_login);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        progressBar = findViewById(R.id.progres_bar_login);

        textViewNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToCreateAccountActivity();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();

            }
        });

    }



    private void login() {
        progressBar.setVisibility(View.VISIBLE);

        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Paper.book().write(USER_EMAIL,email);
                    Paper.book().write(USER_PASSWORD,password);

                    sendToUserHomeActivity();

                } else {
                    Toast.makeText(LoginActvity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void sendToUserHomeActivity() {
        Intent intent = new Intent(LoginActvity.this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);


        startActivity(intent);
        finish();


    }


    private void sendToCreateAccountActivity() {
        Intent intent = new Intent(LoginActvity.this, CreateNewAccountActivity.class);

        startActivity(intent);
    }


}
