package com.technologies.pixelbox.vepami;

import android.graphics.Bitmap;

public class User {
    int id;
    String alias;
    int id_tipoLogin;
    String tipoLogin;
    int id_tipoUsuario;
    String tipoUsuario;
    int id_imagen;
    String localizacion;
    int verificada;
    int id_estado;
    String estado;
    Bitmap imagen;

    PersonalData datosPersonales;

    public User () {
        this.id = 0;
        this.alias = "";
        this.id_tipoLogin = 0;
        this.tipoLogin = "";
        this.id_tipoUsuario = 0;
        this.tipoUsuario = "";
        this.id_imagen = 0;
        this.localizacion = "";
        this.verificada = 0;
        this.id_estado = 0;
        this.estado = "";
        datosPersonales = new PersonalData();
    }

    public User(int id, String alias, int id_tipoLogin, String tipoLogin, int id_tipoUsuario,
                String tipoUsuario, int id_imagen, String localizacion, int verificada, int id_estado,
                String Estado) {
        this.id = id;
        this.alias = alias;
        this.id_tipoLogin = id_tipoLogin;
        this.tipoLogin = tipoLogin;
        this.id_tipoUsuario = id_tipoUsuario;
        this.tipoUsuario = tipoUsuario;
        this.id_imagen = id_imagen;
        this.localizacion = localizacion;
        this.verificada = verificada;
        this.id_estado = id_estado;
        this.estado = estado;
    }
}
