package com.example.hendryshanedeguia.driverretrieval;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Switch dutySwitch;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Firebase References
        currentRef = FirebaseAuth.getInstance();
        String current_uid = currentRef.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(current_uid);

        dutySwitch = (Switch) findViewById(R.id.duty_switch);

        //Changing the status of the switch
        dutySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (dutySwitch.isChecked()) {

                    mRef.child("status").setValue("Available");


                } else {

                    mRef.child("status").setValue("Unavailable");
                }
            }
        });

        //Set the current time
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.txt_date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();


        ref = FirebaseDatabase.getInstance().getReference("Orders");
        lv =(ListView)findViewById(R.id.lv_home);
        listOrders = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent ordIntent = new Intent(HomeActivity.this, OrderSummary.class);
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
                adapter =  new OrderListAdapter(HomeActivity.this,R.layout.order_layout,listOrders);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        int id = item.getItemId();

        if (id == R.id.nav_neworders) {
            // Handle the New Orders Activity

            Intent mainIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(mainIntent);

        } else if (id == R.id.nav_completedorders) {

            Intent completedIntent = new Intent(HomeActivity.this, CompletedActivity.class);
            startActivity(completedIntent);

        } else if (id == R.id.nav_locations) {

            Intent locationIntent = new Intent(HomeActivity.this, MapsActivity.class);
            startActivity(locationIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
