package com.cg.digi.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class browserMarketshareRowMapper implements RowMapper{

	@Override
	public Object mapRow(ResultSet resultSet, int arg1) throws SQLException {
		browserMarketshareResultExtractor extractor = new browserMarketshareResultExtractor();
		Object params = extractor.extractData(resultSet);
		return params;
	}

}
