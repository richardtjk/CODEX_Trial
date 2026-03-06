package com.codextrial.teamservice.controller;

import com.codextrial.teamservice.dto.TeamGenerationRequest;
import com.codextrial.teamservice.dto.TeamGenerationResponse;
import com.codextrial.teamservice.service.TeamGenerationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(originPatterns = {"http://207.211.164.167", "http://207.211.164.167:*", "http://localhost", "http://localhost:*", "http://127.0.0.1", "http://127.0.0.1:*"})
public class TeamController {

    private final TeamGenerationService teamGenerationService;

    public TeamController(TeamGenerationService teamGenerationService) {
        this.teamGenerationService = teamGenerationService;
    }

    @PostMapping("/generate")
    public TeamGenerationResponse generateTeams(@Valid @RequestBody TeamGenerationRequest request) {
        return new TeamGenerationResponse(
                teamGenerationService.createTeams(
                        request.participants(),
                        request.minTeamSize(),
                        request.maxTeamSize(),
                        request.idealTeamSize()
                )
        );
    }
}
