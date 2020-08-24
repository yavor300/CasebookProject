package app.config;

import org.modelmapper.ModelMapper;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class ApplicationBeanConfig {
    @Produces
    public EntityManager entityManager() {
        return Persistence.createEntityManagerFactory("caseBookUnit")
                .createEntityManager();
    }

    @Produces
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
