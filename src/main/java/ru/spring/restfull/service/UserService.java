package ru.spring.restfull.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spring.restfull.exception.NotFoundException;
import ru.spring.restfull.model.User;
import ru.spring.restfull.repository.UserRepository;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> all() {
        return userRepository.findAll()
                .stream()
                .filter(not(User::isRemoved))
                .toList();
    }

    public User getById(long id) {
        var user = userRepository.findById(id);

        if (user.isEmpty() || user.get().isRemoved()) {
            throw new NotFoundException(String.format("ID: %d not found", id));
        }
        return user.get();
    }

    public User findByLoginAndPassword(User user) {
        return userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword())
                .orElseThrow(() -> new NotFoundException(String.format("User: %s not found", user)));
    }

    public User save(User newUser) {
        return userRepository.findByLoginAndPassword(
                        newUser.getLogin(), newUser.getPassword()).map(user -> {
                    if (user.isRemoved()) {
                        user.setRemoved(false);
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseGet(() -> userRepository.save(newUser));
    }

    public User replace(User newUser, long id) {
        return userRepository.findById(id).map(user -> {
                    if (!user.isRemoved()) {
                        if (!isExist(newUser)) {
                            user.setLogin(newUser.getLogin());
                            user.setPassword(newUser.getPassword());
                        }
                        user.setAuthorities(newUser.getAuthorities());
                        return userRepository.save(user);
                    }
                    return newUser;
                })
                .orElseThrow(() -> new NotFoundException(String.format("ID: %d not found", id)));
    }

    public User restore(User checkUser, long id) {
        return userRepository.findById(id).map(user -> {
                    if (user.isRemoved() && user.equals(checkUser)) {
                        user.setRemoved(false);
                        return userRepository.save(user);
                    } else {
                        return user;
                    }
                })
                .orElseThrow(() -> new NotFoundException(String.format("ID: %d not found", id)));
    }

    public User removeById(long id) {
        return userRepository.findById(id).map(user -> {
            if (!user.isRemoved()) {
                user.setRemoved(true);
                return userRepository.save(user);
            }
            return user;
        }).orElseThrow(() -> new NotFoundException(String.format("ID: %d not found", id)));
    }

    public List<User> admin() {
        return userRepository.findAll();
    }

    public List<User> getRemoved() {
        return userRepository.findAll()
                .stream()
                .filter(User::isRemoved)
                .toList();
    }

    private boolean isExist(User user) {
        return userRepository
                .findByLoginAndPassword(user.getLogin(), user.getPassword())
                .isPresent();
    }
}
