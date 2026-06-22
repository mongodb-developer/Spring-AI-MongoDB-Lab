package com.mongodb.devrel.library.domain.service.agent;

import com.mongodb.devrel.library.application.web.controller.response.AgentResponse;
import org.springframework.stereotype.Service;

@Service
public class LibraryOrchestrator {

    private final LibraryPlannerAgent plannerAgent;
    private final RecommendationAgent recommendationAgent;
    private final SummaryAgent summaryAgent;

    public LibraryOrchestrator(
            LibraryPlannerAgent plannerAgent,
            RecommendationAgent recommendationAgent,
            SummaryAgent summaryAgent) {

        this.plannerAgent = plannerAgent;
        this.recommendationAgent = recommendationAgent;
        this.summaryAgent = summaryAgent;
    }

    public AgentResponse process(String question, String conversationId) {


        // TODO: Call the planner agent to decide which which decision to take
        // Add code here

        // TODO: use the `decision.action()` to decide which agent process to take
        // Add code here

    }
}