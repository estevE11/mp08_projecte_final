package com.example.mp08_projecte_final.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.example.mp08_projecte_final.managers.TypeManager;

public class FragmentMachineTypes extends Fragment {
    private DBDatasource db;
    private TypesItemListAdapter listAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_machine_types, container, false);

        this.db = new DBDatasource(getContext());

        Cursor c = this.db.getTypes();
        this.listAdapter = new com.example.mp08_projecte_final.fragments.TypesItemListAdapter(getContext(), c, this);
        ListView lst = (ListView) view.findViewById(R.id.listView);
        lst.setAdapter(this.listAdapter);

        return view;
    }

    public void load() {
        Cursor c = this.db.getTypes();
        Bundle b = new Bundle();

        this.listAdapter.changeCursor(c);
        this.listAdapter.notifyDataSetChanged();
    }
}

class TypesItemListAdapter extends SimpleCursorAdapter {
    private Context context;
    private DBDatasource db;
    private FragmentMachineTypes frag;

    public TypesItemListAdapter(Context context, Cursor c, FragmentMachineTypes frag) {
        super(context, R.layout.item_type, c,
                new String[]{"name", "color"}, // from
                new int[]{R.id.txt_type_name},
                1); // to
        this.context = context;
        this.db = new DBDatasource(this.context);
        this.frag = frag;
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

        item.findViewById(R.id.btn_type_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteAlert(id);
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

    private void openDeleteAlert(int id) {
        Log.d("asdf", "Buenas");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        db.deleteType(id);
                        frag.load();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setMessage("The selected item will be selected").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
