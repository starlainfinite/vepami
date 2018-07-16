package com.technologies.pixelbox.vepami;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterInventoryList extends BaseAdapter{
    protected Activity activity;
    protected ArrayList<Product> inventory;

    public AdapterInventoryList (Activity activity, ArrayList<Product> inventory) {
        this.activity = activity;
        this.inventory = inventory;
    }

    @Override
    public int getCount() {
        return inventory.size();
    }

    public void clear() {
        inventory.clear();
    }

    public void addAll(ArrayList<Product> category) {
        for (int i = 0; i < category.size(); i++) {
            inventory.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return inventory.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.inventory_items, null);
        }

        Product dir = inventory.get(position);
        TextView producto = (TextView) v.findViewById(R.id.textViewProducto);
        producto.setText(dir.getProducto());

        TextView precio = (TextView) v.findViewById(R.id.textViewPrice);
        precio.setText("Precio: $" + String.valueOf(dir.getPrecio()));

        TextView existencia = (TextView) v.findViewById(R.id.textViewExistencia);
        existencia.setText("Existencia: " + String.valueOf(dir.getCantidad()));

        ImageView imagen = (ImageView) v.findViewById(R.id.imageViewBackground);
        if (dir.getThumbnailLocalization().equals("")) {
            imagen.setImageBitmap(null);
        }
        else {
            new FragmentInventory.DownloadImageTask(imagen).execute(inventory.get(position).getThumbnailLocalization());
        }
        return v;
    }
}
