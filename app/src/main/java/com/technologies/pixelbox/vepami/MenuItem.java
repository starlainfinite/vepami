package com.technologies.pixelbox.vepami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuItem extends View {
    public MenuItem(Context context, ViewGroup viewGroup) {
        super(context);
        View v = this;
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.menu_unlogged, viewGroup);
    }
}
