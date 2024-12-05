/* (C) 2022 */
package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Objects;

public class ReceiverDeviceBoot extends BroadcastReceiver {
    @SuppressLint("ShortAlarm")
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper db = new DatabaseHelper(context);

//        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")
//                && db.getMedsCount(false, false) > 0) {
//            Medication[] medications = db.getAllPills();
//            for (Medication medication : medications) {
//                medication.setAlarm(context);
//                medication.setStockupAlarm(context);
//            }
//            new Toasts(context).showCustomToast(context.getString(R.string.device_restart_toast));
//        }
    }
}
