package com.example.bankidoidctryout;

import com.example.bankidoidctryout.utils.OIDCClient;
import com.example.bankidoidctryout.utils.TokenWrapper;
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
        TokenWrapper tokens = OIDCClient.getInstance().makeTokenRequest(code);
        request.getSession().setAttribute("tokens", tokens);
        response.sendRedirect(request.getContextPath() + "/vas");
        // TODO: redirect user to a place and make the call to retrieve the info using access token
    }
}
