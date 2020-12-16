package com.bpwizard.spring.boot.commons.service.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;
import com.bpwizard.spring.boot.commons.service.secureity.oauth2.AuthProvider;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

@Service
public class UserServiceImpl implements UserService<User<Long>, Long> {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final String selectAllColumn = "SELECT id, name, email, password, first_name, last_name, image_url, provider, provider_id, version FROM usr WHERE %s";
	private static final String deleteByIdSql = "DELETE from usr where id = :user_id ";
	private static final String deleteRoleMembershipByIdSql = "DELETE from user_role where user_id = :user_id ";
	private static final String deleteTenantMembershipByIdSql = "DELETE from tenant_user where user_id = :user_id ";
	private static final String saveUserSql = "INSERT usr(" + 
	    "created_by_id, name, email, email_verified, first_name, last_name, image_url, password, provider, provider_id, salt" + 
		") values(:created_by_id, :name, :email, :email_verified, :first_name, :last_name, :image_url, :password, :provider, :provider_id, :salt) " + 
	    "ON DUPLICATE KEY UPDATE " + 
	    "last_modified_by_id= last_modified_by_id:, name = :name, email = :email, email_verified = :email_verified, first_name = :first_name, last_name = :last_name, image_url = :image_url, " + 
	    " password = :password, provider = :provider, provider_id = :provider_id, salt =:salt, " + 
	    "lock_expiration_time = :lock_expiration_time, new_email=:new_email,  new_password=:new_email, attempts=:attempts, version=version+1";
	private static final String createUserSql = "INSERT usr(" + 
		    "created_by_id, name, email, email_verified, first_name, last_name, image_url, password, provider, provider_id, salt, attempts" + 
			") values(:created_by_id, :name, :email, :email_verified, :first_name, :last_name, :image_url, :password, :provider, :provider_id, :salt, 0)";
	private static final String findRolesByNameSql = "SELECT r.id, r.name FROM role r " +
			"LEFT JOIN user_role ur ON r.id = ur.role_id " +
			"LEFT JOIN usr u ON u.id = ur.user_id and u.name=:name";
	private static final String findTenantsByNameSql = 
		"SELECT t.id, t.name FROM tenant t " +
		"LEFT JOIN tenant_user ut ON t.id = ut.tenant_id " +
		"LEFT JOIN usr u ON u.id = ut.user_id and u.name=:name";
	
	private static final String selectIds = "SELECT id FROM %s WHERE name in (%s)";  
		
	private static final String findByIdSql = String.format(selectAllColumn, "id=:id");
	private static final String findByNameSql = String.format(selectAllColumn, "name=:name");
	private static final String findByEmailSql = String.format(selectAllColumn, "email=:email");
	private static final String findRoleByIdSql = "SELECT r.name FROM role r LEFT JOIN role_user ru ON r.id = ru.role_id LEFT JOIN usr u ON u.id=:id AND u.id = ru.user_id";
	private static final String findAllSql = "SELECT * FROM usr %s";
	private static final String findAllNamesSql = "SELECT name FROM usr %s";

	private static final String joinTenantSql = "insert into tenant_user(created_by_id, user_id, tenant_id) values(:created_by_id, :user_id, :tenant_id)";
	private static final String joinRoleSql = "insert into role_user(created_by_id, user_id, role_id) values(:created_by_id, :user_id, :role_id)";
	private static final String removeRoleSql = "delete from role_user where user_id= :user_id and role_id = :role_id ";
	
	@Autowired 
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public Boolean existsByEmail(String email) {
		return this.findByEmail(email).isPresent();
	}

