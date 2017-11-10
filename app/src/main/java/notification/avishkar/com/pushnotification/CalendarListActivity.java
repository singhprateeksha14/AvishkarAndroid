package notification.avishkar.com.pushnotification;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarListActivity extends Activity {

    final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT = 1;
    final int MY_PERMISSIONS_REQUEST_READ_CALENDAR_ATTENDEES = 2;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 3;
    public List<AdvisorEvent> advisorEvents = new ArrayList<AdvisorEvent>();
    AdvisorEvent advisorEvent;
    ListView calendarEventList;
    CalendarListAdapter adapter = null;
    CalendarListActivity activity = null;

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);
        calendarEventList = (ListView) findViewById(R.id.calendarList);
        List<AdvisorEvent> listOfAllEvents = readCalendarEvent(getApplicationContext());
        ArrayList<AdvisorEvent> listOfAdvisorEvents = new ArrayList<AdvisorEvent>();
        for (AdvisorEvent event : listOfAllEvents) {
            if (event.getName().contains("Advisor")) {
                listOfAdvisorEvents.add(event);
            }
        }
      //  Toast.makeText(this, listOfAdvisorEvents.toString(), Toast.LENGTH_LONG).show();
        activity = this;
        Resources res =getResources();
        adapter = new CalendarListAdapter(activity, listOfAdvisorEvents, res);
        calendarEventList.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalendarListActivity.this, NotificationActivity.class);
                startActivity(i);
                ActivityCompat.finishAffinity(CalendarListActivity.this);
            }
        });

    }

    public List<AdvisorEvent> readCalendarEvent(Context context) {

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 6);
        Date endDate = calendar.getTime();
        long beginMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        if (ContextCompat.checkSelfPermission(CalendarListActivity.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(getApplicationContext(), "NOooooo Invite Sent", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(CalendarListActivity.this, new String[]{android.Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_READ_CALENDAR_ATTENDEES);
        }
        Cursor cursor = CalendarContract.Instances.query(context.getContentResolver(), new String[]{"calendar_id", "title", "description",
                "dtstart", "dtend", "eventLocation"}, beginMillis, endMillis);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        advisorEvents.clear();
        for (int i = 0; i < CNames.length; i++) {
            advisorEvent = new AdvisorEvent();
            advisorEvent.setName(cursor.getString(1));
            advisorEvent.setDate(getDate(Long.parseLong(cursor.getString(3))));

            CNames[i] = cursor.getString(1);
            advisorEvents.add(advisorEvent);
            cursor.moveToNext();

        }
        // Toast.makeText(getApplicationContext(), advisorEvents.toString(), Toast.LENGTH_LONG).show();
        return advisorEvents;
    }

    public void onItemClick(int position) {
        Toast.makeText(this, "Item number :" + position + "clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CalendarListActivity.this, activity_home.class);
        startActivity(i);
        finish();
    }

}
