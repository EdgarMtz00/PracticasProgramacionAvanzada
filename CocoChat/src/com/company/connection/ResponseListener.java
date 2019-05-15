package com.company.connection;

import com.google.gson.JsonObject;

public interface ResponseListener {
    void callback(JsonObject response);
}
