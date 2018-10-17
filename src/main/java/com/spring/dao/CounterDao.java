package com.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CounterDao {

	@Value("${app.global.first.value}")
	private int firstValue;

	@Autowired
	NamedParameterJdbcTemplate template;

	@Autowired
	JdbcTemplate jdbcTemplate;

	public int getNextVal() {
		return saveNextInt();
	}

	@PostConstruct
	public void init() {
		createTableIfNotExists();
		insertFirstRecord();
	}

	public void createTableIfNotExists() {
		String query = "CREATE TABLE IF NOT EXISTS TBL_NEXT_INT(ID INT(11) PRIMARY KEY AUTO_INCREMENT,SYSTIME_MS BIGINT)";
		jdbcTemplate.execute(query);
	}

	public void insertFirstRecord() {
		final String query = "INSERT INTO TBL_NEXT_INT(ID,SYSTIME_MS) VALUES(:ID,:MS)";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("ID", firstValue);
		source.addValue("MS", System.currentTimeMillis());
		try {
			template.update(query, source);
		} catch (DuplicateKeyException e) {
			
		}
	}

	public int saveNextInt() {
		KeyHolder kh = new GeneratedKeyHolder();
		final String query = "INSERT INTO TBL_NEXT_INT(SYSTIME_MS) VALUES(:MS)";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("MS", System.currentTimeMillis());
		template.update(query, source, kh);
		return kh.getKey().intValue();
	}
}
