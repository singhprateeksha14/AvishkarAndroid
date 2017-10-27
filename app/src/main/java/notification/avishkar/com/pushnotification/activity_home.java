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
    String[] titles_str = {"My Notifications", "My Calendar", "Key Business Tools", "My Favorites","Principal Blogs","Principal News"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getIntent().getExtras()!=null){
            for(String key : getIntent().getExtras().keySet())
            {
                if(key.equals("title"))
                {
                    Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                }
                else if (key.equals("message"))
                {
                    Toast.makeText(activity_home.this,getIntent().getExtras().getString(key),Toast.LENGTH_LONG).show();
                }
            }
        }

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(activity_home.this,"recent_token: "+recent_token,Toast.LENGTH_LONG).show();
        Log.d(REG_TOKEN,recent_token);

        ListAdapter listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, titles_str);
        home_Listview = (ListView) findViewById(R.id.home_Listview);
        home_Listview.setAdapter(listAdapter);
        home_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(activity_home.this, NotificationListActivity.class);
                String Items = String.valueOf(parent.getItemAtPosition(position));
                if (Items == "My Notifications")
                {
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(activity_home.this,Items+" Activity Not Defined...",Toast.LENGTH_LONG).show();
                }

            }
        });



    }
}
