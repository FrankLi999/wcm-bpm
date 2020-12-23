package com.bpwizard.spring.boot.commons.service.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bpwizard.spring.boot.commons.service.secureity.oauth2.AuthProvider;

public class UserRowMapper implements RowMapper<User<Long>> {
    @Override
    public  User<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
    	User<Long> user = new User<Long>();
    	user.setId(rs.getLong("id"));
    	user.setName(rs.getString("name"));
    	user.setEmail(rs.getString("email"));
    	user.setPassword(rs.getString("password"));
    	user.setFirstName(rs.getString("first_name"));
    	user.setLastName(rs.getString("last_name"));
    	user.setImageUrl(rs.getString("image_url"));
    	user.setProvider(AuthProvider.valueOf(rs.getString("provider")));
    	user.setProviderId(rs.getString("provider_id"));
    	user.setVersion(rs.getLong("version"));
    	user.setCredentialsUpdatedMillis(rs.getLong("credentials_updated_millis"));
        return user;
    }
}
