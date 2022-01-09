package com.twitch.twitchproject.entity.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestBody {
    private final String userId;
    private final String password;

    @JsonCreator // request is different with response, in request we need deserialization using RequestBody from spring, so we need to assign the constructor we want to use for deserialization
    public LoginRequestBody(@JsonProperty("user_id") String userId, @JsonProperty("password") String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
