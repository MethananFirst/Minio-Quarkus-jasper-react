package org.quarkus.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.quarkus.entity.UserEntity;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, Integer> {

    public UserEntity findByUserName(String userName) {
        return find("userName", userName).firstResult();
    }

    public UserEntity findByUserNameAndPassword(String userName, String password) {
        return find("userName = ?1 and password = ?2", userName, password).firstResult();
    }
}
