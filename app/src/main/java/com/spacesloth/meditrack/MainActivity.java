/* (C) 2022 */
package com.spacesloth.meditrack;

import static com.spacesloth.meditrack.Meditrack.CRASH_DATA_INTENT_KEY_STRING;
import static com.spacesloth.meditrack.Meditrack.IS_CRASH_INTENT_KEY_STRING;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements Medication.PillListener {

    private final DatabaseHelper db = new DatabaseHelper(this);
    private final ArrayHelper arrayHelper = new ArrayHelper();
    private final Toasts toasts = new Toasts(this);
    public List<Medication> medications;

    public static int backPresses = 0;

    final Dialogs dialogs = new Dialogs(this);

    RecyclerView recyclerView;

    MainRecyclerViewAdapter myAdapter;
    Button settingsButton, aboutButton, fab;
    ImageView imgPill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        View windowDecorView = this.getWindow().getDecorView();
        windowDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        Intent intent = getIntent();
        if (intent.hasExtra(IS_CRASH_INTENT_KEY_STRING)
                && intent.getBooleanExtra(IS_CRASH_INTENT_KEY_STRING, true)
                && intent.hasExtra(CRASH_DATA_INTENT_KEY_STRING)) {
            dialogs.getCrashDialog(intent.getStringExtra(CRASH_DATA_INTENT_KEY_STRING)).show();
        }
        loadMedicationsFromDatabase();
        setContentViewAndDesign();
        findViewsByIds();
        createRecyclerView();
        makeRecyclerViewItemsSwipable();
        initiateButtons();
//        isSqlDatabaseEmpty();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        boolean newPillAdded = intent.hasExtra(NEW_PILL_INTENT_KEY);
//        int pk = intent.getIntExtra(NEW_PILL_INTENT_KEY, -1);
//
//        if(newPillAdded && pk != -1) {
//            Medication[] newMedicationArray = arrayHelper.addPillToPillArray(medications, myDatabase.getPill(intent.getIntExtra(NEW_PILL_INTENT_KEY, -1)));
//            medications = newMedicationArray;
//            myAdapter.medications = newMedicationArray;
//            myAdapter.notifyItemInserted(newMedicationArray.length - 1);
//        }
//        onNotificationClicked(intent);
    }

    private void onNotificationClicked(Intent intent) {
//        if (intent.hasExtra(PILL_TAKEN_VIA_NOTIFICATION_INTENT_KEY)) {
//            int pk = intent.getIntExtra(PILL_TAKEN_VIA_NOTIFICATION_INTENT_KEY, -1);
//            ArrayHelper arrayHelper = new ArrayHelper();
//            int position = arrayHelper.findPillUsingPrimaryKey(medications, pk);
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            System.out.println("Getting position = " + position);
//            if (layoutManager != null) {
//                View pillView = layoutManager.findViewByPosition(position);
//                if (pillView != null) {
//                    pillView.startAnimation(
//                            AnimationUtils.loadAnimation(this, R.anim.recycler_item_enlarge));
//                }
//            }
//        }
    }

    private void makeRecyclerViewItemsSwipable() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getLayoutPosition();

                        Medication medication = medications.get(viewHolder.getAbsoluteAdapterPosition());

                        switch (direction) {
                            case ItemTouchHelper.RIGHT:
                                myAdapter.notifyItemChanged(position);
//                                dialogs.getPillDeletionDialog(medication, position).show();
                                break;
                            case ItemTouchHelper.LEFT:
                                myAdapter.notifyItemChanged(position);
                                openUpdateMedication(medication);
                                break;
                        }
                    }

                    @Override
                    public void onChildDraw(
                            @NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {

                        new RecyclerViewSwipeDecorator.Builder(
                                        c,
                                        recyclerView,
                                        viewHolder,
                                        dX * 0.85f,
                                        dY * 0.85f,
                                        actionState,
                                        isCurrentlyActive)
                                .addSwipeRightActionIcon(R.drawable.svg_delete)
                                .addSwipeLeftActionIcon(R.drawable.svg_pencil)
                                .create()
                                .decorate();

                        super.onChildDraw(
                                c,
                                recyclerView,
                                viewHolder,
                                dX * 0.85f,
                                dY * 0.85f,
                                actionState,
                                isCurrentlyActive);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

//    private void checkOpenCount() {
//        int count = sharedPrefs.getOpenCountPref();
//        if (count == 0) {
//            dialogs.getWelcomeDialog().show();
//        } else {
//            if (count % 150 == 0) dialogs.getDonationDialog().show();
//        }
//        count = count + 1;
//        sharedPrefs.setOpenCountPref(count);
//    }

    private void setContentViewAndDesign() {
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_main);
    }

    private void findViewsByIds() {
        settingsButton = findViewById(R.id.btn_settings);
        aboutButton = findViewById(R.id.btn_about);
        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initiateButtons() {
        settingsButton.setOnClickListener(v -> openSettingsActivity());
        aboutButton.setOnClickListener(v -> openAboutActivity());
        fab.setOnClickListener(v -> openCreateMedicationActivity());
    }

    private void loadMedicationsFromDatabase() {
        medications = db.getAllMedications(true, true);
//        medications = new ArrayList<>();
    }

    private void createRecyclerView() {
        myAdapter = new MainRecyclerViewAdapter(MainActivity.this, getParent(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    public boolean onContextItemSelected(MenuItem item) {
        Medication medication = medications.get(item.getGroupId() - 1);

        switch (item.getItemId()) {
            case 1:
                openUpdateMedication(medication);
                break;
            case 2:
//                dialogs.getPillDeletionDialog(medication, item.getGroupId() - 1).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
//
//    void isSqlDatabaseEmpty() {
//        if (db.getMedsCount(true, true) == 0) {
//            recyclerView.setVisibility(View.GONE);
//            fab.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onBackPressed() {
        backPresses++;

        switch (backPresses) {
            case 1:
                toasts.showCustomToast(this.getString(R.string.press_back_again_toast));
                break;
            case 2:
                closeApp();
                backPresses = 0;
                break;
        }
    }

    private void openUpdateMedication(Medication medication) {
        Intent intent = new Intent(this, MedicationsEditActivity.class);
        intent.putExtra("id", medication.getId());
        startActivity(intent);
        backPresses = 0;
    }

    private void openCreateMedicationActivity() {
        Intent intent = new Intent(this, MedicationsEditActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
        backPresses = 0;
    }

    private void openAboutActivity() {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        backPresses = 0;
    }

    public void closeApp() {
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

    @Override
    public void notifyAddedPill(Medication medication) {
//        Medication[] newMedicationArray = arrayHelper.addMedicationToArray(myAdapter.medications, medication);
//        this.medications = newMedicationArray;
//        myAdapter.medications = newMedicationArray;
//        myAdapter.notifyItemInserted(newMedicationArray.length - 1);
    }

    @Override
    public void notifyDeletedPill(Medication medication, int position) {
//        Medication[] newMedicationArray = arrayHelper.deleteMedicationFromArray(myAdapter.medications, medication);
//        this.medications = newMedicationArray;
//        myAdapter.medications = newMedicationArray;
//        myAdapter.notifyItemRemoved(position);
//        myAdapter.notifyItemRangeChanged(position, newMedicationArray.length);
    }

    @Override
    public void notifyResetPill(int position) {
        myAdapter.notifyItemChanged(position);
    }
}

android.lock
android/
caches/
daemon/
img/logo.svg
jdks/
native/
wrapper/
