package notification.avishkar.com.pushnotification;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationListActivity extends AppCompatActivity {
    ListView list;
    DatabaseHelper mydb;
    String result, messagingService, title, message, insured, email, phone, policy_num, amount, notes, currency, due_date;
    boolean isPushNotificationSaved = false;
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    Map<String, String> datum = new HashMap<String, String>();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(notification.avishkar.com.pushnotification.R.layout.activity_notification_list);
            String intent = getIntent().getStringExtra("Intent");
            messagingService = getIntent().getStringExtra("MessagingService");
            title = getIntent().getStringExtra("title");
            message = getIntent().getStringExtra("message");
            insured = getIntent().getStringExtra("insured");
            result = getIntent().getStringExtra("Result");
            /*email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone_num");
            policy_num = getIntent().getStringExtra("policy_num");
            amount = getIntent().getStringExtra("amount");
            currency = getIntent().getStringExtra("currency");
            due_date = getIntent().getStringExtra("due_date");
            notes = getIntent().getStringExtra("notes");*/

            Toast.makeText(NotificationListActivity.this, "messagingService:"+messagingService, Toast.LENGTH_LONG).show();
            /*Toast.makeText(NotificationListActivity.this, "title:"+title, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "message:"+message, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "insured:"+insured, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "Result:"+result, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "email:"+email, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "phone_num:"+phone, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "policy_num:"+policy_num, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "amount:"+amount, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "currency:"+currency, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "due_date:"+due_date, Toast.LENGTH_LONG).show();
            Toast.makeText(NotificationListActivity.this, "notes:"+notes, Toast.LENGTH_LONG).show();*/


            //addData();

                /*if (getIntent().getExtras() != null) {
                    Toast.makeText(NotificationListActivity.this, "Notification have data.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NotificationListActivity.this, "Notification does not have data.", Toast.LENGTH_LONG).show();
                }*/

                /*if (messagingService != "MessagingService") {
                if(intent ==null) {
                    Intent activity_home = new Intent(NotificationListActivity.this, activity_home.class);
                    startActivity(activity_home);
                    finish();
                }
                }*/

                    viewAll();
                    list = (ListView) findViewById(notification.avishkar.com.pushnotification.R.id.list);
                    SimpleAdapter adapter = new SimpleAdapter(this, data,
                            android.R.layout.simple_list_item_2,
                            new String[]{"title", "subtitle"},
                            new int[]{android.R.id.text1,
                                    android.R.id.text2});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String Items = String.valueOf(parent.getItemAtPosition(position));
                            Intent i = new Intent(NotificationListActivity.this, NotificationActivity.class);
                            startActivity(i);
                        }
                    });


    }

    public boolean addData() {
        mydb = new DatabaseHelper(this);
        /*if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("title")) {
                    title = getIntent().getExtras().getString(key);
                }
                if (key.equals("message")) {
                    message = getIntent().getExtras().getString(key);
                }
                if (key.equals("insured")) {
                    insured = getIntent().getExtras().getString(key);
                }
                if (key.equals("email")) {
                    email = getIntent().getExtras().getString(key);
                }
                if (key.equals("phone_num")) {
                    phone = getIntent().getExtras().getString(key);
                }
                if (key.equals("policy_num")) {
                    policy_num = getIntent().getExtras().getString(key);
                }
                if (key.equals("amount")) {
                    amount = getIntent().getExtras().getString(key);
                }
                if (key.equals("currency")) {
                    currency = getIntent().getExtras().getString(key);
                }
                if (key.equals("due_date")) {
                    due_date = getIntent().getExtras().getString(key);
                }
                if (key.equals("notes")) {
                    notes = getIntent().getExtras().getString(key);
                }
            }
        }*/
        if (title != null && message != null && insured != null)
        {
            isPushNotificationSaved = mydb.insertData(title, message, insured, email, phone, policy_num, amount, currency, due_date, notes);
            if (isPushNotificationSaved = true) {
                Toast.makeText(NotificationListActivity.this, "Notification Saved Successfully!", Toast.LENGTH_LONG).show();
            }
            if (isPushNotificationSaved != true) {
                Toast.makeText(NotificationListActivity.this, "Notification not Saved Successfully.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(NotificationListActivity.this, "Title, Message and Insured details are missing.", Toast.LENGTH_LONG).show();
        }
        return isPushNotificationSaved;
    }
    public void viewAll()
    {
        mydb = new DatabaseHelper(this);
        Cursor res = mydb.getAllData();
        while (res.moveToNext())
        {
            datum.put("title", res.getInt(0)+". "+res.getString(1));
            datum.put("subtitle", res.getString(2));
            data.add(datum);
            Toast.makeText(NotificationListActivity.this, "title: "+res.getInt(0)+". "+res.getString(1)+ " subtitle: "+res.getString(2), Toast.LENGTH_LONG).show();
        }

        //showMessage("concat_title_str:",concat_title_str);

    }
    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
