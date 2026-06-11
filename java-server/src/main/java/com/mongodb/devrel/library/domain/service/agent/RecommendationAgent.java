package com.mongodb.devrel.library.domain.service.agent;

import com.mongodb.devrel.library.application.web.controller.response.AgentResponse;
import com.mongodb.devrel.library.domain.model.Book;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationAgent {

    private final ChatClient chatClient;
    private final LibraryTools libraryTools;

    public RecommendationAgent(
            ChatClient chatClient,
            LibraryTools libraryTools
    ) {
        this.chatClient = chatClient;
        this.libraryTools = libraryTools;
    }

    public AgentResponse process(String query) {

        String answer = chatClient.prompt()
                .system("""
                        You are a library recommendation specialist.

                        Use your available tools to find books matching
                        the user's interests.

                        Explain why the recommendations are relevant.
                        """)
                .tools(libraryTools)
                .user(query)
                .call()
                .content();

        List<Book> books =
                libraryTools.findBooksSemantically(query);

        return new AgentResponse(
                answer,
                books
        );
    }
}