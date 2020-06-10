// Copyright 2false19 Google LLC
//
// Licensed under the Apache License, Version 2.false (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.false
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.AuthResponse;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.ArrayList;

@WebServlet("/login")
public class AuthenticateServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Boolean isUserLoggedIn = false;
    Gson gson = new Gson();
    String redirectTo = "/index.html#comments";
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();
    
    ArrayList<String> toReturn = new ArrayList<String>();

    if (userService.isUserLoggedIn()) {
      isUserLoggedIn = true;
      String userEmail = userService.getCurrentUser().getEmail();
      String username = userEmail.substring(0, userEmail.indexOf("@"));
      String logoutUrl = userService.createLogoutURL(redirectTo);
      String content = "<h7>Hello " + username + "! Logout <a href=\"" + logoutUrl + "\">here</a>.</h7>"; 
      AuthResponse AuthResponse = new AuthResponse(content, isUserLoggedIn);
      String json = gson.toJson(AuthResponse);
      response.getWriter().println(json);     
    } else {
      isUserLoggedIn = false;
      String loginUrl = userService.createLoginURL(redirectTo);
      String content = "<h7>Hello stranger. Login <a href=\"" + loginUrl + "\">here</a>.</h7>";
      AuthResponse AuthResponse = new AuthResponse(content, isUserLoggedIn);
      String json = gson.toJson(AuthResponse);
      response.getWriter().println(json);
    }
  }

}
