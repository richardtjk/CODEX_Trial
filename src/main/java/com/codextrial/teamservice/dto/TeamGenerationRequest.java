package com.codextrial.teamservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeamGenerationRequest(
        @NotEmpty(message = "participants must not be empty")
        List<@NotNull(message = "participant name must not be null") String> participants,
        @Min(value = 1, message = "minTeamSize must be at least 1")
        int minTeamSize,
        @Min(value = 1, message = "maxTeamSize must be at least 1")
        int maxTeamSize,
        @Min(value = 1, message = "idealTeamSize must be at least 1")
        int idealTeamSize
) {
}
