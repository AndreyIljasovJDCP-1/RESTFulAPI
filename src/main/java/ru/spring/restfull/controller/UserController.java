package ru.spring.restfull.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.spring.restfull.model.User;
import ru.spring.restfull.model.UserModelAssembler;
import ru.spring.restfull.resolver.UserParam;
import ru.spring.restfull.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping("/restAPI/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserModelAssembler assembler;


    //migrate to RESTFULL
    @GetMapping
    public CollectionModel<EntityModel<User>> all() {
        return assembler.toCollectionModel(userService.all());
    }

    @GetMapping("/admin")
    public CollectionModel<EntityModel<User>> admin() {
        return assembler.toCollectionModel(userService.admin());
    }

    @GetMapping("/admin/removed")
    public CollectionModel<EntityModel<User>> getRemoved() {
        return assembler.toCollectionModel(userService.getRemoved());
    }

    @GetMapping("/{id}")
    public EntityModel<User> getById(@PathVariable @Min(1) long id) {
        return assembler.toModel(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    public EntityModel<User> removeById(@PathVariable @Min(1) long id) {
        return assembler.toModel(userService.removeById(id));
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid User user) {
        var entityModel = assembler.toModel(userService.save(user));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> restoreUser(@RequestBody @Valid User checkUser,
                                         @PathVariable @Min(1) Long id) {
        var entityModel = assembler.toModel(userService.restore(checkUser, id));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User newUser,
                                        @PathVariable @Min(1) Long id) {
        var entityModel = assembler.toModel(userService.replace(newUser, id));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // из одной формы
    @PostMapping("/form")
    public EntityModel<User> saveUserFromForm(@UserParam User user) {
        return assembler.toModel(userService.save(user));
    }

    @GetMapping("/form")
    public EntityModel<User> getUserFromForm(@UserParam User user) {
        return assembler.toModel(userService.findByLoginAndPassword(user));
    }
}
