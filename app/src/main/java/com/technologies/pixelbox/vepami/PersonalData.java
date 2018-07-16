package com.technologies.pixelbox.vepami;

public class PersonalData {
    String nombres;
    String apellidoPaterno;
    String apellidoMaterno;
    String correoElectronico;
    String telefono;

    public PersonalData() {
        nombres = "";
        apellidoPaterno = "";
        apellidoMaterno = "";
        correoElectronico = "";
        telefono = "";
    }

    public PersonalData(String nombres, String apellidoPaterno, String apellidoMaterno,
                        String correoElectronico, String telefono) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }
}
