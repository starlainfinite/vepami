package com.technologies.pixelbox.vepami;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id;
    private String producto;
    private int cantidad;
    private float precio;
    private List<ProductImage> productImages;


    public Product() {
        super();
    }

    public Product(String id, String producto, int cantidad, float precio, ProductImage productImage) {
        super();
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.productImages = new ArrayList<>();
        this.productImages.add(productImage);
    }

    public String getThumbnailLocalization() {

        for (ProductImage pi : this.productImages) {
            if (pi.getIsThumbnail() == 1) {
                return pi.getLocalizacion();
            }
        }

        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public ProductImage getProductImage(int index) {
        return productImages.get(index);
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

}
