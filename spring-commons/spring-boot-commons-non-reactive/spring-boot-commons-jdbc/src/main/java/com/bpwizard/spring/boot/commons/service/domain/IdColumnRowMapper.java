package com.bpwizard.spring.boot.commons.service.domain;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class IdColumnRowMapper implements RowMapper<String> {
    @Override
    public  String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("id");
    }
}