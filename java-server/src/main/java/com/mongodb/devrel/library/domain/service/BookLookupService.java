package com.mongodb.devrel.library.domain.service;

import com.mongodb.devrel.library.domain.model.Book;
import com.mongodb.devrel.library.infrastructure.repository.BookRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookLookupService {

    private final BookRepository bookRepository;
    private final VectorStore vectorStore;

    public BookLookupService(BookRepository bookRepository, VectorStore vectorStore) {
        this.bookRepository = bookRepository;
        this.vectorStore = vectorStore;
    }

    /**
     * Performs a semantic (vector) search for books based on the user's query.
     * This method should:
     *  1. Send the query to the vector store using a similarity search request.
     *  2. Retrieve the top matching documents ranked by semantic similarity.
     *  3. Resolve those search results into full Book entities while preserving
     *     the ranking returned by the vector search.
     *
     * @param query natural language search query
     * @return list of books ranked by semantic similarity
     */
    public List<Book> semanticSearchBooks(String query) {

        List<Document> books = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(20)
                        .build()
        );

        return resolveRankedBooks(books);
    }

    /**
     * Performs a semantic (vector) search for books using a natural language query,
     * restricting results to books published within the given year range.
     * This method:
     *  1. Sends the query to the vector store using a similarity search request.
     *  2. Applies a metadata filter so only documents whose publication year falls
     *     between the provided bounds are considered.
     *  3. Retrieves the top matching documents ranked by semantic similarity.
     *  4. Resolves those search results into full Book entities while preserving
     *     vector search ranking.
     *
     * @param query natural language search query
     * @param yearFrom inclusive lower publication year bound
     * @param yearTo inclusive upper publication year bound
     * @return books ranked by semantic similarity within the given year range
     */
    public List<Book> semanticSearchBooksFilteredByYear(
            String query,
            Integer yearFrom,
            Integer yearTo
    ) {
        FilterExpressionBuilder b = new FilterExpressionBuilder();

        var filter = b.and(
                b.gte("year", yearFrom),
                b.lte("year", yearTo)
        ).build();

        List<Document> books = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(20)
                        .filterExpression(filter)
                        .build()
        );

        return resolveRankedBooks(books);
    }

    /**
     * Resolves vector search result documents into full {@link Book} entities
     * while preserving the ranking order returned by the search engine.
     * The vector search returns lightweight documents containing metadata,
     * including book IDs. We:
     *  1. Extract IDs from the result documents.
     *  2. Load all matching books from the repository in a single query.
     *  3. Index the loaded books by ID for quick lookup.
     *  4. Reconstruct the result list in the original ranking order, since
     *     repository queries do not guarantee order preservation.
     *
     * @param docs vector search result documents containing book IDs
     * @return ordered list of books matching the vector ranking
     */
    public List<Book> resolveRankedBooks(List<Document> docs) {

        if (docs == null || docs.isEmpty()) {
            return List.of();
        }

        List<String> ids = docs.stream()
                .map(d -> d.getMetadata().get("id").toString())
                .toList();

        List<Book> found = bookRepository.findAllById(ids);

        Map<String, Book> byId = found.stream()
                .collect(Collectors.toMap(Book::id, b -> b));

        return ids.stream()
                .map(byId::get)
                .toList();
    }
}
