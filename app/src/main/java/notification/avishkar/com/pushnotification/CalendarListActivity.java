package notification.avishkar.com.pushnotification;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarListActivity extends AppCompatActivity {

    public List<AdvisorEvent> advisorEvents = new ArrayList<AdvisorEvent>();
    AdvisorEvent advisorEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);

        List<AdvisorEvent> listOfAllEvents = readCalendarEvent(getApplicationContext());
        List<AdvisorEvent> listOfAdvisorEvents = new ArrayList<AdvisorEvent>();
        for (AdvisorEvent event : listOfAllEvents) {
            if (event.getName().contains("Advisor")) {
                listOfAdvisorEvents.add(event);
            }
        }
        Toast.makeText(this, listOfAdvisorEvents.toString(), Toast.LENGTH_LONG).show();

    }

    public List<AdvisorEvent> readCalendarEvent(Context context) {

        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
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
        Toast.makeText(getApplicationContext(), advisorEvents.toString(), Toast.LENGTH_LONG).show();
        return advisorEvents;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
