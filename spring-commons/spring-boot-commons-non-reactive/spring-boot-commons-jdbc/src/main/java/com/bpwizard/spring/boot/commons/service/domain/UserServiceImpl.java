package com.bpwizard.spring.boot.commons.service.domain;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

@Service
public class UserServiceImpl implements UserService<User<Long>, Long> {
	private static final String selectAllColumn = "SELECT name, email, password, first_name, last_name, image_url, provider, provider_id, version FROM usr WHERE %";
	private static final String deleteByIdSql = "DELETE from usr where id = ? ";
	private static final String deleteRoleMembershipByIdSql = "DELETE from user_role where user_id = ? ";
	private static final String deleteTenantMembershipByIdSql = "DELETE from tenant_user where user_id = ? ";
	private static final String saveUserSql = "INSERT usr(" + 
	    "created_by_id, name, email, email_verified, first_name, last_name, image_url, password, provider, provider_id, salt" + 
		") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " + 
	    "ON DUPLICATE KEY UPDATE " + 
	    "last_modified_by_id= ?, name = ?, email = ?, email_verified = ?, first_name = ?, last_name = ?, image_url = ?, " + 
	    " password = ?, provider = ?, provider_id = ?, salt =?, " + 
	    "lock_expiration_time = ?, new_email=?,  new_password=?, attempts=?, version=version+1";
	private static final String createUserSql = "INSERT usr(" + 
		    "created_by_id, name, email, email_verified, first_name, last_name, image_url, password, provider, provider_id, salt" + 
			") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String findRolesByNameSql = "SELECT r.id, r.name FROM role r " +
			"LEFT JOIN user_role ur ON r.id = ur.role_id " +
			"LEFT JOIN usr u ON u.id = ur.user_id and u.name=:name";
	private static final String findTenantsByNameSql = 
		"SELECT t.id, t.name FROM tenant t " +
		"LEFT JOIN tenant_user ut ON t.id = ut.tenant_id " +
		"LEFT JOIN usr u ON u.id = ut.user_id and u.name=:name";
	
	private static final String selectIds = "SELECT id FROM % WHERE name in (%s)";  
		
	private static final String findByIdSql = String.format(selectAllColumn, "id=:id");
	private static final String findByNameSql = String.format(selectAllColumn, "name=:name");
	private static final String findByEmailSql = String.format(selectAllColumn, "email=:email");
	private static final String findRoleByIdSql = "SELECT r.name FROM role r LEFT JOIN role_user ru ON r.id = ru.user_id LEFT JOIN usr u ON ru.user_ud = u.id AND u.id=:id";
	private static final String findAllSql = "SELECT * FROM usr %s";
	private static final String findAllNamesSql = "SELECT name FROM usr %s";

	private static final String joinTenantSql = "insert into tenant_user(created_by_id, user_id, tenant_id) values(?, ?)";
	private static final String joinRoleSql = "insert into role_user(created_by_id, user_id, role_id) values(?, ?)";
	private static final String removeRoleSql = "delete from role_user where user_id= ? and role_id = ? ";
	
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Boolean existsByEmail(String email) {
		return this.findByEmail(email).isPresent();
	}

