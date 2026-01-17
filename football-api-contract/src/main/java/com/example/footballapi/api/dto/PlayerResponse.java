package com.example.footballapi.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Objects;

@Relation(collectionRelation = "players", itemRelation = "player")
public class PlayerResponse extends RepresentationModel<PlayerResponse> {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String position;

    public PlayerResponse(Long id, String firstName, String lastName, LocalDate dateOfBirth, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.position = position;
    }

    public Long getId() {
        return id;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getPosition() { return position; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerResponse that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
