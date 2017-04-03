package com.cg.digi.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DesktopBrowserVersionRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet resultset, int arg1) throws SQLException {
		DesktopBrowserVersionResultExtractor extractor=new DesktopBrowserVersionResultExtractor();
		Object params=extractor.extractData(resultset);
		return params;
	}
}
