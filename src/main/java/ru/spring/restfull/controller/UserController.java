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
import java.util.List;

@RestController
@Validated
@RequestMapping("/restAPI/users")
public class UserController {
    @Autowired
    UserService userService;

        @GetMapping
    public List<User> all() {
        return userService.all();
    }

    @GetMapping("/admin")
    public List<User> admin() {
        return userService.admin();
    }
    @GetMapping("/admin/removed")
    public List<User> getRemoved() {
        return userService.getRemoved();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable @Min(1) long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public User removeById(@PathVariable @Min(1) long id) {
        return userService.removeById(id);
    }

    @PostMapping
    public User saveUser(@RequestBody @Valid User user) {
        return userService.save(user);
    }

    @PostMapping("/{id}")
    public User restoreUser(@RequestBody @Valid User checkUser,
                            @PathVariable @Min(1) Long id) {
        return userService.restore(checkUser, id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody @Valid User newUser,
                           @PathVariable @Min(1) Long id) {
        return userService.replace(newUser, id);
    }

    @PostMapping("/form")
    public User saveUserFromForm(@UserParam User user) {
        return userService.save(user);
    }

    @GetMapping("/form")
    public User getUserFromForm(@UserParam User user) {
        return userService.findByLoginAndPassword(user);
    }


}
