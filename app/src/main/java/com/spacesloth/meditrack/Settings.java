/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class Settings extends AppCompatActivity implements Dialogs.SettingsDialogListener {

    final Toasts toasts = new Toasts(this);

    boolean settingsChanged = false;

    Button btnBack;
    SwitchCompat clockIs24HrSwitch,
            permanentNotificationsSwitch,
            darkDialogsSwitch,
            appSoundsSwitch;
    Button themesBtn, btnDeleteData, btnLoadTestData;

    private final SharedPrefs sharedPrefs = new SharedPrefs(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("theme_changed_bool")
                && getIntent().getBooleanExtra("theme_changed_bool", false)) {
            settingsChanged = true;
        }
        setContentViewBasedOnThemeSetting();
        initWidgets();
        createOnClickListeners();

        getOnBackPressedDispatcher()
            .addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (settingsChanged) {
                            Process.killProcess(Process.myPid());
                        } else {
                            finish();
                        }
                    }
                });
    }

    private void createOnClickListeners() {
        Dialogs getDialogs = new Dialogs(this);

        btnDeleteData.setOnClickListener(view -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteDatabase();
            int medsCount = db.getMedsCount(true, true);
            int remindersCount = db.getRemindersCount(true);
            String changes =  " " + medsCount + " meds, " + remindersCount + " reminders.";
            toasts.showCustomToast("Deleted the database." + changes);
            settingsChanged = true;
        });
        btnLoadTestData.setOnClickListener(view -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.loadTestData();
            int medsCount = db.getMedsCount(true, true);
            int remindersCount = db.getRemindersCount(true);
            String changes =  " " + medsCount + " meds, " + remindersCount + " reminders.";
            toasts.showCustomToast("Loaded test data." + changes);
            settingsChanged = true;
        });
        btnBack.setOnClickListener(
            view -> {
                if (settingsChanged) {
                    Process.killProcess(Process.myPid());
                } else {
                    finish();
                }
            }
        );
        darkDialogsSwitch.setOnClickListener(
                view -> {
                    sharedPrefs.setDarkDialogsPref(darkDialogsSwitch.isChecked());
                    toasts.showCustomToast(
                            sharedPrefs.getDarkDialogsPref()
                                    ? getString(R.string.dark_dialogs_toast)
                                    : getString(R.string.light_dialogs_toast));
                    settingsChanged = true;
                });
        clockIs24HrSwitch.setOnClickListener(
                view -> {
                    sharedPrefs.set24HourTimeFormatPref(clockIs24HrSwitch.isChecked());
                    toasts.showCustomToast(
                            sharedPrefs.get24HourFormatPref()
                                    ? getString(R.string.time_format_24hr_toast)
                                    : getString(R.string.time_format_12hr_toast));
                    settingsChanged = true;
                });
        appSoundsSwitch.setOnClickListener(
                view -> {
                    sharedPrefs.setPillSoundPref(appSoundsSwitch.isChecked());
                    toasts.showCustomToast(
                            sharedPrefs.getPillSoundPref()
                                    ? getString(R.string.app_sounds_enabled)
                                    : getString(R.string.app_sounds_disabled));
                    settingsChanged = true;
                });
        permanentNotificationsSwitch.setOnClickListener(
                view -> {
                    sharedPrefs.setStickyNotificationsPref(
                            permanentNotificationsSwitch.isChecked());
                    toasts.showCustomToast(
                            sharedPrefs.getStickyNotificationsPref()
                                    ? getString(R.string.sticky_notifications_enabled_toast)
                                    : getString(R.string.sticky_notifications_disabled_toast));
                    settingsChanged = true;
                });
    }

    private void setContentViewBasedOnThemeSetting() {
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_settings);
    }

    private void initWidgets() {
        btnBack = findViewById(R.id.btn_back);
        themesBtn = findViewById(R.id.theme_select_btn);
        clockIs24HrSwitch = findViewById(R.id.clock_24hr_switch);
        darkDialogsSwitch = findViewById(R.id.dark_dialogs_switch);
        btnDeleteData = findViewById(R.id.btn_delete_data);
        btnLoadTestData = findViewById(R.id.btn_load_test_data);
        permanentNotificationsSwitch = findViewById(R.id.sticky_notifications_switch);
        appSoundsSwitch = findViewById(R.id.soundSwitch);

        clockIs24HrSwitch.setChecked(sharedPrefs.get24HourFormatPref());
        darkDialogsSwitch.setChecked(sharedPrefs.getDarkDialogsPref());
        permanentNotificationsSwitch.setChecked(sharedPrefs.getStickyNotificationsPref());
        appSoundsSwitch.setChecked(sharedPrefs.getPillSoundPref());
    }

    @Override
    public void recreateScreen() {
        finish();

        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("theme_changed_bool", true);
        startActivity(intent);
    }
}
