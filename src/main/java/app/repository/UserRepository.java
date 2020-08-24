package app.repository;

import app.domain.entities.User;

import java.util.List;

public interface UserRepository {
    void save(User user);

    User findByUsernameAndPassword(String username, String password);

    List<User> getAllUsers();

    List<User> getFriends(String username);

    User findById(String id);

    void update(User user);
}
