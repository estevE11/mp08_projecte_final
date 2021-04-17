package com.example.mp08_projecte_final.managers;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.db.DBDatasource;

import petrov.kristiyan.colorpicker.ColorPicker;

public class TypeManager extends AppCompatActivity {

    private DBDatasource db;

    private boolean editMode = false;
    private int typeId = 0;

    private int current_selected_color = -11419154;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);

        Bundle b = this.getIntent().getExtras();
        this.editMode = b.getBoolean("editMode");

        if(this.editMode) {
            this.typeId = b.getInt("id");
            this.loadData();
        } else {
            this.current_selected_color = -11419154;
            ((View)findViewById(R.id.view_type_color)).setBackgroundColor(this.current_selected_color);
        }

        findViewById(R.id.button_type_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        findViewById(R.id.view_type_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(TypeManager.this);
                colorPicker.setDefaultColorButton(current_selected_color != 0 ? current_selected_color : -11419154);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        findViewById(R.id.view_type_color).setBackgroundColor(color);
                        current_selected_color = color;
                    }

                    @Override
                    public void onCancel(){
                    }
                });
            }
        });
    }

    public void loadData() {
        Log.d("asdf", ""+this.typeId);
        Cursor type_data = this.db.getType(this.typeId);
        type_data.moveToFirst();
        String name = type_data.getString(1);
        int color = type_data.getInt(2);

        ((TextView)findViewById(R.id.input_type_name)).setText(name);
        ((View)findViewById(R.id.view_type_color)).setBackgroundColor(color);
        this.current_selected_color = color;
    }

    public void submit() {
        String name = ((TextView)findViewById(R.id.input_type_name)).getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("color", this.current_selected_color);
        if(!this.editMode) this.db.insertType(values);
        else this.db.updateType(this.typeId, values);
        this.openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle b = new Bundle();

        b.putInt("tab", 1);

        intent.putExtras(b);
        startActivity(intent);
    }
}