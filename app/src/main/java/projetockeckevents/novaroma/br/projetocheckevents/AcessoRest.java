package projetockeckevents.novaroma.br.projetocheckevents;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by yurifws on 8/31/16.
 */
public class AcessoRest {

    private static final String IP = "172.20.10.14";
    private static final String PORT ="8080";
    private static final StringBuilder url = new StringBuilder()
                                            .append("http://")
                                            .append(IP)
                                            .append(":")
                                            .append(PORT)
                                            .append("/checkEvents/");

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String urlComplemento, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(url.toString() + urlComplemento, params, responseHandler);
    }

    public static void post(String urlComplemento, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(url.toString() + urlComplemento, params, responseHandler);
    }

    public static void post(Context context, String urlComplemento, HttpEntity entity, String contentType,  AsyncHttpResponseHandler responseHandler){
        client.put(context, url.toString() + urlComplemento, entity, contentType, responseHandler);
    }

    public static void put(String urlComplemento, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.put(url.toString() + urlComplemento, params, responseHandler);
    }

    public static void put(Context context, String urlComplemento, HttpEntity entity, String contentType,  AsyncHttpResponseHandler responseHandler){
        client.put(context, url.toString() + urlComplemento, entity, contentType, responseHandler);
    }

    public static void delete(String urlComplemento, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.delete(url.toString() + urlComplemento, params, responseHandler);
    }



}
