package app.web.beans;

import app.domain.models.view.UserViewModel;
import app.service.UserService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class ProfileBean {
    private UserViewModel profile;
    private UserService userService;
    private ModelMapper modelMapper;

    public ProfileBean() {
    }

    @Inject
    public ProfileBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init(){
        String id = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest()).getParameter("id");
        this.profile = this.modelMapper.map(this.userService.findById(id),UserViewModel.class);
        this.profile.setGender(this.profile.getGender().toLowerCase());
    }

    public UserViewModel getProfile() {
        return profile;
    }

    public void setProfile(UserViewModel profile) {
        this.profile = profile;
    }
}
