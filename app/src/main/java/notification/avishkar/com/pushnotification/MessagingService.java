package notification.avishkar.com.pushnotification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Test on 9/22/2017.
 */

public class MessagingService extends FirebaseMessagingService{
    private static final String MESSAGING_SERVICE = "MESSAGING_SERVICE";
    boolean isPushNotificationSaved = false;
    DatabaseHelper mydb;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this,NotificationListActivity.class);
        intent.putExtra("MessagingService","MessagingService");
        intent.putExtra("title",remoteMessage.getData().get("title"));
        intent.putExtra("message",remoteMessage.getData().get("message"));
        intent.putExtra("insured",remoteMessage.getData().get("insured"));
        mydb = new DatabaseHelper(this);
        if (remoteMessage.getData().get("title") != null && remoteMessage.getData().get("message") != null && remoteMessage.getData().get("insured") != null)
        {
            isPushNotificationSaved = mydb.insertData(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("insured"), remoteMessage.getData().get("email"), remoteMessage.getData().get("phone_num"), remoteMessage.getData().get("policy_num"), remoteMessage.getData().get("amount"), remoteMessage.getData().get("currency"), remoteMessage.getData().get("due_date"), remoteMessage.getData().get("notes"));
            if (isPushNotificationSaved = true) {
                intent.putExtra("Result","Success");
            }
            if (isPushNotificationSaved != true) {
                intent.putExtra("Result","Failed");
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("aAviskar");
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(notification.avishkar.com.pushnotification.R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

    }
}
