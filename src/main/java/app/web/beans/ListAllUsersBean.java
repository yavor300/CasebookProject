package app.web.beans;

import app.domain.models.service.UserServiceModel;
import app.domain.models.view.UserViewModel;
import app.service.UserService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class ListAllUsersBean {
    private List<UserViewModel> users;
    private UserService userService;
    private ModelMapper modelMapper;

    public ListAllUsersBean() {
    }

    @Inject
    public ListAllUsersBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        List<UserViewModel> allExistingUsers = this.userService.getAllUsers().stream()
                .map(u -> this.modelMapper.map(u, UserViewModel.class))
                .collect(Collectors.toList());

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        String loggedInUserId = (String) sessionMap.get("user-id");

        List<UserServiceModel> friends = this.userService.findById(loggedInUserId).getFriends();

        List<String> idsToRemove = new ArrayList<>();

        idsToRemove.add(loggedInUserId);

        if (friends != null) {
            for (UserServiceModel friend : friends) {
                idsToRemove.add(friend.getId());
            }
        }

        List<UserViewModel> usersToDisplay = new ArrayList<>();

        for (UserViewModel existingUser : allExistingUsers) {
            if (!idsToRemove.contains(existingUser.getId())) {
                usersToDisplay.add(existingUser);
            }
        }

        this.setUsers(usersToDisplay);
        this.getUsers().forEach(u -> u.setGender(u.getGender().toLowerCase()));
    }

    public void addFriend(String friendId) throws IOException {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String loggedInUserId = (String) sessionMap.get("user-id");

        UserServiceModel loggedInUser = this.userService.findById(loggedInUserId);
        UserServiceModel friend = this.userService.findById(friendId);

        loggedInUser.getFriends().add(friend);
        friend.getFriends().add(loggedInUser);

        this.userService.addFriend(loggedInUser);
        this.userService.addFriend(friend);

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/views" + "/home" + ".jsf");
    }

    public List<UserViewModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserViewModel> users) {
        this.users = users;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
