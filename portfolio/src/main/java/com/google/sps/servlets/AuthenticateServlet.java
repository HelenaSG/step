// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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

    int status = 0;
    Gson gson = new Gson();
    ArrayList<String> toReturn = new ArrayList<String>();

    String redirectTo = "/index.html";
    response.setContentType("application/json");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String logoutUrl = userService.createLogoutURL(redirectTo);

      status = 1;
      toReturn.add("<p>Hello " + userEmail + "!</p>");
      toReturn.add("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
      
    } else {
      String loginUrl = userService.createLoginURL(redirectTo);
      
      status = 0;
      toReturn.add("<p>Hello stranger.</p>");
      toReturn.add("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
    
    toReturn.add(Integer.toString(status));
    String json = gson.toJson(toReturn);
    response.getWriter().println(json);

  }
}
