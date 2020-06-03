package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/poll-data")
public class PollDataServlet extends HttpServlet {

  private Map<String, Integer> pollVotes = new HashMap<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(pollVotes);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String poll = request.getParameter("poll");
    int currentVotes = pollVotes.containsKey(poll) ? pollVotes.get(poll) : 0;
    pollVotes.put(poll, currentVotes + 1);

    response.sendRedirect("/index.html");
  }
}