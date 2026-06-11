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

        AgentDecision decision =
                plannerAgent.decide(question, conversationId);

        return switch (decision.action()) {

            case RECOMMEND_BOOKS ->
                    recommendationAgent.process(
                            decision.query());

            case SUMMARIZE_BOOK ->
                    summaryAgent.process(
                            decision.query());
        };
    }
}