package com.example.hendryshanedeguia.driverretrieval;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView Title = (TextView) findViewById(R.id.Main_Title);
        Typeface tx = Typeface.createFromAsset(getApplicationContext().getAssets(), "BebasNeue.otf");
        Title.setTypeface(tx);

        //id referencing
        btnLogin = (Button) findViewById(R.id.Main_btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //when button is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when you want to go to next activity
                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

    }

}
