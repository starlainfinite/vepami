package com.technologies.pixelbox.vepami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class InventoryItemView extends View {
    ItemEventListener eventListener;

    String idProducto;
    String titulo;
    String precio;

    ImageView imageViewThumbnail;
    TextView textViewTitle;
    TextView textViewPrice;

    public InventoryItemView(Context context, ViewGroup viewGroup, ItemEventListener eventListener) {
        super(context);
        //inflate(context, R.layout.item_inventory, viewGroup);
        View v = this;
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inf.inflate(R.layout.item_inventory, viewGroup);


        imageViewThumbnail = v.findViewById(R.id.imageViewIIThumbnail);
        textViewTitle = v.findViewById(R.id.textViewIITitle);
        textViewPrice = v.findViewById(R.id.textViewIIPrice);
        this.eventListener = eventListener;

        imageViewThumbnail.setOnClickListener(onClickListener);
        textViewPrice.setOnClickListener(onClickListener);
        textViewPrice.setOnClickListener(onClickListener);
    }


    public View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            eventListener.onClickInventoryItem(InventoryItemView.this);
        }
    };

}
