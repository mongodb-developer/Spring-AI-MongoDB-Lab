package com.mongodb.devrel.library.domain.service.agent;

import com.mongodb.devrel.library.application.web.controller.response.AgentResponse;
import com.mongodb.devrel.library.domain.model.Book;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryAgent {

    private final ChatClient chatClient;
    private final LibraryTools libraryTools;

    public SummaryAgent(
            ChatClient chatClient,
            LibraryTools libraryTools
    ) {
        this.chatClient = chatClient;
        this.libraryTools = libraryTools;
    }

    public AgentResponse process(String title) {

        // TODO: Access the library tools and use the `findBookByTitle` tool to get the book mentioned by the user
        // Add code here

        if (book == null) {
            return new AgentResponse(
                    "The requested book could not be found.",
                    List.of()
            );
        }

        // TODO: Create the chat client prompt that provides the LLM the book synopsis and the approptiate instructions to summarize it
        // Add code here



        return new AgentResponse(
                answer,
                List.of(book)
        );
    }
}