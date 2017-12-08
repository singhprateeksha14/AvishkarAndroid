package notification.avishkar.com.pushnotification;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.google.android.gms.maps.model.LatLng;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NotificationActivity extends AppCompatActivity implements GeoTask.Geo {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    LocationTrack locationTrack;

    final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT = 1;
    final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_ATTENDEES = 2;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 3;
    final static int ALL_PERMISSIONS_RESULT = 101;
    ImageButton callButton, calendarInviteButton, emailButton;
    Button Dist_Dura_Call_BTN;
    EditText  locationEdit, nameEdit, emailEdit, phoneEdit, fromlocationEdit;
    TextView tv_result1,tv_result2;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    int mHour, mMinute, mMonth, mDate, mYear;
    String sFromLocation, sLocationEdit;
    Calendar start, end;
    Uri uriCalendarInsertAttendees, uriCalendarInsertEvent;
    ContentValues valuesCalendarInsertAttendees, valuesCalendarInsertEvent;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_notification);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        locationTrack = new LocationTrack(NotificationActivity.this);

        tv_result1= (TextView) findViewById(R.id.textView_result1);
        tv_result2=(TextView) findViewById(R.id.textView_result2);

        calendarInviteButton = (ImageButton) findViewById(R.id.createEventButton);
        Dist_Dura_Call_BTN = (Button) findViewById(R.id.Dist_Dura_Call_BTN);
        callButton = (ImageButton) findViewById(R.id.callButton);

        emailButton = (ImageButton) findViewById(R.id.emailButton);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        emailEdit = (EditText) findViewById(R.id.emailEditText);
        phoneEdit = (EditText) findViewById(R.id.phoneEditText);
        locationEdit = (EditText) findViewById(R.id.locationEditText);
        //Getting  locationEdit

        fromlocationEdit = (EditText) findViewById(R.id.toLocationText);
        //Getting  fromlocationEdit

        myCalendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        nameEdit.setText(getIntent().getStringExtra("name"));
        emailEdit.setText(getIntent().getStringExtra("email"));
        phoneEdit.setText(getIntent().getStringExtra("phone"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

                   /* Log.d("latitude",Double.toString(latitude));
                    Log.d("longitude",Double.toString(longitude));
*/
                   /* double longitude = 72.88261;
                    double latitude = 19.07283;
                    */
            String Adrs = getCompleteAddressString(latitude,longitude);
            fromlocationEdit.setText(Adrs, TextView.BufferType.EDITABLE);


            // Toast.makeText(getApplicationContext(), "Address: "+Adrs, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }

        Dist_Dura_Call_BTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                sFromLocation= fromlocationEdit.getText().toString().trim();
                sLocationEdit= locationEdit.getText().toString().trim();
                System.out.println("FromLocation: "+sFromLocation+"Destination: "+sLocationEdit);

               if(sFromLocation.isEmpty() || sLocationEdit.isEmpty()){
                   AlertDialog.Builder alert = new AlertDialog.Builder(NotificationActivity.this);
                   alert.setTitle("Location Required");
                   alert.setMessage("Please enter From and Destination location to proceed further");
                   alert.setPositiveButton("OK",null);
                   alert.show();
               }
                else {
                /*String CurLoc="Magarpatta,Pune,India";
                String str_to=locationEdit.getText().toString();
*/
                   String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + sFromLocation + "&destinations=" + sLocationEdit + "&mode=driving";
                   System.out.println("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + sFromLocation + "&destinations=" + sLocationEdit + "&mode=driving");
                   //https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + CurLoc + "&destinations=" + str_to + "&mode=driving
                   new GeoTask(NotificationActivity.this).execute(url);
               }
            }
        });


        calendarInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(NotificationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        start = Calendar.getInstance();
                        end = Calendar.getInstance();
                        start.set(mYear, mMonth, mDate, hourOfDay, minute);
                        end.set(mYear, mMonth, mDate, hourOfDay + 1, minute);
                        long startMillis = start.getTimeInMillis();
                        long endMillis = end.getTimeInMillis();
                        TimeZone timeZone = TimeZone.getDefault();
                        ContentResolver cr = getContentResolver();
                        valuesCalendarInsertEvent = new ContentValues();
                        valuesCalendarInsertEvent.put(CalendarContract.Events.TITLE, "Advisor appointment with " + nameEdit.getText().toString());
                        valuesCalendarInsertEvent.put(CalendarContract.Events.EVENT_COLOR, "#314249");
                        valuesCalendarInsertEvent.put(CalendarContract.Events.EVENT_LOCATION, locationEdit.getText().toString());
                        valuesCalendarInsertEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "India");
                        valuesCalendarInsertEvent.put(CalendarContract.Events.DTSTART, startMillis);
                        valuesCalendarInsertEvent.put(CalendarContract.Events.DTEND, endMillis);
                        valuesCalendarInsertEvent.put(CalendarContract.Events.ALL_DAY, 0);
                        valuesCalendarInsertEvent.put(CalendarContract.Events.CALENDAR_ID, 3);
                        // calendarIdExample();
                        if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                            // Toast.makeText(getApplicationContext(), "NOooooo Invite Sent", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(NotificationActivity.this,
                                    new String[]{Manifest.permission.WRITE_CALENDAR},
                                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT);
                        } else {
                            uriCalendarInsertEvent = cr.insert(CalendarContract.Events.CONTENT_URI, valuesCalendarInsertEvent);
                            long eventID = Long.parseLong(uriCalendarInsertEvent.getLastPathSegment());
                            addAttendees(eventID);
                            Toast.makeText(getApplicationContext(), "Invite Sent", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(NotificationActivity.this, CalendarListActivity.class);
                            startActivity(i);
                            ActivityCompat.finishAffinity(NotificationActivity.this);
                        }
                    }
                }, mHour, mMinute, false);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear;
                mDate = dayOfMonth;
                timePickerDialog.show();

            }
        };

        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "on click", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(NotificationActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    // return;
                } else {
                   // Toast.makeText(getApplicationContext(), "have permission", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + phoneEdit.getText().toString()));
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailActivity = new Intent(NotificationActivity.this, SendEmailActivity.class);
                emailActivity.putExtra("email", emailEdit.getText().toString());
                startActivity(emailActivity);

            }
        });
    }

    /*public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };*/

    // The indices for the projection array above.
   /* private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;*/

    // Run query
    // Cursor cur = null;

    /*public void calendarIdExample() {
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
        Toast.makeText(this, "selection: " + selection, Toast.LENGTH_SHORT).show();
        String[] selectionArgs = new String[]{"singh.prateeksha14@gmail.com"
        };

        if (ContextCompat.checkSelfPermission(DummyNotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            // return;
            ActivityCompat.requestPermissions(DummyNotificationActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        } else {
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        }
        // Use the cursor to step through the returned records
        while (cur.moveToNext())

        {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            // Do something with the values...

            Toast.makeText(getApplicationContext(), "callId: " + calID + " displayName: " + displayName + "accountName: " + accountName + "ownerName: " + ownerName, Toast.LENGTH_LONG).show();


        }

    }*/

    public void addAttendees(long eventId) {

        ContentResolver cr = getContentResolver();
        valuesCalendarInsertAttendees = new ContentValues();
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_NAME, nameEdit.getText().toString());
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_EMAIL, emailEdit.getText().toString());
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED);
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
        valuesCalendarInsertAttendees.put(CalendarContract.Attendees.EVENT_ID, eventId);
        if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            //return;
            ActivityCompat.requestPermissions(NotificationActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_ATTENDEES);
        } else {
            uriCalendarInsertAttendees = cr.insert(CalendarContract.Attendees.CONTENT_URI, valuesCalendarInsertAttendees);
        }

    }


    public void setDouble(String result) {

        String res[]=result.split(",");
        Double min=Double.parseDouble(res[0])/60;
        int dist=Integer.parseInt(res[1])/1000;
        String dest_adrs= res[2];
        /*tv_result1.setText("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tv_result2.setText("Distance= " + dist + " kilometers");*/
        //Changes
        /*System.out.println("Des");
        locationEdit.setText(dest_adrs, TextView.BufferType.EDITABLE);*/
        AlertDialog.Builder alert = new AlertDialog.Builder(NotificationActivity.this);
        alert.setTitle("Info");
        alert.setMessage("Duration:" + (int) (min / 60) + " hr " + (int) (min % 60) + " mins"+ "\n"+ "Distance: " + dist + " kilometers");
        alert.setPositiveButton("OK",null);
        alert.show();

    }

    @Override
    // @SuppressWarnings({"MissingPermission"})
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        ContentResolver cr=getContentResolver();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_ATTENDEES: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(getApplicationContext(), "yayy", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                        uriCalendarInsertAttendees = cr.insert(CalendarContract.Attendees.CONTENT_URI, valuesCalendarInsertAttendees);
                    }

                } else {
                    // Toast.makeText(getApplicationContext(), "booo", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "yayy", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(getApplicationContext(), "have permission", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + phoneEdit.getText().toString()));
                        getApplicationContext().startActivity(intent);

                    }

                } else {
                    // Toast.makeText(getApplicationContext(), "boooooo", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR_EVENT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(), "yayy", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(NotificationActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                        uriCalendarInsertEvent = cr.insert(CalendarContract.Events.CONTENT_URI, valuesCalendarInsertEvent);
                        long eventID = Long.parseLong(uriCalendarInsertEvent.getLastPathSegment());
                        addAttendees(eventID);
                        Toast.makeText(getApplicationContext(), "Invite Sent", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NotificationActivity.this, CalendarListActivity.class);
                        startActivity(i);
                        ActivityCompat.finishAffinity(NotificationActivity.this);
                    }

                } else {
                    // Toast.makeText(getApplicationContext(), "booo", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });

                        }
                    }

                }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(NotificationActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void viewAll()
    {
        Cursor res = mydb.getAllData();
        StringBuffer buffer = new StringBuffer();
        String title_str = null;
        String subtitle_str =null;
        int i=0;
        while (res.moveToNext())
        {
            buffer.append("ID: "+ res.getInt(0)+"\n");
            buffer.append("title: "+ res.getString(1)+"\n");
            title_str= res.getString(1);
            buffer.append("message: "+ res.getString(2)+"\n");
            subtitle_str= res.getString(2);
            //datum.put("title", title_str);
            //datum.put("subtitle", subtitle_str);
            //data.add(datum);
            buffer.append("insured: "+ res.getString(3)+"\n");
            buffer.append("email: "+ res.getString(4)+"\n");
            buffer.append("phone: "+ res.getString(5)+"\n");
            buffer.append("policy_num: "+ res.getString(6)+"\n");
            buffer.append("amount: "+ res.getString(7)+"\n");
            buffer.append("currency: "+ res.getString(8)+"\n");
            buffer.append("due_date: "+ res.getString(9)+"\n");
            buffer.append("notes: "+ res.getString(10)+"\n\n");
            i = i+1;
        }

        //showMessage("Data", buffer.toString());
    }

    public List<Address> Dist_Dura_Call_BTN(LatLng point) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
    try{

        geocoder = new Geocoder(this);
        if (point.latitude != 0 || point.longitude != 0) {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getAddressLine(2);
            String comma= ",";
            String currentLocation= address+comma+city+comma+country;
            System.out.println(address + " - " + city + " - " + country);
            System.out.println(currentLocation);
            System.out.println(addresses);
            showMessage("Success", "address" + address + " ,City" + city + " ,Country" + country);
            return addresses;
        }
    }
    catch (IOException e){
            Toast.makeText(this, "latitude and longitude are null", Toast.LENGTH_LONG).show();
            showMessage("Error", "latitude and longitude are null");
            return null;
        } catch (Exception e1){

        showMessage("Error", "Not Getting");
        return null;
    }

        //showMessage("Test", "Test1");
        //getDistanceInfo();
        return null;
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(NotificationActivity.this, CalendarListActivity.class);
        startActivity(i);
        finish();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    private double getDistanceInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        Double dist = 0.0;
        Double latTo= 0.0;
        Double lngTo= 0.0;
        try {


            //destinationAddress = destinationAddress.replaceAll(" ","%20");
            //URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lng1 + "&destination=" + latTo + "," + lngTo + "&mode=driving&sensor=false");
            URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Magarpatta, Hadapsar, Pune&destinations=New Sangvi, Pune&mode=driving");


            HttpURLConnection client=(HttpURLConnection) url.openConnection();


        }  catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject = new JSONObject(stringBuilder.toString());

            JSONArray array = jsonObject.getJSONArray("routes");

            JSONObject routes = array.getJSONObject(0);

            JSONArray legs = routes.getJSONArray("legs");

            JSONObject steps = legs.getJSONObject(0);

            JSONObject distance = steps.getJSONObject("distance");

            Log.i("Distance", distance.toString());
            dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dist;
    }


    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("It has come from ","getFromLocation");
            System.out.println(addresses);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current location address", strReturnedAddress.toString());
            } else {
                Log.w("My Current location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location address", "Can't get Address!");
            Toast.makeText(getApplicationContext(), "Can't Access your location"+ "\n"+ "Please enter it 'From Field' manually" , Toast.LENGTH_LONG).show();

        }
        return strAdd;
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }*/

}
