package com.example.footballapi.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Relation(collectionRelation = "teams", itemRelation = "team")
public class TeamResponse extends RepresentationModel<TeamResponse> {
    private final Long id;
    private final String name;
    private final String stadium;
    private final LocalDateTime founded;
    private final List<PlayerResponse> players;

    public TeamResponse(Long id, String name, String stadium, LocalDateTime founded, List<PlayerResponse> players) {
        this.id = id;
        this.name = name;
        this.stadium = stadium;
        this.founded = founded;
        this.players = players;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getStadium() { return stadium; }
    public LocalDateTime getFounded() { return founded; }
    public List<PlayerResponse> getPlayers() { return players; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamResponse that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
