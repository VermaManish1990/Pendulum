package com.pendulum.gcm;/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 *//*


package com.com.taxi.gcm;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public abstract class GcmIntentService extends IntentService {
    public static final String TAG = "GCM Base Implementation";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String msg = intent.getStringExtra("message");
        String type = intent.getStringExtra("request_type");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(messageType, "Send error: " + extras.toString(), "error");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(messageType, "Deleted messages on server: " +
                        extras.toString(), "Deleted");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                // Post notification of received message.
                sendNotification(messageType, msg, type);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void sendNotification(String googleCloudMessagType, String msg, String notificationType) {
    }

    protected abstract PendingIntent getIntentForPush(String googleCloudMessagType, String msg, String notificationType);

}*/
