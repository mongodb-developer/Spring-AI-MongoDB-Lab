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

    public LibraryAssistant(Advisor ragAdvisor, ChatClient chatClient, BookLookupService bookLookupService) {
        this.ragAdvisor = ragAdvisor;
        this.chatClient = chatClient;
        this.bookLookupService = bookLookupService;
    }

    public RagResponse askLibraryAssistant(String question) {

        // Call our `chatClient` and pass in our `ragAdvisor` and our user `question`.
        // We'll return in our `ChatResponse`
        // CODE HERE
        ChatResponse response = null;

        assert response != null;
        // This will extract the retrieved documents used to inform our LLM response
        List<Document> documents = response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);

        assert documents != null;
        // This will return our full documents from our books collection, to populate the page results
        List<Book> ordered = bookLookupService.resolveRankedBooks(documents);

        String answer = response
                .getResult()
                .getOutput()
                .getText();

        return new RagResponse(answer, ordered);
    }
}
