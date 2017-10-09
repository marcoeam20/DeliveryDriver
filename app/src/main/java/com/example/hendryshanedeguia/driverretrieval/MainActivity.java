package com.example.hendryshanedeguia.driverretrieval;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity extends AppCompatActivity {
    public DatabaseReference ref;
    public List<OrderInformation> listOrders;
    public OrderListAdapter adapter;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ref = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders");
        lv =(ListView)findViewById(R.id.lv);
        listOrders = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent ordIntent = new Intent(MainActivity.this, OrderSummary.class);
                ordIntent.putExtra("Order Summary", lv.getItemIdAtPosition(i));
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
                adapter =  new OrderListAdapter(MainActivity.this,R.layout.new_order_layout,listOrders);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
