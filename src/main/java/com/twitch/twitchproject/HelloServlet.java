package com.twitch.twitchproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchproject.entity.response.Game;
import org.json.JSONObject;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-game")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get parameter & get writer
        // String gamename = request.getParameter("gamename");
        // response.getWriter().print("Game is " + gamename);

        // return JSON
        // response.setContentType("application/json");
        // JSONObject game = new JSONObject();
        // String gamename = request.getParameter("gamename");
        // game.put("name", gamename);
        // response.getWriter().print(game);

        // jackson
        // response.setContentType("application/json");
        // String gamename = request.getParameter("gamename");
        // ObjectMapper mapper = new ObjectMapper();
        // Game.Builder builder = new Game.Builder();
        // builder.setName(gamename);
        // builder.setDeveloper("Blizzard Entertainment");
        // builder.setReleaseTime("Feb 11, 2005");
        // builder.setWebsite("https://www.worldofwarcraft.com");
        // builder.setPrice(49.99);
        // Game game = builder.build();
        // response.getWriter().print(mapper.writeValueAsString(game));
    }

    public void destroy() {
    }
}