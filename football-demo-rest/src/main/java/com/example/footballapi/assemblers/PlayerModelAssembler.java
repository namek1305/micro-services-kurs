package com.example.footballapi.assemblers;

import com.example.footballapi.api.dto.PlayerResponse;
import com.example.footballapi.controller.PlayerController;
import com.example.footballapi.controller.TeamController;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<PlayerResponse, EntityModel<PlayerResponse>> {

    @Override
    public EntityModel<PlayerResponse> toModel(PlayerResponse player) {
        return EntityModel.of(player,
                linkTo(methodOn(PlayerController.class).getPlayerById(player.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).getAllPlayers()).withRel("players"),
                linkTo(methodOn(TeamController.class).getAllTeams()).withRel("teams")
        );
    }

    @Override
    public CollectionModel<EntityModel<PlayerResponse>> toCollectionModel(Iterable<? extends PlayerResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(PlayerController.class).getAllPlayers()).withSelfRel());
    }
}
