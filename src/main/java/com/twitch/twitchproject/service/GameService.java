package com.twitch.twitchproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchproject.entity.response.Game;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Service
public class GameService {
    private static final String TOKEN = "Bearer goqd6l19hxu5pymtv9tj8l38w9svhn";
    private static final String CLIENT_ID = "t5qhyqv738nzwk0uqs1770gik026ac";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;

    // method for build URL
    private String buildGameURL(String url, String gameName, int limit) {
        if (gameName.equals("")) { // get top
            return String.format(url, limit); // replace %s
        } else { // get specific game
            try {
                // Encode special characters in URL like space
                gameName = URLEncoder.encode(gameName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return String.format(url, gameName);
        }
    }

    // define how we use Twitch API and how we handle the response
    private String searchTwitch(String url) throws TwitchException {
        CloseableHttpClient httpclient = HttpClients.createDefault(); // create an object to help do http request

        // Define the response handler to parse and return HTTP response body returned from Twitch
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                int responseCode = httpResponse.getStatusLine().getStatusCode(); // get response status code
                if (responseCode != 200) { // check if the response is valid
                    System.out.println("Response status: " + httpResponse.getStatusLine().getReasonPhrase()); // print out the reason
                    throw new TwitchException("Failed to get result from Twitch API");
                }
                HttpEntity entity = httpResponse.getEntity(); // get response
                if (entity == null) { // the response may be empty
                    throw new TwitchException("Failed to get result from Twitch API");
                }
                JSONObject obj = new JSONObject(EntityUtils.toString(entity)); // put response entity to String and then convert to JSON
                return obj.getJSONArray("data").toString(); // use getJSONArray with key to get target value and put it to string
            }
        };

        try {
            // Define the HTTP request, TOKEN and CLIENT_ID are used for user authentication on Twitch backend
            HttpGet request = new HttpGet(url); // create http get request based on url, we are client for Twitch
            request.setHeader("Authorization", TOKEN); // set up header
            request.setHeader("Client-Id", CLIENT_ID); // set up header
            return httpclient.execute(request, responseHandler); // execute http request and return the Sting if succeed
        } catch (IOException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to get result from Twitch API");
        } finally {
            try {
                httpclient.close(); // make sure we close http
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // based on data we get from Twitch, put into Game class
    private List<Game> getGameList(String data) throws TwitchException {
        ObjectMapper mapper = new ObjectMapper(); // jackson
        try {
            return Arrays.asList(mapper.readValue(data, Game[].class)); // readValue can read data based on later class type, and we already define how to map with JSON in Game class
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new TwitchException("Failed to parse game data from Twitch API");
        }
    }

    // now we are server
    // get top games
    public List<Game> topGames(int limit) throws TwitchException {
        if (limit <= 0) {
            limit = DEFAULT_GAME_LIMIT;
        }
        return getGameList(searchTwitch(buildGameURL(TOP_GAME_URL, "", limit)));
    }

    // get specific info for a game
    public Game searchGame(String gameName) throws TwitchException {
        List<Game> gameList = getGameList(searchTwitch(buildGameURL(GAME_SEARCH_URL_TEMPLATE, gameName, 0)));
        if (gameList.size() != 0) {
            return gameList.get(0);
        }
        return null;
    }
}
