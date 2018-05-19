package com.Appisos.apps.DB;

/**
 * Created by apps on 07/10/2015.
 */
import android.util.Log;
import java.sql.*;

public class SQLServer {
    private String TAG = "Appisos";
    private String SQL;
    private String str;

    public String connection() throws ClassNotFoundException, SQLException{
        //
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        String connectionURL = "jdbc:sqlserver://INMA-PC\\sqlexpress:1433;"+
                "database=Prueba;"+
                "user=apps;"+
                "password=apps1994";
        Connection con = DriverManager.getConnection(connectionURL);
        Log.d(TAG+" Connection", con.toString());

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

        return str;

    }//fin metodo
}//fin clase
