package com.mongodb.devrel.library.domain.service.agent;

public record AgentDecision(
        Action action,
        String query
) {

    public enum Action {
        RECOMMEND_BOOKS,
        SUMMARIZE_BOOK
    }
}