/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "MedicationTracker.db", null, 2);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_CreateMedicationTable =
                "CREATE TABLE IF NOT EXISTS Medications( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "strength REAL NOT NULL, " +
                "strength_units LONGINT NOT NULL, " +
                "count REAL NOT NULL, " +
                "icon INTEGER NOT NULL, " +
                "colour INTEGER NOT NULL, " +
                "refill_reminder BOOLEAN NOT NULL, " +
                "refill_count REAL, " +
                "refill_time BIGINT, " +
                "rx_number TEXT," +
                "active BOOLEAN NOT NULL," +
                "visible BOOLEAN NOT NULL);";

        String sql_CreateReminderTable =
                "CREATE TABLE IF NOT EXISTS Reminders( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "medication_id INTEGER, " +
                "active BOOLEAN, " +
                "count REAL NOT NULL, " +
                "time BIGINT NOT NULL," +
                "start_date BIGINT," +
                "end_date BIGINT," +
                "days_of_week BYTE," +
                "interval INT, " +
                "FOREIGN KEY(medication_id) REFERENCES Medications(id));";

        String sql_CreateRecordTable =
                "CREATE TABLE IF NOT EXISTS Records(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "medication_id INTEGER, " +
                "timestamp BIGINT NOT NULL, " +
                "FOREIGN KEY(medication_id) REFERENCES Medications(id));";

        db.execSQL(sql_CreateMedicationTable);
        db.execSQL(sql_CreateReminderTable);
        db.execSQL(sql_CreateRecordTable);
    }

    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Medications", null, null);
        db.delete("Reminders", null, null);
        db.delete("Records", null, null);
    }

    public void loadTestData() {
        createMedication(new Medication("Bupropion", 300, "mg", 58, -1, -1, false, -1, 0, null, true, true));
        createMedication(new Medication("Cambia", 50, "mg", 16, -1, -1, false, -1, 0, null, true, true));
        createMedication(new Medication("Cyproterone", 50, "mg", 88, -1, -1, false, -1, 0, null, true, true));
        createMedication(new Medication("Estrace", 2, "mg", 305, -1, -1, false, -1, 0, null, true, true));
        createMedication(new Medication("Lorazepam", 0.5F, "mg", 41, -1, -1, false, -1, 0, null, true, true));
        createMedication(new Medication("Sertraline", 300, "mg", 195, -1, -1, false, -1, 0, null, true, true));
        createMedication(new Medication("Quetiapine", 25, "mg", 25, -1, -1, false, -1, 0, null, false, true));
        createMedication(new Medication("Atenolol", 6.25F, "mg", 114, -1, -1, false, -1, 0, null, false, false));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldDbNumber, int newDbNumber) {}

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldDbNumber, int newDbNumber) {}

    public List<Medication> getAllMedications(Boolean visible, Boolean active) {
        List<Medication> medsList = new ArrayList<>();
        String query = "SELECT * FROM Medications WHERE " +
                "visible=" + visible.toString().toUpperCase() + " AND " +
                "active=" + active.toString().toUpperCase();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            medsList.add(returnMedicationFromCursor(c));
            c.moveToNext();
        }
        c.close();
        return medsList;
    }

    public List<Reminder> getAllReminders(Boolean active) {
        List<Reminder> remindersList = new ArrayList<Reminder>();
        String query = "SELECT * FROM Reminders WHERE " +
                "active=" + active.toString().toUpperCase() + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            remindersList.add(returnRemindersFromCursor(c));
        }
        c.close();
        return remindersList;
    }

    public int getMedsCount(Boolean visible, Boolean active) {
        String query = "SELECT COUNT(id) FROM Medications WHERE " +
                "visible=" + visible.toString().toUpperCase() + " AND " +
                "active=" + active.toString().toUpperCase() + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        int rowCount = 0;

        try {
            c.moveToFirst();
            rowCount = c.getInt(0);
        } catch (Exception ignored) {
            // stub
        } finally {
            c.close();
        }

        return rowCount;
    }

    public int getRemindersCount(Boolean active) {
        String query = "SELECT COUNT(id) FROM Reminders WHERE " +
                "active=" + active.toString().toUpperCase() + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        int rowCount = 0;

        try {
            c.moveToFirst();
            rowCount = c.getInt(0);
        } catch (Exception ignored) {
            // stub
        } finally {
            c.close();
        }

        return rowCount;
    }

    public int createMedication(Medication med) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            return (int) db.insert("Medications", null, med.getContentValues());
        } catch (Exception ignored) {
            return -1;
        }
    }

    public Medication readMedication(long id) {
        String query = "SELECT * FROM Medications WHERE id=" + id + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Medication med = returnMedicationFromCursor(c);
        c.close();

        return med;
    }

    public void updateMedication(Medication med) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update("Medications",
                med.getContentValues(),
                "id=?",
                new String[] { String.valueOf(med.getId()) });
    }

    public void deleteMedication(Medication med) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Medications", "id=?",
                new String[] { String.valueOf(med.getId()) });
    }

    public int createReminder(Reminder reminder) {
        return 0;
    }

    public void updateReminder(Reminder reminder) {}

    public void deleteReminder(Reminder reminder) {}

    private Medication returnMedicationFromCursor(Cursor c) {
        return new Medication(
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getFloat(c.getColumnIndexOrThrow("strength")),
                c.getString(c.getColumnIndexOrThrow("strength_units")),
                c.getFloat(c.getColumnIndexOrThrow("count")),
                c.getInt(c.getColumnIndexOrThrow("icon")),
                c.getInt(c.getColumnIndexOrThrow("colour")),
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("refill_reminder"))),
                c.getFloat(c.getColumnIndexOrThrow("refill_count")),
                c.getLong(c.getColumnIndexOrThrow("refill_time")),
                c.getString(c.getColumnIndexOrThrow("rx_number")),
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("active"))),
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("visible")))
        );
    }

    private Reminder returnRemindersFromCursor(Cursor c) {
        return new Reminder(
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("active"))),
                c.getFloat(c.getColumnIndexOrThrow("count")),
                c.getLong(c.getColumnIndexOrThrow("time")),
                c.getLong(c.getColumnIndexOrThrow("start_date")),
                c.getLong(c.getColumnIndexOrThrow("end_date")),
                (byte) c.getInt(c.getColumnIndexOrThrow("days_of_week")),
                c.getInt(c.getColumnIndexOrThrow("interval")),
                c.getInt(c.getColumnIndexOrThrow("medication_id")));
    }
}
