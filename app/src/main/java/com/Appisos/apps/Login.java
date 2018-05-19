package com.Appisos.apps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.os.AsyncTask;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Appisos.apps.DB.HttpDataHandler;
import com.Appisos.apps.DB.LocalStorage;

import java.util.HashMap;

public class Login extends Activity {
    private static String urlString;
    private Button btnLogin;
    private Button btnLinkToRegisterScreen;
    private Button btnForgotPass;
    private EditText input_mail;
    private EditText input_pass;
    private TextView tv;
    //private DatabaseHelper db;
    private String idUser;
    private String user;
    private String pass;
    private String name;
    private String surname;
    private String mail;
    private String TAG = "Appisos";
    private String credentials = "Cred.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getActionBar().hide();

        //
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        //btnForgotPass = (Button) findViewById(R.id.btnForgotPass);
        input_mail = (EditText) findViewById(R.id.input_email);
        input_pass = (EditText) findViewById(R.id.input_password);
        //
        btnLogin.getBackground().setAlpha(90); //62
        btnLinkToRegisterScreen.getBackground().setAlpha(70); //50

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tv.setText("");
                urlString = "http://servidordeprueba1.tk/test/login.php";
                //urlString = "http://192.168.1.5:8080/appisos/login.php";
                //urlString = "https://selfietimeout.com/appisos/login.php";
                //db.getJSON(urlString);
                new GetJSON().execute(urlString);
            }
        }); //btnLogin

        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(Login.this,
                        Register.class);
                startActivity(i);
                finish();
            }
        }); //fin btnLinkToRegisterScreen

    }//FIn onCreate

    public void credentials() {
        String mail = input_mail.getText().toString();
        String pass = input_pass.getText().toString();
    }

    private class GetJSON extends AsyncTask<String, Void, String>{
        LocalStorage ls = new LocalStorage();

        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            HttpDataHandler hh = new HttpDataHandler();
            stream = hh.GetHTTPData(urlString);
            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            //TextView tv = (TextView) findViewById(R.id.tv);
            String idUser = "";
            String nameUser = "";
            String passUser = "";
            String name = "";
            String surname = "";
            String mail = "";
            HashMap datos = null;
            Boolean cred = false;
            //tv.setText(stream);

            /*
                Important in JSON DATA
                -------------------------
                * Square bracket ([) represents a JSON array
                * Curly bracket ({) represents a JSON object
                * JSON object contains key/value pairs
                * Each key is a String and value may be different data types
             */

            //..........Process JSON DATA................
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONObject "User"...........................
                    JSONArray users = reader.getJSONArray("users");

                    for (int i=0; i<users.length(); i++) {
                        //JSONArray innerJa = users.getJSONArray(i);
                        JSONObject jo = users.getJSONObject(i);
                        idUser = jo.getString("idUser");
                        JSONObject jo2 = jo.getJSONObject("user");
                        //Capturamos el nombre de usuario y pass del usuario que hace el login
                        nameUser = jo2.getString("nameUser");
                        passUser = jo2.getString("passUser");
                        //Capturamos el resto de datos del usuario para guardarlos de forma local
                        //y ser cargados y usados por la actividad configuracion mas tarde
                        name = jo2.getString("name");
                        surname = jo2.getString("surname");
                        mail = jo2.getString("mail");

                        user = input_mail.getText().toString();
                        pass = input_pass.getText().toString();

                        //Llamamos al metodo para encriptar el pass

                        Log.d(TAG + " mail", user);
                        Log.d(TAG + " pass", pass);

                        //Tenemos que comparar el pass encriptado
                        if (user.equals(nameUser) && pass.equals(passUser)) {
                            //Guardamos los datos en un archivo local
                            Context context = getApplicationContext();
                            ls.writingLocalStorage(context, credentials, idUser,nameUser, passUser, name, surname,
                                    mail);
                            //Leemos los datos para ver si se han guardado de forma correcta

                            Log.d(TAG + " Context", context.toString());
                            ls.readingLocalStorage(context, credentials);
                            //Al ser correctas las credenciales ejecutamos la actividad
                            //MiPerfil
                            //Intent i2 = new Intent(Login.this, MiPerfil.class);
                            Intent i2 = new Intent(Login.this, Inicio.class);
                            startActivity(i2);
                            cred = true;
                            break;
                        }

                    }//Fin for

                    if (cred != true) {
                        Toast.makeText(getApplicationContext(),
                                "Nombre de usuario o clave incorrectas", Toast.LENGTH_LONG)
                                .show();
                    }


                }catch(Exception e){
                    e.printStackTrace();
                    Log.d(TAG + " ", e.getMessage());
                }

            } // if statement end
        } // onPostExecute() end

    }//GetJSON class

}//FIn activity
