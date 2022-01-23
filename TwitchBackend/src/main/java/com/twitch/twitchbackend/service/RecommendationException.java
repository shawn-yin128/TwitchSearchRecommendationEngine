package com.twitch.twitchbackend.service;

public class RecommendationException extends RuntimeException {
    public RecommendationException(String errorMessage) {
        super(errorMessage);
    }
}