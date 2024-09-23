package com.example.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.crud.entity.UserData;
import java.util.List;

@RestController
public class UserController {
    
    @Autowired
    JdbcTemplate jdbc;

    @PostMapping(value="/crud")
    public UserData saveUser(@RequestBody UserData entity) {
        String query = "INSERT INTO test(username) VALUES ('" + entity.getUserName() + "')";
        jdbc.execute(query);
        
        return entity;
    }

    @GetMapping(value="/crud")
    public List<UserData> getUserData() {
        return jdbc.query("Select * from test", new BeanPropertyRowMapper<>(UserData.class));
    }

    @GetMapping(value = "/crud/{id}")
    public UserData getUserDataById(@PathVariable("id") Long id)
    {
        return jdbc.queryForObject("Select * from test where id=?", (rs, rowNum) -> {
            UserData userData = new UserData();
            // Set values from ResultSet to UserData object
            userData.setId(rs.getLong("id"));
            userData.setUserName(rs.getString("username"));
            // ... set other properties
            return userData;
        }, id);
    }

    @PutMapping(value = "/crud")
    public boolean updateUserData(@RequestBody UserData userData) 
    {
        String query = "Update test set username = ? where id = ?";
        return jdbc.update(query, userData.getUserName(), userData.getId()) > 0;
    }

    @DeleteMapping(value = "/crud/{id}")
    public boolean deleteUserData(@PathVariable("id") Long id)
    {
        return jdbc.update("Delete from test where id=?", id) > 0;
    }
}
