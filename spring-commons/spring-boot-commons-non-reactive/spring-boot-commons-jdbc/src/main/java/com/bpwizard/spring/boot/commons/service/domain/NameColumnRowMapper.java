package com.bpwizard.spring.boot.commons.service.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NameColumnRowMapper implements RowMapper<String> {
    @Override
    public  String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("name");
    }
}