package com.example.bankidoidctryout;

import com.example.bankidoidctryout.utils.OIDCClient;
import com.example.bankidoidctryout.utils.TokenWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

@WebServlet("/vas")
public class GetVASServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TokenWrapper tokens = (TokenWrapper) request.getSession().getAttribute("tokens");
        if (tokens == null) {
            response.sendRedirect(request.getContextPath());
            return;
        };
        String idTokenPayloadString = new String(Base64.getDecoder().decode(tokens.getIdToken().split("\\.")[1]));
        JsonObject idTokenPayload = new Gson().fromJson(idTokenPayloadString, JsonObject.class);
        System.out.println(tokens.getAccessToken());
        JsonObject userInfoPayload = OIDCClient.getInstance().makeUserInfoRequest(tokens.getAccessToken());
        response.setContentType("text/html");
        response.getWriter()
                .append("<h3>Information in the id token payload </br> Scope: profile nnin_altsub</h3>")
                .append("<p> nnin: ")
                .append(idTokenPayload.get("nnin_altsub").getAsString())
                .append("</p>")
                .append("<p> name: ")
                .append(idTokenPayload.get("name").getAsString())
                .append("</p>")
                .append("<p> givenName: ")
                .append(idTokenPayload.get("given_name").getAsString())
                .append("</p>")
                .append("<p> familyName: ")
                .append(idTokenPayload.get("family_name").getAsString())
                .append("</p>")
                .append("<p> birthDate: ")
                .append(idTokenPayload.get("birthdate").getAsString())
                .append("</p>")
                .append("<h3>Information retrieved from the user info VAS service </br> Scope: address phone email</h3>")
                .append("<p> phone: ")
                .append(userInfoPayload.get("phone_number").getAsString())
                .append("</p>");
    }
}
