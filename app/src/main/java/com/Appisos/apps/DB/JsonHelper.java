package com.Appisos.apps.DB;

/**
 * Created by apps on 30/09/2015.
 *
 * 1-Crear la clase o clases con los setters y getters
 * 2-Método para crear el objeto Java usando los setters de las clases anteriores
 * 3-Metodo para generar el objeto JSON a partir del objeto Java y usando los getters de la clase
 * del paso 1.
 * 4-Para que los datos del JSON generado sean a partir de datos introducidos por el usuario
 * por pantalla, se le pasan como parametros (String por ejemplo) primero al metodo que crea
 * el objeto Java y luego al metodo que genera el objeto JSON que llama al metodo anterior.
 */

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.Appisos.apps.Register;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//import com.JSONSendGet.app.google.gson.Gson;

public class JsonHelper {

    private static String TAG = "Appisos";

    // -------------------------------------------------------------------------------------------------------------------------
    // Send JsonObject
    // -------------------------------------------------------------------------------------------------------------------------

    //Using Post
    //Enviar JSON usando POST. No he conseguido capturar los valores desde el webservice en PHP
    public void sendJsonPOST(String urlString, String nameUser, String passUser, String name,
                              String surname, String mail) {

        final String USER_AGENT = "App";
        //Recogemos el valor de de los editText
        /*nameUser = rNameUser.getText().toString();
        passUser = rPassUser.getText().toString();
        name = rName.getText().toString();
        surname = rSurname.getText().toString();
        mail = rMail.getText().toString();*/
        //JSONObject jsonObject = JsonHelper.writeCaptura(nameUser);
        JSONObject jsonObject = JsonHelper.write(nameUser, passUser, name, surname, mail);

        if (jsonObject != null) {
            String jsonString = jsonObject.toString();

            Log.d(TAG + " jsonObject.toString", jsonString);

            try {
                //Codificamos el JSON a URL
                /*jsonString = URLEncoder.encode(jsonString, "UTF-8");
                Log.d(TAG + " jsonObject.toStringURL", jsonString);*/
                jsonString = "json="+jsonString;
                Log.d(TAG + " jsonObject.JSON", jsonString);
                                
                //Connection
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //Specify POST method
                urlConnection.setRequestMethod("POST");
                //Set the headers
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setDoInput(true);
                //Send POST request
                urlConnection.setDoOutput(true);
                Log.d(TAG + " urlConnection", urlConnection.toString());
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                Log.d(TAG + " out", out.toString());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                Log.d(TAG + " bw", bw.toString());
                //bw.write(jsonString.getBytes());

                //jsonView.setText(jsonString);
                //setText(jsonView, jsonString);
                //Metodo para imprimir en TextView el texto. Ejecuta thread independiente
                Register r = new Register();
                r.runThread(jsonString);
                //Leemos la respuesta del servidor (webservice)
                HttpDataHandler hdh = new HttpDataHandler();
                hdh.serverFeedback(urlConnection);
                //Comprobamos el codigo de respuesta. 200 es conexion correcta
                String response = String.valueOf(urlConnection.getResponseCode());
                Log.d(TAG + "responseCode", response);
                Log.d(TAG + " out.write", jsonString);

                //Cerramos
                bw.flush();
                bw.close();

                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    throw new RuntimeException("Failed: HTTP error: "
                            + urlConnection.getResponseCode());
                }


                urlConnection.disconnect();

            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG+" Exception",e.getMessage());
            }
        }//fin if

    }//Fin sendJSON

    //Enviamos datos del login para comprobar su veracidad desde el WebService
    public String sendJsonGET(String urlString, String nameUser, String passUser) {
        String response = null;
        //Recogemos el valor de de los editText
        //nameUser = rNameUser.getText().toString();
        //passUser = rPassUser.getText().toString();
        //name = rName.getText().toString();
        //surname = rSurname.getText().toString();
        //mail = rMail.getText().toString();
        //
        //JSONObject jsonObject = JsonHelper.writeCaptura(nameUser);
        JSONObject jsonObject = JsonHelper.write(nameUser, passUser);

        if (jsonObject != null) {
            String jsonString = jsonObject.toString();

            Log.d(TAG + " jsonObject.toString", jsonString);

            try {
                //Codificamos el JSON a URL
                jsonString = URLEncoder.encode(jsonString, "UTF-8");
                Log.d(TAG + " jsonObject.toStringURL", jsonString);
                //Añadimos a la URL la etiqueta json, que nos servira para capturar la cadena
                //json y el objeto JSON convertido en texto
                urlString = urlString+"?json="+jsonString;
                Log.d(TAG+" urlString", urlString);

                //Conexion
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                //Enviamos los datos por POST
                urlConnection.setDoOutput(true);
                Log.d(TAG + " urlConn", urlConnection.toString());
                //Capturamos la respuesta del servidor. En lugar de esto usamos el metodo
                //serverFeedback que implementa BufferedReader e InputStreamReader
                /*BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));*/

                //Metodo para imprimir en TextView el texto. Ejecuta thread independiente
                //runThread(jsonString);
                //Leemos la respuesta del servidor (webservice)
                HttpDataHandler hdh = new HttpDataHandler();
                hdh.serverFeedback(urlConnection);
                //Comprobamos el codigo de respuesta. 200 es conexion correcta
                response = String.valueOf(urlConnection.getResponseCode());
                Log.d(TAG + "responseCode", response);
                //Si el proceso es correcto se ejecuta la actividad MiPerfil
                /*if (response.equals("200")) {
                    Intent i = new Intent(context, MiPerfil.class);
                    context.startActivity(i);
                    Log.d(TAG+" Register completed", i.toString());
                } else {
                    Log.d(TAG+ "Register incomplete", in.toString());
                }*/
                //Cerramos
                //in.close();

            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG + " Exception", e.getMessage());
            }
        }//fin if

        return response;

    }//fin sendJsonGET

    //Alternativa al metodo anterior. Enviar JSON usando GET. Si he conseguido capturar valores
    //desde el webservice en PHP
    //Enviamos los datos que vamos a necesitar en el WebService para hacer las consultas
    //en la BD. Enviamos los datos personales para gestionar el registro
    //Devolvemos la respuesta del servidor que sera capturada desde la actividad. En caso de ser
    // correcta (200) se ejecuta la actividad.
    public String sendJsonGET(String urlString, String nameUser, String passUser, String name,
                             String surname, String mail) {
        String response = null;
        //Recogemos el valor de de los editText
        //nameUser = rNameUser.getText().toString();
        //passUser = rPassUser.getText().toString();
        //name = rName.getText().toString();
        //surname = rSurname.getText().toString();
        //mail = rMail.getText().toString();
        //
        //JSONObject jsonObject = JsonHelper.writeCaptura(nameUser);
        JSONObject jsonObject = JsonHelper.write(nameUser, passUser, name, surname, mail);

        if (jsonObject != null) {
            String jsonString = jsonObject.toString();

            Log.d(TAG + " jsonObject.toString", jsonString);

            try {
                //Codificamos el JSON a URL
                jsonString = URLEncoder.encode(jsonString, "UTF-8");
                Log.d(TAG + " jsonObject.toStringURL", jsonString);
                //Añadimos a la URL la etiqueta json, que nos servira para capturar la cadena
                //json y el objeto JSON convertido en texto
                urlString = urlString+"?json="+jsonString;
                Log.d(TAG+" urlString", urlString);

                //Conexion
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                //Enviamos los datos por POST
                urlConnection.setDoOutput(true);
                Log.d(TAG + " urlConn", urlConnection.toString());
                //Capturamos la respuesta del servidor. En lugar de esto usamos el metodo
                //serverFeedback que implementa BufferedReader e InputStreamReader
                /*BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));*/

                //Metodo para imprimir en TextView el texto. Ejecuta thread independiente
                //runThread(jsonString);
                //Leemos la respuesta del servidor (webservice)
                HttpDataHandler hdh = new HttpDataHandler();
                hdh.serverFeedback(urlConnection);
                //Comprobamos el codigo de respuesta. 200 es conexion correcta
                response = String.valueOf(urlConnection.getResponseCode());
                Log.d(TAG + "responseCode", response);
                //Si el proceso es correcto se ejecuta la actividad MiPerfil
                /*if (response.equals("200")) {
                    Intent i = new Intent(context, MiPerfil.class);
                    context.startActivity(i);
                    Log.d(TAG+" Register completed", i.toString());
                } else {
                    Log.d(TAG+ "Register incomplete", in.toString());
                }*/
                //Cerramos
                //in.close();

            } catch (Exception e) {
                e.getMessage();
                Log.e(TAG + " Exception", e.getMessage());
            }
        }//fin if

        return response;

    }//fin sendJsonGET

    //Enviamos los datos que vamos a necesitar en el WebService para hacer las consultas
    //en la BD. Enviamos idUser para obtener los nombres de las imagenes que tiene subidas
    //Devolvemos la respuesta del servidor que sera capturada desde la actividad.
    public String sendJsonGET(String urlString, String idUser) {
        String stream = "";

        JSONObject jsonObject = JsonHelper.write(idUser);

        if (jsonObject != null) {
            String jsonString = jsonObject.toString();

            Log.d(TAG + " jsonObject.toString", jsonString);

            try {
                //Codificamos el JSON a URL
                jsonString = URLEncoder.encode(jsonString, "UTF-8");
                Log.d(TAG + " jsonObject.toStringURL", jsonString);
                //Añadimos a la URL la etiqueta json, que nos servira para capturar la cadena
                //json y el objeto JSON convertido en texto
                urlString = urlString+"?json="+jsonString;
                Log.d(TAG+" urlString", urlString);

                //Conexion
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                //Enviamos los datos por POST
                urlConnection.setDoOutput(true);
                Log.d(TAG + " urlConn", urlConnection.toString());
                //Capturamos la respuesta del servidor.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                //Capturamos el stream generado en el webservice
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                Log.d(TAG+" stream img json", stream);
                //Cerramos
                in.close();

            } catch (Exception e) {
                e.getMessage();
                //Log.e(TAG + " Exception", e.getMessage());
            }
        }//fin if
        //Devolvemos el stream
        return stream;

    }//fin sendJsonGET

    // -------------------------------------------------------------------------------------------------------------------------
    // Parse Json
    // -------------------------------------------------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------------------------------------------------
    // Create JsonObject
    // -------------------------------------------------------------------------------------------------------------------------

    /**
     * Output:
     * {"id":10,"listMessages":[
     * {"value":1,"text":"value","id_message":0},
     * {"value":11,"text":"value","id_message":1},
     * {"value":21,"text":"value","id_message":2},
     * {"value":31,"text":"value","id_message":3},
     * {"value":41,"text":"value","id_message":4}
     * ]
     * ,"name":"myname"}
     *
     * @return
     */
    //Create JsonObject for User registration
    public static JSONObject write(String nameUser, String passUser, String name, String surname,
                                   String mail) {

        User uObj = createMyObject(nameUser, passUser, name, surname, mail);

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("nameUser", uObj.getNameUser());
            jsonObject.put("passUser", uObj.getPassUser());
            jsonObject.put("name", uObj.getName());
            jsonObject.put("surname", uObj.getSurname());
            jsonObject.put("mail", uObj.getMail());

            return jsonObject;

        } catch (JSONException je) {
            Log.e(TAG, "error in Write", je);
        }

        return null;
    }//Fin write

    //Create JsonObject for User login
    public static JSONObject write(String login_mail, String login_pass) {

        User uObj = createMyObject(login_mail, login_pass);

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("nameUser", uObj.getNameUser());
            jsonObject.put("passUser", uObj.getPassUser());

            return jsonObject;

        } catch (JSONException je) {
            Log.e(TAG, "error in Write", je);
        }

        return null;
    }//Fin write

    //Create JsonObject for sending user id to the server and get images id
    public static JSONObject write(String idUser) {

        User uObj = createMyObject(idUser);

        try {
            JSONObject jo = new JSONObject();
            jo.put("idUser", uObj.getIdUser());
            //Creamos un ArrayList que contendra los datos del usuario
            /*ArrayList<JSONObject> uList = new ArrayList<JSONObject>();
            //Creamos un objeto
            JSONObject jsonObject = new JSONObject();
            //Añadimos los datos que vamos a necesitar
            jsonObject.put("nameUser", uObj.getNameUser());
            jsonObject.put("passUser", uObj.getPassUser());
            //Añadimos el objeto a la lista
            uList.add(jsonObject);

            jo.put("user", uList);*/

            return jo;

        } catch (JSONException je) {
            Log.e(TAG, "error in Write", je);
        }

        return null;
    }//Fin write

    // -------------------------------------------------------------------------------------------------------------------------
    // Create Java Object
    // -------------------------------------------------------------------------------------------------------------------------
    //Create Java Object for User registration
    public static User createMyObject(String nameUser, String passUser, String name, String surname,
                                      String mail) {

        //Instanciamos clase Users con setters y getters
        User u = new User();

        u.setNameUser(nameUser);
        u.setPassUser(passUser);
        u.setName(name);
        u.setSurname(surname);
        u.setMail(mail);

        return u;
    }//End Method

    //Create Java Object for User login
    public static User createMyObject(String login_mail, String login_pass) {

        //Instanciamos clase Users con setters y getters
        User u = new User();

        u.setNameUser(login_mail);
        u.setPassUser(login_pass);

        return u;
    }

    //Create Java Object for sending user id to the server and get images id
    public static User createMyObject(String idUser) {

        //Instanciamos clase Users con setters y getters
        User u = new User();

        u.setIdUser(idUser);

        return u;
    }

    // -------------------------------------------------------------------------------------------------------------------------
    // Model
    // -------------------------------------------------------------------------------------------------------------------------

    public static class User {
        private String idUser;
        private String nameUser;
        private String passUser;
        private String name;
        private String surname;
        private String mail;
        private String info;
        private String phone;
        private String sex;
        private String image;
        private String profile;
        private String idImage;
        private List<User> user;

        public List<User> getUserList() {
            return user;
        }

        public void setUserList(List<User> user) {
            this.user = user;
        }

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public String getNameUser() {
            return nameUser;
        }

        public void setNameUser(String nameUser) {
            this.nameUser = nameUser;
        }

        public String getPassUser() {
            return passUser;
        }

        public void setPassUser(String passUser) {
            this.passUser = passUser;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getPhone(){
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

    }//fin user

}

