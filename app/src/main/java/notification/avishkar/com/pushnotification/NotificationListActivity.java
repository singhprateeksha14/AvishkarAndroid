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
    //String[] titles_str = {"Jack Sparrow", "Davy Jones", "Will Turner", "Elizabeth Swann"};
    //String[] subtitles_str = {"Premium for Insurance Plan DW231K due on October 21", "Mutual Funds SIP due on October 11th", "Mutual Funds scheme matures on October 4th", "Premium for Insurance Plan LOKH765W due on 12th September"};
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    Map<String, String> datum = new HashMap<String, String>(2);
    ArrayList<Integer> id = new ArrayList<Integer>(0);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(notification.avishkar.com.pushnotification.R.layout.activity_notification_list);
            mydb = new DatabaseHelper(this);
            list = (ListView) findViewById(notification.avishkar.com.pushnotification.R.id.list);
            viewAll();
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "subtitle"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                String Items = String.valueOf(parent.getItemAtPosition(position));
                bundle.putString("ID","1");
                Intent i = new Intent(NotificationListActivity.this, NotificationActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }
    public void viewAll()
    {
        Cursor res = mydb.getAllData();
        String title_str = null;
        String subtitle_str =null;
        while (res.moveToNext())
        {

            title_str= res.getInt(0)+". "+res.getString(1);
            subtitle_str= res.getString(2);
            datum.put("title", title_str);
            datum.put("subtitle", subtitle_str);
            data.add(datum);
            showMessage("ID:",res.getInt(0)+"");
        }
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
