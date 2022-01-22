package com.twitch.twitchproject.service;

// create an exception to identify whether our code is wrong or the twitch api is wrong
public class TwitchException extends RuntimeException {
    public TwitchException(String errorMessage) {
        super(errorMessage);
    }
}