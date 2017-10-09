package com.example.hendryshanedeguia.driverretrieval;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText regName;
    private EditText regUsername;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfPassword;
    private Button reg_btnSubmit;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ProgressDialog regProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //id referencing
        regName = (EditText) findViewById(R.id.txtName);
        regUsername = (EditText) findViewById(R.id.txtUsername);
        regEmail = (EditText) findViewById(R.id.txtEmail);
        regPassword = (EditText) findViewById(R.id.txtPassword);
        regConfPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        reg_btnSubmit = (Button) findViewById(R.id.Ver_btnSubmit);

        mAuth = FirebaseAuth.getInstance();

        regProgress = new ProgressDialog(this);

        //when button is clicked
        reg_btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get String Editable Text
                String name = regName.getEditableText().toString();
                String username = regUsername.getEditableText().toString();
                String email = regEmail.getEditableText().toString();
                String password = regPassword.getEditableText().toString();
                String confpassword = regConfPassword.getEditableText().toString();

                //if fields are empty
                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(username) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(confpassword)){

                    //Display Progress Bar
                    regProgress.setTitle("Registering Driver");
                    regProgress.setMessage("Please wait while we register your account.");
                    regProgress.setCanceledOnTouchOutside(false);
                    regProgress.show();

                    registerUser(name, username, email, password);

                } else {

                    Toast.makeText(getApplicationContext(), "Please check empty fields.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void registerUser(final String name, final String username, final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //get current user
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String driver_id = current_user.getUid();
                    mRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(driver_id);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> driverMap = new HashMap<String, String>();
                    driverMap.put("name", name);
                    driverMap.put("username", username);
                    driverMap.put("email", email);
                    driverMap.put("image", "default");
                    driverMap.put("thumb_image", "default");
                    driverMap.put("device_token", device_token);

                    mRef.setValue(driverMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                regProgress.dismiss();

                                //Go to Main Activity
                                Intent registerIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(registerIntent);
                                finish();


                            }
                        }
                    });

                } else {

                    regProgress.hide();

                    Toast.makeText(getApplicationContext(), "Problem with Registering your Account. Please check fields.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
