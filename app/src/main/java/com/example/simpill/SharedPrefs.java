/* (C) 2022 */
package com.example.simpill;

import static com.example.simpill.Simpill.BLUE_THEME;

import android.content.Context;
import android.content.res.Configuration;

public class SharedPrefs {

    public static final String APP_METADATA = "app_metadata";
    public static final String APP_SETTINGS = "app_settings";

    public static final String ENABLE_DARK_DIALOGS = "enable_dark_dialogs";
    public static final String APP_OPEN_COUNTER = "app_open_counter";
    public static final String APP_THEME = "app_theme";
    public static final String ENABLE_24HR_TIME = "enable_24hr_time";
    public static final String ENABLE_PERMANENT_NOTIFICATIONS = "enable_permanent_notifications";
    public static final String ENABLE_APP_SOUND = "enable_app_sound";

    private final Context context;

    public SharedPrefs(Context context) {
        this.context = context;
    }

    public int getOpenCountPref() {
        return context.getSharedPreferences(APP_METADATA, Context.MODE_PRIVATE)
                .getInt(APP_OPEN_COUNTER, 0);
    }

    public void setOpenCountPref(int openCount) {
        context.getSharedPreferences(APP_METADATA, Context.MODE_PRIVATE)
                .edit()
                .putInt(APP_OPEN_COUNTER, openCount)
                .apply();
    }

    public void setDarkDialogsPref(boolean darkDialogs) {
        context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(ENABLE_DARK_DIALOGS, darkDialogs)
                .apply();
    }

    public boolean getDarkDialogsPref() {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .getBoolean(
                        ENABLE_DARK_DIALOGS,
                        (context.getResources().getConfiguration().uiMode
                                        & Configuration.UI_MODE_NIGHT_MASK)
                                == Configuration.UI_MODE_NIGHT_YES);
    }

    public void setThemesPref(int theme) {
        context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putInt(APP_THEME, theme)
                .apply();
    }

    public int getThemesPref() {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .getInt(APP_THEME, BLUE_THEME);
    }

    public void setStickyNotificationsPref(boolean stickyNotifications) {
        context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(ENABLE_PERMANENT_NOTIFICATIONS, stickyNotifications)
                .apply();
    }

    public boolean getStickyNotificationsPref() {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .getBoolean(ENABLE_PERMANENT_NOTIFICATIONS, false);
    }

    public void set24HourTimeFormatPref(boolean is24HourFormat) {
        context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(ENABLE_24HR_TIME, is24HourFormat)
                .apply();
    }

    public boolean get24HourFormatPref() {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .getBoolean(ENABLE_24HR_TIME, false);
    }

    public void setPillSoundPref(boolean soundOn) {
        context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(ENABLE_APP_SOUND, soundOn)
                .apply();
    }

    public boolean getPillSoundPref() {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
                .getBoolean(ENABLE_APP_SOUND, false);
    }
}
