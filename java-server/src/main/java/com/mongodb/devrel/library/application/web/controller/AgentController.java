package com.mongodb.devrel.library.application.web.controller;

import com.mongodb.devrel.library.application.web.controller.request.AskRequest;
import com.mongodb.devrel.library.application.web.controller.response.AgentResponse;
import com.mongodb.devrel.library.domain.service.agent.LibraryOrchestrator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final LibraryOrchestrator orchestrator;

    public AgentController(LibraryOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping()
    public ResponseEntity<AgentResponse> ask(@RequestBody AskRequest request) {

        AgentResponse answer = orchestrator.process(request.ask(), request.conversationId());

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }


}