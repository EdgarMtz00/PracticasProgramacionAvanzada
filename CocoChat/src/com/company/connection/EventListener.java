package com.company.connection;

import com.google.gson.JsonObject;

public interface EventListener {
    boolean shouldReceiveMessage(JsonObject response);

    void execute(JsonObject response);
}
