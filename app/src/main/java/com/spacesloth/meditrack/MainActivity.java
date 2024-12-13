/* (C) 2022 */
package com.spacesloth.meditrack;

import static com.spacesloth.meditrack.Meditrack.CRASH_DATA_INTENT_KEY_STRING;
import static com.spacesloth.meditrack.Meditrack.IS_CRASH_INTENT_KEY_STRING;

import android.content.Intent;
import android.graphics.Canvas;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity
        implements Reminder.ReminderListener,
        MedicationsEditActivity.MedicationUpdateListener,
        View.OnCreateContextMenuListener {

    private final DatabaseHelper db = new DatabaseHelper(this);
    private final Toasts toasts = new Toasts(this);
    public List<Reminder> reminders;

    public static int backPresses = 0;

    final Dialogs dialogs = new Dialogs(this);

    RecyclerView recyclerView;

    TextView tvTodaysReminders, tvNoReminders;
    ImageView imCapsule;
    MainRecyclerViewAdapter recyclerViewAdapter;
    Button settingsButton, aboutButton, fab;

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

//        DatabaseHelper db = new DatabaseHelper(this);
//        db.deleteDatabase();
//        db.loadTestData();

        loadRemindersFromDatabase();
        setContentViewAndDesign();
        findViewsByIds();
        createRecyclerView();
        makeRecyclerViewItemsSwipable();
        initiateButtons();
        isSqlDatabaseEmpty();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean newReminderAdded = intent.hasExtra("new_reminder_id");
        int newReminderId = intent.getIntExtra("new_reminder_id", -1);
        Reminder newReminder = Reminder.getById(this, newReminderId);

        if(newReminderAdded && newReminderId != -1) {
            reminders.add(newReminder);
            recyclerViewAdapter.reminders.add(newReminder);
            recyclerViewAdapter.notifyItemInserted(reminders.size() - 1);
        }
        onNotificationClicked(intent);
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

                        Reminder reminder = reminders.get(viewHolder.getAbsoluteAdapterPosition());

                        switch (direction) {
                            case ItemTouchHelper.RIGHT:
                                recyclerViewAdapter.notifyItemChanged(position);
//                                dialogs.getPillDeletionDialog(medication, position).show();
                                break;
                            case ItemTouchHelper.LEFT:
                                recyclerViewAdapter.notifyItemChanged(position);
                                openEditReminder(reminder);
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
        tvTodaysReminders = findViewById(R.id.tv_todays_reminders);
        imCapsule = findViewById(R.id.img_capsule);
        tvNoReminders = findViewById(R.id.tv_no_reminders);
        if (reminders.isEmpty()) {
            tvTodaysReminders.setVisibility(View.INVISIBLE);
            imCapsule.setVisibility(View.VISIBLE);
            tvNoReminders.setVisibility(View.VISIBLE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String todaysReminders = new SimpleDateFormat("EE, MMM d, yyyy").format(Calendar.getInstance().getTime());
                tvTodaysReminders.setText(todaysReminders);
            }
        }
    }

    private void initiateButtons() {
        settingsButton.setOnClickListener(v -> openSettings());
        aboutButton.setOnClickListener(v -> openAbout());
        fab.setOnCreateContextMenuListener(this);
//        fab.setOnClickListener(v -> openEditReminder(null));
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        menu.add(-1, 0, 0, "Add Medication");
        menu.add(-1, 1, 1, "Add Reminder");
    }

    private void loadRemindersFromDatabase() {
        reminders = db.getRemindersByDateTime(true, DateTime.now(), "time", "ASC");
    }

    private void createRecyclerView() {
        recyclerViewAdapter = new MainRecyclerViewAdapter(MainActivity.this, getParent(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public boolean onContextItemSelected(MenuItem item) {
        Reminder reminder = null;

        if (item.getGroupId() >= 0) {
            reminder = reminders.get(item.getGroupId());
        }

        switch (item.getItemId()) {
            case 0:
                openEditMedication(null);
                break;
            case 1:
                openEditReminder(reminder);
                break;
//            case 2:
//                dialogs.getPillDeletionDialog(medication, item.getGroupId() - 1).show();
//                db.deleteReminder(reminder);
//                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    void isSqlDatabaseEmpty() {
        if (db.getMedsCount(true, true) == 0) {
            recyclerView.setVisibility(View.GONE);
        }
    }

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

    private void openEditReminder(Reminder reminder) {
        Intent intent = new Intent(this, RemindersEditActivity.class);
        if (reminder != null) intent.putExtra("id", reminder.getId());
        startActivity(intent);
        backPresses = 0;
    }

    private void openEditMedication(Medication med) {
        Intent intent = new Intent(this, MedicationsEditActivity.class);
        if (med != null) intent.putExtra("id", med.getId());
        startActivity(intent);
        backPresses = 0;
    }

    private void openSettings() {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
        backPresses = 0;
    }

    private void openAbout() {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        backPresses = 0;
    }

    public void closeApp() {
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

    @Override
    public void notifyAddedReminder(Reminder reminder) {
        this.reminders.add(reminder);
        recyclerViewAdapter.reminders.add(reminder);
        recyclerViewAdapter.notifyItemInserted(this.reminders.size() - 1);
    }

    @Override
    public void notifyDeletedReminder(Reminder reminder, int position) {
        this.reminders.remove(reminder);
        recyclerViewAdapter.reminders.remove(reminder);
        recyclerViewAdapter.notifyItemRemoved(position);
        recyclerViewAdapter.notifyItemRangeChanged(position, this.reminders.size());
    }

    @Override
    public void notifyResetReminder(int position) {
        recyclerViewAdapter.notifyItemChanged(position);
    }

    @Override
    public void notifyMedicationChange() {
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
