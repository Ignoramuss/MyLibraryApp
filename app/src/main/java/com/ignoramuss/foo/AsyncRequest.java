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
 * Created by ignoramuss on 03-Mar-17.
 * Modification of Cyril T's code on AsyncTask from http://cyriltata.blogspot.in/2013/10/android-re-using-asynctask-class-across.html
 */

// The first parameterized type is the input to doInBackground().
// The second is the input to onProgressUpdate(). The third is the
// input to onPostExecute()
class AsyncRequest extends AsyncTask<String, Integer, byte[]> {

    final static int BUFFER_SIZE = 8192;
    byte[] result;
    private String method;

    // Default method to GET
    public AsyncRequest() {
        method = "GET";
    }

    // Explicitly specify HTTP method
    public AsyncRequest(String m) {
        method = m;
    }

    public void onPreExecute() {

    }

    // Input is the string url/urls sent through execute method of AsyncRequest
    public byte[] doInBackground(String... urls) {
        // get url
        String address = urls[0];
        if (method == "POST") {
            result =  post(address);
            return result;
        }

        if (method == "GET") {
            result =  get(address);
            return result;
        }

        return null;
    }

    public void onProgressUpdate(Integer... progress) {
        // you can implement some progressBar and update it in this record
        // setProgressPercent(progress[0]);
    }

    // Override this method using anonymous classes when you instantiate an object of AsyncRequest
    // to suit your need
    public void onPostExecute(byte[] data) {
        result = data;
    }

    protected void onCancelled(InputStreamReader istream) {

    }


    public byte[] get(String address) {
        // Do some validation here
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream istream = urlConnection.getInputStream();
                // read data in bytes from inputstream
                // treating byte data is left to onPostExecute method
                return readFully(istream);
            }
            finally{
                urlConnection.disconnect();
            }
        } catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    // Not configured yet. Template given. Also need to modify constructor for
    // taking parameters for get/post instead of hard coding url in the calling class
    public byte[] post(String address) {
        try {

            String urlParameters  = "param1=a&param2=b&param3=c";
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            String request        = "http://example.com/index.php";
            URL    url            = new URL( address );
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

    public byte[] fetchResult(){
        return result;
    }

}