	@Override
	public Optional<User<Long>> findByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		User<Long> user = jdbcTemplate.queryForObject(
				findByNameSql, new UserRowMapper(), namedParameters);
        addRoles(user);
		return (user == null) ? Optional.empty() : Optional.of(user);
	}

	@Override
	public User<Long> create(User<Long> user) {
		Object[] params = { 
			WebUtils.currentUserId(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getFirstName(),
		    user.getLastName(), user.getImageUrl(), user.getPassword(), user.getProvider(), user.getProviderId(), user.getSalt()		    
		};
		
	    int[] types = {
	    	Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR,
	    	Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR
	    	
	    };
	    jdbcTemplate.update(createUserSql, params, types);
		
	    User<Long> newUser = this.findByName(user.getName()).get();
		user.setId(newUser.getId());
		
		if (ObjectUtils.isEmpty(user.getTenants())) {
			joinTenantsByName(newUser, user.getTenants());	
		}
		
		if (!ObjectUtils.isEmpty(user.getRoles())) {
			joinRolesByName(newUser, user.getRoles());	
		}
		return user;
	}
	

	
	@Override
	public User<Long> save(User<Long> user, Set<String> removedRoles, Set<String> newRoles) {
		this.saveUser(user);
		this.removeRolesByName(user, removedRoles);
		this.joinRolesByName(user, newRoles);
		return this.findByName(user.getName()).get();
	}
	
	@Override
	public Optional<User<Long>> findById(Long id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		User<Long> user = jdbcTemplate.queryForObject(
				findByIdSql, new UserRowMapper(), namedParameters);
		addRoles(user);
		return (user == null) ? Optional.empty() : Optional.of(user);
	}

	@Override
	public boolean existsById(Long id) {
		return this.findById(id).isPresent();
	}
	
	@Override
	public int deleteById(Long id) {
		
		Object[] params = { id };
	    int[] types = {Types.BIGINT};
	    jdbcTemplate.update(deleteRoleMembershipByIdSql, params, types);
	    jdbcTemplate.update(deleteTenantMembershipByIdSql, params, types);
	    return jdbcTemplate.update(deleteByIdSql, params, types);
		
	}

	@Override
	public int delete(User<Long> user) {
		return this.deleteById(user.getId());
	}

	@Override
	public Optional<User<Long>> findByEmail(String email) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", email);
		User<Long> user = jdbcTemplate.queryForObject(
				findByEmailSql, new UserRowMapper(), namedParameters);
		addRoles(user);
		return (user == null) ? Optional.empty() : Optional.of(user);
	}
	
	@Override
	public List<String> findAllNames(Pageable pageable) {
		return jdbcTemplate.query(
				String.format(findAllNamesSql, JdbcUtils.sqlFragment(pageable)), 
				new NameColumnRowMapper());
	}
	
	@Override
	public List<Role> findRolesByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		return jdbcTemplate.query(
				findRolesByNameSql, 
				new RoleRowMapper(),
				namedParameters);
	}
	
	@Override
	public List<Tenant> findTenantsByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		return jdbcTemplate.query(
				findTenantsByNameSql, 
				new TenantRowMapper(),
				namedParameters);
	}
	
	@Override
	public List<User<Long>> findAll(Pageable pageable) {		
		return jdbcTemplate.query(
				String.format(findAllSql, JdbcUtils.sqlFragment(pageable)), 
				new UserRowMapper());
	}
	
	public int[] joinTenants(User<Long> user, List<Tenant> tenants) {
		if (ObjectUtils.isEmpty(tenants)) {
			return new int[] {};
		}
		List<Object[]> batchArgs = new ArrayList<>();
		for (Tenant tenant: tenants) {
			Object[] params = { WebUtils.currentUserId(), user.getId(), tenant.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs, types);
	}
	
	public int[] joinRoles(User<Long> user, List<Role> roles) {
		if (ObjectUtils.isEmpty(roles)) {
			return new int[] {};
		}
		List<Object[]> batchArgs = new ArrayList<>();
		for (Role role: roles) {
			Object[] params = { WebUtils.currentUserId(), user.getId(), role.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(joinRoleSql, batchArgs, types);
	}
	
	public int[] joinTenantsByName(User<Long> user, Set<String> tenants) {
		if (ObjectUtils.isEmpty(tenants)) {
			return new int[] {};
		}
		List<String> tenantIds = getIds("tenant", tenants);
		List<Object[]> batchArgs = new ArrayList<>();
		for (String tenantId: tenantIds) {
			Object[] params = { WebUtils.currentUserId(), user.getId(), tenantId };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs, types);
	}

	public int[] joinRolesByName(User<Long> user, Set<String> roles) {
		if (ObjectUtils.isEmpty(roles)) {
			return new int[] {};
		}
		List<String> roleIds = getIds("role", roles);
		List<Object[]> batchArgs = new ArrayList<>();
		for (String roleId: roleIds) {
			Object[] params = { WebUtils.currentUserId(), user.getId(), roleId };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(joinRoleSql, batchArgs, types);
	}
	
	public int[] removeRolesByName(User<Long> user, Set<String> roles) {
		if (ObjectUtils.isEmpty(roles)) {
			return new int[] {};
		}
		List<String> roleIds = getIds("role", roles);
		List<Object[]> batchArgs = new ArrayList<>();
		for (String roleId: roleIds) {
			Object[] params = { user.getId(), roleId };
			batchArgs.add(params);
		}
	    int[] types = {Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(removeRoleSql, batchArgs, types);
	}
	private String candidateNames(Set<String> names) {
		StringBuilder candidateNames = new StringBuilder();
		int count = 0;
		for (String name: names) {
			if (count > 0) {
				candidateNames.append(", ");
			}
			candidateNames.append("'").append(name).append("'");
			count ++;
		}
		return candidateNames.toString();
	}
	
	protected List<String> getIds(String table, Set<String> names) {
		String sql = String.format(selectIds, table, candidateNames(names));
		return jdbcTemplate.query(
				sql, 
				new IdColumnRowMapper());
	}
	
	protected void addRoles(User<Long> user) {
		if (user != null) {
			SqlParameterSource idParameters = new MapSqlParameterSource().addValue("id", user.getId());
			user.setRoles(jdbcTemplate.query(
				findRoleByIdSql,
				new NameColumnRowMapper(), 
				idParameters).stream().collect(Collectors.toSet()));
		}
	}
	
	protected User<Long> saveUser(User<Long> user) {
		
		Object[] params = { 
			WebUtils.currentUserId(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getFirstName(),
		    user.getLastName(), user.getImageUrl(), user.getPassword(), user.getProvider(), user.getProviderId(), user.getSalt(),
		    WebUtils.currentUserId(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getFirstName(), user.getLastName(), user.getImageUrl(),
		    user.getPassword(), user.getProvider(), user.getProviderId(), user.getSalt(), user.getLockExpirationTime(), user.getNewEmail(), user.getNewPassword(), user.getAttempts()
		};
		
	    int[] types = {
	    	Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR,
	    	Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
	    	Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
	    	Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR, Types.INTEGER
	    	
	    };
	    jdbcTemplate.update(saveUserSql, params, types);
	    return this.findByName(user.getName()).get();
	}
}