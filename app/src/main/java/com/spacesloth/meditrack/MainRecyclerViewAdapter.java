/* (C) 2022 */
package com.spacesloth.meditrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;

public class MainRecyclerViewAdapter
        extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    SharedPrefs sharedPrefs;
    Dialogs dialogs;
    DatabaseHelper myDatabase;
    AudioHelper audioHelper;
    Toasts toasts;

    final Context context;
    final MainActivity mainActivity;
    Activity parentActivity;

    List<Reminder> reminders;

    MainRecyclerViewAdapter(MainActivity mainActivity, Activity parentActivity, Context context) {
        this.parentActivity = parentActivity;
        this.mainActivity = mainActivity;
        this.reminders = mainActivity.reminders;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        final ConstraintLayout constraintLayout;
        final TextView pillTimeTextView;
        final TextView pillTakeAmountTextView;
        final TextView pillNameTextView;
        final ImageButton takenBtn;
        final ImageButton resetBtn;
        final Button bigButton;
        final ImageView pillBottleImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.reminder_item);
            pillNameTextView = itemView.findViewById(R.id.tvMedicationName);
            pillTakeAmountTextView = itemView.findViewById(R.id.tvReminderTakeAmount);
            pillTimeTextView = itemView.findViewById(R.id.tvReminderTime);
            takenBtn = itemView.findViewById(R.id.btn_take);
            resetBtn = itemView.findViewById(R.id.btn_reset);
            pillBottleImage = itemView.findViewById(R.id.img_capsule);
            bigButton = itemView.findViewById(R.id.bigButton);
            pillBottleImage.setOnCreateContextMenuListener(this);
            bigButton.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(
                ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(this.getAbsoluteAdapterPosition() + 1, 1, 0, R.string.context_menu_update);
            menu.add(this.getAbsoluteAdapterPosition() + 1, 2, 0, R.string.context_menu_delete);
            menu.add(this.getAbsoluteAdapterPosition() + 1, 3, 0, R.string.context_menu_change_color);
        }
    }

    @Override
    public int getItemCount() {
        if (reminders != null) {
            return reminders.size();
        } else {
            return 0;
        }
    }

    @NonNull @Override
    public MainRecyclerViewAdapter.MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reminder thisReminder = reminders.get(position);
        checkForNotificationOpenedOnAppStart(holder, position);
        initAll(holder, thisReminder, position);
    }

    private void checkForNotificationOpenedOnAppStart(MyViewHolder holder, int position) {
        Intent mainActivityIntent = mainActivity.getIntent();
//        if (mainActivityIntent.hasExtra("pill_taken_notification_id")) {
//            int id = mainActivityIntent.getIntExtra("pill_taken_notification_id", -1);
//            ArrayHelper arrayHelper = new ArrayHelper();
//            int pillPosition = medications.get(id);
//            if (pillPosition == position)
//                holder.constraintLayout.startAnimation(
//                        AnimationUtils.loadAnimation(context, R.anim.recycler_item_enlarge));
//        }
    }

    private void initAll(MyViewHolder holder, Reminder reminder, int position) {

        initClasses();
        initTextViews(holder, reminder);
        initButtons(holder, reminder, position);
    }

    private void initClasses() {
        parentActivity = new Activity();
        dialogs = new Dialogs(context);
        myDatabase = new DatabaseHelper(context);
        sharedPrefs = new SharedPrefs(context);
        audioHelper = new AudioHelper(context);
        toasts = new Toasts(context);
    }

    private void initTextViews(MyViewHolder holder, Reminder reminder) {
        Medication medication = Medication.getById(context, reminder.getMedicationId());
        holder.pillNameTextView.setText(medication.getName() + " (" + medication.getStrength() + medication.getStrengthUnits() + ")");
        holder.pillTakeAmountTextView.setText("Take " + reminder.getTakeAmount());
        int hour = (int) (reminder.getTime() / 60);
        int minute = (int) (reminder.getTime() % 60);
        String takeAtString = String.format("Take at %02d:%02d", hour, minute);
        holder.pillTimeTextView.setText(takeAtString);

//        if (holder.takenBtn.getVisibility() == View.VISIBLE) {
//            String takenTime = String.format("Taken at %02d:%02d", hour, minute);
//            holder.pillTimeTextView.setText(takenTime);
//            holder.takenBtn.setVisibility(View.INVISIBLE);
//            holder.takenBtn.setClickable(false);
//            holder.resetBtn.setVisibility(View.VISIBLE);
//            holder.resetBtn.setClickable(true);
//        } else {
//            holder.pillTimeTextView.setText(String.format("Take at %02d:%02d", hour, minute));
//            holder.resetBtn.setVisibility(View.INVISIBLE);
//            holder.resetBtn.setClickable(false);
//            holder.takenBtn.setVisibility(View.VISIBLE);
//            holder.takenBtn.setClickable(true);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.pillNameTextView.setContextClickable(true);
            holder.pillTimeTextView.setContextClickable(true);
        }
    }

    private void initButtons(MyViewHolder holder, Reminder reminder, int position) {
        holder.takenBtn.setOnClickListener(
                v -> {
//                    int hour = (int) (reminder.getTime() / 60);
//                    int minute = (int) (reminder.getTime() % 60);
//                    String takenTime = String.format("Taken at %02d:%02d", hour, minute);
//                    holder.pillTimeTextView.setText(takenTime);
//                    holder.takenBtn.setVisibility(View.INVISIBLE);
//                    holder.takenBtn.setClickable(false);
//                    holder.resetBtn.setVisibility(View.VISIBLE);
//                    holder.resetBtn.setClickable(true);
//
                    Medication medication = Medication.getById(context, reminder.getMedicationId());
                    medication.setCount(medication.getCount() - reminder.getTakeAmount());
                    medication.updateDB(context);
                    toasts.showCustomToast(
                            context.getString(R.string.pill_taken_toast, medication.getName()));
                });

        holder.bigButton.setOnClickListener(
                view -> {
                    Medication medication = Medication.getById(context, reminder.getMedicationId());
                    Intent intent = new Intent(context, MedicationsEditActivity.class);
                    intent.putExtra("med_taken_id", medication.getId());
                    context.startActivity(intent);
                    MainActivity.backPresses = 0;
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.bigButton.setContextClickable(true);
        }
    }
}
