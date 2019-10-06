package com.bpwizard.wcm.repo.hazelcast.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bpwizard.wcm.repo.hazelcast.demo.model.HazelcastUser;


@Repository
public class HazelcastUserDaoImpl implements HazelcastUserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public HazelcastUser findByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        String sql = "SELECT * FROM hzelcast_jet_users WHERE name=:name";

        return namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
    }

    @Override
    public List<HazelcastUser> findAll() {
        String sql = "SELECT * FROM hzelcast_jet_users";

        return namedParameterJdbcTemplate.query(sql, new HashMap<>(), new UserMapper());
    }

    private static final class UserMapper implements RowMapper<HazelcastUser> {

        public HazelcastUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new HazelcastUser()
                    .setId(rs.getInt("id"))
                    .setName(rs.getString("name"))
                    .setEmail(rs.getString("email"));
        }
    }

}