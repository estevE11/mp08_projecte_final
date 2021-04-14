package com.example.mp08_projecte_final.managers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mp08_projecte_final.MainActivity;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.mp08_projecte_final.R;

import java.util.Queue;

import dev.sasikanth.colorsheet.ColorSheet;
import petrov.kristiyan.colorpicker.ColorPicker;

public class TypeManager extends AppCompatActivity {

    private DBDatasource db;

    private int[] colors = new int[] {
            0x000000,
            0xff0000,
            0x00ff00,
            0x0000ff,
            0xff00ff,
            0xffff00,
            0x00ffff,
    };

    private int current_selected_color = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.db = new DBDatasource(this);

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

    public void submit() {
        String name = ((TextView)findViewById(R.id.input_type_name)).getText().toString();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("color", this.current_selected_color);
        this.db.insertType(values);
        this.openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}