package com.Appisos.apps.Img;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import com.Appisos.apps.DB.JsonHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apps on 15/10/2015.
 */
public class GestionImagenes {
    private static String TAG = "Appisos";

    public ArrayList<String> getImgUrl(String idUser) {
        //Variables
        //No necesitamos estas dos variables, se usan para aprovechar los metodos
        //ya existentes
        String nameUser = "",passUser="";
        String stream = "";
        String imgName = "";
        //ArrayList para capturar los nombres de las imagenes
        ArrayList<String> img = new ArrayList<String>();
        //List<String> img = new ArrayList<>();
        //ArrayList para guardar las urls de las imagenes
        ArrayList<String> url = new ArrayList<String>();        
        //List<String> url = new ArrayList<>();
        String urlString = "http://servidordeprueba1.tk/test/getImgName.php";
        //String urlString = "http://172.26.0.15/appisos/getImgName.php";
        String urlImagen = "";
        JsonHelper jh = new JsonHelper();
        //Logica
        try {
            //Enviamos el id del usuario actual y obtenemos el flujo de datos
            stream = jh.sendJsonGET(urlString,idUser);
            //Obtenemos el nombre de la imagen a partir del flujo de datos
            img = getImgName(stream);
            for (int i=0;i<img.size();i++) {
                imgName = img.get(i);
                Log.d(TAG + " imgName getImgUrl", imgName);
                //Finalmente concatenamos los Strings para obtener la URL para descargar la imagen
                urlImagen = "/images/"+idUser+"/img_upload/"+imgName;
                Log.d(TAG + " urlImagen", urlImagen);
                //Guardamos la url en el ArrayList incrementando el indice
                url.add(i,urlImagen);
            }
            //Devolvemos la URL para descarga la imagen
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return ;
        return url;
    }//fin getImgUrl

    public static ArrayList<String> getImgName(String stream) {
        String imgName = "";
        //ArrayList para guardar los nombres de las imagenes
        ArrayList<String> img = new ArrayList<String>();

        //Tratamos el objeto JSON para extraer el nombre de la imagen
        try {
            if (stream != null) {

                JSONObject reader = new JSONObject(stream);

                JSONArray image = reader.getJSONArray("image");

                for (int i=0;i<image.length();i++) {
                    JSONObject jo = image.getJSONObject(i);
                    imgName = jo.getString("imgName");
                    Log.d(TAG+" imgName getImgName", imgName);
                    //Guardamos en el ArrayList el nombre de la imagen
                    img.add(i,imgName);
                    Log.d(TAG + " img getImgName", img.toString());
                }//fin for

            }//fin if
        } catch (JSONException e) {
            e.getMessage();
        }
        return img;
    }//fin getImgName

    //Carga una imagen desde una url a un ImageView
    public void loadImage(Context context, String url, ImageView iv){

        Picasso.with(context)
                .load(url)
                .centerCrop()
                .into(iv);
    }//fin

    //public void

    //A partir de una url y usando la clase que crea una forma circular
    //convierte una imagen en forma circular
    public void circleImage(Activity activity, String url,ImageView iv) {

        Picasso.with(activity)
                .load(url)
                .transform(new CircleTransform())
                .into(iv);
    }

    //Clase que transforma una imagen en circulo
    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}//fin clase
