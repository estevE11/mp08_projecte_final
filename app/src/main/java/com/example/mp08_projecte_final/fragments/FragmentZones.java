package com.example.mp08_projecte_final.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.mp08_projecte_final.managers.TypeManager;
import com.example.mp08_projecte_final.managers.ZoneManager;

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
    private Context context;

    public ZonesItemListAdapter(Context context, Cursor c) {
        super(context, R.layout.item_zone, c,
                new String[]{"name"}, // from
                new int[]{R.id.txt_zone_name},
                1); // to
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = super.getView(position, convertView, parent);

        Cursor c = (Cursor) getItem(position);
        int id_idx = c.getColumnIndex("_id");
        int id = c.getInt(id_idx);

        String name = c.getString(1);
        Log.d("asdf", name);
        ((TextView)item.findViewById(R.id.txt_zone_name)).setText(name);

        item.findViewById(R.id.btn_zone_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditZoneActivity(id);
            }
        });

        return(item);
    }

    private void openEditZoneActivity(int id) {
        Intent intent = new Intent(context.getApplicationContext(), ZoneManager.class);
        Bundle b = new Bundle();

        b.putBoolean("editMode", true);
        b.putInt("id", id);

        intent.putExtras(b);

        context.startActivities(new Intent[]{intent});
    }
}

