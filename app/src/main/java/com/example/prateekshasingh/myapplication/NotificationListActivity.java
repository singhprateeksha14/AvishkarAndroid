package com.example.prateekshasingh.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    String[] titles_str = {"Jack Sparrow", "Davy Jones", "Will Turner", "Elizabeth Swann"};
    String[] subtitles_str = {"Premium for Insurance Plan DW231K due on October 21", "Mutual Funds SIP due on October 11th", "Mutual Funds scheme matures on October 4th", "Premium for Insurance Plan LOKH765W due on 12th September"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        list = (ListView) findViewById(R.id.list);

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
                Intent i = new Intent(NotificationListActivity.this, DummyNotificationActivity.class);
                startActivity(i);
            }
        });

    }

}
