package com.bpwizard.spring.boot.commons.service.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService<U, ID> {
	public Boolean existsByEmail(String email);

	// public U save(U user);
	public U save(U user, Set<String> removedRoles, Set<String> newRoles);
	public U create(U user);

	public Optional<U> findById(ID id);
	
	public Optional<U> findByName(String name);
	
	public boolean existsById(ID id);
	
	public int deleteById(ID id);

	public int delete(U entity);
	
	public Optional<U> findByEmail(String email);
	public List<U> findAll(Pageable pageable);
	public List<String> findAllNames(Pageable pageable);
	public List<Role> findRolesByName(String name);
	public List<Tenant> findTenantsByName(String name);
	
	public int[] joinTenants(User<Long> user, List<Tenant> tenants);
	public int[] joinRoles(User<Long> user, List<Role> roles);
	
	public int[] joinTenantsByName(User<Long> user, Set<String> tenants);
	public int[] joinRolesByName(User<Long> user, Set<String> roles);
	public int[] removeRolesByName(User<Long> user, Set<String> roles);

}