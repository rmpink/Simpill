/* (C) 2022 */
package com.spacesloth.meditrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        final TextView tvTimeHeader, tvTakeAll, tvUntakeAll, pillTakeAmountTextView, pillNameTextView;
        final ImageButton takeBtn, resetBtn;
        final Button bigButton;
        final ImageView imgBackground, medLookImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.reminder_item);
            tvTimeHeader = itemView.findViewById(R.id.tvTimeHeader);
            tvTakeAll = itemView.findViewById(R.id.tv_take_all);
            tvUntakeAll = itemView.findViewById(R.id.tv_untake_all);
            pillNameTextView = itemView.findViewById(R.id.tvMedicationName);
            pillTakeAmountTextView = itemView.findViewById(R.id.tvReminderTakeAmount);
            takeBtn = itemView.findViewById(R.id.btn_take);
            resetBtn = itemView.findViewById(R.id.btn_reset);
            medLookImage = itemView.findViewById(R.id.img_capsule);
            bigButton = itemView.findViewById(R.id.bigButton);
            imgBackground = itemView.findViewById(R.id.background);
            medLookImage.setOnCreateContextMenuListener(this);
            bigButton.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(
                ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int pos = this.getAbsoluteAdapterPosition();
            menu.add(pos, 1, 0, R.string.context_menu_update);
            menu.add(pos, 2, 0, R.string.context_menu_delete);
            menu.add(pos, 3, 0, R.string.context_menu_change_color);
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
//        Intent mainActivityIntent = mainActivity.getIntent();
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
        initTextViews(holder, reminder, position);
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

    private void initTextViews(MyViewHolder holder, Reminder reminder, int position) {
        Medication medication = Medication.getById(context, reminder.getMedicationId());
        holder.pillNameTextView.setText(medication.getName() + " (" + medication.getStrength() + medication.getStrengthUnits() + ")");
        holder.pillTakeAmountTextView.setText("Take " + reminder.getTakeAmount());

        // CHANGE Medication Icon back to an INT, i.e. Medication.getIconId()
        Resources res = context.getResources();
        int lookId = res.getIdentifier(medication.getIcon(), "drawable", context.getPackageName());
        holder.medLookImage.setImageResource(lookId);
        int c = medication.getColour();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            holder.medLookImage.setColorFilter(new BlendModeColorFilter(c, BlendMode.MODULATE));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.medLookImage.setColorFilter(c, PorterDuff.Mode.MULTIPLY);
        }

        // TODO: STILL NEEDS TO CHECK TO SEE IF SHIT HAS ALREADY BEEN TAKEN AND ADJUST ACCORDINGLY

        // SHOW/HIDE TIME TO TAKE MEDICATION DEPENDING ON IF THE PREVIOUS ONE IS THE SAME
        // TODO: This is still buggy as well
        if (position > 0 && reminder.getTime() == reminders.get(position-1).getTime()) {
            holder.tvTimeHeader.setVisibility(View.GONE);
            holder.tvTakeAll.setVisibility(View.GONE);
            holder.tvUntakeAll.setVisibility(View.GONE);
        } else {
            int hour = (int) (reminder.getTime() / 60);
            int minute = (int) (reminder.getTime() % 60);
            holder.tvTimeHeader.setText(String.format("%02d:%02d", hour, minute));
        }

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
        }
    }

    private void initButtons(MyViewHolder holder, Reminder reminder, int position) {
        holder.takeBtn.setOnClickListener(
                v -> {
                    holder.takeBtn.setVisibility(View.INVISIBLE);
                    holder.takeBtn.setClickable(false);
                    holder.resetBtn.setVisibility(View.VISIBLE);
                    holder.resetBtn.setClickable(true);

                    holder.imgBackground.setImageResource(R.color.ij_teal);

                    Medication medication = Medication.getById(context, reminder.getMedicationId());
                    medication.setCount(medication.getCount() - reminder.getTakeAmount());
                    medication.updateDB(context);
                    String currentTime = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                    }
                    holder.pillTakeAmountTextView.setText("You took " + reminder.getTakeAmount() + " at " + currentTime);

                    toasts.showCustomToast(
                            context.getString(R.string.pill_taken_toast, medication.getName()));
                });

        holder.resetBtn.setOnClickListener(
                v -> {
                    holder.takeBtn.setVisibility(View.VISIBLE);
                    holder.takeBtn.setClickable(true);
                    holder.resetBtn.setVisibility(View.INVISIBLE);
                    holder.resetBtn.setClickable(false);

                    holder.imgBackground.setImageResource(R.color.black);

                    Medication medication = Medication.getById(context, reminder.getMedicationId());
                    medication.setCount(medication.getCount() + reminder.getTakeAmount());
                    medication.updateDB(context);
                    holder.pillTakeAmountTextView.setText("Take " + reminder.getTakeAmount());

                    toasts.showCustomToast(
                            context.getString(R.string.pill_reset_toast, medication.getName()));
                });

        holder.tvTakeAll.setOnClickListener(
                v -> {
//                    long time = reminder.getTime();
                    // TAKE ALL MEDS FOR REMINDERS THAT MATCH time FOR TODAY
                    holder.tvTakeAll.setVisibility(View.GONE);
                    holder.tvUntakeAll.setVisibility(View.VISIBLE);
                });

        holder.tvUntakeAll.setOnClickListener(
                v -> {
//                    long time = reminder.getTime();
                    // UNTAKE ALL MEDS FOR REMINDERS THAT MATCH time FOR TODAY
                    holder.tvTakeAll.setVisibility(View.VISIBLE);
                    holder.tvUntakeAll.setVisibility(View.GONE);
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
