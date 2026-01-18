package com.example.playmon_server.player;

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
public class PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    public Player save(Player player) {
        if(player.getId() != null) {
            update(player);
            return player;
        }
        String sql = "INSERT INTO players (puuid, game_name, tag_line, revision_date, profile_icon_id, summoner_level) VALUES (?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, player.getPuuid());
            ps.setString(2, player.getGameName());
            ps.setString(3, player.getTagLine());
            ps.setObject(4, player.getRevisionDate());
            ps.setInt(5, player.getProfileIconId());
            ps.setLong(6, player.getSummonerLevel());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            player.setId(keyHolder.getKey().longValue());
        }

        return player;
    }

    public void update(Player player) {
        String sql = "UPDATE players SET game_name = ?, tag_line = ?, last_played_game_id = ?, revision_date = ?, profile_icon_id = ?, summoner_level = ? WHERE puuid = ?";

        jdbcTemplate.update(sql,
                player.getGameName(),
                player.getTagLine(),
                player.getLastPlayedGameId(),
                player.getRevisionDate(),
                player.getProfileIconId(),
                player.getSummonerLevel(),
                player.getPuuid()
        );
    }

    public Optional<Player> findByGameNameTagLine(String gameName, String tagLine) {
        String sql = "SELECT * FROM players WHERE game_name = ? AND tag_line = ?";
        try {
            Player player = jdbcTemplate.queryForObject(sql, playerRowMapper(), gameName, tagLine);
            return Optional.ofNullable(player);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Player> findByPuuid(String puuid) {
        String sql = "SELECT * FROM players WHERE puuid = ?";
        try {
            Player player = jdbcTemplate.queryForObject(sql, playerRowMapper(), puuid);
            return Optional.ofNullable(player);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public RowMapper<Player> playerRowMapper() {
        return (rs, rowNum) -> Player.builder()
                .id(rs.getLong("id"))
                .puuid(rs.getString("puuid"))
                .gameName(rs.getString("game_name"))
                .tagLine(rs.getString("tag_line"))
                .lastPlayedGameId(rs.getString("last_played_game_id"))
                .revisionDate(rs.getLong("revision_date"))
                .profileIconId(rs.getInt("profile_icon_id"))
                .summonerLevel(rs.getInt("summoner_level"))
                .build();

    }
}