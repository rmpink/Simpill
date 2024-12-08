/* (C) 2022 */
package com.spacesloth.meditrack;

import static com.spacesloth.meditrack.Meditrack.CRASH_DATA_INTENT_KEY_STRING;
import static com.spacesloth.meditrack.Meditrack.IS_CRASH_INTENT_KEY_STRING;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RemindersActivity extends AppCompatActivity implements Reminder.ReminderListener {

    private final DatabaseHelper db = new DatabaseHelper(this);
//    private final Toasts toasts = new Toasts(this);
    public Reminder[] reminders;

    public static int backPresses = 0;

    final Dialogs dialogs = new Dialogs(this);

    RecyclerView recyclerView;
    RemindersRecyclerViewAdapter myAdapter;
    Button btnBack, fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(IS_CRASH_INTENT_KEY_STRING)
                && intent.getBooleanExtra(IS_CRASH_INTENT_KEY_STRING, true)
                && intent.hasExtra(CRASH_DATA_INTENT_KEY_STRING)) {
            dialogs.getCrashDialog(intent.getStringExtra(CRASH_DATA_INTENT_KEY_STRING)).show();
        }
        loadRemindersFromDatabase();
        setContentViewAndDesign();
        findViewsByIds();
        createRecyclerView();
        makeRecyclerViewItemsSwipable();
        initiateButtons();
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

                        Reminder reminder = reminders[viewHolder.getAbsoluteAdapterPosition()];

                        switch (direction) {
                            case ItemTouchHelper.RIGHT:
                                myAdapter.notifyItemChanged(position);
//                                dialogs.getPillDeletionDialog(reminder, position).show();
                                break;
                            case ItemTouchHelper.LEFT:
                                myAdapter.notifyItemChanged(position);
//                                openUpdateReminder(reminder);
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
        btnBack = findViewById(R.id.btn_back);
        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initiateButtons() {
        btnBack.setOnClickListener(v -> finish());
//        fab.setOnClickListener(v -> openActivityNewReminder());
    }

    private void loadRemindersFromDatabase() {
        reminders = db.getAllReminders(true, null, null).toArray(new Reminder[0]);
    }

    private void createRecyclerView() {
        myAdapter = new RemindersRecyclerViewAdapter(RemindersActivity.this, getParent(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    public boolean onContextItemSelected(MenuItem item) {
        Reminder reminder = reminders[item.getGroupId() - 1];

        switch (item.getItemId()) {
            case 1:
//                openUpdateReminder(reminder);
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

//    private void openUpdateReminder(Reminder reminder) {
//        Intent intent = new Intent(this, RemindersEditActivity.class);
//        intent.putExtra("id", reminder.getId());
//        startActivity(intent);
//        backPresses = 0;
//    }
//
//    private void openActivityNewReminder() {
//        Intent intent = new Intent(this, RemindersEditActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void notifyAddedReminder(Reminder reminder) {
//        Reminder[] newReminderArray = arrayHelper.addReminderToArray(myAdapter.reminders, reminder);
//        this.reminders = newReminderArray;
//        myAdapter.reminders = newReminderArray;
//        myAdapter.notifyItemInserted(newReminderArray.length - 1);
    }

    @Override
    public void notifyDeletedReminder(Reminder reminder, int position) {
//        Reminder[] newReminderArray = arrayHelper.deleteMedicationFromArray(myAdapter.reminders, reminder);
//        this.reminders = newReminderArray;
//        myAdapter.reminders = newReminderArray;
//        myAdapter.notifyItemRemoved(position);
//        myAdapter.notifyItemRangeChanged(position, newReminderArray.length);
    }

    @Override
    public void notifyResetReminder(int position) {
        myAdapter.notifyItemChanged(position);
    }
}
