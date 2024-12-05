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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
    Activity myActivity;

    List<Medication> medications;

    MainRecyclerViewAdapter(MainActivity mainActivity, Activity myActivity, Context context) {
        this.myActivity = myActivity;
        this.mainActivity = mainActivity;
        this.medications = mainActivity.medications;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        final ConstraintLayout constraintLayout;
        final TextView pillTimeTextView;
        final TextView pillNameTextView;
        final ImageButton takenBtn;
        final ImageButton resetBtn;
        final Button bigButton;
        final ImageView pillBottleImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout =
                    itemView.findViewById(R.id.reminder_item);
            pillNameTextView = itemView.findViewById(R.id.tvMedicationName);
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
            menu.add(
                    this.getAbsoluteAdapterPosition() + 1,
                    3,
                    0,
                    R.string.context_menu_change_color);
        }
    }

    @Override
    public int getItemCount() {
        return medications.size();
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
        Medication currentMedication = medications.get(position);
        checkForNotificationOpenedOnAppStart(holder, position);
        initAll(holder, currentMedication, position);
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

    private void initAll(MyViewHolder holder, Medication medication, int position) {

        initClasses();
        initTextViews(holder, medication);
        initButtons(holder, medication, position);
    }

    private void initClasses() {
        myActivity = new Activity();
        dialogs = new Dialogs(context);
        myDatabase = new DatabaseHelper(context);
        sharedPrefs = new SharedPrefs(context);
        audioHelper = new AudioHelper(context);
        toasts = new Toasts(context);
    }

    private void initTextViews(MyViewHolder holder, Medication medication) {
        holder.pillNameTextView.setTextSize(27.0f);
        holder.pillTimeTextView.setTextSize(15.0f);

        holder.pillNameTextView.setText(medication.getName());

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
            holder.pillNameTextView.setContextClickable(true);
            holder.pillTimeTextView.setContextClickable(true);
        }
    }

    private void initButtons(MyViewHolder holder, Medication medication, int position) {
//        holder.takenBtn.setOnClickListener(
//                v -> {
//                    medication.takePill(context);
//                    String time = medication.getTimeTaken();
//                    String takenAtTime = context.getString(R.string.taken_at, time);
//
//                    holder.pillTimeTextView.setText(takenAtTime);
//                    holder.takenBtn.setVisibility(View.INVISIBLE);
//                    holder.takenBtn.setClickable(false);
//                    holder.resetBtn.setVisibility(View.VISIBLE);
//                    holder.resetBtn.setClickable(true);
//                    if (sharedPrefs.getPillSoundPref()) audioHelper.getTakenPlayer().start();
//                    toasts.showCustomToast(
//                            context.getString(R.string.pill_taken_toast, medication.getName()));
//                });

        holder.bigButton.setOnClickListener(
                view -> {
                    Intent intent = new Intent(context, MedicationsEditActivity.class);
                    intent.putExtra("pill_taken_id", medication.getId());
                    context.startActivity(intent);
                    MainActivity.backPresses = 0;
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.bigButton.setContextClickable(true);
        }
    }
}
