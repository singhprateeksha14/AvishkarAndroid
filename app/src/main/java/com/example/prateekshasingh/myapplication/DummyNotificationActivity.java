package com.example.prateekshasingh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DummyNotificationActivity extends AppCompatActivity {

    Button calendarInviteButton;
    EditText nameEdit, emailEdit, phoneEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_notification);

        calendarInviteButton = (Button) findViewById(R.id.createEventButton);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        emailEdit = (EditText) findViewById(R.id.emailEditText);
        phoneEdit = (EditText) findViewById(R.id.phoneEditText);

        calendarInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("client_name", nameEdit.getText().toString());
                intent.putExtra("client_email", emailEdit.getText().toString());
                intent.putExtra("client_phone", phoneEdit.getText().toString());
                startActivity(intent);
            }
        });
    }

}
