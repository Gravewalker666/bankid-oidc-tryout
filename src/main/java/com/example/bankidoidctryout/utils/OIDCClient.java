package com.example.bankidoidctryout.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Logger;

public class OIDCClient {
    private static OIDCClient oidcClient;
    private static final Logger logger = Logger.getLogger(OIDCClient.class.getName());

    public static OIDCClient getInstance() throws IOException {
        if (oidcClient != null) return oidcClient;
        return new OIDCClient();
    }

    private final String authenticationUrl;
    private final String tokenUrl;
    private final Properties properties;

    public OIDCClient() throws IOException {
        properties = new Properties();
        properties.load(OIDCClient.class.getResourceAsStream("/application.properties"));

        Client client = ClientBuilder.newClient();
        Response response = client.target(Configurations.CONFIG_URL).request().get();
        JsonObject configurations = new Gson().fromJson(response.readEntity(String.class), JsonObject.class);
        this.authenticationUrl = configurations.get("authorization_endpoint").getAsString();
        this.tokenUrl = configurations.get("token_endpoint").getAsString();
    }

    public String getAuthenticationUrl() {
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

    public TokenWrapper makeTokenRequest(String code) {
        String clientId = properties.getProperty("client-id");
        String clientSecret = properties.getProperty("client-secret");
        String redirectUrl = properties.getProperty("redirect-url");

        MultivaluedHashMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUrl);

        Client client = ClientBuilder.newClient();
        Response response = client.target(tokenUrl).request().header(
                "Authorization", "Basic " +
                        Base64.getEncoder().encodeToString(
                                (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)
                        )
        ).post(Entity.form(formData));
        JsonObject responseBody = new Gson().fromJson(response.readEntity(String.class), JsonObject.class);
        logger.info(responseBody.toString());
        return new TokenWrapper(
                responseBody.get("access_token").toString(),
                responseBody.get("id_token").getAsString()
        );
    }

    private String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }
}
