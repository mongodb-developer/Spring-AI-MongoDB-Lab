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

        // TODO: Create the chat client prompt that provides the LLM the libraryTools we created
        //      and instructions to get semantically similar books
        // Add code here



        List<Book> books =
                libraryTools.findBooksSemantically(query);

        return new AgentResponse(
                answer,
                books
        );
    }
}