/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.ContentValues;
import android.content.Context;

public class Reminder {
    private final ContentValues contentValues = new ContentValues(10);

    private int id;
    private boolean active = false;
    private float count = 0.0f;
    private long time = -1;
    private long startDate = -1;
    private long endDate = -1;
    private byte daysOfWeek = 0;
    private int interval = -1;
    private int medicationId = -1;

    public Reminder(int _id, boolean _active, float _count, long _time, long _startDate,
                    long _endDate, byte _daysOfWeek, int _interval, int _medicationId) {

        if ( _id >=0) {
            id = _id;
        }

        this.active = _active;
        this.count = _count;
        this.time = _time;
        this.startDate = _startDate;
        this.endDate = _endDate;
        this.daysOfWeek = _daysOfWeek;
        this.interval = _interval;
        this.medicationId = _medicationId;
    }

    public Reminder() {}

    public static Reminder getById(int _id) {
        return new Reminder();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public float getCount() { return count; }
    public void setCount(float count) { this.count = count; }
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
        contentValues.put("id", id);
        contentValues.put("active", active);
        contentValues.put("count", count);
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
