package com.example.playmon_server.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Subscription> findByUserPlayer(long userId, long playerId) {
        String sql = "SELECT * FROM subscriptions WHERE user_id = ? AND player_id = ?";
        try {
            Subscription subscription = jdbcTemplate.queryForObject(sql, subscriptionRowMapper(), userId, playerId);
            return Optional.ofNullable(subscription);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public RowMapper<Subscription> subscriptionRowMapper() {
        return (rs, rowNum) -> Subscription.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .playerId(rs.getLong("player_id"))
                .build();
    }

    public boolean exists(long userId, long playerId) {
        String sql = "SELECT count(*) FROM subscriptions WHERE user_id = ? AND player_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, playerId);
        return count != null && count > 0;
    }

    public Subscription save(long userId, long playerId) {
        String sql = "INSERT INTO subscriptions (user_id, player_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setLong(2, playerId);
            return ps;
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();

        return Subscription.builder()
                .id(generatedId)
                .userId(userId)
                .playerId(playerId)
                .build();
    }
}