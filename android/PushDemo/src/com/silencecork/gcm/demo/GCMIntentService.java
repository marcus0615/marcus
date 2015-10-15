package com.silencecork.gcm.demo;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

	NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		
		String type = GoogleCloudMessaging.getInstance(this).getMessageType(intent);
		if (!GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(type)) {
			return;
		}
		
		if (!extras.isEmpty()) {

			// read extras as sent from server
			String message = extras.getString("message");
			String serverTime = extras.getString("timestamp");
			sendNotification("Message: " + message + "\n" + "Server Time: "
					+ serverTime);
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Notification from GCM")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());
	}

}
