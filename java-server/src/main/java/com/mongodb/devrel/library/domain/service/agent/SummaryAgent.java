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

        Book book = libraryTools.findBookByTitle(title);

        if (book == null) {
            return new AgentResponse(
                    "The requested book could not be found.",
                    List.of()
            );
        }

        String answer = chatClient.prompt()
                .system("""
                You are a librarian.

                Summarize the provided book synopsis.

                Do not mention metadata.
                Do not mention search results.
                Do not mention tools.

                Keep the summary under 100 words.
                """)
                .user(book.synopsis())
                .call()
                .content();

        return new AgentResponse(
                answer,
                List.of(book)
        );
    }
}