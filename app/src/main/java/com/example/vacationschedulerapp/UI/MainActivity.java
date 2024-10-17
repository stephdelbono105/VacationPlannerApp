package com.example.vacationschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vacationschedulerapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


//Main activity and sign in screen


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button signInButton = findViewById(R.id.button);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);



        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }



        signInButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateCredentials(username, password)) {

                    Intent intent = new Intent(MainActivity.this, VacationList.class);
                   // intent.putExtra("test", "Information Sent");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validateCredentials(String username, String password) {
        if (username==null || username.isEmpty()) {
            Toast.makeText(this, "Please Enter a Username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!username.matches("^[a-zA-Z0-9]*$")) {
            Toast.makeText(this, "Username cannot contain spaces or special characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password==null || password.isEmpty()) {
            Toast.makeText(this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8 || !password.matches(".*\\d.*")){
            Toast.makeText(this, "Password must be at least 8 characters long and contain at least one number", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Password Hashing
        String hashedInputPassword = hashPassword(password);

        //Validate hashed password
        if (!hashedInputPassword.equals(hashPassword(password))) {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




}

