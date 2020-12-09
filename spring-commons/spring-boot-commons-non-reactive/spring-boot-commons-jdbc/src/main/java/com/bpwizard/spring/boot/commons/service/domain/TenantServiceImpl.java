package com.bpwizard.spring.boot.commons.service.domain;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.bpwizard.spring.boot.commons.jdbc.JdbcUtils;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

@Service
public class TenantServiceImpl implements TenantService<Tenant, Long> {
	
	private static final String selectAllColumn = "SELECT id, name FROM tenant WHERE %";
	private static final String deleteByIdSql = "DELETE from tenant where id = ? ";
	private static final String deleteRoleMembershipByIdSql = "DELETE from tenant_role where role_id = ? ";
	private static final String deleteUserMembershipByIdSql = "DELETE from tenant_user where user_id = ? ";
	private static final String saveSql = "INSERT tenant(" + 
	    "created_by_id, name" + 
		") values(?, ?) " + 
	    "ON DUPLICATE KEY UPDATE " + 
	    "last_modified_by_id= ?, name = ?, version=version+1";
	
	private static final String addUserSql = "insert into tenant_user(created_by_id, user_id, tenant_id) values(?, ?)";
	private static final String addRoleSql = "insert into tenant_role(created_by_id, role_id, tenant_id) values(?, ?)";
	
	private static final String removeUserSql = "DELETE FROM tenant_user WHERE user_id = ? and role_id=?";
	private static final String removeRoleSql = "DELETE FROM tenant_role WHERE member_id = ? and role_id=?";
	
	private static final String findRolesByNameSql = "SELECT r.id, r.name FROM role r " +
			"LEFT JOIN tenant_role tr ON r.id = tr.role_id " +
			"LEFT JOIN tenant t ON t.id = tr.tenant_id and t.id=:id";
	private static final String findUsersByNameSql = "SELECT u.id, u.name FROM usr u " +
			"LEFT JOIN tenant_user tu ON u.id = tu.user_id " +
			"LEFT JOIN tenant t ON t.id = tu.tenant_id and t.id=:id";
		
	private static final String findByIdSql = String.format(selectAllColumn, "id=:id");
	private static final String findByNameSql = String.format(selectAllColumn, "name=:name");
	private static final String findAllSql = "SELECT id, name FROM tenant %s";
	private static final String findAllNamesSql = "SELECT name FROM tenant %s";

	@Autowired 
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Optional<Tenant> findByName(String name) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
		Tenant tenant = jdbcTemplate.queryForObject(
				findByNameSql, new TenantRowMapper(), namedParameters);

		return (tenant == null) ? Optional.empty() : Optional.of(tenant);
	}

	@Override
	public Tenant save(Tenant tenant) {
		Object[] params = { 
			WebUtils.currentUserId(), tenant.getName(),
		    WebUtils.currentUserId(), tenant.getName()
		};
		
	    int[] types = {
	    	Types.BIGINT, Types.VARCHAR,
	    	Types.BIGINT, Types.VARCHAR
	    	
	    };
	    jdbcTemplate.update(saveSql, params, types);
	    return this.findByName(tenant.getName()).get();
	}

	@Override
	public Optional<Tenant> findById(Long id) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		Tenant tenant = jdbcTemplate.queryForObject(
				findByIdSql, new TenantRowMapper(), namedParameters);

		return (tenant == null) ? Optional.empty() : Optional.of(tenant);
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
		jdbcTemplate.update(deleteUserMembershipByIdSql, params, types);
	    return jdbcTemplate.update(deleteByIdSql, params, types);		
	}

	@Override
	public int delete(Tenant tenant) {
        return this.deleteById(tenant.getId());
	}

	@Override
	public List<Tenant> findAll(Pageable pageable) {
		return jdbcTemplate.query(
				String.format(findAllSql, JdbcUtils.sqlFragment(pageable)), 
				new TenantRowMapper());
	}

	@Override
	public List<String> findAllTenantNames(Pageable pageable) {
		return jdbcTemplate.query(
				String.format(findAllNamesSql, JdbcUtils.sqlFragment(pageable)), 
				new NameColumnRowMapper());
	}

	@Override
	public int addUser(Tenant tenant, User<Long> user) {
		Object[] params = { WebUtils.currentUserId(), user.getId(), tenant.getId()};
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(addUserSql, params, types);
	}

	@Override
	public int[] addUsers(Tenant tenant, List<User<Long>> users) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (User<Long> user: users) {
			Object[] params = { WebUtils.currentUserId(), user.getId(), tenant.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(addUserSql, batchArgs, types);
	}

	@Override
	public int addRole(Tenant tenant, Role role) {
		Object[] params = {  WebUtils.currentUserId(), role.getId(), tenant.getId()};
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(addRoleSql, params, types);
	}

	@Override
	public int[] addRoles(Tenant tenant, List<Role> roles) {
		List<Object[]> batchArgs = new ArrayList<>();
		for (Role role: roles) {
			Object[] params = { WebUtils.currentUserId(), role.getId(), tenant.getId() };
			batchArgs.add(params);
		}
	
	    int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};

	    return jdbcTemplate.batchUpdate(addRoleSql, batchArgs, types);
	}

	@Override
	public int removeUser(Tenant tenant, User<Long> user) {
		Object[] params = { user.getId(), tenant.getId() };
	    int[] types = {Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(removeUserSql, params, types);
	}

	@Override
	public int removeRole(Tenant tenant, Role role) {
		Object[] params = { role.getId(), tenant.getId() };
	    int[] types = {Types.BIGINT, Types.BIGINT};
	    return jdbcTemplate.update(removeRoleSql, params, types);
	}

	@Override
	public List<User<Long>> findUsersByName(String tenant) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", tenant);
		return jdbcTemplate.query(
				findUsersByNameSql, 
				new UserRowMapper(),
				namedParameters);
	}

	@Override
	public List<Role> findRolesByName(String tenant) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", tenant);
		return jdbcTemplate.query(
				findRolesByNameSql, 
				new RoleRowMapper(),
				namedParameters);
	}

	

}

