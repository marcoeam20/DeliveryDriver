package com.example.hendryshanedeguia.driverretrieval;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by HendryShanedeGuia on 11/09/2017.
 */

public class  OrderListAdapter extends  ArrayAdapter<OrderInformation> {
    public Activity context;
    public int resource;
    public List<OrderInformation> listOrders;

    public OrderListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<OrderInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listOrders = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource,parent,false);

        TextView tvOrderID = (TextView) v.findViewById(R.id.tvOrderID);
        TextView tvCustomerUsername = (TextView) v.findViewById(R.id.tvCustomerUsername);
        TextView tvCustomerAddress = (TextView) v.findViewById(R.id.tvCustomerAddress);

        tvOrderID.setText(listOrders.get(position).getOrderID());
        tvCustomerUsername.setText(listOrders.get(position).getCustUsername());
        tvCustomerAddress.setText(listOrders.get(position).getCustAddress());

        return v;

    }
}
