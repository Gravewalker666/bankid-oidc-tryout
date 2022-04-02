package com.example.bankidoidctryout;

import com.example.bankidoidctryout.utils.OIDCClient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectionUrl = OIDCClient.getInstance().getAuthenticationUrl();
        response.sendRedirect(redirectionUrl);
    }
}
