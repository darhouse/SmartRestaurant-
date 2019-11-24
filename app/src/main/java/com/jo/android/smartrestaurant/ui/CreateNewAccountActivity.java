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
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.R;
import com.jo.android.smartrestaurant.model.User;

import java.util.regex.Pattern;

public class CreateNewAccountActivity extends AppCompatActivity {

    private EditText editTextFirstName,
            editTextLastName,
            editTextEmail,
            editTextPassword,
            editTextConfirmPassWord,
            editTextPhoneNumber;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView alreadyHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        init();
        mAuth = FirebaseAuth.getInstance();

    }

    private void init(){
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextEmail = findViewById(R.id.edit_text_new_email);
        editTextPassword = findViewById(R.id.edit_text_new_password);
        editTextConfirmPassWord = findViewById(R.id.edit_text_confirm_new_password);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone);
        buttonRegister = findViewById(R.id.btn_create_account);
        progressBar = findViewById(R.id.progres_bar_register);
        alreadyHaveAnAccount = findViewById(R.id.tv_have_account);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    createNewAccount();
                }

            }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewAccountActivity.this,LoginActvity.class);
                startActivity(intent);
            }
        });
    }

    private void createNewAccount() {
        progressBar.setVisibility(View.VISIBLE);
        String password = editTextPassword.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String firstName = editTextFirstName.getText().toString();
        final String lastName = editTextLastName.getText().toString();
        final String phone = editTextPhoneNumber.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(firstName, lastName, email, phone);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        sendUserTologinActivity();
                                        Toast.makeText(CreateNewAccountActivity.this, "done", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(CreateNewAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(CreateNewAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void sendUserTologinActivity() {
        Intent intent=new Intent(CreateNewAccountActivity.this,LoginActvity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean isValid = true;
        String userFirstNameText = editTextFirstName.getText().toString();
        String userLastNameText = editTextLastName.getText().toString();
        String userEmailText = editTextEmail.getText().toString();
        String userPasswordText = editTextPassword.getText().toString();
        String userPasswordConfirmText = editTextConfirmPassWord.getText().toString();

        if(userFirstNameText.trim().isEmpty()){
            editTextFirstName.setError("Required");
            isValid = false;
        }else{
            editTextFirstName.setError(null);
        }

        if(userLastNameText.trim().isEmpty()){
            editTextLastName.setError("Required");
            isValid = false;
        }else{
            editTextLastName.setError(null);
        }

        if(userEmailText.trim().isEmpty()){
            editTextEmail.setError("Required");
            isValid = false;
        }else if (!(isValidEmail(userEmailText))){
            editTextEmail.setError("please enter a valid Email");
            isValid = false;
        }
        else{
            editTextEmail.setError(null);
        }


        if(userPasswordText.trim().isEmpty()){
            editTextPassword.setError("Required");
            isValid = false;
        }else if (userPasswordText.length() < 6 ){
            editTextPassword.setError("password should be more than 6 characters " );
            isValid = false;
        }
        else{
            editTextPassword.setError(null);
        }

        if(userPasswordConfirmText.trim().isEmpty()){
            editTextConfirmPassWord.setError("Required");
            isValid = false;
        }else{
            editTextConfirmPassWord.setError(null);
        }

        if(!(userPasswordConfirmText.equals(userPasswordText))){
            editTextPassword.setError("password does not match");
            editTextConfirmPassWord.setError("password does not match");
        }else{
            editTextPassword.setError(null);
            editTextConfirmPassWord.setError(null);
        }

        return isValid;
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
