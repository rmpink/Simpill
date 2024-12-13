package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ColourPickerSquareView extends View {
    Paint paint;
    Shader vertShader;
    final float[] colour = { 1.f, 1.f, 1.f };

    public ColourPickerSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColourPickerSquareView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("DrawAllocation")
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            vertShader = new LinearGradient(0.f, 0.f, 0.f, this.getMeasuredHeight(), 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
        }
        int rgb = Color.HSVToColor(colour);
        Shader horiShader = new LinearGradient(0.f, 0.f, this.getMeasuredWidth(), 0.f, 0xffffffff, rgb, Shader.TileMode.CLAMP);
        ComposeShader cShader = new ComposeShader(vertShader, horiShader, PorterDuff.Mode.MULTIPLY);
        paint.setShader(cShader);
        canvas.drawRect(0.f, 0.f, this.getMeasuredWidth(), this.getMeasuredHeight(), paint);
    }

    void setHue(float hue) {
        colour[0] = hue;
        invalidate();
    }
}
