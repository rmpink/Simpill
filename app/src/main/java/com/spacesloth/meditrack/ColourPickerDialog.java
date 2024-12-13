package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

public class ColourPickerDialog {
    public interface OnColourPickerSquareListener {
        void onCancel(ColourPickerDialog dialog);
        void onOk(ColourPickerDialog dialog, int colour);
    }

    final Context context;
    final AlertDialog dialog;
    final OnColourPickerSquareListener listener;
    final ColourPickerSquareView vSatVal;
    final Guideline gTop, gLeft;
    final View vHue, vChosenColour;
    final ImageView ivSatValCursor, ivHueCursor, ivLookPreview;
    final float[] chosenColourHSV = new float[3];

    @SuppressLint("ClickableViewAccessibility")
    public ColourPickerDialog(Context _context, int _colour, String _look, OnColourPickerSquareListener _listener) {
        this.context = _context;
        this.listener = _listener;
        Color.colorToHSV(_colour, chosenColourHSV);

        View inflater = LayoutInflater.from(context).inflate(R.layout.colour_picker_dialog, null);

        vHue = inflater.findViewById(R.id.img_hue);
        vSatVal = inflater.findViewById(R.id.v_sat_val);
        ivHueCursor = inflater.findViewById(R.id.img_hue_cursor);
        ivSatValCursor = inflater.findViewById(R.id.img_sat_val_cursor);
        vChosenColour = inflater.findViewById(R.id.v_chosen_colour);
        gTop = inflater.findViewById(R.id.h_guideline_start);
        gLeft = inflater.findViewById(R.id.v_guideline_start);
        ivLookPreview = inflater.findViewById(R.id.iv_look_preview);

        vSatVal.setHue(getHue());
        vChosenColour.setBackgroundColor(_colour);

        int resId = context.getResources().getIdentifier(_look, "drawable", context.getPackageName());
        ivLookPreview.setImageResource(resId);

        if (_colour != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ivLookPreview.setColorFilter(new BlendModeColorFilter(_colour, BlendMode.MODULATE));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ivLookPreview.setColorFilter(_colour, PorterDuff.Mode.MULTIPLY);
            }
        }

        vHue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent evt) {
                int action = evt.getAction();

                if (action == MotionEvent.ACTION_MOVE ||
                    action == MotionEvent.ACTION_DOWN ||
                    action == MotionEvent.ACTION_UP) {

                    float y = evt.getY();
                    int vHueMeasuredHeight = vHue.getMeasuredHeight();
                    if (y < 0.f) y = 0.f;
                    if (y > vHueMeasuredHeight) y = vHueMeasuredHeight - 0.001f;

                    float hue = 360.f - (360.f/vHue.getMeasuredHeight() *  y);
                    if (hue == 360.f) hue = 0.f;
                    setHue(hue);

                    vSatVal.setHue(getHue());
                    moveHueCursor();
                    vChosenColour.setBackgroundColor(getColour());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ivLookPreview.setColorFilter(new BlendModeColorFilter(getColour(), BlendMode.MODULATE));
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ivLookPreview.setColorFilter(getColour(), PorterDuff.Mode.MULTIPLY);
                    }
                    return true;
                }
                return false;
            }
        });
        vSatVal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent evt) {
                int action = evt.getAction();

                if (action == MotionEvent.ACTION_MOVE ||
                        action == MotionEvent.ACTION_DOWN ||
                        action == MotionEvent.ACTION_UP) {

                    float x = evt.getX();
                    if (x < 0.f) x = 0.f;
                    if (x > vSatVal.getMeasuredWidth()) x = vSatVal.getMeasuredWidth();

                    float y = evt.getY();
                    if (y < 0.f) y = 0.f;
                    if (y > vSatVal.getMeasuredHeight()) y = vSatVal.getMeasuredHeight();

                    setSat(1.f / vSatVal.getMeasuredWidth() * x);
                    setVal(1.f - (1.f / vSatVal.getMeasuredHeight() * y));

                    moveSatValCursor();
                    vChosenColour.setBackgroundColor(getColour());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ivLookPreview.setColorFilter(new BlendModeColorFilter(getColour(), BlendMode.MODULATE));
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ivLookPreview.setColorFilter(getColour(), PorterDuff.Mode.MULTIPLY);
                    }
                    return true;
                }
                return false;
            }
        });

        dialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ColourPickerDialog.this.listener != null) {
                            ColourPickerDialog.this.listener.onOk(ColourPickerDialog.this, getColour());
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ColourPickerDialog.this.listener != null) {
                            ColourPickerDialog.this.listener.onCancel(ColourPickerDialog.this);
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (ColourPickerDialog.this.listener != null) {
                            ColourPickerDialog.this.listener.onCancel(ColourPickerDialog.this);
                        }
                    }
                }).create();
        dialog.setView(inflater, 0, 0, 0, 0);

        ViewTreeObserver vto = inflater.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moveHueCursor();
                moveSatValCursor();
                inflater.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private int getColour() { return (Color.HSVToColor(chosenColourHSV) & 0xffffffff); }

    private float getHue() { return chosenColourHSV[0]; }
    private float getSat() { return chosenColourHSV[1]; }
    private float getVal() { return chosenColourHSV[2]; }

    private void setHue(float hue) { chosenColourHSV[0] = hue; }
    private void setSat(float sat) { chosenColourHSV[1] = sat; }
    private void setVal(float val) { chosenColourHSV[2] = val; }

    protected void moveHueCursor() {
        float y = vHue.getMeasuredHeight() - (getHue() * vHue.getMeasuredHeight()/360.f);
        if (y == vHue.getMeasuredHeight()) y = 0.f;

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivHueCursor.getLayoutParams();
        layoutParams.topMargin = (int) (vHue.getTop() + y - Math.floor((double) ivHueCursor.getMeasuredHeight() / 2) - gTop.getTop());
        ivHueCursor.setLayoutParams(layoutParams);
    }

    protected void moveSatValCursor() {
        float x = getSat() * vSatVal.getMeasuredWidth();
        float y = (1.f -  getVal()) * vSatVal.getMeasuredHeight();

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivSatValCursor.getLayoutParams();
        layoutParams.leftMargin = (int) (vSatVal.getLeft() + x - Math.floor((double) ivSatValCursor.getMeasuredWidth() / 2) - gLeft.getLeft());
        layoutParams.topMargin = (int) (vSatVal.getTop() + y - Math.floor((double) ivSatValCursor.getMeasuredHeight() / 2) - gTop.getTop());
        ivSatValCursor.setLayoutParams(layoutParams);
    }

    public void show() { dialog.show(); }
    public AlertDialog getDialog() { return dialog; }
}
