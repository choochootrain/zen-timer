package com.choochootrain.zentimer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationListAdapter extends BaseAdapter implements ListAdapter {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SWITCH = 1;

    public static final int TYPE_MAX_COUNT = 2;

    private ArrayList<String> data;
    private LayoutInflater inflater;

    private ArrayList<Integer> viewTypes;

    public NavigationListAdapter(Context context) {
        data = new ArrayList<String>();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewTypes = new ArrayList<Integer>();
    }

    public void addItem(String item, int type) {
        data.add(item);
        viewTypes.add(type);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypes.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_ITEM:
                    convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
                    label = (TextView)convertView.findViewById(android.R.id.text1);
                    break;
                case TYPE_SWITCH:
                    convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
                    label = (TextView)convertView.findViewById(android.R.id.text1);
                    break;
            }
            convertView.setTag(label);
        } else {
            label = (TextView)convertView.getTag();
        }
        label.setText(data.get(position));
        return convertView;
    }
}
