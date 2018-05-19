package com.Appisos.apps.Auxiliar;

/**
 * Created by apps on 15/10/2015.
 */
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
//import sun.misc.BASE64Encoder;
//import sun.misc.BASE64Decoder;

public class Encriptado {

    /*public static void main(String[] args) {
        try {
            System.out.println("Obteniendo generador de claves con cifrado AES");
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            System.out.println("Generando clave");
            SecretKey key = keygen.generateKey();
            System.out.println("Obteniendo objeto Cipher con cifrado AES");
            Cipher aesCipher = Cipher.getInstance("AES");
            System.out.println("Configurando Cipher para encriptar");
            aesCipher.init(Cipher.ENCRYPT_MODE, key);
            System.out.println("Preparando mensaje");
            String mensaje = "Mensaje que se cifrara con AES";
            System.out.println("Mensaje original: "+mensaje);
            System.out.println("Cifrando mensaje");
            //
            byte[] cifrado = new byte[64];
            cifrado = aesCipher.doFinal(mensaje.getBytes());
            String mensajeCifrado = new String(cifrado);
            System.out.println("Mensaje cifrado: "+mensajeCifrado);
            //
            System.out.println("Configurando Cipher para desencriptar");
            aesCipher.init(Cipher.DECRYPT_MODE, key);
            System.out.println("Descifrando mensaje");
            String mensajeDescifrado = new String(aesCipher.doFinal(cifrado));
            System.out.println("Mensaje descifrado: "+mensajeDescifrado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//fin main*/

    //Encriptado y desencriptado usando AES
    public SecretKey encriptar(String mensaje) throws Exception {

        //Obteniendo generador de claves con cifrado AES
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //Generando clave
        SecretKey key = keygen.generateKey();
        //Obteniendo objeto Cipher con cifrado AES
        Cipher aesCipher = Cipher.getInstance("AES");
        //Configurando Cipher para encriptar
        aesCipher.init(Cipher.ENCRYPT_MODE, key);
        //Preparando mensaje
        mensaje = "Mensaje que se cifrara con AES";
        //System.out.println("Mensaje original: "+mensaje);
        //Cifrando mensaje
        //
        byte[] cifrado = new byte[64];
        cifrado = aesCipher.doFinal(mensaje.getBytes());
        String mensajeCifrado = new String(cifrado);
        //System.out.println("Mensaje cifrado: "+mensajeCifrado);
        //
        return key;
    }

    public String desencriptar(String mensaje, SecretKey key) throws Exception{

        Cipher aesCipher = Cipher.getInstance("AES");
        byte[] cifrado = new byte[64];
        cifrado = aesCipher.doFinal(mensaje.getBytes());
        //Configurando Cipher para desencriptar
        aesCipher.init(Cipher.DECRYPT_MODE, key);
        //Descifrando mensaje
        String mensajeDescifrado = new String(aesCipher.doFinal(cifrado));
        //System.out.println("Mensaje descifrado: "+mensajeDescifrado);

        return mensajeDescifrado;
    }

    //Encriptado y desencriptado usando Blowfish
    //Extraidos de edwin.baculsoft.com/2013/02/a-simple-blowfish-encryption-decryption-using-java/
    public static String encriptarBlow(String password) throws Exception {
        byte[] keyData = (password).getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] data = cipher.doFinal(password.getBytes());
        //String mensajeCifrado = new String(new BASE64Encoder().encode(hasil));
        String mensajeCifrado = Base64.encodeToString(data, Base64.DEFAULT);
        return mensajeCifrado;
    }

    //Se le pasan dos parametros el primero el pass introducido por el usuario
    //en la caja de texto y luego el pass recibido de la BD
    public static String desencriptarBlow(String passUser, String passDB) throws Exception {
        byte[] keyData = (passUser).getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] data = Base64.decode(passDB, Base64.DEFAULT);
        String pass = new String(data, "UTF-8");
        //byte[] hasil = cipher.doFinal(new BASE64Decoder().decodeBuffer(passDB));
        //return hasil.toString();
        return pass;
    }
}//fin class

