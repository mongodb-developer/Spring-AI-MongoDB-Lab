package com.mongodb.devrel.library.domain.service.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class LibraryPlannerAgent {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public LibraryPlannerAgent(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    public AgentDecision decide(String question, String conversationId) {

        return chatClient.prompt()
                .system("""
                        You are a library request planner.

                        Determine which specialist agent should handle the request.

                        RECOMMEND_BOOKS
                        - User wants recommendations
                        - User wants books similar to another book
                        - User wants books about a topic
                        - User wants books matching a theme

                        SUMMARIZE_BOOK
                        - User asks what a specific book is about
                        - User requests a summary
                        - User asks about a specific title

                        Extract the most useful query for the worker agent.
                        
                        Examples:

                        User: Recommend books like Dune
                        Action: RECOMMEND_BOOKS
                        Query: Dune

                        User: Books about dragons
                        Action: RECOMMEND_BOOKS
                        Query: dragons

                        User: What is The Hobbit about?
                        Action: SUMMARIZE_BOOK
                        Query: The Hobbit
                        """)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .conversationId(conversationId)
                        .build())
                .user(question)
                .call()
                .entity(AgentDecision.class);
    }
}
