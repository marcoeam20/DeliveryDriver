package com.example.hendryshanedeguia.driverretrieval;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderSummary extends AppCompatActivity {

    public CircleImageView CustPic;
    public TextView OrdId;
    public TextView CustName;
    public TextView CustAddress;
    public TextView CustNumber;
    public TextView Quantity;
    public TextView Description;
    public TextView UnitPrice;
    public TextView TotalAmount;
    public TextView Gross;
    public TextView Promo;
    public TextView Discount;
    public TextView Sales;

    public Button btn_OrderDelivered;


    DatabaseReference mRef;
    DatabaseReference mCustomers;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);


        CustPic = (CircleImageView) findViewById(R.id.ord_CustPic);
        OrdId = (TextView) findViewById(R.id.ord_OrdId);
        CustName = (TextView) findViewById(R.id.ord_CustName);
        CustAddress = (TextView) findViewById(R.id.ord_CustAddress);
        CustNumber = (TextView) findViewById(R.id.ord_CustNum);
        Quantity = (TextView) findViewById(R.id.ord_qty);
        UnitPrice = (TextView) findViewById(R.id.ord_unitprice);
        TotalAmount = (TextView) findViewById(R.id.ord_total);
        Gross = (TextView) findViewById(R.id.ord_gross);
        Promo = (TextView) findViewById(R.id.ord_promo);
        Discount = (TextView) findViewById(R.id.ord_discount);
        Sales = (TextView) findViewById(R.id.ord_sales);

        mRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        mCustomers = FirebaseDatabase.getInstance().getReference().child("Customers");

        Intent intent = getIntent();

        final String orderId = mRef.push().getKey();
        OrdId.setText(orderId);




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
                        Intent HomeIntent = new Intent(OrderSummary.this, HomeActivity.class);
                        startActivity(HomeIntent);
                        finish();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }
}
