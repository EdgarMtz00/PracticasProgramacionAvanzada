package com.company.connection;

import com.google.gson.JsonObject;

public interface RequestEvent {
    JsonObject execute();

    void onComplete(JsonObject response);
}
