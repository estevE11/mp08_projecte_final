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
import com.example.mp08_projecte_final.managers.MachineManager;
import com.example.mp08_projecte_final.managers.TypeManager;

public class FragmentMachines extends Fragment {

    private DBDatasource db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_machines, container, false);

        this.db = new DBDatasource(getContext());

        Cursor c = this.db.getMachines();
        ListAdapter listAdapter = new MachinesItemListAdapter(getContext(), c);
        ListView lst = (ListView) view.findViewById(R.id.listView);
        lst.setAdapter(listAdapter);

        return view;
    }
}

class MachinesItemListAdapter extends SimpleCursorAdapter {
    private Context context;

    public MachinesItemListAdapter(Context context, Cursor c) {
        super(context, R.layout.item_machine, c,
                new String[]{"name", "serial_number"}, // from
                new int[]{R.id.txt_type_name, R.id.txt_description},
                1); // to
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = super.getView(position, convertView, parent);

        Cursor c = (Cursor) getItem(position);
        int id_idx = c.getColumnIndex("_id");
        int id = c.getInt(id_idx);

        String name = c.getString(0);
        TextView txt_name = (TextView)item.findViewById(R.id.txt_type_name);

        String serial_number = c.getString(1);
        TextView txt_serial_number = (TextView)item.findViewById(R.id.txt_description);

        item.findViewById(R.id.btn_machine_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditMachineActivity(id);
            }
        });

        return(item);
    }

    private void openEditMachineActivity(int id) {
        Intent intent = new Intent(context.getApplicationContext(), MachineManager.class);
        Bundle b = new Bundle();

        b.putBoolean("editMode", true);
        b.putInt("id", id);

        intent.putExtras(b);

        context.startActivities(new Intent[]{intent});
    }
}
