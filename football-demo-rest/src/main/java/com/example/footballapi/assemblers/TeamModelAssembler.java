package com.example.footballapi.assemblers;

import com.example.footballapi.api.dto.TeamResponse;
import com.example.footballapi.controller.PlayerController;
import com.example.footballapi.controller.TeamController;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamModelAssembler implements RepresentationModelAssembler<TeamResponse, EntityModel<TeamResponse>> {

    @Override
    public EntityModel<TeamResponse> toModel(TeamResponse team) {
        return EntityModel.of(team,
                linkTo(methodOn(TeamController.class).getTeamById(team.getId())).withSelfRel(),
                linkTo(methodOn(TeamController.class).getAllTeams()).withRel("teams"),
                linkTo(methodOn(PlayerController.class).getAllPlayers()).withRel("players")
        );
    }

    @Override
    public CollectionModel<EntityModel<TeamResponse>> toCollectionModel(Iterable<? extends TeamResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(TeamController.class).getAllTeams()).withSelfRel());
    }
}
