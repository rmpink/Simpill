/* (C) 2022 */
package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

public class RemindersEditActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    Button btnBack, btnAddReminder;
    ImageView imgPill, imgBlisterPack;
    EditText etTakeAmount;
    TextView tvTime, tvMedication, tvStrength;

    Reminder reminder = new Reminder();
    Medication medication = new Medication();

//    final DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_edit_reminder);

        findViewsByIds();
        initiateButtons();
        getAndSetIntentData();
        setAddButtonEnabledState();
    }

    private void getAndSetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("reminder_taken_id")) {
            reminder = Reminder.getById(this, intent.getIntExtra("reminder_taken_id", -1));
            Medication med = Medication.getById(this, reminder.getMedicationId());
            tvMedication.setText(med.getName());
            tvStrength.setText(String.valueOf(med.getStrength()));
            etTakeAmount.setText(String.valueOf(reminder.getTakeAmount()));

            if (reminder.isActive()) {
                int hour = (int) (reminder.getTime() / 60);
                int minute = (int) (reminder.getTime() % 60);
                @SuppressLint({"NewApi", "LocalSuppress"})
                    LocalTime time = LocalTime.of(hour, minute);

                tvTime.setText("Take at " + time.toString());
            }

            btnAddReminder.setText("Update");
            btnAddReminder.setOnClickListener(v -> updateReminder());
        }
    }

    private void findViewsByIds() {
        btnBack = findViewById(R.id.btn_back);

        imgPill = findViewById(R.id.img_capsule);
        imgBlisterPack = findViewById(R.id.img_blister_pack);
        tvMedication = findViewById(R.id.tv_medication_name);
        tvStrength = findViewById(R.id.tv_strength);
        tvTime = findViewById(R.id.tv_time);
        etTakeAmount = findViewById(R.id.et_take_amount);

        imgBlisterPack.setOnClickListener(v -> focusOn(etTakeAmount));

        btnAddReminder = findViewById(R.id.btn_add_reminder);
    }

    private void focusOn(EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void initiateButtons() {
        btnAddReminder.setOnClickListener(v -> createReminder());
        btnBack.setOnClickListener(v -> finish());
    }

    private void createReminder() {
        int newMedId = medication.getId();
        float newTakeAmount = Float.parseFloat(etTakeAmount.getText().toString());
        long newReminderTime = Integer.parseInt(tvTime.getText().toString());
        long newStartTime = 0;
        long newEndTime = -1;
        byte newDaysOfWeek = (byte)0;
        int newInterval = 0;

        Reminder newReminder = new Reminder(
                true,
                newTakeAmount,
                newReminderTime,
                0,
                -1,
                (byte)0,
                0,
                newMedId
        );

        if (newReminder.createDB(this)) {
            Log.i("asdf", "New Reminder ID is " + newReminder.getId());
            finish();
        } else {
            Log.e("asdf", "Error, something went wrong.");
        }
    }

    private void updateReminder() {
        int newMedId = medication.getId();
        float newTakeAmount = Float.parseFloat(etTakeAmount.getText().toString());
        long newReminderTime = Integer.parseInt(tvTime.getText().toString());
        long newStartTime = 0;
        long newEndTime = -1;
        byte newDaysOfWeek = (byte)0;
        int newInterval = 0;

        Reminder newReminder = new Reminder(
                true,
                newTakeAmount,
                newReminderTime,
                0,
                -1,
                (byte)0,
                0,
                newMedId
        );

        newReminder.updateDB(this);
        finish();
    }

    private void setAddButtonEnabledState() {
        boolean timeEmpty = tvTime.getText().toString().trim().isEmpty();
        boolean medIdInvalid = medication.getId() == -1;
        boolean takeAmountEmpty = etTakeAmount.getText().toString().trim().isEmpty();

        btnAddReminder.setEnabled(!timeEmpty && !medIdInvalid && !takeAmountEmpty);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setAddButtonEnabledState();
    }

    public interface ReminderCreationListener {
        void notifyAddedReminder(Reminder reminder);
    }
}
