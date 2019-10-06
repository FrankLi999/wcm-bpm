package com.example.demo.dao;

import com.example.demo.model.User;

import java.util.List;

public interface UserDao {

    User findByName(String name);

    List<User> findAll();

}