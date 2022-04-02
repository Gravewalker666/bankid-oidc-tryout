package com.example.bankidoidctryout;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter()
                .append("<html>")
                .append("<head>")
                .append("</head>")
                .append("<body>")
                .append("<a href=\"login\">Sign in with BankId</a>")
                .append("</body>")
                .append("</html>");
    }
}
