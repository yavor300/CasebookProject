package app.web.beans;

import app.domain.models.service.UserServiceModel;
import app.domain.models.view.FriendViewModel;
import app.service.UserService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class FriendsBean {
    private List<FriendViewModel> friends;
    private UserService userService;
    private ModelMapper modelMapper;

    public FriendsBean() {
    }

    @Inject
    public FriendsBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String loggedInUserId = (String) sessionMap.get("user-id");

        this.setFriends(
                this.userService.findById(loggedInUserId).getFriends()
                .stream()
                .map(f -> this.modelMapper.map(f, FriendViewModel.class))
                .collect(Collectors.toList())

        );
    }

    public void unfriend(String friendId) throws IOException {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String loggedInUserId = (String) sessionMap.get("user-id");

        this.userService.remove(loggedInUserId, friendId);

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/views" + "/friends" + ".jsf");
    }

    public List<FriendViewModel> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendViewModel> friends) {
        this.friends = friends;
    }
}
