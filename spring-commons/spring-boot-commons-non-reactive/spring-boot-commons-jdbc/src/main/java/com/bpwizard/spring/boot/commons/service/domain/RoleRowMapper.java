package com.bpwizard.spring.boot.commons.service.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public  Role mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Role role = new Role();
    	role.setId(rs.getLong("id"));
    	role.setName(rs.getString("name"));
        return role;
    }
}