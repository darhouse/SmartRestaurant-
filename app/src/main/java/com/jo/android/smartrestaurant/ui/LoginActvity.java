package com.jo.android.smartrestaurant.ui;

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
import com.jo.android.smartrestaurant.R;

import java.util.regex.Pattern;

import io.paperdb.Paper;

import static com.jo.android.smartrestaurant.ui.MainActivity.USER_EMAIL;
import static com.jo.android.smartrestaurant.ui.MainActivity.USER_PASSWORD;

public class LoginActvity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String sentCode;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewNewAccount;
    private ProgressBar progressBar;
    private TextView goToManagerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actvity);
        getSupportActionBar().hide();
        Paper.init(this);

        init();



    }
    private void init(){
        textViewNewAccount = findViewById(R.id.tv_new_account);
        buttonLogin = findViewById(R.id.btn_login);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        progressBar = findViewById(R.id.progres_bar_login);
        goToManagerLogin = findViewById(R.id.tv_go_to_manager);

        textViewNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToCreateAccountActivity();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidate()){
                    login();
                }else{
                    Toast.makeText(LoginActvity.this, "you must enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goToManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToManagerLogin();
            }
        });
    }

    private void sendToManagerLogin(){
        Intent intent = new Intent(LoginActvity.this,ManagerLoginActivity.class);
        startActivity(intent);
    }

    private boolean isValidate() {
        boolean isValid= true;
       String email = editTextEmail.getText().toString();
       String password = editTextPassword.getText().toString();

       if(email.trim().isEmpty()){
           editTextEmail.setError("Required");
           isValid=false;
       }
       else if (!(isValidEmail(email))){
           editTextEmail.setError("please enter a valid Email");
           isValid = false;
       }else{
           editTextEmail.setError(null);
       }

       if(password.trim().isEmpty()){
           editTextPassword.setError("Required");
           isValid=false;
       }else if(password.length() < 6 ){
           editTextPassword.setError("password must be 6 characters");
            isValid=false;
       }else{
           editTextPassword.setError(null);
       }
        return isValid;
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

    private boolean isValidEmail(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
