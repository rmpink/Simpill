/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.ContentValues;
import android.content.Context;

public class Reminder {
    private final ContentValues contentValues = new ContentValues(8);

    private int id;
    private boolean active = false;
    private float takeAmount = 0.0f;
    private long time = -1;
    private long startDate = -1;
    private long endDate = -1;
    private byte daysOfWeek = (byte) 0;
    private int interval = -1;
    private int medicationId = -1;

    public Reminder(boolean _active, float _takeAmount, long _time, long _startDate,
                    long _endDate, byte _daysOfWeek, int _interval, int _medicationId) {

        this.active = _active;
        this.takeAmount = _takeAmount;
        this.time = _time;
        this.startDate = _startDate;
        this.endDate = _endDate;
        this.daysOfWeek = _daysOfWeek;
        this.interval = _interval;
        this.medicationId = _medicationId;

        this.setContentValues();
    }

    public Reminder() {}

    public static Reminder getById(Context ctx, int _id) {
        DatabaseHelper db = new DatabaseHelper(ctx);
        Reminder reminder = db.readReminder(_id);
        reminder.id = _id;
        db.close();
        return reminder;
    }

    public int getId() { return id; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public float getTakeAmount() { return takeAmount; }
    public void setTakeAmount(float takeAmount) { this.takeAmount = takeAmount; }
    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }
    public byte getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(byte daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    public int getMedicationId() { return medicationId; }
    public void setMedicationId(int medicationId) { this.medicationId = medicationId; }

    // Separate out Context and DatabaseHelper from this class completely.
    public Boolean createDB(Context context) {
        setContentValues();
        try (DatabaseHelper db = new DatabaseHelper(context)) {
            this.id = db.createReminder(this);
        }

        return this.id >= 0;
    }

    public void updateDB(Context context) {
        setContentValues();
        try (DatabaseHelper db = new DatabaseHelper(context)) {
            db.updateReminder(this);
        }
    }

    public void deleteDB(Context context, int recyclerViewPosition) {
        try (DatabaseHelper db = new DatabaseHelper(context)) {
            db.deleteReminder(this);
        }

        ReminderListener reminderListener = (ReminderListener) context;
        reminderListener.notifyDeletedReminder(this, recyclerViewPosition);
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues() {
        contentValues.put("active", active);
        contentValues.put("count", takeAmount);
        contentValues.put("time", time);
        contentValues.put("start_date", startDate);
        contentValues.put("end_date", endDate);
        contentValues.put("days_of_week", daysOfWeek);
        contentValues.put("interval", interval);
        contentValues.put("medication_id", medicationId);
    }

    public void fulfill(Context context) {
    }

    public interface ReminderListener {

        void notifyAddedReminder(Reminder reminder);

        void notifyDeletedReminder(Reminder reminder, int position);

        void notifyResetReminder(int position);
    }
}
