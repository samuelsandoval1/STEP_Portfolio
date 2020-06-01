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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private static final Gson gson = new Gson();

  private int maxComments = 5;


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Comment> comments = fetchComments();

    response.setContentType("application/json;");
    String json = gson.toJson(comments);
    response.getWriter().println(json);
  }

    private List<Comment> fetchComments() {
        Query query = new Query("Comment").addSort("timeStamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        List<Entity> resultsList = results.asList(FetchOptions.Builder.withLimit(maxComments));

        List<Comment> comments = new ArrayList<>();
        for (Entity entity : resultsList) {
            long id = entity.getKey().getId();
            String author = (String) entity.getProperty("author");
            String comment = (String) entity.getProperty("comment");
            Date timestamp = (Date) entity.getProperty("timeStamp");

            Comment commentObject = new Comment(id, author, comment, timestamp);
            comments.add(commentObject);
        }
        return comments;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String comment = request.getParameter("comment-text");
        storeComments(comment);

        response.sendRedirect("/index.html");
    }

    private void storeComments(String comment) {
        String author = "user";
        
        LocalDateTime localDate = LocalDateTime.now();

        Date timeStamp = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("author", author);
        commentEntity.setProperty("comment", comment);
        commentEntity.setProperty("timeStamp", timeStamp);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
    }
}