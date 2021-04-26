package com.example.mp08_projecte_final.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.example.mp08_projecte_final.managers.MachineManager;
import com.example.mp08_projecte_final.managers.TypeManager;

public class FragmentMachines extends Fragment {

    private DBDatasource db;
    private MainActivity mainActivity;

    MachinesItemListAdapter listAdapter;

    String[] orderItems = {"Client","City","Address","Last check"};

    TextView txt_search;
    TextView txt_filter;
    private String searchFilter = "";
    private Integer orderFilter = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_machines, container, false);

        this.db = new DBDatasource(getContext());

        Cursor c = this.db.getMachines();
        this.listAdapter = new MachinesItemListAdapter(getContext(), c, this, this.mainActivity);
        ListView lst = (ListView) view.findViewById(R.id.listView);
        lst.setAdapter(this.listAdapter);

        this.txt_search = (TextView)view.findViewById(R.id.text_search);
        this.txt_filter = (TextView)view.findViewById(R.id.text_filter);

        view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSearch();
            }
        });

        view.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogOrder();
            }
        });

        this.load();

        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mainActivity = (MainActivity) activity;
    }

    public void load() {
        if(this.searchFilter.isEmpty()) {
            this.txt_search.setText("No filter");
        } else {
            this.txt_search.setText("Searching \"" + this.searchFilter + "\" serial number");
        }

        if(this.orderFilter == null) {
            this.txt_filter.setText("No filter");
        } else {
            this.txt_filter.setText("Ordering by " + this.orderItems[this.orderFilter]);
        }
        Cursor c = this.db.getFilteredMachines(this.searchFilter, this.orderFilter, "asc");

        this.listAdapter.changeCursor(c);
        this.listAdapter.notifyDataSetChanged();
    }

    private void openDialogSearch() {
        AlertDialog ad;

        ad = new AlertDialog.Builder(getContext()).create();
        ad.setTitle("");
        ad.setMessage("Search serial number");

        // Ahora forzamos que aparezca el editText
        final EditText edtValor = new EditText(getContext());
        edtValor.setText(this.searchFilter);
        ad.setView(edtValor);

        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Insert", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int whichButton) {
                String val = "";
                try {
                    val = edtValor.getText().toString();
                    searchFilter = val;
                    load();
                } catch (Exception e) {
                    edtValor.setError( "Input needs to be a number!");
                }

            }
        });

        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // no fem res.
            }
        });
        ad.show();
    }

    private void openDialogOrder() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("AlertDialog");
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(this.orderItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderFilter = which;
            }
        });
        alertDialog.setPositiveButton("Set filter", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int whichButton) {
                String val = "";
                try {
                    load();
                } catch (Exception e) {
                }

            }
        });

        alertDialog.setNegativeButton("Remove filter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                orderFilter = null;
                load();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}

class MachinesItemListAdapter extends SimpleCursorAdapter {
    private Context context;
    private FragmentMachines frag;
    private DBDatasource db;
    private MainActivity mainActivity;

    public MachinesItemListAdapter(Context context, Cursor c, FragmentMachines frag, MainActivity mainActivity) {
        super(context, R.layout.item_machine, c,
                new String[]{"name", "serial_number", "telf", "email"}, // from
                new int[]{R.id.txt_type_name, R.id.txt_description},
                1); // to
        this.context = context;
        this.db = new DBDatasource(this.context);
        this.frag = frag;
        this.mainActivity = mainActivity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = super.getView(position, convertView, parent);

        Cursor c = (Cursor) getItem(position);
        int id_idx = c.getColumnIndex("_id");
        int id = c.getInt(id_idx);

        int type_id_idx = c.getColumnIndex("id_type");
        int type_id = c.getInt(type_id_idx);

        Cursor type = this.db.getType(type_id);
        type.moveToFirst();
        int color = type.getInt(type.getColumnIndex("color"));
        item.findViewById(R.id.rect_type_color).setBackgroundColor(color);

        String serial_number = c.getString(c.getColumnIndex("serial_number"));
        String telf = c.getString(c.getColumnIndex("telf"));
        String email = c.getString(c.getColumnIndex("email"));

        item.findViewById(R.id.btn_machine_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditMachineActivity(id);
            }
        });

        item.findViewById(R.id.btn_machine_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteAlert(id);
            }
        });

        item.findViewById(R.id.btn_machine_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCall(Integer.parseInt(telf));
            }
        });

        item.findViewById(R.id.btn_machine_mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(email, serial_number);
            }
        });

        item.findViewById(R.id.btn_machine_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.openMapWithFocus(id);
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

        context.startActivity(intent);
    }

    private void phoneCall(int num) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + num));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
            else
            {
                context.startActivity(phoneIntent);
            }
        }
        else
        {
            context.startActivity(phoneIntent);
        }
    }

    private void sendMail(String mail, String serial) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Provera revisió màquina nº" + serial);
        intent.putExtra(Intent.EXTRA_TEXT, "Enviat desde l'app de Buidem");
        intent.setType("message/rfc822");
        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void openDeleteAlert(int id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        db.deleteMachine(id);
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
