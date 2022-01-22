package com.twitch.twitchproject.controller;

import com.twitch.twitchproject.entity.db.Item;
import com.twitch.twitchproject.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    private GameService gameService;

    @Autowired
    public SearchController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody // auto convert object to json
    public Map<String, List<Item>> search(@RequestParam(value = "game_id") String game_id) {
        return gameService.searchItems(game_id);
    }
}
