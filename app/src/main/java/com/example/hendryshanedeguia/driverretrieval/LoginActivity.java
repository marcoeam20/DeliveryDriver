package com.example.hendryshanedeguia.driverretrieval;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText log_txtEmail;
    private EditText log_txtPassword;
    private Button log_btnSubmit;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ProgressDialog logProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView Title = (TextView) findViewById(R.id.txtTitle);
        Typeface tx = Typeface.createFromAsset(getApplicationContext().getAssets(), "BebasNeue.otf");
        Title.setTypeface(tx);

        //id referencing
        mAuth = FirebaseAuth.getInstance();

        log_txtEmail = (EditText) findViewById(R.id.txtEmail);
        log_txtPassword = (EditText) findViewById(R.id.txtPassword);
        log_btnSubmit = (Button) findViewById(R.id.log_btnSubmit);

        logProgress = new ProgressDialog(this);



        //Firebase Authentication Listener - calls the Authentication inside Firebase
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //get the current user
                FirebaseUser driver = FirebaseAuth.getInstance().getCurrentUser();

                if(driver!=null){
                    //Dismiss progress dialog after user is successfully verified
                    logProgress.dismiss();

                    //Go to Main Activity
                    Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    String driverid = driver.getUid();
                    mainIntent.putExtra("driverID", driverid);
                    startActivity(mainIntent);
                    finish();
                    return;

                }

            }
        };

        //when button is clicked
        log_btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get String Editable Text
                String email = log_txtEmail.getEditableText().toString();
                String password = log_txtPassword.getEditableText().toString();



                loginUser(email, password);


            }
        });

    }



    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //if user inputted is correct
                if(!task.isSuccessful()){





                    logProgress.hide();

                    //Display an Error Message if user cannot login
                    Toast.makeText(getApplicationContext(), "Cannot verify your credentials. Please try again", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //add Authentication Listener
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //remove Authentication Listener
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
