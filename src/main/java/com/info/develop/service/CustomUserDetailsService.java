package com.info.develop.service;

import com.info.develop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user details for username: {}", username);
        Optional<User> userOptional = findByUsername(username);
        
        if (!userOptional.isPresent()) {
            log.warn("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        
        User user = userOptional.get();
        log.info("User found: {}. Building UserDetails object.", username);

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.getEnabled())
            .build();
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = :username";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("username", username);
        
        // Use query which returns a list to avoid EmptyResultDataAccessException
        java.util.List<User> users = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setFirstName(rs.getString("first_name"));
            u.setLastName(rs.getString("last_name"));
            u.setEmail(rs.getString("email"));
            u.setPhone(rs.getString("phone"));
            u.setRole(rs.getString("role"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            u.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return u;
        });

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}