package com.Appisos.apps.DB;
 /**
 * Created by apps on 24/09/2015.
 */
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;

public class HttpDataHandler {

    static String stream = null;
    private String TAG = "Appisos";

    public HttpDataHandler(){
    }

    public String GetHTTPData(String urlString){
        try{
            //Connection
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                // if response code = 200 ok
                //BufferedInputStream wraps an existing OutputStream and buffers (almacena) the input
                //getIntpuStream returns an InputStream object
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                //Read the BufferedInputStream                
                //InputStreamReader turns a byte stream into a character stream 
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                //Alternative 1: Does it work?
                //BufferedReader r= new BufferedReader(new InputStreamReader(urlConnection.getInputStream));
                //Alternative 2: Does it work?
                //InputStream in = urlConnection.getInputStream();
                //BufferedReader r = new BufferedReader(in);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();

                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            }
            else
            {
                // Do something
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {

        }
        // Return the data from specified url
        return stream;
    }//Fin GetHTTPData

    public void SendHTTPData(String urlString, JSONObject json) {

        try {

            //Connection
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {

                //getOutputStream returns an OutputStream object
                OutputStream os = urlConnection.getOutputStream();
                //OutputStreamWriter is intended to wrap an OutputStream
                //thereby turning the byte based output stream into a character
                //based writer
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


                bw.write(json.toString());
                bw.flush();
                bw.close();

                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                    throw new RuntimeException("Failed: HTTP error: "
                            +urlConnection.getResponseCode());
                }

                //Capturamos el stream para comprobar que es correcto

                /*BufferedReader br = new BufferedReader(new InputStreamReader(
                        (urlConnection.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }*/

                urlConnection.disconnect();
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }//fin sethttpdata

    //Recibe la respuesta del webservice
    public String serverFeedback(HttpURLConnection urlConnection) throws IOException {
        String stream;
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        // Read the BufferedInputStream
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            sb.append(line);
        }
        stream = sb.toString();
        Log.d(TAG + " InputStream", stream);
        return stream;
    }//fin serverFeedback
}