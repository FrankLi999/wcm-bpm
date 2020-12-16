package com.bpwizard.spring.boot.commons.service.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;

public interface RoleService<R, ID> {
	public Optional<R> findByName(String name);
	
	public void save(R role);
	public R create(R role);

	public Optional<R> findById(ID id);

	public boolean existsById(ID id);

	public int deleteById(ID id);

	public int delete(R entity);

	public List<R> findAll(Pageable pageable);
	public List<String> findAllRoleNames(Pageable pageable);
	
	public int addUser(Role role, User<Long> user);
	public int[] addUsers(Role role, List<User<Long>> users);
	public int removeUser(Role role, User<Long> user);

	public int addRole(Role role, Role member);
	public int[] addRoles(Role role, List<Role> members);
	public int[] joinTenants(Role role, List<Tenant> tenants);
	public int[] joinTenantsByName(Role role, Set<String> tenants);
	
	public int removeRole(Role role, Role member);
	
	public List<User<Long>> findUsersByName(String role);
	public List<Role> findRolesByName(String role);
}

