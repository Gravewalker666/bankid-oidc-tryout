package com.example.bankidoidctryout.utils;

public class TokenWrapper {
    private final String accessToken;
    private final String idToken;

    public TokenWrapper(String accessToken, String idToken) {
        this.accessToken = accessToken;
        this.idToken = idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getIdToken() {
        return idToken;
    }
}
