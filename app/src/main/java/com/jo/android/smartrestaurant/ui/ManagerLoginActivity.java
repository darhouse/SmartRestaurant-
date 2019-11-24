package com.jo.android.smartrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jo.android.smartrestaurant.R;

import io.paperdb.Paper;

import static com.jo.android.smartrestaurant.ui.MainActivity.USER_EMAIL;
import static com.jo.android.smartrestaurant.ui.MainActivity.USER_PASSWORD;

public class ManagerLoginActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText editTextResturanName, editTextEmail, editTextPassword;
    Button btnManagerLogin;
    TextView textViewGotoUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        init();


    }


    private void init() {
    progressBar = findViewById(R.id.progres_bar_login);
    editTextResturanName = findViewById(R.id.edit_text_resturan_name);
    editTextEmail = findViewById(R.id.edit_text_manager_email);
    editTextPassword = findViewById(R.id.edit_text_manager_password);
    btnManagerLogin = findViewById(R.id.btn_manager_login);
    textViewGotoUserLogin = findViewById(R.id.tv_go_to_user_login);

    textViewGotoUserLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendToUserLoginActivity();
        }
    });

    btnManagerLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(isValidate()) {
               login();
            }
        }
    });
    }


    private boolean isValidate() {
        boolean isValid= true;
        String name = editTextResturanName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(name.trim().isEmpty()){
            editTextResturanName.setError("Required");
            isValid=false;
        }else{
            editTextResturanName.setError(null);
        }

        if(email.trim().isEmpty()){
            editTextEmail.setError("Required");
            isValid=false;
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

    private void sendToManagerHomeActivity() {
        Intent intent = new Intent(ManagerLoginActivity.this,ManagerHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void sendToUserLoginActivity() {
        Intent intent = new Intent(ManagerLoginActivity.this,LoginActvity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

                    sendToManagerHomeActivity();
                } else {
                    Toast.makeText(ManagerLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}
