package com.company.connection;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSocket {
    private static final String serverIp = "127.0.0.1";
    private static final int serverPort = 5002;
    private Socket client;
    private PrintWriter out;
    private ResponseListener mListener;

    public ClientSocket(){
        try {
            client = new Socket(serverIp, serverPort);
            out = new PrintWriter(
                    new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListener(ResponseListener mListener){
        this.mListener = mListener;
    }

    private boolean validateBytes(byte[] array){
        for (byte b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    public void request(String path, JsonObject jsonRequest){
        JsonObject data = new JsonObject();
        jsonRequest = (jsonRequest == null)? new JsonObject() : jsonRequest;
        data.addProperty("path", path);
        data.add("request", jsonRequest);
        new Thread(() -> {
            try {
                System.out.println(data.toString());
                out.println(data.toString());
                InputStream stream = client.getInputStream();
                while (true) {
                    if(stream.available() != 0){
                        byte[] b = new byte[stream.available()];
                        stream.read(b);
                        if(validateBytes(b)) {
                            String strResponse = new String(b, StandardCharsets.UTF_8);
                            JsonReader jsonReader = new JsonReader(new StringReader(strResponse));
                            jsonReader.setLenient(true);
                            Gson gson = new Gson();
                            JsonObject response = gson.fromJson(jsonReader, JsonObject.class);
                            mListener.callback(response);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}