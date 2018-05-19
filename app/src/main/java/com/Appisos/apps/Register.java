package com.Appisos.apps;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import com.Appisos.apps.DB.JsonHelper;
import com.Appisos.apps.DB.LocalStorage;
import com.Appisos.apps.Fragments.RegisterFragment;
import com.Appisos.apps.Fragments.RegisterFragment2;

public class Register extends FragmentActivity implements View.OnClickListener {
    private EditText rNameUser;
    private EditText rPassUser;
    private EditText rPassCofirm;
    private EditText rName;
    private EditText rSurname;
    private EditText rMail;
    private TextView jsonView; //tv en el layout
    //private Button btnReg;
    private Button btnNext;
    private String TAG = "Selfietimeout";
    private String nameUser;
    private String passUser;
    private String passConfirm;
    private String name;
    private String surname;
    private String mail;
    //private String urlString = "http://192.168.1.5:8080/appisos/RegisterJsonGET.php";
    private String urlString = "http://servidordeprueba1.tk/test/RegisterJsonGET.php";
    private String urlString2 = "http://192.168.1.10:1080/appisos/RegisterJsonGET.php";
    private String urlString3 = "http://192.168.1.10:1080/appisos/PruebaPOST.php";
    private Boolean register = false;
    private String btnText;
    private int State = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //
        Fragment fragment = new RegisterFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layoutToReplace, fragment);
        ft.commit();

        //Inicializamos las variables que necesitaremos
        //Los EditText en los que el usuario introduce los datos
        /*rNameUser = (EditText) findViewById(R.id.rUserName);
        rPassUser = (EditText) findViewById(R.id.rPassword);
        rName = (EditText) findViewById(R.id.rName);
        rSurname = (EditText) findViewById(R.id.rLastname);
        rMail = (EditText) findViewById(R.id.rEmail);*/
        //TextView de prueba
        //jsonView = (TextView) findViewById(R.id.tvr);
        //Botones para realizar las acciones
        //En la nueva version existen un unico boton que realiza las dos acciones:
        //siguiente y finalizar (registro)
        //reg = (Button) findViewById(R.id.btnRegister);
        //reg.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btnNext);
        //Con una variable tipo String capturamos el valor del boton
        //Se le añade trim para que no considere los espacios
        btnText = btnNext.getText().toString().trim();
        Log.d(TAG+" btnText", btnText);
        btnNext.setOnClickListener(this);

        /*next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setContentView(R.layout.register_fragment2);
                Fragment fragment2 = new RegisterFragment2();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.layoutToReplace, fragment2);
                ft.commit();

            }
        }); //fin boton*/

        /*reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SendData().execute(urlString);

            }
        }); //fin boton*/

    }//fin onCreate

    @Override
    public void onClick(View v) {

        if (btnText.compareTo("Siguiente >") == 0) {

            btnNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //setContentView(R.layout.register_fragment2);
                    Fragment fragment2 = new RegisterFragment2();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.layoutToReplace, fragment2);
                    ft.addToBackStack(null);
                    ft.commit();
                    //Inicializamos las variables EditText con los layouts que les corresponden
                    rNameUser = (EditText) findViewById(R.id.rUserName);
                    rPassUser = (EditText) findViewById(R.id.rPassword);
                    //
                    rPassCofirm = (EditText) findViewById(R.id.rPasswordConfirm);
                    //Capturamos las variables nombre usuario y password introoucidos por el
                    //usuario en los editText
                    nameUser = rNameUser.getText().toString();
                    passUser = rPassUser.getText().toString();
                    //
                    passConfirm = rPassCofirm.getText().toString();
                    Log.d(TAG + " nameUser", nameUser);
                    Log.d(TAG + " passUser", passUser);
                    //Cambiamos el boton de texto
                    btnNext.setText("Finalizar");
                    Log.d(TAG + " btnNext", btnNext.getText().toString());
                    //State = 1;
                    //Log.d(TAG+" State", String.valueOf(State));
                    //Log.d(TAG+" Inside State0", btnText);
                    //Ejecutamos el meteodo que se encargara de accionar el segundo evento
                    //del boton, guardar y finalizar el registro. Le pasamos como argumento
                    //las variables capturadas en el primer fragment
                    if (passUser.compareTo(passConfirm)== 0 ) {
                        //btnNext.setText("Finalizar");
                        register(btnNext, nameUser, passUser);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Por favor vuelva a introducir la contraseña", Toast.LENGTH_LONG)
                                .show();
                    }


                }
            }); //fin boton
            //
        }//fin if

    }//fin onClick

    //Metodo para ejecutar la accion de guardar registro. Se le pasan como argumentos
    //las variables capturas en el primer fragment
    private void register(Button button, final String nameUser, final String passUser) {
        Log.d(TAG+" Inside State1", btnNext.toString());

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG+" inside btnFinalizar", v.toString());
                //Inicializamos las variables EditText con los layouts que les corresponden
                rName = (EditText) findViewById(R.id.rName);
                rSurname = (EditText) findViewById(R.id.rLastname);
                rMail = (EditText) findViewById(R.id.rEmail);
                //Capturamos las variables introducidas por el usuario en el segundo fragment
                name = rName.getText().toString();
                surname = rSurname.getText().toString();
                mail = rMail.getText().toString();
                Log.d(TAG+" name", name);
                Log.d(TAG+" surname", surname);
                Log.d(TAG+" mail", mail);
                //Llamamos y ejecutamos la clase SendData pasandole como parametros las
                //variables introducidas por el usuario en los EditText
                new SendData().execute(urlString, nameUser, passUser, name, surname, mail);

            }
        }); //fin boton
    }

    //Clase asincrona para enviar los datos
    public class SendData extends AsyncTask<String, Void, Void> {
        String response;
        /*EditText rUsername;
        String username;

        //Se crea un constructor dentro de la clase Async para poder obtener los datos del
        //EditText usando getText, ya que desde dentro del metodo doInBackground da error
        public SendData(EditText rUserName, String username) {
            username = rUserName.getText().toString();
            Log.d(TAG+" Construc Async", username);
        }*/

        @Override
        protected Void doInBackground(String... params) {

            //sendJsonPOST(urlString);
            JsonHelper jh = new JsonHelper();
            LocalStorage ls = new LocalStorage();
            Context context = getApplicationContext();
            String credentials = "Cred.txt";
            try {
                response = jh.sendJsonGET(urlString, nameUser, passUser, name, surname, mail);
                ls.writingLocalStorage(context, credentials, nameUser, passUser, name, surname, mail);
                //Ejecutamos la actividad si la salida del metodo que envia los datos es correcta
                //es decir si la conexion con el servidor es correcta
                if (response.equals("200")) {
                    Intent i = new Intent(Register.this, MiPerfil.class);
                    startActivity(i);
                    Log.d(TAG+" Register completed", i.toString());
                } else {
                    Log.d(TAG+ "Register incomplete", "Register incomplete");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            return null;
        }//fin doInBackground

    }//fin senddata

    //----------------------------------------------------------------------------------------------
    //-------------METODOS AUXILIARES---------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    private String getText(EditText rUserName, String username) {
        return username = rUserName.getText().toString();

    }

    private void setText(TextView jsonView, String string) {

        jsonView.setText(string);
    }

    //Permite realizar setText sobre un TextView desde fuera del thread principal
    public void runThread(String jsonString) {
        final String str = jsonString;

        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    jsonView.setText(str);
                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG+" insideThread", e.getMessage());
        }
    }//fin runThread

}//fin clase
