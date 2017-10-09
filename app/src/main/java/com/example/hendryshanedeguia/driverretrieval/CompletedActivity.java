package com.example.hendryshanedeguia.driverretrieval;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompletedActivity extends AppCompatActivity {

    public DatabaseReference ref, driverHistory;
    public List<OrderInformation> completedList;
    public OrderListAdapter adapter;
    ListView lv;

    public CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.appbarComplete);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("DELIVERED ORDERS");


        lv = (ListView) findViewById(R.id.lv_completed);
        completedList = new ArrayList<>();
        Intent x = getIntent();
         String userID = x.getStringExtra("driverID");


        driverHistory = FirebaseDatabase.getInstance().getReference("Drivers").child(userID).child("order(s)");

        driverHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String theID = snapshot.child("orderID").getValue().toString();
                    ref = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                OrderInformation oi = snapshot.getValue(OrderInformation.class);
                                if(TextUtils.equals(snapshot.child("orderID").getValue().toString(),theID)){
                                    //Toast.makeText(getApplicationContext(),theID,Toast.LENGTH_SHORT).show();
                                    completedList.add(oi);
                                }

                            }
                            adapter = new OrderListAdapter(CompletedActivity.this, R.layout.completed_order_layout, completedList);
                            lv.setAdapter(adapter);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

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
