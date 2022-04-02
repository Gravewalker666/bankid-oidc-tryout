package com.example.bankidoidctryout;

import com.example.bankidoidctryout.utils.OIDCClient;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/callback")
public class CallbackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        JsonObject jsonObject = OIDCClient.getInstance().makeTokenRequest(code);
        request.getSession().setAttribute("tokenResponse", jsonObject);
        response.sendRedirect("/");
        // TODO: redirect user to a place and display the contents of json object
    }
}
