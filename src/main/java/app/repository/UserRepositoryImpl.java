package app.repository;

import app.domain.entities.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;

    @Inject
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(User user) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(user);
        this.entityManager.getTransaction().commit();
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        this.entityManager.getTransaction().begin();
        User user = this.entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        this.entityManager.getTransaction().commit();
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        this.entityManager.getTransaction().begin();
        List<User> allUsers = this.entityManager.createQuery(
                "SELECT u FROM User u", User.class)
                .getResultList();
        this.entityManager.getTransaction().commit();
        return allUsers;
    }

    @Override
    public List<User> getFriends(String id) {
        this.entityManager.getTransaction().begin();
        List<User> friends = this.entityManager.createQuery(
                "SELECT u.friends FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();
        this.entityManager.getTransaction().commit();
        return friends;
    }

    @Override
    public User findById(String id) {
        return this.entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void update(User user) {
        this.entityManager.getTransaction().begin();
        this.entityManager.merge(user);
        this.entityManager.getTransaction().commit();
    }
}
