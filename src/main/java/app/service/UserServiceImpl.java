package app.service;

import app.domain.entities.User;
import app.domain.models.service.UserServiceModel;
import app.repository.UserRepository;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Inject
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(UserServiceModel user) {
        this.userRepository.save(
                this.modelMapper.map(user, User.class)
        );
    }

    @Override
    public UserServiceModel findByUsernameAndPassword(String username, String password) {
        return this.modelMapper.map(this.userRepository.findByUsernameAndPassword(username, password), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return this.userRepository.getAllUsers()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> getFriends(String id) {
        return this.userRepository.getFriends(id)
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel findById(String id) {
        return this.modelMapper.map(this.userRepository.findById(id), UserServiceModel.class);
    }

    @Override
    public void addFriend(UserServiceModel userServiceModel) {
        this.userRepository.update(this.modelMapper.map(userServiceModel, User.class));
    }

    @Override
    public void remove(String userId, String friendId) {
        User loggedInUser = this.userRepository.findById(userId);
        User friend = this.userRepository.findById(friendId);

        loggedInUser.getFriends().remove(friend);
        friend.getFriends().remove(loggedInUser);

        this.userRepository.update(loggedInUser);
        this.userRepository.update(friend);
    }
}
