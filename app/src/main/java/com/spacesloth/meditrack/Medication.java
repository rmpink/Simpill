/* (C) 2022 */
package com.spacesloth.meditrack;

import android.content.ContentValues;
import android.content.Context;

public class Medication {
    private final ContentValues contentValues = new ContentValues(12);

    private int id;
    private String name = "";
    private float strength = 0;
    private String strengthUnits = "";
    private float count = 0.0f;
    private int iconIdx = -1;
    private int colourIdx = -1;
    private boolean refillReminder = false;
    private float refillCount = 0.0f;
    private long refillTime = -1;
    private String rxNumber = "";
    private boolean active = false;
    private boolean visible = false;

    public Medication(String _name, float _strength, String _strengthUnits, float _count,
            int _iconIdx, int _colourIdx, boolean _refillReminder, float _refillCount,
            long _refillTime, String _rxNumber, boolean _active, boolean _visible) {

        name = _name;
        strength = _strength;
        strengthUnits = _strengthUnits;
        count = _count;
        iconIdx = _iconIdx;
        colourIdx = _colourIdx;
        refillReminder = _refillReminder;
        refillCount = _refillCount;
        refillTime = _refillTime;
        rxNumber = _rxNumber;
        active = _active;
        visible = _visible;

        this.setContentValues();
    }

    public Medication() {}

    public static Medication getById(Context ctx, int _id) {
        DatabaseHelper db = new DatabaseHelper(ctx);
        Medication med = db.readMedication(_id);
        med.id = _id;
        db.close();
        return med;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public float getStrength() { return strength; }
    public void setStrength(float strength) { this.strength = strength; }
    public String getStrengthUnits() { return strengthUnits; }
    public void setStrengthUnits(String strengthUnits) { this.strengthUnits = strengthUnits; }
    public float getCount() { return count; }
    public void setCount(float count) { this.count = count; }
     public int getIconIdx() { return iconIdx; }
    public void setIconIdx(int iconIdx) { this.iconIdx = iconIdx; }
    public int getColourIdx() { return colourIdx; }
    public void setColourIdx(int colourIdx) { this.colourIdx = colourIdx; }
    public boolean isRefillReminder() { return refillReminder; }
    public void setRefillReminder(boolean refillReminder) { this.refillReminder = refillReminder; }
    public float getRefillCount() { return refillCount; }
    public void setRefillCount(float refillCount) { this.refillCount = refillCount; }
    public long getRefillTime() { return refillTime; }
    public void setRefillTime(long refillTime) { this.refillTime = refillTime; }
    public String getRxNumber() { return rxNumber; }
    public void setRxNumber(String rxNumber) { this.rxNumber = rxNumber; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }


    // Separate out Context and DatabaseHelper from this class completely.
    public Boolean createDB(Context context) {
        setContentValues();
        try (DatabaseHelper db = new DatabaseHelper(context)) {
            this.id = db.createMedication(this);
        }
        return this.id >= 0;
    }

    public void updateDB(Context context) {
        setContentValues();
        try (DatabaseHelper db = new DatabaseHelper(context)) {
            db.updateMedication(this);
        }
    }

    public void deleteDB(Context context, int recyclerViewPosition) {
        try (DatabaseHelper db = new DatabaseHelper(context)) {
            db.deleteMedication(this);
        }

        MedicationListener medicationListener = (MedicationListener) context;
        medicationListener.notifyDeletedMedication(this, recyclerViewPosition);
    }

    public ContentValues getContentValues() {
        return contentValues;
    }

    public void setContentValues() {
        contentValues.put("name", name);
        contentValues.put("strength", strength);
        contentValues.put("strength_units", strengthUnits);
        contentValues.put("count", count);
        contentValues.put("icon", iconIdx);
        contentValues.put("colour", colourIdx);
        contentValues.put("refill_reminder", refillReminder);
        contentValues.put("refill_count", refillCount);
        contentValues.put("refill_time", refillTime);
        contentValues.put("rx_number", rxNumber);
        contentValues.put("active", active);
        contentValues.put("visible", visible);
    }

    public interface MedicationListener {

        void notifyAddedMedication(Medication medication);

        void notifyDeletedMedication(Medication medication, int position);

        void notifyResetMedication(int position);
    }
}
