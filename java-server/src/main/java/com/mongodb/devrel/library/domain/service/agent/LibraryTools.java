package com.mongodb.devrel.library.domain.service.agent;

import com.mongodb.devrel.library.domain.model.Book;
import com.mongodb.devrel.library.domain.service.BookLookupService;
import com.mongodb.devrel.library.infrastructure.repository.BookRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibraryTools {

    private final MongoDBAtlasVectorStore vectorStore;
    private final BookRepository bookRepository;
    private final BookLookupService bookLookupService;

    public LibraryTools(MongoDBAtlasVectorStore vectorStore, BookRepository bookRepository, BookLookupService bookLookupService) {
        this.vectorStore = vectorStore;
        this.bookRepository = bookRepository;
        this.bookLookupService = bookLookupService;
    }

    // TODO: create the `findBookByTitle` tool that takes in the string Title and returns the book document
    // Add code here


    // TODO: create the `findBooksSemantically` tool that takes a user query a returns semantically similar books
    // Add code here


}