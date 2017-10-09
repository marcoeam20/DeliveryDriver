package com.example.hendryshanedeguia.driverretrieval;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity {

    Button btnAccept;
    TextView OrderId;
    String status;

    DatabaseReference mRef, completedDB, pendingDB, driverHistory,driverHistoryStatus,driverHistoryIndi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        btnAccept = (Button) findViewById(R.id.btn_COAccept);
        OrderId = (TextView) findViewById(R.id.tv_COordId);

        final Intent x = getIntent();
        final String theID = x.getStringExtra("orderID");
        final String pendingid = x.getStringExtra("pendingID");
        final String user = x.getStringExtra("driverID");
        OrderId.setText(theID);

        completedDB = FirebaseDatabase.getInstance().getReference("Orders").child("Completed Orders");
        pendingDB = FirebaseDatabase.getInstance().getReference("Orders").child("Pending Orders").child(pendingid);
        driverHistory = FirebaseDatabase.getInstance().getReference("Drivers").child(user);
        driverHistoryStatus = FirebaseDatabase.getInstance().getReference("Drivers").child(user);
        driverHistoryIndi = FirebaseDatabase.getInstance().getReference("Drivers").child(user).child("order(s)");




        mRef = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(OrderId.getText().toString());
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map updateStatus = new HashMap();
                updateStatus.put("orderStatus", "Completed");
                mRef.updateChildren(updateStatus);



                String completedUID = completedDB.push().getKey();
                Map transferData = new HashMap();
                transferData.put("CompletedOrderID", theID);
                completedDB.child(completedUID).setValue(transferData);

                final Map driverData = new HashMap();
                driverData.put("orderID", theID);
                driverHistoryStatus.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         status = dataSnapshot.child("hasHistory").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(TextUtils.equals(status,"false")){
                    driverHistory.child("order(s)").push().setValue(driverData);
                }
                else
                {
                 driverHistoryIndi.push().setValue(driverData);
                }

                pendingDB.removeValue();

                Intent home = new Intent(getApplicationContext(), CompletedActivity.class);
                home.putExtra("driverID", user);
                startActivity(home);

            }


        });



    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {

            Intent x = getIntent();
            String user = x.getStringExtra("driverID");
            Intent back = new Intent(getApplicationContext(), HomeActivity.class);
            back.putExtra("driverID", user);
            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }

    }


