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

public class RemindersRecyclerViewAdapter
        extends RecyclerView.Adapter<RemindersRecyclerViewAdapter.MyViewHolder> {

    SharedPrefs sharedPrefs;
    Dialogs dialogs;
    DatabaseHelper db;
    AudioHelper audioHelper;
    Toasts toasts;

    final Context context;
    final RemindersActivity remindersActivity;
    Activity myActivity;

    Reminder[] reminders;

    RemindersRecyclerViewAdapter(RemindersActivity remindersActivity, Activity myActivity, Context context) {
        this.myActivity = myActivity;
        this.remindersActivity = remindersActivity;
        this.reminders = remindersActivity.reminders;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        final ConstraintLayout constraintLayout;
        final TextView tvMedicationName;
        final ImageButton btnTake;
        final ImageButton btnReset;
        final Button btnReminderItem;
        final ImageView imgCapsule;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout =
                    itemView.findViewById(R.id.reminder_item);
            tvMedicationName = itemView.findViewById(R.id.tvMedicationName);
            btnTake = itemView.findViewById(R.id.btn_take);
            btnReset = itemView.findViewById(R.id.btn_reset);
            imgCapsule = itemView.findViewById(R.id.img_capsule);
            btnReminderItem = itemView.findViewById(R.id.bigButton);

            imgCapsule.setOnCreateContextMenuListener(this);
            btnReminderItem.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(
                ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(this.getAbsoluteAdapterPosition() + 1, 1, 0, R.string.context_menu_update);
            menu.add(this.getAbsoluteAdapterPosition() + 1, 2, 0, R.string.context_menu_delete);
        }
    }

    @Override
    public int getItemCount() {
        return reminders.length;
    }

    @NonNull @Override
    public RemindersRecyclerViewAdapter.MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reminder currentReminder = reminders[position];
        checkForNotificationOpenedOnAppStart(holder, position);
        initAll(holder, currentReminder, position);
    }

    private void checkForNotificationOpenedOnAppStart(MyViewHolder holder, int position) {
        Intent remindersActivityIntent = remindersActivity.getIntent();
//        if (remindersActivityIntent.hasExtra("pill_taken_notification_id")) {
//            int id = remindersActivityIntent.getIntExtra("pill_taken_notification_id", -1);
//            ArrayHelper arrayHelper = new ArrayHelper();
//            int pillPosition = arrayHelper.findReminderById(reminders, id);
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
        myActivity = new Activity();
        dialogs = new Dialogs(context);
        db = new DatabaseHelper(context);
//        sharedPrefs = new SharedPrefs(context);
//        audioHelper = new AudioHelper(context);
        toasts = new Toasts(context);
    }

    private void initTextViews(MyViewHolder holder, Reminder reminder) {
//        holder.tvMedicationName.setTextSize(27.0f);
//        holder.tvReminderTime.setTextSize(15.0f);

//        holder.pillNameTextView.setText(medication.getName());

//        if (medication.getTaken() == PILL_TAKEN_VALUE) {
//            String takenTime = context.getString(R.string.taken_at, medication.getTimeTaken());
//            holder.pillTimeTextView.setText(takenTime);
//            holder.takenBtn.setVisibility(View.INVISIBLE);
//            holder.takenBtn.setClickable(false);
//            holder.resetBtn.setVisibility(View.VISIBLE);
//            holder.resetBtn.setClickable(true);
//        } else {
//            holder.pillTimeTextView.setText(times);
//            holder.resetBtn.setVisibility(View.INVISIBLE);
//            holder.resetBtn.setClickable(false);
//            holder.takenBtn.setVisibility(View.VISIBLE);
//            holder.takenBtn.setClickable(true);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.tvMedicationName.setContextClickable(true);
        }
    }

    private void initButtons(MyViewHolder holder, Reminder reminder, int position) {
        holder.btnTake.setOnClickListener(
            v -> {
                reminder.fulfill(context);

                holder.btnTake.setVisibility(View.INVISIBLE);
                holder.btnTake.setClickable(false);
                holder.btnReset.setVisibility(View.VISIBLE);
                holder.btnReset.setClickable(true);
                toasts.showCustomToast(
                    context.getString(R.string.pill_taken_toast, Medication.getById(this.context, reminder.getMedicationId())));
            });

        holder.btnReminderItem.setOnClickListener(
                view -> {
//                    Intent intent = new Intent(context, RemindersEditActivity.class);
//                    intent.putExtra("id", reminder.getId());
//                    context.startActivity(intent);
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.btnReminderItem.setContextClickable(true);
        }
    }
}
