package com.bpwizard.spring.boot.commons.service.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TenantRowMapper  implements RowMapper<Tenant> {
    @Override
    public  Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Tenant Tenant = new Tenant();
    	Tenant.setId(rs.getLong("id"));
    	Tenant.setName(rs.getString("name"));
        return Tenant;
    }
}