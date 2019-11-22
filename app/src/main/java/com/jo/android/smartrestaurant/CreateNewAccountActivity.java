package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.model.User;

public class CreateNewAccountActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextConfirmPassWord, editTextPhoneNumber;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextEmail = findViewById(R.id.edit_text_new_email);
        editTextPassword = findViewById(R.id.edit_text_new_password);
        editTextConfirmPassWord = findViewById(R.id.edit_text_confirm_new_password);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone);
        buttonRegister = findViewById(R.id.btn_create_account);
        progressBar = findViewById(R.id.progres_bar_register);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
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
                                     sendUserTologonActivity();
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

    private void sendUserTologonActivity() {
        Intent intent=new Intent(CreateNewAccountActivity.this,LoginActvity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
