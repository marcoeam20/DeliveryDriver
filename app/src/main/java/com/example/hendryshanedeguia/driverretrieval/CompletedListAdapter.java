package com.example.hendryshanedeguia.driverretrieval;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * Created by macbook on 27/09/2017.
 */

public class  CompletedListAdapter extends  ArrayAdapter<OrderInformation> {
    public Activity context;
    public int resource;
    public List<OrderInformation> list;

    public CompletedListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<OrderInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource,parent,false);

        TextView tvOrderID = (TextView) v.findViewById(R.id.tvOrderID);
        TextView tvCustomerUsername = (TextView) v.findViewById(R.id.tvCustomerUsername);
        TextView tvCustomerAddress = (TextView) v.findViewById(R.id.tvCustomerAddress);

        tvOrderID.setText(list.get(position).getOrderID());
        tvCustomerUsername.setText(list.get(position).getCustUsername());
        tvCustomerAddress.setText(list.get(position).getCustAddress());

        return v;

    }
}