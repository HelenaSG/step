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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;//import Comment class
import com.google.appengine.api.datastore.FetchOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for listing comments. */
@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    int max = 3;
    int userChoice = getUserChoice(request);
    if (userChoice != -1) {
        max = userChoice;
    }
    System.out.println("max"+max);

    Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    //PreparedQuery results = datastore.prepare(query);
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(max));
    
    //Get properties of the entity
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results){
      long id = entity.getKey().getId();
      String content = (String) entity.getProperty("content");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comment = new Comment(id, content, timestamp);
      comments.add(comment);
    }

    //Convert a List to JSON
    Gson gson = new Gson();
    String json = gson.toJson(comments);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  /** Returns the choice entered by the user, or -1 if the choice was invalid. */
  private int getUserChoice(HttpServletRequest request) {
    // Get the input from the form.
    String userChoiceString = request.getParameter("user-choice");
    System.out.println("userChoiceString"+userChoiceString);
    // Convert the input to an int.
    int userChoice;
    try {
      userChoice = Integer.parseInt(userChoiceString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + userChoiceString);
      return -1;
    }

    // Check that the input is between 1 and 50.
    if (userChoice < 1 || userChoice > 50) {
      System.err.println("user choice is out of range: " + userChoiceString);
      return -1;
    }

    return userChoice;
  }
  
}
