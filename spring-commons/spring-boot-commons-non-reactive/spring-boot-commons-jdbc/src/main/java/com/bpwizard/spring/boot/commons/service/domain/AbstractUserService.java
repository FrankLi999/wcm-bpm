package com.bpwizard.spring.boot.commons.service.domain;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
/**
 * Abstract UserRepository interface
 */
@NoRepositoryBean
public interface AbstractUserService <U extends AbstractUser<ID>, ID extends Serializable> extends PagingAndSortingRepository<U, ID> {
	
	Optional<U> findByEmail(String email);
}
