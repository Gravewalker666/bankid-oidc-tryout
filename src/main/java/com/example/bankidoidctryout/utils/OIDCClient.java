package com.example.bankidoidctryout.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class OIDCClient {
    private static OIDCClient oidcClient;

    public static OIDCClient getInstance() {
        if (oidcClient != null) return oidcClient;
        return new OIDCClient();
    }

    private String authenticationUrl;

    public OIDCClient() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(Configurations.CONFIG_URL).request().get();
        JsonObject configurations = new Gson().fromJson(response.readEntity(String.class), JsonObject.class);
        this.authenticationUrl = configurations.get("authorization_endpoint").getAsString();
    }

    public String getAuthenticationUrl() throws IOException {
        Properties properties = new Properties();
        properties.load(OIDCClient.class.getResourceAsStream("/application.properties"));
        String clientId = encode(properties.getProperty("client-id"));
        String redirectedUrl = encode(properties.getProperty("redirect-url"));
        return String.format(
                "%s?client_id=%s" +
                "&scope=%s" +
                "&redirect_uri=%s" +
                "&response_type=%s",
                authenticationUrl, clientId, "openid+profile", redirectedUrl, "code"
        );
    }

    private String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }
}
