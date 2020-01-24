package com.bpwizard.monitoring.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bpwizard.monitoring.db.entities.PersonEntity;

/**
 * 
 * <pre>
 *  	JPA repository interface for {@link PersonEntity} class
 * </pre>
 */
@Repository
public interface PersonRepository  extends JpaRepository<PersonEntity, Integer>{

}
