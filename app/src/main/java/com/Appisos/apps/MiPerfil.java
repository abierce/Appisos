package com.Appisos.apps;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.Appisos.apps.Auxiliar.DrawerItem;
import com.Appisos.apps.Auxiliar.DrawerListAdapter;
import com.Appisos.apps.Auxiliar.Logout;
import com.Appisos.apps.DB.JsonHelper;
import com.Appisos.apps.DB.LocalStorage;
import com.Appisos.apps.Fragments.MiPerfilFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MiPerfil extends AppCompatActivity {
    private String str;
    private String TAG = "Appisos";
    private TextView tvm;
    //
    private ActionBar actionbar;
    private DrawerLayout NavDrawerlayout;
    private ListView NavList;
    private ArrayList<DrawerItem> NavItems;
    private TypedArray NavIcons;
    private String[] titulos;
    //NavigationAdapter NavAdapter;
    private ActionBarDrawerToggle DrawerToggle;
    private DrawerLayout DrawerLayout;
    private TextView tv;
    //variables fichero
    String idUser="";
    String nameUser="";
    String passUser="";
    LocalStorage lc = new LocalStorage();
    String filename = "Cred.txt";
    String[] data;
    //String urlString = "http://192.168.1.5:8080/appisos/RegisterJsonGET.php";
    String urlString = "http://servidordeprueba1.tk/appisos/RegisterJsonGET.php";
    private String response = null;

    @Override
    //@SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mi_perfil);
        setContentView(R.layout.drawer_layout);

        //Reemplazamos el FrameLayout del layout del Menu Lateral con el layout de la activity
        Fragment fragment = new MiPerfilFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        //Inicializamos los elementos graficos que necesitaremos mas adelante
        tv =  (TextView) findViewById (R.id.profile_name);
        Log.d(TAG+" before Reading", nameUser);
        //Leemos los datos personales del fichero local, que retorna un array con los mismos
        data = lc.readingLocalStorage(getApplicationContext(), filename);
        //Recorremos el array y sacamos el valor de nameUser
        for (int i=0; i<data.length;i++) {
            idUser = data[0];
            nameUser = data[1];
            passUser = data[2];
        }
        Log.d(TAG + " Profile nameUser", nameUser);
        //Mostramos nombre de usuario en el TextView
        tv.setText(nameUser);
        //Enviamos los datos leidos del archivo al servidor
        new getImages().execute(idUser, nameUser, passUser);

        /*if (response.equals("200")) {
            Intent i = new Intent(Mi.this, MiPerfil.class);
            startActivity(i);
            Log.d(TAG+" Register completed", i.toString());
        } else {
            Log.d(TAG+ "Register incomplete", "Register incomplete");
        }*/

        //Clase de prueba para trabajar con SQL Server. Se conecta a una instancia SQL Server
        //instalada en un PC de la red local y carga datos de la BD.
        //new SQLServer().execute();

        //------ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        //------ACTIONBAR

        //------NAVIGATION DRAWER
        //Drawer Layout
        NavDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //List
        NavList = (ListView) findViewById(R.id.drawer_list);
        //Si quisieramos establecer header lo hariamos aqui
        //TOmamos listado de imgs desde drawable
        NavIcons = getResources().obtainTypedArray(R.array.nav_icons);
        //Tomamos listado de titulos desde el string-array de los recursos @string/nav_options
        titulos = getResources().getStringArray(R.array.nav_titles);
        //Listado de titulos de barra de navegacion
        NavItems = new ArrayList<DrawerItem>();
        //Agregamos objetos DrwaerItem al array
        //Inicio
        //NavItems.add(new DrawerItem(titulos[0], R.drawable.home_bt_menu));
        NavItems.add(new DrawerItem(titulos[0], NavIcons.getResourceId(0, -1)));
        //Perfil
        //NavItems.add(new DrawerItem(titulos[1], NavIcons.getResourceId(1, -1))); //El primer numero era 1 pero da error
        //Log out
        //NavItems.add(new DrawerItem(titulos[5], NavIcons.getResourceId(2, -1))); //El primer numero era 5 pero da error
        //Declaramos y seteamos nuestro adaptador al cual le pasamos el array con los titulos
        NavList.setAdapter(new DrawerListAdapter(this, NavItems));
        NavList.setOnItemClickListener(new DrawerItemClickListener());

        NavDrawerlayout.setDrawerListener(DrawerToggle);
        //Permitir que la ActionBar muestre el menu lateral
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_bt);
        //
        //DrawerLayout.openDrawer(GravityCompat.START);
        //
        //Añadimos el DrawerToggle como drawerListener
        DrawerToggle = new ActionBarDrawerToggle(
                this,
                DrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };//fin DrawerToggle

        //Seteamos la escucha
        //DrawerLayout.setDrawerListener(DrawerToggle);
        //------NAVIGATION DRAWER

    }//fin onCreate

    //-----------ACTIONBAR
    //Añadimos el actionBar al activity actual
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_mi_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = DrawerLayout.isDrawerOpen(NavList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    //----------ACTIONBAR
    //Gestiionamos los eventos al pulsar los iconos del ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (DrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_config:
                Intent i = new Intent(MiPerfil.this, Configuracion.class);
                startActivity(i);
                return true;
            case R.id.action_search:
                //Intent i2 = new Intent(Register.this, Configuracion.class);
                //startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }//fin switch

    }//fin metodo

    //-------------NAV DRAWER
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DrawerToggle.onConfigurationChanged(newConfig);
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        DrawerToggle.syncState();
    }*/

    //-------------NAV DRAWER
    //Clase para gestionar los eventos del Navigation Drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            //Gestionamos las opciones de navegacion
            Intent intent;
            switch (position) {
                case 0:
                    intent = new Intent(MiPerfil.this, Inicio.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(MiPerfil.this, MiPerfil.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(MiPerfil.this, Logout.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }//fin switch
        }//fin selectItem

        //Metodo añadido bajo sugerencia de un error en tiempo de ejecucion
        //si no se añade da error

    }//fin drawerItemClickListener

    private class getImages extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            getIdImages(idUser, nameUser, passUser);

            return null;
        }
    }

    private void getIdImages(String idUser, String nameUser, String passUser) {
        String idImage;
        //String urlString = "http://192.168.1.5:8080/appisos/getIdImages.php";
        String urlString = "http://servidordeprueba1.tk/appisos/getIdImages.php";
        JsonHelper jh = new JsonHelper();
        String[] idImg = null;
        //Enviamos datos al servidor y capturamos el stream generado
        String stream = jh.sendJsonGET(urlString, idUser);
        //Si no esta vacio....
        if (stream != null) {
            try {
                // Get the full HTTP Data as JSONObject
                JSONObject reader = new JSONObject(stream);

                // Get the JSONObject "User"...........................
                JSONArray users = reader.getJSONArray("images");

                for (int i = 0; i < users.length(); i++) {
                    //JSONArray innerJa = users.getJSONArray(i);
                    JSONObject jo = users.getJSONObject(i);
                    idImage = jo.getString("idImage");
                    //Guardamos las ids en una array
                    //idImage = idImg[i];
                    idImg[i] = idImage;
                    Log.d(TAG+" idImage", idImage);
                }//fin for

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG + " ", e.getMessage());
            }

        }//fin if
    }//fin getIdImages

    private void getIdImage() {

    }

    //------------------AUXILIARES

    private void loadName(TextView tv){


    }


    //Clase para gestionar la conexion a SQL Server
    private class SQLServer extends AsyncTask<String, Void, Void> {
        //private SQLServer sqlc = new SQLServer();

        @Override
        protected Void doInBackground(String... params) {

                connection();

            return null;
        }
    }

    public String connection() {
        //
        String SQL;
        try {
            //
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionURL = "jdbc:sqlserver://INMA-PC\\sqlexpress:1433;"+
                    "database=Prueba;"+
                    "user=apps;"+
                    "password=apps1994";
            Connection con = DriverManager.getConnection(connectionURL);
            Log.d(TAG + " Connection", con.toString());

            //Create and execute an SQL statement that returns some data.
            SQL = "SELECT * FROM Prueba.dbo.Usuarios;";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            Log.d(TAG+" ResultSet", rs.toString());
            //Iterate through data
            while (rs.next()) {
                str = rs.getNString(1);
                Log.d(TAG+" IterateData", rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG+" Exception", e.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG+" Exception", e.toString());
        }

        return str;

    }//fin metodo
}
