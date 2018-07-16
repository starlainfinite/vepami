package com.technologies.pixelbox.vepami;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DatosPersonalesActivity extends AppCompatActivity{

    EditText nombres;
    EditText apellidoPaterno;
    EditText apellidoMaterno;
    EditText correoElectronico;
    EditText telefono;
    Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        nombres = findViewById(R.id.editTextDPNombres);
        apellidoPaterno = findViewById(R.id.editTextDPApellidoPaterno);
        apellidoMaterno = findViewById(R.id.editTextDPApellidoMaterno);
        correoElectronico = findViewById(R.id.editTextDPCorreoElectronico);
        telefono = findViewById(R.id.editTextTelefono);

        guardar.setOnClickListener(onClickListener);
    }

    Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };



    class GetUserTask extends AsyncTask<ExecuteTaskParameters/*Params*/, Void/*Progress*/, String/*Result*/> {
        InputStream iStream = null;
        String response;
        String items;
        List<String> dataRow = new ArrayList<String>();
        List<String> columns = new ArrayList<String>();

        protected String doInBackground(ExecuteTaskParameters... params) {
            try {
                HttpURLConnection urlConnection = null;
                int length = 500;
                URL url;
                url = new URL(params[0].getUrl());
                    this.columns = params[0].getColumns();
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    int res = urlConnection.getResponseCode();
                    response = urlConnection.getResponseMessage();
                    if (res != 200) {
                        return response;
                    }
                    iStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (iStream != null) {
                        try {
                            iStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                return e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
