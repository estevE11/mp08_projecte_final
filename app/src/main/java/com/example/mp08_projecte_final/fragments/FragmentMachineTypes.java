package com.example.mp08_projecte_final.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class FragmentMachineTypes extends Fragment {
    private DBDatasource db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_machine_types, container, false);

        this.db = new DBDatasource(getContext());

        Cursor c = this.db.getTypes();
        ListAdapter listAdapter = new com.example.mp08_projecte_final.fragments.TypesItemListAdapter(getContext(), c);
        ListView lst = (ListView) view.findViewById(R.id.listView);
        lst.setAdapter(listAdapter);

        return view;
    }
}

class TypesItemListAdapter extends SimpleCursorAdapter {
    private Context context;

    public TypesItemListAdapter(Context context, Cursor c) {
        super(context, R.layout.item_type, c,
                new String[]{"name", "color"}, // from
                new int[]{R.id.txt_type_name},
                1); // to
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = super.getView(position, convertView, parent);

        Cursor c = (Cursor) getItem(position);
        int id_idx = c.getColumnIndex("_id");
        int id = c.getInt(id_idx);

        int name_idx = c.getColumnIndex("name");
        String name = c.getString(name_idx);
        ((TextView)item.findViewById(R.id.txt_type_name)).setText(name);

        int clr = 0;
        String color = c.getString(2);
        if(color == null) {
            color = "-14654801";
        }
        clr = Integer.parseInt(color);
        item.findViewById(R.id.view_type_color).setBackgroundColor(clr);

        item.findViewById(R.id.btn_type_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditTypeActivity(id);
            }
        });

        return(item);
    }

    private void openEditTypeActivity(int id) {
        Intent intent = new Intent(context.getApplicationContext(), TypeManager.class);
        Bundle b = new Bundle();

        b.putBoolean("editMode", true);
        b.putInt("id", id);

        intent.putExtras(b);

        context.startActivities(new Intent[]{intent});
    }
}
