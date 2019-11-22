package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    public static final String USER_PASSWORD="user_password";
    public static final String USER_EMAIL="user_email";

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);

        String email=Paper.book().read(USER_EMAIL);
        String password=Paper.book().read(USER_PASSWORD);
        if(email!=""&&password!=""){

            if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){

                sendUserToHome();

            }
            else{
                sendUserToLogin();
            }


        }
        else{

            sendUserToLogin();


        }



    }

    private void sendUserToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActvity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        startActivity(intent);
    }

    private void sendUserToHome() {
        Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        startActivity(intent);

    }
}
