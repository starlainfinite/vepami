package com.technologies.pixelbox.vepami;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    int fase = 0;
    User user;
    Button buttonLogin;
    EditText editTextEMail;
    EditText editTextContrasena;
    CheckBox checkBoxRecordar;
    CheckBox checkBoxAdministrador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // SET DATA SENDED FROM MAIN ACTIVITY
        Intent intent = getIntent();
        user = new User();
        user.id = intent.getIntExtra("id", 0);
        user.alias = intent.getStringExtra("alias");
        user.id_tipoLogin = intent.getIntExtra("id_tipoLogin", 0);
        user.tipoLogin = intent.getStringExtra("tipo_login");
        user.id_tipoUsuario = intent.getIntExtra("id_tipoUsuario", 0);
        user.tipoUsuario = intent.getStringExtra("tipo_usuario");
        user.id_imagen = intent.getIntExtra("id_imagen", 0);
        user.localizacion = intent.getStringExtra("localizacion");
        user.verificada = intent.getIntExtra("verificada", 0);
        user.id_estado = intent.getIntExtra("id_estado", 0);
        user.estado = intent.getStringExtra("estado");

        // LINK VIEWS
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(onClickListener);

        editTextEMail = findViewById(R.id.editTextEMail);
        editTextContrasena = findViewById(R.id.editTextPassword);
        checkBoxAdministrador = findViewById(R.id.checkBoxAdministrador);
        checkBoxRecordar = findViewById(R.id.checkBoxRecordarCuenta);

    }


    private Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateData("SelectUsuariosByAlias");
        }
    };


    protected void CreateData(String f)
    {
        StringBuilder mainURL = new StringBuilder(R.string.main_url);//"http://www.pixelbox-tech.com/vepami/main.php?");
        List<String> columns = new ArrayList<String>();
        ExecuteTaskParameters exParams = new ExecuteTaskParameters();
        switch (f)
        {
            case "SelectUsuariosByAlias":
                fase = 0;
                mainURL.append("f=SelectUsuariosByAlias&Alias=");
                mainURL.append(editTextEMail.getText());
                columns.clear();
                columns.add("ID: ");
                exParams.setUrl(mainURL.toString());
                exParams.setColumns(columns);
                new GetUserTask().execute(exParams);
                break;
            case "InsertUsuariosSendConfirmation":
                fase = 1;
                mainURL.append("f=InsertUsuariosSendConfirmation&Alias=");
                mainURL.append(editTextEMail.getText());
                mainURL.append("&Contrasena=");
                mainURL.append(editTextContrasena.getText());
                mainURL.append("&ID_TipoLogin=1");
                if (checkBoxAdministrador.getVisibility() == View.VISIBLE) {
                    if (checkBoxAdministrador.isChecked()) {
                        mainURL.append("&ID_TipoUsuario=1");
                    } else {
                        mainURL.append("&ID_TipoUsuario=2");
                    }
                }
                else {
                    mainURL.append("&ID_TipoUsuario=2");
                }

                exParams.setUrl(mainURL.toString());
                new GetUserTask().execute(exParams);
                break;
            case "SelectUsuariosByNicknamePasswordLoginType":
                fase = 2;
                mainURL.append("f=SelectUsuariosByNicknamePasswordLoginType&Alias=");
                mainURL.append(editTextEMail.getText());
                mainURL.append("&Contrasena=");
                mainURL.append(editTextContrasena.getText());
                mainURL.append("&ID_TipoLogin=1");
                columns.clear();
                columns.add("ID: ");
                columns.add("Alias: ");
                columns.add("ID_TipoLogin: ");
                columns.add("Tipo_Login: ");
                columns.add("ID_TipoUsuario: ");
                columns.add("Tipo_Usuario: ");
                columns.add("ID_Imagen: ");
                columns.add("Localizacion: ");
                columns.add("Verificada: ");
                columns.add("ID_Estado: ");
                columns.add("Estado: ");

                exParams.setColumns(columns);
                exParams.setUrl(mainURL.toString());
                new GetUserTask().execute(exParams);
                break;

            default:
                break;
        }
    }

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
                if (fase == 0 || fase == 2) {
                    this.columns = params[0].getColumns();
                }
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
                    if (fase == 0 || fase == 2) {
                        int row = 0;
                        String item;
                        while ((item = r.readLine()) != null) {
                            if (item.equals("0 results")) {
                                return item;
                            }
                            if (!item.isEmpty()) {
                                if (!this.columns.isEmpty()) {
                                    for (String s : this.columns) {
                                        if (item.contains(s)) {
                                            dataRow.add(item.replaceFirst(s, ""));
                                            break;
                                        }
                                    }
                                    if (dataRow.size() == this.columns.size() * (row + 1)) {
                                        int pos = row * this.columns.size();
                                        if (fase == 2) {
                                            user.id = ParserNumbers.getInt(dataRow.get(0 + pos));
                                            user.alias = dataRow.get(1 + pos);
                                            user.id_tipoLogin = ParserNumbers.getInt(dataRow.get(2 + pos));
                                            user.tipoLogin = dataRow.get(3 + pos);
                                            user.id_tipoUsuario = ParserNumbers.getInt(dataRow.get(4 + pos));
                                            user.tipoUsuario = dataRow.get(5 + pos);
                                            user.id_imagen = ParserNumbers.getInt(dataRow.get(6 + pos));
                                            user.localizacion = dataRow.get(7 + pos);
                                            user.verificada = ParserNumbers.getInt(dataRow.get(8 + pos));
                                            user.id_estado = ParserNumbers.getInt(dataRow.get(9 + pos));
                                            user.estado = dataRow.get(10 + pos);
                                        }
                                        row++;
                                    }
                                } else {
                                    return item;
                                }
                            }
                        }
                    }
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
            if (fase == 0) {
                if (result.equals("0 results")) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(LoginActivity.this);
                    }
                    StringBuilder str = new StringBuilder();
                    str.append("El correo electronico: '");
                    str.append(editTextEMail.getText());
                    str.append("' no se encuentra registrado en el sistema, te gustaria utilizarlo como tu identificador de usuario?");
                    builder.setTitle("CORREO NO REGISTRADO!")
                            .setMessage(str)
                            .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    CreateData("InsertUsuariosSendConfirmation");
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
                else {
                    CreateData("SelectUsuariosByNicknamePasswordLoginType");
                }
            }
            if (fase == 1) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                }
                builder.setTitle("!")
                        .setMessage("Un codigo de verificacion ha sido enviado a su correo electronico, " +
                                "reviselo y siga las instrucciones, si el correo demora no olvide revisar el correo no deseado," +
                                "si bien el correo no se encuentra o demora mucho, solicite reenvio de codigo en el menu principal")
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                CreateData("SelectUsuariosByNicknamePasswordLoginType");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
            if (fase == 2) {
                Intent intent = new Intent();
                if (user.id > 0) {
                    intent.putExtra("id", user.id);
                    intent.putExtra("alias", user.alias);
                    intent.putExtra("id_tipoLogin", user.id_tipoLogin);
                    intent.putExtra("tipo_login", user.tipoLogin);
                    intent.putExtra("id_tipoUsuario", user.id_tipoUsuario);
                    intent.putExtra("tipo_usuario", user.tipoUsuario);
                    intent.putExtra("id_imagen", user.id_imagen);
                    intent.putExtra("localizacion", user.localizacion);
                    intent.putExtra("verificada", user.verificada);
                    intent.putExtra("id_estado", user.id_estado);
                    intent.putExtra("estado", user.estado);


                    if (checkBoxRecordar.isChecked()) {
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        // SET ID
                        editor.putInt("id", user.id);
                        // SET USER NAME
                        editor.putString("al", user.alias);
                        // SET ID LOGIN TYPE
                        editor.putInt("itl", user.id_tipoLogin);
                        // SET LOGIN TYPE
                        editor.putString("tl", user.tipoLogin);
                        // SET ID USER TYPE
                        editor.putInt("itu", user.id_tipoUsuario);
                        // SET USER TYPE
                        editor.putString("tu", user.tipoUsuario);
                        // SET ID IMAGE
                        editor.putInt("iim", user.id_imagen);
                        // SET IMAGE LOCATION
                        editor.putString("lo", user.localizacion);
                        // SET ACCOUNT VERIFICATION
                        editor.putInt("ve", user.verificada);
                        // SET ID STATUS
                        editor.putInt("ist", user.id_estado);
                        // SET STATUS
                        editor.putString("st", user.estado);
                        editor.commit();
                    }
                    setResult(1, intent); //The data you want to send back
                    finish();
                }
                else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(LoginActivity.this);
                    }
                    builder.setTitle("DATOS INCORRECTOS!")
                            .setMessage("Los datos introducidos son incorrectos, por favor verifique su informacion e intente de nuevo")
                            .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            }
        }
    }
}