	@Override
	public Optional<User<Long>> findByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		
		User<Long> user = null; 
		try {
			user = jdbcTemplate.queryForObject(
					findByNameSql, namedParameters, new UserRowMapper());
	        addRoles(user);
		} catch (EmptyResultDataAccessException e) {
			logger.info("Not able to find user " + name, e);
		}
		return (user == null) ? Optional.empty() : Optional.of(user);
	}

	@Override
	public User<Long> create(User<Long> user) {
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("created_by_id", WebUtils.currentUserId())
				.addValue("name", user.getName())
				.addValue("email", user.getEmail())
				.addValue("email_verified", user.getEmailVerified())
				.addValue("first_name", user.getFirstName())
				.addValue("last_name", user.getLastName())
				.addValue("image_url", user.getImageUrl())
				.addValue("password", user.getPassword())
				.addValue("provider", (user.getProvider() != null) ? user.getProvider().name() : AuthProvider.local.name())
				.addValue("provider_id", user.getProviderId())
				.addValue("salt", user.getSalt());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbcTemplate.update(createUserSql, paramSource, keyHolder);
		
	    user.setId(keyHolder.getKeyAs(BigInteger.class).longValue());
		
		if (!ObjectUtils.isEmpty(user.getTenants())) {
			joinTenantsByName(user, user.getTenants());	
		}
		
		if (!ObjectUtils.isEmpty(user.getRoles())) {
			joinRolesByName(user, user.getRoles());	
		}
		return user;
	}
	

	
	@Override
	public void save(User<Long> user, Set<String> removedRoles, Set<String> newRoles) {
		this.saveUser(user);
		this.removeRolesByName(user, removedRoles);
		this.joinRolesByName(user, newRoles);
	}
	
	@Override
	public Optional<User<Long>> findById(Long id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		User<Long> user = null;
		try {
			user = jdbcTemplate.queryForObject(
				findByIdSql, namedParameters, new UserRowMapper());
			addRoles(user);
		} catch (EmptyResultDataAccessException e) {
			logger.info("Not able to find user " + id, e);
		}

		return (user == null) ? Optional.empty() : Optional.of(user);
	}

	@Override
	public boolean existsById(Long id) {
		return this.findById(id).isPresent();
	}
	
	@Override
	public int deleteById(Long id) {
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("user_id", id);
	    jdbcTemplate.update(deleteRoleMembershipByIdSql, paramSource); //params, types);
	    jdbcTemplate.update(deleteTenantMembershipByIdSql, paramSource); //params, types);
	    return jdbcTemplate.update(deleteByIdSql, paramSource); //params, types);
		
	}

	@Override
	public int delete(User<Long> user) {
		return this.deleteById(user.getId());
	}

	@Override
	public Optional<User<Long>> findByEmail(String email) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", email);
		User<Long> user = null;
		try {
			user = jdbcTemplate.queryForObject(
				findByEmailSql, namedParameters, new UserRowMapper());
			addRoles(user);
		} catch (EmptyResultDataAccessException e) {
			logger.info("Not able to find user " + email, e);
		}
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
				namedParameters,
				new RoleRowMapper());
	}
	
	@Override
	public List<Tenant> findTenantsByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		return jdbcTemplate.query(
				findTenantsByNameSql, 
				namedParameters,
				new TenantRowMapper());
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
		List<SqlParameterSource> batchArgs = new ArrayList<>();
		for (Tenant tenant: tenants) {
			SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("created_by_id", WebUtils.currentUserId())
					.addValue("user_id", user.getId())
					.addValue("tenant_id", tenant.getId());
			batchArgs.add(namedParameters);
		}


	    return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()])); //batchArgs, types);
	}
	
	public int[] joinRoles(User<Long> user, List<Role> roles) {
		if (ObjectUtils.isEmpty(roles)) {
			return new int[] {};
		}
		List<SqlParameterSource> batchArgs = new ArrayList<>();
		for (Role role: roles) {
			SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("created_by_id", WebUtils.currentUserId())
					.addValue("user_id", user.getId())
					.addValue("role_id", role.getId());
			batchArgs.add(namedParameters);
		}

	    return jdbcTemplate.batchUpdate(joinRoleSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()])); //batchArgs, types);
	}
	
	public int[] joinTenantsByName(User<Long> user, Set<String> tenants) {
		if (ObjectUtils.isEmpty(tenants)) {
			return new int[] {};
		}
		List<String> tenantIds = getIds("tenant", tenants);
		List<SqlParameterSource> batchArgs = new ArrayList<>();
		for (String tenantId: tenantIds) {
			SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("created_by_id", WebUtils.currentUserId())
					.addValue("user_id", user.getId())
					.addValue("tenant_id", tenantId);
			batchArgs.add(namedParameters);
		}

	    return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()])); //batchArgs, types);
	}

	public int[] joinRolesByName(User<Long> user, Set<String> roles) {
		if (ObjectUtils.isEmpty(roles)) {
			return new int[] {};
		}
		List<String> roleIds = getIds("role", roles);
		List<SqlParameterSource> batchArgs = new ArrayList<>();
		for (String roleId: roleIds) {
			SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("created_by_id", WebUtils.currentUserId())
					.addValue("user_id", user.getId())
					.addValue("role_id", roleId);
			batchArgs.add(namedParameters);
		}
	
	    return jdbcTemplate.batchUpdate(joinRoleSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()])); //batchArgs, types);
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

	    return jdbcTemplate.batchUpdate(removeRoleSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()])); //batchArgs, types);
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
				idParameters,
				new NameColumnRowMapper()).stream().collect(Collectors.toSet()));
		}
	}
	
	protected User<Long> saveUser(User<Long> user) {
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("created_by_id", WebUtils.currentUserId())
				.addValue("name", user.getName())
				.addValue("email", user.getEmail())
				.addValue("email_verified", user.getEmailVerified())
				.addValue("first_name", user.getFirstName())
				.addValue("last_name", user.getLastName())
				.addValue("image_url", user.getImageUrl())
				.addValue("password", user.getPassword())
				.addValue("provider", user.getProvider())
				.addValue("provider_id", user.getProviderId())
				.addValue("salt", user.getSalt())
		        .addValue("last_modified_by_id", WebUtils.currentUserId())
		        .addValue("lock_expiration_time",user.getLockExpirationTime())
		        .addValue("new_email", user.getNewEmail())
		        .addValue("new_password", user.getNewPassword())
		        .addValue("attempts", user.getAttempts());
		
	    jdbcTemplate.update(saveUserSql, paramSource); //params, types);
	    return this.findByName(user.getName()).get();
	}
}