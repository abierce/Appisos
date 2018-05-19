package com.Appisos.apps;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.Appisos.apps.DB.LocalStorage;

import java.io.FileInputStream;

public class Configuracion extends AppCompatActivity {
    private Button guardar;
    private EditText cNameUser;
    private EditText cPass;
    private EditText cPassConfirm;
    private EditText cName;
    private EditText cSurname;
    private EditText cEmail;
    private String login_mail;
    private String login_pass;
    private String nameUser;
    private String passUser;
    private String name;
    private String surname;
    private String mail;
    //private String urlString = "http://192.168.1.5:8080/appisos/configuracion.php";
    private String urlString = "http://servidordeprueba1.tk/appisos/configuracion.php";
    private String TAG = "Appisos";
    LocalStorage ls = new LocalStorage();
    private String[] strArr;
    private ActionBar actionbar;
    private ImageView iv;
    private DrawerLayout drawer_layout;
    private ListView drawer_lv;
    private String[] drawer_op;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        guardar = (Button) findViewById(R.id.btnGuardar);
        cNameUser = (EditText) findViewById(R.id.cUserName);
        cPass = (EditText) findViewById(R.id.cPassword);
        cPassConfirm = (EditText) findViewById(R.id.cPassConfirm);
        cName = (EditText) findViewById(R.id.cName);
        cSurname = (EditText) findViewById(R.id.cSurname);
        cEmail = (EditText) findViewById(R.id.cEmail);
        Context context = getApplicationContext();

        //Configuramos el logo de cabecera
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);


        //Ejecutamos la clase asincrona que debe recoger los datos del usuario logeado
        //y mostrarlos en los EditText
        new sendData().execute();

        /*guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }//fin onclick
        }); //fin seOnClickListener*/


    }//fin onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_configuracion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            case R.id.action_filter:
                //Intent i2 = new Intent(Register.this, Configuracion.class);
                //startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class sendData extends AsyncTask<Void, Void, Void> {
        LocalStorage lc = new LocalStorage();

        @Override
        protected Void doInBackground(Void... params) {
            String stream = "";
            FileInputStream fin = null;
            String idUser = "";
            String nameUser = "";
            String passUser = "";
            String name = "";
            String surname = "";
            String mail = "";
            String filename = "Cred.txt";
            Context context = getApplicationContext();
            String[] data;

            //sendJsonGET(urlString, login_mail, login_pass);

            //Leemos los datos personales del usuario, creamos un array tipo String y lo
            //inicializamos con la salida del metodo de lectura
            try {
                data = lc.readingLocalStorage(context, filename);

                for (int i=0; i<data.length;i++) {
                    //idUser = data[0]; //No necesario se indica como guia
                    nameUser = data[1];
                    passUser = data[2];
                    name = data[3];
                    surname = data[4];
                    mail = data[5];
                }

                runThread(nameUser, passUser, name, surname, mail);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG+" Reading IOException", e.getMessage());
            }

            return null;
        }

    }//fin sendData

    public void runThread(final String nameUser, final String passUser, final String name,
                          final String surname, final String mail) {

        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    cNameUser.setText(""+nameUser);
                    cPass.setText(""+passUser);
                    cName.setText(""+name);
                    cSurname.setText(""+surname);
                    cEmail.setText(""+mail);
                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG+" insideThread", e.getMessage());
        }
    }//fin runThread

    /*private void getIntent(Bundle savedInstanceState) {
        //
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        //Bundle extras = getIntent().getExtras();
        login_mail = extras.getString("Login mail");
        login_pass = extras.getString("Login pass");
        //

    }*/
}//fin clase
