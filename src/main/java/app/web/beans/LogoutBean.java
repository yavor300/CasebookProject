package app.web.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;

@Named
@RequestScoped
public class LogoutBean {
    public void logout() throws IOException {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/views" + "/index" + ".jsf");
    }
}
