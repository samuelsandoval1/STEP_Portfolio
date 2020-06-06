package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "UserAPI",
    description = "UserAPI: Login / Logout with UserService",
    urlPatterns = "/userapi"
)
public class UserServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LinkedHashMap<String, String> Url_and_Bool = new LinkedHashMap<>();

        UserService userService = UserServiceFactory.getUserService();
        String thisUrl = request.getRequestURI();
        String url;
        String isLoggedIn;

        if(!userService.isUserLoggedIn()) {
            url = userService.createLoginURL("/index.html");
            isLoggedIn = "false"; 
         }
        else {
            url = userService.createLogoutURL("/index.html");
            isLoggedIn = "true";
         }
        Url_and_Bool.put("Url", url);
        Url_and_Bool.put("Bool", isLoggedIn);
        
        Gson gson = new Gson();
        String Url_and_Bool_Json = gson.toJson(Url_and_Bool);

        response.setContentType("application/json;");
        response.getWriter().println(Url_and_Bool_Json);
    }
}