/* (C) 2022 */
package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class Dialogs extends AppCompatDialogFragment {

    final Toasts toasts;
    final Context context;

    public Dialogs(Context context) {
        this.context = context;
        this.toasts = new Toasts(context);
    }

    private boolean isDarkMode(Context context) {
        return new SharedPrefs(context).getDarkDialogsPref();
    }
//
//    public Dialog getChooseSupplyAmountDialog(int supply) {
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_pill_amount, null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        ConstraintLayout dialogLayout =
//                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
//        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
//        ImageView pillIcon = dialogView.findViewById(R.id.imageView13);
//        Button doneBtn = dialogView.findViewById(R.id.done_btn);
//        Button addBtn = dialogView.findViewById(R.id.addBtn);
//        Button minusBtn = dialogView.findViewById(R.id.minusBtn);
//        EditText enterAmountEditText = dialogView.findViewById(R.id.calendar_btn);
//
//        if (isDarkMode(context)) {
//            dialogLayout.setBackground(
//                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
//            enterAmountEditText.setTextColor(
//                    ResourcesCompat.getColor(context.getResources(), R.color.white, null));
//            titleTextView.setBackground(
//                    AppCompatResources.getDrawable(
//                            context, R.drawable.bg_dialog_title_dark));
//            doneBtn.setBackground(
//                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
//        }
//
//        super.onAttach(context);
//        PillAmountDialogListener pillAmountDialogListener = (PillAmountDialogListener) context;
//
//        enterAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        if (supply > 0) {
//            enterAmountEditText.setText(String.valueOf(supply));
//        }
//
//        doneBtn.setOnClickListener(
//                view -> {
//                    pillAmountDialogListener.applyPillSupply(
//                            enterAmountEditText.getText().toString());
//                    dialog.dismiss();
//                });
//
//        addBtn.setOnClickListener(
//                view -> {
//                    int pillAmount;
//                    if (enterAmountEditText.getText().toString().equals("")) {
//                        pillAmount = 30;
//                    } else {
//                        pillAmount = Integer.parseInt(enterAmountEditText.getText().toString());
//                    }
//                    enterAmountEditText.setText(String.valueOf(pillAmount + 1));
//                });
//        minusBtn.setOnClickListener(
//                view -> {
//                    int pillAmount;
//                    if (enterAmountEditText.getText().toString().equals("")) {
//                        pillAmount = 30;
//                    } else {
//                        pillAmount = Integer.parseInt(enterAmountEditText.getText().toString());
//                    }
//                    if (!(pillAmount - 1 <= 0)) {
//                        enterAmountEditText.setText(String.valueOf(pillAmount - 1));
//                    }
//                });
//
//        return dialog;
//    }
//
//    public Dialog getChooseReminderAmountDialog(int amount) {
//        View dialogView =
//                LayoutInflater.from(context).inflate(R.layout.dialog_choose_reminder_amount, null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        ConstraintLayout dialogLayout =
//                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
//        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
//        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
//        Button doneBtn = dialogView.findViewById(R.id.done_btn);
//        Button addBtn = dialogView.findViewById(R.id.addBtn);
//        Button minusBtn = dialogView.findViewById(R.id.minusBtn);
//        EditText enterAmountEditText = dialogView.findViewById(R.id.calendar_btn);
//
//        if (isDarkMode(context)) {
//            int color = ResourcesCompat.getColor(context.getResources(), R.color.white, null);
//            dialogLayout.setBackground(
//                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
//            titleTextView.setBackground(
//                    AppCompatResources.getDrawable(
//                            context, R.drawable.bg_dialog_title_dark));
//            messageTextView.setTextColor(color);
//            enterAmountEditText.setTextColor(color);
//        }
//
//        enterAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        enterAmountEditText.setText(String.valueOf(amount));
//
//        doneBtn.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    getChooseTimesDialog(Integer.parseInt(enterAmountEditText.getText().toString()))
//                            .show();
//                });
//
//        addBtn.setOnClickListener(view -> {
//            int currentAmount = Integer.parseInt(enterAmountEditText.getText().toString());
//            enterAmountEditText.setText(String.valueOf(currentAmount + 1));
//        });
//        minusBtn.setOnClickListener(
//                view -> {
//                    int currentAmount = Integer.parseInt(enterAmountEditText.getText().toString());
//                    if (currentAmount - 1 > 0) {
//                        enterAmountEditText.setText(String.valueOf(currentAmount - 1));
//                    }
//                });
//
//        return dialog;
//    }
//
//    public Dialog getChooseTimesDialog(int clocks) {
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_times, null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        ConstraintLayout dialogLayout =
//                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
//        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
//        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
//
//        if (isDarkMode(context)) {
//            int color = ResourcesCompat.getColor(context.getResources(), R.color.white, null);
//            dialogLayout.setBackground(
//                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
//            titleTextView.setBackground(
//                    AppCompatResources.getDrawable(
//                            context, R.drawable.bg_dialog_title_dark));
//            messageTextView.setTextColor(color);
//        }
//
//        Button doneBtn = dialogView.findViewById(R.id.btnDone);
//
//        TimesRecyclerViewAdapter timesRecyclerViewAdapter =
//                new TimesRecyclerViewAdapter(context, clocks);
//        RecyclerView recyclerView = dialogView.findViewById(R.id.times_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemViewCacheSize(clocks + 1);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(timesRecyclerViewAdapter);
//
//        ChooseTimesDialogListener chooseTimesDialogListener = (ChooseTimesDialogListener) context;
//
//        doneBtn.setOnClickListener(
//                view -> {
//                    if (timesRecyclerViewAdapter.checkForEmptyTimes()) {
//                        toasts.showCustomToast(context.getString(R.string.time_enter));
//                    } else if (timesRecyclerViewAdapter.checkForAdjacentTimes()) {
//                        toasts.showCustomToast(context.getString(R.string.time_warning_toast));
//                    } else {
//                        dialog.dismiss();
//                        chooseTimesDialogListener.returnTimesStringArray(
//                                timesRecyclerViewAdapter
//                                        .returnTimeStringsArrayFromRecyclerViewClass());
//                        getAlarmOrNotificationDialog().show();
//                    }
//                });
//
//        return dialog;
//    }
//
//    public Dialog getFrequencyDialog() {
//        View dialogView =
//                LayoutInflater.from(context).inflate(R.layout.dialog_choose_frequency, null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        ConstraintLayout dialogLayout =
//                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
//        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
//        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
//
//        TextView multipleDailyTextView = dialogView.findViewById(R.id.multiple_daily);
//        TextView dailyTextView = dialogView.findViewById(R.id.daily);
//        TextView everyOtherDayTextView = dialogView.findViewById(R.id.every_other_day);
//        TextView weeklyTextView = dialogView.findViewById(R.id.weekly);
//        TextView customIntervalTextView = dialogView.findViewById(R.id.custom_interval);
//
//        if (isDarkMode(context)) {
//            int color = ResourcesCompat.getColor(context.getResources(), R.color.white, null);
//            dialogLayout.setBackground(
//                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
//            titleTextView.setBackground(
//                    AppCompatResources.getDrawable(
//                            context, R.drawable.bg_dialog_title_dark));
//            messageTextView.setTextColor(color);
//            dailyTextView.setTextColor(color);
//            multipleDailyTextView.setTextColor(color);
//            everyOtherDayTextView.setTextColor(color);
//            weeklyTextView.setTextColor(color);
//        }
//
//        ChooseFrequencyDialogListener chooseFrequencyDialogListener =
//                (ChooseFrequencyDialogListener) context;
//
//        multipleDailyTextView.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    chooseFrequencyDialogListener.setInterval(DatabaseHelper.MULTIPLE_DAILY);
//                    getChooseReminderAmountDialog(2).show();
//                    chooseFrequencyDialogListener.hideIntervalSubText();
//                });
//        dailyTextView.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    chooseFrequencyDialogListener.setInterval(DatabaseHelper.DAILY);
//                    chooseFrequencyDialogListener.openTimePicker();
//                    chooseFrequencyDialogListener.hideIntervalSubText();
//                });
//        everyOtherDayTextView.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    chooseFrequencyDialogListener.setInterval(DatabaseHelper.EVERY_OTHER_DAY);
//                    chooseFrequencyDialogListener.openStartDatePicker();
//                });
//        weeklyTextView.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    chooseFrequencyDialogListener.setInterval(DatabaseHelper.WEEKLY);
//                    chooseFrequencyDialogListener.openStartDatePicker();
//                });
//        customIntervalTextView.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    getCustomIntervalDialog().show();
//                });
//
//        return dialog;
//    }
//
//    public Dialog getCustomIntervalDialog() {
//        View dialogView =
//                LayoutInflater.from(context).inflate(R.layout.dialog_choose_interval, null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        ConstraintLayout dialogLayout =
//                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
//        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
//        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
//        Button doneBtn = dialogView.findViewById(R.id.done_btn);
//        Button addBtn = dialogView.findViewById(R.id.addBtn);
//        Button minusBtn = dialogView.findViewById(R.id.minusBtn);
//        EditText enterAmountEditText = dialogView.findViewById(R.id.calendar_btn);
//
//        if (isDarkMode(context)) {
//            int color = ResourcesCompat.getColor(context.getResources(), R.color.white, null);
//            dialogLayout.setBackground(
//                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
//            titleTextView.setBackground(
//                    AppCompatResources.getDrawable(
//                            context, R.drawable.bg_dialog_title_dark));
//            messageTextView.setTextColor(color);
//            enterAmountEditText.setTextColor(color);
//        }
//
//        enterAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        ChooseFrequencyDialogListener chooseFrequencyDialogListener =
//                (ChooseFrequencyDialogListener) context;
//
//        addBtn.setOnClickListener(
//                view -> {
//                    int days;
//                    if (enterAmountEditText.getText().toString().equals("")) {
//                        days = 2;
//                    } else {
//                        days = Integer.parseInt(enterAmountEditText.getText().toString()) + 1;
//                    }
//                    enterAmountEditText.setText(String.valueOf(days));
//                });
//        minusBtn.setOnClickListener(
//                view -> {
//                    int days;
//                    if (enterAmountEditText.getText().toString().equals("")) {
//                        days = 2;
//                    } else if (Integer.parseInt(enterAmountEditText.getText().toString()) > 1) {
//                        days = Integer.parseInt(enterAmountEditText.getText().toString()) - 1;
//                    } else {
//                        days = Integer.parseInt(enterAmountEditText.getText().toString());
//                    }
//                    enterAmountEditText.setText(String.valueOf(days));
//                });
//
//        doneBtn.setOnClickListener(
//                view -> {
//                    dialog.dismiss();
//                    chooseFrequencyDialogListener.setInterval(
//                            Integer.parseInt(enterAmountEditText.getText().toString()));
//                    chooseFrequencyDialogListener.openStartDatePicker();
//                });
//        return dialog;
//    }
//
//    public Dialog getStartDateDialog(Medication medication) {
//        View dialogView =
//                LayoutInflater.from(context)
//                        .inflate(
//                                isDarkMode(context)
//                                        ? R.layout.dialog_choose_start_date_dark
//                                        : R.layout.dialog_choose_start_date,
//                                null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        Button doneBtn = dialogView.findViewById(R.id.done_btn);
//
//        DateTimeManager dateTimeManager = new DateTimeManager();
//
//        GetStartDateDialogListener getStartDateDialogListener =
//                (GetStartDateDialogListener) context;
//
//        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener(
//                (view, year, month, dayOfMonth) -> {
//                    month = month + 1;
//
//                    String selectedDate = year + "/" + month + "/" + dayOfMonth;
//
//                    if (dateTimeManager.formatDateTimeStringAsLong(selectedDate + " 12:00")
//                            < System.currentTimeMillis()) {
//                        doneBtn.setOnClickListener(
//                                v -> {
//                                    getPastDateDialog().show();
//                                });
//                    } else {
//                        doneBtn.setOnClickListener(
//                                v -> {
//                                    medication.setStartDate(selectedDate);
//                                    getStartDateDialogListener.applyStartDate(selectedDate);
//                                    dialog.dismiss();
//                                });
//                    }
//                });
//
//        doneBtn.setOnClickListener(view -> dialog.dismiss());
//        return dialog;
//    }
//
//    public Dialog getStockupDateDialog(Medication medication) {
//        View dialogView =
//                LayoutInflater.from(context)
//                        .inflate(
//                                isDarkMode(context)
//                                        ? R.layout.dialog_choose_start_date_dark
//                                        : R.layout.dialog_choose_start_date,
//                                null);
//        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
//        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
//
//        titleTextView.setText(context.getString(R.string.enter_pill_refill_date_reminder));
//        messageTextView.setText(context.getString(R.string.refill_dialog_msg));
//
//        Button doneBtn = dialogView.findViewById(R.id.done_btn);
//
//        DateTimeManager dateTimeManager = new DateTimeManager();
//
//        GetStockupDateDialogListener getStockupDateDialogListener =
//                (GetStockupDateDialogListener) context;
//
//        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener(
//                (view, year, month, dayOfMonth) -> {
//                    month = month + 1;
//                    String selectedDate = year + "/" + month + "/" + dayOfMonth;
//                    if (dateTimeManager.formatDateTimeStringAsLong(selectedDate + " 12:00")
//                            < System.currentTimeMillis()) {
//                        doneBtn.setOnClickListener(
//                                v -> {
//                                    getPastDateDialog().show();
//                                });
//                    } else {
//                        doneBtn.setOnClickListener(
//                                v -> {
//                                    medication.setStockupDate(selectedDate);
//                                    getStockupDateDialogListener.applyStockup(selectedDate);
//                                    dialog.dismiss();
//                                });
//                    }
//                });
//
//        doneBtn.setOnClickListener(view -> dialog.dismiss());
//        return dialog;
//    }

    @SuppressLint("InlinedApi")
    public Dialog getPermissionOverlayDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ConstraintLayout dialogLayout =
                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
        Button rightButton = dialogView.findViewById(R.id.btnYes);
        Button leftButton = dialogView.findViewById(R.id.btnNo);

        if (isDarkMode(context)) {
            dialogLayout.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
            titleTextView.setBackground(
                    AppCompatResources.getDrawable(
                            context, R.drawable.bg_dialog_title_dark));
            messageTextView.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        } else {
            titleTextView.setBackground(
                    AppCompatResources.getDrawable(
                            context, R.drawable.bg_dialog_title_dark));
        }
        rightButton.setBackground(
                AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
        leftButton.setBackground(
                AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));

        titleTextView.setText(context.getString(R.string.ask_overlay_permission_dialog_title));
        messageTextView.setText(context.getString(R.string.ask_overlay_permission_dialog_message));
        leftButton.setText(context.getString(R.string.dismiss));
        rightButton.setText(context.getString(R.string.settings));

        rightButton.setOnClickListener(
                v -> {
                    context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                    dialog.dismiss();
                });
        leftButton.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }

    public Dialog getAlarmOrNotificationDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ConstraintLayout dialogLayout =
                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
        Button rightButton = dialogView.findViewById(R.id.btnYes);
        Button leftButton = dialogView.findViewById(R.id.btnNo);

        if (isDarkMode(context)) {
            dialogLayout.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
            titleTextView.setBackground(
                    AppCompatResources.getDrawable(
                            context, R.drawable.bg_dialog_title_dark));
            messageTextView.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
        rightButton.setBackground(
                AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
        leftButton.setBackground(
                AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));

        titleTextView.setText(context.getString(R.string.alarm_or_notification_dialog_title));
        messageTextView.setText(context.getString(R.string.alarm_or_notification_dialog_message));
        rightButton.setText(R.string.alarm);
        leftButton.setText(R.string.notification);

        return dialog;
    }

    public Dialog getCustomAlarmDialog() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ConstraintLayout dialogLayout =
                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
        Button rightButton = dialogView.findViewById(R.id.btnYes);
        Button leftButton = dialogView.findViewById(R.id.btnNo);

        if (isDarkMode(context)) {
            dialogLayout.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
            titleTextView.setBackground(
                    AppCompatResources.getDrawable(
                            context, R.drawable.bg_dialog_title_dark));
            messageTextView.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.white, null));
            rightButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
            leftButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
        } else {
            rightButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
            leftButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
        }

        titleTextView.setText(context.getString(R.string.custom_alarm_dialog_title));
        messageTextView.setText(context.getString(R.string.custom_alarm_dialog_message));
        rightButton.setText(R.string.default_alarm);
        leftButton.setText(R.string.custom_alarm);

        return dialog;
    }

    public TimePickerDialog getTimePickerDialog(int cachedHour, int cachedMinute) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);

        TimePickerDialog.OnTimeSetListener timeSetListener =
                (timePicker, selectedHour, selectedMinute) -> {
                    String amOrPm;
                    String time;


                    if (selectedMinute < 10) {
                        time = selectedHour + ":0" + selectedMinute;
                    } else {
                        time = selectedHour + ":" + selectedMinute;
                    }
                    if (selectedHour < 10) {
                        time = "0" + selectedHour + ":" + selectedMinute;
                    }
                    if (selectedHour < 10 && selectedMinute < 10) {
                        time = "0" + selectedHour + ":0" + selectedMinute;
                    }

                    Log.i("TIME", "SELECTED TIME: " + time);

                };

        return new TimePickerDialog(
                context,
                sharedPrefs.getDarkDialogsPref()
                        ? TimePickerDialog.THEME_DEVICE_DEFAULT_DARK
                        : TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                timeSetListener,
                cachedHour,
                cachedMinute,
                sharedPrefs.get24HourFormatPref());
    }

    public Dialog getCrashDialog(String error) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_crash, null);
        Dialog dialog = new AlertDialog.Builder(context).setView(dialogView).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ConstraintLayout dialogLayout =
                dialogView.findViewById(R.id.custom_dialog_constraint_layout);
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title_textview);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message_textview);
        Button rightButton = dialogView.findViewById(R.id.btnYes);
        Button leftButton = dialogView.findViewById(R.id.btnNo);

        if (isDarkMode(context)) {
            dialogLayout.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_dark));
            titleTextView.setBackground(
                    AppCompatResources.getDrawable(
                            context, R.drawable.bg_dialog_title_dark));
            messageTextView.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.white, null));
            rightButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
            leftButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
        } else {
            rightButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
            leftButton.setBackground(
                    AppCompatResources.getDrawable(context, R.drawable.bg_dialog_bottom_btn_dark));
        }

        titleTextView.setText(context.getString(R.string.crash_dialog_title));
        messageTextView.setText(error);
        leftButton.setText(R.string.report);
        rightButton.setText(R.string.ok);
        leftButton.setOnClickListener(
                v -> {
                    Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                    selectorIntent.setData(Uri.parse("mailto:"));

                    String emailAddress = "meditrackdev@gmail.com";
                    String emailTitle = "Meditrack Crash Report";
                    String emailBody =
                            "Hi Stephen,\n\n"
                                    + "I encountered an error while using Meditrack. Here is the error"
                                    + " log:\n\n"
                                    + error;

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    emailIntent.setSelector(selectorIntent);

                    context.startActivity(
                            Intent.createChooser(emailIntent, "Send error report..."));
                });
        rightButton.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }

    public interface PillNameDialogListener {
        void applyPillName(String userPillName);
    }

    public interface PillAmountDialogListener {
        void applyPillSupply(String pillSupply);
    }

    public interface SettingsDialogListener {
        void recreateScreen();
    }

    public interface ChooseFrequencyDialogListener {
        void openTimePicker();

        void setInterval(int intervalInDays);

        void hideIntervalSubText();

        void openStartDatePicker();
    }

    public interface PillReminderAmountDialogListener {
        void applyNumberOfReminders(int reminders);
    }

    public interface ChooseTimesDialogListener {
        void returnTimesStringArray(String[] times);
    }

    public interface GetStartDateDialogListener {
        void applyStartDate(String startDate);
    }

    public interface GetStockupDateDialogListener {
        void applyStockup(String stockupDate);
    }
}