/* (C) 2022 */
package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

public class AudioHelper {

    public static final int ALARM_REMINDER_SESSION_ID = 1;
    public static final long[] vibratorPattern = {0, 0, 500, 1220, 1600, 1220, 1600, 1220, 1583};

    final Context context;

    public AudioHelper(Context context) {
        this.context = context;
    }

    @SuppressLint("InlinedApi")
    public MediaPlayer getAlarmPlayer(Uri alarmUri) {
        MediaPlayer alarmPlayer;
        try {
            alarmPlayer =
                    MediaPlayer.create(
                            context,
                            alarmUri,
                            null,
                            new AudioAttributes.Builder()
                                    .setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .build(),
                            ALARM_REMINDER_SESSION_ID);
            alarmPlayer.setLooping(true);
        } catch (NullPointerException nullPointerException) {
            alarmPlayer =
                    MediaPlayer.create(
                            context,
                            null,
                            null,
                            new AudioAttributes.Builder()
                                    .setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .build(),
                            ALARM_REMINDER_SESSION_ID);
            alarmPlayer.setLooping(true);
        }
        return alarmPlayer;
    }

    public MediaPlayer getShakePlayer() {
        return MediaPlayer.create(context, R.raw.shake);
    }

    public MediaPlayer getTakenPlayer() {
        MediaPlayer takenPlayer = MediaPlayer.create(context, R.raw.correct);
        takenPlayer.setVolume(0.5f, 0.5f);
        return takenPlayer;
    }

    public Vibrator getVibrator() {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
