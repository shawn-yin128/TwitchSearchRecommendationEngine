package com.twitch.twitchproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchproject.entity.response.Game;
import com.twitch.twitchproject.service.GameService;
import com.twitch.twitchproject.service.TwitchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class GameController {
    private GameService gameService;

    // /game?game_name=whatever
    // /game

    @Autowired
    public GameController(GameService gameService){ // use Spring to inject object of service
        this.gameService = gameService;
    }

    // define the get API
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public void getGame(@RequestParam(value = "game_name", required = false) String gameName, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Return the dedicated game information if gameName is provided in the request URL, otherwise return the top x games.
            if (gameName != null) { // no game name so we get top games
                response.getWriter().print(new ObjectMapper().writeValueAsString(gameService.searchGame(gameName)));
            } else { // have game name then we get info for this specific game
                response.getWriter().print(new ObjectMapper().writeValueAsString(gameService.searchTopGames(0)));
            }
        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }
}
