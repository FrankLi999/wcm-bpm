package com.bpwizard.myresources.resource.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // void save(User user);

    // Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

//    void saveRelation(Follow followRelation);
//
//    Optional<Follow> findRelation(String userId, String targetId);
//
//    void removeRelation(Follow followRelation);
}
