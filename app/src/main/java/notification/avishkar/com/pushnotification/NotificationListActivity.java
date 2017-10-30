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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationListActivity extends AppCompatActivity {
    ListView list;
    DatabaseHelper mydb;
    String[] titles_str = {"Jack Sparrow", "Davy Jones", "Will Turner", "Elizabeth Swann"};
    String[] subtitles_str = {"Premium for Insurance Plan DW231K due on October 21", "Mutual Funds SIP due on October 11th", "Mutual Funds scheme matures on October 4th", "Premium for Insurance Plan LOKH765W due on 12th September"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(notification.avishkar.com.pushnotification.R.layout.activity_notification_list);
        mydb = new DatabaseHelper(this);
        list = (ListView) findViewById(notification.avishkar.com.pushnotification.R.id.list);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < titles_str.length; i++) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", titles_str[i]);
            datum.put("subtitle", subtitles_str[i]);
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "subtitle"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(NotificationListActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });
        viewAll();
    }
    public void viewAll()
    {
        Cursor res = mydb.getAllData();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext())
        {
            buffer.append("ID: "+ res.getInt(0)+"\n");
            buffer.append("title: "+ res.getString(1)+"\n");
            buffer.append("message: "+ res.getString(2)+"\n");
            buffer.append("insured: "+ res.getString(3)+"\n");
            buffer.append("email: "+ res.getString(4)+"\n");
            buffer.append("phone: "+ res.getString(5)+"\n");
            buffer.append("policy_num: "+ res.getString(6)+"\n");
            buffer.append("amount: "+ res.getString(7)+"\n");
            buffer.append("currency: "+ res.getString(8)+"\n");
            buffer.append("due_date: "+ res.getString(9)+"\n");
            buffer.append("notes: "+ res.getString(10)+"\n\n");

        }

        showMessage("Data", buffer.toString());
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
