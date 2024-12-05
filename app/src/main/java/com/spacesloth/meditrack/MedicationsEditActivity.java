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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

public class MedicationsEditActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    Button btnBack, btnAddMedication;
    ImageView imgPill, imgMedLook, imgBlisterPack, imgPillBottle, imgCalendar;
    TextView tvMedicationName, tvMedicationLook, tvMedicationLookDetails, tvStrength, tvCount,
            tvRefill, tvRefillDetails;
    EditText etMedication, etStrength, etCount;
    Spinner spnUnits;

    Medication med = new Medication();

//    final DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_new_medication);

        findViewsByIds();
        initiateButtons();
        initiateUnitsSpinner();
        getAndSetIntentData();
        setAddButtonEnabledState();
    }

    private void initiateUnitsSpinner() {
        spnUnits.setOnItemSelectedListener(this);
    }

    private void getAndSetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            med = Medication.getById(this, intent.getIntExtra("id", -1));
            etMedication.setText(med.getName());
            etStrength.setText(String.valueOf(med.getStrength()));
            ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spnUnits.getAdapter();
            spnUnits.setSelection(spinnerAdapter.getPosition(med.getStrengthUnits()));
            etCount.setText(String.valueOf(med.getCount()));

            if (med.isRefillReminder()) {
                int hour = (int) (med.getRefillTime() / 60);
                int minute = (int) (med.getRefillCount() % 60);
                @SuppressLint({"NewApi", "LocalSuppress"})
                    LocalTime time = LocalTime.of(hour, minute);

                String reminderText = "Remind me to refill this medication when I have " +
                        med.getRefillCount() + " left.\nReminder time: " + time.toString();

                tvRefillDetails.setText(reminderText);
                btnAddMedication.setText("Update");
            }

            btnAddMedication.setOnClickListener(v -> updateMedication());
        }
    }

    private void findViewsByIds() {
        btnBack = findViewById(R.id.btn_back);

        imgPill = findViewById(R.id.img_capsule);
        tvMedicationName = findViewById(R.id.tv_medication_name);
        etMedication = findViewById(R.id.et_medication);
        imgPill.setOnClickListener(v -> focusOn(etMedication));
        tvMedicationName.setOnClickListener(v -> focusOn(etMedication));
        etMedication.setOnFocusChangeListener(this);

//        imgMedLook = findViewById(R.id.img_med_look);
//        tvMedicationLook = findViewById(R.id.tv_medication_look);
//        tvMedicationLookDetails = findViewById(R.id.tv_look_details);
        // TODO

        imgBlisterPack = findViewById(R.id.img_blister_pack);
        tvStrength = findViewById(R.id.tv_strength);
        etStrength = findViewById(R.id.et_strength);
        imgBlisterPack.setOnClickListener(v -> focusOn(etStrength));
        tvStrength.setOnClickListener(v -> focusOn(etStrength));
        spnUnits = findViewById(R.id.spn_units);
        etStrength.setOnFocusChangeListener(this);

        imgPillBottle = findViewById(R.id.img_pill_bottle);
        tvCount = findViewById(R.id.tv_count);
        etCount = findViewById(R.id.et_count);
        imgPillBottle.setOnClickListener(v -> focusOn(etCount));
        tvCount.setOnClickListener(v -> focusOn(etCount));
        etCount.setOnFocusChangeListener(this);

        imgCalendar = findViewById(R.id.img_calendar);
        tvRefill = findViewById(R.id.tv_refill);
        tvRefillDetails = findViewById(R.id.tv_refill_details);

        btnAddMedication = findViewById(R.id.btn_add_medication);
    }

    private void focusOn(EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void initiateButtons() {
        btnAddMedication.setOnClickListener(v -> createMedication());
        btnBack.setOnClickListener(v -> finish());
    }

    private void createMedication() {
        Log.i("asdf", "ENTERING CREATE MEDICATION");
        String newMedName = etMedication.getText().toString().trim();
        float newMedStrength = Float.parseFloat(etStrength.getText().toString());
        String newMedStrengthUnits = spnUnits.getSelectedItem().toString();
        float newMedCount = Float.parseFloat(etCount.getText().toString());

        Medication newMed = new Medication(
                newMedName,
                newMedStrength,
                newMedStrengthUnits,
                newMedCount,
                -1,
                -1,
                false,
                0.0f,
                -1,
                "",
                true,
                true
        );

        if (newMed.createDB(this)) {
            Log.i("asdf", "New Med ID is " + newMed.getId());
            finish();
        } else {
            Log.e("asdf", "Error, something went wrong.");
        }
    }

    private void updateMedication() {
        String updatedMedName = etMedication.getText().toString().trim();
        float updatedMedStrength = Float.parseFloat(etStrength.getText().toString());
        String updatedMedStrengthUnits = spnUnits.getSelectedItem().toString();
        float updatedMedCount = Float.parseFloat(etCount.getText().toString());

        Medication newMed = new Medication(
                updatedMedName,
                updatedMedStrength,
                updatedMedStrengthUnits,
                updatedMedCount,
                -1,
                -1,
                false,
                0.0f,
                -1,
                "",
                true,
                true
        );

        newMed.updateDB(this);
        finish();
    }

    private void setAddButtonEnabledState() {
        boolean medNameEmpty = etMedication.getText().toString().trim().isEmpty();
        boolean medStrengthEmpty = etStrength.getText().toString().trim().isEmpty();
        boolean medCountEmpty = etCount.getText().toString().trim().isEmpty();

        btnAddMedication.setEnabled(!medNameEmpty && !medStrengthEmpty && !medCountEmpty);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setAddButtonEnabledState();
    }

    public interface MedicationCreationListener {
        void notifyAddedMedication(Medication medication);
    }
}
