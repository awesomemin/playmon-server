package com.example.playmon_server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public User save(User user) {
        if(user.getId() != null) {
            update(user);
            return user;
        }

        String sql = "INSERT INTO users (email, name, provider, provider_id, role) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getName());
            ps.setString(3, user.getProvider());
            ps.setString(4, user.getProviderId());
            ps.setString(5, user.getRole().name());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getId());
    }

    public RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .provider(rs.getString("provider"))
                .providerId(rs.getString("provider_id"))
                .role(Role.valueOf(rs.getString("role")))
                .build();
    }
}