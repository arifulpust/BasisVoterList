package com.ariful.basisvotarlistapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariful.basisvotarlistapp.R;
import com.ariful.basisvotarlistapp.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dream71 on 14/11/2017.
 */

public class MenuAdapter extends BaseAdapter {
    Context context;
   public List<String> itemNames;
    private static LayoutInflater inflater = null;

    public MenuAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.itemNames = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }

    @Override
    public int getCount() {
        return itemNames.size();
    }

    @Override
    public Object getItem(int position) {
        return itemNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.right_drawer_menu_item, null);
        }

        TextView textView = (TextView) vi.findViewById(R.id.txtItemName);
        LinearLayout menu_layout = (LinearLayout) vi.findViewById(R.id.menu_layout);
        textView.setText(itemNames.get(position));

        Log.e("position wd",""+MainActivity.listPosition);
        if (position == MainActivity.listPosition) {

            menu_layout.setBackgroundColor(Color.parseColor("#557db8"));
        }
        else
        {
            menu_layout.setBackgroundColor(Color.parseColor("#4B729E"));
        }
//        else{
//            menu_layout.setBackgroundColor(Color.parseColor("#50547cb8"));
//        }

        return vi;
    }

}