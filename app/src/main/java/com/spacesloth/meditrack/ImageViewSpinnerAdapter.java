package com.spacesloth.meditrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageViewSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Integer> images;
    LayoutInflater inflater;
    int colour;

    public ImageViewSpinnerAdapter(Context applicationContext, List<Integer> images, int colour) {
        this.context = applicationContext;
        this.images = images;
        this.inflater = LayoutInflater.from(applicationContext);
        this.colour = colour;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return this.images.get(i);
    }

    public int getPosition(int s) {
        if (images.contains(s)) {
            return images.indexOf(s);
        } else {
            return -1;
        }
    }

    public View getItemView() {
        View v = inflater.inflate((R.layout.image_view_adapter_item), null);
        return v.findViewById(R.id.image);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int c) {
        colour = c;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.image_view_adapter_item, null);
        ImageView icon = view.findViewById(R.id.image);
        icon.setImageResource(images.get(i));
        icon.setBackgroundColor(context.getResources().getColor(R.color.dark_background_nav_bar_color));

        if (colour != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                icon.setColorFilter(new BlendModeColorFilter(colour, BlendMode.MODULATE));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                icon.setColorFilter(colour, PorterDuff.Mode.MULTIPLY);
            }
        }
        return view;
    }
}
