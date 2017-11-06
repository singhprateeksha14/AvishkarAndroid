package notification.avishkar.com.pushnotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.iid.FirebaseInstanceId;

public class activity_home extends AppCompatActivity {
    private static final String REG_TOKEN = "REG_TOKEN";
    ListView home_Listview;
    DatabaseHelper mydb;
    String title, message, insured, email, phone, policy_num, amount, notes, currency, due_date;
    String[] titles_str = {"My Notifications        	>", "My Calendar        	 	>", "Key Business Tools      	>", "My Favorites            	>", "Principal Blogs 	 	>", "Principal News      		>"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mydb = new DatabaseHelper(this);
        addData();
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(activity_home.this, "recent_token: " + recent_token, Toast.LENGTH_LONG).show();
        Log.d(REG_TOKEN, recent_token);

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles_str);
        home_Listview = (ListView) findViewById(R.id.home_Listview);
        home_Listview.setAdapter(listAdapter);

        home_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Items = String.valueOf(parent.getItemAtPosition(position));
                if (Items.contains("My Notifications")) {
                    Intent i = new Intent(activity_home.this, NotificationListActivity.class);
                    startActivity(i);
                } else if (Items.contains("My Calendar")) {
                    Intent i = new Intent(activity_home.this, CalendarListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(activity_home.this, Items + " Activity Not Defined...", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public boolean addData() {
        mydb = new DatabaseHelper(this);
        boolean isPushNotificationSaved = false;
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("title")) {
                  //  Toast.makeText(activity_home.this, getIntent().getExtras().getString(key), Toast.LENGTH_LONG).show();
                    title = getIntent().getExtras().getString(key);
                }
                if (key.equals("message")) {
                  //  Toast.makeText(activity_home.this, getIntent().getExtras().getString(key), Toast.LENGTH_LONG).show();
                    message = getIntent().getExtras().getString(key);
                }
                if (key.equals("insured")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    insured = getIntent().getExtras().getString(key);
                }
                if (key.equals("email")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    email = getIntent().getExtras().getString(key);
                }
                if (key.equals("phone")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    phone = getIntent().getExtras().getString(key);
                }
                if (key.equals("policy_num")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    policy_num = getIntent().getExtras().getString(key);
                }
                if (key.equals("amount")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    amount = getIntent().getExtras().getString(key);
                }
                if (key.equals("currency")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    currency = getIntent().getExtras().getString(key);
                }
                if (key.equals("due_date")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    due_date = getIntent().getExtras().getString(key);
                }
                if (key.equals("notes")) {
                    //Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                    notes = getIntent().getExtras().getString(key);
                }
            }
        }
        if (title != null && message != null && insured != null) {
            //String tile, String message, String Insured, String email, String phone,String policy_num, Integer amount, String currency, String due_Date, String notes
            isPushNotificationSaved = mydb.insertData(title, message, insured, email, phone, policy_num, amount, currency, due_date, notes);
            if (isPushNotificationSaved = true) {
               // Toast.makeText(activity_home.this, "Push Notification Saved! Details : Title: " + title + " Message: " + message + " Insured: " + insured, Toast.LENGTH_LONG).show();
            }
            if (isPushNotificationSaved != true) {
               // Toast.makeText(activity_home.this, "Push Notification Not Saved! Details : Title: " + title + " Message: " + message + " Insured: " + insured, Toast.LENGTH_LONG).show();
            }
        } else {
           // Toast.makeText(activity_home.this, "Push Notification Details : Title: " + title + " Message: " + message + " Insured: " + insured, Toast.LENGTH_LONG).show();
        }
        return isPushNotificationSaved;
    }
}
