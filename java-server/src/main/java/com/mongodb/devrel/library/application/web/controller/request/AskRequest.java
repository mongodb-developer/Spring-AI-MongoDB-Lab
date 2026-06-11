package com.mongodb.devrel.library.application.web.controller.request;

public record AskRequest(
        String ask,
        String conversationId
) {
}
