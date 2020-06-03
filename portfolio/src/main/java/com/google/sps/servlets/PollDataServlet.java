package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/poll-data")
public class PollDataServlet extends HttpServlet {

  private LinkedHashMap<Integer, Integer> bigfootSightings = new LinkedHashMap<>();
  private String bigfootSightingsJson;

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/bigfoot-sightings-by-year.csv"));

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      if(cells.length > 0) {
        if(cells[0] != "" && cells[1] != ""){
          Integer year = Integer.valueOf(cells[0]);
          Integer sightings = Integer.valueOf(cells[1]);
          bigfootSightings.put(year, sightings);
        }
      } 
    }
    Gson gson = new Gson();
    bigfootSightingsJson = gson.toJson(bigfootSightings);
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().println(bigfootSightingsJson);
  }
}