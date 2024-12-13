/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;

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
                "icon TEXT NOT NULL, " +
                "colour TEXT NOT NULL, " +
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
        db.delete("Records", null, null);
        db.delete("Reminders", null, null);
        db.delete("Medications", null, null);
    }

    public void loadTestData() {
//        int bup_id, cam_id, cyp_id, est_id, lor_id, ser_id, que_id, ate_id;
//        bup_id = createMedication(new Medication("Abilify", 1, "mg", 10, "look_bullet", "", false, -1, 0, null, true, true));
//        cam_id = createMedication(new Medication("Cambia", 50, "mg", 16, "look_triangle", "", false, -1, 0, null, true, true));
//        cyp_id = createMedication(new Medication("Cyproterone", 50, "mg", 88, "look_circleline", "", false, -1, 0, null, true, true));
//        est_id = createMedication(new Medication("Estrace", 2, "mg", 305, "look_circleline", "#ADD8E6", false, -1, 0, null, true, true));
//        lor_id = createMedication(new Medication("Lorazepam", 0.5F, "mg", 41, "look_circleline", "", false, -1, 0, null, true, true));
//        ser_id = createMedication(new Medication("Sertraline", 50, "mg", 195, "look_capsulemono", "#FFFF00", false, -1, 0, null, true, true));
        int a_id, b_id, c_id, d_id, e_id, f_id, g_id, h_id, i_id, j_id, k_id, l_id, m_id, n_id, o_id, p_id, q_id, r_id, s_id, t_id, u_id, v_id, w_id, x_id, y_id, z_id;
        a_id = createMedication(new Medication("Alastor", 1, "units", 10, "look_bullet", "#7FFFD4", true, 10, 100, null, true, false));
        b_id = createMedication(new Medication("Belphegor", 2, "mg", 20, "look_butterfly", "#A52A2A", false, -1, 0, null, false, true));
        c_id = createMedication(new Medication("Camio", 3, "IU", 30, "look_butterflyline", "#00FFFF", false, -1, 0, null, true, true));
        d_id = createMedication(new Medication("Dantalion", 4, "mcg", 40, "look_capsule", "#696969", false, -1, 0, null, true, true));
        e_id = createMedication(new Medication("Eligos", 5, "mcg/ml", 50, "look_capsuleduo", "#50C878", true, 20, 200, null, true, true));
        f_id = createMedication(new Medication("Focalor", 6, "mcg", 60, "look_capsulemono", "#4E9258", false, -1, 0, null, true, true));
        g_id = createMedication(new Medication("Glasya-Labolas", 7, "mEq", 70, "look_circle", "#F3E3C3", false, -1, 0, null, true, true));
        h_id = createMedication(new Medication("Hurakan", 8, "mL", 80, "look_circleline", "#D462FF", false, -1, 0, null, true, true));
        i_id = createMedication(new Medication("Ishtar", 9, "%", 90, "look_diamond", "#FF7722", true, 30, 300, null, true, false));
        j_id = createMedication(new Medication("Jormungand", 10, "mg/g", 100, "look_donut", "#A0CFEC", false, -1, 0, null, true, true));
        k_id = createMedication(new Medication("Kappas", 11, "mg/cm2", 110, "look_egg", "#4CC552", false, -1, 0, null, true, true));
        l_id = createMedication(new Medication("Lix Tetrax", 12, "mg/ml", 120, "look_eight", "#E42217", false, -1, 0, null, true, true));
        m_id = createMedication(new Medication("Melchom", 13, "mcg/hr", 130, "look_eightline", "#915F6D", false, -1, 0, null, true, true));
        n_id = createMedication(new Medication("Nybbas", 14, "units", 140, "look_gelcap", "#9D00FF", false, -1, 0, null, false, true));
        o_id = createMedication(new Medication("Oriax", 15, "mg", 150, "look_halfmoon", "#F8F0E3", true, 40, 400, null, false, true));
        p_id = createMedication(new Medication("Phenex", 16, "IU", 160, "look_octagon", "#A74AC7", false, -1, 0, null, true, false));
        q_id = createMedication(new Medication("Qemetiel", 17, "mcg", 170, "look_pentagon", "#F7CAC9", false, -1, 0, null, true, true));
        r_id = createMedication(new Medication("Ribesal", 18, "mcg/ml", 180, "look_pentagonline", "#EB5406", false, -1, 0, null, false, false));
        s_id = createMedication(new Medication("Stolas", 19, "mEq", 190, "look_rect", "#FBB917", false, -1, 0, null, true, false));
        t_id = createMedication(new Medication("Torngarsuk", 20, "mL", 200, "look_roundedrect", "#A0D6B4", false, -1, 0, null, true, true));
        u_id = createMedication(new Medication("Uvall", 21, "%", 210, "look_shield", "#7F5A58", true, 50, 500, null, true, true));
        v_id = createMedication(new Medication("Vapula", 22, "mg/g", 220, "look_shieldline", "#F6358A", false, -1, 0, null, true, true));
        w_id = createMedication(new Medication("Wechuge", 23, "mg/cm2", 230, "look_square", "", false, -1, 0, null, true, true));
        x_id = createMedication(new Medication("Xaphan", 24, "mg/ml", 240, "look_squarelines", "#C35817", false, -1, 0, null, true, true));
        y_id = createMedication(new Medication("Yan-gant-y-tan", 25, "mcg/hr", 250, "look_tablet", "#E2F516", false, -1, 0, null, false, true));
        z_id = createMedication(new Medication("Zepar", 26, "units", 260, "look_tabletline", "#893BFF", false, -1, 0, null, true, true));

        // EDGE CASES
        // --- Not active, but visible
//        que_id = createMedication(new Medication("Quetiapine", 25, "mg", 25, "", "", false, -1, 0, null, false, true));
        // --- Not active, not visible
//        ate_id = createMedication(new Medication("Atenolol", 6.25F, "mg", 114, "", "", false, -1, 0, null, false, false));

//        int bup_rem_id, cyp_rem_id, est_rem_id1, est_rem_id2, est_rem_id3, ser_rem_id, ate_rem_id, past_rem_id, future_rem_id;
//        bup_rem_id = createReminder(new Reminder(true, 1.0f, 570, 1704067200000L, 1735689600000L, (byte)127, -1, bup_id));
//        cyp_rem_id = createReminder(new Reminder(true, 3.0f, 570, 1704067200000L, 1735689600000L, (byte)34, -1, cyp_id));
//        est_rem_id1 = createReminder(new Reminder(true, 1.0f, 570, 1704067200000L, 1735689600000L, (byte)101, -1, est_id));
//        est_rem_id2 = createReminder(new Reminder(true, 1.0f, 960, 1704067200000L, 1735689600000L, (byte)127, -1, est_id));
//        est_rem_id3 = createReminder(new Reminder(true, 1.0f, 1350, 1704067200000L, 1735689600000L, (byte)17, -1, est_id));
//        ser_rem_id = createReminder(new Reminder(true, 1.0f, 570, 1704067200000L, 1735689600000L, (byte)127, 100, ser_id));
        int a_rem_id, b_rem_id, c_rem_id, d_rem_id, e_rem_id, f_rem_id, g_rem_id, h_rem_id, i_rem_id, j_rem_id, k_rem_id, l_rem_id, m_rem_id, n_rem_id, o_rem_id, p_rem_id, q_rem_id, r_rem_id, s_rem_id, t_rem_id, u_rem_id, v_rem_id, w_rem_id, x_rem_id, y_rem_id, z_rem_id;
        e_rem_id = createReminder(new Reminder(true, 1.0f, 300, 1704067200000L, 1735689600000L, (byte)127, -1, e_id));
        m_rem_id = createReminder(new Reminder(true, 2.0f, 300, 1704067200000L, 1735689600000L, (byte)127, -1, m_id));
        i_rem_id = createReminder(new Reminder(true, 3.0f, 300, 1704067200000L, 1735689600000L, (byte)127, -1, i_id));
        l_rem_id = createReminder(new Reminder(true, 4.0f, 300, 1704067200000L, 1735689600000L, (byte)127, -1, l_id));
        y_rem_id = createReminder(new Reminder(true, 5.0f, 300, 1704067200000L, 1735689600000L, (byte)127, -1, y_id));

        i_rem_id = createReminder(new Reminder(true, 1.0f, 780, 1704067200000L, 1735689600000L, (byte)127, -1, i_id));
        s_rem_id = createReminder(new Reminder(true, 2.0f, 780, 1704067200000L, 1735689600000L, (byte)127, -1, s_id));

        c_rem_id = createReminder(new Reminder(true, 1.0f, 1260, 1704067200000L, 1735689600000L, (byte)127, -1, c_id));
        u_rem_id = createReminder(new Reminder(true, 2.0f, 1260, 1704067200000L, 1735689600000L, (byte)127, -1, u_id));
        t_rem_id = createReminder(new Reminder(true, 3.0f, 1260, 1704067200000L, 1735689600000L, (byte)127, -1, t_id));
        e_rem_id = createReminder(new Reminder(true, 4.0f, 1260, 1704067200000L, 1735689600000L, (byte)127, -1, e_id));

        // EDGE CASES
        // --- Active = FALSE
//        ate_rem_id = createReminder(new Reminder(false, 0.25f, 1080, 1704067200000L, 1735689600000L, (byte)23, 100, ate_id));
        // --- End DateTime < Current DateTime
//        past_rem_id = createReminder(new Reminder(true, 0.25f, 1080, 1700000000000L, 1704067200000L, (byte)23, 100, ate_id));
        // --- Start DateTime > Current DateTime
//        future_rem_id = createReminder(new Reminder(true, 0.25f, 1080, 1735689600000L, 1800000000000L, (byte)23, 100, ate_id));
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

    public List<Reminder> getAllReminders(Boolean active, String orderBy, String direction) {
        List<Reminder> remindersList = new ArrayList<>();
        String query = "SELECT * FROM Reminders WHERE " +
                "active=" + active.toString().toUpperCase();

        if (orderBy != null) {
            query += " ORDER BY " + orderBy + " " + direction;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            remindersList.add(returnReminderFromCursor(c));
            c.moveToNext();
        }
        c.close();
        return remindersList;
    }

    public List<Reminder> getRemindersByDateTime(Boolean active, DateTime dateTime, String orderBy, String direction) {
        long currentEpoch = dateTime.toDate().getTime();
        List<Reminder> remindersList = new ArrayList<>();
        String query = "SELECT * FROM Reminders WHERE " +
                "active = " + active.toString().toUpperCase() + " AND " +
                "start_date < " + currentEpoch + " AND " +
                "end_date > " + currentEpoch;

        if (orderBy != null) {
            query += " ORDER BY " + orderBy + " " + direction;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            remindersList.add(returnReminderFromCursor(c));
            c.moveToNext();
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

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            return (int) db.insert("Reminders", null, reminder.getContentValues());
        } catch (Exception ignored) {
            return -1;
        }
    }

    public Reminder readReminder(int id) {
        String query = "SELECT * FROM Reminders WHERE id=" + id + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Reminder reminder = returnReminderFromCursor(c);
        c.close();

        return reminder;
    }

    public void updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update("Reminders",
                reminder.getContentValues(),
                "id=?",
                new String[] { String.valueOf(reminder.getId()) });
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Reminders", "id=?",
                new String[] { String.valueOf(reminder.getId()) });
    }

    private Medication returnMedicationFromCursor(Cursor c) {
        return new Medication(
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getFloat(c.getColumnIndexOrThrow("strength")),
                c.getString(c.getColumnIndexOrThrow("strength_units")),
                c.getFloat(c.getColumnIndexOrThrow("count")),
                c.getString(c.getColumnIndexOrThrow("icon")),
                c.getString(c.getColumnIndexOrThrow("colour")),
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("refill_reminder"))),
                c.getFloat(c.getColumnIndexOrThrow("refill_count")),
                c.getLong(c.getColumnIndexOrThrow("refill_time")),
                c.getString(c.getColumnIndexOrThrow("rx_number")),
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("active"))),
                Boolean.getBoolean(c.getString(c.getColumnIndexOrThrow("visible")))
        );
    }

    private Reminder returnReminderFromCursor(Cursor c) {
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
