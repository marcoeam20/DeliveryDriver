package com.example.hendryshanedeguia.driverretrieval;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompletedActivity extends AppCompatActivity {

    public DatabaseReference ref;
    public List<OrderInformation> listOrders;
    public OrderListAdapter adapter;
    ListView lv;

    public CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);


        ref = FirebaseDatabase.getInstance().getReference("Orders");
        lv =(ListView)findViewById(R.id.lv_completed);
        listOrders = new ArrayList<>();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent ordIntent = new Intent(CompletedActivity.this, OrderSummary.class);
                ordIntent.putExtra("Completed Order Summary", lv.getItemIdAtPosition(i));
                startActivity(ordIntent);
            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    OrderInformation oi = snapshot.getValue(OrderInformation.class);
                    listOrders.add(oi);
                }
                adapter =  new OrderListAdapter(CompletedActivity.this,R.layout.completed_order_layout,listOrders);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
