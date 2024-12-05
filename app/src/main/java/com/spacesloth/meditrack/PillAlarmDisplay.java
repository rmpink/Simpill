/* (C) 2022 */
package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class PillAlarmDisplay extends AppCompatActivity {

    TextView pillName, pillTime;
    Button takenBtn, dismissBtn;

    Intent intent;
    Medication medication;

    AudioHelper audioHelper;
    Vibrator vibrator;
    final long[] vibratorPattern = {0, 0, 500, 1220, 1600, 1220, 1600, 1220, 1583};
    MediaPlayer alarmPlayer, takenPlayer;

    @SuppressLint("InlinedApi")
    final AudioAttributes audioAttributes =
            new AudioAttributes.Builder()
                    .setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

    private final SharedPrefs sharedPrefs = new SharedPrefs(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioHelper = new AudioHelper(this);
        intent = getIntent();
//        medication =
//                new DatabaseHelper(this)
//                        .getPill(intent.getIntExtra(PRIMARY_KEY_INTENT_KEY_STRING, -1));
        setContentViewBasedOnThemeSetting();
        initVibratorAndAlarm();
        initWidgets();
        createOnClickListeners();

        pillName.setText(medication.getName());
//        String currentTime =
//                this.getString(
//                        R.string.its_time,
//                        sharedPrefs.get24HourFormatPref()
//                                ? dateTimeManager.getCurrentTimeString()
//                                : new DateTimeManager()
//                                        .convert24HrTimeTo12HrTime(
//                                                dateTimeManager.getCurrentTimeString()));
//        pillTime.setText(currentTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager =
                    (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null) {
                keyguardManager.requestDismissKeyguard(this, null);
            }
        } else {
            getWindow()
                    .addFlags(
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        getOnBackPressedDispatcher()
                .addCallback(
                        this,
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {}
                        });
    }

    void initVibratorAndAlarm() {
//        vibrator = audioHelper.getVibrator();
//        alarmPlayer = audioHelper.getAlarmPlayer(medication.getCustomAlarmUri());
//        alarmPlayer.start();
//        if (medication.getCustomAlarmUri() == DEFAULT_ALARM_URI)
//            vibrator.vibrate(vibratorPattern, 0, audioAttributes);
    }

    void stopAlarmAndVibrator() {
        alarmPlayer.stop();
        alarmPlayer.release();
        vibrator.cancel();
    }

    void setContentViewBasedOnThemeSetting() {
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_pill_alarm);
    }

    void initWidgets() {
        pillName = findViewById(R.id.pill_name_textview);
        pillTime = findViewById(R.id.pill_time_textview);
        takenBtn = findViewById(R.id.taken_pill_alarm_btn);
        dismissBtn = findViewById(R.id.dismiss_alarm_btn);
    }

    void createOnClickListeners() {
//        takenBtn.setOnClickListener(
//                v -> {
//                    medication.takePill(this);
//                    medication.deleteActiveNotifications(this);
//                    vibrator.cancel();
//                    stopAlarmAndVibrator();
//                    takenPlayer = MediaPlayer.create(PillAlarmDisplay.this, R.raw.correct);
//                    takenPlayer.start();
//                    finish();
//                    startActivity(new Intent(this, MainActivity.class));
//                });
        dismissBtn.setOnClickListener(
                v -> {
                    stopAlarmAndVibrator();
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                });
    }
}
