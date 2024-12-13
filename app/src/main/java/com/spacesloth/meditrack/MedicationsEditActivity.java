/* (C) 2022 */
package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MedicationsEditActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener,
        View.OnFocusChangeListener {

    Button btnBack, btnAddMedication;
    TextView tvMedicationName, tvMedicationLookDetails, tvStrength, tvCount,
            tvRefill, tvRefillDetails;
    EditText etMedication, etStrength, etCount;
    ImageView imMedLook;
    Spinner spnMedicationLook, spnStrengthUnits;

    Medication med = new Medication();
    int currentLook = 2131230890;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_edit_medication);

        findViewsByIds();
        initiateButtons();
        initiateSpinners();
        getAndSetIntentData();
        setAddButtonEnabledState();
    }

    private void initiateSpinners() {
        spnStrengthUnits.setOnItemSelectedListener(this);
        spnMedicationLook.setOnItemSelectedListener(this);
        List<Integer> medLooks = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            medLooks = Arrays.stream(getResources().getIntArray(R.array.looks_array)).boxed().collect(Collectors.toList());
        }
        ImageViewSpinnerAdapter medLookAdapter = new ImageViewSpinnerAdapter(this, medLooks, 0);
        spnMedicationLook.setAdapter(medLookAdapter);
    }

    private void getAndSetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("med_taken_id")) {
            med = Medication.getById(this, intent.getIntExtra("med_taken_id", -1));
            etMedication.setText(med.getName());
            etStrength.setText(String.valueOf(med.getStrength()));

            ArrayAdapter<String> unitsAdapter = (ArrayAdapter<String>) spnStrengthUnits.getAdapter();
            spnStrengthUnits.setSelection(unitsAdapter.getPosition(med.getStrengthUnits()));

            ImageViewSpinnerAdapter medLookAdapter = (ImageViewSpinnerAdapter) spnMedicationLook.getAdapter();
            medLookAdapter.setColour(med.getColour());
            spnMedicationLook.setSelection(medLookAdapter.getPosition(med.getIcon()));
            currentLook = med.getIcon();
            etCount.setText(String.valueOf(med.getCount()));

            if (med.isRefillReminder()) {
                int hour = (int) (med.getRefillTime() / 60);
                int minute = (int) (med.getRefillCount() % 60);
                @SuppressLint({"NewApi", "LocalSuppress"})
                    LocalTime time = LocalTime.of(hour, minute);

                String reminderText = "Remind me to refill this medication when I have " +
                        med.getRefillCount() + " left.\nReminder time: " + time.toString();

                tvRefillDetails.setText(reminderText);
            }

            btnAddMedication.setText("Update");
            btnAddMedication.setOnClickListener(v -> updateMedication());
        }
    }

    private void findViewsByIds() {
        btnBack = findViewById(R.id.btn_back);

        tvMedicationName = findViewById(R.id.tv_medication_name);
        etMedication = findViewById(R.id.et_medication);
        tvMedicationName.setOnClickListener(v -> focusOn(etMedication));
        etMedication.setOnFocusChangeListener(this);
        tvMedicationLookDetails = findViewById(R.id.tv_look_details);
        spnMedicationLook = findViewById(R.id.spn_med_look);
        imMedLook = findViewById(R.id.im_med_look);

        tvStrength = findViewById(R.id.tv_strength);
        etStrength = findViewById(R.id.et_strength);
        tvStrength.setOnClickListener(v -> focusOn(etStrength));
        spnStrengthUnits = findViewById(R.id.spn_units);
        etStrength.setOnFocusChangeListener(this);

        tvCount = findViewById(R.id.tv_count);
        etCount = findViewById(R.id.et_count);
        tvCount.setOnClickListener(v -> focusOn(etCount));
        etCount.setOnFocusChangeListener(this);

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
        imMedLook.setOnClickListener(v -> openColourPickerDialog());
        btnBack.setOnClickListener(v -> finish());
    }

    private void openColourPickerDialog() {
        ImageViewSpinnerAdapter adapter = (ImageViewSpinnerAdapter) MedicationsEditActivity.this.spnMedicationLook.getAdapter();
        int colour = adapter.getColour();
        ColourPickerDialog dialog = new ColourPickerDialog(this, colour, currentLook, new ColourPickerDialog.OnColourPickerSquareListener() {
            @Override
            public void onCancel(ColourPickerDialog dialog) {
                // nuffin
            }

            @Override
            public void onOk(ColourPickerDialog dialog, int colour) {
                ImageViewSpinnerAdapter adapter = (ImageViewSpinnerAdapter) MedicationsEditActivity.this.spnMedicationLook.getAdapter();
                adapter.setColour(colour);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void createMedication() {
        String newMedName = etMedication.getText().toString().trim();
        float newMedStrength = Float.parseFloat(etStrength.getText().toString());
        String newMedStrengthUnits = spnStrengthUnits.getSelectedItem().toString();
        float newMedCount = Float.parseFloat(etCount.getText().toString());
        ImageViewSpinnerAdapter adapter = (ImageViewSpinnerAdapter) MedicationsEditActivity.this.spnMedicationLook.getAdapter();

        Medication newMed = new Medication(
                newMedName,
                newMedStrength,
                newMedStrengthUnits,
                newMedCount,
                currentLook,
                adapter.getColour(),
                false,
                0.0f,
                -1,
                "",
                true,
                true
        );

        if (newMed.createDB(this)) {
            finish();
        }
    }

    private void updateMedication() {
        String updatedMedName = etMedication.getText().toString().trim();
        float updatedMedStrength = Float.parseFloat(etStrength.getText().toString());
        String updatedMedStrengthUnits = spnStrengthUnits.getSelectedItem().toString();
        float updatedMedCount = Float.parseFloat(etCount.getText().toString());
        ImageViewSpinnerAdapter adapter = (ImageViewSpinnerAdapter) MedicationsEditActivity.this.spnMedicationLook.getAdapter();

        Medication newMed = new Medication(
                updatedMedName,
                updatedMedStrength,
                updatedMedStrengthUnits,
                updatedMedCount,
                currentLook,
                adapter.getColour(),
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spn_med_look) {
            currentLook = (int) parent.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setAddButtonEnabledState();
    }

    public interface MedicationUpdateListener {
        void notifyMedicationChange();
    }
}
