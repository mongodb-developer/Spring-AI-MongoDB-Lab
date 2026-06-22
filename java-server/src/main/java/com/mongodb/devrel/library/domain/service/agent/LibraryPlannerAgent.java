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

    // TODO: Create the `decide` method that will take in the String question, and a String `conversationId` (we'll use this later), and return the `AgentDecision`.
    //  The `chatClient` prompt will describe the agents available, and when to use them.
    // Add code here


}
