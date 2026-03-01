package com.codextrial.teamservice.service;

import com.codextrial.teamservice.dto.TeamDto;
import com.codextrial.teamservice.exception.InvalidTeamConfigurationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeamGenerationServiceTest {

    private final TeamGenerationService service = new TeamGenerationService();

    @Test
    void createsTeamsUsingClosestAverageToIdeal() {
        List<String> participants = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

        List<TeamDto> teams = service.createTeams(participants, 2, 4, 3);

        assertEquals(3, teams.size());
        assertEquals(4, teams.get(0).members().size());
        assertEquals(3, teams.get(1).members().size());
        assertEquals(3, teams.get(2).members().size());
    }

    @Test
    void throwsWhenConstraintsAreImpossible() {
        List<String> participants = List.of("A", "B", "C");

        assertThrows(InvalidTeamConfigurationException.class,
                () -> service.createTeams(participants, 4, 5, 4));
    }

    @Test
    void throwsWhenIdealIsOutsideMinMaxRange() {
        List<String> participants = List.of("A", "B", "C", "D");

        assertThrows(InvalidTeamConfigurationException.class,
                () -> service.createTeams(participants, 2, 3, 4));
    }
}
