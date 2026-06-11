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

    @Tool(description = """
        Search the library catalogue for books matching a theme,
        topic, genre, character type, plot element, or synopsis.

        Examples:
        - books about dragons
        - mystery novels
        - space exploration
        - coming of age stories

        Returns the most relevant books from the library catalogue.
        Each result includes the title, author, publication year,
        and synopsis.
        """)
    public List<Book> findBooksSemantically(String query) {

        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(5)
                        .build()
        );

        if (results.isEmpty()) {
            return List.of();
        }

        return bookLookupService.resolveRankedBooks(results);
    }

    @Tool(description = """
    Find a specific book by title.
    
    Returns the complete book information.
    """)
    public Book findBookByTitle(String title) {

        return bookRepository.findBookByTitle(title);
    }

}