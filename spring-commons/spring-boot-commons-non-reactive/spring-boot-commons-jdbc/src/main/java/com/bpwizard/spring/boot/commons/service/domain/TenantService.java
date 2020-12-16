package com.bpwizard.spring.boot.commons.service.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TenantService<T, ID> {
	
	public Optional<T> findByName(String name);
	public T create(T entity);
	public void save(T entity);
	public Optional<T> findById(ID id);
	public boolean existsById(ID id);
	public int deleteById(ID id);
	public int delete(T entity);
	
	public List<T> findAll(Pageable pageable);
	public List<String> findAllTenantNames(Pageable pageable);
	
	public int addUser(Tenant tenant, User<Long> user);
	public int[] addUsers(Tenant tenant, List<User<Long>> users);
	
	public int addRole(Tenant tenant, Role role);
	public int[] addRoles(Tenant tenant, List<Role> role);
	
	public int removeUser(Tenant tenant, User<Long> user);
	public int removeRole(Tenant tenant, Role role);
	
	public List<User<Long>> findUsersByName(String tenant);
	public List<Role> findRolesByName(String tenant);
}

