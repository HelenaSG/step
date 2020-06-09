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

import com.google.appengine.api.datastore.*;
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
    String redirectTo = "/index.html#comments";
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();
    
    ArrayList<String> toReturn = new ArrayList<String>();

    if (userService.isUserLoggedIn()) {
      status = 1;
      String userEmail = userService.getCurrentUser().getEmail();
      String username = userEmail.substring(0, userEmail.indexOf("@"));
      String logoutUrl = userService.createLogoutURL(redirectTo);
      toReturn.add("<h7>Hello " + username + "!</h7>");
      toReturn.add("<h7>Logout <a href=\"" + logoutUrl + "\">here</a>.</h7>");
      
    } else {
      status = 0;
      String loginUrl = userService.createLoginURL(redirectTo);
      toReturn.add("<h7>Hello stranger.</h7>");
      toReturn.add("<h7>Login <a href=\"" + loginUrl + "\">here</a>.</h7>");
    }
    
    toReturn.add(Integer.toString(status));
    String json = gson.toJson(toReturn);
    response.getWriter().println(json);
  }

}
