package com.technologies.pixelbox.vepami;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DetailItemActivity extends AppCompatActivity {
    ImageButton leftButton;
    ImageButton rightButton;
    ImageView imageView;
    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewPrice;

    List<Bitmap> images;

    Product product;
    public DetailItemActivity(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed_item);
        Intent intent = getIntent();

        leftButton = findViewById(R.id.buttonLeft);
        rightButton = findViewById(R.id.buttonRight);

        leftButton.setOnClickListener(onClickListenerLeftButton);
        rightButton.setOnClickListener(onClickListenerRightButton);

        imageView = findViewById(R.id.imageViewShowcase);
        textViewTitle = findViewById(R.id.textViewIITitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewPrice = findViewById(R.id.textViewPrice);


        String id = getIntent().getStringExtra("id");
        int cantidad = getIntent().getIntExtra("canidad", 0);;
        float precio = getIntent().getFloatExtra("precio", 0);
        String producto = getIntent().getStringExtra("producto");
        int id_imagen = getIntent().getIntExtra("id_imagen", 0);
        int isThumbnail = getIntent().getIntExtra("thumbnail", 0);
        String localizacion = getIntent().getStringExtra("localizacion");
        ProductImage pi = new ProductImage(id_imagen, isThumbnail, localizacion);

        byte[] data = getIntent().getByteArrayExtra("bitmap");
        if (data != null) {
            int lenght = getIntent().getByteArrayExtra("bitmap").length;
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeByteArray(data, 0, lenght);
            imageView.setImageBitmap(bitmap);
        }

        product = new Product(id, producto, cantidad, precio, pi);

        textViewPrice.setText("$" + String.valueOf(product.getPrecio()));
        textViewDescription.setText(product.getProducto());
        textViewTitle.setText(product.getProducto());
    }



    private View.OnClickListener onClickListenerLeftButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener onClickListenerRightButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
