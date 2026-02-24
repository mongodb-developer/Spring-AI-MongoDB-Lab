package com.mongodb.devrel.library.domain.service;

import com.mongodb.devrel.library.domain.model.Book;
import com.mongodb.devrel.library.domain.model.Author;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookEmbeddingService {

    private final VectorStore vectorStore;

    public BookEmbeddingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Stores books in the vector store by embedding their synopsis text.
     * When implementing this method, you should:
     *  1. Iterate over the provided books and ignore any entries that do not
     *     contain a usable synopsis, since there is nothing meaningful to embed.
     *  2. Create a Document for each valid book where:
     *        - the synopsis becomes the text content to embed,
     *        - metadata includes identifiers and useful fields such as book ID,
     *          title, genres, and author names.
     *  3. Send the resulting documents to the vector store so embeddings are
     *     generated and stored for later semantic search.
     *
     * @param booksToEmbed books whose synopsis should be embedded and indexed
     */
    public void storeBooksWithSynopsisEmbedded(List<Book> booksToEmbed) {

        if (booksToEmbed == null || booksToEmbed.isEmpty()) {
            return;
        }

        List<Document> docs = booksToEmbed.stream()
                .map(this::toVectorDocument)   // convert each book
                .filter(Objects::nonNull)      // skip books without synopsis
                .toList();

        if (!docs.isEmpty()) {
            // TODO: store documents in vectorStore
        }
    }


    private Document toVectorDocument(Book book) {

        // Use synopsis as the text to embed
        String text = book.synopsis();

        // Skip books without usable text
        if (text == null || text.isBlank()) {
            return null;
        }

        Map<String, Object> metadata = new HashMap<>();

        if (book.id() != null) {
            metadata.put("id", book.id());
        }

        if (book.title() != null && !book.title().isBlank()) {
            metadata.put("title", book.title());
        }

        if (book.authors() != null && !book.authors().isEmpty()) {
            List<String> authorNames = book.authors().stream()
                    .map(Author::name)
                    .filter(Objects::nonNull)
                    .toList();

            if (!authorNames.isEmpty()) {
                metadata.put("authors", authorNames);
            }
        }

        if (book.genres() != null && !book.genres().isEmpty()) {
            metadata.put("genres", book.genres());
        }

        if (book.year() != null) {
            metadata.put("year", book.year());
        }

        return new Document(text, metadata);
    }

}
