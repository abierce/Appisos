package com.Appisos.apps;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.Appisos.apps.DB.HttpDataHandler;
import com.Appisos.apps.DB.JsonHelper;
import com.Appisos.apps.DB.LocalStorage;
import com.Appisos.apps.Img.GestionImagenes;

import java.util.ArrayList;

import static android.util.Log.d;

public class Inicio extends AppCompatActivity {
    String TAG = "Appisos";
    TextView TVI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        TVI = (TextView) findViewById(R.id.TVI);

        new getImg().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class getImg extends AsyncTask<String, Void,Void> {
        GestionImagenes gi = new GestionImagenes();

        @Override
        protected Void doInBackground(String... params) {
            Context context = getApplication();
            LocalStorage ls = new LocalStorage();
            String data[];
            String idUser = "";
            String urlString = "";
            ArrayList<String> img = new ArrayList<String>();

            try {
                data = ls.readingLocalStorage(context, "Cred.txt");

                for (int i=0; i<data.length;i++) {
                    idUser = data[0]; //No necesario se indica como guia
                    Log.d(TAG + " idUser Inicio", idUser);
                }
                HttpDataHandler hh = new HttpDataHandler();
                urlString = hh.GetHTTPData("http://servidordeprueba1.tk/test/getImgName.php");
                img = gi.getImgUrl(idUser);
                for (int i = 0; i < img.size(); i++) {
                    urlString = img.get(i);
                    Log.d(TAG + " Inicio urlString", urlString);
                    runThread(urlString);
                }

            } catch (Exception e) {
                e.printStackTrace();
                //Log.d(TAG + " Reading IOException", e.getMessage());
            }

            return null;
        }
    }//fin getImg

    public void runThread(final String urlString) {

        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    TVI.setText(""+urlString);

                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG+" insideThread", e.getMessage());
        }
    }//fin runThread

}//fin class
