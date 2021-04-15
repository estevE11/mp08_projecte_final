package com.example.mp08_projecte_final.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.db.DBDatasource;

public class FragmentZones extends Fragment {
    private DBDatasource db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_zones, container, false);

        this.db = new DBDatasource(getContext());

        Cursor c = this.db.getZones();
        ListAdapter listAdapter = new com.example.mp08_projecte_final.fragments.ZonesItemListAdapter(getContext(), c);
        ListView lst = (ListView) view.findViewById(R.id.listView);
        lst.setAdapter(listAdapter);

        return view;
    }
}

class ZonesItemListAdapter extends SimpleCursorAdapter {
    private Context conxtext;

    public ZonesItemListAdapter(Context context, Cursor c) {
        super(context, R.layout.item_zone, c,
                new String[]{"name"}, // from
                new int[]{R.id.txt_zone_name},
                1); // to
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = super.getView(position, convertView, parent);

        Cursor c = (Cursor) getItem(position);

        String name = c.getString(1);
        Log.d("asdf", name);
        ((TextView)item.findViewById(R.id.txt_zone_name)).setText(name);

        return(item);
    }
}

