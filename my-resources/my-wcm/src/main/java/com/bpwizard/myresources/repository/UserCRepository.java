package com.bpwizard.myresources.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bpwizard.myresources.model.UserC;

public interface UserCRepository extends CrudRepository<UserC, Long> {

    List<UserC> findByLastName(@Param("lastname") String lastname);

    List<UserC> findByFirstName(@Param("firstname") String firstname);

}
