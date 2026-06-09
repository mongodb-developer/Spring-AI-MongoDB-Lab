package com.mongodb.devrel.library.domain.service;

import com.mongodb.devrel.library.application.web.controller.response.RagResponse;
import com.mongodb.devrel.library.domain.model.Book;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryAssistant {

    private final Advisor ragAdvisor;
    private final ChatClient chatClient;
    private final BookLookupService bookLookupService;
    private final DocumentRetriever documentRetriever;

    public LibraryAssistant(Advisor ragAdvisor, ChatClient chatClient, BookLookupService bookLookupService, DocumentRetriever documentRetriever) {
        this.ragAdvisor = ragAdvisor;
        this.chatClient = chatClient;
        this.bookLookupService = bookLookupService;
        this.documentRetriever = documentRetriever;
    }

    public RagResponse askLibraryAssistant(String question) {

        ChatResponse response = chatClient.prompt()
                .advisors(ragAdvisor)
                .user(question)
                .call()
                .chatResponse();

        // This will extract the retrieved documents used to inform our LLM response.
        // it is done separately here as there is no way to access the advisor results when using the chatClient to retrieve
        List<Document> documents =
                documentRetriever.retrieve(new Query(question));

        List<Book> books = documents == null
                ? List.of()
                : bookLookupService.resolveRankedBooks(documents);

        String answer = response == null
                ? "Unable to generate a response."
                : response.getResult().getOutput().getText();

        return new RagResponse(answer, books);
    }
}
