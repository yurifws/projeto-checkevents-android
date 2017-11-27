package projetockeckevents.novaroma.br.projetocheckevents.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by yurifws on 9/24/16.
 */

public final class Helper {

    public  static String codificar(String string){
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public  static String codificar(String string, String enc){
        try {
            return URLEncoder.encode(string, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public  static String decodificar(String string){
        try {
            return URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public  static String decodificar(String string, String enc){
        try {
            return URLDecoder.decode(string, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
