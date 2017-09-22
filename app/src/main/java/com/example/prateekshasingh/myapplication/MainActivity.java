package com.example.prateekshasingh.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button setAppointmentButton;
    EditText location, notes, fromDate, fromTime, toDate, toTime;
    DatePickerDialog datePickerDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    int mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = getIntent().getStringExtra("client_name");
        String email = getIntent().getStringExtra("client_email");
        String phone = getIntent().getStringExtra("client_phone");

        setAppointmentButton = (Button) findViewById(R.id.startButton);
        myCalendar = Calendar.getInstance();
        // Launch Time Picker Dialog
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        Toast.makeText(getApplicationContext(),hourOfDay + ":" + minute,Toast.LENGTH_LONG).show();
                    }
                }, mHour, mMinute, false);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Toast.makeText(getApplicationContext(),year+" "+monthOfYear+" "+dayOfMonth,Toast.LENGTH_LONG).show();
                // updateLabel();
                timePickerDialog.show();

            }

        };

        setAppointmentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

    }
}