package com.bpwizard.spring.boot.commons.service.domain;


import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
public class RoleServiceImpl implements RoleService<Role, Long> {
	private static final String selectAllColumn = "SELECT id, name FROM role WHERE %";
	private static final String deleteByIdSql = "DELETE from role where id = ? ";
	private static final String deleteRoleMembershipByIdSql = "DELETE from role_role where user_id = ? ";
	private static final String deleteTenantMembershipByIdSql = "DELETE from tenant_role where user_id = ? ";
	private static final String saveSql = "INSERT role(" + 
	    "created_by_id, name" + 
		") values(?, ?) " + 
	    "ON DUPLICATE KEY UPDATE " + 
	    "last_modified_by_id=?, name = ?, version=version+1";
	
	private static final String addUserSql = "insert into role_user(created_by_id, user_id, role_id) values(?, ?, ?)";
	private static final String addRoleSql = "insert into role_role(created_by_id, role_id, member_id) values(?, ?, ?)";
	private static final String joinTenantSql = "insert into tenant_role(created_by_id, role_id, tenant_id) values(?, ?, ?)";
	private static final String removeUserSql = "DELETE FROM role_user WHERE user_id = ? and role_id=?";
	private static final String removeRoleSql = "DELETE FROM role_role WHERE member_id = ? and role_id=?";
	
	private static final String findRolesByNameSql = "SELECT mr.id, mr.name FROM role mr " +
			"LEFT JOIN role_role rr ON mr.id = rr.member_id " +
			"LEFT JOIN role r ON r.id = rr.role_id and r.id=:id";
	private static final String findUsersByNameSql = "SELECT u.id, u.name FROM user u " +
			"LEFT JOIN role_user ur ON u.id = ur.user_id " +
			"LEFT JOIN role r ON r.id = ur.role_id and r.id=:id";
		
	private static final String findByIdSql = String.format(selectAllColumn, "id=:id");
	private static final String findByNameSql = String.format(selectAllColumn, "name=:name");
	private static final String findAllSql = "SELECT id, name FROM role %s";
	private static final String findAllNamesSql = "SELECT name FROM role %s";
	
	private static final String selectIds = "SELECT id FROM % WHERE name in (%s)"; 
	
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public Optional<Role> findByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		Role role = jdbcTemplate.queryForObject(
				findByNameSql, new RoleRowMapper(), namedParameters);

		return (role == null) ? Optional.empty() : Optional.of(role);
	}

	@Override
	public Role create(Role role) {
		Object[] params = { 
			WebUtils.currentUserId(), role.getName(),
		    WebUtils.currentUserId(), role.getName()
		};
		
	    int[] types = {
	    	Types.BIGINT, Types.VARCHAR,
	    	Types.BIGINT, Types.VARCHAR
	    	
	    };
	    jdbcTemplate.update(saveSql, params, types);
	    Role newRole = this.findByName(role.getName()).get();
	    role.setId(newRole.getId());
	    if (ObjectUtils.isEmpty(role.getTenants())) {
	    	joinTenantsByName(role, role.getTenants());
		}
	    return role;
	}
	@Override
	public Role save(Role role) {
		Object[] params = { 
			WebUtils.currentUserId(), role.getName(),
		    WebUtils.currentUserId(), role.getName()
		};
		
	    int[] types = {
	    	Types.BIGINT, Types.VARCHAR,
	    	Types.BIGINT, Types.VARCHAR
	    	
	    };
	    jdbcTemplate.update(saveSql, params, types);
	    return this.findByName(role.getName()).get();
	}

	@Override
	public Optional<Role> findById(Long id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		Role role = jdbcTemplate.queryForObject(
				findByIdSql, new RoleRowMapper(), namedParameters);

		return (role == null) ? Optional.empty() : Optional.of(role);
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
	public int delete(Role role) {
		return this.deleteById(role.getId());
	}
	
	@Override
	public List<String> findAllRoleNames(Pageable pageable) {
		return jdbcTemplate.query(
				String.format(findAllNamesSql, JdbcUtils.sqlFragment(pageable)), 
				new NameColumnRowMapper());
	}

	@Override
	public List<Role> findAll(Pageable pageable) {
		return jdbcTemplate.query(
				String.format(findAllSql, JdbcUtils.sqlFragment(pageable)), 
				new RoleRowMapper());
	}
	
	public int addUser(Role role, User<Long> user) {
		Object[] params = { WebUtils.currentUserId(), user.getId() , role.getId()};
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(addUserSql, params, types);
	}
	
	public int[] addUsers(Role role, List<User<Long>> users) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (User<Long> user: users) {
			Object[] params = {WebUtils.currentUserId(),  user.getId() , role.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(addUserSql, batchArgs, types);
	}
	
	public int removeUser(Role role, User<Long> user) {
		Object[] params = { user.getId(), role.getId() };
	    int[] types = {Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(removeUserSql, params, types);		
	}
	
	public int addRole(Role role, Role member) {
		Object[] params = { WebUtils.currentUserId(), member.getId(), role.getId() };
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(addRoleSql, params, types);
	}
	
	public int[] addRoles(Role role, List<Role> members) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (Role member: members) {
			Object[] params = { WebUtils.currentUserId(), member.getId(), role.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(addRoleSql, batchArgs, types);
	}
	
	public int[] joinTenants(Role role, List<Tenant> tenants) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (Tenant tenant: tenants) {
			Object[] params = { WebUtils.currentUserId(), role.getId(), tenant.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs, types);
	}

	public int[] joinTenantsByName(Role role, Set<String> tenants) {
		List<String> tenantIds = getIds("tenant", tenants);
		List<Object[]> batchArgs = new ArrayList<>();
		for (String tenantId: tenantIds) {
			Object[] params = { WebUtils.currentUserId(), role.getId(), tenantId };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs, types);
	}
	
	public int removeRole(Role role, Role member) {
		Object[] params = { member.getId(), role.getId() };
	    int[] types = {Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(removeRoleSql, params, types);
	}
	
	public List<User<Long>> findUsersByName(String role) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", role);
		return jdbcTemplate.query(
				findUsersByNameSql, 
				new UserRowMapper(),
				namedParameters);
	}
	
	public List<Role> findRolesByName(String role) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", role);
		return jdbcTemplate.query(
				findRolesByNameSql, 
				new RoleRowMapper(),
				namedParameters);
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
}

