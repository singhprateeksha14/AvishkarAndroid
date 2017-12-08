package notification.avishkar.com.pushnotification;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendEmailActivity extends AppCompatActivity {

    EditText subject, message;
    String email;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        subject =(EditText)findViewById(R.id.subjectEditText);
        message = (EditText)findViewById(R.id.mesageEditText);
        sendEmail = (Button)findViewById(R.id.sendEmailButton);

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());

                intent.setType("*/*");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    Toast.makeText(getApplicationContext(),"Sending Email...",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                finish();

            }
        });
    }
}
