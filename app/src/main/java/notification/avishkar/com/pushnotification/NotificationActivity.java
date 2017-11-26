package notification.avishkar.com.pushnotification;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimeZone;

public class NotificationActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT = 1;
    final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_ATTENDEES = 2;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 3;
    ImageButton callButton, calendarInviteButton, emailButton;
    Button Dist_Dura_Call_BTN;
    EditText  locationEdit, nameEdit, emailEdit, phoneEdit;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    int mHour, mMinute, mMonth, mDate, mYear;
    Calendar start, end;
    Uri uriCalendarInsertAttendees, uriCalendarInsertEvent;
    ContentValues valuesCalendarInsertAttendees, valuesCalendarInsertEvent;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_notification);

        calendarInviteButton = (ImageButton) findViewById(R.id.createEventButton);
        Dist_Dura_Call_BTN = (Button) findViewById(R.id.Dist_Dura_Call_BTN);
        callButton = (ImageButton) findViewById(R.id.callButton);
        emailButton = (ImageButton) findViewById(R.id.emailButton);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        emailEdit = (EditText) findViewById(R.id.emailEditText);
        phoneEdit = (EditText) findViewById(R.id.phoneEditText);
        locationEdit = (EditText) findViewById(R.id.locationEditText);
        myCalendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        nameEdit.setText(getIntent().getStringExtra("name"));
        emailEdit.setText(getIntent().getStringExtra("email"));
        phoneEdit.setText(getIntent().getStringExtra("phone"));

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Dist_Dura_Call_BTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Dist_Dura_Call_BTN();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        calendarInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(NotificationActivity.this, date, myCalendar
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
                        valuesCalendarInsertEvent = new ContentValues();
                        valuesCalendarInsertEvent.put(CalendarContract.Events.TITLE, "Advisor appointment with " + nameEdit.getText().toString());
                        valuesCalendarInsertEvent.put(CalendarContract.Events.EVENT_COLOR, "#314249");
                        valuesCalendarInsertEvent.put(CalendarContract.Events.EVENT_LOCATION, locationEdit.getText().toString());
                        valuesCalendarInsertEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "India");
                        valuesCalendarInsertEvent.put(CalendarContract.Events.DTSTART, startMillis);
                        valuesCalendarInsertEvent.put(CalendarContract.Events.DTEND, endMillis);
                        valuesCalendarInsertEvent.put(CalendarContract.Events.ALL_DAY, 0);
                        valuesCalendarInsertEvent.put(CalendarContract.Events.CALENDAR_ID, 3);
                        // calendarIdExample();
                        if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                            // Toast.makeText(getApplicationContext(), "NOooooo Invite Sent", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(NotificationActivity.this,
                                    new String[]{Manifest.permission.WRITE_CALENDAR},
                                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT);
                        } else {
                            uriCalendarInsertEvent = cr.insert(CalendarContract.Events.CONTENT_URI, valuesCalendarInsertEvent);
                            long eventID = Long.parseLong(uriCalendarInsertEvent.getLastPathSegment());
                            addAttendees(eventID);
                            Toast.makeText(getApplicationContext(), "Invite Sent", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(NotificationActivity.this, CalendarListActivity.class);
                            startActivity(i);
                            ActivityCompat.finishAffinity(NotificationActivity.this);
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

        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "on click", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(NotificationActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    // return;
                } else {
                   // Toast.makeText(getApplicationContext(), "have permission", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + phoneEdit.getText().toString()));
                    getApplicationContext().startActivity(intent);
                }
            }
        });
    }

    /*public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };*/

    // The indices for the projection array above.
   /* private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;*/

    // Run query
    // Cursor cur = null;

    /*public void calendarIdExample() {
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
        Toast.makeText(this, "selection: " + selection, Toast.LENGTH_SHORT).show();
        String[] selectionArgs = new String[]{"singh.prateeksha14@gmail.com"
        };

        if (ContextCompat.checkSelfPermission(DummyNotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            // return;
            ActivityCompat.requestPermissions(DummyNotificationActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
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

    }*/

    public void addAttendees(long eventId) {

        ContentResolver cr = getContentResolver();
        valuesCalendarInsertAttendees = new ContentValues();
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_NAME, nameEdit.getText().toString());
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_EMAIL, emailEdit.getText().toString());
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED);
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.EVENT_ID, eventId);
        if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            //return;
            ActivityCompat.requestPermissions(NotificationActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_ATTENDEES);
        } else {
            uriCalendarInsertAttendees = cr.insert(CalendarContract.Attendees.CONTENT_URI, valuesCalendarInsertAttendees);
        }

    }

    @Override
    // @SuppressWarnings({"MissingPermission"})
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        ContentResolver cr=getContentResolver();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_ATTENDEES: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(getApplicationContext(), "yayy", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                        uriCalendarInsertAttendees = cr.insert(CalendarContract.Attendees.CONTENT_URI, valuesCalendarInsertAttendees);
                    }

                } else {
                    // Toast.makeText(getApplicationContext(), "booo", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "yayy", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(getApplicationContext(), "have permission", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + phoneEdit.getText().toString()));
                        getApplicationContext().startActivity(intent);

                    }

                } else {
                    // Toast.makeText(getApplicationContext(), "boooooo", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "yayy", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                        uriCalendarInsertEvent = cr.insert(CalendarContract.Events.CONTENT_URI, valuesCalendarInsertEvent);
                        long eventID = Long.parseLong(uriCalendarInsertEvent.getLastPathSegment());
                        addAttendees(eventID);
                        Toast.makeText(getApplicationContext(), "Invite Sent", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NotificationActivity.this, CalendarListActivity.class);
                        startActivity(i);
                        ActivityCompat.finishAffinity(NotificationActivity.this);
                    }

                } else {
                    // Toast.makeText(getApplicationContext(), "booo", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void viewAll()
    {
        Cursor res = mydb.getAllData();
        StringBuffer buffer = new StringBuffer();
        String title_str = null;
        String subtitle_str =null;
        int i=0;
        while (res.moveToNext())
        {
            buffer.append("ID: "+ res.getInt(0)+"\n");
            buffer.append("title: "+ res.getString(1)+"\n");
            title_str= res.getString(1);
            buffer.append("message: "+ res.getString(2)+"\n");
            subtitle_str= res.getString(2);
            //datum.put("title", title_str);
            //datum.put("subtitle", subtitle_str);
            //data.add(datum);
            buffer.append("insured: "+ res.getString(3)+"\n");
            buffer.append("email: "+ res.getString(4)+"\n");
            buffer.append("phone: "+ res.getString(5)+"\n");
            buffer.append("policy_num: "+ res.getString(6)+"\n");
            buffer.append("amount: "+ res.getString(7)+"\n");
            buffer.append("currency: "+ res.getString(8)+"\n");
            buffer.append("due_date: "+ res.getString(9)+"\n");
            buffer.append("notes: "+ res.getString(10)+"\n\n");
            i = i+1;
        }

        //showMessage("Data", buffer.toString());
    }

    public void Dist_Dura_Call_BTN() throws IOException {
        showMessage("Test", "Test1");

        URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Magarpatta, Hadapsar, Pune&destinations=New Sangvi, Pune&mode=drive");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } finally {
            urlConnection.disconnect();
        }

    }

    private void readStream(InputStream in) {
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NotificationActivity.this, CalendarListActivity.class);
        startActivity(i);
        finish();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
