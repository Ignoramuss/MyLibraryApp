package com.ignoramuss.foo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mayan_000 on 03-Mar-17.
 */


class AsyncRequest extends AsyncTask<String, Integer, byte[]> {

    OnAsyncRequestComplete caller;
    final static int BUFFER_SIZE = 8192;
    Context context;
    String method = "GET";

    // Two Constructors

    public AsyncRequest() {

    }

    public AsyncRequest(String m) {
        method = m;
    }

//    public AsyncRequest(Activity a, String m) {
//        caller = (OnAsyncRequestComplete) a;
//        context = a;
//        method = m;
//    }
//
//    public AsyncRequest(Activity a) {
//        caller = (OnAsyncRequestComplete) a;
//        context = a;
//    }

    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(InputStreamReader response);
    }

    public byte[] doInBackground(String... urls) {
        // get url pointing to entry point of API
        String address = urls[0].toString();
        if (method == "POST") {
            return post(address);
        }

        if (method == "GET") {
            return get(address);
        }

        return null;
    }

    public void onPreExecute() {
    }

    public void onProgressUpdate(Integer... progress) {
        // you can implement some progressBar and update it in this record
        // setProgressPercent(progress[0]);
    }


    //     public void onPostExecute(InputStreamReader istream) {
////         caller.asyncResponse(istream);
//
//     }
    public void onPostExecute(byte[] data){
        try{
            String response = new String(data, "UTF-8");
            Log.e("HEREE", response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    protected void onCancelled(InputStreamReader istream) {
//
//        caller.asyncResponse(istream);
//    }


    public byte[] get(String address) {
        // Do some validation here
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream istream = urlConnection.getInputStream();
                return readFully(istream);
//                return istream;
//                return stringifyResponse(istream);
            }
            finally{
                urlConnection.disconnect();
            }
        } catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public byte[] post(String address) {
        try {

            String urlParameters  = "param1=a&param2=b&param3=c";
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            String request        = "http://example.com/index.php";
            URL    url            = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }
            InputStream istream = conn.getInputStream();
            return readFully(istream);
//            return istream;
//            return stringifyResponse(istream);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

    }

    private String stringifyResponse(InputStreamReader istream) {
        String stringContent = "";
        try{
            BufferedReader bufferedReader = new BufferedReader(istream);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            stringContent = stringBuilder.toString();
        } catch (Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }
        finally {
            return stringContent;
        }
    }

    public static byte[] readFully(InputStream input) throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

}