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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class NotificationListActivity extends AppCompatActivity {
    ListView list;
    DatabaseHelper mydb;
    String result, messagingService, title, message, insured, email, phone, policy_num, amount, notes, currency, due_date;
    boolean isPushNotificationSaved = false;
    List<HashMap<String, String>> data = new ArrayList<>();
    HashMap<String, String> datum = new HashMap<>();
    String ID1;
    int parseInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(notification.avishkar.com.pushnotification.R.layout.activity_notification_list);
        String intent = getIntent().getStringExtra("Intent");
        messagingService = getIntent().getStringExtra("MessagingService");
        if (messagingService == "MessagingService") {
            title = getIntent().getStringExtra("title");
            message = getIntent().getStringExtra("message");
            insured = getIntent().getStringExtra("insured");
            result = getIntent().getStringExtra("Result");
            Toast.makeText(NotificationListActivity.this, "messagingService:" + messagingService, Toast.LENGTH_LONG).show();
        }
        viewAll();
        list = (ListView) findViewById(R.id.list);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.list_item, new String[]{"Title", "subtitle"},
                new int[]{R.id.text1, R.id.text2});
        Iterator it = datum.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("Title", pair.getKey().toString());
            resultMap.put("subtitle", pair.getValue().toString());
            data.add(resultMap);
        }
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String Items = String.valueOf(parent.getItemAtPosition(position));
                System.out.println("Value of Items"+Items);

                String[] split= Items.split(" Title=");
                ID1= split[1];
                char ID2= ID1.charAt(0);
                parseInt= Integer.parseInt(String.valueOf(ID2));
                System.out.println("ID1:"+ID1.charAt(0));

                fetchdetails();
            }
        });
    }
    public void viewAll() {
        mydb = new DatabaseHelper(this);
        Cursor res = mydb.getAllData();
        res.moveToFirst();
        String CNames[] = new String[res.getCount()];
        for (int i = 0; i < CNames.length; i++) {
            datum.put(res.getInt(0) + ". " + res.getString(1), res.getString(2));
            res.moveToNext();
        }
    }

    public void fetchData(){

        mydb = new DatabaseHelper(this);
        Cursor res = mydb.getAllData();
        res.moveToFirst();
        String CNames[] = new String[res.getCount()];
        for (int i = 0; i < CNames.length; i++) {
            datum.put(res.getInt(0) + ". " + res.getString(1), res.getString(2));
            res.moveToNext();
        }

    }

    public void fetchdetails(){

        mydb = new DatabaseHelper(this);
        Cursor res = mydb.getData(parseInt);
        res.moveToFirst();
        String CNames[] = new String[res.getCount()];
        for (int i = 0; i < CNames.length; i++) {
            Intent intent = new Intent(NotificationListActivity.this, NotificationActivity.class);
            intent.putExtra("name", res.getString(3));
            intent.putExtra("email", res.getString(4));
            intent.putExtra("phone", res.getString(5));
            startActivity(intent);
        }

    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NotificationListActivity.this, activity_home.class);
        startActivity(i);
        finish();
    }
}
