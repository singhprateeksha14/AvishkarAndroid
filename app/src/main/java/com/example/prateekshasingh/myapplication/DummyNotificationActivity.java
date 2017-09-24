package com.example.prateekshasingh.myapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.TimeZone;

public class DummyNotificationActivity extends AppCompatActivity {

    Button calendarInviteButton;
    EditText nameEdit, emailEdit, phoneEdit, notesEdit, locationEdit;
    DatePickerDialog datePickerDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    int mHour, mMinute, mMonth, mDate, mYear;
    Calendar start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_notification);

        calendarInviteButton = (Button) findViewById(R.id.createEventButton);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        emailEdit = (EditText) findViewById(R.id.emailEditText);
        phoneEdit = (EditText) findViewById(R.id.phoneEditText);
        notesEdit = (EditText) findViewById(R.id.notesEditText);
        locationEdit = (EditText) findViewById(R.id.locationEditText);
        myCalendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        calendarInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(DummyNotificationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        start = Calendar.getInstance();
                        end = Calendar.getInstance();
                        start.set(mYear, mMonth, mDate, hourOfDay, minute);
                        end.set(mYear, mMonth, mDate, hourOfDay + 1, minute);
                        long startMillis = start.getTimeInMillis();
                        long endMillis = end.getTimeInMillis();
                        TimeZone timeZone = TimeZone.getDefault();
                        ContentResolver cr = getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(CalendarContract.Events.TITLE, "Advisor appointment with " + nameEdit.getText().toString());
                        values.put(CalendarContract.Events.EVENT_COLOR, "#314249");
                        values.put(CalendarContract.Events.EVENT_LOCATION, locationEdit.getText().toString());
                        values.put(CalendarContract.Events.EVENT_TIMEZONE, "India");
                        values.put(CalendarContract.Events.DTSTART, startMillis);
                        values.put(CalendarContract.Events.DTEND, endMillis);
                        values.put(CalendarContract.Events.ALL_DAY, 0);
                        values.put(CalendarContract.Events.CALENDAR_ID, 3);
                        // calendarIdExample();
                        if (ContextCompat.checkSelfPermission(DummyNotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        } else {
                            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                            long eventID = Long.parseLong(uri.getLastPathSegment());
                            Toast.makeText(getApplicationContext(), "Invite Sent", Toast.LENGTH_SHORT).show();
                            addAttendees(eventID);
                        }
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
                mYear = year;
                mMonth = monthOfYear;
                mDate = dayOfMonth;
                timePickerDialog.show();

            }
        };
    }

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    // Run query
    Cursor cur = null;

    public void calendarIdExample() {
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
        Toast.makeText(this, "selection: " + selection, Toast.LENGTH_SHORT).show();
        String[] selectionArgs = new String[]{"singh.prateeksha14@gmail.com"
        };

        if (ContextCompat.checkSelfPermission(DummyNotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        }
        // Use the cursor to step through the returned records
        while (cur.moveToNext())

        {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            // Do something with the values...

            Toast.makeText(getApplicationContext(), "callId: " + calID + " displayName: " + displayName + "accountName: " + accountName + "ownerName: " + ownerName, Toast.LENGTH_LONG).show();


        }

    }

    public void addAttendees(long eventId) {

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Attendees.ATTENDEE_NAME, nameEdit.getText().toString());
        values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, emailEdit.getText().toString());
        values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
        values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED);
        values.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
        values.put(CalendarContract.Attendees.EVENT_ID, eventId);
        if (ContextCompat.checkSelfPermission(DummyNotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Uri uri = cr.insert(CalendarContract.Attendees.CONTENT_URI, values);
        }
    }


}
