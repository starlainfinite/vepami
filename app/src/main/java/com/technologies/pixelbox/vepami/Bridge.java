package com.technologies.pixelbox.vepami;

import android.graphics.Bitmap;

public class Bridge {
    private Bitmap image;
    private int row;

    public Bridge() {
        super();
    }

    public Bridge (Bitmap image, int row) {
        super();
        this.image = image;
        this.row = row;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Bitmap getImage() {
        return this.image;
    }
}
