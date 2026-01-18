package com.example.footballapi.graphql;

import com.example.footballapi.api.dto.PlayerRequest;
import com.example.footballapi.api.dto.PlayerResponse;
import com.example.footballapi.service.PlayerService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@DgsComponent
public class PlayerDataFetcher {

    private final PlayerService playerService;

    public PlayerDataFetcher(PlayerService playerService) {
        this.playerService = playerService;
    }

    @DgsQuery
    public List<PlayerResponse> players() {
        return playerService.findAll();
    }

    @DgsQuery
    public PlayerResponse playerById(@InputArgument Long id) {
        return playerService.findById(id);
    }

    @DgsMutation
    public PlayerResponse createPlayer(@InputArgument("input") Map<String, Object> input) {
        PlayerRequest request = new PlayerRequest(
                (String) input.get("firstName"),
                (String) input.get("lastName"),
                LocalDate.parse((String) input.get("dateOfBirth")),
                (String) input.get("position")
        );
        return playerService.create(request);
    }

    @DgsMutation
    public PlayerResponse updatePlayer(@InputArgument Long id, @InputArgument("input") Map<String, Object> input) {
        PlayerRequest request = new PlayerRequest(
                (String) input.get("firstName"),
                (String) input.get("lastName"),
                LocalDate.parse((String) input.get("dateOfBirth")),
                (String) input.get("position")
        );
        return playerService.update(id, request);
    }

    @DgsMutation
    public Long deletePlayer(@InputArgument Long id) {
        playerService.deleteById(id);
        return id;
    }
}
