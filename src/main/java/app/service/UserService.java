package app.service;

import app.domain.models.service.UserServiceModel;

import java.util.List;

public interface UserService {
    void save(UserServiceModel user);

    UserServiceModel findByUsernameAndPassword(String username, String password);

    List<UserServiceModel> getAllUsers();

    List<UserServiceModel> getFriends(String username);

    UserServiceModel findById(String id);

    void addFriend(UserServiceModel userServiceModel);

    void remove(String userId, String friendId);
}
