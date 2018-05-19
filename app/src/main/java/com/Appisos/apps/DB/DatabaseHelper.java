package com.Appisos.apps.DB;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by apps on 29/09/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "prueba.db";
    private static final int SCHEMA_VERSION = 1;
    //Variable para el patron singleton
    private static DatabaseHelper singleton = null;
    private static Context ctxt = null;
    private HttpDataHandler hdh;

    //Se crea patron Singleton para que solo se pueda instanciar una
    //vez la clase DatabaseHelper
    synchronized static DatabaseHelper getInstance(Context ctxt) {
        if (singleton == null) {
            singleton = new DatabaseHelper(ctxt.getApplicationContext());
        }

        return(singleton);
    } // fin DatabaseHelper

    private DatabaseHelper(Context ctxt) {
        super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);

    }// fin DatabaseHelper

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            //db.execSQL("CREATE TABLE ...");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }//Fin onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //throw new RunTimeException(ctxt.getString(R.string.on_upgrade_error));
    }

    public String createJSON (String urlString, Users users) {

        try {
            //Creamos el JSONObject principal
            JSONObject jsonOb = new JSONObject();

            //Creamos el primer array (users)
            JSONArray jsonArr = new JSONArray();

            //Recorremos el array
            for (Users.UsersMain um : users.getUsersList()) {
                //Creamos el JSONObject dentro del array que contendra
                //los datos
                JSONObject jo = new JSONObject();
                jo.put("uid", um.getIdUser());
                jo.put("user", um.getUserList());
                //Añadimos el JSONObject al array
                jsonArr.put(jo);
            }//fin for

            //Añadimos el array al objeto principal
            jsonOb.put("users", jsonArr);

            //Llamamos al metodo setHTTPdata de la clase HttpDataHandler que gestiona
            //el envio y recepcion de flujo de datos. Le pasamos como parametros
            //la url y el objeto json creado
            hdh.SendHTTPData(urlString, jsonOb);
            return jsonOb.toString();

        } catch (Exception e) {
            Toast.makeText(ctxt.getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }//Fin catch

        return null;

    }//Fin metodo
}
