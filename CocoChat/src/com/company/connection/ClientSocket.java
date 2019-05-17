package com.company.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que se encarga de la comunicacion con el usuario
 */
public class ClientSocket {
    private static final String serverIp = "127.0.0.1";
    private static final int serverPort = 5002;
    private Socket client;
    private PrintWriter out;
    private ReentrantLock lock = new ReentrantLock();
    private BlockingQueue<RequestEvent> listeners = new LinkedBlockingQueue<>();
    private ArrayList<EventListener> nonRemovableListeners = new ArrayList<>();

    public ClientSocket() {
        try {
            client = new Socket(serverIp, serverPort);
            out = new PrintWriter(
                    new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                InputStream stream = client.getInputStream();
                while (true) {
                    try {
                        if (listeners.size() != 0) {
                            RequestEvent event = listeners.take();
                            JsonObject data = event.execute();
                            out.write(data.toString());
                            out.flush();

                            byte[] b;
                            while (true) {
                                b = new byte[stream.available()];
                                stream.read(b);
                                if (!validateBytes(b)) {
                                    continue;
                                }
                                String strResponse = new String(b, StandardCharsets.UTF_8);
                                JsonReader jsonReader = new JsonReader(new StringReader(strResponse));
                                jsonReader.setLenient(true);
                                Gson gson = new Gson();
                                JsonObject response = gson.fromJson(jsonReader, JsonObject.class);
                                System.out.println(data.toString() + "|||||||||||||||||" + response.toString());
                                event.onComplete(response);
                                break;
                            }
                        } else if (stream.available() != 0){
                            byte[] b = new byte[stream.available()];
                            stream.read(b);
                            if (!validateBytes(b)) {
                                continue;
                            }
                            lock.lock();
                            String strResponse = new String(b, StandardCharsets.UTF_8);
                            JsonReader jsonReader = new JsonReader(new StringReader(strResponse));
                            jsonReader.setLenient(true);
                            Gson gson = new Gson();
                            JsonObject response = gson.fromJson(jsonReader, JsonObject.class);
                            for (EventListener listener : nonRemovableListeners) {
                                if (listener.shouldReceiveMessage(response)) {
                                    listener.execute(response);
                                }
                            }

                            lock.unlock();
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Comprueba que un arreglo de bytes no este inicializado en 0
     * @param array
     * @return
     */
    private boolean validateBytes(byte[] array) {
        if (array.length == 0) {
            return false;
        }
        for (byte b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Realiza una peticion al usuario
     * @param path ruta del servidor
     * @param jsonRequest informacion de la peticion
     * @param listener Funcion a llamar cuando se obtenga una respuesta
     */
    public void request(String path, JsonObject jsonRequest, ResponseListener listener) {
        JsonObject data = new JsonObject();
        if (jsonRequest == null) {
            jsonRequest = new JsonObject();
            jsonRequest.addProperty("foo", "foo");
        }
        data.addProperty("path", path);
        data.add("request", jsonRequest);

        listeners.add(new RequestEvent() {
            @Override
            public JsonObject execute() {
                return data;
            }

            @Override
            public void onComplete(JsonObject response) {
                listener.callback(response);
            }
        });

        /*
        new Thread(() -> {
            try {
                lock.lock();
                out.println(data.toString());
                InputStream stream = client.getInputStream();
                while (true) {
                    if (stream.available() != 0) {
                        byte[] b = new byte[stream.available()];
                        stream.read(b);
                        if (path.equals("")) {
                            lock.unlock();
                            return;
                        }
                        if (validateBytes(b)) {
                            String strResponse = new String(b, StandardCharsets.UTF_8);
                            JsonReader jsonReader = new JsonReader(new StringReader(strResponse));
                            jsonReader.setLenient(true);
                            Gson gson = new Gson();
                            JsonObject response = gson.fromJson(jsonReader, JsonObject.class);
                            System.out.println(data.toString() + "|||||||||||||||||" + response.toString());
                            mListener.callback(response);
                            lock.unlock();
                            break;
                        }
                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        */
    }

    public void addEventListener(EventListener listener) {
        lock.lock();
        nonRemovableListeners.add(listener);
        lock.unlock();
    }

    public void removeEventListener(EventListener listener) {
        nonRemovableListeners.remove(listener);
    }
}