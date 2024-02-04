package com.example.retrofit_api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {
    ArrayList<Student> mData;
    Context mContext;
    LayoutInflater inflater;

    public StudentAdapter(ArrayList<Student> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Student> mData){
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) v = inflater.inflate(R.layout.lv_item, null);
        TextView name, address;
        name = v.findViewById(R.id.tvItemName);
        address = v.findViewById(R.id.tvItemAddress);

        name.setText(mData.get(position).getName().toString());
        address.setText(mData.get(position).getAddress().toString());
        return v;
    }
}
