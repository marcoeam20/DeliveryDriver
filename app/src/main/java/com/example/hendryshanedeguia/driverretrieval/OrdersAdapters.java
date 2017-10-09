package com.example.hendryshanedeguia.driverretrieval;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by macbook on 01/10/2017.
 */

public class OrdersAdapters extends ArrayAdapter<OrderSumModel> {
    public Activity context;
    public int resource;
    public List<OrderSumModel> orderlist;

    public OrdersAdapters(@NonNull Activity context, @LayoutRes int resource, @NonNull List<OrderSumModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        orderlist = objects;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, parent, false);

        TextView tvOrdQty = (TextView) v.findViewById(R.id.tvOSQuantity);
        TextView tvOrdDesc = (TextView) v.findViewById(R.id.tvOSName);
        TextView tvOrdPrice = (TextView) v.findViewById(R.id.tvOSPrice);
        TextView tvOrdTotal = (TextView) v.findViewById(R.id.tvOSTotal);

        tvOrdQty.setText(orderlist.get(position).getOrderQuantity());
        tvOrdDesc.setText(orderlist.get(position).getProdName());
        tvOrdPrice.setText(orderlist.get(position).getProdPrice());
        tvOrdTotal.setText(orderlist.get(position).getSubTotal());

        return v;
    }
}
