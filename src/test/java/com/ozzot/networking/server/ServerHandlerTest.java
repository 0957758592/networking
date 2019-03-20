package com.ozzot.networking.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServerHandlerTest {

    private String jsonRequestFalse = "{\"id\":2,\"type\":\"getById\"";
    private String jsonRequestTrue = "{\"id\":2,\"type\":\"getById\"}";

    @Test
    public void JsonNotValid() {
        assertFalse(ServerHandler.isJsonValid(jsonRequestFalse));
    }

    @Test
    public void JsonIsValid() {
        assertTrue(ServerHandler.isJsonValid(jsonRequestTrue));
    }
}