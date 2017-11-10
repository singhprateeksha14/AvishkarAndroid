package notification.avishkar.com.pushnotification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class activity_home extends AppCompatActivity {
    private static final String REG_TOKEN = "REG_TOKEN";
    TextView notifications, calendar,businessTools, favourites;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        notifications = (TextView) findViewById(R.id.notifications);
        calendar = (TextView) findViewById(R.id.calendarText);
        businessTools = (TextView) findViewById(R.id.businessToolsText);
        favourites = (TextView) findViewById(R.id.favText);
        mydb = new DatabaseHelper(this);
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(activity_home.this, "recent_token: " + recent_token, Toast.LENGTH_LONG).show();
        Log.d(REG_TOKEN, recent_token);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_home.this, NotificationListActivity.class);
                startActivity(i);
                ActivityCompat.finishAffinity(activity_home.this);
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_home.this, CalendarListActivity.class);
                startActivity(i);
                ActivityCompat.finishAffinity(activity_home.this);

            }
        });
        businessTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"This page is not ready",Toast.LENGTH_SHORT).show();
            }
        });
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"This page is not ready",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
