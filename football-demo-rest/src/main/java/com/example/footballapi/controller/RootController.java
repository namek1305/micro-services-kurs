package com.example.footballapi.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.*;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(PlayerController.class).getAllPlayers()).withRel("players"),
                linkTo(methodOn(TeamController.class).getAllTeams()).withRel("teams"),
                Link.of("/swagger-ui.html").withRel("documentation")
        );
        return rootModel;
    }
}
