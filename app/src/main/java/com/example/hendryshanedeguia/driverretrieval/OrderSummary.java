package com.example.hendryshanedeguia.driverretrieval;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderSummary extends AppCompatActivity {


    public TextView OrdId;
    public TextView CustName;
    public TextView CustAddress;
    public TextView CustNumber;

    public TextView Gross;
    public TextView Promo;
    public TextView Discount;
    public TextView Sales;

    public Button btn_OrderDelivered;


    DatabaseReference mRef;
    DatabaseReference mCustomers;
    DatabaseReference mOrderSummary;

    public List<OrderSumModel> orderlist;
    public OrdersAdapters ordAdapter;
    ListView lvo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        OrdId = (TextView) findViewById(R.id.ord_OrderId);
        CustName = (TextView) findViewById(R.id.ord_Custname);
        CustAddress = (TextView) findViewById(R.id.ord_CustAddress);
        CustNumber = (TextView) findViewById(R.id.ord_CustNum);
        Gross = (TextView) findViewById(R.id.ord_gross);
        Promo = (TextView) findViewById(R.id.ord_promo);
        Discount = (TextView) findViewById(R.id.ord_discount);
        Sales = (TextView) findViewById(R.id.ord_sales);
        mRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("All Orders");
        mCustomers = FirebaseDatabase.getInstance().getReference().child("Customers");

        Intent intent = getIntent();
        final String orderId = intent.getStringExtra("orderID");
        final String pendingId = intent.getStringExtra("pendingID");
        final String user = intent.getStringExtra("driverID");
        OrdId.setText(orderId);



        mRef.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("custUsername").getValue().toString();
                String number = dataSnapshot.child("custContact").getValue().toString();
                String address = dataSnapshot.child("custAddress").getValue().toString();
                String gross = dataSnapshot.child("orderGross").getValue().toString();
                String sales = dataSnapshot.child("orderBill").getValue().toString();
                String VAT  = dataSnapshot.child("orderVAT").getValue().toString();
                String promo = dataSnapshot.child("promo").getValue().toString();

                CustName.setText(name);
                CustAddress.setText(address);
                CustNumber.setText(number);
                Gross.setText(gross);
                Promo.setText(promo);
                Discount.setText(VAT);
                Sales.setText(sales);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ///////////////// LIST VIEW FOR ORDER SUMMARY /////////////////
        mOrderSummary = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(orderId).child("order(s)");
        lvo = (ListView) findViewById(R.id.lv_ord);
        orderlist = new ArrayList<>();

        mOrderSummary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    OrderSumModel osa = snapshot.getValue(OrderSumModel.class);
                    orderlist.add(osa);
                }

                ordAdapter = new OrdersAdapters(OrderSummary.this, R.layout.orders_summary_layout, orderlist);
                lvo.setAdapter(ordAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /////////////// WHEN ITEM IS ITEM DELIVERED ///////////////////

        btn_OrderDelivered = (Button) findViewById(R.id.btn_OrderDelivered);



        btn_OrderDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummary.this);

                builder.setTitle("The Order Has Been Delivered");
                builder.setMessage("Please wait while we wait for the Customer to confirm the order.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent HomeIntent = new Intent(OrderSummary.this, Signature.class);
                        HomeIntent.putExtra("orderID", orderId);
                        HomeIntent.putExtra("pendingID", pendingId);
                        HomeIntent.putExtra("driverID", user);

                        startActivity(HomeIntent);
                        finish();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

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
