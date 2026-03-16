package com.mongodb.devrel.library.infrastructure.config;

import com.mongodb.devrel.library.domain.model.Book;
import com.mongodb.devrel.library.domain.service.BookEmbeddingService;
import com.mongodb.devrel.library.infrastructure.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EmbeddingInitializer {

    private final BookEmbeddingService bookEmbeddingService;
    private final MongoTemplate mongoTemplate;
    private final BookRepository bookRepository;

    @Value("${spring.ai.vectorstore.mongodb.collection-name}")
    private String vectorCollectionName;

    public EmbeddingInitializer(
            BookEmbeddingService bookEmbeddingService,
            MongoTemplate mongoTemplate,
            BookRepository bookRepository)
    {
        this.bookEmbeddingService = bookEmbeddingService;
        this.mongoTemplate = mongoTemplate;
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void embedBooksOnStartup() {

        Set<String> embeddedIds = new HashSet<>(
                mongoTemplate.getCollection(vectorCollectionName)
                        .distinct("metadata.id", String.class)
                        .into(new ArrayList<>())
        );

        List<Book> allBooks = bookRepository.findAll();

        List<Book> missing = allBooks.stream()
                .filter(b -> b.id() != null)
                .filter(b -> !embeddedIds.contains(b.id()))
                .toList();

        System.out.println("Books missing embeddings: " + missing.size());

        if (!missing.isEmpty()) {
            bookEmbeddingService.storeBooksWithSynopsisEmbedded(missing);
        }

        System.out.println("Finished generating vector embeddings.");
    }
}
