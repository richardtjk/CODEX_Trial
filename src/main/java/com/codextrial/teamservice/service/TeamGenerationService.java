package com.codextrial.teamservice.service;

import com.codextrial.teamservice.dto.TeamDto;
import com.codextrial.teamservice.exception.InvalidTeamConfigurationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamGenerationService {

    public List<TeamDto> createTeams(List<String> participants, int minTeamSize, int maxTeamSize, int idealTeamSize) {
        validateInputs(participants, minTeamSize, maxTeamSize, idealTeamSize);

        int totalParticipants = participants.size();
        int minTeams = divideAndRoundUp(totalParticipants, maxTeamSize);
        int maxTeams = totalParticipants / minTeamSize;

        if (minTeams > maxTeams) {
            throw new InvalidTeamConfigurationException(
                    "Unable to create teams with the requested constraints for " + totalParticipants + " participants"
            );
        }

        int selectedTeamCount = chooseTeamCount(totalParticipants, idealTeamSize, minTeams, maxTeams);
        return distributeParticipants(participants, selectedTeamCount);
    }

    private void validateInputs(List<String> participants, int minTeamSize, int maxTeamSize, int idealTeamSize) {
        if (minTeamSize > maxTeamSize) {
            throw new InvalidTeamConfigurationException("minTeamSize must be less than or equal to maxTeamSize");
        }
        if (idealTeamSize < minTeamSize || idealTeamSize > maxTeamSize) {
            throw new InvalidTeamConfigurationException("idealTeamSize must be between minTeamSize and maxTeamSize");
        }
        if (participants == null || participants.isEmpty()) {
            throw new InvalidTeamConfigurationException("participants must not be empty");
        }
    }

    private int chooseTeamCount(int totalParticipants, int idealTeamSize, int minTeams, int maxTeams) {
        int bestTeamCount = minTeams;
        double smallestDistance = Double.MAX_VALUE;

        for (int teamCount = minTeams; teamCount <= maxTeams; teamCount++) {
            double averageSize = (double) totalParticipants / teamCount;
            double distance = Math.abs(averageSize - idealTeamSize);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                bestTeamCount = teamCount;
            }
        }

        return bestTeamCount;
    }

    private List<TeamDto> distributeParticipants(List<String> participants, int teamCount) {
        int totalParticipants = participants.size();
        int baseSize = totalParticipants / teamCount;
        int teamsWithExtraMember = totalParticipants % teamCount;

        List<TeamDto> teams = new ArrayList<>();
        int index = 0;

        for (int i = 0; i < teamCount; i++) {
            int currentSize = baseSize + (i < teamsWithExtraMember ? 1 : 0);
            List<String> members = new ArrayList<>(participants.subList(index, index + currentSize));
            teams.add(new TeamDto("Team " + (i + 1), members));
            index += currentSize;
        }

        return teams;
    }

    private int divideAndRoundUp(int dividend, int divisor) {
        return (dividend + divisor - 1) / divisor;
    }
}
