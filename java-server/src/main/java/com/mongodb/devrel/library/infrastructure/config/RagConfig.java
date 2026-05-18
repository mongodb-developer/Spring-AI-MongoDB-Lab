package com.mongodb.devrel.library.infrastructure.config;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    private final VectorStore vectorStore;

    public RagConfig(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // Add a document retriever `Bean` that uses our vector store,
    // and performs vector searches with a similarity threshold
    // of 0.7, and returns the top 6 results

    // CODE HERE



    // Build a `PromptTemplate` `Bean that gets sent to the LLM, along
    // with the retrieved documents that informs it on the role it
    // is playing, a helpful library assistant, along with how we
    // want it to answer our questions

    // CODE HERE



    // Build a `RetrievalAugmentationAdvisor` with our configured `retriever`,
    // our `ragPromptTemplate`, and we will also tell it we do not want to
    // answer if we have no relevant documents in our db.

    // CODE HERE
}
