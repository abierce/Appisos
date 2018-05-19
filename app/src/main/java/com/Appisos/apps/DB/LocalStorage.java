package com.Appisos.apps.DB;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by apps on 07/10/2015.
 */
public class LocalStorage {
    private String TAG = "Appisos";

    //Se ha de añadir context si se usa el metodo desde fuera de la activity
    public HashMap writingLocalStorage(Context context, String filename, String idUser, String nameUser, String passUser,
                                        String name, String surname, String mail) throws FileNotFoundException, IOException {

        //Creamos un HashMap para guardar los datos de forma temporal y siguiendo el esquema
        //clave, valor
        HashMap<String, String> datos = new HashMap<String, String>();
        //Guardamos los datos
        datos.put("idUser", idUser);
        datos.put("nameUser", nameUser);
        datos.put("passUser", passUser);
        datos.put("name", name);
        datos.put("surname", surname);
        datos.put("eMail", mail);

        //Usamos FileOutputStream y le pasamos el nombre del archivo que guardara los datos
        //y el modo de accceso
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        //El metodo openFileOutput() devuelve una instancia de FileOutputStream.
        //Llamamos al metodo write para guardar los datos en el archivo.
        oos.writeObject(datos);
        Log.d(TAG + " Saving data locally", oos.toString());
        Log.d(TAG + " Writing data", datos.toString());
        oos.close();

        return datos;
    }

    //Se ha de añadir context si se usa el metodo desde fuera de la activity
    public HashMap writingLocalStorage(Context context, String filename, String nameUser, String passUser,
                                       String name, String surname, String mail) throws FileNotFoundException, IOException {

        //Creamos un HashMap para guardar los datos de forma temporal y siguiendo el esquema
        //clave, valor
        HashMap<String, String> datos = new HashMap<String, String>();
        //Guardamos los datos
        datos.put("nameUser", nameUser);
        datos.put("passUser", passUser);
        datos.put("name", name);
        datos.put("surname", surname);
        datos.put("eMail", mail);

        //Usamos FileOutputStream y le pasamos el nombre del archivo que guardara los datos
        //y el modo de accceso
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        //El metodo openFileOutput() devuelve una instancia de FileOutputStream.
        //Llamamos al metodo write para guardar los datos en el archivo.
        oos.writeObject(datos);
        Log.d(TAG + " Saving data locally", oos.toString());
        Log.d(TAG + " Writing data", datos.toString());
        oos.close();

        return datos;
    }

    //¿Es necesario el context?
    /*
     * Context context = getApplicationContext();
      * String filename = "filename.txt";
      * String str = readingLocalStorage(context, filename);
     */
    public String[] readingLocalStorage(Context context, String filename) {
        String idUser = "";
        String nameUser = "";
        String passUser = "";
        String name = "";
        String surname = "";
        String mail = "";
        String[] data = new String[6];

        try {
            //
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //
            HashMap<String, Object> fileOb = (HashMap<String, Object>) ois.readObject();
            Log.d(TAG+ " Reading data locally", fileOb.toString());
            //Recorremos el HashMap
            Iterator<String> iter = fileOb.keySet().iterator();
            while (iter.hasNext()) {
                //String key;
                String key = iter.next();
                //Log.d(TAG + " iterator", key + "" + fileOb.get(key));
                if (key.equals("idUser")) {
                    idUser = (String) fileOb.get(key);
                }
                if (key.equals("nameUser")) {
                    nameUser = (String) fileOb.get(key);
                }
                if (key.equals("passUser")) {
                    passUser = (String) fileOb.get(key);
                }
                if (key.equals("name")) {
                    name = (String) fileOb.get(key);
                }
                if (key.equals("surname")) {
                    surname = (String) fileOb.get(key);
                }
                if (key.equals("eMail")) {
                    mail = (String) fileOb.get(key);
                }



            }//fin while iter

            //fin.close();

            //Array de String para devolver los datos
            data[0] = idUser;
            data[1] = nameUser;
            data[2] = passUser;
            data[3] = name;
            data[4] = surname;
            data[5] = mail;

            return data;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG+" Reading IOException", e.getMessage());
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            //Log.d(TAG+" Reading ClassNotFoundException", e2.getMessage());
        }

        Log.d(TAG+ "Reading data", idUser);
        Log.d(TAG+" Reading data", nameUser);
        Log.d(TAG+" Reading data", passUser);
        Log.d(TAG+" Reading data", name);
        Log.d(TAG+" Reading data", surname);
        Log.d(TAG + " Reading data", mail);

        //return idUser+""+nameUser+""+passUser+""+name+""+surname+""+mail;

        return null;

        /*while ((c = fin.read()) != -1) {
            read = read + Character.toString((char)c);
        }*/

    }//fin metodo




}
