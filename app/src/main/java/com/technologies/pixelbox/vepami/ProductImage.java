package com.technologies.pixelbox.vepami;



public class ProductImage {
    private int id;
    private String localizacion;
    private int isThumbnail;


    public ProductImage(int id, int isThumbnail, String localizacion) {
        this.id = id;
        this.localizacion = localizacion;
        this.isThumbnail = isThumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public int getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(int isThumbnail) {
        this.isThumbnail = isThumbnail;
    }
}
