package com.example.footballapi.graphql;

import com.example.footballapi.api.dto.TeamRequest;
import com.example.footballapi.api.dto.TeamResponse;
import com.example.footballapi.service.TeamService;
import com.example.footballapi.service.PlayerService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DgsComponent
public class TeamDataFetcher {

    private final TeamService teamService;
    private final PlayerService playerService;

    public TeamDataFetcher(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @DgsQuery
    public List<TeamResponse> teams() {
        return teamService.findAll();
    }

    @DgsQuery
    public TeamResponse teamById(@InputArgument Long id) {
        return teamService.findById(id);
    }

    @DgsMutation
    public TeamResponse createTeam(@InputArgument("input") Map<String, Object> input) {
        String name = (String) input.get("name");
        String stadium = (String) input.get("stadium");
        LocalDateTime founded = LocalDateTime.parse((String) input.get("founded"));
        List<Integer> playerIdsRaw = (List<Integer>) input.get("playerIds");
        List<Long> playerIds = playerIdsRaw == null ? null :
                playerIdsRaw.stream().map(Integer::longValue).collect(Collectors.toList());

        TeamRequest request = new TeamRequest(name, stadium, founded, playerIds);
        return teamService.create(request);
    }

    @DgsMutation
    public TeamResponse updateTeam(@InputArgument Long id, @InputArgument("input") Map<String, Object> input) {
        String name = (String) input.get("name");
        String stadium = (String) input.get("stadium");
        LocalDateTime founded = LocalDateTime.parse((String) input.get("founded"));
        List<Integer> playerIdsRaw = (List<Integer>) input.get("playerIds");
        List<Long> playerIds = playerIdsRaw == null ? null :
                playerIdsRaw.stream().map(Integer::longValue).collect(Collectors.toList());

        TeamRequest request = new TeamRequest(name, stadium, founded, playerIds);
        return teamService.update(id, request);
    }

    @DgsMutation
    public Long deleteTeam(@InputArgument Long id) {
        teamService.deleteById(id);
        return id;
    }

    @DgsData(parentType = "Team", field = "players")
    public List<?> players(DataFetchingEnvironment dfe) {
        TeamResponse team = dfe.getSource();
        return team.getPlayers();
    }
}
