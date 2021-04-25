package com.example.mp08_projecte_final.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.example.mp08_projecte_final.R;

public class CalendarUtils {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void openDateInput(Context ctx, EditText input) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                input.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }
}
