package com.example.hendryshanedeguia.driverretrieval;

import android.content.Intent;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth currentRef;
    DatabaseReference mRef;

    public DatabaseReference ref;
    public List<OrderInformation> listOrders;
    public OrderListAdapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent uiD = getIntent();
        String idTaken = uiD.getStringExtra("driverID");
        Toast.makeText(getApplicationContext(), idTaken + "", Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FRESHCART DRIVER");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Firebase References
        currentRef = FirebaseAuth.getInstance();
        final String current_uid = currentRef.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(current_uid);





        final Switch dutySwitch = (Switch) findViewById(R.id.duty_switch);

        //Changing the status of the switch
        dutySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (dutySwitch.isChecked()) {


                    mRef.child("status").setValue("Available");
                    ref = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders");
                    lv =(ListView)findViewById(R.id.lv_home);
                    listOrders = new ArrayList<>();

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final TextView tvOrderID = (TextView) view.findViewById(R.id.tvOrderID);
                            final TextView tvName = (TextView) view.findViewById(R.id.tvCustomerUsername);
                            final TextView tvAddress = (TextView) view.findViewById(R.id.tvCustomerAddress);
                            final String orderID = tvOrderID.getText().toString();
                            final String name = tvName.getText().toString();
                            final String address = tvAddress.getText().toString();

                            ref.child(orderID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Intent x = getIntent();
                                    String current_uid = x.getStringExtra("driverID");

                                    String pendingID = dataSnapshot.child("pendingID").getValue().toString();
                                    Intent ordIntent = new Intent(HomeActivity.this, OrderSummary.class);
                                    ordIntent.putExtra("Order Summary", lv.getItemIdAtPosition(i));
                                    ordIntent.putExtra("pendingID", pendingID);
                                    ordIntent.putExtra("driverID", current_uid);
                                    ordIntent.putExtra("orderID",orderID);
                                    ordIntent.putExtra("custUsername",name);
                                    ordIntent.putExtra("custAddress",address);
                                    startActivity(ordIntent);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                OrderInformation oi = snapshot.getValue(OrderInformation.class);
                                if(TextUtils.equals(snapshot.child("orderStatus").getValue().toString(),"Pending")){
                                    listOrders.add(oi);
                                }

                            }
                            adapter =  new OrderListAdapter(HomeActivity.this,R.layout.order_layout,listOrders);
                            lv.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {

                    mRef.child("status").setValue("Unavailable");
                    lv.setAdapter(null);

                }
            }
        });

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.txt_date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);

//                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("My_Date");
//                                databaseReference.child("init_date").setValue(stringdate);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent x = getIntent();
            String user = x.getStringExtra("driverID");
            Intent back = new Intent(getApplicationContext(), HomeActivity.class);
            back.putExtra("driverID", user);
            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.account_settings) {
            Intent accountIntent = new Intent(HomeActivity.this, AccountSettingsActivity.class);
            startActivity(accountIntent);
            return true;

        } else if (id == R.id.account_logout){

            FirebaseAuth.getInstance().signOut();
            Intent LogoutIntent = new Intent(HomeActivity.this, StartActivity.class);
            startActivity(LogoutIntent);
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final String current_uid = currentRef.getCurrentUser().getUid();
        int id = item.getItemId();

        if (id == R.id.nav_completedorders) {
            Intent uiD = getIntent();
            String idTaken  = uiD.getStringExtra("driverID");

            Intent completedIntent = new Intent(HomeActivity.this, CompletedActivity.class);
            completedIntent.putExtra("driverID", idTaken);
            startActivity(completedIntent);

        } else if (id == R.id.nav_locations) {

            Intent locationIntent = new Intent(HomeActivity.this, MapsActivity.class);
            locationIntent.putExtra("driverID", current_uid);
            startActivity(locationIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
