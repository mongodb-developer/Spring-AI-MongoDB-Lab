package com.mongodb.devrel.library.domain.service;

import com.mongodb.devrel.library.application.web.controller.response.RagResponse;
import com.mongodb.devrel.library.domain.model.Book;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryAssistant {

    private final Advisor ragAdvisor;
    private final ChatClient chatClient;
    private final DocumentRetriever retriever;
    private final BookLookupService bookLookupService;

    public LibraryAssistant(Advisor ragAdvisor, ChatClient chatClient, DocumentRetriever retriever, BookLookupService bookLookupService) {
        this.ragAdvisor = ragAdvisor;
        this.chatClient = chatClient;
        this.retriever = retriever;
        this.bookLookupService = bookLookupService;
    }

    public RagResponse askLibraryAssistant(String question) {

        long startTotal = System.nanoTime();

        // --- Retrieval timing ---
        long startRetrieval = System.nanoTime();

        List<Document> docs = retriever.retrieve(new Query(question));
        List<Book> ordered = bookLookupService.resolveRankedBooks(docs);

        long retrievalMs = (System.nanoTime() - startRetrieval) / 1_000_000;

        // --- LLM timing ---
        long startLlm = System.nanoTime();

        String answer = chatClient.prompt()
                .advisors(ragAdvisor)
                .user(question)
                .call()
                .content();

        long llmMs = (System.nanoTime() - startLlm) / 1_000_000;

        long totalMs = (System.nanoTime() - startTotal) / 1_000_000;

        System.out.printf(
                "Timing → Retrieval: %d ms | LLM: %d ms | Total: %d ms%n",
                retrievalMs, llmMs, totalMs
        );


        return new RagResponse(answer, ordered);
    }
}
