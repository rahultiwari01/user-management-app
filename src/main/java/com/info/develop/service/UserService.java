package com.info.develop.service;

import com.info.develop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setRole(rs.getString("role"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return user;
        }
    };
    
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, userRowMapper);
    }
    
    public Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        
        try {
            User user = jdbcTemplate.queryForObject(sql, params, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public User createUser(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, phone, role, enabled, created_at, updated_at) " +
                    "VALUES (:username, :password, :firstName, :lastName, :email, :phone, :role, :enabled, :createdAt, :updatedAt)";
        
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", passwordEncoder.encode(user.getPassword()))
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("phone", user.getPhone())
                .addValue("role", user.getRole() != null ? user.getRole() : "USER")
                .addValue("enabled", user.getEnabled())
                .addValue("createdAt", now)
                .addValue("updatedAt", now);
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        
        user.setId(keyHolder.getKey().longValue());
        return user;
    }
    
    public Optional<User> updateUser(Long id, User userDetails) {
        // Fetch the existing user from the database and apply updates if found.
        return getUserById(id).map(existingUser -> {
            // Merge non-null fields from the request into the existing user object.
            if (userDetails.getUsername() != null) {
                existingUser.setUsername(userDetails.getUsername());
            }
            if (userDetails.getFirstName() != null) {
                existingUser.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null) {
                existingUser.setLastName(userDetails.getLastName());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPhone() != null) {
                existingUser.setPhone(userDetails.getPhone());
            }
            if (userDetails.getRole() != null) {
                existingUser.setRole(userDetails.getRole());
            }
            // Note: For 'enabled' to be optional, the 'enabled' field in the User model
            // should be of type Boolean, not the primitive boolean.
            if (userDetails.getEnabled() != null) {
                existingUser.setEnabled(userDetails.getEnabled());
            }

            LocalDateTime now = LocalDateTime.now();
            existingUser.setUpdatedAt(now);

            String sql = "UPDATE users SET username = :username, first_name = :firstName, last_name = :lastName, " +
                         "email = :email, phone = :phone, role = :role, enabled = :enabled, updated_at = :updatedAt WHERE id = :id";

            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", existingUser.getId()).addValue("username", existingUser.getUsername()).addValue("firstName", existingUser.getFirstName()).addValue("lastName", existingUser.getLastName()).addValue("email", existingUser.getEmail()).addValue("phone", existingUser.getPhone()).addValue("role", existingUser.getRole()).addValue("enabled", existingUser.getEnabled()).addValue("updatedAt", now);

            jdbcTemplate.update(sql, params);
            return existingUser;
        });
    }
    
    public Optional<User> updateUserPassword(Long id, String newPassword) {
        // First check if user exists
        if (!getUserById(id).isPresent()) {
            return Optional.empty();
        }
        
        String sql = "UPDATE users SET password = :password, updated_at = :updatedAt WHERE id = :id";
        
        LocalDateTime now = LocalDateTime.now();
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("password", passwordEncoder.encode(newPassword))
                .addValue("updatedAt", now);
        
        int rowsAffected = jdbcTemplate.update(sql, params);
        
        if (rowsAffected > 0) {
            return getUserById(id);
        }
        
        return Optional.empty();
    }
    
    public boolean deleteUser(Long id) {
        String sql = "DELETE FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        
        int rowsAffected = jdbcTemplate.update(sql, params);
        return rowsAffected > 0;
    }
    
    public List<User> searchUsersByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email LIKE :email ORDER BY created_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("email", "%" + email + "%");
        return jdbcTemplate.query(sql, params, userRowMapper);
    }
    
    public List<User> searchUsersByName(String name) {
        String sql = "SELECT * FROM users WHERE first_name LIKE :name OR last_name LIKE :name ORDER BY created_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("name", "%" + name + "%");
        return jdbcTemplate.query(sql, params, userRowMapper);
    }
    
    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = :username";
        MapSqlParameterSource params = new MapSqlParameterSource("username", username);
        
        try {
            User user = jdbcTemplate.queryForObject(sql, params, userRowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}