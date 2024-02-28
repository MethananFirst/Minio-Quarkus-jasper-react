package org.quarkus.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.quarkus.dto.UserDto;
import org.quarkus.entity.UserEntity;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Transactional
    public List<UserEntity> listAllUser() {
        return UserEntity.listAll();
    }

    public UserEntity getUserById(Integer id) {
        return UserEntity.findById(id);
    }

    @Transactional
    public UserEntity addUser(UserDto dto) {
        UserEntity user = new UserEntity();

        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
//        user.setRole(dto.getRole());
        user.setRole("User");
        user.setCreated_at(ZonedDateTime.now());
        user.setUpdated_at(ZonedDateTime.now());
        user.persist();
        return user;
    }

    @Transactional
    public void updateUser(Integer id, UserDto dto) {
        Optional<UserEntity> users = UserEntity.findByIdOptional(id);

        if (users.isEmpty()) {
            throw new NullPointerException("User not found!!!");
        }

        UserEntity user = users.get();
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setUpdated_at(ZonedDateTime.now());
        user.persist();
    }

    @Transactional
    public void removeUser(Integer id) {
        Optional<UserEntity> user = UserEntity.findByIdOptional(id);

        if (user.isEmpty()){
            throw new NullPointerException("Address not found!!!");
        }

        UserEntity user1 = user.get();

        user1.delete();
    }
}
