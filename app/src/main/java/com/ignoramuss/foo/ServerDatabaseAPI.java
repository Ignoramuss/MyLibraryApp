package com.ignoramuss.foo;

/**
 * Created by ignoramuss on 04-Mar-17.
 */

public class ServerDatabaseAPI {

    final private String SERVER_ADDRESS = BuildConfig.serverAPIAddress;

    public ServerDatabaseAPI(){

    }

    public void store(String information){
        String service = SERVER_ADDRESS + "store_in_database.py?payload=" + information;
        AsyncRequest server_call = new AsyncRequest(){
            @Override
            public void onPostExecute(byte[] data) {
                super.onPostExecute(data);
            }
        };
        server_call.execute(service);
    }

    public void getBooks(){
        String service = SERVER_ADDRESS + "retrieve_from_database.py?username=";
        AsyncRequest server_call = new AsyncRequest(){
            @Override
            public void onPostExecute(byte[] data) {
                super.onPostExecute(data);
            }
        };
        server_call.execute(service);
    }
}
