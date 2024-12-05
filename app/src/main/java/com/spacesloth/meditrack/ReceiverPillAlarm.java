/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverPillAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//        Medication medication = databaseHelper.getPill(intent.getIntExtra(PRIMARY_KEY_INTENT_KEY_STRING, -1));
//        medication.sendPillNotification(context);
//        medication.setAlarm(context);
    }
}
